/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.Vanish can not be copied and/or distributed without the express
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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import java.util.Set;
import java.util.stream.Collectors;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Vanish extends Addon implements Listener {

  private NamespacedKey keyVanish;

  @Override
  protected void onEnable() {
		keyVanish = new NamespacedKey(getPlugin(), "vanish");
		getPlugin().getCommandManager().registerCommand(new VanishCommand());
    Bukkit.getPluginManager().registerEvents(this, getPlugin());
  }

  @Override
  protected void onDisable() {

  }

  @Override
  public String getModuleName() {
    return "Vanish";
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onJoin(final PlayerJoinEvent event) {
		getVanishedPlayers().forEach(this::hidePlayer);
  }

  public void hidePlayer(final Player player) {

    player.setCanPickupItems(false);
    player.getPersistentDataContainer().set(keyVanish, PersistentDataType.BYTE, (byte) 0);

    Bukkit.getOnlinePlayers().forEach(cur -> {
      if (!cur.hasPermission("ct.vanish.seeothers")) {
        cur.hidePlayer(getPlugin(), player);
      }
    });
  }

  public void showPlayer(final Player player) {
    player.setCanPickupItems(true);

    final PersistentDataContainer container = player.getPersistentDataContainer();
    if (container.has(keyVanish, PersistentDataType.BYTE)) {
      container.remove(keyVanish);
    }

    Bukkit.getOnlinePlayers().forEach(cur -> cur.showPlayer(getPlugin(), player));
  }

  public Set<Player> getVanishedPlayers() {
    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getPersistentDataContainer().has(keyVanish, PersistentDataType.BYTE)).collect(Collectors.toSet());
  }

  public boolean isVanished(final Player player) {
    return player.getPersistentDataContainer().has(keyVanish, PersistentDataType.BYTE);
  }

  @CommandAlias("vanish")
  @CommandPermission("ct.vanish")
  private class VanishCommand extends BaseCommand {

    @Default
    public void vanishCommand(final Player sender) {

      if (isVanished(sender)) {
				showPlayer(sender);
        sender.sendMessage(F.main("Admin", "Du bist wieder sichtbar!"));
      } else {
				hidePlayer(sender);
        sender.sendMessage(F.main("Admin", "Du bist jetzt unsichtbar!"));
      }
    }
  }
}