package net.crytec.internal;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PlayerData {

  private JavaPlugin plugin;
  private UUID owner;
  private YamlConfiguration config;
  private File file;

  public void writeDefaults(final YamlConfiguration config) {

  }

  public final void initialize(final JavaPlugin plugin, final UUID owner, final File file, final YamlConfiguration config) {
    this.plugin = plugin;
    this.owner = owner;
    this.config = config;
    this.file = file;

  }

  public void initialize() {

  }

  public final boolean isOwnerOnline() {
    return Bukkit.getPlayer(owner) != null;
  }

  public final boolean saveConfig() {
    try {
      config.save(file);
      return true;
    } catch (final IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public final JavaPlugin getPlugin() {
    return plugin;
  }

  public final UUID getOwner() {
    return owner;
  }

  public final YamlConfiguration getConfig() {
    return config;
  }

  public final File getFile() {
    return file;
  }

  public abstract void saveData(YamlConfiguration config);

  public abstract void loadData(YamlConfiguration config);
}
