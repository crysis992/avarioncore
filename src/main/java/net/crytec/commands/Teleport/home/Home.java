/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.home.Home can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.crytec.internal.CorePlayer;
import net.crytec.internal.settings.HomeSetting;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@CommandAlias("home")
public class Home extends BaseCommand {


  @Default
  @CommandCompletion("@userhomes")
  public void homeCommand(final Player player, @Single @Optional final String home) {

    final HomeSetting homes = CorePlayer.get(player).getData(HomeSetting.class);

    if (home == null) {
      if (homes.getHomes().size() == 1) {

        final Location destination = homes.getHomes().values().stream().findFirst().get();
        if (destination == null) {
          player.sendMessage(F.main("Home", "Der Homepunkt ist aktuell nicht erreichbar."));
          return;
        } else {
          player.teleport(destination);
          UtilPlayer.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);
        }
      }

      player.sendMessage(F.main("Home", "Deine Homepunkte (" + homes.getHomes().size() + "/" + getMaxHomes(player.getUniqueId()) + "):"));

      final List<String> homelist = new ArrayList<>(homes.getHomes().keySet());
      homelist.sort(String.CASE_INSENSITIVE_ORDER);
      player.sendMessage(F.main("Home", F.format(homelist, ", ", "Keine")));
    } else {

      if (!homes.hasHome(home)) {
        player.sendMessage(F.error("Der angegebende Homepunkt existiert nicht."));
        return;
      }

      final Location destination = homes.getHome(home);
      if (destination == null) {
        player.sendMessage(F.main("Home", "Der Homepunkt ist aktuell nicht erreichbar."));
        return;
      }

      player.sendMessage(F.main("Home", "Teleportiere nach " + home + "..."));
      player.teleport(destination, TeleportCause.COMMAND);
      UtilPlayer.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);
    }
  }

  @Subcommand("admin")
  @CommandPermission("ct.home.others")
  @Syntax("<Spieler> [Home]")
  public void homeAdmin(final Player player, final OfflinePlayer target, @Single @Optional final String home) {
    final HomeSetting homes = CorePlayer.get(player).getData(HomeSetting.class);

    if (home == null) {
      player.sendMessage(F.main("Home", "Homes von " + F.name(target.getName()) + "(" + homes.getHomes().size() + "/" + getMaxHomes(target.getUniqueId()) + "):"));

      final List<String> homelist = new ArrayList<>(homes.getHomes().keySet());
      homelist.sort(String.CASE_INSENSITIVE_ORDER);
      player.sendMessage(F.main("Home", F.format(homelist, ", ", "Keine")));
      return;
    }

    if (!homes.hasHome(home)) {
      player.sendMessage(F.error("Der angegebende Homepunkt existiert nicht."));
      return;
    }

    final Location destination = homes.getHome(home);
    if (destination == null) {
      player.sendMessage(F.main("Home", "Der Homepunkt ist aktuell nicht erreichbar."));
      return;
    }

    player.sendMessage(F.main("Home", "Teleportiere nach " + home + "..."));
    player.teleport(destination, TeleportCause.COMMAND);
    UtilPlayer.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);

  }

  public int getMaxHomes(final UUID uuid) {
    return 5;
  }

//  public int getMaxHomes(final UUID uuid) {
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