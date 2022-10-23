/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.portal.gui.PortalEditor can not be copied and/or distributed without the express
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
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import net.crytec.util.PlayerChatInput;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PortalEditor implements InventoryProvider {

  private final PortalAddon addon;
  private final Portal portal;

  public PortalEditor(final PortalAddon addon, final Portal portal) {
    this.addon = addon;
    this.portal = portal;
  }

  @Override
  public void init(final Player player, final InventoryContent contents) {

    contents.set(0, ClickableItem.of(new ItemBuilder(Material.BOOK).name("§fName bearbeiten").build(), e -> {

      player.closeInventory();
      player.sendMessage(F.main("Portal", "Bitte gebe einen neuen Namen in den Chat ein:"));
      PlayerChatInput.get(player, name -> {
        portal.setDisplayname(name);
        reopen(player, contents);
      });

    }));

    if (!portal.isBungeePortal()) {

      contents.set(1, ClickableItem.of(new ItemBuilder(Material.ENDER_PEARL).name("§fZielort festlegen").build(), e -> {

        portal.setDestination(player.getLocation());
        player.sendMessage(F.main("Portal", "Du hast den Zielort für das Portal aktualisiert."));
        reopen(player, contents);
      }));
    }

    contents.set(2, ClickableItem.of(new ItemBuilder(Material.ANVIL).name("§fPermission setzen").build(), e -> {

      if (e.isLeftClick()) {

        player.closeInventory();
        player.sendMessage(F.main("Portal", "Bitte gebe die Permission ein:"));
        PlayerChatInput.get(player, permission -> {
          portal.setPermission(permission);
          reopen(player, contents);
        });
      } else {
        portal.setPermission("");
        player.sendMessage(F.main("Portal", "Die Permission wurde entfernt."));
      }

    }));

    contents.set(3, ClickableItem.of(new ItemBuilder(Material.REDSTONE).name("§fBungee Portal").lore("§7Bungeecord: " + F.tf(portal.isBungeePortal())).build(), e -> {

      if (portal.isBungeePortal()) {
        portal.setBungeePortal(false);
      } else {
        portal.setBungeePortal(true);
      }
      reopen(player, contents);
    }));

    if (portal.isBungeePortal()) {
      contents.set(4, ClickableItem.of(new ItemBuilder(Material.ENDER_PEARL).name("§fZielserver setzen").build(), e -> {

        player.closeInventory();
        player.sendMessage(F.main("Portal", "Bitte gebe den Zielserver ein:"));
        PlayerChatInput.get(player, server -> {
          portal.setServer(server);
          reopen(player, contents);
        });
      }));
    }

  }
}
