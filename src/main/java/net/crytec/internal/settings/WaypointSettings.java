package net.crytec.internal.settings;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map.Entry;
import lombok.Getter;
import net.crytec.AvarionCore;
import net.crytec.internal.PlayerData;
import net.crytec.libs.commons.utils.UtilLoc;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class WaypointSettings extends PlayerData {

  @Getter
  private final HashMap<String, Location> waypoints = Maps.newHashMap();

  private static final NamespacedKey key = new NamespacedKey(AvarionCore.getPlugin(), "waypoints");

  @Override
  public void initialize() {
  }

  public void addWaypoint(final String id, final Location location) {
		waypoints.put(id, location);
  }

  public void deleteWaypoint(final String id) {
		waypoints.remove(id);
  }

  public HashMap<String, Location> getWaypointsInWorld(final World world) {
    final HashMap<String, Location> points = Maps.newHashMap();
		waypoints.entrySet().stream().filter(wp -> wp.getValue().getWorld().equals(world)).forEach(entry -> points.put(entry.getKey(), entry.getValue()));
    return points;
  }


  @Override
  public void loadData(final YamlConfiguration config) {
    final ConfigurationSection section = config.getConfigurationSection(key.getKey());
    if (section == null) {
      return;
    }

    for (final String wp : section.getKeys(false)) {
			waypoints.put(wp, UtilLoc.locFromString(section.getString(wp)));
    }

  }

  @Override
  public void saveData(final YamlConfiguration config) {
    for (final Entry<String, Location> wp : waypoints.entrySet()) {
      config.set(key.getKey() + "." + wp.getKey(), UtilLoc.locToString(wp.getValue()));
    }
  }
}