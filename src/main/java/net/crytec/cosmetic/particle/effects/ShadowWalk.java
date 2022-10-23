/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.ShadowWalk can not be copied and/or distributed without the express
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

public class ShadowWalk extends IParticleEffect {

  private boolean foot = true;
  private double lastx = 0;
  private double lastz = 0;

  public ShadowWalk(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    if (!isMoving) {
      return;
    }

    final Location l = getPlayer().getLocation();
    l.setY(Math.floor(l.getY()));

    if (!l.clone().subtract(0.0D, 1.0D, 0.0D).getBlock().isEmpty()) {
      final double x = Math.cos(Math.toRadians(getPlayer().getLocation().getYaw())) * 0.25D;
      final double y = Math.sin(Math.toRadians(getPlayer().getLocation().getYaw())) * 0.25D;

      if (l.getX() == lastx && l.getZ() == lastz) {
        return;
      }
			lastx = l.getX();
			lastz = l.getZ();

      if (foot) {
        l.add(x, 0.025D, y);
      } else {
        l.subtract(x, -0.025D, y);
      }
      Particle.SMOKE_NORMAL.builder().location(l).extra(0).offset(0.05, 0, 0.05).receivers(32).spawn();
			foot = !foot;
    }
  }

}
