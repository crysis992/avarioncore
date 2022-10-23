/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.pets.pathfinder.PathfinderGoalWalkToOwner can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.pets.pathfinder;

import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PathfinderGoalWalkToOwner extends PathfinderGoal {

  private final EntityInsentient pet;
  private final double speed;
  private final static double distanceSquared = 12;
  private final Player p;

  public PathfinderGoalWalkToOwner(final EntityInsentient entityInsentient, final float speed, final Player owner) {
    pet = entityInsentient;
    p = owner;
    this.speed = speed;
  }

  @Override
  public boolean a() { // should execute
    if (!p.isOnline() || p.isFlying() || p.isInsideVehicle() || p.isDead()) {
      return false;
    }

    final Location targetLocation = p.getLocation();

    if (pet.getBukkitEntity().getWorld() != targetLocation.getWorld()) {
      return false;
    }
    if (pet.getBukkitEntity().getLocation().distanceSquared(targetLocation) > 225) {
      pet.getBukkitEntity().teleport(targetLocation);
      return false;
    }

    return pet.getBukkitEntity().getLocation().distanceSquared(p.getLocation()) > distanceSquared;
  }

  @Override
  public boolean b() { // shouldContinueExecuting
    return a();
  }

  @Override
  public void c() { // startExecuting
    final EntityPlayer owner = ((CraftPlayer) p).getHandle();

    pet.getControllerLook().a(owner, pet.pitch, pet.yaw); //TODO Update NMS
    pet.getNavigation().a(owner, speed);

    pet.getNavigation().a(owner.locX(), owner.locY(), owner.locZ(), speed);

  }

  @Override
  public void e() { // updateTask
    super.e();
    if (pet.getBukkitEntity().getLocation().distanceSquared(p.getLocation()) > 4 * 4) {
      c();
    }
  }

//  @Override
//  public boolean C_() {
//    return true;
//  }
}
