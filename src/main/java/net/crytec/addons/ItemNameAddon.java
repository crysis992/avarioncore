/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.ItemNameAddon can not be copied and/or distributed without the express
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

package net.crytec.addons;

import net.crytec.libs.commons.utils.language.LanguageAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemNameAddon extends Addon implements Listener {

  @Override
  protected void onEnable() {
  }

  @Override
  protected void onDisable() {
  }

  @Override
  public String getModuleName() {
    return "ItemNames";
  }

  @EventHandler
  public void itemMerge(final ItemMergeEvent e) {
    final Item item = e.getTarget();
    final Item i = e.getEntity();
    final int a = i.getItemStack().getAmount() + item.getItemStack().getAmount();
    final String count = " x " + a;
    item.setCustomName(pickName(item) + ChatColor.translateAlternateColorCodes('&', count));
  }

  @EventHandler
  public void spawnItem(final PlayerDropItemEvent e) {
    final Item item = e.getItemDrop();
    item.setCustomName(pickName(item));
    item.setCustomNameVisible(true);
    if (item.getItemStack().getAmount() > 1) {
      final int a = item.getItemStack().getAmount();
      final String count = " x " + a;
      item.setCustomName(item.getCustomName() + ChatColor.translateAlternateColorCodes('&', count));
    }
  }

  @EventHandler
  public void spawnItem(final ItemSpawnEvent e) {
    final Item item = e.getEntity();
    item.setCustomName(pickName(item));
    item.setCustomNameVisible(true);
    if (item.getItemStack().getAmount() > 1) {
      final int a = item.getItemStack().getAmount();
      final String count = " x " + a;
      item.setCustomName(item.getCustomName() + ChatColor.translateAlternateColorCodes('&', count));
    }
  }

  public String pickName(final Item item) {
    return LanguageAPI.getItemName(item.getItemStack());
  }

}
