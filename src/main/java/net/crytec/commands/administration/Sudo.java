/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Sudo can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.Description;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.util.F;

@CommandAlias("sudo")
@CommandPermission("ct.sudo")
@Description("Führt einen Command für einen anderen Spieler aus.")
public class Sudo extends BaseCommand {

  @Default
  @CommandCompletion("@players @nothing")
  public void sudo(final CommandIssuer sender, final OnlinePlayer target, final String command) {
    target.getPlayer().performCommand(command);
    sender.sendMessage(F.main("Admin", "Du hast " + F.elem("/" + command) + " für " + F.name(target.getPlayer().getDisplayName()) + " ausgeführt"));
  }
}
