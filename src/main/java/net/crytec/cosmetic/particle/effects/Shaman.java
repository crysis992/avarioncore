/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Shaman can not be copied and/or distributed without the express
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

public class Shaman extends IParticleEffect {

  private Location location;
  private static final double angle = 0.17453292519943295;

  public Shaman(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    if (getPlayer().getTicksLived() % 2 == 0) {
			location = getPlayer().getLocation();
      double d = 0.2;
      double d2 = 0.5;
      int n = 0;
      while (n < 3) {
        int n2 = 0;
        while (n2 < 2) {
          final double d3 = n2 == 1 ? 180 : 0;
          final Vector vector = new Vector(Math.cos((double) getPlayer().getTicksLived() * angle + Math.toRadians(d3))
              * d, d2, Math.sin((double) getPlayer().getTicksLived() * angle + Math.toRadians(d3)) * d);

          Particle.FIREWORKS_SPARK.builder().location(location.add(vector)).count(0).offset(0, 0, 0).extra(0).receivers(64).spawn();

					location.subtract(vector);
          ++n2;
        }
        d2 += 0.7;
        d += 0.2;
        ++n;
      }
    }
  }

}
