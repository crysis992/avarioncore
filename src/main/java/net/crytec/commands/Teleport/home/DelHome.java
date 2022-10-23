/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.home.DelHome can not be copied and/or distributed without the express
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

package net.crytec.commands.Teleport.home;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import net.crytec.internal.CorePlayer;
import net.crytec.internal.settings.HomeSetting;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("delhome")
public class DelHome extends BaseCommand {

  @Default
  @CommandCompletion("@userhomes")
  public void deleteHome(final Player player, @Single final String home) {
    final HomeSetting homes = CorePlayer.get(player).getData(HomeSetting.class);

    if (homes.getHomeAmount() == 0) {
      player.sendMessage(F.error("Du hast keine Homepunkte."));
      return;
    }

    if (homes.getHome(home) == null) {
      player.sendMessage(F.error("Es existiert kein Homepunkt mit dem Namen " + F.elem(home)));
      return;
    }

    homes.deleteHome(home);
    player.sendMessage(F.main("Home", "Der angegebende Homepunkt wurde gelöscht."));
  }
}