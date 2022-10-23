/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.RainCloud can not be copied and/or distributed without the express
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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RainCloud extends IParticleEffect {

  public RainCloud(final Player player) {
    super(player);
  }


  @Override
  public void onUpdate(final boolean isMoving) {
    if (isMoving) {
      Particle.WATER_SPLASH.builder().location(getPlayer().getLocation().add(0.0D, 1.0D, 0.0D)).offset(0.2, 0.2, 0.2).extra(0).count(4).receivers(24).spawn();
    } else {
      Particle.CLOUD.builder().location(getPlayer().getLocation().add(0.0D, 3.5D, 0.0D)).offset(0.6, 0.6, 0.6).count(8).extra(0).receivers(24).spawn();
      Particle.DRIP_WATER.builder().location(getPlayer().getLocation().add(0.0D, 3.5D, 0.0D)).offset(0.4, 0.1, 0.4).extra(0).count(4).receivers(24).spawn();
			getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.WEATHER_RAIN_ABOVE, 0.1F, 1.0F);
    }
  }

}
