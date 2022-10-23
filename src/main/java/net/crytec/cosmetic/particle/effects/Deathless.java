/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Deathless can not be copied and/or distributed without the express
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

public class Deathless extends IParticleEffect {

  private double y = 0.1;

  public Deathless(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location location = getPlayer().getLocation();
    int n = 1;
    while (n < 3) {
      final double d = n % 2 == 0 ? -1.5 : 1.5;
      final double d2 = (double) getPlayer().getTicksLived() * 3.141592653589793 / 18.0;
      final Vector vector = new Vector(Math.cos(d2) * d, y, Math.sin(d2) * d);
      Particle.END_ROD.builder().location(location).offset(vector.getX(), vector.getY(), vector.getZ()).extra(0.1).count(0).receivers(64).spawn();
      vector.setY(0);
      Particle.SPELL_MOB.builder().location(location.add(vector)).extra(0).receivers(64).data(new DustOptions(Color.fromRGB(30, 255, 255), 1)).spawn();
      location.subtract(vector);
			y += 0.1;
      if (y >= 1.0) {
				y = 0.0;
      }
      ++n;
    }
  }
}