/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.hooks.HookManager can not be copied and/or distributed without the express
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

package net.crytec.addons.timber.hooks;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class HookManager {

  private static HookManager instance;

  private final Set<TimberHook> hooks;

  private HookManager() {
    hooks = Sets.newHashSet();
  }

  /**
   * Gets the instance of the HookManager
   *
   * @return The instance of the HookManager
   */
  public static HookManager getInstance() {
    if (instance == null) {
      instance = new HookManager();
    }
    return instance;
  }

  /**
   * Hooks into compatible plugins
   */
  public void hook() {
    tryHook("Jobs", JobsRebornHook.class);
  }

  /**
   * Tries to hook into a compatible plugin
   *
   * @param pluginName The name of the plugin
   * @param hookClass  The hook class
   */
  private void tryHook(final String pluginName, final Class<? extends TimberHook> hookClass) {
    if (!Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
      return;
    }
    try {
      hooks.add(hookClass.getDeclaredConstructor().newInstance());
      Bukkit.getConsoleSender().sendMessage(String.format("Hooks: Hooked into %s!", pluginName));
    } catch (final Exception ex) {
      Bukkit.getConsoleSender().sendMessage(String.format("Hooks: Unable to hook with %s, the version installed is not supported!", pluginName));
    }
  }

  /**
   * Applies the loaded hooks
   *
   * @param player     The player to apply the hook for
   * @param treeBlocks The blocks of the tree that were broken
   */
  public void applyHooks(final Player player, final HashSet<Block> treeBlocks) {
    final Set<TimberHook> invalidHooks = new HashSet<>();
    for (final TimberHook hook : hooks) {
      try {
        hook.apply(player, treeBlocks);
      } catch (final Exception ex) {
        invalidHooks.add(hook);
      }
    }

    for (final TimberHook hook : invalidHooks) {
      hooks.remove(hook);
    }
  }

}
