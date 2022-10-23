/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.chestlock.Lock can not be copied and/or distributed without the express
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

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;

public class Lock {
	
	private final UUID owner;
	private final Set<UUID> members;
	
	@JsonCreator
	public static Lock jacksonConstructor(@JsonProperty("owner") UUID owner, @JsonProperty("members") Set<UUID> members ) {
		return new Lock(owner, members);
	}
	
	private Lock(UUID uuid, Set<UUID> member) {
		this.owner = uuid;
		this.members = member;
	}
	
	public Lock(UUID owner) {
		this.owner = owner;
		this.members = Sets.newHashSet();
	}

	public UUID getOwner() {
		return owner;
	}

	public Set<UUID> getMembers() {
		return members;
	}
	
	@JsonIgnore
	public boolean canAccess(UUID uuid) {
		return uuid.equals(this.owner) || this.members.contains(uuid);
	}
	
	public boolean addMember(UUID uuid) {
		if (this.members.size() >= 5) {
			return false;
		}
		this.members.add(uuid);
		return true;
	}
	
	public void removeMember(UUID uuid) {
		this.members.remove(uuid);
	}
}