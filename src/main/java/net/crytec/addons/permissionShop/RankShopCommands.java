/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.permissionShop.RankShopCommands can not be copied and/or distributed without the express
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

package net.crytec.addons.permissionShop;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("rangshop")
@CommandPermission("ct.rangshop")
public class RankShopCommands extends BaseCommand {

  private final LuckPermRankShop plugin;

  public RankShopCommands(final LuckPermRankShop plugin) {
    this.plugin = plugin;
  }

  @Default
  public void rankShopBase(final Player sender, final CommandHelp help) {
    help.showHelp();
  }

  @Subcommand("reload")
  @CommandPermission("lpr.admin.reload")
  public void adminReload(final CommandIssuer issuer) {
		plugin.reloadConfiguration();
		plugin.reload();
    issuer.sendMessage(F.main("Rangshop", "Die Konfiguration wurde neu geladen."));
  }

}
