/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.packets.wrapper.WrapperPlayClientUseEntity can not be copied and/or distributed without the express
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
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class WrapperPlayClientUseEntity extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.USE_ENTITY;

	public WrapperPlayClientUseEntity() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientUseEntity(final PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve entity ID of the target.
	 * 
	 * @return The current entity ID
	 */
	public int getTargetID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Retrieve the entity that was targeted.
	 * 
	 * @param world - the current world of the entity.
	 * @return The targeted entity.
	 */
	public Entity getTarget(final World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity that was targeted.
	 * 
	 * @param event - the packet event.
	 * @return The targeted entity.
	 */
	public Entity getTarget(final PacketEvent event) {
		return getTarget(event.getPlayer().getWorld());
	}

	/**
	 * Set entity ID of the target.
	 * 
	 * @param value - new value.
	 */
	public void setTargetID(final int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Type.
	 * 
	 * @return The current Type
	 */
	public EntityUseAction getType() {
		return handle.getEntityUseActions().read(0);
	}

	/**
	 * Set Type.
	 * 
	 * @param value - new value.
	 */
	public void setType(final EntityUseAction value) {
		handle.getEntityUseActions().write(0, value);
	}

	/**
	 * Retrieve the target vector.
	 * <p>
	 * Notes: Only if {@link #getType()} is {@link EntityUseAction#INTERACT_AT}.
	 * 
	 * @return The target vector or null
	 */
	public Vector getTargetVector() {
		return handle.getVectors().read(0);
	}

	/**
	 * Set the target vector.
	 * 
	 * @param value - new value.
	 */
	public void setTargetVector(final Vector value) {
		handle.getVectors().write(0, value);
	}
}