/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.portal.PortalCommands can not be copied and/or distributed without the express
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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.AllArgsConstructor;
import net.crytec.addons.portal.gui.PortalList;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("portal")
@CommandPermission("ct.portal")
@AllArgsConstructor
public class PortalCommands extends BaseCommand {

  private final PortalAddon addon;

  @Default
  public void sendHelp(final Player issuer, final CommandHelp help) {
    SmartInventory.builder().provider(new PortalList(addon)).title("Registrierte Portale").size(4, 9).build().open(issuer);
  }

  @Subcommand("create")
  public void createPortal(final Player sender, @Single final String portalname, @Single final String regionID) {
    final ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(sender.getWorld())).getRegion(regionID);

    if (region == null) {
      sender.sendMessage("Es existiert keine WorldGuard Region mit der ID " + F.name(regionID));
    }

    final Portal portal = new Portal(sender.getWorld(), region);
    portal.setDisplayname(portalname);
    portal.setDestination(sender.getLocation());
    addon.addPortal(region, portal);

    sender.sendMessage(F.main("Portal", "Das Portal wurde erfolgreich erstellt."));
    sender.sendMessage(F.main("Portal", "Nutze nun " + F.name("/portal") + " um das Portal zu konfigurieren."));
  }

  @Subcommand("delete")
  public void deletePortal(final Player sender, @Single final String regionID) {
    final ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(sender.getWorld())).getRegion(regionID);

    if (region == null) {
      sender.sendMessage("Es existiert keine WorldGuard Region mit der ID " + F.name(regionID));
    }

    if (!addon.isPortalRegion(region)) {
      sender.sendMessage("Die angegebene Region ist eine Portal Region [" + F.name(regionID) + "]");
    }

    final Portal portal = addon.getPortal(region);

    addon.removePortal(region);

    sender.sendMessage(F.main("Portal", "Das Portal wurde gelöscht."));
  }

}
