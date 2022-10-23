package net.crytec.internal.settings;

import java.util.HashMap;
import lombok.Getter;
import net.crytec.AvarionCore;
import net.crytec.internal.PlayerData;
import net.crytec.libs.commons.utils.UtilLoc;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class HomeSetting extends PlayerData {

  @Getter
  private final HashMap<String, Location> homes = new HashMap<>();

  private static final NamespacedKey key = new NamespacedKey(AvarionCore.getPlugin(), "homes");

  public boolean addHome(final String name, final Location location) {
    if (homes.containsKey(name)) {
      homes.put(name, location);
      return true;
    } else {

      if (homes.size() >= getMaxHomes()) {
        return false;
      } else {
        homes.put(name, location);
        return true;
      }
    }
  }

  public boolean deleteHome(final String name) {
    if (homes.containsKey(name)) {
      homes.remove(name);
      getConfig().set(key.getKey() + "." + name, null);
      return true;
    }
    return false;
  }

  public int getMaxHomes() {
//    final User user = AvarionCore.getPlugin().getPermissionManager().getApi().getUser(getOwner());
//
//    if (user == null) {
//      return 1;
//    }
//
//    final UserData cachedData = user.getCachedData();
//    final Contexts contexts = AvarionCore.getPlugin().getPermissionManager().getApi().getContextForUser(user).get();
//    final MetaData metaData = cachedData.getMetaData(contexts);
//
//    final String toParse = metaData.getMeta().getOrDefault("homelimit", "1");
//
//    return UtilMath.isInt(toParse) ? Integer.valueOf(toParse) : 1;
    return 5;
  }

  public boolean hasHome(final String name) {
    return homes.containsKey(name);
  }

  public Location getHome(final String name) {
    return homes.get(name);
  }

  public int getHomeAmount() {
    return homes.size();
  }

  @Override
  public void loadData(final YamlConfiguration config) {
    final ConfigurationSection section = config.getConfigurationSection(key.getKey());
    if (section == null) {
      return;
    }

    for (final String name : section.getKeys(false)) {
      homes.put(name, UtilLoc.locFromString(section.getString(name)));
    }
  }

  @Override
  public void saveData(final YamlConfiguration config) {
    config.set(key.getKey(), null);
    for (final String name : homes.keySet()) {
      final Location loc = homes.get(name);
      if (loc == null) {
        continue;
      }
      config.set(key.getKey() + "." + name, UtilLoc.locToString(loc));
    }
  }
}