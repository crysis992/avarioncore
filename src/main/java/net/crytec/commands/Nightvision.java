/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Nightvision can not be copied and/or distributed without the express
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
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CommandAlias("nightvision|nv|nachtsicht")
@CommandPermission("ct.nightvision")
@Description("Erlaubt es dir im dunkeln zu sehen.")
public class Nightvision extends BaseCommand {


  @Default
  @CommandCompletion("@players")
  public void activateNightVision(final Player sender, @Optional final OnlinePlayer op) {

    if (op != null) {
      final Player target = op.getPlayer();
      final boolean mode = switchNightVision(target);
      sender.sendMessage(F.main("Nachtsicht", "Nachtsicht für Spieler " + F.name(target.getDisplayName()) + " wurde " + F.ctf(mode, "§2aktiviert", "§4deaktiviert")));
      target.sendMessage(F.main("Nachtsicht", F.name(sender.getDisplayName()) + " hat Nachtsicht für dich " + F.ctf(mode, "§2aktiviert", "§4deaktiviert")));
      UtilPlayer.playSound(target, Sound.ENTITY_SPLASH_POTION_THROW, 0.5F, 0.9F);
    } else {
      sender.sendMessage(F.main("Nachtsicht", "Nachtsicht wurde " + F.ctf(switchNightVision(sender), "§2aktiviert", "§4deaktiviert")));
      UtilPlayer.playSound(sender, Sound.ENTITY_SPLASH_POTION_THROW, 0.5F, 0.9F);
    }
  }

  private boolean switchNightVision(final Player target) {
    if (target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      target.removePotionEffect(PotionEffectType.NIGHT_VISION);
      return false;
    } else {
      target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false, true), true);
      return true;
    }
  }
}