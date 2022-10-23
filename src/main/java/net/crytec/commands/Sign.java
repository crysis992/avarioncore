/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Sign can not be copied and/or distributed without the express
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
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.google.common.collect.Lists;
import java.util.List;
import net.crytec.AvarionCore;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@CommandAlias("sign")
@CommandPermission("ct.itemsign")
@Description("Erlaubt es dir ein Item zu gravieren.")
public class Sign extends BaseCommand {

  private final NamespacedKey key;

  private final NamespacedKey signedAt;
  private final NamespacedKey signedBy;

  public Sign(final AvarionCore plugin) {
		key = new NamespacedKey(plugin, "itemsigning");
		signedAt = new NamespacedKey(plugin, "signedAt");
		signedBy = new NamespacedKey(plugin, "signedBy");
  }

  @Default
  public void signItem(final Player issuer, final String text) {

    if (issuer.getInventory().getItemInMainHand() == null) {
      issuer.sendMessage(F.error("Du musst ein Item in der Hand halten."));
      return;
    }

    final ItemStack item = issuer.getInventory().getItemInMainHand();
    final ItemMeta meta = item.getItemMeta();

    if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.TAG_CONTAINER)) {
      issuer.sendMessage(F.error("Dieses Item hat bereits eine gravur."));
      return;
    }

    final List<String> lore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();

    lore.add("");
    lore.add("§fPersönliche Nachricht von §6" + issuer.getName() + ":");

    final String F = WordUtils.wrap(text, 20);
    final String[] lines = F.split(System.lineSeparator());

    for (final String line : lines) {
      lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', line));
    }

    meta.setLore(lore);

    final PersistentDataContainer container = meta.getPersistentDataContainer();

    final PersistentDataContainer signedData = container.getAdapterContext().newPersistentDataContainer();
    signedData.set(signedAt, PersistentDataType.LONG, System.currentTimeMillis());
    signedData.set(signedBy, PersistentDataType.STRING, issuer.getUniqueId().toString());

    container.set(key, PersistentDataType.TAG_CONTAINER, signedData);

    item.setItemMeta(meta);

    issuer.getInventory().setItemInMainHand(item);

    UtilPlayer.playSound(issuer, Sound.ITEM_BOOK_PAGE_TURN);
    issuer.sendMessage("Du hast folgendes eingraviert: " + ChatColor.translateAlternateColorCodes('&', text));
  }

}
