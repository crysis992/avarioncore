/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Blizzard can not be copied and/or distributed without the express
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

import net.crytec.cosmetic.particle.IParticleEffect;
import org.bukkit.entity.Player;

public class Blizzard extends IParticleEffect {

//	private final PhoenixRecharge recharge;

  public Blizzard(final Player player) {
    super(player);
//		recharge = PhoenixAPI.get().getRecharge();
  }

  @Override
  public void onUpdate(final boolean isMoving) {
//		if (isMoving) {
//			Particle.SNOW_SHOVEL.builder().location(getPlayer().getLocation().add(0.0D, 1.0D, 0.0D)).offset(0.2, 0.2, 0.2).receivers(24).spawn();
//
//		} else {
//			getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.WEATHER_RAIN_ABOVE, 0.015F, 0.2F);
//
//			double scale = getPlayer().getTicksLived() % 50 / 50.0D;
//			for (int i = 0; i < 8; i++) {
//				double r = (1.0D - scale) * 3.141592653589793D * 2.0D;
//
//				double x = Math.sin(r + i * 0.7853981633974483D) * (r % 12.566370614359172D) * 0.4D;
//				double z = Math.cos(r + i * 0.7853981633974483D) * (r % 12.566370614359172D) * 0.4D;
//
//				Particle.SNOW_SHOVEL.builder().location(getPlayer().getLocation().add(x, scale * 3.0D, z)).extra(0).receivers(24).spawn();
//
//				if ((scale > 0.95D) && (recharge.use(getPlayer(), "Blizzard", 1000L, false, false))) {
//
//					Particle.SNOW_SHOVEL.builder().location(getPlayer().getLocation().add(0.0D, scale * 3.5D, 0.0D)).extra(0.2).receivers(24).count(60).spawn();
//					getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.BLOCK_SNOW_STEP, 1.0F, 1.5F);
//				}
//			}
//		}
  }

}
