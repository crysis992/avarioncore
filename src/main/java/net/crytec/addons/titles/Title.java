/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.titles.Title can not be copied and/or distributed without the express
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

package net.crytec.addons.titles;

import java.util.List;
import lombok.Getter;
import net.crytec.AvarionCore;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Title {

  public Title(final String title, final String permission, final Material icon, final List<String> description) {
    this.title = title;
    this.permission = permission;
    this.icon = icon;
    this.description = description;
  }

  private final String title;
  @Getter
  private final String permission;
  @Getter
  private final Material icon;
  @Getter
  private final List<String> description;

  public boolean canUse(final Player player) {
    return player.hasPermission(permission);
  }

  public String getTitle() {
    return ChatColor.translateAlternateColorCodes('&', title);
  }

  public void setTitle(final Player player) {
    AvarionCore.getPlugin().getPermissionManager().setPlayerPrefix(player, title, 654);
    AvarionCore.getPlugin().getScoreBoardManager().setPrefix(player, ChatColor.translateAlternateColorCodes('&', getTitle()));
  }


  public ClickableItem getGUIRepresenter(final Player player, final TitleAddon addon) {
    final ItemBuilder builder = new ItemBuilder(getIcon());
    builder.name(getTitle());
    builder.lore(getDescription());

    if (!canUse(player)) {
      builder.lore("");
      builder.lore("§4Du hast diesen Title noch nicht freigeschaltet.");
    }

    return ClickableItem.of(builder.build(), e -> {

      if (e.isRightClick()) {
        AvarionCore.getPlugin().getPermissionManager().removePrefixWithPriority(player, 654);
        AvarionCore.getPlugin().getScoreBoardManager().setPrefix(player, AvarionCore.getPlugin().getPermissionManager().getPlayerPrefix(player));
        player.closeInventory();
        UtilPlayer.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.45F, 0.8F);
      } else {
        if (!canUse(player)) {
          return;
        }
        setTitle(player);
        player.closeInventory();
        UtilPlayer.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.45F, 0.1F);
      }
    });
  }
}