/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 11.12.19, 14:38	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.listener.WorldGuardRegionListener can not be copied and/or distributed without the express
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

package net.crytec.listener;

import com.google.common.collect.Sets;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.crytec.AvarionCore;
import net.crytec.util.TaskManager;
import net.crytec.util.regionevents.MovementWay;
import net.crytec.util.regionevents.RegionEnterEvent;
import net.crytec.util.regionevents.RegionEnteredEvent;
import net.crytec.util.regionevents.RegionLeaveEvent;
import net.crytec.util.regionevents.RegionLeftEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class WorldGuardRegionListener implements Listener {

  private final Object2ObjectOpenHashMap<Player, Set<ProtectedRegion>> playerRegions = new Object2ObjectOpenHashMap<>();
  private final RegionContainer container;

  public WorldGuardRegionListener(final AvarionCore plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
    container = WorldGuard.getInstance().getPlatform().getRegionContainer();
  }


  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerKick(final PlayerKickEvent e) {
    final Set<ProtectedRegion> regions = playerRegions.remove(e.getPlayer());
    if (regions != null) {
      for (final ProtectedRegion region : regions) {
        new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e).callEvent();
        new RegionLeftEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e).callEvent();
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(final PlayerQuitEvent e) {
    final Set<ProtectedRegion> regions = playerRegions.remove(e.getPlayer());
    if (regions != null) {
      for (final ProtectedRegion region : regions) {
        new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e).callEvent();
        new RegionLeftEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e).callEvent();
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerMove(final PlayerMoveEvent e) {
    if (e.getPlayer().hasMetadata("NPC")) {
      return;
    }

    final Location from = e.getFrom();
    final Location to = e.getTo();

    if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
      return;
    }

    e.setCancelled(updateRegions(e.getPlayer(), MovementWay.MOVE, to, e));
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerChangeWorlds(final PlayerChangedWorldEvent event) {
    final Player player = event.getPlayer();
    if (player.hasMetadata("NPC")) {
      return;
    }
    clearRegions(player, event);
    updateRegions(player, MovementWay.WORLD_CHANGE, event.getPlayer().getLocation(), event);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerTeleport(final PlayerTeleportEvent event) {
    final Player player = event.getPlayer();
    if (player.hasMetadata("NPC")) {
      return;
    }
    final TeleportCause cause = event.getCause();
    MovementWay movementType = MovementWay.TELEPORT;
    if (cause == TeleportCause.END_PORTAL || cause == TeleportCause.NETHER_PORTAL) {
      clearRegions(player, event);
      movementType = MovementWay.WORLD_CHANGE;
    }
    if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
      clearRegions(player, event);
    }
    updateRegions(player, movementType, event.getTo(), event);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(final PlayerJoinEvent e) {
    updateRegions(e.getPlayer(), MovementWay.SPAWN, e.getPlayer().getLocation(), e);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerRespawn(final PlayerRespawnEvent e) {
    updateRegions(e.getPlayer(), MovementWay.SPAWN, e.getRespawnLocation(), e);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onVehicleMove(final VehicleMoveEvent event) {
    final List<Entity> passengers = event.getVehicle().getPassengers();

    for (final Entity ent : passengers) {
      if (!(ent instanceof Player)) {
        continue;
      }
      updateRegions((Player) ent, MovementWay.RIDE, ent.getLocation(), event);
    }
  }

  private void clearRegions(final Player player, final PlayerEvent event) {
    for (final ProtectedRegion region : playerRegions.get(player)) {
      new RegionLeaveEvent(region, player, MovementWay.WORLD_CHANGE, event).callEvent();
      new RegionLeftEvent(region, player, MovementWay.WORLD_CHANGE, event).callEvent();
    }
    playerRegions.put(player, Sets.newHashSet());
  }

  private synchronized boolean updateRegions(final Player player, final MovementWay movement, final Location to, final Event event) {
    Set<ProtectedRegion> regions = playerRegions.get(player);
    if (regions == null) {
      regions = Sets.newHashSet();
    }
    final Set<ProtectedRegion> oldRegions = Sets.newHashSet(regions);

    final com.sk89q.worldedit.util.Location Wto = BukkitAdapter.adapt(to);
    final RegionManager rm = container.get(BukkitAdapter.adapt(to.getWorld()));
    final HashSet<ProtectedRegion> appRegions = new HashSet<>(rm.getApplicableRegions(Wto.toVector().toBlockPoint()).getRegions());
    final ProtectedRegion globalRegion = rm.getRegion("__global__");

    if (globalRegion != null) {
      appRegions.add(globalRegion);
    }

    for (final ProtectedRegion region : appRegions) {
      if (!regions.contains(region)) {
        final RegionEnterEvent e = new RegionEnterEvent(region, player, movement, event);
        Bukkit.getPluginManager().callEvent(e);

        if (e.isCancelled()) {
          regions.clear();
          regions.addAll(oldRegions);

          return true;
        } else {

          TaskManager.runTaskLater(() -> Bukkit.getPluginManager().callEvent(new RegionEnteredEvent(region, player, movement, event)), 1L);
          regions.add(region);
        }
      }
    }

    final Iterator<ProtectedRegion> itr = regions.iterator();

    while (itr.hasNext()) {
      final ProtectedRegion region = itr.next();
      if (!appRegions.contains(region)) {
        if (rm.getRegion(region.getId()) != region) {
          itr.remove();
          continue;
        }
        final RegionLeaveEvent e = new RegionLeaveEvent(region, player, movement, event);

        Bukkit.getPluginManager().callEvent(e);

        if (e.isCancelled()) {
          regions.clear();
          regions.addAll(oldRegions);
          return true;
        } else {
          TaskManager.runTaskLater(() -> Bukkit.getPluginManager().callEvent(new RegionLeftEvent(region, player, movement, event)), 1L);
          itr.remove();
        }
      }
    }

    playerRegions.put(player, regions);
    return false;
  }

}
