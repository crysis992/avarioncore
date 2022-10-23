/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.EmeraldTwirl can not be copied and/or distributed without the express
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
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class EmeraldTwirl extends IParticleEffect {

  public EmeraldTwirl(final Player player) {
    super(player);
  }

  @Override
  public void onUpdate(final boolean isMoving) {
    final float x = (float) (Math.sin(getPlayer().getTicksLived() / 7.0D) * 1.0D);
    final float z = (float) (Math.cos(getPlayer().getTicksLived() / 7.0D) * 1.0D);
    final float y = (float) (Math.cos(getPlayer().getTicksLived() / 17.0D) * 1.0D + 1.0D);

    Particle.VILLAGER_HAPPY.builder().location(getPlayer().getLocation().add(x, y, z)).receivers(24).extra(0).spawn();
  }

}
