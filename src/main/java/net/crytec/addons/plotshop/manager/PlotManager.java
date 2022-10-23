/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.manager.PlotManager can not be copied and/or distributed without the express
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

package net.crytec.addons.plotshop.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Maps;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import net.crytec.AvarionCore;
import net.crytec.addons.plotshop.PlotShopAddon;
import net.crytec.addons.plotshop.data.ExpireablePlot;
import net.crytec.addons.plotshop.data.Plot;
import net.crytec.addons.plotshop.data.PlotGroup;
import net.crytec.addons.plotshop.events.PlotExpireEvent;
import net.crytec.addons.plotshop.events.PlotPurchaseEvent;
import net.crytec.addons.plotshop.events.PlotRentEvent;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.util.F;
import net.crytec.util.WorldEditUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotManager {

  private final PlotShopAddon addon;

  private final File schematicFolder;
  private final File plotfolder;

  private final HashMap<String, Plot> plots = Maps.newHashMap();

  private final ObjectMapper mapper;

  public PlotManager(final PlotShopAddon addon) {
    this.addon = addon;
    plotfolder = new File(AvarionCore.getPlugin().getDataFolder(), "plots");
    schematicFolder = new File(plotfolder, "templates");

    mapper = new ObjectMapper();
    mapper.registerModules(new ParameterNamesModule());
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

  }

  public boolean canPurchasePlot(final Player player, final Plot plot) {
    if (plot.hasOwner()) {
      player.sendMessage(F.error("Dieses Grundstück hat bereits einen Besitzer."));
      return false;
    }

    if (!player.hasPermission(plot.getPlotGroup().getPermission())) {
      player.sendMessage(F.error("Du hast nicht die benötigten Rechte um dieses Grundstück zu kaufen."));
      return false;
    }
    final long plotAmount = plots.values().stream().filter(check -> check.getWorld().equals(player.getWorld().getUID()) && check.hasOwner() && check.getOwner().equals(player.getUniqueId())).count();

    if (plotAmount >= plot.getPlotGroup().getPurchaseLimit()) {
      player.sendMessage(F.error("Du hast bereits die maximale Anzahl an Grundstücken von diesem Typ."));
      return false;
    } else {
      return true;
    }
  }

  public void expireCheck() {
    for (final Plot plot : plots.values()) {
      if (!(plot instanceof ExpireablePlot)) {
        continue;
      }

      final ExpireablePlot expireablePlot = (ExpireablePlot) plot;

      if (expireablePlot.isExpired()) {
        new PlotExpireEvent(plot).callEvent();
        resetPlot(plot);
        addon.getPlugin().getLogger().info("Grundstück " + plot.getProtectedRegionID() + " ist abgelaufen und wurde zurückgesetzt.");
      }
    }
  }

  public boolean isPlotRegion(final ProtectedRegion region) {
    return plots.containsKey(region.getId());
  }

  public void purchasePlot(final Player player, final Plot plot) {
    plot.setOwner(player.getUniqueId());
    plot.setPurchaseDate(System.currentTimeMillis());
    plot.getRegion().getOwners().addPlayer(player.getUniqueId());

    if (plot instanceof ExpireablePlot) {
      final ExpireablePlot expireablePlot = (ExpireablePlot) plot;

      final long expireDate = UtilTime.LocalDateToMillis(LocalDateTime.now().plusDays(plot.getPlotGroup().getRentDays()));
      expireablePlot.setExpireDate(expireDate);
      player.sendMessage(F.main("Plots", "Du hast das Grundstück bis zum " + expireablePlot.getExpireDateFormatted() + " gemietet."));
      new PlotRentEvent(plot, player).callEvent();
    } else {
      new PlotPurchaseEvent(plot).callEvent();
    }
    savePlot(plot);
  }

  public void resetPlot(final Plot plot) {
    plot.setOwner(null);
    plot.setPurchaseDate(0);

    plot.getRegion().getMembers().removeAll();
    plot.getRegion().getOwners().removeAll();
    savePlot(plot);
    final Region region = new CuboidRegion(plot.getRegion().getMinimumPoint(), plot.getRegion().getMaximumPoint());
    WorldEditUtil.restoreRegionBlocks(new File(schematicFolder, plot.getProtectedRegionID()), region, Bukkit.getWorld(plot.getWorld()));

    if (plot instanceof ExpireablePlot) {
      new PlotExpireEvent(plot).callEvent();
    }
  }

  public void deletePlot(final Plot plot) {
    plots.remove(plot.getProtectedRegionID());

    final File file = new File(plotfolder, plot.getProtectedRegionID() + ".yml");
    final File schematic = new File(schematicFolder, plot.getProtectedRegionID() + ".schem");

    if (!file.delete() || !schematic.delete()) {
      addon.getPlugin().getLogger().warning("Failed to delete plot datafiles for " + plot.getProtectedRegionID());
    }

    plot.getRegion().getOwners().removeAll();
    plot.getRegion().getMembers().removeAll();

    if (plot instanceof ExpireablePlot) {
      new PlotExpireEvent(plot).callEvent();
    }
  }

  public void savePlot(final Plot plot) {
    final File file = new File(plotfolder, plot.getProtectedRegionID() + ".json");
    try {
      mapper.writeValue(file, plot);
    } catch (final IOException e) {
      addon.getPlugin().getLogger().severe("Failed to save Plot " + plot.getProtectedRegionID() + ".json");
      e.printStackTrace();
    }
  }

  public void registerPlot(final Plot plot) {
    final Region region = new CuboidRegion(plot.getRegion().getMinimumPoint(), plot.getRegion().getMaximumPoint());
    WorldEditUtil.saveRegionBlocks(region, new File(schematicFolder, plot.getProtectedRegionID()), Bukkit.getWorld(plot.getWorld()));
    plots.put(plot.getProtectedRegionID(), plot);
    savePlot(plot);
  }

  public Plot getPlot(final String regionID) {
    return plots.get(regionID);
  }

  public Collection<Plot> getPlotMap() {
    return plots.values();
  }


  public void load() {
    final Collection<File> plots = FileUtils.listFiles(plotfolder, new String[]{"json"}, false);
    addon.getPlugin().getLogger().info("Loading " + plots.size() + " plots");

    for (final File plotFile : plots) {

      try {
        final Plot plot = mapper.readValue(plotFile, Plot.class);
        this.plots.put(plot.getProtectedRegionID(), plot);
      } catch (final IOException e) {
        addon.getPlugin().getLogger().severe("Failed to read plot " + plotFile.getName());
        e.printStackTrace();
      }
    }

    // Import Debug

    final File old = new File(Bukkit.getWorldContainer() + File.separator + "plugins" + File.separator + "ServerManagerOLD" + File.separator + "plots");
    if (!old.exists()) {
      System.out.println("[Plots] No import data found!");
      return;
    }

    final Collection<File> plotData = FileUtils.listFiles(old, new String[]{"yml"}, false);

    for (final File data : plotData) {

      if (data.getName().contains("shop")) {
        continue;
      }

      try (final BufferedReader reader = new BufferedReader(new FileReader(data))) {
        String line = reader.readLine();

        @NotNull
        World world = null;
        UUID owner = null;
        long expiration = 0;
        long purchaseDate = 0;
        String regionID = null;
        double price = 0;
        final PlotGroup group = PlotGroup.CITY_BEGINNER;

        while (line != null) {
          final String[] lineData = line.replace(" ", "").split(":");

          switch (lineData[0]) {
            case "regionId":
              regionID = lineData[1];
              break;
            case "world":
              world = Bukkit.getWorld(lineData[1]);
              break;
            case "owner":
              owner = UUID.fromString(lineData[1]);
              break;
            case "purchaseDate":
              purchaseDate = Long.valueOf(lineData[1]);
              break;
            case "expireDate":
              expiration = Long.valueOf(lineData[1]);
              break;
            case "price":
              price = Double.valueOf(lineData[1]);
              break;
            default:
              break;
          }
          line = reader.readLine();
        }

        if (regionID.contains("shop")) {
          return;
        }

        if (this.plots.containsKey(regionID)) {
          System.out.println(regionID + " is already imported. Skipping..");
          continue;
        }

        try {
          final Plot plot = new Plot(world.getUID(), regionID, group);
          plot.setPrice(price);
          plot.setOwner(owner);
          plot.setPurchaseDate(purchaseDate);
          savePlot(plot);
        } catch (final Exception ex) {
          System.out.println("Failed to find RegionID " + regionID + " in world " + world.getName());
          continue;
        }
      } catch (final IOException e) {
        e.printStackTrace();
      }


    }

  }
}
