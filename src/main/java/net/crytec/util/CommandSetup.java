/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.CommandSetup can not be copied and/or distributed without the express
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

package net.crytec.util;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.crytec.AvarionCore;
import net.crytec.addons.plotshop.PlotShopAddon;
import net.crytec.addons.plotshop.data.Plot;
import net.crytec.addons.plotshop.data.PlotGroup;
import net.crytec.internal.CorePlayer;
import net.crytec.internal.settings.HomeSetting;
import net.crytec.libs.commons.utils.UtilMath;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.util.RayTraceResult;

public class CommandSetup {

  public static void registerCommandConditions(final PaperCommandManager manager) {
    manager.getCommandConditions().addCondition("iteminhand", (context -> {
      final BukkitCommandIssuer issuer = context.getIssuer();
      if (issuer.isPlayer()) {
        if (issuer.getPlayer().getInventory().getItemInMainHand() == null || issuer.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
          throw new ConditionFailedException("Du musst ein Item in der Hand halten.");
        }
      } else {
        throw new ConditionFailedException("Cannt be executed by console");
      }
    }));
  }


  public static void registerContextResolver(final PaperCommandManager manager) {

    manager.getCommandContexts().registerIssuerAwareContext(Block.class, issuer -> {
      final Player player = issuer.getPlayer();

      final double distance = issuer.getFlagValue("distance", 10);

      final RayTraceResult result = player.rayTraceBlocks(distance, FluidCollisionMode.NEVER);

      if (result == null || result.getHitBlock() == null) {
        throw new InvalidCommandArgument("Du musst einen Block anschauen.");
      }

      return result.getHitBlock();
    });

    manager.getCommandContexts().registerContext(PlotGroup.class, c -> {

      final String arg = c.popFirstArg();

      if (!EnumUtils.isValidEnum(PlotGroup.class, arg)) {
        throw new InvalidCommandArgument(arg + " ist keine gültige PlotGroup");
      } else {
        return PlotGroup.valueOf(arg);
      }
    });

    manager.getCommandContexts().registerContext(Plot.class, c -> {
      final String arg = c.popFirstArg();

      final PlotShopAddon addon = (PlotShopAddon) AvarionCore.getPlugin().getAddonManager().getAddon(PlotShopAddon.class);
      final Plot plot = addon.getManager().getPlot(arg);
      if (plot == null) {
        throw new InvalidCommandArgument("There is no plot with the given id of " + arg);
      }
      return plot;
    });

    manager.getCommandContexts().registerContext(ProtectedRegion.class, c -> {
      final String input = c.popFirstArg();
      final Player sender = c.getPlayer();

      final RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(sender.getWorld()));

      if (regionManager.hasRegion(input)) {
        return regionManager.getRegion(input);
      } else {
        throw new InvalidCommandArgument("There is no WorldGuard Region with the given id " + input);
      }
    });

    manager.getCommandContexts().registerContext(GameMode.class, c -> {

      final String tag = c.popFirstArg();
      if (EnumUtils.isValidEnum(GameMode.class, tag.toUpperCase())) {
        return GameMode.valueOf(tag.toUpperCase());
      } else if (UtilMath.isInt(tag)) {

        switch (Integer.parseInt(tag)) {
          case 0:
            return GameMode.SURVIVAL;
          case 1:
            return GameMode.CREATIVE;
          case 2:
            return GameMode.ADVENTURE;
          case 3:
            return GameMode.SPECTATOR;
          default:
            throw new InvalidCommandArgument("Invalid GameMode specified.");
        }

      } else {
        throw new InvalidCommandArgument("Invalid GameMode specified.");
      }
    });
  }


  /**
   * Valid CommandCompletions:
   *
   * <pre>
   * '@warp - Warp Name'
   * '@userhomes - Player homes'
   * '@entitytype - EntityTypes'
   * '@gamemode - GameMode'
   * '@attachmenttype - NPCDataAttachment'
   * '@npc - Citizens NPC'
   * </pre>
   *
   * @param plugin
   * @param manager
   */
  public static void registerCommandCompletion(final AvarionCore plugin, final PaperCommandManager manager) {
//    manager.getCommandCompletions().registerCompletion("warp", c -> {
//      return ImmutableList.copyOf(plugin.getWarpManager().getWarps().stream().collect(Collectors.toList()));
//    });

    manager.getCommandCompletions().registerCompletion("userhomes", c -> {
      final HomeSetting h = CorePlayer.get(c.getPlayer()).getData(HomeSetting.class);
      return ImmutableList.copyOf(h.getHomes().keySet());
    });

    manager.getCommandCompletions().registerCompletion("entitytype", c -> {
      return ImmutableList.copyOf(Arrays.stream(EntityType.values()).map(EntityType::toString).collect(Collectors.toList()));
    });

    final Stream<String> gameModeStream = Arrays.stream(GameMode.values()).map(mode -> mode.toString().toLowerCase());
    final Stream<String> gameModeOrdinalStream = Arrays.stream(GameMode.values()).map(mode -> String.valueOf(mode.ordinal()));
    final Stream<String> gameModeCompletion = Streams.concat(gameModeStream, gameModeOrdinalStream);

    final ImmutableList<String> gameModeList = ImmutableList.<String>builder().addAll(gameModeCompletion.collect(Collectors.toList())).build();

    manager.getCommandCompletions().registerCompletion("gamemode", c -> {
      return gameModeList;
    });

    manager.getCommandCompletions().registerCompletion("villagerprofession", c -> {
      return ImmutableList.copyOf(Arrays.stream(Profession.values()).map(Profession::toString).collect(Collectors.toList()));
    });

    manager.getCommandCompletions().registerCompletion("protectedRegion", c -> {

      final String input = c.getInput();
      final Player sender = c.getPlayer();

      final RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(sender.getWorld()));

      return regionManager.getRegions().keySet().stream().filter(id -> id.startsWith(input)).collect(Collectors.toSet());
    });

    manager.getCommandCompletions().registerCompletion("plotgroup", c -> {
      return ImmutableList.copyOf(Arrays.stream(PlotGroup.values()).map(PlotGroup::toString).collect(Collectors.toList()));
    });

    manager.getCommandCompletions().registerCompletion("plot", c -> {
      final PlotShopAddon addon = (PlotShopAddon) AvarionCore.getPlugin().getAddonManager().getAddon(PlotShopAddon.class);
      return addon.getManager().getPlotMap().stream().map(Plot::getProtectedRegionID).collect(Collectors.toSet());
    });
  }
}
