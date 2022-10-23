/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.gui.PlotMemberGUI can not be copied and/or distributed without the express
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

package net.crytec.addons.plotshop.gui;

import net.crytec.addons.plotshop.data.Plot;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlotMemberGUI implements InventoryProvider {

  private final Plot plot;
  private final ItemStack border = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();

  public PlotMemberGUI(final Plot plot) {
    this.plot = plot;
  }

  @Override
  public void init(final Player player, final InventoryContent contents) {
//    contents.fillBorders(ClickableItem.empty(border));
//
//    final SkullCache cache = AvarionCore.getPlugin().getSkullCache();
//
//    for (final UUID uuid : plot.getRegion().getMembers().getUniqueIds()) {
//      final ItemStack skull = new ItemBuilder(cache.getSkull(uuid).clone()).name(cache.getName(uuid)).lore("§fKlicke um den Spieler zu entfernen.").build();
//
//      contents.add(ClickableItem.of(skull, e -> {
//        plot.getRegion().getMembers().removePlayer(uuid);
//        player.sendMessage(F.main("Plots", "Du hast die Baurechte von " + F.name(cache.getName(uuid)) + " entfernt."));
//        UtilPlayer.playSound(player, Sound.ENTITY_LEASH_KNOT_BREAK, 0.8F, 1.25F);
//        reopen(player, contents);
//      }));
//
//    }
//
//    contents.set(SlotPos.of(3, 4), new
//
//        InputButton(new ItemBuilder(Material.EMERALD).
//
//        name("§2Spieler hinzufügen").
//
//        build(), "Spieler", input ->
//
//    {
//
//      if (!cache.isPlayerKnown(input)) {
//        player.sendMessage(F.error("Es wurde kein Spieler mit diesem Nickname gefunden."));
//        UtilPlayer.playSound(player, Sound.ENTITY_LEASH_KNOT_BREAK, 0.8F, 1.25F);
//        return;
//      }
//
//      final UUID uuid = cache.getUUID(input);
//      final DefaultDomain members = plot.getRegion().getMembers();
//      members.addPlayer(uuid);
//      plot.getRegion().setMembers(members);
//
//      player.sendMessage(F.main("Plots", "Du hast " + F.name(input) + " Baurechte auf diesem Grundstück gegeben."));
//      UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.8F, 1.25F);
//      reopen(player, contents);
//    }))
//
//  }
  }
}