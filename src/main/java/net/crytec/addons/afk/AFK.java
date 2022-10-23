/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.afk.AFK can not be copied and/or distributed without the express
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

package net.crytec.addons.afk;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import net.crytec.addons.Addon;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.libs.protocol.scoreboard.api.PlayerBoardManager;
import net.crytec.util.F;
import net.crytec.util.PermissionRegistrar;
import net.crytec.util.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class AFK extends Addon implements Runnable, Listener {

  private PlayerBoardManager boardManager;

  private final HashMap<Player, Pair<Float, Long>> lastMovePosition = Maps.newHashMap();
  private final HashMap<Player, Long> afkTime = Maps.newHashMap();

  // Configuration
  private long afkAfter = TimeUnit.MINUTES.toMillis(5);
  private long kickAfter = TimeUnit.MINUTES.toMillis(10);


  private final Permission bypassPerm = PermissionRegistrar.addPermission("ct.afk.bypass", "Umgeht den AFK Modus", PermissionDefault.OP);

  @Override
  public String getModuleName() {
    return "AFK";
  }


  @Override
  protected void onEnable() {

    setConfigEntry("options.afkAfter", 5);
    setConfigEntry("options.kickAfter", 10);

    Bukkit.getPluginManager().registerEvents(this, getPlugin());

    afkAfter = TimeUnit.MINUTES.toMillis(getConfig().getInt("options.afkAfter", 5));
    kickAfter = TimeUnit.MINUTES.toMillis(getConfig().getInt("options.kickAfter", 10));

    boardManager = getPlugin().getUtilManager().getScoreboardAPI().getBoardManager();

    getPlugin().getCommandManager().registerCommand(new AFKCommand(this));

    for (final Player player : Bukkit.getOnlinePlayers()) {
      lastMovePosition.put(player, Pair.of(player.getLocation().getYaw(), System.currentTimeMillis()));
    }

    Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), this, 100, 100);
  }

  @Override
  protected void onDisable() {
    afkTime.keySet().iterator().forEachRemaining(player -> setAFK(player, false));
  }


  @EventHandler
  public void loadCache(final PlayerJoinEvent event) {
    lastMovePosition.put(event.getPlayer(), Pair.of(event.getPlayer().getLocation().getYaw(), System.currentTimeMillis()));
  }

  @EventHandler
  public void unloadCache(final PlayerQuitEvent event) {
    lastMovePosition.remove(event.getPlayer());
    afkTime.remove(event.getPlayer());
  }

  @EventHandler
  public void updateOnChat(final AsyncPlayerChatEvent event) {
    updateAFK(event.getPlayer());
  }

  @EventHandler
  public void blockFish(final PlayerFishEvent event) {
    if (isAFK(event.getPlayer()) && event.getState() == State.CAUGHT_FISH) {
      event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 1, 0.85F);
      event.getPlayer().sendActionBar("§cDu kannst im AFK Modus nicht mit der Welt interagieren.");
      event.setCancelled(true);
      event.setExpToDrop(0);
    }
  }

  @EventHandler
  public void blockInteract(final PlayerInteractEvent event) {
    if (isAFK(event.getPlayer())) {
      event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 1, 0.85F);
      event.getPlayer().sendActionBar("§cDu bist als 'AFK' markiert und kannst nicht mit der Umgebung interagieren.");
      event.setCancelled(true);
    }
  }

  long getAFKTime(final Player player) {
    return afkTime.getOrDefault(player, 0L);
  }

  void setAFK(final Player player, final boolean status) {
    if (status) {
      boardManager.setSuffix(player, "§7 [AFK]");
      afkTime.put(player, System.currentTimeMillis());
    } else {
      lastMovePosition.put(player, Pair.of(player.getLocation().getYaw(), System.currentTimeMillis()));
      afkTime.remove(player);
      boardManager.setSuffix(player, "");
    }
  }

  boolean isAFK(final Player player) {
    return afkTime.containsKey(player);
  }


  private void updateAFK(final Player player) {
    if (player.hasPermission(bypassPerm)) {
      return;
    }

    final Pair<Float, Long> data = lastMovePosition.get(player);

    if (player.getLocation().getYaw() != data.getLeft()) {

      lastMovePosition.put(player, Pair.of(player.getLocation().getYaw(), System.currentTimeMillis()));

      if (isAFK(player)) {
        TaskManager.runTask(() -> setAFK(player, false));
        Bukkit.broadcastMessage(F.main("AFK", F.name(player.getDisplayName()) + " ist nach " + F.elem(UtilTime.getElapsedTime(getAFKTime(player))) + " wieder da."));
        return;
      }
    }
    if (!isAFK(player) && UtilTime.isElapsed(data.getRight() + afkAfter)) {
      TaskManager.runTask(() -> setAFK(player, true));
      F.broadcast("AFK", F.name(player.getDisplayName()) + " ist nun AFK.");
      return;
    }

    if (isAFK(player) && UtilTime.isElapsed(getAFKTime(player) + kickAfter)) {
      TaskManager.runTask(() -> player.kickPlayer("§cDu warst zu lange inaktiv"));
    }
  }

  @Override
  public void run() {
    Bukkit.getOnlinePlayers().forEach(this::updateAFK);
  }
}