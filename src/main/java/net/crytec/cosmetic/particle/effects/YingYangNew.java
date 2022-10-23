/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.YingYangNew can not be copied and/or distributed without the express
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
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class YingYangNew extends IParticleEffect {

  public YingYangNew(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location location = getPlayer().getLocation();
    int n = 1;
    while (n < 3) {
      final double d = n % 2 == 0 ? -0.8 : 0.8;
      final Vector vector = new Vector(Math.cos((double) getPlayer().getTicksLived() * 3.141592653589793 / 18.0) * d, 0.1, Math.sin((double) getPlayer().getTicksLived() * 3.141592653589793 / 18.0) * d);

      Particle.FIREWORKS_SPARK.builder().location(location).count(0).offset(vector.getX(), vector.getY(), vector.getZ()).extra((double) 0.3F).receivers(64).spawn();
      Particle.REDSTONE.builder().location(location.add(vector.multiply(2))).extra(0).data(new DustOptions(Color.BLACK, 0.5F)).receivers(24).spawn();
      location.subtract(vector);
      ++n;
    }
  }
}