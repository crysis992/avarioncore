/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.commands.PlotUserCommands can not be copied and/or distributed without the express
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

package net.crytec.addons.plotshop.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Optional;
import lombok.AllArgsConstructor;
import net.crytec.AvarionCore;
import net.crytec.addons.plotshop.PlotShopAddon;
import net.crytec.addons.plotshop.data.Plot;
import net.crytec.addons.plotshop.gui.PlotManageGUI;
import net.crytec.addons.plotshop.manager.PlotManager;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("plot")
@AllArgsConstructor
public class PlotUserCommands extends BaseCommand {

  private final PlotShopAddon addon;
  private final PlotManager manager;

  private final WorldGuardPlatform wgPlatform = WorldGuard.getInstance().getPlatform();


  @Default
  public void plotBase(final Player sender) {
    final RegionManager manager = wgPlatform.getRegionContainer().get(BukkitAdapter.adapt(sender.getWorld()));
    final ApplicableRegionSet regions = manager.getApplicableRegions(BukkitAdapter.asBlockVector(sender.getLocation()));

    final Optional<ProtectedRegion> region = regions.getRegions().stream().filter(rg -> rg.getOwners().contains(sender.getUniqueId()))
        .filter(this.manager::isPlotRegion).findFirst();

    if (region.isEmpty()) {
      sender.sendMessage(F.main("Plots", "Du befindest dich nicht auf deinem Grundstück."));
      return;
    }

    final Plot plot = this.manager.getPlot(region.get().getId());

    SmartInventory.builder().provider(new PlotManageGUI(addon, plot)).title("Dein Grundstück").size(2).build().open(sender);
  }

  @Subcommand("help")
  public void sendHelp(final CommandIssuer issuer, final CommandHelp help) {
    help.showHelp(issuer);
  }

//  @Subcommand("list")
//  public void plotList(final Player sender) {
//    sender.sendMessage(F.main("Plots", "Deine Grundstücke:"));
//    manager.getPlayerData(sender.getUniqueId()).getPlots().forEach(plot -> {
//      sender.sendMessage(plot.getRegionID());
//    });
//  }

  @Subcommand("kaufen")
  public void plotPurchase(final Player sender) {

    if (!addon.cacheContains(sender)) {
      sender.sendMessage(F.error("Du musst auf einem Grundstück stehen!"));
      return;
    }
    final Plot plot = addon.getCachedPurchasePlot(sender);

    if (plot == null) {
      sender.sendMessage(F.error("Diese Region steht nicht zum verkauf!"));
      return;
    }

    if (!manager.canPurchasePlot(sender, plot)) {
      return;
    }

    if (AvarionCore.getPlugin().getEconomy().withdrawPlayer(sender, plot.getPrice()).transactionSuccess()) {
      manager.purchasePlot(sender, plot);
      sender.sendMessage(F.main("Plots", "Du hast das Grundstück gekauft"));
    } else {
      sender.sendMessage(F.error("Du hast nicht genug Geld um das Grundstück zu kaufen."));
    }
  }

//  @Subcommand("info")
//  public void plotInfo(final CommandSender sender, final String id) {
//
//    final Plot plot = manager.getPlot(id);
//
//    if (plot == null) {
//      sender.sendMessage(F.error("Unbekannte Region!"));
//      return;
//    }
//
//    sender.sendMessage("§7§m===============§6 Grundstück §7§m===============");
//    sender.sendMessage("§7Gruppe: §e" + plot.getPlotGroup());
//    sender.sendMessage("§7Region: §e" + plot.getRegionID());
//    sender.sendMessage("§7Preis: §e" + plot.getPrice());
//    sender.sendMessage("§7Besitzer: §e" + ((plot.getOwner() == null) ? "Keiner" : Bukkit.getOfflinePlayer(plot.getOwner()).getName()));
//    sender.sendMessage("§7Gekauft am: §e" + plot.getPurchaseDateFormatted());
//    sender.sendMessage("§7Läuft ab: §e" + plot.getExpireDateFormatted());
//    return;
//  }

}
