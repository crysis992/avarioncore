/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.teleportRequest.TeleportRequestManager can not be copied and/or distributed without the express
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

package net.crytec.commands.teleportRequest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TeleportRequestManager {


  public TeleportRequestManager() {
    requests = Maps.newHashMap();

    listener = new RemovalListener<>() {
      @Override
      public void onRemoval(final RemovalNotification<UUID, TeleportRequestType> notification) {
        final Player requester = Bukkit.getPlayer(notification.getKey());
        if (requester == null) {
          return;
        }
        requester.sendMessage(F.main("Teleport", "Deine Teleportanfrage ist abgelaufen."));
      }
    };

  }

  private final HashMap<UUID, Cache<UUID, TeleportRequestType>> requests;
  private final RemovalListener<UUID, TeleportRequestType> listener;

  public void addRequest(final Player requester, final Player target, final TeleportRequestType type) {
    if (!requests.containsKey(target.getUniqueId())) {
      requests.put(target.getUniqueId(), CacheBuilder.newBuilder().maximumSize(50).expireAfterWrite(40, TimeUnit.SECONDS).build());
    } else {
      requests.get(target.getUniqueId()).cleanUp();

      if (requests.get(target.getUniqueId()).asMap().containsKey(requester.getUniqueId())) {
        requester.sendMessage(F.main("Teleport", "Du hast " + F.name(target.getDisplayName()) + " bereits eine Anfrage geschickt."));
        return;
      }

    }
    requests.get(target.getUniqueId()).put(requester.getUniqueId(), type);
  }

  public boolean hasRequest(final Player player) {
    if (!requests.containsKey(player.getUniqueId())) {
      return false;
    } else {
      requests.get(player.getUniqueId()).cleanUp();
      return requests.get(player.getUniqueId()).size() > 0;
    }
  }

  public Set<UUID> getActiveRequests(final Player player) {
    if (!requests.containsKey(player.getUniqueId())) {
      return Sets.newHashSet();
    }
    requests.get(player.getUniqueId()).cleanUp();
    return requests.get(player.getUniqueId()).asMap().keySet();
  }

  public void denyRequest(final Player player, final UUID targetID) {
    if (requests.containsKey(player.getUniqueId())
        && requests.get(player.getUniqueId()).asMap().containsKey(player.getUniqueId())) {
      requests.get(player.getUniqueId()).invalidate(targetID);
    }
  }

  public void acceptRequest(final Player player, final UUID targetID) {
    final Player target = Bukkit.getPlayer(targetID);

    if (target == null) {
      player.sendMessage(F.main("Teleport", "Teleport abgebrochen, Spieler ist nicht mehr online."));
      return;
    }

    target.sendMessage(F.main("Teleport", F.name(target.getDisplayName()) + " hat deine Teleportanfrage angenommen. Teleportiere..."));
    target.teleport(player);
    requests.get(player.getUniqueId()).invalidate(targetID);
    UtilPlayer.playSound(target, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5F, 1);
  }


  public enum TeleportRequestType {
    TELEPORT_TO,
    TELEPORT_HERE
  }

}
