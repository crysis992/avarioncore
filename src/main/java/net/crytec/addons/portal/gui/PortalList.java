/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.portal.gui.PortalList can not be copied and/or distributed without the express
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

package net.crytec.addons.portal.gui;

import net.crytec.addons.portal.Portal;
import net.crytec.addons.portal.PortalAddon;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.libs.commons.utils.UtilLoc;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PortalList implements InventoryProvider {

  private final PortalAddon addon;

  public PortalList(final PortalAddon addon) {
    this.addon = addon;
  }

  @Override
  public void init(final Player player, final InventoryContent contents) {

    for (final Portal portal : addon.getAllPortals()) {

      contents.add(ClickableItem.of(new ItemBuilder(Material.PRISMARINE_SHARD).name("§f" + portal.getDisplayname())
              .lore("§7Displayname: §6" + portal.getDisplayname())
              .lore("§7Zielort: §6" + ((portal.getDestination() == null) ? "§cNicht definiert" : UtilLoc.getLogString(portal.getDestination())))
              .lore("§7PortalRegion: §6" + portal.getProtectedRegion())
              .build()
          , e -> {
            SmartInventory.builder().provider(new PortalEditor(addon, portal)).title("Portal bearbeiten").size(4, 9).build()
                .open(player);
          }));
    }
  }
}
