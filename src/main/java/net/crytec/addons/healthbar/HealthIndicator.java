/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.healthbar.HealthIndicator can not be copied and/or distributed without the express
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

package net.crytec.addons.healthbar;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import net.crytec.addons.Addon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HealthIndicator extends Addon implements Listener {

  private HashMap<UUID, HealthIndicatorUser> data;

  @Override
  public void onEnable() {
		data = Maps.newHashMap();

    for (final Player player : Bukkit.getOnlinePlayers()) {
			data.put(player.getUniqueId(), new HealthIndicatorUser(player));
    }
    Bukkit.getPluginManager().registerEvents(this, getPlugin());
    Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), new HealthBarTask(this), 100L, 60);
  }

  @Override
  public void onDisable() {
    for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
			data.get(player.getUniqueId()).getBar().removeAll();
    }
		data.clear();
  }

  @Override
  public String getModuleName() {
    return "HealthBars";
  }

  @EventHandler
  public void init(final PlayerJoinEvent e) {
		data.put(e.getPlayer().getUniqueId(), new HealthIndicatorUser(e.getPlayer()));
  }

  @EventHandler
  public void removeOnQuit(final PlayerQuitEvent e) {
		data.get(e.getPlayer().getUniqueId()).getBar().removeAll();
		data.remove(e.getPlayer().getUniqueId());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void entityDamageByEntity(final EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}

    final Entity target = event.getEntity();
    final Entity damager = event.getDamager();

		if (!(target instanceof LivingEntity)) {
			return;
		}

    final LivingEntity entity = (LivingEntity) event.getEntity();
    if (damager instanceof Player) {
      final Player player = (Player) damager;
			data.get(player.getUniqueId()).sendEntityInfo(entity, event.getFinalDamage());
    } else {
      if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player) {
        final Player player2 = (Player) ((Projectile) damager).getShooter();
				data.get(player2.getUniqueId()).sendEntityInfo(entity, event.getFinalDamage());
      }
    }
  }

  public Collection<HealthIndicatorUser> getTrackedPlayers() {
    return data.values();
  }
}
