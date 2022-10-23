/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.buildtools.listener.SignEditor can not be copied and/or distributed without the express
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

package net.crytec.addons.buildtools.listener;

import com.destroystokyo.paper.MaterialTags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.crytec.util.F;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignEditor implements Listener {

  private final boolean useWorldGuard;
  private final RegionContainer container;

  public SignEditor(final boolean useWorldGuard) {
    this.useWorldGuard = useWorldGuard;
		container = WorldGuard.getInstance().getPlatform().getRegionContainer();
  }

  @EventHandler
  public void onSignChange(final SignChangeEvent e) {
    if (!e.getPlayer().hasPermission("ct.sign.color")) {
      return;
    }
    for (int i = 0; i < 4; i++) {
      e.setLine(i, ChatColor.translateAlternateColorCodes('&', e.getLine(i)));
    }
  }

  private boolean canModifyPainting(final Player player, final Block block) {
    if (player.hasPermission("worldguard.region.bypass." + block.getWorld().getName())) {
      return true;
    }

    final LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
    return container.createQuery().testState(BukkitAdapter.adapt(block.getLocation()), lp, Flags.BLOCK_PLACE);
  }

  @EventHandler(ignoreCancelled = true)
  public void openSignGUI(final PlayerInteractEvent e) {
    if (!e.getPlayer().isSneaking()) {
      return;
    }
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK || !MaterialTags.SIGNS.isTagged(e.getClickedBlock())) {
      return;
    }

    if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
      e.getPlayer().sendMessage(F.error("Um ein Schild zu bearbeiten darfst du kein Item in der Hand halten."));
      return;
    }

    if (useWorldGuard) {
      if (!canModifyPainting(e.getPlayer(), e.getClickedBlock())) {
        e.getPlayer().sendMessage(F.error("Du hast in dieser Region keine Berechtigung."));
        return;
      }
    }

    final Sign sign = (Sign) e.getClickedBlock().getState();
    e.getPlayer().openSign(sign);
  }
}