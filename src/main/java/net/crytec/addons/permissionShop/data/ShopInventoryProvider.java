/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.permissionShop.data.ShopInventoryProvider can not be copied and/or distributed without the express
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

package net.crytec.addons.permissionShop.data;

import net.crytec.AvarionCore;
import net.crytec.addons.permissionShop.LuckPermRankShop;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class ShopInventoryProvider implements InventoryProvider {

  private static final Economy eco = AvarionCore.getPlugin().getEconomy();

  private final LuckPermRankShop manager;

  public ShopInventoryProvider(final LuckPermRankShop instance) {
		manager = instance;
  }

  @Override
  public void init(final Player player, final InventoryContent contents) {

    for (final ShopNodeEntry entry : manager.getShopContent()) {

      final ItemBuilder builder = new ItemBuilder(entry.getIcon()).name(entry.getDisplayname()).lore(entry.getDescription());
      builder.setItemFlag(ItemFlag.HIDE_ATTRIBUTES);
      builder.setItemFlag(ItemFlag.HIDE_ENCHANTS);

      if (entry.getModelData() > 0) {
        builder.setModelData(entry.getModelData());
      }

      if (manager.hasPurchased(player, entry)) {
        builder.lore("");
        builder.lore("§2Bereits gekauft");
        builder.enchantment(Enchantment.ARROW_INFINITE);
        contents.add(ClickableItem.empty(builder.build()));
      } else {

        if (entry.getRequired() != null && manager.getByID(entry.getRequired()).isPresent() && !manager.hasPurchased(player, manager.getByID(entry.getRequired()).get())) {
          builder.lore("§cDieses Paket benötigt zuvor das " + F.name(manager.getByID(entry.getRequired()).get().getDisplayname()) + "§c Paket.");
          contents.add(ClickableItem.empty(builder.build()));
          continue;
        }

        if (eco.has(player, entry.getPrice())) {
          contents.add(ClickableItem.of(builder.build(), e -> SmartInventory.builder().size(3, 9).title("Bestätigen").provider(new ShopContentConfirmProvider(manager, entry)).build().open(player)));

        } else {

          builder.lore("§cDu hast nicht genug " + eco.currencyNameSingular());
          contents.add(ClickableItem.empty(builder.build()));

        }
      }

    }

  }
}