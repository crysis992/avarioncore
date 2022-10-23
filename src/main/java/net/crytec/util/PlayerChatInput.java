/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.PlayerChatInput can not be copied and/or distributed without the express
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

package net.crytec.util;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import net.crytec.AvarionCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerChatInput implements Listener {

  private static final HashMap<UUID, Consumer<String>> players = Maps.newHashMap();

  public PlayerChatInput(final AvarionCore core) {
    Bukkit.getPluginManager().registerEvents(this, core);
  }

  public static void get(final Player player, final Consumer<String> result) {
    players.put(player.getUniqueId(), result);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onChat(final AsyncPlayerChatEvent event) {
    if (players.containsKey(event.getPlayer().getUniqueId())) {
      final Consumer<String> result = players.get(event.getPlayer().getUniqueId());
      players.remove(event.getPlayer().getUniqueId());
      TaskManager.runTask(() -> result.accept(event.getMessage()));
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent e) {
    players.remove(e.getPlayer().getUniqueId());
  }

}
