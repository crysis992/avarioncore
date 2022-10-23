/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.arrowtrails.ArrowTrail can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.arrowtrails;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import net.crytec.AvarionCore;
import net.crytec.cosmetic.CosmeticManager;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArrowTrail implements Listener {

  private final Map<UUID, String> _active = new HashMap<>();
  private final Map<String, ArrowTrailData> _trails = new HashMap<>();

  public ArrowTrail(final CosmeticManager manager) {

    if (manager.getConfig().getConfigurationSection("arrowtrail") == null) {

      manager.getConfig().set("arrowtrail.heart.particle", Particle.HEART.toString());
      manager.getConfig().set("arrowtrail.heart.name", "&cHerz");
      manager.getConfig().set("arrowtrail.heart.permission", "arrowtrail.heart");
      manager.getConfig().set("arrowtrail.heart.icon", "CAKE");
      manager.getConfig().set("arrowtrail.heart.description", Arrays.asList("&7Standard Beschreibung", "&7Zeile 2"));
      manager.getConfig().set("arrowtrail.heart.amount", 5);

      manager.getConfig().set("arrowtrail.note.particle", Particle.NOTE.toString());
      manager.getConfig().set("arrowtrail.note.name", "&aNoten");
      manager.getConfig().set("arrowtrail.note.permission", "arrowtrail.note");
      manager.getConfig().set("arrowtrail.note.icon", "NOTE_BLOCK");
      manager.getConfig().set("arrowtrail.note.description", Arrays.asList("&7Standard Beschreibung", "&7Zeile 2"));
      manager.getConfig().set("arrowtrail.note.amount", 5);
      manager.saveConfig();
    }

    final ConfigurationSection section = manager.getConfig().getConfigurationSection("arrowtrail");

    section.getKeys(false).forEach(key -> {
      final ConfigurationSection data = section.getConfigurationSection(key);
      String name = ChatColor.translateAlternateColorCodes('&', data.getString("name"));
      name = ChatColor.stripColor(name);

      List<String> desc = data.getStringList("description");
      desc = desc.stream().map(x -> ChatColor.translateAlternateColorCodes('&', x)).collect(Collectors.toList());

			_trails.put(name, new ArrowTrailData(new ItemBuilder(Material.valueOf(data.getString("icon")))
          .name(ChatColor.translateAlternateColorCodes('&', data.getString("name")))
          .lore(desc).build()
          , name, data.getString("permission"), Particle.valueOf(data.getString("particle")), data.getInt("amount", 5)));
    });

    Bukkit.getPluginManager().registerEvents(this, AvarionCore.getPlugin());

  }

  public Collection<ArrowTrailData> getTrailData() {
    return _trails.values();
  }

  public boolean hasActiveTrail(final Player player) {
    return _active.containsKey(player.getUniqueId());
  }

  public void deactivateTrail(final Player player) {
		_active.remove(player.getUniqueId());
  }

  public void setActiveTrail(final Player player, final ArrowTrailData data) {
		_active.put(player.getUniqueId(), data.getName());
  }

  public ArrowTrailData getActiveTrail(final Player player) {
		if (!hasActiveTrail(player)) {
			return null;
		}
    return _trails.get(_active.get(player.getUniqueId()));
  }

  @EventHandler
  public void onShoot(final PlayerLaunchProjectileEvent e) {

		if (!hasActiveTrail(e.getPlayer())) {
			return;
		}

    if (e.getProjectile() instanceof Arrow) {
      new ArrowTrailTask((Arrow) e.getProjectile(), getActiveTrail(e.getPlayer()));
    }
  }
}