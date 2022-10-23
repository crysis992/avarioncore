/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.BloodHelix can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.particle.effects;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;

import net.crytec.cosmetic.particle.IParticleEffect;

public class BloodHelix extends IParticleEffect {

	public BloodHelix(Player player) {
		super(player);
	}

	@Override
	public void onUpdate(boolean isMoving) {
		if (isMoving) {
			Particle.REDSTONE.builder().location(getPlayer().getLocation().add(0.0D, 1.0D, 0.0D)).offset(0.2F, 0.2F, 0.2F).extra(0).allPlayers().receivers(64).data(new DustOptions(Color.RED, 0.75F)).spawn();
		} else {
			for (int height = 0; height <= 20; height++) {
				for (int i = 0; i < 2; i++) {
					double lead = i * Math.PI;

					double heightLead = height * 0.3141592653589793D;

					float x = (float) (Math.sin(getPlayer().getTicksLived() / 20.0D + lead + heightLead) * 1.200000047683716D);
					float z = (float) (Math.cos(getPlayer().getTicksLived() / 20.0D + lead + heightLead) * 1.200000047683716D);

					float y = 0.15F * height;

					Location location = getPlayer().getLocation().clone().add(x * (1.0D - height / 22.0D), y, z * (1.0D - height / 22.0D));
		
					Particle.REDSTONE.builder().location(location).offset(0, 0, 0).extra(0).allPlayers().receivers(64).data(new DustOptions(Color.RED, 0.75F)).spawn();
				}
			}
		}
	}
}