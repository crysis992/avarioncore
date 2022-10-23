/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.portal.TeleportWarmupThread can not be copied and/or distributed without the express
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

package net.crytec.addons.portal;

import java.util.function.Consumer;
import lombok.Getter;
import net.crytec.AvarionCore;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class TeleportWarmupThread implements Runnable {

  private final Player player;
  @Getter
  private final Consumer<Boolean> completed;
  @Getter
  private final Portal portal;
  @Getter
  private int countdown = 5;
  @Getter
  private final BukkitTask task;

  public TeleportWarmupThread(final Player player, final Portal portal, final Consumer<Boolean> completed) {
    this.player = player;
    this.portal = portal;
    this.completed = completed;
    player.sendTitle("§7Reise nach " + portal.getDisplayname(), "", 0, 20, 0);
    task = Bukkit.getScheduler().runTaskTimer(AvarionCore.getPlugin(), this, 20, 20);
    player.playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 0.5F, 0.79F);
  }

  @Override
  public void run() {
    if (countdown == 0) {
      completed.accept(true);
      player.resetTitle();
      task.cancel();
      return;
    } else if (countdown == 4) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false), true);
    } else if (countdown == 3) {
      player.playSound(player.getLocation(), Sound.ITEM_ELYTRA_FLYING, 1, 1);
    } else if (countdown == 2) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3, false, false), true);
    } else if (countdown == 1) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3, false, false), true);
    }

    player.sendTitle("", "§8Teleport in " + countdown + " Sekunden..", 0, 20, 0);
    countdown--;
  }
}