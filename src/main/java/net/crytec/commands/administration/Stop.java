/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Stop can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import net.crytec.AvarionCore;
import net.crytec.util.F;
import org.bukkit.Bukkit;

@CommandAlias("cstop")
@CommandPermission("ct.stop")
public class Stop extends BaseCommand {

  private static final int SHUTDOWN_SECONDS = 10;
  private static final int SHUTDOWN_TICKS = SHUTDOWN_SECONDS * 20;

  @Default
  @Description("Startet einen 10 Sekunden Countdown. Anschließend wird der Server gestoppt.")
  public void stopServer(final CommandIssuer sender) {

    sender.sendMessage(F.main("Admin", "Der Server fährt in 10 Sekunden herunter."));

    Bukkit.getOnlinePlayers().forEach(player -> {

      player.sendTitle("Server Shutdown", "In " + SHUTDOWN_SECONDS + " Sekunden", 10, 40, 10);

    });

    Bukkit.getScheduler().runTaskLater(AvarionCore.getPlugin(), () -> {
      Bukkit.savePlayers();
      Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(Bukkit.getShutdownMessage()));
      Bukkit.getWorlds().forEach(world -> world.save());

      Bukkit.getScheduler().runTaskLater(AvarionCore.getPlugin(), () -> {
        Bukkit.shutdown();
      }, 10L);

    }, SHUTDOWN_TICKS);

  }

  @Subcommand("now")
  @Description("Stoppt den Server sofort.")
  public void stopNow(final CommandIssuer issuer) {
    Bukkit.savePlayers();
    Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(Bukkit.getShutdownMessage()));
    Bukkit.getWorlds().forEach(world -> world.save());
    Bukkit.shutdown();
  }
}
