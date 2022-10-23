/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.Warp can not be copied and/or distributed without the express
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

@CommandAlias("warp|warps")
public class Warp extends BaseCommand {

//	@Default
//	@CommandCompletion("@warp")
//	public void warpTo(final Player sender, @Values("@warp") @Optional final String warp) {
//		final WarpManager wm = AvarionCore.getPlugin().getWarpManager();
//
//
//		if (warp != null && !warp.isEmpty()) {
//
//			if (!sender.hasPermission("warp." + warp)) {
//				sender.sendMessage(Bukkit.getPermissionMessage());
//				return;
//			}
//
//			sender.teleport(wm.getWarp(warp), TeleportCause.COMMAND);
//			UtilPlayer.playSound(sender, Sound.ENTITY_ENDERMAN_TELEPORT);
//			sender.sendMessage(F.main("Teleport", "Du wurdest zu Warp " + F.name(warp) + " teleportiert."));
//			return;
//		}
//
//
//		if (!sender.hasPermission("ct.warplist")) {
//			sender.sendMessage(Bukkit.getPermissionMessage());
//			return;
//		}
//
//		sender.sendMessage(F.main("Warp", "Verfügbare Warps:"));
//		final List<String> warps = wm.getWarps();
//		Collections.sort(warps, String.CASE_INSENSITIVE_ORDER);
//		sender.sendMessage(F.main("Warp", F.format(warps, ", ", "Keine Warps vorhanden")));
//		return;
//	}
}