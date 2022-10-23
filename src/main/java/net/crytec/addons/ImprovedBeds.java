/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.ImprovedBeds can not be copied and/or distributed without the express
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

package net.crytec.addons;

import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ImprovedBeds extends Addon implements Listener {

  private static final int percentage = 51;

  @Override
  protected void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, getPlugin());
  }

  @Override
  protected void onDisable() {
  }

  @Override
  public String getModuleName() {
    return "Improved Beds";
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    if (event.getPlayer().isSleepingIgnored()) {
      event.getPlayer().setSleepingIgnored(false);
    }
  }

  @EventHandler
  public void onBedLeave(final PlayerBedLeaveEvent event) {
    if (event.getPlayer().isSleepingIgnored()) {
      event.getPlayer().setSleepingIgnored(false);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onBedEnter(final PlayerBedEnterEvent event) {
    final World world = event.getPlayer().getWorld();

		if (world.getPlayerCount() == 1) {
			return;
		}

    final int players = world.getPlayerCount();
    final int required = Math.round((percentage * players) / 100);

    int sleeping = 0;

    for (final Player player : world.getPlayers()) {
      if (player.isSleeping() || player.isSleepingIgnored()) {
        sleeping++;
      }
    }

    final double res = Math.round(((float) (sleeping * 100) / players) * 100.0) / 100.0;

    final int missing = required - sleeping;

    Bukkit.getLogger().warning("DEBUG: World: " + world.getName() + " Sleeping: " + sleeping + " Total: " + players + " Required: " + required + " percentage: " + res);

    if (missing > 0) {
      for (final Player player : event.getPlayer().getWorld().getPlayers()) {
        player.sendMessage(F.main("Info", F.name(event.getPlayer().getDisplayName()) + " möchte schlafen. Es fehlen " + F.elem(String.valueOf((required - sleeping))) + " Spieler um die Nacht zu überspringen."));
      }
      Bukkit.getLogger().warning(F.name(event.getPlayer().getDisplayName()) + " möchte schlafen. Es fehlen " + F.elem(String.valueOf((sleeping - required))) + " Spieler um die Nacht zu überspringen.");
    }

    if (res >= percentage) {
      System.out.println("More than 51% are sleeping - Attempting to skip the night.");
      for (final Player player : world.getPlayers()) {
        if (!player.isSleeping() && !player.isSleepingIgnored()) {
          player.setSleepingIgnored(true);
          System.out.println("Set " + player.getName() + " to sleepingIgnored");
        }
      }
    }
  }
}