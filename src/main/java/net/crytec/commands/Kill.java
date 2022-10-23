/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Kill can not be copied and/or distributed without the express
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

package net.crytec.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.google.common.collect.ImmutableSet;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import net.crytec.util.F;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@CommandAlias("killall")
@CommandPermission("ct.killall")
@Description("KILL IT WITH FIRE!!")
public class Kill extends BaseCommand {


  @Default
  public void onKill(final Player player, final KillTarget targetType) {

    player.sendMessage(F.main("Kill", "Es wurden " + F.elem(String.valueOf(targetType.killAt(player.getWorld())) + " Entities gelöscht.")));

  }

  @Subcommand("type")
  @CommandCompletion("@mobs")
  public void onKillType(final Player player, final EntityType type, @Default("10") final Integer radius) {
    player.getNearbyEntities(radius, radius, radius).stream().filter(ent -> ent.getType().equals(type))
        .forEach(Entity::remove);

  }

  @AllArgsConstructor
  public enum KillTarget {

    PLAYER(ImmutableSet.<EntityType>builder()
        .add(EntityType.PLAYER)
        .build()),
    HOSTILE(ImmutableSet.<EntityType>builder()
        .add(EntityType.BLAZE)
        .add(EntityType.CAVE_SPIDER)
        .add(EntityType.CREEPER)
        .add(EntityType.DROWNED)
        .add(EntityType.ELDER_GUARDIAN)
        .add(EntityType.ENDERMAN)
        .add(EntityType.ENDERMITE)
        .add(EntityType.EVOKER)
        .add(EntityType.EVOKER_FANGS)
        .add(EntityType.GHAST)
        .add(EntityType.GIANT)
        .add(EntityType.GUARDIAN)
        .add(EntityType.HUSK)
        .add(EntityType.ILLUSIONER)
        .add(EntityType.MAGMA_CUBE)
        .add(EntityType.PHANTOM)
        .add(EntityType.PIG_ZOMBIE)
        .add(EntityType.PILLAGER)
        .add(EntityType.RAVAGER)
        .add(EntityType.SHULKER)
        .add(EntityType.SKELETON)
        .add(EntityType.SPIDER)
        .add(EntityType.SILVERFISH)
        .add(EntityType.SLIME)
        .add(EntityType.STRAY)
        .add(EntityType.VEX)
        .add(EntityType.VINDICATOR)
        .add(EntityType.WITCH)
        .add(EntityType.WITHER_SKELETON)
        .add(EntityType.ZOMBIE)
        .add(EntityType.ZOMBIE_VILLAGER)
        .build()),
    ANIMAL(ImmutableSet.<EntityType>builder()
        .add(EntityType.CHICKEN)
        .add(EntityType.CAT)
        .add(EntityType.COD)
        .add(EntityType.BAT)
        .add(EntityType.COW)
        .add(EntityType.DOLPHIN)
        .add(EntityType.DONKEY)
        .add(EntityType.FOX)
        .add(EntityType.HORSE)
        .add(EntityType.LLAMA)
        .add(EntityType.MULE)
        .add(EntityType.MUSHROOM_COW)
        .add(EntityType.OCELOT)
        .add(EntityType.PANDA)
        .add(EntityType.PARROT)
        .add(EntityType.PIG)
        .add(EntityType.POLAR_BEAR)
        .add(EntityType.PUFFERFISH)
        .add(EntityType.RABBIT)
        .add(EntityType.SALMON)
        .add(EntityType.SHEEP)
        .add(EntityType.SKELETON_HORSE)
        .add(EntityType.SQUID)
        .add(EntityType.TROPICAL_FISH)
        .add(EntityType.TURTLE)
        .add(EntityType.WOLF)
        .add(EntityType.ZOMBIE_HORSE)
        .build()),
    AMBIENT(ImmutableSet.<EntityType>builder()
        .add(EntityType.AREA_EFFECT_CLOUD)
        .add(EntityType.ARROW)
        .add(EntityType.COD)
        .add(EntityType.BAT)
        .add(EntityType.DRAGON_FIREBALL)
        .add(EntityType.EGG)
        .add(EntityType.ENDER_CRYSTAL)
        .add(EntityType.ENDER_PEARL)
        .add(EntityType.ENDER_SIGNAL)
        .add(EntityType.EXPERIENCE_ORB)
        .add(EntityType.FALLING_BLOCK)
        .add(EntityType.FIREBALL)
        .add(EntityType.FIREWORK)
        .add(EntityType.FISHING_HOOK)
        .add(EntityType.LIGHTNING)
        .add(EntityType.LLAMA_SPIT)
        .add(EntityType.PRIMED_TNT)
        .add(EntityType.PUFFERFISH)
        .add(EntityType.SALMON)
        .add(EntityType.SHULKER_BULLET)
        .add(EntityType.SMALL_FIREBALL)
        .add(EntityType.SNOWBALL)
        .add(EntityType.SPECTRAL_ARROW)
        .add(EntityType.SPLASH_POTION)
        .add(EntityType.THROWN_EXP_BOTTLE)
        .add(EntityType.TRIDENT)
        .add(EntityType.TROPICAL_FISH)
        .add(EntityType.WITHER_SKULL)
        .build()),
    FRIENDLY(ImmutableSet.<EntityType>builder()
        .add(EntityType.IRON_GOLEM)
        .add(EntityType.SNOWMAN)
        .add(EntityType.TRADER_LLAMA)
        .add(EntityType.VILLAGER)
        .add(EntityType.WANDERING_TRADER)
        .build()),
    ITEM(ImmutableSet.<EntityType>builder()
        .add(EntityType.DROPPED_ITEM)
        .add(EntityType.EXPERIENCE_ORB)
        .build()),
    BOSS(ImmutableSet.<EntityType>builder()
        .add(EntityType.ENDER_DRAGON)
        .add(EntityType.WITHER)
        .build()),
    VEHICLE(ImmutableSet.<EntityType>builder()
        .add(EntityType.BOAT)
        .add(EntityType.MINECART)
        .add(EntityType.MINECART_CHEST)
        .add(EntityType.MINECART_COMMAND)
        .add(EntityType.MINECART_FURNACE)
        .add(EntityType.MINECART_HOPPER)
        .add(EntityType.MINECART_MOB_SPAWNER)
        .add(EntityType.MINECART_TNT)
        .build());

    private final ImmutableSet<EntityType> killTypes;

    public int killAt(final World world) {

      int count = 0;

      for (final Entity entity : world.getEntities().stream().filter(entity -> killTypes.contains(entity.getType())).collect(Collectors.toList())) {

        entity.remove();
        count++;

      }

      return count;

    }

  }

}
