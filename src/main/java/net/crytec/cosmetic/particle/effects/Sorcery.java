/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.Sorcery can not be copied and/or distributed without the express
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Sorcery extends IParticleEffect {

  private static final double angle = 0.14279966607226333;
  private static final double angularVelocityY = 0.3141592653589793;
  private static final ItemStack data = new ItemStack(Material.OBSIDIAN);

  private double radius = 0.0;
  private int height = 70;
  private final int max = 60;

  public Sorcery(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final Location location = getPlayer().getLocation().clone().subtract(0.0, 0.3, 0.0);
    final double d = (double) getPlayer().getTicksLived() * angularVelocityY;
    int n = 1;
    while (n < 4) {
      final double d2 = 120 * n;
      final Vector vector = new Vector(Math.cos((double) getPlayer().getTicksLived() * angle + Math.toRadians(d2)) * radius, (double) height * 0.05, Math.sin((double) getPlayer().getTicksLived() * angle + Math.toRadians(d2)) * radius);
      UtilMath.rotateAroundAxisY(vector, d);

      Particle.SPELL_WITCH.builder().location(location.add(vector)).receivers(64).count(1).offset(0, 0, 0).extra(0).spawn();
      Particle.ITEM_CRACK.builder().location(location).receivers(64).data(data).extra(0).spawn();

      location.subtract(vector);
      ++n;
    }
    radius = height > max / 2 ? (radius += 0.05000000074505806) : (radius -= 0.05000000074505806);
    --height;
    if (height <= 0) {
      height = max;
      radius = 0.0;
    }
  }

}
