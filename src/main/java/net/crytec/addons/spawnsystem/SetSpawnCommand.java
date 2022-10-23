/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.spawnsystem.SetSpawnCommand can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import lombok.AllArgsConstructor;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("setspawn")
@CommandPermission("ct.setspawn")
@AllArgsConstructor
public class SetSpawnCommand extends BaseCommand {

  private final SpawnManager addon;

  @Default
  public void setSpawn(final Player issuer) {
    if (addon.getConfig().getBoolean("perWorldSpawn")) {
      addon.addWorldSpawn(issuer.getWorld(), issuer.getLocation());
      issuer.sendMessage(F.main(F.elem("Spawn"), "Spawnpunkt für die Welt " + F.name(issuer.getWorld().getName()) + " wurde festgelegt."));
    } else {
      addon.setMainSpawn(issuer.getLocation());
      issuer.sendMessage(F.main(F.elem("Spawn"), "Spawnpunkt wurde gesetzt."));
    }
  }

}
