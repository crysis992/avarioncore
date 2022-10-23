/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Meteor can not be copied and/or distributed without the express
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

import java.util.ArrayList;
import java.util.List;
import net.crytec.cosmetic.particle.IParticleEffect;
import net.crytec.libs.commons.utils.UtilMath;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Meteor extends IParticleEffect {

  private final List<MeteorData> meteors = new ArrayList<>();

  public Meteor(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    if (getPlayer().getTicksLived() % 30 == 0) {
      new MeteorData(getPlayer().getLocation());
    }
		meteors.removeIf(meteor -> {
          if (meteor.location.distance(meteor.end) <= 1.0) {
            meteor.explode();
            return true;
          }
          meteor.move();
          return false;
        }
    );
  }

  private class MeteorData {

    Location location;
    Location end;
    Vector v;

    public MeteorData(final Location location) {
      this.location = location;
			end = location.clone().add((double) UtilMath.getRandomWithExclusion(-3, 3, 0), 0.0, (double) UtilMath.getRandomWithExclusion(-3, 3, 0));
      location.add((double) UtilMath.getRandomWithExclusion(-3, 3, 0, 1), 3.0, (double) UtilMath.getRandomWithExclusion(-3, 3, 0));
			v = end.toVector().subtract(location.toVector()).normalize();
			v.multiply(0.1);
			meteors.add(this);
    }

    public void move() {
      if (location == null) {
        return;
      }
			location.add(v);
      final Vector flame = v.clone().multiply(-1).add(new Vector(UtilMath.randomRange(-0.01f, 0.01f), UtilMath.randomRange(-0.01f, 0.01f), UtilMath.randomRange(-0.01f, 0.01f)));
      Particle.FLAME.builder().location(location).count(0).offset(flame.getX(), flame.getY(), flame.getZ()).receivers(64).extra(1).spawn();
      final Vector smoke = v.clone().multiply(1);
      Particle.SMOKE_LARGE.builder().location(location).extra(0).offset(smoke.getX(), smoke.getY(), smoke.getZ()).receivers(64).spawn();
    }

    private void explode() {
      double d = 0.0;
      while (d <= 6.283185307179586) {
        final Vector vector = new Vector(Math.cos(d), 0.0, Math.sin(d));
        Particle.FLAME.builder().location(location).extra(0.1).count(0).offset(vector.getX(), vector.getY(), vector.getZ()).receivers(64).spawn();
        d += 0.3141592653589793;
      }
    }
  }
}
