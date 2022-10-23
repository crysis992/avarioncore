/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Back can not be copied and/or distributed without the express
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
import net.crytec.libs.commons.utils.UtilLoc;
import net.crytec.util.F;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;

@CommandAlias("back")
@CommandPermission("ct.back")
public class Back extends BaseCommand {

  @Default
  public void backCommand(final Player sender) {

    if (sender.hasMetadata("back_location")) {
      sender.sendMessage(F.main("Teleport", "Teleportiere zur letzten bekannten Position"));

      final MetadataValue val = sender.getMetadata("back_location").get(0);
      final Location loc = UtilLoc.locFromString(val.asString());

      sender.teleport(loc, TeleportCause.COMMAND);
    } else {
      sender.sendMessage(F.main("Teleport", "Deine letzte Position ist nicht bekannt."));
    }
  }
}
