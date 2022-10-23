/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.buildtools.listener.DoorListener can not be copied and/or distributed without the express
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

package net.crytec.addons.buildtools.listener;

import net.crytec.libs.commons.utils.UtilPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Openable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class DoorListener implements Listener {
	
	@EventHandler
	public void openDoor(final PlayerInteractEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (!event.getPlayer().hasPermission("ct.buildtools")) return;
		
		final Block block = event.getClickedBlock();
		
		if (block.getType() == Material.IRON_DOOR || block.getType() == Material.IRON_TRAPDOOR) {
			
			final BlockState state = block.getState();
			
            final Openable door = (Openable) state.getBlockData();
			
			if (door.isOpen()) {
				door.setOpen(false);
				UtilPlayer.playSound(event.getPlayer(), Sound.BLOCK_IRON_DOOR_CLOSE);
			} else {
				door.setOpen(true);
				UtilPlayer.playSound(event.getPlayer(), Sound.BLOCK_IRON_DOOR_OPEN);
			}
            state.setBlockData(door);
            state.update();
		}
	}
}