/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.pets.types.PetHorseBlizzard can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.pets.types;

import net.crytec.cosmetic.pets.EquipablePet;
import net.crytec.cosmetic.pets.PetRegistration;
import net.crytec.cosmetic.pets.pathfinder.PathfinderGoalWalkToOwner;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityHorse;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.World;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Player;

public class PetHorseBlizzard extends EntityHorse implements EquipablePet {

  public PetHorseBlizzard(final World world, final Player owner) {
    super(EntityTypes.HORSE, world);
    setPosition(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ());
    setLocation(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), owner.getLocation().getYaw(), owner.getLocation().getPitch());
    setCustomNameVisible(true);
    setAgeRaw(-24000);
    ageLocked = true;

    PetRegistration.clearGoals(goalSelector, targetSelector);

    goalSelector.a(0, new PathfinderGoalFloat(this));
    goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 5.0F));
    goalSelector.a(2, new PathfinderGoalWalkToOwner(this, 1.2F, owner));
  }

  @Override
  public boolean damageEntity(final DamageSource damagesource, final float f) {
    return false;
  }

  @Override
  public void setOnFire(final int i) {
    return;
  }

  @Override
  protected void burnFromLava() {
    return;
  }

  @Override
  protected void dropDeathLoot(final DamageSource source, final int arg1, final boolean arg2) {
    return;
  }


  @Override // Prevent editing the horseinventory in any way.
  public boolean a(final EntityHuman entityhuman) {
    return false;
  }

//	@Override
//	public void n() {
//		super.n();
//
//		if (this.locX != this.lastX || this.locZ != this.lastZ) { // Check if
//																	// the
//																	// entity
//																	// has moved
//			ParticleEffect.CLOUD.display(0.55F, 0.15F, 0.55F, 0.10F, 2, bukkitEntity.getLocation(), 20D);
//		} else {
//			ParticleEffect.CLOUD.display(0.4F, 0.25F, 0.4F, 0, 2, bukkitEntity.getLocation(), 20D);
//		}
//	}

  // Do nothing with NBT
  // Pets should not be stored to world save files
  // start
  @Override
  public void b(final NBTTagCompound nbttagcompound) {
  }

  @Override
  public boolean c(final NBTTagCompound nbttagcompound) {
    return false;
  }

  @Override
  public void a(final NBTTagCompound nbttagcompound) {
  }

  @Override
  public boolean d(final NBTTagCompound nbttagcompound) {
    return false;
  }

  @Override
  public void equipPet() {
    final Horse be = (Horse) getBukkitEntity();
    be.setColor(Color.WHITE);
    be.setDomestication(be.getMaxDomestication());
    be.setStyle(Style.WHITE);
    be.setTamed(true);
  }

  // end
}
