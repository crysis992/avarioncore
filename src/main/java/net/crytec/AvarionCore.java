/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.AvarionCore can not be copied and/or distributed without the express
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

package net.crytec;

import co.aikar.commands.PaperCommandManager;
import de.schlichtherle.io.File;
import lombok.Getter;
import net.crytec.addons.AddonManager;
import net.crytec.addons.ResourcePack;
import net.crytec.addons.Vanish;
import net.crytec.addons.WorldLicences;
import net.crytec.addons.afk.AFK;
import net.crytec.addons.armorstandeditor.ArmorStandEditor;
import net.crytec.addons.chairs.Chairs;
import net.crytec.addons.chestlock.ChestLockAddon;
import net.crytec.addons.healthbar.HealthIndicator;
import net.crytec.addons.loreDisplay.LoreDisplayAddon;
import net.crytec.addons.permissionShop.LuckPermRankShop;
import net.crytec.addons.plotshop.PlotShopAddon;
import net.crytec.addons.portal.PortalAddon;
import net.crytec.addons.schematicbrowser.SchematicBrowser;
import net.crytec.addons.spawnsystem.SpawnManager;
import net.crytec.addons.timber.UltimateTimber;
import net.crytec.addons.titles.TitleAddon;
import net.crytec.addons.waypoints.WaypointAddon;
import net.crytec.commands.*;
import net.crytec.commands.Teleport.*;
import net.crytec.commands.Teleport.home.DelHome;
import net.crytec.commands.Teleport.home.Home;
import net.crytec.commands.Teleport.home.SetHome;
import net.crytec.commands.administration.*;
import net.crytec.internal.CorePlayer;
import net.crytec.internal.PlayerDataManager;
import net.crytec.internal.settings.HomeSetting;
import net.crytec.internal.settings.WaypointSettings;
import net.crytec.libs.protocol.scoreboard.api.PlayerBoardManager;
import net.crytec.listener.GeneralListener;
import net.crytec.listener.JoinListener;
import net.crytec.manager.PermissionManager;
import net.crytec.plan.PlanRegistry;
import net.crytec.util.CommandSetup;
import net.crytec.util.UtilManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public class AvarionCore extends JavaPlugin {

  @Getter
  private static AvarionCore plugin;
  @Getter
  private PermissionManager permissionManager;
  @Getter
  private PaperCommandManager commandManager;
  @Getter
  private AddonManager addonManager;

  private PlayerDataManager userManager;

  private ResourcePack resourcepackHandler;

  @Getter
  private UtilManager utilManager;
  @Getter
  private Economy economy;

  @Override
  public void onLoad() {
    AvarionCore.plugin = this;
  }

  @Override
  public void onEnable() {
    final File dir = new File(getDataFolder(), "addons");
    if (!dir.exists() && !dir.mkdir()) {
      getLogger().severe("Failed to create plugin directory.");
    }

    userManager = new PlayerDataManager(this);
    userManager.registerStorageAdapter(this, HomeSetting.class);
    userManager.registerStorageAdapter(this, WaypointSettings.class);

    utilManager = new UtilManager(this);

    commandManager = new PaperCommandManager(this);
    commandManager.getLocales().setDefaultLocale(Locale.GERMAN);
    commandManager.enableUnstableAPI("help");

    CommandSetup.registerCommandCompletion(this, commandManager);
    CommandSetup.registerContextResolver(commandManager);
    CommandSetup.registerCommandConditions(commandManager);

    permissionManager = new PermissionManager();

    // Register listener
    new GeneralListener(this);
    new JoinListener(this);

    if (!setupEconomy()) {
      getLogger().warning("Unable to locate an Economy Plugin. Some parts of this plugin might not behave correctly!");
    }

    addonManager = new AddonManager(this);

    // Register Addons
    addonManager.registerAddon(ArmorStandEditor.class);
    addonManager.registerAddon(AFK.class);
    addonManager.registerAddon(Vanish.class);
    addonManager.registerAddon(TitleAddon.class);
    addonManager.registerAddon(UltimateTimber.class);
    addonManager.registerAddon(SpawnManager.class);
    addonManager.registerAddon(SchematicBrowser.class);
    addonManager.registerAddon(PlotShopAddon.class);
    addonManager.registerAddon(PortalAddon.class);
    addonManager.registerAddon(LuckPermRankShop.class);
//    addonManager.registerAddon(PaintingSwitch.class);
    addonManager.registerAddon(LoreDisplayAddon.class);
    addonManager.registerAddon(ChestLockAddon.class);
    addonManager.registerAddon(HealthIndicator.class);
    addonManager.registerAddon(WaypointAddon.class);
    addonManager.registerAddon(WorldLicences.class);
    addonManager.registerAddon(Chairs.class);

    Bukkit.getCommandMap().getKnownCommands().remove("reload");
    Bukkit.getCommandMap().getKnownCommands().remove("bukkit:reload");
    Bukkit.getCommandMap().getKnownCommands().remove("rl");
    Bukkit.getCommandMap().getKnownCommands().remove("bukkit:rl");
    Bukkit.getCommandMap().getKnownCommands().remove("spigot:restart");

    registerCommands();

    if (getConfig().getBoolean("resourcepack.enabled")) {
      resourcepackHandler = new ResourcePack(this);
      getLogger().info("Enabled Resourcepack Handler");
    }

    addonManager.enable();

    if (Bukkit.getPluginManager().getPlugin("Plan") != null) {
      try {
        final PlanRegistry reg = new PlanRegistry(this);
        reg.init();

      } catch (final Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void onDisable() {
    if (resourcepackHandler != null) {
      getLogger().info("Shutting down resourcepack server...");
      resourcepackHandler.shutdown();
    }

    for (final Player player : Bukkit.getOnlinePlayers()) {
      final CorePlayer cp = CorePlayer.get(player.getUniqueId());
      if (cp == null) {
        continue;
      }
      cp.unload();
    }

    addonManager.shutdown();
  }

  private void registerCommands() {
    commandManager.registerCommand(new Warp());
    commandManager.registerCommand(new Setwarp());
    commandManager.registerCommand(new DeleteWarp());
    commandManager.registerCommand(new Home());
    commandManager.registerCommand(new SetHome());
    commandManager.registerCommand(new DelHome());
    commandManager.registerCommand(new Spawner());
    commandManager.registerCommand(new Trashbin());
    commandManager.registerCommand(new Workbench());
    commandManager.registerCommand(new Nightvision());
    commandManager.registerCommand(new Heal());
    commandManager.registerCommand(new Whois());
    commandManager.registerCommand(new SpawnMob());
    commandManager.registerCommand(new Hat());
    commandManager.registerCommand(new Fly());
    commandManager.registerCommand(new TimeCommand(), true);
    commandManager.registerCommand(new Memory());
    commandManager.registerCommand(new Gamemode());
    commandManager.registerCommand(new Stop(), true);
    commandManager.registerCommand(new Sudo());
    commandManager.registerCommand(new OpenInv());
    commandManager.registerCommand(new TopCommand());
    commandManager.registerCommand(new Speed());
    commandManager.registerCommand(new Repair());
    commandManager.registerCommand(new ClearInventory());
    commandManager.registerCommand(new Bonemeal());
    commandManager.registerCommand(new Weather());
    commandManager.registerCommand(new Back());
    commandManager.registerCommand(new Teleport());
    commandManager.registerCommand(new TeleportHere());
    commandManager.registerCommand(new TeleportAll());
    commandManager.registerCommand(new TeleportPosition());
    commandManager.registerCommand(new NoPickup());
    commandManager.registerCommand(new ChunkTeleport());
    commandManager.registerCommand(new Seen());
    commandManager.registerCommand(new Kill());
    commandManager.registerCommand(new Sign(this));
    commandManager.registerCommand(new PrintUnicode());
    commandManager.registerCommand(new Playtime());
    commandManager.registerCommand(new CPULoad());
    commandManager.registerCommand(new Admin());
  }

  private boolean setupEconomy() {
    if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    final RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      return false;
    }
    economy = rsp.getProvider();
    return true;
  }

  public PlayerDataManager getUserManager() {
    return userManager;
  }

  public PlayerBoardManager getScoreBoardManager() {
    return utilManager.getScoreboardAPI().getBoardManager();
  }

}