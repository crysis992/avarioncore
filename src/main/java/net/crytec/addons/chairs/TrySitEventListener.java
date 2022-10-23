/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.chairs.TrySitEventListener can not be copied and/or distributed without the express
 *  permission of crysis992
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of AvarionCraft.de and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AvarionCraft.de
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AvarionCraft.de.
 *
 */

package net.crytec.addons.chairs;

import net.crytec.util.F;
import net.crytec.util.PermissionRegistrar;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class TrySitEventListener implements Listener {

  public Chairs plugin;
  private final Permission sitPermission = PermissionRegistrar.addPermission("ct.sit", "Erlaubt das sitzen auf Stufen", PermissionDefault.OP);
  private final static int maxDistance = 2;
  private final static int maxChairWidth = 3;

  public TrySitEventListener(final Chairs plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerInteract(final PlayerInteractEvent event) {
    if (event.getHand() != EquipmentSlot.HAND && event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      final Player player = event.getPlayer();
      final Block block = event.getClickedBlock();
      if (sitAllowed(player, block)) {
        final Location sitLocation = getSitLocation(block, player.getLocation().getYaw());
        if (plugin.getPlayerSitData().sitPlayer(player, block, sitLocation)) {
          event.setCancelled(true);
        }
      }
    }
  }

  private boolean sitAllowed(final Player player, final Block block) {
    if (isSitting(player) || !player.hasPermission(sitPermission) || player.isSneaking()) {
      return false;
    }

    // Check for item in hand
    if (player.getInventory().getItemInMainHand().getType() != Material.AIR && player.getInventory().getItemInOffHand().getType() != Material.AIR) {
      return false;
    }

    // Sit occupied check
    if (plugin.getPlayerSitData().isBlockOccupied(block)) {
      player.sendMessage(F.main("Chairs", "Dieser Sitz ist schon von " + F.name(plugin.getPlayerSitData().getPlayerOnChair(block).getName()) + " belegt."));
      return false;
    }

//		Step step = null;
//		WoodenStep wStep = null;

    // Check for block is chair
    if (isValidChair(block) && block.getBlockData() instanceof Stairs) {

      final Stairs stairs = (Stairs) block.getBlockData();

      int chairwidth = 1;

      // Check if block beneath chair is solid.
      if (block.getRelative(BlockFace.DOWN).isLiquid()) {
        return false;
      }
      if (block.getRelative(BlockFace.DOWN).isEmpty()) {
        return false;
      }
      if (!block.getRelative(BlockFace.DOWN).getType().isSolid()) {
        return false;
      }

      if (!block.getRelative(BlockFace.UP).isEmpty()) {
        return false;
      }

      // Check for distance distance between player and chair.
      if (player.getLocation().distance(block.getLocation().add(0.5, 0, 0.5)) > maxDistance) {
        return false;
      }

      // Check if block is inverted
      if (stairs.isWaterlogged() || stairs.getHalf() == Half.TOP) {
        return false;
      }

      // Check for maximal chair width (only for stairs)
      if (maxChairWidth > 0 && stairs != null) {
        if (stairs.getFacing() == BlockFace.NORTH || stairs.getFacing() == BlockFace.SOUTH) {
          chairwidth += getChairWidth(block, BlockFace.EAST);
          chairwidth += getChairWidth(block, BlockFace.WEST);
        } else if (stairs.getFacing() == BlockFace.EAST || stairs.getFacing() == BlockFace.WEST) {
          chairwidth += getChairWidth(block, BlockFace.NORTH);
          chairwidth += getChairWidth(block, BlockFace.SOUTH);
        }

        return chairwidth <= maxChairWidth;
      }

      return true;
    }

    return false;
  }

  private Location getSitLocation(final Block block, final Float playerYaw) {
    final double sh = 0.7;

    Stairs stairs = null;
    if (block.getState().getData() instanceof Stairs) {
      stairs = (Stairs) block.getState().getData();
    }

    final Location plocation = block.getLocation();
    plocation.add(0.5D, (sh - 0.5D), 0.5D);

    // Rotate the player's view to the descending side of the block.
    if (plugin.autoRotate && stairs != null) {
      switch (stairs.getFacing()) {
        case NORTH: {
          plocation.setYaw(180);
          break;
        }
        case EAST: {
          plocation.setYaw(-90);
          break;
        }
        case SOUTH: {
          plocation.setYaw(0);
          break;
        }
        case WEST: {
          plocation.setYaw(90);
          break;
        }
        default: {
        }
      }
    } else {
      plocation.setYaw(playerYaw);
    }
    return plocation;
  }


  private boolean isValidChair(final Block block) {
    return plugin.SIT_BLOCKS.isTagged(block);
  }

  private boolean isSitting(final Player player) {
    return plugin.getPlayerSitData().isSitting(player);
  }

  private int getChairWidth(final Block block, final BlockFace face) {
    int width = 0;
    // Go through the blocks next to the clicked block and check if there are any further stairs.
    for (int i = 1; i <= maxChairWidth; i++) {
      final Block relative = block.getRelative(face, i);
      if (relative.getBlockData() instanceof Stairs) {
        if (isValidChair(relative) && ((Stairs) relative.getBlockData()).getFacing() == ((Stairs) block.getBlockData()).getFacing()) {
          width++;
        } else {
          break;
        }
      }
    }

    return width;
  }
}