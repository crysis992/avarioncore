/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.treefall.NoAnimationTreeDestroyer can not be copied and/or distributed without the express
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

import java.util.HashSet;
import org.bukkit.block.Block;

class NoAnimationTreeDestroyer {

  /*
  Only ever triggers when people have tree falling animations off in the config
   */
  static void destroyTree(final HashSet<Block> blocks, final boolean hasSilkTouch) {
    // Drop loot and plant a new sapling
    for (final Block block : blocks) {
      TreeLoot.dropTreeLoot(block.getBlockData(), block.getLocation().clone().add(0.5, 0.5, 0.5), hasSilkTouch);
      TreeReplant.replaceOriginalBlock(block);
    }
  }

}
