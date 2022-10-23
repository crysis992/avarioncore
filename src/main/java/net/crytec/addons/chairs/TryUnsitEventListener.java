/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.chairs.TryUnsitEventListener can not be copied and/or distributed without the express
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

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class TryUnsitEventListener implements Listener {

  public Chairs plugin;

  public TryUnsitEventListener(final Chairs plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerTeleport(final PlayerTeleportEvent event) {
    final Player player = event.getPlayer();
    if (plugin.getPlayerSitData().isSitting(player)) {
			plugin.getPlayerSitData().unsitPlayer(player);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(final PlayerQuitEvent event) {
    final Player player = event.getPlayer();
    if (plugin.getPlayerSitData().isSitting(player)) {
			plugin.getPlayerSitData().unsitPlayer(player);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerDeath(final PlayerDeathEvent event) {
    final Player player = event.getEntity();
    if (plugin.getPlayerSitData().isSitting(player)) {
			plugin.getPlayerSitData().unsitPlayer(player);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onExitVehicle(final VehicleExitEvent e) {
    if (e.getVehicle().getPassenger() instanceof Player) {
      final Player player = (Player) e.getVehicle().getPassenger();
      if (plugin.getPlayerSitData().isSitting(player)) {
        if (!plugin.getPlayerSitData().unsitPlayer(player)) {
          e.setCancelled(true);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBlockBreak(final BlockBreakEvent event) {
    final Block b = event.getBlock();
    if (plugin.getPlayerSitData().isBlockOccupied(b)) {
      final Player player = plugin.getPlayerSitData().getPlayerOnChair(b);
			plugin.getPlayerSitData().unsitPlayer(player);
    }
  }

}
