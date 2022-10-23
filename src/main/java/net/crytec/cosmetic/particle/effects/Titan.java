/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Titan can not be copied and/or distributed without the express
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
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Titan extends IParticleEffect {

  private static final double radius = 3.5D;


  public Titan(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location location = getPlayer().getLocation();
    int n = 0;
    while (n < 4) {
      final double d = 120 * n;
      final Vector vector = new Vector(Math.cos(Math.toRadians(d)) * radius, 0.1, Math.sin(Math.toRadians(d)) * radius);
      UtilMath.rotateAroundAxisY(vector, (getPlayer().getTicksLived() * 3.141592653589793 / 46.0));
      location.add(vector);
      final Vector vector2 = getPlayer().getLocation().toVector().subtract(location.toVector()).normalize();
      Particle.FLAME.builder().location(location).offset(vector2.getX(), vector2.getY(), vector2.getZ()).count(0).extra(0.15).receivers(64).spawn();
      location.subtract(vector);
      final Vector vector3 = new Vector(Math.cos(Math.toRadians(d)) * radius, 0.1, Math.sin(Math.toRadians(d)) * radius);
      UtilMath.rotateAroundAxisY(vector3, (getPlayer().getTicksLived() * 3.141592653589793 / 46.0 * -1.0));
      location.add(vector3);
      final Vector vector4 = getPlayer().getLocation().toVector().subtract(location.toVector()).normalize();
      Particle.FLAME.builder().location(location).offset(vector4.getX(), vector4.getY(), vector4.getZ()).count(0).extra(0.15).receivers(64).spawn();
      location.subtract(vector3);
      ++n;
    }
  }


  public void update() {

  }


}
