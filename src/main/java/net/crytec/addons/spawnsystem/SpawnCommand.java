/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.spawnsystem.SpawnCommand can not be copied and/or distributed without the express
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

package net.crytec.addons.spawnsystem;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.AllArgsConstructor;
import net.crytec.libs.commons.utils.UtilPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@CommandAlias("spawn")
@AllArgsConstructor
public class SpawnCommand extends BaseCommand {

  private final SpawnManager addon;

  @Default
  public void spawnTeleport(final Player issuer) {
    if (addon.getConfig().getBoolean("perWorldSpawn")) {
      if (addon.hasWorldSpawn(issuer.getWorld())) {
        issuer.teleport(addon.getSpawn(issuer.getWorld()), TeleportCause.COMMAND);
      } else {
        issuer.teleport(addon.getSpawn(), TeleportCause.COMMAND);
      }
      UtilPlayer.playSound(issuer, Sound.ENTITY_ENDERMAN_TELEPORT);
    }
    issuer.teleport(addon.getSpawn(), TeleportCause.COMMAND);
    UtilPlayer.playSound(issuer, Sound.ENTITY_ENDERMAN_TELEPORT);
  }
}