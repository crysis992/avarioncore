/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Speed can not be copied and/or distributed without the express
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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("speed")
@CommandPermission("ct.speed")
public class Speed extends BaseCommand {

  @Default
  @CommandCompletion("@range:5 @players")
  public void setSpeed(final Player sender, int speed, @Optional final OnlinePlayer op) {

    if (op == null) {
      if (speed > 5) {
        speed = 5;
      }
      final float s = (float) speed / 10;

      if (sender.isFlying()) {
        sender.setFlySpeed(s);
        sender.sendMessage(F.main("Speed", "Deine Fluggeschwindigkeit wurde auf " + speed + " gesetzt."));
        return;
      } else {
        sender.setWalkSpeed(s);
        sender.sendMessage(F.main("Speed", "Deine Laufgeschwindigkeit wurde auf " + speed + " gesetzt."));
        return;
      }

    } else {
      if (!sender.hasPermission("ct.speed.others")) {
        sender.sendMessage(Bukkit.getPermissionMessage());
        return;
      }
      if (speed > 5) {
        speed = 5;
      }
      final float s = (float) speed / 10;
      final Player target = op.getPlayer();

      if (target.isFlying()) {
        target.setFlySpeed(s);
        target.sendMessage(F.main("Speed", "Deine Fluggeschwindigkeit wurde auf " + speed + " gesetzt."));
        sender.sendMessage(F.main("Speed", "Fluggeschwindigkeit von " + F.name(target.getDisplayName()) + " wurde auf " + speed + " gesetzt."));
        return;
      } else {
        target.setWalkSpeed(s);
        target.sendMessage(F.main("Speed", "Deine Laufgeschwindigkeit wurde auf " + speed + " gesetzt."));
        sender.sendMessage(F.main("Speed", "Laufgeschwindigkeit von " + F.name(target.getDisplayName()) + " wurde auf " + speed + " gesetzt."));
        return;
      }


    }
  }
}
