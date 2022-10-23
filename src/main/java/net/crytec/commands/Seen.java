/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Seen can not be copied and/or distributed without the express
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

package net.crytec.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.util.F;
import org.bukkit.OfflinePlayer;

@CommandAlias("seen")
@CommandPermission("ct.seen")
@Description("Zeigt an, wann ein Spieler zuletzt online war")
public class Seen extends BaseCommand {
	
	@Default
	public void seenLookup(final CommandIssuer issuer, final OfflinePlayer player) {
		if (player.isOnline()) {
			issuer.sendMessage(F.main("Info", F.name(player.getName()) + " §2ist gerade online."));
			return;
		}
		
		issuer.sendMessage(F.main("Info", F.name(player.getName()) + " war zuletzt vor " +  UtilTime.getElapsedTime(player.getLastSeen()) + " online."  ));
	}
}