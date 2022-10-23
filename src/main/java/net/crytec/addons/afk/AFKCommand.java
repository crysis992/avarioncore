/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.afk.AFKCommand can not be copied and/or distributed without the express
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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("afk")
public class AFKCommand extends BaseCommand {

	public AFKCommand(final AFK addon) {
		this.addon = addon;
	}

	private final AFK addon;

	@Default
	public void toggleAfk(final Player issuer, @Optional final String reason) {
		if (addon.isAFK(issuer)) {
			F.broadcast("AFK", F.name(issuer.getDisplayName()) + " ist nach " + F.elem(UtilTime.getElapsedTime(addon.getAFKTime(issuer))) + " wieder da.");
			addon.setAFK(issuer, false);
		} else {
			addon.setAFK(issuer, true);

			if (reason != null && !reason.isEmpty()) {
				Bukkit.broadcastMessage(F.main("AFK", F.name(issuer.getDisplayName()) + " ist nun AFK. Grund: " + F.elem(reason)));
			} else {
				Bukkit.broadcastMessage(F.main("AFK", F.name(issuer.getDisplayName()) + " ist nun AFK."));
			}

		}
	}

}
