/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.CPULoad can not be copied and/or distributed without the express
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

package net.crytec.commands.administration;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.google.common.collect.Sets;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import net.crytec.AvarionCore;
import net.crytec.libs.commons.utils.UtilMath;
import net.crytec.libs.protocol.scoreboard.api.Entry;
import net.crytec.libs.protocol.scoreboard.api.EntryBuilder;
import net.crytec.libs.protocol.scoreboard.api.ScoreboardHandler;
import net.crytec.util.F;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandAlias("mbb")
@CommandPermission("ct.monitorboard")
public class CPULoad extends BaseCommand {

  private final OperatingSystemMXBean osBean;
  private final HashSet<UUID> active;

  public CPULoad() {
    osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    active = Sets.newHashSet();
  }

  @Default
  public void toggleSidebar(final Player player) {
    if (!active.contains(player.getUniqueId())) {

      AvarionCore.getPlugin().getScoreBoardManager().setBoard(player, new MonitorBoard());
      active.remove(player.getUniqueId());
      return;
    }
    AvarionCore.getPlugin().getScoreBoardManager().resetBoard(player);
    active.add(player.getUniqueId());
  }

  @Subcommand("gc")
  public void callGC(final Player player) {
    Runtime.getRuntime().gc();
  }

  private static int getFreeRam() {
    final Runtime runtime = Runtime.getRuntime();
    return Math.round((float) (runtime.freeMemory() / 1048576L));
  }

  private static int getMaxRam() {
    final Runtime runtime = Runtime.getRuntime();
    return Math.round((float) (runtime.maxMemory() / 1048576L));
  }

  private static int getUsedRam() {
    return getTotalRam() - getFreeRam();
  }

  private static int getTotalRam() {
    final Runtime runtime = Runtime.getRuntime();
    return Math.round((float) (runtime.totalMemory() / 1048576L));
  }


  private final class MonitorBoard implements ScoreboardHandler {

    @Override
    public String getTitle(final Player player) {
      return "§c" + player.getWorld().getName();
    }

    @Override
    public List<Entry> getEntries(final Player player) {
      final double load = UtilMath.trim(2, osBean.getProcessCpuLoad() * 100);
      final World world = player.getWorld();

      return new EntryBuilder().blank()
          .next("§fCPU: " + load)
          .next("§f" + F.getPercentageBar(load, 100, 50, "\u007C"))
          .blank()
          .next("§fMemory:")
          .next(F.name(String.valueOf(getUsedRam())) + "/" + F.name(String.valueOf(getMaxRam())) + " MB")
          .blank()
          .next("§fActive Players: §6" + world.getPlayerCount())
          .next("§fLoaded Chunks: §6" + world.getLoadedChunks().length)
          .next("§fLoaded Entities: §6" + world.getEntityCount())
          .build();
    }
  }

}
