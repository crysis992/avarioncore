/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.AngelFairy can not be copied and/or distributed without the express
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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AngelFairy extends IParticleEffect {

    private Vector vector = new Vector(1, 0, 0);
    private Location currentLocation;
    private Location targetLocation;
    private double noMoveTime = 0.0;
    private double movementSpeed = 0.2;
	
	public AngelFairy(final Player player) {
		super(player);
      currentLocation = player.getLocation();
      targetLocation = getNewTarget();
	}

	@Override
	public void onUpdate(final boolean isMoving) {
        if (!currentLocation.getWorld().equals((Object) getPlayer().getWorld())) {
            currentLocation = getPlayer().getLocation();
        }
        final double d = getPlayer().getEyeLocation().distance(currentLocation);
        double d2 = currentLocation.distance(targetLocation);
        if (d2 < 1.0 || d > 3.0) {
            targetLocation = getNewTarget();
        }
        d2 = currentLocation.distance(targetLocation);
        if (UtilMath.random.nextDouble() > 0.98) {
            noMoveTime = (double)System.currentTimeMillis() + UtilMath.randomRange(0.0, 2000.0);
        }
        if (getPlayer().getEyeLocation().distance(currentLocation) < 3.0) {
            movementSpeed = noMoveTime > (double)System.currentTimeMillis() ? Math.max(0.0, movementSpeed - 0.0075) : Math.min(0.1, movementSpeed + 0.0075);
        } else {
            noMoveTime = 0.0;
            movementSpeed = Math.min(0.15 + d * 0.05, movementSpeed + 0.02);
        }
      vector.add(targetLocation.toVector().subtract(currentLocation.toVector()).multiply(0.2));
        if (vector.length() < 1.0) {
            movementSpeed = vector.length() * movementSpeed;
        }
      vector = vector.normalize();
        if (d2 > 0.1) {
            currentLocation.add(vector.clone().multiply(movementSpeed));
        }
      getPlayer().getWorld().spawnParticle(Particle.END_ROD, currentLocation, 1, 0.0, 0.0, 0.0, 0.0);

      getPlayer().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, currentLocation, 1, (double)0.05f, (double)0.05f, (double)0.05f, (double)0.05f);
            
        if (getPlayer().getTicksLived() % 250 == 0) {
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_WITCH_AMBIENT, 0.09f, 2.0f);
        }
	}
	
    private Location getNewTarget() {
        return getPlayer().getEyeLocation().add(Math.random() * 6.0 - 3.0, Math.random() * 1.5, Math.random() * 6.0 - 3.0);
    }

}
