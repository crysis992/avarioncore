/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.chestlock.ChestLockAddon can not be copied and/or distributed without the express
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

package net.crytec.addons.chestlock;

import com.destroystokyo.paper.MaterialSetTag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.crytec.addons.Addon;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import net.crytec.util.PermissionRegistrar;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataType;

public class ChestLockAddon extends Addon implements Listener {

  private MaterialSetTag chests;

  private NamespacedKey lockID;
  private final Set<BlockFace> facings = Sets.newHashSetWithExpectedSize(4);
  private final Permission bypassPerm = PermissionRegistrar.addPermission("ct.lock.bypass", "Bypass any locked chest.", PermissionDefault.OP);
  private final ObjectMapper mapper = new ObjectMapper();
  private final HashMap<UUID, Cache<Long, Lock>> cache = Maps.newHashMap();

  @Override
  protected void onEnable() {
    lockID = new NamespacedKey(getPlugin(), "jsonLock");

    chests = new MaterialSetTag(new NamespacedKey(getPlugin(), "protectedChests"),
        Material.CHEST,
        Material.TRAPPED_CHEST,
        Material.FURNACE,
        Material.BLAST_FURNACE,
        Material.SMOKER,
        Material.BARREL
    );

    facings.add(BlockFace.EAST);
    facings.add(BlockFace.NORTH);
    facings.add(BlockFace.SOUTH);
    facings.add(BlockFace.WEST);

    getPlugin().getCommandManager().registerCommand(new ChestLockCommand(this));
    getPlugin().getCommandManager().registerCommand(new ChestUnlockCommand(this));

    Bukkit.getPluginManager().registerEvents(this, getPlugin());

    //Fill Cache Map with all world IDs
    for (final World world : Bukkit.getWorlds()) {
      cache.put(world.getUID(), CacheBuilder.newBuilder().maximumSize(300).expireAfterAccess(10, TimeUnit.MINUTES).build());
    }

    //Clean Cache every 30 Minutes
    Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> cache.values().forEach(Cache::cleanUp), 36000, 36000);
  }

  @Override
  protected void onDisable() {

  }

  @Override
  public String getModuleName() {
    return "Chestlock";
  }

  @EventHandler
  public void onWorldLoad(final WorldLoadEvent event) {
    cache.put(event.getWorld().getUID(), CacheBuilder.newBuilder().maximumSize(500).expireAfterAccess(10, TimeUnit.MINUTES).build());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChestPlace(final BlockPlaceEvent event) {
    if (event.isCancelled() || !chests.isTagged(event.getBlock())) {
      return;
    }

    final Player player = event.getPlayer();

    if (player.getGameMode() == GameMode.CREATIVE) {
      return;
    }

    final Block block = event.getBlock();
    boolean canLock = true;

    for (final BlockFace face : facings) {
      final Block relative = block.getRelative(face);
      if (!chests.isTagged(relative)) {
        continue;
      }

      final TileState chest = (TileState) relative.getState();

      if (isLocked(chest) && !canAccess(player, chest)) {
        event.setCancelled(true);
        player.sendMessage("Du kannst hier keine Kiste platzieren.");
        canLock = false;
        break;
      }
    }

    if (canLock && !player.isSneaking()) {
      lockChest(block, player.getUniqueId());
      player.sendMessage(F.main("Info", "Dieser Behälter ist nun verschlossen."));
    }
  }

  @EventHandler
  public void onChestInteract(final PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (!chests.isTagged(event.getClickedBlock())) {
      return;
    }

    final Player player = event.getPlayer();
    final BlockState state = event.getClickedBlock().getState();

    if (!(state instanceof TileState)) {
      return;
    }

    if (!isLocked(state)) {
      return;
    }

    if (isLocked(state) && canAccess(player, (TileState) state)) {
      if (player.getGameMode() == GameMode.SPECTATOR) {
        final OfflinePlayer op = Bukkit.getOfflinePlayer(getLock((TileState) state).get().getOwner());
        player.sendMessage(F.main("Info", "Dieser Behälter wurde von " + F.name((op.getName() == null) ? op.getUniqueId().toString() : op.getName()) + " verschlossen."));
        return;
      }
      return;
    }

    event.setCancelled(true);
    player.sendActionBar("§cDieser Behälter ist verschlossen");
    UtilPlayer.playSound(player, Sound.BLOCK_CHEST_LOCKED);
  }

  @EventHandler
  public void HopperMoveItem(final InventoryMoveItemEvent event) {
    if (event.getSource().getType() != InventoryType.CHEST || event.getSource().getLocation() == null) {
      return;
    }

    final Block block = event.getSource().getLocation().getBlock();
    if (!chests.isTagged(block)) {
      return;
    }

    if (isLocked(block.getState())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onChestBreak(final BlockBreakEvent event) {
    if (!chests.isTagged(event.getBlock())) {
      return;
    }

    final Player player = event.getPlayer();
    final BlockState state = event.getBlock().getState();

    if (!(state instanceof TileState)) {
      return;
    }

    if (isLocked(state)) {

      if (!canAccess(player, (TileState) state)) {
        event.setCancelled(true);
        player.sendActionBar("§cDiese Kiste ist gesichert und kann nicht zerstört werden.");
      } else {
        cache.get(state.getWorld().getUID()).invalidate(state.getBlock().getBlockKey());
      }
    }
  }

  public boolean canAccess(final Player player, final TileState container) {
    if (player.hasPermission(bypassPerm)) {
      return true;
    }

    return getLock(container).get().canAccess(player.getUniqueId());
  }

  public boolean isLocked(final BlockState state) {
    if (!(state instanceof TileState)) {
      return false;
    }

    final TileState chest = (TileState) state;
    return chest.getPersistentDataContainer().has(lockID, PersistentDataType.STRING);
  }

  public Optional<Lock> getLock(final TileState state) {
    Lock lock = null;
    try {
      lock = cache.get(state.getWorld().getUID()).get(state.getBlock().getBlockKey(), () -> {
        final String content = state.getPersistentDataContainer().get(lockID, PersistentDataType.STRING);
        System.out.println("Added Value");
        return mapper.readValue(content, Lock.class);
      });
    } catch (final ExecutionException ex) {
      ex.printStackTrace();
    }
    return Optional.ofNullable(lock);
  }

  public boolean addMember(final TileState container, final OfflinePlayer target) {
    final Optional<Lock> tlock = getLock(container);

    if (tlock.isEmpty()) {
      return false;
    }

    final Lock lock = tlock.get();
    lock.addMember(target.getUniqueId());

    return updateLock(container, lock);
  }

  public boolean removeMember(final TileState container, final OfflinePlayer target) {
    final Optional<Lock> tlock = getLock(container);

    if (tlock.isEmpty()) {
      return false;
    }

    final Lock lock = tlock.get();
    lock.removeMember(target.getUniqueId());
    return updateLock(container, lock);
  }

  public boolean lockChest(final Block block, final UUID uuid) {
    final BlockState state = block.getState();

    if (!(state instanceof TileState)) {
      return false;
    }

    final TileState container = (TileState) state;

    final Lock lock = new Lock(uuid);

    cache.get(state.getWorld().getUID()).put(block.getBlockKey(), lock);

    return updateLock(container, lock);
  }

  public void unlockChest(final Block block) {
    final BlockState state = block.getState();

    if (!(state instanceof TileState)) {
      return;
    }
    final TileState container = (TileState) state;
    getLock(container).ifPresent(lock -> updateLock(container, null));
  }


  private boolean updateLock(final TileState state, @Nullable final Lock lock) {
    try {
      if (state instanceof Chest && ((Chest) state).getInventory().getHolder() instanceof DoubleChest) {
        final Chest chest = (Chest) state;
        final InventoryHolder holder = chest.getInventory().getHolder();

        if (holder instanceof DoubleChest) {
          final DoubleChest dc = (DoubleChest) holder;

          final TileState left = (TileState) dc.getLeftSide();
          final TileState right = (TileState) dc.getRightSide();

          if (lock == null) {

            left.getPersistentDataContainer().remove(lockID);
            right.getPersistentDataContainer().remove(lockID);

            cache.get(state.getWorld().getUID()).invalidate(left.getBlock().getBlockKey());
            cache.get(state.getWorld().getUID()).invalidate(right.getBlock().getBlockKey());

          } else {
            left.getPersistentDataContainer().set(lockID, PersistentDataType.STRING, mapper.writeValueAsString(lock));
            right.getPersistentDataContainer().set(lockID, PersistentDataType.STRING, mapper.writeValueAsString(lock));
          }

          left.update(true);
          right.update(true);
          return true;
        }
      }

      if (lock == null) {
        state.getPersistentDataContainer().remove(lockID);
        cache.get(state.getWorld().getUID()).invalidate(state.getBlock().getBlockKey());
      } else {
        state.getPersistentDataContainer().set(lockID, PersistentDataType.STRING, mapper.writeValueAsString(lock));
      }
      state.update(true);
      return true;
    } catch (final JsonProcessingException exception) {
      exception.printStackTrace();
      return false;
    }
  }
}