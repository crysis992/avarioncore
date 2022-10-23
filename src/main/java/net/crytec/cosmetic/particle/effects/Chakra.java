/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Chakra can not be copied and/or distributed without the express
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

import com.google.common.collect.Maps;
import java.util.Map;
import net.crytec.cosmetic.particle.IParticleEffect;
import net.crytec.libs.commons.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Chakra extends IParticleEffect {

  private static final double radius = 3.0;
  private final Map<Integer, Location> locs = Maps.newHashMap();


  public Chakra(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {

    if (getPlayer().getTicksLived() % 3 == 0) {
      final Location location = getPlayer().getLocation();
      final Vector vector = UtilMath.getRandomVector().multiply(radius);
      vector.setY(Math.abs((double) vector.getY()));

      Particle.REDSTONE.builder().location(location.add(vector)).count(1).extra(0).color(Color.fromBGR(0, 204, 204)).spawn();

      locs.put(UtilMath.randomRange((int) 0, (int) 99999), location);
    }
    locs.keySet().removeIf(n -> {
          final Location location = (Location) locs.get((Object) n);
          if (location.distance(getPlayer().getLocation().add(0.0, 1.0, 0.0)) < 0.5) {
            return true;
          }
          final Vector vector = getPlayer().getLocation().clone().add(0.0, 1.0, 0.0).toVector().subtract(location.toVector());
          final Location location2 = location.add(vector.multiply(0.1));

          Particle.REDSTONE.builder()
              .location(location2)
              .extra(0)
              .count(1)
              .color(Color.fromBGR(0, UtilMath.randomRange((int) 160, (int) 250), UtilMath.randomRange((int) 160, (int) 250)))
              .spawn();
          return false;
        }
    );


  }

}