/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Repair can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.crytec.libs.commons.utils.language.LanguageAPI;
import net.crytec.util.F;
import net.crytec.util.UtilGear;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("repair|fix")
@CommandPermission("ct.repair")
public class Repair extends BaseCommand {

  @Default
  public void repair(final Player sender) {

    if (UtilGear.isRepairable(sender.getInventory().getItemInMainHand())) {

      final ItemStack item = sender.getInventory().getItemInMainHand();
      final ItemMeta meta = item.getItemMeta();

      if (((Damageable) meta).getDamage() == 0) {
        sender.sendMessage(F.main("Repair", "Dieses Item ist nicht beschädigt."));
        return;
      }
      ((Damageable) meta).setDamage(0);
      item.setItemMeta(meta);
      final String itemname = LanguageAPI.getItemName(sender.getInventory().getItemInMainHand());
      sender.sendMessage(F.main("Repair", "Du hast " + F.name(itemname) + " repariert."));
    } else {
      sender.sendMessage(F.error("Dieses Item lässt sich nicht reparieren!"));
    }
  }

  @Subcommand("all")
  @CommandPermission("ct.repair.all")
  public void repairAll(final Player sender) {

    for (final ItemStack item : sender.getInventory().getContents()) {
      final ItemMeta meta = item.getItemMeta();

      if (UtilGear.isRepairable(item) && (((Damageable) meta).getDamage() > 0)) {
        final String itemname = LanguageAPI.getItemName(item);
        sender.sendMessage(F.main("Repair", "Du hast " + F.name(itemname) + " repariert."));
        ((Damageable) meta).setDamage(0);
        item.setItemMeta(meta);
      }
    }
  }
}