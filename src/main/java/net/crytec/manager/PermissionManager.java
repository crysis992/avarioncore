/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.manager.PermissionManager can not be copied and/or distributed without the express
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

package net.crytec.manager;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PermissionManager {

  @Getter
  private final LuckPerms api;


  public PermissionManager() {
    api = setupPermHook();
  }

  private LuckPerms setupPermHook() {
    final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager()
        .getRegistration(LuckPerms.class);
    if (provider != null) {
      return provider.getProvider();
    }
    return null;
  }

  public boolean setPlayerPrefix(final Player player, final String prefix, final int priority) {
    final User user = api.getUserManager().getUser(player.getUniqueId());
    final Node node = PrefixNode.builder(prefix, priority).build();

    final DataMutateResult result = user.data().add(node);

    api.getUserManager().saveUser(user);
    return result.wasSuccessful();
  }

  public void removePrefixWithPriority(final Player player, final int priority) {
    final User user = api.getUserManager().getUser(player.getUniqueId());

    user.data().clear(n -> n.getType() == NodeType.PREFIX);
    api.getUserManager().saveUser(user);
  }

  public void addPlayerPermission(final Player player, final String permission) {
    final User user = api.getUserManager().getUser(player.getUniqueId());
    final Node node = PermissionNode.builder().permission(permission).build();

    final DataMutateResult result = user.data().add(node);
    api.getUserManager().saveUser(user);
  }


  public String getPriority(final Player player) {
    final CachedMetaData metaData = api.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData(QueryOptions.nonContextual());

    final String meta = metaData.getMetaValue("tab-order");

    return (meta == null) ? "99" : meta;
  }

  public ChatColor getPlayerColor(final Player player) {
    final CachedMetaData metaData = api.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData(QueryOptions.nonContextual());

    final String meta = metaData.getMetaValue("rangcolor");

    return ChatColor.getByChar((meta == null) ? "&7" : meta);
  }

  public String getPlayerPrefix(final Player player) {
    final CachedMetaData metaData = api.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData(QueryOptions.nonContextual());
    final String prefix = metaData.getPrefix();
    return ChatColor.translateAlternateColorCodes('&', (prefix == null) ? "" : prefix);
  }
}