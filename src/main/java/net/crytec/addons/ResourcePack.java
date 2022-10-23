/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.ResourcePack can not be copied and/or distributed without the express
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
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import net.crytec.AvarionCore;
import net.crytec.internal.network.HttpServer;
import net.crytec.internal.network.HttpServer.PhoenixConnection;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

public class ResourcePack implements Listener {

  public ResourcePack(final AvarionCore plugin) {
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(this, plugin);
    plugin.getCommandManager().registerCommand(new ReloadResource());
    resourcepacks = Maps.newHashMap();
    servedPack = Maps.newHashMap();

    final File resourceFolder = new File(plugin.getDataFolder(), "resourcepacks");

    if (!resourceFolder.exists()) {
      plugin.getDataFolder().mkdir();
    }

    final File file = new File(plugin.getDataFolder(), "resourcepacks.yml");

    if (!file.exists()) {
      try {
        file.createNewFile();

        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("ip", "127.0.0.1");
        cfg.set("port", 9555);
        cfg.set("defaultpack", "resources");
        cfg.save(file);
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }

    initialize();

    AvarionCore.getPlugin().getCommandManager().getCommandCompletions().registerStaticCompletion("resourcepacks", resourcepacks.keySet());

  }

  private final AvarionCore plugin;
  private final HashSet<UUID> attempts = Sets.newHashSet();
  private final HashMap<UUID, String> servedPack;
  private final HashMap<String, Pair<File, String>> resourcepacks;

  private HttpServer server;
  private String defaultPack;

  private int port;
  private String ip;

  private void initialize() {
    final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "resourcepacks.yml"));
    final File resourcefolder = new File(plugin.getDataFolder(), "resourcepacks");

    for (final File file : Files.fileTreeTraverser().breadthFirstTraversal(resourcefolder)) {
      if (!file.isFile()) {
        continue;
      }

      final String name = file.getName().replace(".zip", "");
      final String hash = checksum(file);

      resourcepacks.put(name, Pair.of(file, hash));
      Bukkit.getConsoleSender().sendMessage("§2Resourcepack §f" + name + "§2  SHA1 hash: §f" + hash);
    }

    port = cfg.getInt("port", 9555);
    ip = cfg.getString("ip", "127.0.0.1");
    defaultPack = cfg.getString("defaultpack", "resources");

