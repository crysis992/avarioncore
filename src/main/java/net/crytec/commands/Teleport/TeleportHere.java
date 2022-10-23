/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.TeleportHere can not be copied and/or distributed without the express
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
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@CommandAlias("tphere")
@CommandPermission("ct.tphere")
public class TeleportHere extends BaseCommand {

  @Default
  @CommandCompletion("@players")
  public void teleportHere(final Player issuer, final OnlinePlayer target) {
    issuer.sendMessage(F.main("Teleport", "Teleportiere " + F.name(target.getPlayer().getDisplayName()) + " zu dir."));
    target.getPlayer().teleport(issuer.getLocation(), TeleportCause.COMMAND);
    UtilPlayer.playSound(target.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT);
    target.getPlayer().sendMessage(F.main("Teleport", "Du wurdest zu " + F.name(issuer.getDisplayName()) + "§7 teleportiert."));
  }

}
