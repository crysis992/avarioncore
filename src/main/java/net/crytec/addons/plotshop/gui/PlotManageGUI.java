/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.gui.PlotManageGUI can not be copied and/or distributed without the express
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

package net.crytec.addons.plotshop.gui;

import java.util.concurrent.TimeUnit;
import net.crytec.AvarionCore;
import net.crytec.addons.plotshop.PlotShopAddon;
import net.crytec.addons.plotshop.data.ExpireablePlot;
import net.crytec.addons.plotshop.data.Plot;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlotManageGUI implements InventoryProvider {

  private final Plot plot;
  private final PlotShopAddon addon;
  private final ItemStack border = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();

  public PlotManageGUI(final PlotShopAddon addon, final Plot plot) {
    this.addon = addon;
    this.plot = plot;
  }

  @Override
  public void init(final Player player, final InventoryContent contents) {
    contents.fillBorders(ClickableItem.empty(border));

    final ItemStack members = new ItemBuilder(Material.PLAYER_HEAD).name("§7Mitglieder")
        .lore("§7Klicke hier um die Mitglieder dieser Region zu verwalten.")
        .build();
    final ItemStack ploticon = new ItemBuilder(Material.BOOKSHELF).name("§7Grundstück")
        .lore("§7Gruppe: §f" + plot.getPlotGroup().getDisplayname())
        .lore("§7Region: §f" + plot.getProtectedRegionID())
        .lore("§7Gekauft am: §f" + plot.getPurchaseDateFormatted())
        .lore("§7Läuft ab in: §f" + ((plot instanceof ExpireablePlot) ? ((ExpireablePlot) plot).getExpireDateFormatted() : "Niemals"))
        .build();

    final ItemStack extendRent = new ItemBuilder(Material.GOLD_NUGGET).name("§7Miete verlängern")
        .lore("§7Klicke  hier um die Mietdauer der Region")
        .lore("§7zu verlängern")
        .build();

    final ItemStack deleteIcon = new ItemBuilder(Material.TNT).name("§4Grundstück aufgeben")
        .lore("§4Hier kannst du dein Grundstück aufgeben")
        .lore("§4Das Grundstück wird anschließend zurückgesetzt.")
        .lore("§c§l<Coming soon>")
        .build();

    final ItemStack sellIcon = new ItemBuilder(Material.BOOKSHELF).name("Grundstück verkaufen").lore("§c§lComing soon").build();

    contents.set(0, ClickableItem.of(members, e -> {
      SmartInventory.builder().provider(new PlotMemberGUI(plot)).title("Mitglieder").size(4).build().open(player);
    }));

    contents.set(4, ClickableItem.empty(ploticon));

    if ((plot instanceof ExpireablePlot) && plot.getPlotGroup().getRentDays() > 0) {
      final ExpireablePlot expireablePlot = (ExpireablePlot) plot;
      contents.set(5, ClickableItem.of(extendRent, e -> {

        if (AvarionCore.getPlugin().getEconomy().withdrawPlayer(player, plot.getPrice()).transactionSuccess()) {

          final long newExpireDate = expireablePlot.getExpireDate() + TimeUnit.DAYS.toMillis(plot.getPlotGroup().getRentDays());

          expireablePlot.setExpireDate(newExpireDate);
          addon.getManager().savePlot(plot);
          player.sendMessage(F.main("Plots", "Neues Ablaufdatum: " + expireablePlot.getExpireDateFormatted()));
          reopen(player, contents);
        } else {
          player.sendMessage(F.error("Du hast nicht genug Geld um die Miete zu verlängern."));
        }
      }));
    }

    contents.set(8, ClickableItem.of(deleteIcon, e -> {
    }));
    contents.set(7, ClickableItem.of(sellIcon, e -> {
    }));
  }
}
