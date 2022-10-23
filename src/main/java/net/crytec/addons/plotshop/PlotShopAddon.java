/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.PlotShopAddon can not be copied and/or distributed without the express
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

package net.crytec.addons.plotshop;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import net.crytec.AvarionCore;
import net.crytec.addons.Addon;
import net.crytec.addons.plotshop.commands.PlotAdminCommands;
import net.crytec.addons.plotshop.commands.PlotUserCommands;
import net.crytec.addons.plotshop.data.ExpireablePlot;
import net.crytec.addons.plotshop.data.Plot;
import net.crytec.addons.plotshop.manager.AdminBoard;
import net.crytec.addons.plotshop.manager.PlotBoard;
import net.crytec.addons.plotshop.manager.PlotManager;
import net.crytec.addons.plotshop.runnables.ExpireCheckRunnable;
import net.crytec.libs.protocol.scoreboard.api.PlayerBoardManager;
import net.crytec.util.F;
import net.crytec.util.regionevents.RegionEnteredEvent;
import net.crytec.util.regionevents.RegionLeftEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlotShopAddon extends Addon implements Listener {

  private PlayerBoardManager board;

  @Getter
  private PlotManager manager;

  private final HashMap<UUID, Plot> tempPurchaseCache = Maps.newHashMap();

  @Override
  protected void onEnable() {
    final File plotfolder = new File(AvarionCore.getPlugin().getDataFolder(), "plots");
    if (!plotfolder.exists() && plotfolder.mkdir()) {
      getPlugin().getLogger().info("Created plots Folder");
    }
    final File schematicfolder = new File(plotfolder, "templates");
    if (!schematicfolder.exists() && schematicfolder.mkdir()) {
      getPlugin().getLogger().info("Created Template Folder");
    }

    Plot.addon = this;

    board = getPlugin().getScoreBoardManager();

    manager = new PlotManager(this);
    manager.load();

    AvarionCore.getPlugin().getCommandManager().registerCommand(new PlotUserCommands(this, manager));
    AvarionCore.getPlugin().getCommandManager().registerCommand(new PlotAdminCommands(this));

    Bukkit.getScheduler().runTaskTimer(getPlugin(), new ExpireCheckRunnable(this), 100L, 6000L);

    Bukkit.getPluginManager().registerEvents(this, getPlugin());
  }

  @Override
  protected void onDisable() {

  }

  @Override
  public String getModuleName() {
    return "PlotShop";
  }

  @EventHandler
  public void onRegionEnter(final RegionEnteredEvent event) {
    if (manager.isPlotRegion(event.getRegion())) {

      final Plot plot = manager.getPlot(event.getRegion().getId());
      final Player player = event.getPlayer();

      if (plot.getOwner() == null) {
        board.setBoard(player, new PlotBoard(plot));
        tempPurchaseCache.put(player.getUniqueId(), plot);

      } else if (plot instanceof ExpireablePlot) {
        final ExpireablePlot expireablePlot = (ExpireablePlot) plot;
        if (player.getUniqueId().equals(plot.getOwner()) && expireablePlot.isAboutToExpire()) {
          player.sendMessage(F.main("Plot", "Dein Grundstück wird in " + F.name(expireablePlot.getExpireHours() + " Stunden") + " ablaufen."));
        }
      }

      if (plot.getOwner() != null && event.getPlayer().hasPermission("plot.mod")) {
        board.setBoard(player, new AdminBoard(manager.getPlot(event.getRegion().getId())));
      }
    }
  }

  @EventHandler
  public void onRegionEnter(final RegionLeftEvent event) {
    if (manager.isPlotRegion(event.getRegion())) {
      board.resetBoard(event.getPlayer());
      tempPurchaseCache.remove(event.getPlayer().getUniqueId());
    }
  }

  public boolean cacheContains(final Player player) {
    return tempPurchaseCache.containsKey(player.getUniqueId());
  }

  public Plot getCachedPurchasePlot(final Player player) {
    return tempPurchaseCache.get(player.getUniqueId());
  }

}