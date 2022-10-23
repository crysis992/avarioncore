/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.TeleportPosition can not be copied and/or distributed without the express
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

package net.crytec.commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("tppos")
@CommandPermission("ct.tppos")
public class TeleportPosition extends BaseCommand {


  @Default
  @CommandCompletion("@nothing @nothing @nothing @worlds @players")
  @Syntax("<X> <Y> <Z> <Welt> [Spieler]")
  public void teleportPosition(final CommandSender sender, final int x, final int y, final int z, final World world, @Optional final OnlinePlayer player) {

    if (player == null) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(F.error("Dieser Command kann nur von einem Spieler ausgeführt werden."));
        return;
      }

      final Player p = (Player) sender;
      p.teleport(new Location(world, x, y, z));
      UtilPlayer.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT);
    } else {

      player.getPlayer().teleport(new Location(world, x, y, z));
      UtilPlayer.playSound(player.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT);
      sender.sendMessage(F.main("Telepot", F.name(player.getPlayer().getDisplayName()) + " wurde an die angegebene Position teleportiert."));
    }
  }
}