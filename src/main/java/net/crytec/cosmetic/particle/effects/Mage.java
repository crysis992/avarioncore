/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Mage can not be copied and/or distributed without the express
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

public class Mage extends IParticleEffect {

  public Mage(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location location = getPlayer().getLocation();
    location.add(0.0, 2.5, 0.0);
    int n = 1;
    while (n < 3) {
      final double d = n % 2 == 0 ? -1.5 : 1.5;
      final double d2 = (double) getPlayer().getTicksLived() * 3.141592653589793 / 18.0;
      final Vector vector = new Vector(Math.cos(d2) * d, -1.5, Math.sin(d2) * d);
      Particle.FLAME.builder().location(location).offset(vector.getX(), vector.getY(), vector.getZ()).count(0).extra(0.1).receivers(64).spawn();
      Particle.CLOUD.builder().location(location.add(vector)).extra(0).count(0).offset(0, 0, 0).receivers(64).spawn();
      location.subtract(vector);
      ++n;
    }
  }

}
