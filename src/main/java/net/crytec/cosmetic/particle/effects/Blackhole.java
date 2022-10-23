/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Blackhole can not be copied and/or distributed without the express
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
import net.crytec.libs.commons.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Blackhole extends IParticleEffect {

  private static final double angle = 0.14279966607226333;
  private static final double angularVelocityY = 0.09817477042468103;

  public Blackhole(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location location = getPlayer().getLocation().clone().subtract(0.0, 0.3, 0.0);
    if (isMoving) {
      Particle.CRIT_MAGIC.builder().location(getPlayer().getLocation().add(0.0D, 1.0D, 0.0D)).offset(0.2, 0.2, 0.2).extra(0).receivers(24).spawn();
      Particle.REDSTONE.builder().location(getPlayer().getLocation()).extra(0).receivers(24).data(new DustOptions(Color.fromRGB(204, 0, 204), 1)).spawn();
      return;
    }

    int n = 1;
    while (n < 4) {
      int n2 = 1;
      while (n2 < 8) {
        final double d = 51 * n2;
        final double d2 = (double) getPlayer().getTicksLived() * angularVelocityY + Math.toRadians(n * 30);
        final double d3 = (float) n * 0.4f;
        final Vector vector = new Vector(Math.cos((double) getPlayer().getTicksLived() * angle + Math.toRadians(d)) * d3, (double) n
            * 0.3, Math.sin((double) getPlayer().getTicksLived() * angle + Math.toRadians(d)) * d3);
        UtilMath.rotateAroundAxisY(vector, d2);
        Particle.REDSTONE.builder().location(getPlayer().getLocation().add(vector)).extra(0).receivers(64).data(new DustOptions(Color.fromRGB(0, 0, 0), 1)).spawn();
        if (UtilMath.randomRange(0, 100) >= 95) {
          Particle.REDSTONE.builder().location(location).extra(0).receivers(64).data(new DustOptions(Color.fromRGB(204, 0, 204), 1)).spawn();
          Particle.SPELL_MOB.builder().location(location).extra(0).receivers(64).data(new DustOptions(Color.fromRGB(1, 1, 1), 1)).spawn();
        }
        location.subtract(vector);
        ++n2;
      }
      ++n;
    }
    if (getPlayer().getTicksLived() % 10 == 0) {
      final Location location2 = location.clone().add((double) UtilMath.randomRange(-1.5f, 1.5f), 1.5, (double) UtilMath.randomRange(-1.5f, 1.5f));
      final Vector offset = getPlayer().getLocation().toVector().subtract(location2.toVector()).normalize();
      Particle.FLAME.builder().location(location2).offset(offset.getX(), offset.getY(), offset.getZ()).extra(0).receivers(64).spawn();
    }
  }

}