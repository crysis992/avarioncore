/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.IParticleEffect can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.particle;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public abstract class IParticleEffect {

	private final Player player;

	public IParticleEffect(final Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void update(final boolean isMoving) {
		if (player.getGameMode() == GameMode.SPECTATOR) return;
		onUpdate(isMoving);
	}

	public abstract void onUpdate(boolean isMoving);
}