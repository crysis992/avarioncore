package net.crytec.internal;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginStorage {

  private final JavaPlugin plugin;
  private final File dataPath;

  private final HashMap<UUID, HashMap<String, PlayerData>> playerData = Maps.newHashMap();
  private final HashMap<String, Class<? extends PlayerData>> storageAdapters = Maps.newHashMap();

  public PluginStorage(final JavaPlugin plugin) {
    this.plugin = plugin;
    dataPath = new File(plugin.getDataFolder(), "playerstorage");
    if (!dataPath.exists()) {
      dataPath.mkdirs();
    }
  }

  public void addStorageAdapter(final Class<? extends PlayerData> clazz) {
    storageAdapters.put(clazz.getName(), clazz);
  }

  public void loadPlayer(final UUID uuid) {

    final File cfg = new File(dataPath, uuid.toString() + ".yml");
    if (!cfg.exists()) {
      try {
        cfg.createNewFile();
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }

    final YamlConfiguration config = YamlConfiguration.loadConfiguration(cfg);
    playerData.putIfAbsent(uuid, Maps.newHashMap());

    for (final String key : storageAdapters.keySet()) {
      try {
        final Class<? extends PlayerData> clazz = storageAdapters.get(key);

        if (clazz.getConstructors().length != 1 || clazz.getConstructors()[0].getParameterCount() >= 1) {
          throw new IllegalPluginAccessException("The use of an constructor with parameters is forbidden at class " + clazz.getName());
        }

        final PlayerData data = clazz.newInstance();
        data.initialize(plugin, uuid, cfg, config);

        playerData.get(uuid).put(key, data);
        data.loadData(config);
        data.initialize();
      } catch (final Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void unloadPlayer(final UUID uuid) {
    final File cfg = new File(dataPath, uuid.toString() + ".yml");
    final YamlConfiguration config = YamlConfiguration.loadConfiguration(cfg);

    if (!playerData.containsKey(uuid)) {
      return;
    }

    for (final PlayerData data : playerData.get(uuid).values()) {
      data.saveData(config);
    }

    try {
      config.save(cfg);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public PlayerData getPlayerData(final Player player, final String key) {
    return getPlayerData(player.getUniqueId(), key);
  }

  public <T extends PlayerData> T getPlayerData(final UUID uuid, final String key) {
    return (T) playerData.get(uuid).get(key);
  }

}
