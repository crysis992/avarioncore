/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.pets.PetType can not be copied and/or distributed without the express
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

import net.crytec.cosmetic.pets.types.PetCaveSpider;
import net.crytec.cosmetic.pets.types.PetChicken;
import net.crytec.cosmetic.pets.types.PetCow;
import net.crytec.cosmetic.pets.types.PetCreeper;
import net.crytec.cosmetic.pets.types.PetEndermite;
import net.crytec.cosmetic.pets.types.PetHorse;
import net.crytec.cosmetic.pets.types.PetHorseBlizzard;
import net.crytec.cosmetic.pets.types.PetInfernalHorror;
import net.crytec.cosmetic.pets.types.PetLama;
import net.crytec.cosmetic.pets.types.PetMushroomCow;
import net.crytec.cosmetic.pets.types.PetPig;
import net.crytec.cosmetic.pets.types.PetPigZombie;
import net.crytec.cosmetic.pets.types.PetPolarBear;
import net.crytec.cosmetic.pets.types.PetRabbit;
import net.crytec.cosmetic.pets.types.PetSheep;
import net.crytec.cosmetic.pets.types.PetSpider;
import net.crytec.cosmetic.pets.types.PetTurtle;
import net.crytec.cosmetic.pets.types.PetVillager;
import net.crytec.cosmetic.pets.types.PetWolf;
import net.crytec.cosmetic.pets.types.PetZombie;
import net.minecraft.server.v1_15_R1.Entity;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;


public enum PetType {

  CHICKEN(PetChicken.class, EntityType.CHICKEN, Material.CHICKEN_SPAWN_EGG),
  COW(PetCow.class, EntityType.COW, Material.COW_SPAWN_EGG),
  CAVE_SPIDER(PetCaveSpider.class, EntityType.CAVE_SPIDER, Material.CAVE_SPIDER_SPAWN_EGG),
  CREEPER(PetCreeper.class, EntityType.CREEPER, Material.CREEPER_SPAWN_EGG),
  BLIZZARD(PetHorseBlizzard.class, EntityType.HORSE, Material.HORSE_SPAWN_EGG),
  ENDERMITE(PetEndermite.class, EntityType.ENDERMITE, Material.ENDERMITE_SPAWN_EGG),
  HORSE(PetHorse.class, EntityType.HORSE, Material.HORSE_SPAWN_EGG),
  INFERNAL_HORROR(PetInfernalHorror.class, EntityType.HORSE, Material.HORSE_SPAWN_EGG),
  LAMA(PetLama.class, EntityType.LLAMA, Material.LLAMA_SPAWN_EGG),
  MUSHROOM_COW(PetMushroomCow.class, EntityType.MUSHROOM_COW, Material.MOOSHROOM_SPAWN_EGG),
  PIGZOMBIE(PetPigZombie.class, EntityType.PIG_ZOMBIE, Material.ZOMBIE_PIGMAN_SPAWN_EGG),
  PIG(PetPig.class, EntityType.PIG, Material.PIG_SPAWN_EGG),
  POLAR_BEAR(PetPolarBear.class, EntityType.POLAR_BEAR, Material.POLAR_BEAR_SPAWN_EGG),
  RABBIT(PetRabbit.class, EntityType.RABBIT, Material.RABBIT_SPAWN_EGG),
  SHEEP(PetSheep.class, EntityType.SHEEP, Material.SHEEP_SPAWN_EGG),
  SPIDER(PetSpider.class, EntityType.SPIDER, Material.SPIDER_SPAWN_EGG),
  TURTLE(PetTurtle.class, EntityType.TURTLE, Material.TURTLE_SPAWN_EGG),
  WOLF(PetWolf.class, EntityType.WOLF, Material.WOLF_SPAWN_EGG),
  VILLAGER(PetVillager.class, EntityType.VILLAGER, Material.VILLAGER_SPAWN_EGG),
  ZOMBIE(PetZombie.class, EntityType.ZOMBIE, Material.ZOMBIE_SPAWN_EGG);

  private final Class<? extends Entity> clazz;
  private final EntityType entityType;
  private final Material spawnEgg;


  PetType(final Class<? extends Entity> Petclass, final EntityType entityType, final Material egg) {
    clazz = Petclass;
    this.entityType = entityType;
    spawnEgg = egg;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public Class<? extends Entity> getPetClass() {
    return clazz;
  }

  public Material getSpawnEggIcon() {
    return spawnEgg;
  }

  public static PetType getPetType(final String type) {
    if (type == null) {
      return null;
    }
    try {
      return PetType.valueOf(type.toUpperCase());
    } catch (final IllegalArgumentException iae) {
      return null;
    }
  }
}
