/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Spawner can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.crytec.libs.commons.utils.language.LanguageAPI;
import net.crytec.util.F;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@CommandAlias("spawner")
@CommandPermission("ct.spawner")
@Description("Ändert den Spawner an deiner Zielposition zu einem anderen Typ.")
public class Spawner extends BaseCommand {

  @Default
  @CommandCompletion("@entitytype")
  public void setSpawnerType(final Player sender, final EntityType type) {
    final Block block = sender.rayTraceBlocks(10).getHitBlock();

    if (block == null || block.getType() != Material.SPAWNER) {
      sender.sendMessage(F.error("Kein Spawner Block gefunden."));
      return;
    }

    final CreatureSpawner spawner = (CreatureSpawner) block.getState();
    spawner.setSpawnedType(type);
    spawner.update();
    sender.sendMessage(F.main("Admin", "Der Spawnertype wurde in " + F.name(LanguageAPI.getEntityName(type)) + " geändert."));

  }
}