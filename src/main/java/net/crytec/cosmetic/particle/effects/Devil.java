/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Devil can not be copied and/or distributed without the express
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

public class Devil extends IParticleEffect {

  public Devil(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location center = getPlayer().getLocation();
    final Vector vector = new Vector(randomRange(-1.0f, 1.0f), 0.0f, randomRange(-1.0f, 1.0f));
    center.add(vector);

    Particle.FLAME.builder().location(center).receivers(24).count(0).extra(0).offset((double) randomRange(0.4f, 1.0f), 0, 0.10000000149011612).spawn();
    Particle.SMOKE_LARGE.builder().location(center).receivers(24).count(0).extra(0).offset((double) randomRange(0.4f, 1.0f), 0, 0.10000000149011612).spawn();
  }

  public static double randomRange(final double n, final double n2) {
    return (Math.random() < 0.5) ? ((1.0 - Math.random()) * (n2 - n) + n) : (Math.random() * (n2 - n) + n);
  }

}
