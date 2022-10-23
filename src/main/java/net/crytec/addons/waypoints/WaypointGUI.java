/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.waypoints.WaypointGUI can not be copied and/or distributed without the express
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

package net.crytec.addons.waypoints;

import java.util.HashMap;
import java.util.Map.Entry;
import net.crytec.internal.CorePlayer;
import net.crytec.internal.settings.WaypointSettings;
import net.crytec.inventoryapi.anvil.AnvilGUI;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.libs.commons.utils.UtilLoc;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class WaypointGUI implements InventoryProvider {

  @Override
  public void init(final Player player, final InventoryContent contents) {

    final WaypointSettings data = CorePlayer.get(player.getUniqueId()).getData(WaypointSettings.class);

    final HashMap<String, Location> worldpoints = data.getWaypointsInWorld(player.getWorld());

    for (final Entry<String, Location> point : worldpoints.entrySet()) {

      if (!point.getValue().getWorld().equals(player.getWorld())) {
        if (point.getValue().getWorld() == null) {
          data.deleteWaypoint(point.getKey());
          player.sendMessage(F.main("Wegpunkt", "Der Wegpunkt " + point.getKey() + " wurde gelöscht da die Welt nicht mehr existiert."));
        }
        continue;
      }

      contents.add(ClickableItem.of(new ItemBuilder(Material.MAP).name("§bWegpunkt " + point.getKey())
              .lore("§7Koordinaten: §6" + UtilLoc.getLogString(point.getValue()))
              .lore("§7Entfernung: §d" + player.getLocation().distance(point.getValue()) + "§7 Blöcke.")
              .build()
          , event -> {

            if (event.getClick() == ClickType.LEFT) {
              player.setCompassTarget(point.getValue());
              player.sendMessage(F.main("Wegpunkt", "§7Dein Kompass zeigt nun auf die Kooridaten von Wegpunkt " + F.name(point.getKey())));
              player.sendMessage("§7Der Wegpunkt ist " + (int) player.getLocation().distance(point.getValue()) + "§7 Blöcke entfernt.");
              player.closeInventory();
              UtilPlayer.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 1, 1.25F);
            } else if (event.getClick() == ClickType.RIGHT) {
              data.deleteWaypoint(point.getKey());
              player.sendMessage(F.main("Wegpunkt", "§7Der Wegpunkt " + F.name(point.getKey()) + " wurde entfernt."));
              UtilPlayer.playSound(player, Sound.BLOCK_TRIPWIRE_CLICK_ON, 1, 0.85F);
              reopen(player, contents);
            }
          }));
    }

    contents.add(ClickableItem.of(new ItemBuilder(Material.BEACON).name("§2Weltspawn").build(), event -> {
      player.sendMessage(F.main("Wegpunkt", "§7Dein Kompass zeigt nun auf den Spawnpunkt der aktuellen Welt"));
      player.setCompassTarget(player.getWorld().getSpawnLocation());
      player.sendMessage("§7Der Wegpunkt ist " + (int) player.getLocation().distance(player.getWorld().getSpawnLocation()) + "§7 Blöcke entfernt.");
      player.closeInventory();
      UtilPlayer.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 1, 1.25F);
    }));

    contents.add(ClickableItem.of(new ItemBuilder(Material.COMPASS).name("§2Neuen Wegpunkt hinzufügen.").build(), event -> {

      if (worldpoints.size() >= 34) {
        player.sendMessage(F.main("Wegpunkt", "§4Du hast bereits die maximale Anzahl an Wegpunkten in dieser Welt."));
        return;
      }

      new AnvilGUI(player, "Wegpunkt", (p, v) -> {
        final String id = v.replace(".", "").replace(":", "");
        data.addWaypoint(id, player.getLocation());
        reopen(player, contents);
        player.sendMessage(F.main("Wegpunkt", "§7Du hast einen neuen Wegpunkt hinzugefügt."));
        UtilPlayer.playSound(player, Sound.ENTITY_PAINTING_PLACE, 1, 0.85F);
        return null;
      });
    }));

  }


}
