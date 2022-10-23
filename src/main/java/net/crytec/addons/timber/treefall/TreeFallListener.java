/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.treefall.TreeFallListener can not be copied and/or distributed without the express
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

package net.crytec.addons.timber.treefall;

import de.tr7zw.changeme.nbtapi.NBTItem;
import java.util.HashSet;
import net.crytec.addons.timber.UltimateTimber;
import net.crytec.addons.timber.events.TreeFallEvent;
import net.crytec.addons.timber.events.TreeFellEvent;
import net.crytec.addons.timber.hooks.HookManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class TreeFallListener implements Listener {

  private final Boolean requireNBT;
  private final String requiredNBTTag;

  public TreeFallListener(final UltimateTimber instance) {
    requireNBT = instance.getConfig().getBoolean("requirenbt");
    requiredNBTTag = instance.getConfig().getString("requiredtag");
  }

  /*
  This is the starting point for the whole effect
  It's been broken up instead of chained in order to make step-by-step debugging easier
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTreeBreak(final BlockBreakEvent event) {

    final Block block = event.getBlock();
    final Player player = event.getPlayer();

    if (event.isCancelled() || !TreeChecker.getValidWoodMaterials().isTagged(event.getBlock())) {
      return;
    }

    final ItemStack axe = player.getInventory().getItemInMainHand();

    if (player.getGameMode() == GameMode.CREATIVE || !axe.getType().toString().contains("AXE")) {
      return;
    }

    if (requireNBT) {
      final NBTItem nbt = new NBTItem(axe);

      if (!nbt.hasKey(requiredNBTTag)) {
        return;
      }
    }

    if (!UltimateTimber.getInstance().isChopping(event.getPlayer())) {
      return;
    }

    final TreeChecker treeChecker = new TreeChecker();
    final HashSet<Block> blocks = treeChecker.parseTree(block);

        /*
        Previous list will be null if no valid tree is found
         */
    if (blocks == null) {
      return;
    }

    //Call event that tree will fall
    final TreeFallEvent treeFallEvent = new TreeFallEvent(event.getPlayer(), treeChecker, block);
    Bukkit.getPluginManager().callEvent(treeFallEvent);
    if (treeFallEvent.isCancelled()) {
      return;
    }
        
        /*
        Everything beyond this point assumes that the tree was valid
         */

    // Do not let any items drop, it will be handled later
    event.setDropItems(false);

    // Apply hooks
    HookManager.getInstance().applyHooks(event.getPlayer(), blocks);

    AxeDurability.adjustAxeDamage(blocks, event.getPlayer());

    TreeSounds.tipOverNoise(block.getLocation());

    final TreeFallAnimation treeFallAnimation = new TreeFallAnimation();
    treeFallAnimation.startAnimation(block, blocks, event.getPlayer());

    //Call event that a tree has fell
    final TreeFellEvent treeFellEvent = new TreeFellEvent(event.getPlayer(), treeChecker, block);
    Bukkit.getPluginManager().callEvent(treeFellEvent);

  }

}
