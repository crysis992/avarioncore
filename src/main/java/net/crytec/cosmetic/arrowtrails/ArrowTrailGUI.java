/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.arrowtrails.ArrowTrailGUI can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.arrowtrails;

import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ArrowTrailGUI implements InventoryProvider {

  private final ArrowTrail addon;

  public ArrowTrailGUI(final ArrowTrail addon) {
    this.addon = addon;
  }

  @Override
  public void init(final Player player, final InventoryContent content) {

    content.set(SlotPos.of(5, 8), ClickableItem.of(new ItemBuilder(Material.REDSTONE_BLOCK).name("§cDeaktivieren").build(), e -> {
      if (!addon.hasActiveTrail(player)) {
        player.sendMessage(F.main("Arrow Trail", "Du hast aktuell keine aktive Pfeil-Leuchtspur!"));
        return;
      }
			addon.deactivateTrail(player);
      player.closeInventory();
      player.sendMessage(F.main("Arrow Trail", "Pfeil-Leuchtspur deaktiviert!"));
    }));

    for (final ArrowTrailData data : addon.getTrailData()) {

      ItemStack item = data.getIcon().clone();

      if (addon.getActiveTrail(player) == data) {
        item = new ItemBuilder(item).enchantment(Enchantment.ARROW_INFINITE).setItemFlag(ItemFlag.HIDE_ENCHANTS).build();
      }

      content.add(ClickableItem.of(item, e -> {
        if (player.hasPermission(data.getPermission())) {
					addon.setActiveTrail(player, data);
          player.sendMessage(F.main("Pfeilspur", "Die Leuchtspur " + F.name(data.getName()) + "  wurde aktiviert."));
          player.closeInventory();
        }
      }));
    }
  }
}