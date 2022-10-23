/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.spawnsystem.SpawnManager can not be copied and/or distributed without the express
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

package net.crytec.addons.spawnsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import net.crytec.AvarionCore;
import net.crytec.addons.Addon;
import net.crytec.libs.commons.utils.UtilLoc;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnManager extends Addon implements Listener {

  private Location spawn;
  private boolean voidTeleport;
  private List<String> voidWorlds = new ArrayList<>();
  private final HashMap<String, Location> worldSpawns = new HashMap<>();

  @Override
  public void onEnable() {

    AvarionCore.getPlugin().getCommandManager().registerCommand(new SetSpawnCommand(this));
    AvarionCore.getPlugin().getCommandManager().registerCommand(new SpawnCommand(this));

    setConfigEntry("spawn", UtilLoc.locToString(Bukkit.getWorlds().get(0).getSpawnLocation()));
    setConfigEntry("voidTeleport.enabled", false);
    setConfigEntry("voidTeleport.active_worlds", Collections.singletonList("world"));
    setConfigEntry("perWorldSpawn", false);

    spawn = UtilLoc.locFromString(getConfig().getString("spawn"));
    voidTeleport = getConfig().getBoolean("voidTeleport.enabled");
    voidWorlds = getConfig().getStringList("voidTeleport.active_worlds");

    if (getConfig().isSet("worldspawns")) {

      final ConfigurationSection section = getConfig().getConfigurationSection("worldspawns");

      for (final String world : section.getKeys(false)) {
        worldSpawns.put(world, UtilLoc.locFromString(getConfig().getString("worldspawns." + world)));
      }

    }

    Bukkit.getPluginManager().registerEvents(this, getPlugin());
  }

  @Override
  public void onDisable() {
  }

  @Override
  public String getModuleName() {
    return "SpawnManager";
  }

  @EventHandler
  public void TeleportOnVoid(final PlayerMoveEvent e) {
    if (!voidTeleport) {
      return;
    }
    if (!voidWorlds.contains(e.getPlayer().getWorld().getName())) {
      return;
    }
    if (e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
      return;
    }

    if (e.getPlayer().getLocation().getY() < 0) {
      e.getPlayer().teleport(spawn);
      e.getPlayer().setFallDistance(0);
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onRespawn(final PlayerRespawnEvent e) {
    if (getConfig().getBoolean("perWorldSpawn") && worldSpawns.containsKey(e.getPlayer().getWorld().getName())) {
      e.setRespawnLocation(getSpawn(e.getPlayer().getWorld()));
    } else {
      e.setRespawnLocation(spawn);
    }
  }

  public void setMainSpawn(final Location location) {
    getConfig().set("spawn", UtilLoc.locToString(location));
    saveConfig();
    spawn = location;
  }

  public void addWorldSpawn(final World world, final Location spawn) {
    worldSpawns.put(world.getName(), spawn);
    getConfig().set("worldspawns." + world.getName(), UtilLoc.locToString(spawn));
    saveConfig();

  }

  public Location getSpawn() {
    return spawn;
  }

  public Location getSpawn(final World world) {
    return worldSpawns.getOrDefault(world.getName(), spawn);
  }

  public boolean hasWorldSpawn(final World world) {
    return worldSpawns.containsKey(world.getName());
  }
}
