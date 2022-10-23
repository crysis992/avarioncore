/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.chestlock.ChestLockCommand can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.UUID;
import java.util.stream.Collectors;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

@CommandAlias("lock")
public class ChestLockCommand extends BaseCommand {

	private final ChestLockAddon addon;

	public ChestLockCommand(final ChestLockAddon addon) {
		this.addon = addon;
	}
	
	private boolean canModifyArea(final Player player, final Block block) {
		if (player.hasPermission("worldguard.region.bypass." + block.getWorld().getName())) return true;
		
		final RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		final LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);

		return query.testBuild(BukkitAdapter.adapt(block.getLocation()), lp, Flags.BLOCK_PLACE);
	}

	@Default
	@Description("Verschließt einen Behälter.")
	public void lockCommand(final Player sender) {
		final RayTraceResult rt = sender.rayTraceBlocks(10);
		if (rt == null || rt.getHitBlock() == null) {
			sender.sendMessage(F.main("Sicherung", "Du musst eine Kiste anschauen."));
			return;
		}

		final Block block = rt.getHitBlock();
		
		if (!canModifyArea(sender, block)) {
			sender.sendMessage(F.error("Du benötigst Baurechte in diesem Gebiet um eine Kiste zu sichern."));
			return;
		}
		
		final BlockState state = block.getState();
		
		if (!(state instanceof TileState)) {
			sender.sendMessage(F.error(F.name(state.getType().toString()) + " ist kein Container."));
			return;
		}
		
		if (addon.isLocked(state) && !addon.canAccess(sender, (TileState) state)) {
			sender.sendMessage(F.error("Dieser Behälter gehört dir nicht."));
			return;
		}

		if (addon.lockChest(block, sender.getUniqueId())) {
			sender.sendMessage(F.main("Info", "Dieser Behälter ist nun verschlossen."));
		} else {
			sender.sendMessage(F.error("Dieser Behälter lässt sich nicht verschließen."));
		}
	}
	
	@Subcommand("addmember")
	@Syntax("<Spieler>")
	public void addMember(final Player player, @co.aikar.commands.annotation.Flags("distance=5") final Block block, final OfflinePlayer target) {
		final BlockState state = block.getState();
		
		if (!addon.isLocked(state)) {
			player.sendMessage(F.error("Dieser Behälter ist nicht verschlossen."));
			return;
		}
		
		if (!addon.canAccess(player, (TileState) state)) {
			player.sendMessage(F.error("Dieser Behälter gehört dir nicht."));
			return;
		}
		
		final TileState container = (TileState) state;

		if (addon.getLock(container).isPresent()) {

			if (addon.addMember(container, target)) {
				player.sendMessage(F.main("Sicherung", F.name(target.getName()) + " hat nun Zugriff auf diesen Behälter"));
			} else {
				player.sendMessage(F.error("Es können maximal 5 andere Spieler Zugriff auf diese Kiste erhalten."));
				return;
			}
		} else {
			player.sendMessage(F.error("Es können maximal 5 andere Spieler Zugriff auf diese Kiste erhalten."));
		}
	}
	
	@Subcommand("delmember")
	@Syntax("<Spieler>")
	public void removeMember(final Player player, @co.aikar.commands.annotation.Flags("distance=5") final Block block, final OfflinePlayer target) {
		final BlockState state = block.getState();
		
		if (!addon.isLocked(state)) {
			player.sendMessage(F.error("Dieser Behälter ist nicht verschlossen."));
			return;
		}
		
		if (!addon.canAccess(player, (TileState) state)) {
			player.sendMessage(F.error("Dieser Behälter gehört dir nicht."));
			return;
		}
		
		final TileState container = (TileState) state;

		if (addon.getLock(container).isPresent()) {
			addon.removeMember(container, target);
		} else {
			player.sendMessage("Der Container konnte nicht aktualisiert werden.");
		}
	}

	@Subcommand("info")
	@Description("Zeigt dir den Besitzer des Behälters an.")
	@Syntax("info")
	public void lockInfo(final Player sender, @co.aikar.commands.annotation.Flags("distance=5") final Block block) {
		if (!(block.getState() instanceof TileState)) {
			sender.sendMessage(F.error("Dieser Block lässt sich nicht verschließen."));
			return;
		}
		
		final TileState state = (TileState) block.getState();

		if (addon.getLock(state).isPresent()) {
			final Lock lock = addon.getLock(state).get();

			sender.sendMessage(F.main("Sicherung", "Diese Kiste gehört " + F.name(Bukkit.getOfflinePlayer(lock.getOwner()).getName())));
			sender.sendMessage(F.main("Sicherung", "Mitglieder: " +  F.format(lock.getMembers().stream().map(op -> Bukkit.getOfflinePlayer(op).getName()).collect(Collectors.toSet()), ", ", "keine")  ));
		} else {
			sender.sendMessage("Dieser Behälter ist nicht verschlossen");
		}
	}

	@Subcommand("adminlock")
	@CommandPermission("ct.adminlock")
	@Description("Setzt den angegebenen Spieler als Besitzer.")
	public void adminLock(final Player sender, final String target) {

		final OfflinePlayer player = Bukkit.getOfflinePlayer(target);
		if (!player.hasPlayedBefore()) {
			sender.sendMessage("Dieser Spieler existiert nicht.");
		}

		final UUID uuid = player.getUniqueId();

		final RayTraceResult rt = sender.rayTraceBlocks(10);
		if (rt == null || rt.getHitBlock() == null) {
			sender.sendMessage(F.main("Sicherung", "Du musst einen Behälter anschauen."));
			return;
		}

		final Block block = rt.getHitBlock();

		if (addon.lockChest(block, uuid)) {
			sender.sendMessage(F.main("Info", "Dieser Behälter ist nun verschlossen."));
		} else {
			sender.sendMessage(F.error("Dieser Behälter lässt sich nicht verschließen."));
		}
	}
}