/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.chestlock.ChestUnlockCommand can not be copied and/or distributed without the express
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

package net.crytec.addons.chestlock;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.crytec.util.F;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

@CommandAlias("unlock")
public class ChestUnlockCommand extends BaseCommand {

	private final ChestLockAddon addon;

	public ChestUnlockCommand(final ChestLockAddon addon) {
		this.addon = addon;
	}

	@Default
	@Description("Öffnet einen Behälter.")
	public void unlockCommand(final Player sender) {
		final RayTraceResult rt = sender.rayTraceBlocks(10);
		if (rt == null || rt.getHitBlock() == null) {
			sender.sendMessage(F.main("Sicherung", "Du musst einen Behälter anschauen."));
			return;
		}

		final Block block = rt.getHitBlock();
		final BlockState state = block.getState();

		if (!addon.isLocked(state)) {
			sender.sendMessage(F.error("Dieser Behälter ist nicht verschlossen"));
			return;
		}

		if (!(state instanceof TileState)) {
			sender.sendMessage(F.error(F.name(state.getType().toString()) + " ist kein Container."));
			return;
		}

		if (!addon.canAccess(sender, (TileState) state)) {
			sender.sendMessage(F.error("Dieser Behälter gehört dir nicht."));
			return;
		}

		addon.unlockChest(block);
		sender.sendMessage(F.main("Info", "Der Behälter wurde aufgeschlossen."));
	}
}