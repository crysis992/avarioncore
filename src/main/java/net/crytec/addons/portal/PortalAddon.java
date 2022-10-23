/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.portal.PortalAddon can not be copied and/or distributed without the express
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Maps;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import net.crytec.AvarionCore;
import net.crytec.addons.Addon;
import net.crytec.internal.data.serializer.modules.LocationModule;
import org.bukkit.Bukkit;

public class PortalAddon extends Addon {

  private final HashMap<ProtectedRegion, Portal> portals = Maps.newHashMap();
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void onEnable() {
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.registerModules(new ParameterNamesModule(), new LocationModule());

    try {
      loadData();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    AvarionCore.getPlugin().getCommandManager().registerCommand(new PortalCommands(this));
    Bukkit.getPluginManager().registerEvents(new PortalListener(this), getPlugin());
  }

  @Override
  protected void onDisable() {
    try {
      saveData();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getModuleName() {
    return "Portals";
  }

  public Collection<Portal> getAllPortals() {
    return portals.values();
  }

  public boolean isPortalRegion(final ProtectedRegion region) {
    return portals.containsKey(region);
  }

  public Portal getPortal(final ProtectedRegion region) {
    return portals.get(region);
  }

  public void removePortal(final ProtectedRegion region) {
    portals.remove(region);
  }

  public void addPortal(final ProtectedRegion region, final Portal portal) {
    portals.put(region, portal);
  }

  private void loadData() throws IOException {
    final File file = new File(getPlugin().getDataFolder(), "portals.json");
    if (!file.exists()) {
      return;
    }

    final CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, Portal.class);
    final List<Portal> portals = mapper.readValue(file, typeReference);

    for (final Portal portal : portals) {
      System.out.println("Loaded portal " + portal.getProtectedRegion());
      this.portals.put(portal.getWgRegion(), portal);
    }
  }

  private void saveData() throws IOException {
    final List<Portal> tmp = new ArrayList<>(portals.values());

    if (tmp.isEmpty()) {
      return;
    }

    final File file = new File(getPlugin().getDataFolder(), "portals.json");
    if (!file.exists() && file.createNewFile()) {
      getPlugin().getLogger().info("Created portals.json");
    }

    mapper.writeValue(file, tmp);
  }
}