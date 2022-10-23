/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Heal can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

@CommandAlias("heal")
@CommandPermission("ct.heal")
@Description("Heilt dich oder den angegebenen Spieler.")
public class Heal extends BaseCommand {

  @Default
  @CommandCompletion("@players")
  public void healTarget(final Player sender, @Optional final OnlinePlayer op) {

    if (op != null) {
      final Player target = op.getPlayer();
			heal(target);
      sender.sendMessage(F.main("Heal", "Du hast " + F.name(target.getDisplayName()) + " geheilt."));
      target.sendMessage(F.main("Heal", "Du wurdest von " + F.name(sender.getDisplayName()) + " geheilt."));
      UtilPlayer.playSound(target, Sound.ENTITY_WITCH_DRINK, 0.5F, 0.9F);
    } else {
			heal(sender);
      sender.sendMessage(F.main("Heal", "Du wurdest geheilt."));
      UtilPlayer.playSound(sender, Sound.ENTITY_WITCH_DRINK, 0.5F, 0.9F);
    }
  }

  private void heal(final Player p) {
    p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    p.setFoodLevel(20);
    p.setSaturation(5F);
    p.getActivePotionEffects().forEach(potion -> p.removePotionEffect(potion.getType()));
    p.setFireTicks(0);
  }
}