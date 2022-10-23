/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.chairs.PlayerSitData can not be copied and/or distributed without the express
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

package net.crytec.addons.chairs;

import java.util.HashMap;
import net.crytec.addons.chairs.events.PlayerChairSitEvent;
import net.crytec.addons.chairs.events.PlayerChairUnsitEvent;
import net.crytec.util.F;
import net.crytec.util.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerSitData {

	private final HashMap<Player, SitData> sit = new HashMap<>();
	private final HashMap<Block, Player> sitblock = new HashMap<>();

	public boolean isSitting(final Player player) {
		return sit.containsKey(player) && sit.get(player).sitting;
	}

	public boolean isBlockOccupied(final Block block) {
		return sitblock.containsKey(block);
	}

	public Player getPlayerOnChair(final Block chair) {
		return sitblock.get(chair);
	}

	public boolean sitPlayer(final Player player,  final Block blocktooccupy, Location sitlocation) {
		final PlayerChairSitEvent playersitevent = new PlayerChairSitEvent(player, sitlocation.clone());
		Bukkit.getPluginManager().callEvent(playersitevent);
		if (playersitevent.isCancelled()) {
			return false;
		}
		sitlocation = playersitevent.getSitLocation().clone();
		player.sendMessage(F.main("Chairs", "Du sitzt nun."));
		final SitData sitdata = new SitData();
		
		
		final Location standloc = sitlocation.getBlock().getLocation().clone().add(0.5D, -1.2D, 0.5D);
		standloc.setYaw(sitlocation.getYaw());
		
		final ArmorStand seat = (ArmorStand) player.getWorld().spawnEntity(standloc, EntityType.ARMOR_STAND);
		sitdata.armorstand = seat;

		
		seat.setBasePlate(false);
		seat.setVisible(false);
		seat.setGravity(false);
		seat.setAI(false);
		seat.setCollidable(false);
		
		sitdata.block = blocktooccupy;
		sitdata.teleportloc = player.getLocation();

		player.getLocation().setYaw(sitlocation.getYaw());
		seat.addPassenger(player);

		sit.put(player, sitdata);
		sitblock.put(blocktooccupy, player);
		sitdata.sitting = true;
		return true;
	}

	public boolean unsitPlayer(final Player player) {
		final SitData sitdata = sit.get(player);
		final PlayerChairUnsitEvent playerunsitevent = new PlayerChairUnsitEvent(player, sitdata.teleportloc.clone());
		Bukkit.getPluginManager().callEvent(playerunsitevent);
		if (playerunsitevent.isCancelled()) {
			return false;
		}
		sitdata.sitting = false;
		player.leaveVehicle();
		sitdata.armorstand.remove();
		player.setSneaking(false);

		TaskManager.runTaskLater(() -> player.teleport(playerunsitevent.getTeleportLocation().clone().add(0, 2, 0)), 2L);

		sitblock.remove(sitdata.block);
		sit.remove(player);
		player.sendMessage(F.main("Chairs", "Du bist aufgestanden"));
		return true;
	}

	private static class SitData {

		private boolean sitting;
		private Entity armorstand;
		private Location teleportloc;
		private Block block;

	}
}