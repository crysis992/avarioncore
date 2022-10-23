/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.CosmeticGUI can not be copied and/or distributed without the express
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

package net.crytec.cosmetic;

import net.crytec.cosmetic.arrowtrails.ArrowTrailGUI;
import net.crytec.cosmetic.particle.TrailGUI;
import net.crytec.cosmetic.pets.PetGUI;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CosmeticGUI implements InventoryProvider {

  public CosmeticGUI(final CosmeticManager manager) {
    this.manager = manager;

    trails = new ItemBuilder(Material.BLAZE_POWDER).name("§7Leuchtspuren").build();
    arrowTrails = new ItemBuilder(Material.ARROW).name("§7Pfeilspuren").build();
    pets = new ItemBuilder(Material.MOOSHROOM_SPAWN_EGG).name("§7Begleiter").build();
  }

  private final CosmeticManager manager;

  private final ItemStack trails;
  private final ItemStack arrowTrails;
  private final ItemStack pets;

  @Override
  public void init(final Player player, final InventoryContent contents) {

    contents.set(SlotPos.of(0, 3), ClickableItem.of(trails, e -> {
      SmartInventory.builder().provider(new TrailGUI(manager.getTrailManager())).title("Leuchtspuren").size(6).build().open(player);
      UtilPlayer.playSound(player, Sound.BLOCK_CHEST_OPEN, 0.35F, 1.4F);
    }));

    contents.set(SlotPos.of(0, 4), ClickableItem.of(arrowTrails, e -> {
      SmartInventory.builder().provider(new ArrowTrailGUI(manager.getArrowTrailManager())).title("Pfeilspuren").size(6).build().open(player);
      UtilPlayer.playSound(player, Sound.BLOCK_CHEST_OPEN, 0.35F, 1.4F);
    }));

    contents.set(SlotPos.of(0, 5), ClickableItem.of(pets, e -> {
      SmartInventory.builder().provider(new PetGUI(manager.getPetManager())).title("Begleiter").size(6, 9).build().open(player);
      UtilPlayer.playSound(player, Sound.BLOCK_CHEST_OPEN, 0.35F, 1.4F);
    }));
  }

}
