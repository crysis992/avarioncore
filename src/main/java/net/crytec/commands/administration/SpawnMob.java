/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.SpawnMob can not be copied and/or distributed without the express
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
import java.util.concurrent.ThreadLocalRandom;
import net.crytec.libs.commons.utils.language.LanguageAPI;
import net.crytec.util.F;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@CommandAlias("spawnmob")
@CommandPermission("ct.spawnmob")
public class SpawnMob extends BaseCommand {


  @Default
  @CommandCompletion("@entitytype")
  public void spawnMob(final Player sender, final EntityType type, @Default("1") final Integer amount) {

    for (int i = 0; i <= amount; i++) {
      final Location l = sender.getLocation().clone().add(ThreadLocalRandom.current().nextInt(-2, 2), 0, ThreadLocalRandom.current().nextInt(-2, 2));
      sender.getWorld().spawnEntity(l, type);
    }

    sender.sendMessage(F.main("Admin", "Du hast " + LanguageAPI.getEntityName(type) + " gespawnt."));
  }
}
