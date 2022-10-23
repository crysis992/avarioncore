/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.CrushedCandyCane can not be copied and/or distributed without the express
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

import io.netty.util.internal.ThreadLocalRandom;
import net.crytec.cosmetic.particle.IParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrushedCandyCane extends IParticleEffect {

  private int step;

  private static final ItemStack green = new ItemStack(Material.GREEN_DYE);
  private static final ItemStack red = new ItemStack(Material.RED_DYE);
  private static final ItemStack white = new ItemStack(Material.BONE_MEAL);

  public CrushedCandyCane(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
		if (step > 360) {
			step = 0;
		}
    final Location center = getPlayer().getEyeLocation().add(0, 0.6, 0);
    final double inc = (2 * Math.PI) / 20;
    final double angle = step * inc;
    final double x = Math.cos(angle) * 1.1f;
    final double z = Math.sin(angle) * 1.1f;
    center.add(x, 0, z);
		for (int i = 0; i < 15; i++) {
			Particle.ITEM_CRACK.builder().location(center).offset(0.2, 0.2, 0.2).extra(0).count(1).data(getRandomColor()).receivers(64).spawn();
		}
		step++;

  }

  private static ItemStack getRandomColor() {
    final float f = ThreadLocalRandom.current().nextFloat();
		if (f > 0.98) {
			return green;
		} else if (f > 0.49) {
			return red;
		} else {
			return white;
		}
  }
}