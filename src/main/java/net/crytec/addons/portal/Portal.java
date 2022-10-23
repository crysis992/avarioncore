/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.portal.Portal can not be copied and/or distributed without the express
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

package net.crytec.addons.portal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Portal {

  private final String protectedRegion;
  private final UUID worldID;

  private Location destination;
  private String permission;
  private String displayname;

  private boolean isBungeePortal = false;
  private String server = "local";

  @JsonIgnore
  private final ProtectedRegion wgRegion;

  public Portal(final World world, final ProtectedRegion region) {
    protectedRegion = region.getId();
    worldID = world.getUID();
    wgRegion = region;
  }

  @JsonCreator()
  public Portal(final UUID worldID, final String protectedRegion) {
    this.worldID = worldID;
    this.protectedRegion = protectedRegion;

    final World world = Bukkit.getWorld(worldID);
    if (world == null) {
      Bukkit.getLogger().severe("Failed to load portal " + protectedRegion + " because World with ID " + worldID + " is not loaded!");
      throw new IllegalArgumentException("Failed to load portal");
    }

    final RegionManager container = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));

    if (!container.hasRegion(protectedRegion)) {
      throw new IllegalArgumentException("WorldGuard region " + protectedRegion + " does no longer exist!");
    }
    wgRegion = container.getRegion(protectedRegion);
  }

  public String getProtectedRegion() {
    return protectedRegion;
  }

  public UUID getWorldID() {
    return worldID;
  }

  public Location getDestination() {
    return destination;
  }

  public void setDestination(final Location destination) {
    this.destination = destination;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(final String permission) {
    this.permission = permission;
  }

  public String getDisplayname() {
    return displayname;
  }

  public void setDisplayname(final String displayname) {
    this.displayname = displayname;
  }

  public boolean isBungeePortal() {
    return isBungeePortal;
  }

  public void setBungeePortal(final boolean bungeePortal) {
    isBungeePortal = bungeePortal;
  }

  public String getServer() {
    return server;
  }

  public void setServer(final String server) {
    this.server = server;
  }

  @JsonIgnore
  public ProtectedRegion getWgRegion() {
    return wgRegion;
  }

  @JsonIgnore
  public boolean hasPermission(final Player player) {
    if (permission == null || permission.isEmpty()) {
      return true;
    } else {
      return player.hasPermission(getPermission());
    }
  }
}