    startHttpd(cfg.getInt("port", 9555));
  }

  public void shutdown() {
    server.terminate();
  }

  private void startHttpd(final int port) {
    try {
      server = new HttpServer(port) {
        @Override
        public File requestFileCallback(final PhoenixConnection connection, final String request) {
          final Player player = getAddress(connection);
          if (player == null) {
            plugin.getLogger().info("Unknown connection from '" + connection.getClient().getInetAddress() + "'. Aborting...");
            return null;
          }

          if (!resourcepacks.containsKey(request)) {
            plugin.getLogger().info("Unknown resource pack request from '" + connection.getClient().getInetAddress() + "'. Aborting...");
            return null;
          }
          plugin.getLogger().info("Connection " + connection.getClient().getInetAddress() + " is requesting " + request);
          return resourcepacks.get(request).getKey();
        }

        @Override
        public void onSuccessfulRequest(final PhoenixConnection connection, final String request) {
          plugin.getLogger().info("Successfully served '" + request + "' to " + connection.getClient().getInetAddress().getHostAddress());
        }

        @Override
        public void onClientRequest(final PhoenixConnection connection, final String request) {
          plugin.getLogger().info("Request '" + request + "' recieved from " + connection.getClient().getInetAddress().getHostAddress());
        }

        @Override
        public void onRequestError(final PhoenixConnection connection, final int code) {
          plugin.getLogger().info("Error " + code + " when attempting to serve " + connection.getClient().getInetAddress().getHostAddress());
        }
      };
      // Start the web server
      server.start();
      plugin.getLogger().info(ChatColor.GREEN + "Successfully started the mini http daemon!");
    } catch (final IOException e1) {
      plugin.getLogger().severe(ChatColor.RED + "Unable to start the mini http daemon! Disabling...");
    }
  }


  private String checksum(final File input) {
    try (final InputStream in = new FileInputStream(input)) {
      final MessageDigest digest = MessageDigest.getInstance("SHA1");
      final byte[] block = new byte[2048];
      int length;

      while ((length = in.read(block)) > 0) {
        digest.update(block, 0, length);
      }

      final byte[] bytes = digest.digest();
      final String hash = String.format("%040x", new BigInteger(1, bytes));
      return hash;
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void sendOnJoin(final PlayerJoinEvent event) {

    event.getPlayer().setInvulnerable(true);

    Bukkit.getScheduler().runTaskLater(AvarionCore.getPlugin(), () -> {
      event.getPlayer().sendMessage("§7Loading Resourcepack...");
      sendResourcePack(event.getPlayer(), defaultPack);
    }, 60L);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void sendOnJoin(final PlayerQuitEvent event) {
    servedPack.remove(event.getPlayer().getUniqueId());
    attempts.remove(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void resourceEvent(final PlayerResourcePackStatusEvent e) {

    final UUID id = e.getPlayer().getUniqueId();
    final Player player = e.getPlayer();

    if (e.getStatus() == Status.SUCCESSFULLY_LOADED) {
      player.setInvulnerable(false);
    } else if (e.getStatus() == Status.FAILED_DOWNLOAD) {

      if (attempts.contains(id)) {
        attempts.remove(id);
        kickPlayer(player);
        return;
      } else {
        Bukkit.getConsoleSender().sendMessage("§4" + e.getPlayer().getName() + " failed to download the resource pack - Trying again in 3 seconds before kicking..");
        Bukkit.getScheduler().runTaskLater(AvarionCore.getPlugin(), () -> {
          attempts.add(id);
          sendResourcePack(player, servedPack.get(id));
        }, 60L);
      }

    } else if (e.getStatus() == Status.DECLINED) {
      kickPlayer(player);
    }
  }

  private void kickPlayer(final Player player) {
    player.sendMessage("§4Um auf diesem Server zu spielen musst du das Resourcepack akzeptieren!");
    player.sendMessage("");
    player.sendMessage("§7Bitte aktiviere dazu Server-Resourcenpakete.");
    player.sendMessage("§6*  §7Gehe dazu in deine Serverliste und klicke auf '§2Bearbeiten'");
    player.sendMessage("§6*  §7Stelle nun Server-Resourcenpakete auf '§2Aktiviert'");

    player.kickPlayer("§4Resourcepack wurde nicht akzeptiert!");
  }

  private void sendResourcePack(final Player player, final String request) {
    final StringBuilder builder = new StringBuilder("http://").append(ip).append(":").append(port).append("/").append(request);
    player.setResourcePack(builder.toString(), resourcepacks.get(request).getValue());
    servedPack.put(player.getUniqueId(), request);
  }


  private Player getAddress(final PhoenixConnection connection) {
    final byte[] mac = connection.getClient().getInetAddress().getAddress();
    for (final Player player : Bukkit.getOnlinePlayers()) {
      if (Arrays.equals(player.getAddress().getAddress().getAddress(), mac)) {
        return player;
      }
    }
    return null;
  }


  @CommandAlias("reloadresource")
  @CommandPermission("ct.admin")
  public class ReloadResource extends BaseCommand {

    @Default
    public void reloadResourcepacks(final CommandIssuer issuer) {
      plugin.reloadConfig();
      issuer.sendMessage(F.main("Admin", "Lade Resourcepacks neu..."));
      initialize();
    }

    @Subcommand("request")
    @CommandCompletion("@resourcepacks")
    public void request(final Player sender, final String pack) {
      sendResourcePack(sender, defaultPack);
    }
  }
}