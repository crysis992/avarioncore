/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Gamemode can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.util.F;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm")
@CommandPermission("ct.gamemode")
public class Gamemode extends BaseCommand {

  @Default
  @CommandCompletion("@gamemode")
  @Syntax("<Spielmodus> [Spieler]")
  @Description("Erlabut es dir den Spielmodus zu wechseln.")
  public void gamemode(final Player sender, final GameMode mode, @Optional final OnlinePlayer op) {

    if (op != null) {
      final Player target = op.getPlayer();
      target.sendMessage(F.main("Admin", "Dein Spielmodus wurde in " + F.name(StringUtils.capitalize(mode.name().toLowerCase())) + " geändert!"));
      sender.sendMessage(F.main("Admin", "Du hast den Spielmodus von " + F.name(target.getDisplayName()) + " in " + F.name(StringUtils.capitalize(mode.name().toLowerCase())) + " geändert!"));
      target.setGameMode(mode);
    } else {
      sender.sendMessage(F.main("Admin", "Dein Spielmodus wurde in " + F.name(StringUtils.capitalize(mode.name().toLowerCase())) + " geändert!"));
      sender.setGameMode(mode);
    }
  }

}