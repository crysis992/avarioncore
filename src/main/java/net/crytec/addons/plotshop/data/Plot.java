/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.data.Plot can not be copied and/or distributed without the express
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

package net.crytec.addons.plotshop.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.UUID;
import net.crytec.addons.plotshop.PlotShopAddon;
import net.crytec.libs.commons.utils.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class Plot {

  @JsonIgnore
  public static PlotShopAddon addon;

  private final String protectedRegionID;
  private final UUID world;
  private final PlotGroup plotGroup;
  private double price = 0;

  private UUID owner;
  private long purchaseDate = 0;

  private final ProtectedRegion region;

  public Plot(final World world, final ProtectedRegion region, final PlotGroup plotGroup) {
    protectedRegionID = region.getId();
    this.region = region;
    this.world = world.getUID();
    this.plotGroup = plotGroup;
  }

  @JsonCreator
  public Plot(final UUID world, final String protectedRegionID, final PlotGroup plotGroup) {
    final World bukkitWorld = Bukkit.getWorld(world);
    if (bukkitWorld == null) {
      throw new IllegalArgumentException("Failed to load Plot " + protectedRegionID + " because the world is not loaded.");
    }
    if (plotGroup == null) {
      throw new IllegalArgumentException("Failed to load Plot " + protectedRegionID + " because the PlotGroup is no longer valid!");
    }
    final RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(bukkitWorld));
    if (manager == null) {
      throw new IllegalArgumentException("Failed to load Plot " + protectedRegionID + " because WorldGuard RegionManager does not exist.");
    }

    if (!manager.hasRegion(protectedRegionID)) {
      throw new IllegalArgumentException("Failed to load Plot " + protectedRegionID + " because WorldGuard has no region with the given name in this world.");
    }

    this.protectedRegionID = protectedRegionID;
    this.world = world;
    this.plotGroup = plotGroup;
    region = manager.getRegion(protectedRegionID);
  }


  public boolean hasOwner() {
    return (owner != null);
  }

  @JsonIgnore
  public String getPurchaseDateFormatted() {
    if (purchaseDate == 0) {
      return "Nicht verkauft";
    }
    return UtilTime.when(purchaseDate);
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(final double price) {
    this.price = price;
  }

  public UUID getOwner() {
    return owner;
  }

  public void setOwner(final UUID owner) {
    this.owner = owner;
  }

  public long getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(final long purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  public String getProtectedRegionID() {
    return protectedRegionID;
  }

  @JsonIgnore
  public ProtectedRegion getRegion() {
    return region;
  }

  public UUID getWorld() {
    return world;
  }

  public PlotGroup getPlotGroup() {
    return plotGroup;
  }
}