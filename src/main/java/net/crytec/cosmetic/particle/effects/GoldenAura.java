/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.GoldenAura can not be copied and/or distributed without the express
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
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GoldenAura extends IParticleEffect {


  private static final double angle = 0.14279966607226333;
  private static final double radius = 1.0;
  private static final ItemStack data = new ItemStack(Material.GOLDEN_APPLE);
  private static final double max = 2.0;

  private double height = 0.0;
  private Location location;
  private boolean loop = false;


  public GoldenAura(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
      location = getPlayer().getLocation();
    int n = 1;
    while (n < 4) {
      final double d = 120 * n;
      final Vector vector = new Vector(Math.cos((double) getPlayer().getTicksLived() * angle + Math.toRadians(d)) * radius, height, Math.sin((double) getPlayer().getTicksLived() * angle + Math.toRadians(d)) * radius);
      Particle.REDSTONE.builder().location(location.add(vector)).receivers(64).data(new DustOptions(Color.fromRGB(255, 255, 0), 1)).spawn();
      Particle.ITEM_CRACK.builder().location(location).offset(0.1, 0.1, 0.1).extra(0).receivers(64).data(data).spawn();
        location.subtract(vector);
      ++n;
    }
    if (!loop) {
        height += 0.05;
      if (height >= max) {
          loop = true;
      }
    } else {
        height -= 0.05;
      if (height <= 0.0) {
          loop = false;
      }
    }
  }
}