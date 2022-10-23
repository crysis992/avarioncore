/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.packets.wrapper.WrapperPlayServerMount can not be copied and/or distributed without the express
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
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerMount extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.MOUNT;

	public WrapperPlayServerMount() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerMount(final PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Entity ID.
	 * <p>
	 * Notes: vehicle's EID
	 * 
	 * @return The current Entity ID
	 */
	public int getEntityID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Retrieve the entity involved in this event.
	 * 
	 * @param world - the current world of the entity.
	 * @return The involved entity.
	 */
	public Entity getEntity(final World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity involved in this event.
	 * 
	 * @param event - the packet event.
	 * @return The involved entity.
	 */
	public Entity getEntity(final PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	/**
	 * Set Entity ID.
	 * 
	 * @param value - new value.
	 */
	public void setEntityID(final int value) {
		handle.getIntegers().write(0, value);
	}

	public int[] getPassengerIds() {
		return handle.getIntegerArrays().read(0);
	}

	public void setPassengerIds(final int[] value) {
		handle.getIntegerArrays().write(0, value);
	}

	public List<Entity> getPassengers(final PacketEvent event) {
		return getPassengers(event.getPlayer().getWorld());
	}

	public List<Entity> getPassengers(final World world) {
		final int[] ids = getPassengerIds();
		final List<Entity> passengers = new ArrayList<>();
		final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

		for (final int id : ids) {
			final Entity entity = manager.getEntityFromID(world, id);
			if (entity != null) {
				passengers.add(entity);
			}
		}

		return passengers;
	}

	public void setPassengers(final List<Entity> value) {
		final int[] array = new int[value.size()];
		for (int i = 0; i < value.size(); i++) {
			array[i] = value.get(i).getEntityId();
		}

		setPassengerIds(array);
	}
}