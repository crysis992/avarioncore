/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.teleportRequest.TeleportRequest can not be copied and/or distributed without the express
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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import java.util.UUID;
import net.crytec.commands.teleportRequest.TeleportRequestManager.TeleportRequestType;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpa")
public class TeleportRequest extends BaseCommand {

  public TeleportRequest(final TeleportRequestManager manager) {
    this.manager = manager;
  }

  private final TeleportRequestManager manager;

  @Default
  @CommandCompletion("@players")
  public void sendRequest(final Player player, final OnlinePlayer target) {
    manager.addRequest(player, target.getPlayer(), TeleportRequestType.TELEPORT_TO);
    player.sendMessage(F.main("Teleport", "Anfrage an " + target.getPlayer().getName() + " wurde gesendet"));

    target.getPlayer().sendMessage(F.main("Teleport", "Du hast eine neue Teleportanfrage von " + player.getDisplayName()));
  }

  @Subcommand("accept")
  @CommandCompletion("@players")
  public void acceptRequest(final Player player, final OnlinePlayer target) {
    if (manager.getActiveRequests(player).isEmpty()) {
      player.sendMessage("Keine aktiven Anfragen.");
      return;
    }

    if (!manager.getActiveRequests(player).contains(target.getPlayer().getUniqueId())) {
      player.sendMessage(F.main("Teleport", "Du hast keine Anfrage von " + F.name(target.getPlayer().getDisplayName())));
      return;
    }
    manager.acceptRequest(player, target.getPlayer().getUniqueId());
  }

  @Subcommand("deny")
  @CommandCompletion("@players")
  public void denyRequest(final Player player, final String targetID) {
    if (manager.getActiveRequests(player).isEmpty()) {
      player.sendMessage("Keine aktiven Anfragen.");
      return;
    }

    final UUID target = UUID.fromString(targetID);
    if (target == null) {
      return;
    }
    manager.denyRequest(player, target);
  }

  @Subcommand("list")
  public void listRequests(final Player player) {
    player.sendMessage(F.main("Teleport", "Liste deiner Teleportanfragen:"));

    if (manager.getActiveRequests(player).isEmpty()) {
      player.sendMessage(F.main("Teleport", "Du hast keine aktiven Teleportanfragen."));
      return;
    }

    manager.getActiveRequests(player).forEach(id -> {
      final Player t = Bukkit.getPlayer(id);
      if (t != null) {
        player.sendMessage(t.getDisplayName());
      }
    });
  }
}
