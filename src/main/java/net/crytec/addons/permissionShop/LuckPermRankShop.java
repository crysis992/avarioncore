/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.permissionShop.LuckPermRankShop can not be copied and/or distributed without the express
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

package net.crytec.addons.permissionShop;

import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import net.crytec.AvarionCore;
import net.crytec.addons.Addon;
import net.crytec.addons.permissionShop.data.ShopNodeEntry;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.entity.Player;

public class LuckPermRankShop extends Addon {


  @Getter
  private LuckPerms api;

  private final Set<ShopNodeEntry> shopEntry = Sets.newLinkedHashSet();

  @Override
  public void onEnable() {
    api = LuckPermsProvider.get();
    AvarionCore.getPlugin().getCommandManager().registerCommand(new RankShopCommands(this));

    load();
  }

  @Override
  public void onDisable() {

  }

  @Override
  public String getModuleName() {
    return "PermissionShop";
  }

  private void load() {
//    final ConfigurationSection section = getConfig().getConfigurationSection("");
//
//    if (section == null) {
//      return;
//    }
//
//    for (final String key : section.getKeys(false)) {
//      final ConfigurationSection entry = section.getConfigurationSection(key);
//
//      final String displayname = ChatColor.translateAlternateColorCodes('&', entry.getString("displayname"));
//      final List<String> description = entry.getStringList("description").stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
//
//      final List<Node> permissions = Lists.newArrayList();
//
//      for (final String perm : entry.getStringList("permission")) {
//
//        final Builder builder;
//
//        if (perm.startsWith("meta:")) {
//          final String meta = perm.replace("meta:", "");
//          final String[] ar = meta.split(";");
//
//          final String metaKey = ar[0];
//          final String metaValue = ar[1];
//
//          builder = LuckPerms.getApi().getNodeFactory().makeMetaNode(metaKey, metaValue);
//        } else {
//
//          builder = LuckPerms.getApi().getNodeFactory().newBuilder(perm);
//
//          if (!entry.getString("server", "global").equals("global")) {
//            builder.setServer(entry.getString("server"));
//          }
//
//          if (!entry.getString("world", "global").equals("global")) {
//            builder.setWorld(entry.getString("world"));
//          }
//
//        }
//
//        permissions.add(builder.build());
//      }
//
//      final double price = entry.getDouble("price", 0D);
//
//      final int modelData = entry.getInt("modelData", 0);
//
//      final String tempIcon = entry.getString("icon", "PAPER");
//
//      String required = null;
//
//      if (entry.isSet("condition")) {
//        required = entry.getString("condition");
//      }
//
//      if (Material.getMaterial(tempIcon) == null) {
//        getPlugin().getLogger().severe("Failed to load Icon for entry " + key + ". There is no valid Material entry for the given input value:  " + tempIcon);
//        continue;
//      }
//
//      final Material icon = Material.getMaterial(tempIcon);
//
//      final ShopNodeEntry tmp = new ShopNodeEntry(key, displayname, description, permissions, price, icon, modelData, required);
//      shopEntry.add(tmp);
//    }
  }

  public Set<ShopNodeEntry> getShopContent() {
    return shopEntry;
  }

  // TODO Check for all permissions
  public boolean hasPurchased(final Player player, final ShopNodeEntry entry) {
    return true;
//    final User user = api.getUser(player.getUniqueId());
//
//    final Node node = entry.getNodes().get(0);
//    if (node.isMeta()) {
//      final Optional<Node> owningNode = user.getOwnNodes().stream().filter(n -> n.isMeta()).filter(n -> n.getMeta().getKey().equals(node.getMeta().getKey())).findFirst();
//      if (!owningNode.isPresent()) {
//        return false;
//      }
//
//      final String vo = owningNode.get().getMeta().getValue();
//      final String v1 = node.getMeta().getValue();
//
//      if (UtilMath.isInt(vo) && UtilMath.isInt(v1)) {
//        if (Integer.parseInt(vo) >= Integer.parseInt(v1)) {
//          return true;
//        } else {
//          return false;
//        }
//      } else {
//        return false;
//      }
//    }
//
//    return user.hasPermission(entry.getNodes().get(0)).asBoolean();
  }

  public Optional<ShopNodeEntry> getByID(final String id) {
    return shopEntry.stream().filter(node -> node.getId().equals(id)).findFirst();
  }

  public void reload() {
    shopEntry.clear();
    load();
  }

}