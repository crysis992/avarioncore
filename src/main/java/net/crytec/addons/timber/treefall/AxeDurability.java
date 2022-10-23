/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.treefall.AxeDurability can not be copied and/or distributed without the express
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
import java.util.HashSet;
import java.util.Random;
import net.crytec.AvarionCore;
import net.crytec.addons.timber.utils.TreeConverter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class AxeDurability {

  private static final Random random = new Random();

  private final static MaterialSetTag VALID_AXES = new MaterialSetTag(new NamespacedKey(AvarionCore.getPlugin(), "axes"),
      Material.DIAMOND_AXE,
      Material.GOLDEN_AXE,
      Material.IRON_AXE,
      Material.STONE_AXE,
      Material.WOODEN_AXE);

  /**
   * Applies damage to the axe the player is holding based on logs they broke
   *
   * @param blocks The blocks that are part of the tree
   * @param player The player
   */
  public static void adjustAxeDamage(final HashSet<Block> blocks, final Player player) {
    if (player.getGameMode().equals(GameMode.CREATIVE)) {
      return;
    }

    final ItemStack item = player.getInventory().getItemInMainHand();

    if (!VALID_AXES.isTagged(item)) {
      return;
    }

    final int unbreakingLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);

    final ItemMeta itemMeta = item.getItemMeta();
    final Damageable damageableMeta = (Damageable) itemMeta;
    for (final Block block : blocks) {
      final Material material = TreeConverter.convertWoodToLog(block.getType());
      if (isMaterialDurable(material) && checkUnbreakingChance(unbreakingLevel)) {
        damageableMeta.setDamage(damageableMeta.getDamage() + 1);
      }
    }

    item.setItemMeta((ItemMeta) damageableMeta);

    if (((Damageable) item.getItemMeta()).getDamage() >= item.getType().getMaxDurability()) {
      player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    }
  }

  /**
   * Checks is a material should apply durability
   *
   * @param material The material to check
   * @return If durability should be applied
   */
  private static boolean isMaterialDurable(final Material material) {
    return material.equals(Material.ACACIA_LOG) ||
        material.equals(Material.BIRCH_LOG) ||
        material.equals(Material.DARK_OAK_LOG) ||
        material.equals(Material.JUNGLE_LOG) ||
        material.equals(Material.OAK_LOG) ||
        material.equals(Material.SPRUCE_LOG) ||
        material.equals(Material.STRIPPED_ACACIA_LOG) ||
        material.equals(Material.STRIPPED_BIRCH_LOG) ||
        material.equals(Material.STRIPPED_DARK_OAK_LOG) ||
        material.equals(Material.STRIPPED_JUNGLE_LOG) ||
        material.equals(Material.STRIPPED_OAK_LOG) ||
        material.equals(Material.STRIPPED_SPRUCE_LOG);
  }

  /**
   * Check if durbility should be applied based on the unbreaking enchantment
   *
   * @param level The level of the unbreaking enchantment
   * @return True if durability should be applied, otherwise false
   */
  private static boolean checkUnbreakingChance(final int level) {
    return ((double) 1 / (level + 1)) > random.nextDouble();
  }

}
