package net.crytec.internal;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.UUID;
import net.crytec.AvarionCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDataManager implements Listener {

  private final HashBiMap<UUID, Integer> internalPlayerIDs = HashBiMap.create();
  private final HashMap<Class<? extends JavaPlugin>, PluginStorage> plugins = Maps.newHashMap();

  private final AvarionCore plugin;

  public PlayerDataManager(final AvarionCore plugin) {
    this.plugin = plugin;
    CorePlayer.manager = this;
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void cacheDataOnLogin(final AsyncPlayerPreLoginEvent event) {
    final UUID uuid = event.getUniqueId();

    try {
      internalPlayerIDs.putIfAbsent(uuid, (internalPlayerIDs.size() + 1));
    } catch (final IllegalArgumentException exception) {
      plugin.getLogger().severe("[PhoenixAPI] Failed to initialize data for UUID " + uuid + "(" + event.getName() + "): " + exception.getMessage());
      Bukkit.getScheduler().runTask(plugin, () -> attemptLater(uuid));
    }
  }

  private void attemptLater(final UUID uuid) {
    try {
      internalPlayerIDs.putIfAbsent(uuid, (internalPlayerIDs.size() + 1));
    } catch (final IllegalArgumentException exception) {
      Bukkit.getLogger().severe("[PhoenixAPI] Failed to initialize data for UUID " + uuid.toString() + " Plugin related data may be broken until a relog!");
    }
  }

  public void registerStorageAdapter(final JavaPlugin plugin, final Class<? extends PlayerData> dataClass) {
    plugins.computeIfAbsent(plugin.getClass(), (c) -> new PluginStorage(plugin)).addStorageAdapter(dataClass);
  }

  public int getInternalId(final UUID uuid) {
    return internalPlayerIDs.get(uuid);
  }

  public int getInternalId(final Player player) {
    return internalPlayerIDs.get(player.getUniqueId());
  }

  public UUID getUUIDFromId(final int id) {
    return internalPlayerIDs.inverse().getOrDefault(id, null);
  }

  public PluginStorage getPluginStorage(final JavaPlugin plugin) {
    return plugins.get(plugin.getClass());
  }

  public <T extends PlayerData> T getPlayerData(final UUID player, final JavaPlugin plugin, final Class<? extends PlayerData> dataClass) {
    return getPluginStorage(plugin).getPlayerData(player, dataClass.getName());
  }

  public void loadData(final UUID uuid) {
    plugins.values().forEach(storage -> storage.loadPlayer(uuid));
  }

  public void loadData(final JavaPlugin plugin, final UUID uuid) {
    getPluginStorage(plugin).loadPlayer(uuid);
  }

  public void unloadData(final UUID uuid) {
    plugins.values().forEach(storage -> storage.unloadPlayer(uuid));
  }

  public void unloadData(final JavaPlugin plugin, final UUID uuid) {
    getPluginStorage(plugin).unloadPlayer(uuid);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void loadDataOnJoin(final PlayerJoinEvent event) {
    loadData(event.getPlayer().getUniqueId());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void unloadPlayerData(final PlayerQuitEvent event) {
    plugins.values().forEach(storage -> storage.unloadPlayer(event.getPlayer().getUniqueId()));
  }

  @EventHandler
  public void onPluginDisable(final PluginDisableEvent event) {
    if (plugins.containsKey(event.getPlugin().getClass())) {
      Bukkit.getOnlinePlayers().forEach(cur -> plugins.get(event.getPlugin().getClass()).unloadPlayer(cur.getUniqueId()));
      plugins.remove(event.getPlugin().getClass());
    }
  }
}