/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.listener.GeneralListener can not be copied and/or distributed without the express
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

import net.crytec.AvarionCore;
import net.crytec.libs.commons.utils.UtilLoc;
import net.crytec.libs.commons.utils.UtilMath;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class GeneralListener implements Listener {

  private final AvarionCore plugin;

  public GeneralListener(final AvarionCore plugin) {
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void blockFlyDamage(final EntityDamageByEntityEvent event) {

    if (event.getDamager() instanceof Player) {
      final Player attacker = (Player) event.getDamager();
      if (attacker.getGameMode() != GameMode.CREATIVE && attacker.isFlying()) {
        event.setCancelled(true);
        attacker.sendMessage(F.main("Info", "Im Flugmodus darfst du keine Kreaturen angreifen."));
      }
    }
  }

  @EventHandler
  public void blockFlyDamage(final ProjectileLaunchEvent event) {

    if (event.getEntity().getShooter() instanceof Player) {
      final Player shooter = (Player) event.getEntity().getShooter();
      if (shooter.getGameMode() != GameMode.CREATIVE && shooter.isFlying()) {
        event.setCancelled(true);
        shooter.sendMessage(F.main("Info", "Im Flugmodus darfst du keine Projektile abfeuern."));
      }
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void storeLastLoc(final PlayerTeleportEvent e) {

    if (e.getFrom().getWorld() == e.getTo().getWorld()) {
      final double offset = UtilMath.offset(e.getFrom(), e.getTo());
      if (offset <= 15.0D) {
        return;
      }
    }

    e.getPlayer().setMetadata("last_location", new FixedMetadataValue(plugin, UtilLoc.locToString(e.getFrom())));
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void storeLastLoconDeath(final PlayerDeathEvent e) {
    e.getEntity().setMetadata("last_location", new FixedMetadataValue(plugin, UtilLoc.locToString(e.getEntity().getLocation())));
  }

  @EventHandler(ignoreCancelled = true)
  public void noPickupMode(final PlayerAttemptPickupItemEvent e) {
    if (e.getPlayer().hasMetadata("nopickup")) {
      e.setFlyAtPlayer(false);
      e.setCancelled(true);
    }
  }
}