/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.packets.wrapper.AbstractPacket can not be copied and/or distributed without the express
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
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;

public abstract class AbstractPacket {
	// The packet we will be modifying
	protected PacketContainer handle;

	/**
	 * Constructs a new strongly typed wrapper for the given packet.
	 * 
	 * @param handle - handle to the raw packet data.
	 * @param type - the packet type.
	 */
	protected AbstractPacket(final PacketContainer handle, final PacketType type) {
		// Make sure we're given a valid packet
		if (handle == null)
			throw new IllegalArgumentException("Packet handle cannot be NULL.");
		if (!Objects.equal(handle.getType(), type))
			throw new IllegalArgumentException(handle.getHandle()
					+ " is not a packet of type " + type);

		this.handle = handle;
	}

	/**
	 * Retrieve a handle to the raw packet data.
	 * 
	 * @return Raw packet data.
	 */
	public PacketContainer getHandle() {
		return handle;
	}

	/**
	 * Send the current packet to the given receiver.
	 * 
	 * @param receiver - the receiver.
	 * @throws RuntimeException If the packet cannot be sent.
	 */
	public void sendPacket(final Player receiver) {
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(receiver,
					getHandle());
		} catch (final InvocationTargetException e) {
			throw new RuntimeException("Cannot send packet.", e);
		}
	}

	/**
	 * Send the current packet to all online players.
	 */
	public void broadcastPacket() {
		ProtocolLibrary.getProtocolManager().broadcastServerPacket(getHandle());
	}

	/**
	 * Simulate receiving the current packet from the given sender.
	 * 
	 * @param sender - the sender.
	 * @throws RuntimeException If the packet cannot be received.
	 * @deprecated Misspelled. recieve to receive
	 * @see #receivePacket(Player)
	 */
	@Deprecated
	public void recievePacket(final Player sender) {
		try {
			ProtocolLibrary.getProtocolManager().recieveClientPacket(sender,
					getHandle());
		} catch (final Exception e) {
			throw new RuntimeException("Cannot recieve packet.", e);
		}
	}

	/**
	 * Simulate receiving the current packet from the given sender.
	 * 
	 * @param sender - the sender.
	 * @throws RuntimeException if the packet cannot be received.
	 */
	public void receivePacket(final Player sender) {
		try {
			ProtocolLibrary.getProtocolManager().recieveClientPacket(sender,
					getHandle());
		} catch (final Exception e) {
			throw new RuntimeException("Cannot receive packet.", e);
		}
	}
}