/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.ApocalypticCloud can not be copied and/or distributed without the express
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

import net.crytec.AvarionCore;
import net.crytec.cosmetic.particle.IParticleEffect;
import net.crytec.libs.commons.utils.UtilLoc;
import net.crytec.libs.commons.utils.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ApocalypticCloud extends IParticleEffect {

	public ApocalypticCloud(final Player player) {
		super(player);
	}

	@Override
	public void onUpdate(final boolean isMoving) {
		final Location location = getPlayer().getLocation();

		int n = 0;
		while (n < 2) {
			
			Particle.SMOKE_LARGE.builder().location(location.clone().add((double) UtilMath.randomRange(-1.0f, 1.0f), 2.5, (double) UtilMath.randomRange(-1.0f, 1.0f))).receivers(64).offset(0, 0, 0).extra(0).spawn();
			Particle.SMOKE_LARGE.builder().location(location.clone().add((double) UtilMath.randomRange(-1.0f, 1.0f), 2.7, (double) UtilMath.randomRange(-1.0f, 1.0f))).receivers(64).offset(0, 0, 0).extra(0).spawn();
			++n;
		}
		if (getPlayer().getTicksLived() % 3 == 0) {
			Particle.FLAME.builder().location(location.clone().add((double) UtilMath.randomRange(-0.8f, 0.8f), 2.5, (double) UtilMath.randomRange(-0.8f, 0.8f))).offset(0, -1.5, 0).extra(0.1).receivers(64).spawn();
		}
		final Location loc = location.clone().add((double) UtilMath.randomRange(-0.8f, 0.8f), 2.5, (double) UtilMath.randomRange(-0.8f, 0.8f));
		Particle.REDSTONE.builder().location(loc).offset(0, 0, 0).receivers(64).data(new DustOptions(Color.fromRGB(255, 128, 0), 0.75F)).spawn();
		
		if (getPlayer().getTicksLived() % 120 == 0) {
			playThunder(location.clone().add((double) UtilMath.randomRange(-0.5f, 0.5f), 2.7, (double) UtilMath.randomRange(-0.5f, 0.5f)));
		}
	}
	
	private void playThunder(final Location location) {
		UtilLoc.getClosestPlayersFromLocation(location, 4.0).forEach(player -> {
			Bukkit.getScheduler().runTask(AvarionCore.getPlugin(), () -> {
				player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.05f, 0.0f);
				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 3));
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20, 1));
			});
		});
		final Location location2 = location.clone();
		Vector vector = UtilMath.getRandomVector();
		vector.setY(-Math.abs(vector.getY() - 2.0));
		final int n = UtilMath.randomRange(20, 40);
		int n2 = 0;
		while (n2 < 50) {
			final float f = (float) n2 * 0.06f / 28.0f;
			final Vector vector2 = vector.clone().multiply(f);
			location2.add(vector2);
			Particle.REDSTONE.builder().location(location2).receivers(64).extra(0).data(new DustOptions(Color.fromRGB(255, 0, 0), 0.6F)).spawn();
			if (n2 == n || n2 == n + 10) {
				vector = UtilMath.getRandomVector();
				vector.setY(-Math.abs(vector.getY()));
			}
			++n2;
		}
	}

}
