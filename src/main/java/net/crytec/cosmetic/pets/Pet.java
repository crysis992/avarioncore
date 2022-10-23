/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.pets.Pet can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.pets;

import java.lang.reflect.Constructor;
import lombok.Getter;
import net.crytec.cosmetic.pets.events.PetSpawnEvent;
import net.minecraft.server.v1_15_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class Pet {

  private final PetType type;
  private final Player owner;
  private Entity petEntity;

  private String name;

  @Getter
  private final String defaultName;

  private boolean hidden = false;
  private boolean hasCustomName = false;

  public static PetManager manager;


  public Pet(final Player owner, final PetType type, final String defaultName) {
    this.type = type;
    this.owner = owner;
    this.defaultName = "&2" + owner.getName() + "'s " + defaultName;
    spawn();
  }


  public PetType getType() {
    return type;
  }

  public Entity getEntity() {
    return petEntity;
  }

  public String getName() {
    return name;
  }

  public boolean hasCustomName() {
    return hasCustomName;
  }

  public void respawn() {
    kill();
    spawn();
  }


  public void kill() {
    if (petEntity == null) {
      return; // Do nothing, pet is not spawned.
    }
    final net.minecraft.server.v1_15_R1.Entity craftEnt = ((CraftEntity) petEntity).getHandle();
    craftEnt.die();
    if (!petEntity.isDead()) {
      petEntity.remove();
    }
  }

  public void setHidden(final boolean hidden) {
    if (hidden) {
      this.hidden = true;
      kill();
    } else {
      if (petEntity.isDead()) {
        spawn();
      }
      this.hidden = false;
    }
  }

  public boolean isHidden() {
    return hidden;
  }

  public void spawn() {
    final PetSpawnEvent evt = new PetSpawnEvent(owner, this);
    Bukkit.getPluginManager().callEvent(evt);

    if (evt.isCancelled()) {
      return;
    }
    final CraftWorld world = (CraftWorld) owner.getWorld();

    try {
      final Constructor<?> cons = type.getPetClass().getConstructor(World.class, Player.class);
      final Object[] para = new Object[]{world.getHandle(), owner};

      final net.minecraft.server.v1_15_R1.Entity ent = (net.minecraft.server.v1_15_R1.Entity) cons.newInstance(para);
      world.addEntity(ent, SpawnReason.CUSTOM);

      if (ent instanceof EquipablePet) {
        final EquipablePet p = (EquipablePet) ent;
        p.equipPet();
      }

      petEntity = ent.getBukkitEntity();

    } catch (final Exception ex) {
      ex.printStackTrace();
    }

    if (hasCustomName) {
      petEntity.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
    } else {
      petEntity.setCustomName(ChatColor.translateAlternateColorCodes('&', defaultName));
    }
  }

  public void setName(final String name) {
    if (petEntity == null) {
      this.name = name;
      return;
    }
    petEntity.setCustomName(name);
    hasCustomName = true;
    this.name = name;
  }
}