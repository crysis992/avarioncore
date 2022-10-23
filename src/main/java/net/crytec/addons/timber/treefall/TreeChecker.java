/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.treefall.TreeChecker can not be copied and/or distributed without the express
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

import com.destroystokyo.paper.MaterialSetTag;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import net.crytec.AvarionCore;
import net.crytec.addons.timber.UltimateTimber;
import net.crytec.addons.timber.utils.TreeConverter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class TreeChecker {

    /*
    Used to check if a piece of wood is a part of the tree
     */


  private final static MaterialSetTag VALID_LOG_MATERIALS = new MaterialSetTag(new NamespacedKey(AvarionCore.getPlugin(), "timerlog"), Material.ACACIA_LOG,
      Material.ACACIA_WOOD,
      Material.STRIPPED_ACACIA_LOG,
      Material.STRIPPED_ACACIA_WOOD,
      Material.BIRCH_LOG,
      Material.BIRCH_WOOD,
      Material.STRIPPED_BIRCH_LOG,
      Material.STRIPPED_BIRCH_WOOD,
      Material.DARK_OAK_LOG,
      Material.DARK_OAK_WOOD,
      Material.STRIPPED_DARK_OAK_LOG,
      Material.STRIPPED_DARK_OAK_WOOD,
      Material.JUNGLE_LOG,
      Material.JUNGLE_WOOD,
      Material.STRIPPED_JUNGLE_LOG,
      Material.STRIPPED_JUNGLE_WOOD,
      Material.OAK_LOG,
      Material.OAK_WOOD,
      Material.STRIPPED_OAK_LOG,
      Material.STRIPPED_OAK_WOOD,
      Material.SPRUCE_LOG,
      Material.SPRUCE_WOOD,
      Material.STRIPPED_SPRUCE_LOG,
      Material.STRIPPED_SPRUCE_WOOD,
      Material.MUSHROOM_STEM);
	
    /*
    Used to check if a leaf is a part of the tree
     */

  private static final Tag<Material> VALID_LEAF_MATERIALS = Tag.LEAVES;

  /**
   * Gets a Set of all valid wood materials
   *
   * @return A Set of all valid wood materials
   */
  public static MaterialSetTag getValidWoodMaterials() {
    return VALID_LOG_MATERIALS;
  }

  private static final Set<Vector> VALID_TRUNK_OFFSETS, VALID_BRANCH_OFFSETS, VALID_LEAF_OFFSETS;

  private HashSet<Block> treeBlocks;
  private int maxDistanceFromLog;
  private Material logType, leafType;
  private int startingBlockY;
  private int maxBranchBlocksAllowed;
  private int numLeavesRequiredForTree;
  private boolean allowMixedTreeTypes;
  private boolean onlyBreakLogsUpwards;
  private boolean destroyBaseLog;
  private boolean entireTreeBase;
  private boolean isMushroom = false;

  static {
    VALID_BRANCH_OFFSETS = new HashSet<>();
    VALID_TRUNK_OFFSETS = new HashSet<>();
    VALID_LEAF_OFFSETS = new HashSet<>();

    // 3x2x3 centered around log, excluding -y axis
    for (int x = -1; x <= 1; x++) {
      for (int y = 0; y <= 1; y++) {
        for (int z = -1; z <= 1; z++) {
          VALID_BRANCH_OFFSETS.add(new Vector(x, y, z));
        }
      }
    }

    // 3x3x3 centered around log
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -1; z <= 1; z++) {
          VALID_TRUNK_OFFSETS.add(new Vector(x, y, z));
        }
      }
    }

    // Adjacent blocks to log
    for (int i = -1; i <= 1; i += 2) {
      VALID_LEAF_OFFSETS.add(new Vector(i, 0, 0));
      VALID_LEAF_OFFSETS.add(new Vector(0, i, 0));
      VALID_LEAF_OFFSETS.add(new Vector(0, 0, i));
    }
  }

  /**
   * Parses a block for a potential tree
   *
   * @param block The based block of the potential tree
   * @return A HashSet of all blocks in the tree, or null if no tree was found
   */
  protected HashSet<Block> parseTree(final Block block) {
      treeBlocks = new HashSet<>();
      treeBlocks.add(block);

    // Set tree information
      logType = block.getType();
      leafType = TreeConverter.convertLogToLeaf(logType);
      startingBlockY = block.getLocation().getBlockY();
      isMushroom = logType.equals(Material.MUSHROOM_STEM);

    // Load settings for algorithm
    final FileConfiguration config = UltimateTimber.getInstance().getConfig();
      allowMixedTreeTypes = config.getBoolean("allowMixedTreeTypes");
      maxBranchBlocksAllowed = config.getInt("maximumBlocks");
      numLeavesRequiredForTree = config.getInt("minimumLeaves");
      onlyBreakLogsUpwards = config.getBoolean("breakOnlyUpwards");
      destroyBaseLog = false;
      entireTreeBase = config.getBoolean("entireTreeBrokenToFall");

    // Detect tree trunk
    final Set<Block> trunkBlocks = new HashSet<>();
    trunkBlocks.add(block);
    Block targetBlock = block;
    while (isValidLogType((targetBlock = targetBlock.getRelative(BlockFace.UP)).getType())) {
        treeBlocks.add(targetBlock);
      trunkBlocks.add(targetBlock);
    }

    // Tree must be at least 2 blocks tall
    if (treeBlocks.size() < 2) {
      return null;
    }

    // Detect branches off the main trunk
    for (final Block trunkBlock : trunkBlocks) {
        recursiveBranchSearch(trunkBlock);
    }

    // Make it so trees only break as many leaves as they have to
      maxDistanceFromLog = getMaxLeafDistanceFromLog();

    // Detect leaves off the trunk/branches
    final Set<Block> branchBlocks = new HashSet<>(treeBlocks);
    for (final Block branchBlock : branchBlocks) {
        recursiveLeafSearch(branchBlock, 1);
    }

    // Trees need at least 5 leaves
    if (!isMushroom && treeBlocks.stream().filter(x -> isValidLeafType(x.getType())).count() < numLeavesRequiredForTree) {
      return null;
    }

    // The lowest logs of the tree must not have a plantable surface below them
    if (entireTreeBase) {
      final int lowestY = treeBlocks.stream().min(Comparator.comparingInt(Block::getY)).get().getY();
      final boolean isTreeGrounded = treeBlocks.stream().filter(x -> x.getY() == lowestY).anyMatch(x -> {
        final Material typeBelow = x.getRelative(BlockFace.DOWN).getType();
        return (typeBelow.equals(Material.DIRT) ||
            typeBelow.equals(Material.COARSE_DIRT) ||
            typeBelow.equals(Material.PODZOL) ||
            typeBelow.equals(Material.GRASS_BLOCK) ||
            isValidLogType(typeBelow)) &&
            !x.equals(block) &&
            isValidLogType(x.getType());
      });
      if (isTreeGrounded) {
        return null;
      }
    }

    // Delete the starting block if applicable
    if (destroyBaseLog) {
        treeBlocks.remove(block);
    }

    return treeBlocks;
  }

  /**
   * Recursively searches for branches off a given block
   *
   * @param block The next block to check for a branch
   */
  private void recursiveBranchSearch(final Block block) {
    if (treeBlocks.size() > maxBranchBlocksAllowed) {
      return;
    }

    for (final Vector offset : onlyBreakLogsUpwards ? VALID_BRANCH_OFFSETS : VALID_TRUNK_OFFSETS) {
      final Block targetBlock = block.getRelative(offset.getBlockX(), offset.getBlockY(), offset.getBlockZ());
      if (isValidLogType(targetBlock.getType()) && !treeBlocks.contains(targetBlock)) {
          treeBlocks.add(targetBlock);
        if (!onlyBreakLogsUpwards || targetBlock.getLocation().getBlockY() > startingBlockY) {
            recursiveBranchSearch(targetBlock);
        }
      }
    }
  }

  /**
   * Recursively searches for leaves that are next to this tree
   *
   * @param block           The next block to check for a leaf
   * @param distanceFromLog The distance this leaf is from a log
   */
  private void recursiveLeafSearch(final Block block, final int distanceFromLog) {
    if (distanceFromLog > maxDistanceFromLog) {
      return;
    }

    for (final Vector offset : !isMushroom ? VALID_LEAF_OFFSETS : VALID_TRUNK_OFFSETS) {
      final Block targetBlock = block.getRelative(offset.getBlockX(), offset.getBlockY(), offset.getBlockZ());
      if (isValidLeafType(targetBlock.getType()) || (isMushroom && isMushroomBlock(targetBlock.getType()))) {
        if (!treeBlocks.contains(targetBlock) && !doesLeafBorderInvalidLog(targetBlock)) {
            treeBlocks.add(targetBlock);
        }
          recursiveLeafSearch(targetBlock, distanceFromLog + 1);
      }
    }
  }

  /**
   * Checks if a leaf is bordering a log that isn't part of this tree
   *
   * @param block The block to check
   * @return If the leaf borders an invalid log
   */
  private boolean doesLeafBorderInvalidLog(final Block block) {
    for (final Vector offset : VALID_TRUNK_OFFSETS) {
      final Block targetBlock = block.getRelative(offset.getBlockX(), offset.getBlockY(), offset.getBlockZ());
      if (isValidLogType(targetBlock.getType()) && !treeBlocks.contains(targetBlock)) {
        return true;
      }
    }
    return false;
  }

  private boolean isValidLogType(final Material material) {
    if (allowMixedTreeTypes) {
      return VALID_LOG_MATERIALS.isTagged(material);
    }
    return material.equals(logType);
  }

  private boolean isValidLeafType(final Material material) {
    if (allowMixedTreeTypes) {
      return VALID_LEAF_MATERIALS.isTagged(material);
    }
    return material.equals(leafType);
  }

  /**
   * Checks if a block is a mushroom head block
   *
   * @param material The block to check
   * @return If the given block is a mushroom
   */
  private boolean isMushroomBlock(final Material material) {
    return material.equals(Material.BROWN_MUSHROOM_BLOCK) || material.equals(Material.RED_MUSHROOM_BLOCK);
  }

  /**
   * Gets the max distance away from a log based on how many logs there are and the leaf type
   *
   * @return The max distance away a leaf can be from a log
   */
  private int getMaxLeafDistanceFromLog() {
    final int numLogs = treeBlocks.size();

    switch (leafType) {

      case ACACIA_LEAVES:
        return 5;

      case BIRCH_LEAVES:
        return 4;

      case DARK_OAK_LEAVES:
        return 5;

      case JUNGLE_LEAVES:
        if (numLogs > 15) {
          return 5;
        }
        return 4;

      case OAK_LEAVES:
        if (numLogs > 15) {
          return 6;
        }
        if (numLogs > 6) {
          return 5;
        }
        return 4;

      case SPRUCE_LEAVES:
        if (numLogs > 15) {
          return 6;
        }
        return 5;

      case MUSHROOM_STEM:
        return 4;

      default:
        return -1;
    }
  }

  public HashSet<Block> getTreeBlocks() {
    return treeBlocks;
  }

}
