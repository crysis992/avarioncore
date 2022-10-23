/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Santa can not be copied and/or distributed without the express
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

public class Santa extends IParticleEffect {

  public Santa(final Player player) {
    super(player);
  }

  private final DustOptions color = new DustOptions(Color.RED, 1);

  @Override
  public void onUpdate(final boolean isMoving) {
    if (isMoving) {
      return;
    }

    if (getPlayer().getTicksLived() % 5 != 0) {
      return;
    }
    final Location location = getPlayer().getLocation();
    double n = 0.3;
    for (double n2 = 1.8; n2 <= 2.4; n2 += 0.2) {
      final double n3 = n * 64.0;
      final double n4 = 6.283185307179586 / n3;
      for (int n5 = 0; n5 < n3; ++n5) {
        final double n6 = n5 * n4;
        final Vector vector = new Vector(n * Math.cos(n6), n2, n * Math.sin(n6));
        UtilMath.rotateAroundAxisX(vector, Math.toRadians(location.getPitch()) * 3.141592653589793 / 18.0);
        UtilMath.rotateAroundAxisY(vector, Math.toRadians(-location.getYaw()));
        if (n2 <= 2.0) {
					getPlayer().getWorld().spawnParticle(Particle.REDSTONE, location.add(vector), 1, color);
        } else {
					getPlayer().getWorld().spawnParticle(org.bukkit.Particle.END_ROD, location.add(vector), 1, 0.0, 0.0, 0.0, 0.0);
        }
        location.subtract(vector);
      }
      n -= 0.1;
    }
  }

}
