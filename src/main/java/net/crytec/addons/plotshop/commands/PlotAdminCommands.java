/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.commands.PlotAdminCommands can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crytec.addons.plotshop.PlotShopAddon;
import net.crytec.addons.plotshop.data.Plot;
import net.crytec.addons.plotshop.data.PlotGroup;
import net.crytec.util.F;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@CommandAlias("plot")
public class PlotAdminCommands extends BaseCommand {

  public PlotAdminCommands(final PlotShopAddon addon) {
    this.addon = addon;
  }

  private final PlotShopAddon addon;
  private BukkitTask task;

  @Subcommand("admin delete")
  @CommandPermission("ct.plot.admin")
  public void plotAdminDelete(final Player sender, final String id) {
    if (addon.getManager().getPlot(id) == null) {
      sender.sendMessage(F.error(F.name(id) + " existiert nicht."));
    }

    final Plot plot = addon.getManager().getPlot(id);
    addon.getManager().deletePlot(plot);
  }

  @Subcommand("admin info")
  @CommandCompletion("@protectedRegion")
  public void sendInfo(final Player sender, final ProtectedRegion region) {
    if (addon.getManager().getPlot(region.getId()) == null) {
      sender.sendMessage(F.error("Diese Region ist nicht als Grundstück registriert!"));
      return;
    }
    final Plot plot = addon.getManager().getPlot(region.getId());

    sender.sendMessage("Region: " + plot.getProtectedRegionID());
    sender.sendMessage("Gruppe: " + plot.getPlotGroup());
    sender.sendMessage("Welt ID: " + plot.getWorld());
    sender.sendMessage("Preis: " + plot.getPrice());
  }

  @Subcommand("admin create")
  @CommandCompletion("@protectedRegion @nothing @plotgroup")
  public void plotAdminCreate(final Player sender, final ProtectedRegion region, final double price, final PlotGroup group) {

    if (addon.getManager().getPlot(region.getId()) != null) {
      sender.sendMessage(F.error("Diese Region ist bereits als Grundstück markiert!"));
      return;
    }

    final Plot plot = new Plot(sender.getWorld(), region, group);
    plot.setPrice(price);
    addon.getManager().registerPlot(plot);

    sender.sendMessage(F.main("Plots", "Die Region " + F.name(region.getId()) + " steht nun zum verkauf bereit!"));
  }

//  @Subcommand("admin bulkcreate")
//  @Syntax("<ID Pattern> <Preis> <Gruppe>")
//  @CommandPermission("ct.plot.admin")
//  public void plotAdminCreateBulk(final Player sender, final String id, final double price, final PlotGroup group) {
//
//    task = Bukkit.getScheduler().runTaskTimer(addon.getPlugin(), new Runnable() {
//
//      int i = 0;
//      int failattempts;
//
//      @Override
//      public void run() {
//        final ProtectedRegion region = addon.getWgPlatform().getRegionContainer()
//            .get(BukkitAdapter.adapt(sender.getWorld())).getRegion(id + i);
//
//        if (failattempts > 4) {
//          task.cancel();
//          sender.sendMessage(F.main("Plots", "Bulkcreation abgeschlossen. Es wurden " + i + " neue Grundstücke erstellt."));
//          return;
//        }
//
//        if (region == null) {
//          sender.sendMessage(F.error(F.name(id + i) + " ist keine WorldGuard Region!"));
//          failattempts++;
//          i++;
//          return;
//        }
//
//        if (addon.getManager().getPlot(id) != null) {
//          sender.sendMessage(F.error(F.name(id + i) + " steht bereits zum verkauf!"));
//          return;
//        }
//
//        final Plot plot = new Plot(sender.getWorld(), region, group);
//        plot.setPrice(price);
//        addon.getManager().registerPlot(plot);
//
//        sender.sendMessage(F.main("Plots", "Die Region " + F.name(id + i) + " steht nun zum verkauf bereit!"));
//        i++;
//      }
//
//    }, 20, 15);
//  }

  @Subcommand("admin reset")
  @CommandPermission("ct.plot.admin")
  public void plotRemove(final Player sender, final String id) {

    final Plot plot = addon.getManager().getPlot(id);

    if (plot == null) {
      sender.sendMessage(F.error("Unbekannte Region!"));
    }

    addon.getManager().resetPlot(plot);
    sender.sendMessage(F.main("Plots", "Du hast das Grundstück gelöscht"));
  }
}