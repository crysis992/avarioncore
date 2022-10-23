/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.UltimateTimber can not be copied and/or distributed without the express
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

package net.crytec.addons.timber;

import net.crytec.addons.Addon;
import net.crytec.addons.timber.hooks.HookManager;
import net.crytec.addons.timber.treefall.TreeFallAnimation;
import net.crytec.addons.timber.treefall.TreeFallListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UltimateTimber extends Addon {

    private static UltimateTimber INSTANCE;

//    private final List<UUID> isNotChopping = new ArrayList<>();

    public static UltimateTimber getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
//      getPlugin().getCommandManager().registerCommand(new ToggleTreeChop());
        
        final boolean save = false;
        //Allow mixed log/leaf to be considered as one tree
      setConfigEntry("allowMixedTreeTypes", false);
        //Max amount of logs that can be broken with one chop
      setConfigEntry("maximumBlocks", 120);
        //Minimum amount of leaves to be considered as a tree
      setConfigEntry("minimumLeaves", 5);
         //"Only break logs above the block broken"
      setConfigEntry("breakOnlyUpwards", true);
        //Entire tree base must be broken for the tree to fall
      setConfigEntry("entireTreeBrokenToFall", false);
        //Replant saplings
      setConfigEntry("replantSaplings", true);

      setConfigEntry("requirenbt", true);
      setConfigEntry("requiredtag", "timberaxe");
        /*
        Register the main event that handles toppling down trees
         */
        Bukkit.getServer().getPluginManager().registerEvents(new TreeFallListener(this), getPlugin());

        /*
        Prevent falling blocks from forming new blocks on the floor
         */
        Bukkit.getServer().getPluginManager().registerEvents(new TreeFallAnimation(), getPlugin());
        
        /*
        Hook into supported plugins
         */
        HookManager.getInstance().hook();
    }

    @Override
    public void onDisable() {

    }

  @Override
  public String getModuleName() {
    return "TreeChop";
  }

//    public boolean toggleChopping(final Player player) {
//        final boolean removed = isNotChopping.remove(player.getUniqueId());
//        if (!removed)
//          isNotChopping.add(player.getUniqueId());
//        return removed;
//    }

//    public boolean isChopping(final Player player) {
//        return !isNotChopping.contains(player.getUniqueId());
//    }
    public boolean isChopping(final Player player) { return true; }
}
