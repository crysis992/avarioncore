/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.loreDisplay.LoreDisplayAddon can not be copied and/or distributed without the express
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

package net.crytec.addons.loreDisplay;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.crytec.addons.Addon;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;

public class LoreDisplayAddon extends Addon {

  private final HashMap<String, Pair<String, List<String>>> nbtInfo = Maps.newHashMap();

  @Override
  protected void onEnable() {

    getPlugin().getCommandManager().registerCommand(new LoreDisplayCommand(this));

    final File config = new File(getPlugin().getDataFolder(), "iteminfo.yml");

    if (!config.exists()) {
      try {
        config.createNewFile();

        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);

        cfg.set("data.vcompass.displayname", "&6Kompass");
        cfg.set("data.vcompass.lore", Arrays.asList("&7Beschreibung", "&7Beschreibung"));

        cfg.save(config);

      } catch (final IOException ignored) {
      }
    }
    loadConfiguration();
    getPlugin().getLogger().info("Loaded " + nbtInfo.size() + " NBT ItemInfos");
    ProtocolLibrary.getProtocolManager().addPacketListener(new LorePacketAdapter(this));
  }

  @Override
  protected void onDisable() {
  }

  @Override
  public String getModuleName() {
    return "ItemPacketDescription";
  }

  public Optional<Pair<String, List<String>>> getInfo(final String key) {
    return Optional.ofNullable(nbtInfo.get(key));
  }

  public void loadConfiguration() {
    final File config = new File(getPlugin().getDataFolder(), "iteminfo.yml");
    final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);

    final ConfigurationSection section = cfg.getConfigurationSection("data");
    if (section == null) {
      return;
    }

    for (final String key : section.getKeys(false)) {
      final ConfigurationSection entry = section.getConfigurationSection(key);
      if (entry == null) {
        continue;
      }

      List<String> lore = null;
      String name = null;

      if (entry.isSet("lore")) {
        lore = entry.getStringList("lore").stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
      }

      if (entry.isSet("displayname")) {
        name = ChatColor.translateAlternateColorCodes('&', entry.getString("displayname", null));
      }

      nbtInfo.put(key, Pair.of(name, lore));
    }
  }
}