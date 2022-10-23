/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Fly can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@CommandAlias("fly")
@CommandPermission("ct.fly")
public class Fly extends BaseCommand {

	
	@Default
	@CommandCompletion("@players")
	public void enableFly(final Player issuer, @Optional final OnlinePlayer op) {
		if (op != null) {
			if (!issuer.hasPermission("ct.fly.others")) {
				issuer.sendMessage(F.error(Bukkit.getPermissionMessage()));
				return;
			}
			
			final Player target = op.getPlayer();
			
			if (target.getGameMode() == GameMode.CREATIVE) {
				issuer.sendMessage(F.error(target.getDisplayName() + "§7 ist im Kreativmodus."));
				return;
			}
			
			final boolean mode = toggleFlight(target);
			issuer.sendMessage(F.main("Fly", "Flugmodus für " + F.name(target.getDisplayName()) + " wurde " + F.tf(mode)));
			target.sendMessage(F.main("Fly", "Flugmodus wurde " + F.tf(mode)));
			UtilPlayer.playSound(target, Sound.ENTITY_PARROT_FLY, 0.6F, 1.25F);
		} else {
			if (issuer.getGameMode() == GameMode.CREATIVE) {
				issuer.sendMessage(F.error("Im Kreativmodus nicht möglich."));
				return;
			}
			issuer.sendMessage(F.main("Fly", "Flugmodus wurde " + F.tf(toggleFlight(issuer))));
			UtilPlayer.playSound(issuer, Sound.ENTITY_PARROT_FLY, 0.6F, 1.25F);
		}
	}
	
	private boolean toggleFlight(final Player target) {
		if (target.getAllowFlight()) {
			target.setAllowFlight(false);
			target.setFlying(false);
			return false;
		} else {
			target.setAllowFlight(true);
			target.setFlying(true);
			return true;
		}
	}
}