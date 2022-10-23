/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Memory can not be copied and/or distributed without the express
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
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.World;

@CommandAlias("memory|mem")
@CommandPermission("ct.memory")
public class Memory extends BaseCommand {

  long startuptime;

  public Memory() {
		startuptime = System.currentTimeMillis();
  }

  @Default
  public void showMemory(final CommandIssuer sender) {

    sender.sendMessage(F.main("§cMonitor", "Das Plugin läuft seit: §c" + UtilTime.getElapsedTime(startuptime) + "§7."));
    sender.sendMessage(F.main("§cMonitor", "RAM verbrauch: " + F.name(String.valueOf(getUsedRam())) + "/" + F.name(String.valueOf(getMaxRam())) + " MB"));
    sender.sendMessage("§f[" + F.getPercentageBar(getUsedRam(), getMaxRam(), 50, "|") + "§f]");
    sender.sendMessage("");

    for (final World w : Bukkit.getWorlds()) {

      final StringBuilder message = new StringBuilder();
      message.append(F.main("Monitor", w.getName()));
      message.append(" §7Chunks: §c");
      message.append(w.getLoadedChunks().length);

      message.append(" §7Spieler: §c");
      message.append(w.getPlayerCount());

      message.append(" §7Entities: §c");
      message.append(w.getEntityCount());

      sender.sendMessage(message.toString());
    }
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

}
