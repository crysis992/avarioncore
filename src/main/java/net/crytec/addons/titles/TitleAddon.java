/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.titles.TitleAddon can not be copied and/or distributed without the express
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

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.crytec.addons.Addon;
import net.crytec.inventoryapi.SmartInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Player;

public class TitleAddon extends Addon {

  private final Set<Title> titles = Sets.newHashSet();

  @Override
  protected void onEnable() {
    setConfigEntry("title.reisender.displayname", "&7[Reisender]");
    setConfigEntry("title.reisender.permission", "title.reisender");
    setConfigEntry("title.reisender.icon", Material.OAK_SIGN.toString());
    setConfigEntry("title.reisender.description", Arrays.asList("&7Diesen Titel erhälst du nach", "&7erfolgreicher freischaltung auf", "&7unserem Server."));

    final ConfigurationSection section = getConfig().getConfigurationSection("title");
    if (section == null) {
      return;
    }

    for (final String title : section.getKeys(false)) {
      final ConfigurationSection entry = section.getConfigurationSection(title);

      final String display = entry.getString("displayname");
      final String permission = entry.getString("permission", "");
      final List<String> description = entry.getStringList("description").stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
      Material icon = Material.OAK_SIGN;
      if (EnumUtils.isValidEnum(Material.class, entry.getString("icon", "SIGN"))) {
        icon = Material.valueOf(entry.getString("icon", "SIGN"));
      }
      titles.add(new Title(display, permission, icon, description));
    }

    //TODO Fix Title NPC
//    AvarionCore.getPlugin().getNpcDataHandler().registerAttachment(new TitleNPCAttachment(this));
    log("Loaded " + titles.size() + " Titles");
  }

  public Set<Title> getAllTitles() {
    return titles;
  }

  @Override
  protected void onDisable() {
  }

  @Override
  public String getModuleName() {
    return "Titles";
  }

  public void openTitleInterface(final Player player) {
    SmartInventory.builder().size(4).provider(new TitleGUI(this)).title("§lDeine Titel").build().open(player);
  }
}