/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.permissionShop.data.ShopContentConfirmProvider can not be copied and/or distributed without the express
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
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class ShopContentConfirmProvider implements InventoryProvider {

  private static final Economy eco = AvarionCore.getPlugin().getEconomy();

  private final LuckPermRankShop manager;
  private final ShopNodeEntry entry;

  public ShopContentConfirmProvider(final LuckPermRankShop instance, final ShopNodeEntry entry) {
    manager = instance;
    this.entry = entry;
  }

  @Override
  public void init(final Player player, final InventoryContent contents) {
//
//    contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(" ").build()));
//
//    contents.set(SlotPos.of(1, 6), ClickableItem.of(new ItemBuilder(Material.EMERALD).name("Bestätigen")
//        .lore("§7Dir werden nach der Bestätigung")
//        .lore("§6" + entry.getPrice() + " " + eco.currencyNamePlural() + "§7 abgezogen.")
//        .build(), e -> {
//
//      player.closeInventory();
//
//      if (!eco.withdrawPlayer(player, entry.getPrice()).transactionSuccess()) {
//        player.sendMessage(F.error("Du hast nicht genug Geld."));
//        return;
//      }
//
//      final User user = manager.getApi().getUser(player.getUniqueId());
//
//      for (final Node node : entry.getNodes()) {
//
//        if (node.isMeta()) {
//          final Optional<Node> tr = user.getOwnNodes().stream().filter(n -> n.isMeta()).filter(n -> n.getMeta().getKey().equals(node.getMeta().getKey())).findFirst();
//          if (tr.isPresent()) {
//            user.unsetPermission(tr.get());
//          }
//        }
//
//        final DataMutateResult result = user.setPermission(node);
//
//        if (result.asBoolean()) {
//          player.sendMessage(F.main("Info", "Du hast dir " + F.name(entry.getDisplayname()) + " gekauft!"));
//        } else {
//          player.sendMessage(F.error("Oops! Es ist etwas schief gelaufen."));
//        }
//      }
//
//			manager.getApi().getUserManager().saveUser(user);
//    }));
//
//    contents.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.BARRIER).name("§cAbbrechen").build(), e -> {
//      SmartInventory.builder().size(5, 9).title("Permission Shop").provider(new ShopInventoryProvider(manager)).build().open(player);
//    }));

  }
}