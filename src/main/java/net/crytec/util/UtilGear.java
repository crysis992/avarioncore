/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.UtilGear can not be copied and/or distributed without the express
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

package net.crytec.util;

import lombok.experimental.UtilityClass;
import net.crytec.AvarionCore;
import net.crytec.libs.commons.utils.MaterialSet;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

@UtilityClass
public class UtilGear {

  public static final MaterialSet ARMOR = new MaterialSet(
      new NamespacedKey(AvarionCore.getPlugin(), "armortypes"), Material.LEATHER_BOOTS,
      Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS,
      Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS,
      Material.GOLDEN_BOOTS,

      Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,

      Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
      Material.DIAMOND_BOOTS,

      Material.TURTLE_HELMET, Material.ELYTRA);

  public static final MaterialSet AXE =
      new MaterialSet(new NamespacedKey(AvarionCore.getPlugin(), "axes"), Material.WOODEN_AXE,
          Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE);

  public static final MaterialSet SWORDS =
      new MaterialSet(new NamespacedKey(AvarionCore.getPlugin(), "swords"), Material.WOODEN_SWORD,
          Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD);

  public static final MaterialSet SHOVEL = new MaterialSet(
      new NamespacedKey(AvarionCore.getPlugin(), "shovel"), Material.WOODEN_SHOVEL, Material.STONE_SHOVEL,
      Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL);

  public static final MaterialSet HOE =
      new MaterialSet(new NamespacedKey(AvarionCore.getPlugin(), "hoe"), Material.WOODEN_HOE,
          Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE);

  public static final MaterialSet PICKAXE =
      new MaterialSet(new NamespacedKey(AvarionCore.getPlugin(), "pickaxe"), Material.WOODEN_PICKAXE,
          Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE,
          Material.DIAMOND_PICKAXE);

  public static final MaterialSet TOOL =
      new MaterialSet(new NamespacedKey(AvarionCore.getPlugin(), "tools"), Material.WOODEN_AXE,
          Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL,

          Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL,

          Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE, Material.STONE_SHOVEL,

          Material.IRON_AXE, Material.IRON_HOE, Material.IRON_PICKAXE, Material.IRON_SHOVEL,

          Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE,
          Material.DIAMOND_SHOVEL);

  public static final MaterialSet RANGED_WEAPON =
      new MaterialSet(new NamespacedKey(AvarionCore.getPlugin(), "rangedweapon"), Material.BOW);

  public static final MaterialSet FISHING_TOOL =
      new MaterialSet(new NamespacedKey(AvarionCore.getPlugin(), "fishingtool"), Material.FISHING_ROD);

  /**
   * Returns true if the given ItemStack is a Weapon (Sword or Axe)
   *
   * @param item The Itemstack
   * @return true if the given item is a weapon
   */
  public static boolean isWeapon(final ItemStack item) {
    return AXE.isTagged(item) || SWORDS.isTagged(item);
  }

  /**
   * Returns true if the given Item is repairable (Either a Weapon/Tool or Bow)
   *
   * @param item The Itemstack
   * @return true if the item is repairable
   */
  public static boolean isRepairable(final ItemStack item) {
    return (item.getItemMeta() instanceof Damageable);
  }

  /**
   * Returns true if the item is destroyed
   *
   * @param item
   * @param damage
   * @return
   */
  public static boolean damageItem(final ItemStack item, final int damage) {
    final Damageable dmg = (Damageable) item.getItemMeta();

    if ((dmg.getDamage() + damage) >= item.getType().getMaxDurability()) {
      item.setItemMeta((ItemMeta) dmg);
      item.setType(Material.AIR);
      return true;
    }

    dmg.setDamage(dmg.getDamage() + damage);
    item.setItemMeta((ItemMeta) dmg);
    return false;
  }

}
