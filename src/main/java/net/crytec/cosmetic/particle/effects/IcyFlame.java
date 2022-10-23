/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.IcyFlame can not be copied and/or distributed without the express
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
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class IcyFlame extends IParticleEffect {

  private static final double radius = 1.8;


  public IcyFlame(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location location = getPlayer().getLocation();
    location.setPitch(0.0f);
    int n = 1;
    while (n < 4) {
      final double d = 120 * n;
      final Vector vector = new Vector(Math.cos((getPlayer().getTicksLived() * 3.141592653589793 / 32.0 + Math.toRadians(d))) * radius, 0.1, Math.sin((getPlayer().getTicksLived() * 3.141592653589793 / 32.0 + Math.toRadians(d))) * radius);
      location.add(vector);
      final Vector vector2 = getPlayer().getLocation().toVector().subtract(location.toVector()).normalize();

      Particle.FLAME.builder().location(location).extra(0.1F).count(0).offset(vector.getX(), vector.getY(), vector.getZ()).spawn();

      location.subtract(vector);
      location.add(vector.multiply(-1));

      Particle.END_ROD.builder().location(location.clone().add(0, 0.2, 0)).offset(vector2.getX(), vector2.getY(), vector2.getZ()).extra(0.1F).count(0).spawn();

      location.subtract(vector);
      ++n;
    }


  }


}