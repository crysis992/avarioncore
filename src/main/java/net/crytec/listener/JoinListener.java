/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.listener.JoinListener can not be copied and/or distributed without the express
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

package net.crytec.listener;

import net.crytec.AvarionCore;
import net.crytec.libs.protocol.scoreboard.api.PlayerBoardManager;
import net.crytec.manager.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

  private final PlayerBoardManager boardManager;
  private final PermissionManager permManager;

  private final NamespacedKey playerID;
  private final AvarionCore plugin;

  public JoinListener(final AvarionCore plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
    boardManager = plugin.getScoreBoardManager();
    permManager = plugin.getPermissionManager();

    this.plugin = plugin;
    playerID = new NamespacedKey(plugin, "playerID");
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void blockJoinMessage(final PlayerJoinEvent event) {
    event.setJoinMessage(null);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void firstJoin(final PlayerJoinEvent e) {
    final Player p = e.getPlayer();

    if (p.getGameMode() != GameMode.SURVIVAL && !p.hasPermission("ct.gamemode")) {
      p.setGameMode(GameMode.SURVIVAL);
    }

    if (p.getGameMode() == GameMode.SURVIVAL && p.hasPermission("ct.fly") && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
      p.setAllowFlight(true);
      p.setFlying(true);
    }

    boardManager.addPlayer(p, permManager.getPriority(p));
    boardManager.setPrefix(p, permManager.getPlayerPrefix(p));
  }

  @EventHandler(priority = EventPriority.LOW)
  public void removeQuitMessage(final PlayerQuitEvent e) {
    e.setQuitMessage(null);
  }
}