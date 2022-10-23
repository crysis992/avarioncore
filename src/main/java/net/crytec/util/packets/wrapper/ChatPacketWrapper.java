/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.packets.wrapper.ChatPacketWrapper can not be copied and/or distributed without the express
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
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import java.util.Arrays;

public class ChatPacketWrapper extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.CHAT;

	public ChatPacketWrapper() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public ChatPacketWrapper(final PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve the chat message.
	 * <p>
	 * Limited to 32767 bytes
	 * 
	 * @return The current message
	 */
	public WrappedChatComponent getMessage() {
		return handle.getChatComponents().read(0);
	}

	/**
	 * Set the message.
	 * 
	 * @param value - new value.
	 */
	public void setMessage(final WrappedChatComponent value) {
		handle.getChatComponents().write(0, value);
	}

	public ChatType getChatType() {
		return handle.getChatTypes().read(0);
	}

	public void setChatType(final ChatType type) {
		handle.getChatTypes().write(0, type);
	}

	/**
	 * Retrieve Position.
	 * <p>
	 * Notes: 0 - Chat (chat box) ,1 - System Message (chat box), 2 - Above
	 * action bar
	 * 
	 * @return The current Position
	 * @deprecated Magic values replaced by enum
	 */
	@Deprecated
	public byte getPosition() {
		final Byte position = handle.getBytes().readSafely(0);
		if (position != null) {
			return position;
		} else {
			return getChatType().getId();
		}
	}

	/**
	 * Set Position.
	 * 
	 * @param value - new value.
	 * @deprecated Magic values replaced by enum
	 */
	@Deprecated
	public void setPosition(final byte value) {
		handle.getBytes().writeSafely(0, value);

		if (EnumWrappers.getChatTypeClass() != null)
		{
			Arrays.stream(ChatType.values()).filter(t -> t.getId() == value).findAny()
			      .ifPresent(t -> handle.getChatTypes().writeSafely(0, t));
		}
	}
}