/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.FireRings can not be copied and/or distributed without the express
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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FireRings extends IParticleEffect {

  private final double radius = 1.0;
  private final double amount = radius * 20.0;
  private final double inc = 6.283185307179586 / amount;
  private double x;
  private double z;
  private double y;
  private double angle;
  private Location location;

  public FireRings(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    if (isMoving) {
      Particle.FLAME.builder().location(getPlayer().getLocation().add(0.0D, 1.0D, 0.0D)).offset(0.2, 0.2, 0.2).receivers(24).spawn();
    } else {
			location = getPlayer().getLocation();
			angle = (double) getPlayer().getTicksLived() * inc;
			x = radius * Math.cos(angle);
			z = radius * Math.sin(angle);
			y = radius * Math.cos(angle);
      final Vector vector = new Vector(x, y + 1.0, z);
			location.add(vector);
			getPlayer().getWorld().spawnParticle(Particle.FLAME, location, 1, 0.0, 0.0, 0.0, 0.0);
			location.subtract(vector);
      vector.setY(-vector.getY() + 2.0);
			location.add(vector);
			getPlayer().getWorld().spawnParticle(Particle.FLAME, location, 1, 0.0, 0.0, 0.0, 0.0);
			getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.BLOCK_FIRE_AMBIENT, 0.2F, 1.0F);
    }
  }

}
