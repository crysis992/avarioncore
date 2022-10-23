/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.TopCommand can not be copied and/or distributed without the express
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
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@CommandAlias("top")
@CommandPermission("ct.top")
public class TopCommand extends BaseCommand {

  @Default
  public void teleportToTop(final Player sender) {

    final Location target = sender.getLocation().getWorld().getHighestBlockAt(sender.getLocation()).getLocation();

    if (target.getY() == sender.getLocation().getY()) {
      sender.sendMessage(F.error("Du bist bereits am höchsten Punkt der Map!"));
      return;
    }

    target.setY(target.getY() + 1);
    sender.teleport(target, TeleportCause.COMMAND);
    UtilPlayer.playSound(sender, Sound.ENTITY_ENDERMAN_TELEPORT);
    sender.sendMessage(F.main("Teleport", "Du wurdest zum höchten Block der Map teleportiert."));
  }
}