/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.waypoints.WaypointAddon can not be copied and/or distributed without the express
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

import net.crytec.addons.Addon;
import net.crytec.internal.CorePlayer;
import net.crytec.internal.settings.WaypointSettings;
import net.crytec.inventoryapi.SmartInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class WaypointAddon extends Addon implements Listener {

  @Override
  protected void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, getPlugin());
  }

  @Override
  protected void onDisable() {
  }

  @Override
  public String getModuleName() {
    return "Waypoints";
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onCompassInteract(final PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (event.getItem() == null || event.getItem().getType() != Material.COMPASS) {
      return;
    }
    if (!event.getPlayer().isSneaking() || event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    event.setUseInteractedBlock(Result.DENY);
    event.setUseItemInHand(Result.DENY);

    final WaypointSettings data = CorePlayer.get(event.getPlayer().getUniqueId()).getData(WaypointSettings.class);

    SmartInventory.builder().provider(new WaypointGUI()).title("Deine Wegpunkte (" + data.getWaypoints().size() + ") ").size(5).build()
        .open(event.getPlayer());
  }
}