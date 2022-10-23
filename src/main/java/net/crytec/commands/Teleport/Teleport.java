/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.Teleport can not be copied and/or distributed without the express
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

package net.crytec.commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@CommandAlias("tp")
@CommandPermission("ct.tp")
public class Teleport extends BaseCommand {

  @Default
  @CommandCompletion("@players @players")
  public void teleport(final Player sender, final OnlinePlayer target, @Optional final OnlinePlayer op) {

    if (op != null) {
      sender.sendMessage(F.main("Teleport", "Teleportiere " + F.name(target.getPlayer().getDisplayName()) + " zu " + F.name(op.getPlayer().getDisplayName())));
      UtilPlayer.playSound(target.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT);
      target.getPlayer().teleport(op.getPlayer(), TeleportCause.COMMAND);
    } else {

      sender.sendMessage(F.main("Teleport", "Teleportiere zu " + F.name(target.getPlayer().getDisplayName())));
      sender.teleport(target.getPlayer(), TeleportCause.COMMAND);
      UtilPlayer.playSound(sender, Sound.ENTITY_ENDERMAN_TELEPORT);

    }
  }
}
