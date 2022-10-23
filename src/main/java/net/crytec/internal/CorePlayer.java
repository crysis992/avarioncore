package net.crytec.internal;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import net.crytec.AvarionCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CorePlayer {

  private static final HashMap<UUID, CorePlayer> players = Maps.newHashMap();

  @Getter
  private final UUID uuid;
  protected static PlayerDataManager manager;

  public CorePlayer(final UUID uuid) {
    this.uuid = uuid;
    players.put(uuid, this);

    final Player player = Bukkit.getPlayer(uuid);
//    if (player != null && player.isOnline()) {
//
//      final User user = AvarionCore.getPlugin().getPermissionManager().getApi().getUser(this.uuid);
//      final UserData cachedData = user.getCachedData();
//      final Optional<Contexts> contexts = AvarionCore.getPlugin().getPermissionManager().getApi().getContextForUser(user);
//
//      if (contexts.isPresent()) {
//        final MetaData metaData = cachedData.getMetaData(contexts.get());
//        final String rangcolor = metaData.getMeta().getOrDefault("rangcolor", "&f");
//        player.setDisplayName(ChatColor.translateAlternateColorCodes('&', rangcolor) + player.getName());
//      }
//    }
  }

  public void reloadData() {
    manager.loadData(AvarionCore.getPlugin(), uuid);
  }

  public static CorePlayer get(final UUID uuid) {
    if (!players.containsKey(uuid)) {
      new CorePlayer(uuid);
    }
    return players.get(uuid);
  }

  public static CorePlayer get(final Player player) {
    return get(player.getUniqueId());
  }

  public void unload() {
    players.remove(uuid);
  }

  public <T extends PlayerData> T getData(final Class<? extends PlayerData> clazz) {
    return manager.getPlayerData(uuid, AvarionCore.getPlugin(), clazz);
  }
}