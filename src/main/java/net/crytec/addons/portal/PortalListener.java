/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.portal.PortalListener can not be copied and/or distributed without the express
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

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.HashMap;
import net.crytec.AvarionCore;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import net.crytec.util.regionevents.RegionEnteredEvent;
import net.crytec.util.regionevents.RegionLeftEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

public class PortalListener implements Listener {

  private final PortalAddon addon;
  private final HashMap<Player, TeleportWarmupThread> threads = Maps.newHashMap();

  public PortalListener(final PortalAddon addon) {
    this.addon = addon;
  }

  @EventHandler(ignoreCancelled = true)
  public void onRegionLeave(final RegionLeftEvent event) {
    if (!addon.isPortalRegion(event.getRegion())) {
      return;
    }

    final Player player = event.getPlayer();
    if (!threads.containsKey(player)) {
      return;
    }

    final TeleportWarmupThread thread = threads.get(player);
    thread.getTask().cancel();
    threads.remove(player);

    if (thread.getCountdown() == 0) {
      return;
    }

    player.removePotionEffect(PotionEffectType.BLINDNESS);
    player.removePotionEffect(PotionEffectType.SLOW);
    player.removePotionEffect(PotionEffectType.CONFUSION);
    player.stopSound(Sound.ITEM_ELYTRA_FLYING);
    player.resetTitle();
  }

  @EventHandler(ignoreCancelled = true)
  public void onRegionEnter(final RegionEnteredEvent event) {
    if (!addon.isPortalRegion(event.getRegion())) {
      return;
    }

    final Portal portal = addon.getPortal(event.getRegion());
    final Player player = event.getPlayer();

    if (!portal.hasPermission(player)) {
      return;
    }

    final PortalEnterEvent portalEnterEvent = new PortalEnterEvent(player, portal);
    portalEnterEvent.callEvent();

    if (portalEnterEvent.isCancelled()) {

//      final Vector push = UtilVec.getPushVector(player, portal.getPortalCenter().clone().add(-0.5, 0, -0.5), 3);
//      push.setY(1.25).normalize();
//
//      player.setVelocity(player.getVelocity().add(push));

      UtilPlayer.playSound(player, Sound.ENTITY_IRON_GOLEM_ATTACK, 0.8F, 0.7F);
      return;
    }

    if (portal.isBungeePortal() && portal.getServer() != null && !portal.getServer().isEmpty()) {
      sendToServer(portal.getServer(), player);
      return;
    }

    if (portal.getDestination() == null) {
      Bukkit.broadcast("§7Spieler " + event.getPlayer().getName() + " hat eine ungültige Portal Region betreten!", "ct.notify");
      return;
    }

    final TeleportWarmupThread thread = new TeleportWarmupThread(player, portal, c -> {
      if (c) {
        player.stopSound(Sound.ITEM_ELYTRA_FLYING);
        player.teleportAsync(portal.getDestination(), TeleportCause.PLUGIN);

        player.sendMessage(F.main("Portal", "Du wurdest zu " + F.name(portal.getDisplayname()) + " teleportiert."));
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_SMALL_FALL, 1, 1);
      }
    });

    threads.put(player, thread);
  }

  private void sendToServer(final String server, final Player player) {
    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Connect");
    out.writeUTF(server);
    player.sendPluginMessage(AvarionCore.getPlugin(), "BungeeCord", out.toByteArray());
  }
}
