/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.AddonManager can not be copied and/or distributed without the express
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

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import net.crytec.AvarionCore;
import org.bukkit.ChatColor;

/**
 * The type Addon manager.
 */
public class AddonManager {

  private final AvarionCore plugin;
  private final Object2ObjectLinkedOpenHashMap<Class<? extends Addon>, Addon> registeredAddons;

  /**
   * Instantiates a new Addon manager.
   *
   * @param plugin the plugin
   */
  public AddonManager(final AvarionCore plugin) {
    this.plugin = plugin;
    registeredAddons = new Object2ObjectLinkedOpenHashMap<>();
  }

  /**
   * Gets addon.
   *
   * @param clazz the clazz
   * @return the addon
   */
  public Addon getAddon(final Class<? extends Addon> clazz) {
    return registeredAddons.get(clazz);
  }

  /**
   * Shutdown.
   */
  public void shutdown() {
    for (final Addon addon : registeredAddons.values()) {
      addon.disable();
    }
  }

  /**
   * Enable.
   */
  public void enable() {

    for (final Addon addon : registeredAddons.values()) {
      try {
        addon.enable(plugin);
        plugin.getLogger().info(ChatColor.GREEN + "Successfully enabled addon " + ChatColor.DARK_GREEN + addon.getModuleName());
      } catch (final Exception ex) {
        plugin.getLogger().severe("Failed to enable Addon " + addon.getModuleName());
        ex.printStackTrace();
      }
    }
  }

  /**
   * Register addon.
   *
   * @param clazz the clazz
   */
  public final void registerAddon(final Class<? extends Addon> clazz) {
    if (Stream.of(clazz.getConstructors()).noneMatch(c -> c.getParameterCount() == 0)) {
      plugin.getLogger().severe("Failed to register Class " + clazz.getName() + ". Class requires a parementerless Constructor!");
      return;
    }
    try {
      final Addon addon = clazz.getConstructor().newInstance();
      registeredAddons.put(clazz, addon);
    } catch (final InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}