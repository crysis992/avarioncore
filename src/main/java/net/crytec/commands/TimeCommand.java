/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.TimeCommand can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("time|settime")
@CommandPermission("ct.time")
@Description("Erlabut es dir die Zeit zu ändern.")
public class TimeCommand extends BaseCommand {

//  private final PhoenixRecharge recharge;

  @Default
  public void setTimeCommand(final Player issuer, final int time) {
    setTime(issuer, time);
  }

  @Subcommand("day")
  public void setDay(final Player issuer) {
    setTime(issuer, 1000);
  }

  @Subcommand("night")
  public void setNight(final Player issuer) {
    setTime(issuer, 14000);
  }

  private void setTime(final Player issuer, final int time) {
//		if (time < 0 || time > 24000) {
//			issuer.sendMessage(F.error(time + " muss zwischen 0 und 24000 sein."));
//			return;
//		}
//
//		if (!recharge.isUsable(issuer, "timeSet")) {
//			//FIXME Implement remaining time
////			long remain = Recharge.Instance.get(issuer).get("timeSet").getRemaining();
//			issuer.sendMessage(F.error("Dieser Command kann nur alle 30 Minuten genutzt werden."));
////			issuer.sendMessage("§e" + UtilTime.getTimeUntil(remain) + "§7 verbleibend.");
//			return;
//		}
//
//		final String timeformat = DateTickFormat.format24(issuer.getWorld().getTime());
//		final String worldname = issuer.getWorld().getName();
//
//		issuer.getWorld().setTime(time);
//		Bukkit.broadcastMessage("§7Die Zeit in Welt " + F.name(worldname) + " wurde von " + F.name(issuer.getDisplayName()) + " auf " + timeformat + " gesetzt");
//		recharge.use(issuer, "timeSet", "Zeit verändern", TimeUnit.MINUTES.toMillis(30), false, false);
//
//		UtilPlayer.playSound(issuer, Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE);
  }

}