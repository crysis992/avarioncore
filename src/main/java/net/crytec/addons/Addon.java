/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.Addon can not be copied and/or distributed without the express
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

import co.aikar.commands.BaseCommand;
import java.io.File;
import java.io.IOException;
import net.crytec.AvarionCore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class Addon {

  protected AvarionCore plugin;

  private YamlConfiguration configuration;

  /**
   * This is called when the module enables
   */
  protected abstract void onEnable();

  /**
   * Called when the module disables
   */
  protected abstract void onDisable();

  public final AvarionCore getPlugin() {
    return plugin;
  }

  private boolean configUpdated = false;

  /**
   * Returns the name of the module
   *
   * @return Modules name
   */
  public abstract String getModuleName();

  public final void enable(final AvarionCore plugin) {
    this.plugin = plugin;
    final File dataFolder = new File(plugin.getDataFolder() + File.separator + "data");
    final File addonFolder = new File(plugin.getDataFolder() + File.separator + "addons");
    if (!dataFolder.exists()) {
      dataFolder.mkdir();
    }
    if (!addonFolder.exists()) {
      addonFolder.mkdir();
    }

    try {
      onEnable();
      if (configUpdated) {
        saveConfig();
      }
    } catch (final Exception ex) {
      plugin.getLogger().severe("Failed to initialize module " + getModuleName());
      ex.printStackTrace();
    }
  }

  public final void disable() {
    try {
      onDisable();
    } catch (final Exception ex) {
      plugin.getLogger().severe("Failed to shutdown module " + getModuleName());
      ex.printStackTrace();
    }
  }

  @NotNull
  public final YamlConfiguration getConfig() {
    if (configuration != null) {
      return configuration;
    }

    final File confiFile = new File(plugin.getDataFolder() + File.separator + "addons" + File.separator + getModuleName() + ".yml");
    if (!confiFile.exists()) {
      try {
        confiFile.createNewFile();
      } catch (final IOException e) {
        plugin.getLogger().severe("Failed to create configuration file for module " + getModuleName() + ": " + e.getMessage());
      }
    }
    final YamlConfiguration config = YamlConfiguration.loadConfiguration(confiFile);
    configuration = config;
    return config;
  }

  public final void saveConfig() {
    final File confiFile = new File(plugin.getDataFolder() + File.separator + "addons" + File.separator + getModuleName() + ".yml");
    try {
      configuration.save(confiFile);
    } catch (final IOException e) {
      plugin.getLogger().severe("Failed to save configuration file for module " + getModuleName() + ": " + e.getMessage());
    }
  }

  public final void reloadConfiguration() {
    final File confiFile = new File(plugin.getDataFolder() + File.separator + "addons" + File.separator + getModuleName() + ".yml");
    configuration = YamlConfiguration.loadConfiguration(confiFile);
  }

  public final void setConfigEntry(@NotNull final String path, @NotNull final Object value) {
    if (!getConfig().isSet(path)) {
      getConfig().set(path, value);
      configUpdated = true;
    }
  }

  protected final void registerCommand(final BaseCommand command) {
    getPlugin().getCommandManager().registerCommand(command);
  }

  /**
   * Logs a message to the console with infolevel INFO. This includes the modulename
   *
   * @param message The message to send
   */
  protected final void log(final String message) {
    plugin.getLogger().info("(" + getModuleName() + ") " + message);
  }
}