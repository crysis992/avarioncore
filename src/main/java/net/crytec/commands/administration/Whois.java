/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Whois can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.AvarionCore;
import net.crytec.libs.commons.utils.UtilLoc;
import net.crytec.libs.commons.utils.UtilMath;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.util.F;
import org.apache.commons.lang.StringUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;


@CommandAlias("whois")
@CommandPermission("ct.whois")
public class Whois extends BaseCommand {

  @Default
  @CommandCompletion("@players")
  public void whoisResult(final CommandIssuer sender, final OnlinePlayer player) {

    final Player target = player.getPlayer();

    sender.sendMessage("§7 - Nickname: " + AvarionCore.getPlugin().getPermissionManager().getPlayerPrefix(target) + " " + target.getName());
    sender.sendMessage("§7 - Gesundheit: §c" + Math.round(target.getHealth()) + "§4/§c" + (int) Math.round(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    sender.sendMessage("§7 - EXP: " + UtilMath.trim(2, (double) target.getExp()) + " (Level: " + target.getLevel() + ")");
    sender.sendMessage("§7 - Position: " + UtilLoc.getLogString(target.getLocation()));
    sender.sendMessage("§7 - IP: " + target.getAddress().getHostString());
    sender.sendMessage("§7 - Spielmodus: " + StringUtils.capitalize((target.getGameMode().name().toLowerCase())));
    sender.sendMessage("§7 - OP: " + F.tf(target.isOp()));
    sender.sendMessage("§7 - Flugmodus: " + F.ctf(target.isFlying(), "§2fliegt", "§cfliegt nicht"));
    sender.sendMessage("§7 - Erster Besuch: " + UtilTime.when(target.getFirstPlayed()));
  }
}