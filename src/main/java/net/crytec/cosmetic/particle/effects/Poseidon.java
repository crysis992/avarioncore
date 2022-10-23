/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Poseidon can not be copied and/or distributed without the express
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

public class Poseidon extends IParticleEffect {

  private static final double angle = 0.15707963267948966;

  private double radius = 1.0;
  private double height = 0.0;
  private double height2 = 1.0;
  private double radius2 = 1.0;

  public Poseidon(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location location = getPlayer().getLocation();
    final Location location2 = getPlayer().getLocation();
    int n = 1;
    while (n < 3) {
      final double d = 180 * n;
      final Vector vector = new Vector(Math.cos((double) getPlayer().getTicksLived() * angle + Math.toRadians(d)) * radius, height, Math.sin((double) getPlayer().getTicksLived() * angle + Math.toRadians(d)) * radius);
      Particle.DRIP_WATER.builder().location(location.add(vector)).count(0).extra(0).offset(0, 0, 0).receivers(64).spawn();
      location.subtract(vector);
      vector.setX(Math.cos((double) getPlayer().getTicksLived() * angle + Math.toRadians(d)) * radius2);
      vector.setY(height2);
      vector.setZ(Math.sin((double) getPlayer().getTicksLived() * angle + Math.toRadians(d)) * radius2);
      Particle.REDSTONE.builder().location(location2.add(vector)).count(1).extra(0).data(new DustOptions(Color.fromRGB(0, 0, 255), 1)).receivers(64).spawn();
      location2.subtract(vector);
      ++n;
    }
      height += 0.05;
      height2 += 0.05;
    if (height >= 2.5) {
        height = 0.0;
        radius = 1.0;
    }
    if (height2 >= 2.5) {
        height2 = 0.0;
        radius2 = 1.0;
    }
    if (height2 >= 2.1) {
        radius2 -= 0.10000000149011612;
    }
    if (height >= 2.1) {
        radius -= 0.10000000149011612;
    }
  }

}
