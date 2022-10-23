/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.TeleportAll can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@CommandAlias("tpall")
@CommandPermission("ct.tphere")
public class TeleportAll extends BaseCommand {

	@Default
	public void teleportAll(final Player issuer) {
		issuer.sendMessage(F.main("Teleport", "Teleportiere alle Spieler zu dir..."));
		for (final Player target : Bukkit.getOnlinePlayers()) {
			target.teleport(issuer, TeleportCause.COMMAND);
			target.sendMessage(F.main("Teleport", "Du wurdest zu " + F.name(issuer.getDisplayName()) + "�7 teleportiert."));
		}
	}

}
