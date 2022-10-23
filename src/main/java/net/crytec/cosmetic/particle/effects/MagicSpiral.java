/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.MagicSpiral can not be copied and/or distributed without the express
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

public class MagicSpiral extends IParticleEffect {

  private double x;
  private double z;
  private double y = 0.0;
  private Location location;
  private static final double angle = 0.19634954084936207;
  private boolean loop;

  public MagicSpiral(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
		location = getPlayer().getLocation();
		x = Math.cos((double) getPlayer().getTicksLived() * angle);
		z = Math.sin((double) getPlayer().getTicksLived() * angle);
    final Vector vector = new Vector(x, y, z);
		getPlayer().getWorld().spawnParticle(Particle.SPELL_INSTANT, location.add(vector), 1, 0.0, 0.0, 0.0, 0.0);
		getPlayer().getWorld().spawnParticle(Particle.SPELL_WITCH, location, 1, 0.20000000298023224, 0.20000000298023224, 0.20000000298023224, 0.03999999910593033);
    if (loop) {
			y += 0.05;
      if (y >= 2.0) {
				loop = false;
      }
    } else {
			y -= 0.05;
      if (y <= 0.0) {
				loop = true;
      }
    }
  }

}
