/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.ClearInventory can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("clear|ci")
@CommandPermission("ct.clearinventory")
public class ClearInventory extends BaseCommand {

  @Default
  @CommandCompletion("@players")
  public void clearInventory(final Player sender, @Optional final OnlinePlayer target) {

    if (target == null) {
      sender.getInventory().clear();
      sender.sendMessage(F.main("Admin", "Dein Inventar wurde geleert!"));
    } else {
      target.getPlayer().getInventory().clear();
      target.getPlayer().sendMessage(F.main("Admin", "Dein Inventar wurde von " + F.name(sender.getDisplayName())) + " geleert!");
    }
  }
}