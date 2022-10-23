/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.packets.wrapper.WrapperPlayServerEntityMetadata can not be copied and/or distributed without the express
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

package net.crytec.util.packets.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntityMetadata extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Play.Server.ENTITY_METADATA;

	public WrapperPlayServerEntityMetadata() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerEntityMetadata(final PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Entity ID.
	 * <p>
	 * Notes: entity's ID
	 * 
	 * @return The current Entity ID
	 */
	public int getEntityID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Entity ID.
	 * 
	 * @param value - new value.
	 */
	public void setEntityID(final int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 * 
	 * @param world - the current world of the entity.
	 * @return The spawned entity.
	 */
	public Entity getEntity(final World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 * 
	 * @param event - the packet event.
	 * @return The spawned entity.
	 */
	public Entity getEntity(final PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	/**
	 * Retrieve Metadata.
	 * 
	 * @return The current Metadata
	 */
	public List<WrappedWatchableObject> getMetadata() {
		return handle.getWatchableCollectionModifier().read(0);
	}

	/**
	 * Set Metadata.
	 * 
	 * @param value - new value.
	 */
	public void setMetadata(final List<WrappedWatchableObject> value) {
		handle.getWatchableCollectionModifier().write(0, value);
	}
}