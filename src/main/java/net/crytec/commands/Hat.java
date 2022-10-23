/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Hat can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.libs.commons.utils.UtilInv;
import net.crytec.libs.commons.utils.language.LanguageAPI;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("hat")
@CommandPermission("ct.hat")
@Description("Setzt dir einen Block auf den Kopf")
public class Hat extends BaseCommand {

  @Default
  @CommandCompletion("@players")
  @Conditions("iteminhand")
  public void hatTarget(final Player sender, @Optional final OnlinePlayer op) {

    if (op != null) {
      if (!sender.hasPermission("ct.hat.others")) {
        sender.sendMessage(F.error(Bukkit.getPermissionMessage()));
        return;
      }

      final Player target = op.getPlayer();

      if (target.getInventory().getHelmet() != null) {
        UtilInv.insert(target, target.getInventory().getHelmet(), true);
      }

      final String itemname = LanguageAPI.getItemName(sender.getInventory().getItemInMainHand());

      sender.sendMessage(F.main("Hat", "Du hast " + F.name(target.getDisplayName() + " ") + F.name(itemname) + " auf den Kopf gesetzt."));
      target.sendMessage(F.main("Hat", F.name(sender.getDisplayName()) + " hat dir " + F.name(itemname) + " auf den Kopf gesetzt."));
      target.getInventory().setHelmet(sender.getInventory().getItemInMainHand());
    } else {
      if (sender.getInventory().getHelmet() != null) {
        UtilInv.insert(sender, sender.getInventory().getHelmet());
      }

      final String itemname = LanguageAPI.getItemName(sender.getInventory().getItemInMainHand());
      sender.sendMessage(F.main("Hat", "Du hast " + F.name(itemname) + " auf den Kopf gesetzt."));
      sender.getInventory().setHelmet(sender.getInventory().getItemInMainHand());
      if (sender.getGameMode() != GameMode.CREATIVE) {
        sender.getInventory().setItemInMainHand(null);
      }
    }
  }
}
