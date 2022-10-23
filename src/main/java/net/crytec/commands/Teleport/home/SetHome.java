/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.home.SetHome can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import java.util.UUID;
import net.crytec.internal.CorePlayer;
import net.crytec.internal.settings.HomeSetting;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("sethome")
public class SetHome extends BaseCommand {

  @Default
  public void setHome(final Player player, @Single final String home) {

    final HomeSetting homes = CorePlayer.get(player).getData(HomeSetting.class);
    final int max = getMaxHomes(player.getUniqueId());

    if (home.contains("-") || home.contains(";") || home.contains(".")) {
      player.sendMessage(F.error("Dieser Name enthält ungültige Zeichen!"));
      return;
    }

    if (homes.getHomeAmount() >= max) {
      player.sendMessage(F.error("Du hast bereits die maximale anzahl an Homepunkten erreicht."));
      return;
    }

    homes.addHome(home, player.getLocation());
    player.sendMessage(F.main("Home", "Du hast einen neuen Homepunkt angelegt."));
  }

  //FIXME implement new LuckPerms API
  private int getMaxHomes(final UUID uuid) {
    return 5;
  }
//  private int getMaxHomes(final UUID uuid) {
//    final User user = AvarionCore.getPlugin().getPermissionManager().getApi().getUser(uuid);
//
//    if (user == null) {
//      return 1;
//    }
//
//    final UserData cachedData = user.getCachedData();
//    final Contexts contexts = AvarionCore.getPlugin().getPermissionManager().getApi().getContextForUser(user).get();
//    final MetaData metaData = cachedData.getMetaData(contexts);
//
//    final String toParse = metaData.getMeta().getOrDefault("homelimit", "1");
//
//    return UtilMath.isInt(toParse) ? Integer.parseInt(toParse) : 1;
//  }
}