/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.WorldLicences can not be copied and/or distributed without the express
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

package net.crytec.addons;

import com.google.common.collect.Maps;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WorldLicences extends Addon implements Listener {


  private HashMap<String, Pair<String, String>> restrictedWorlds;


  @Override
  protected void onEnable() {
    setConfigEntry("worlds.endwelt.permission", "lizenz.endwelt");
    setConfigEntry("worlds.endwelt.failedMessage", "§cUm diese Welt zu betreten benötigst du die &7[&eEndwelt Lizenz&7]");

    restrictedWorlds = Maps.newHashMap();

    final ConfigurationSection section = getConfig().getConfigurationSection("worlds");
    if (section == null) {
      return;
    }

    for (final String world : section.getKeys(false)) {
      restrictedWorlds.put(world, Pair.of(getConfig().getString("worlds." + world + ".permission"), getConfig().getString("worlds." + world + ".failedMessage")));
    }

    Bukkit.getPluginManager().registerEvents(this, getPlugin());

  }

  @Override
  protected void onDisable() {
  }

  @Override
  public String getModuleName() {
    return "WorldLicences";
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldChange(final PlayerTeleportEvent event) {

    final String world = event.getTo().getWorld().getName();

    if (!restrictedWorlds.containsKey(world)) {
      return;
    }
    if (event.getPlayer().hasPermission(restrictedWorlds.get(world).getKey())) {
      return;
    }

    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', restrictedWorlds.get(world).getValue()));
    event.setCancelled(true);
  }
}