/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.TrailManager can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.particle;

import com.google.common.collect.Maps;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.crytec.AvarionCore;
import net.crytec.cosmetic.CosmeticManager;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.libs.commons.utils.UtilMath;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.libs.commons.utils.lang.EnumUtils;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TrailManager implements Listener, Runnable {

  public TrailManager(final CosmeticManager manager) {

    this.manager = manager;

    lastMove = Maps.newHashMap();
    activeEffects = Maps.newHashMap();
    particleConfigs = new EnumMap<>(ParticlePack.class);

    initialize();

    Bukkit.getPluginManager().registerEvents(this, AvarionCore.getPlugin());
    Bukkit.getScheduler().runTaskTimerAsynchronously(AvarionCore.getPlugin(), this, 20, 1L);
  }

  private final CosmeticManager manager;

  private final HashMap<UUID, Long> lastMove;
  private final HashMap<UUID, IParticleEffect> activeEffects;
  private final EnumMap<ParticlePack, TrailConfig> particleConfigs;

  private void initialize() {

    boolean shouldSave = false;

    for (final ParticlePack particle : ParticlePack.values()) {

      if (!manager.getConfig().isSet("particles." + particle.toString())) {
        manager.getConfig().set(particle.toString() + ".name", particle.toString().toLowerCase());
        manager.getConfig().set(particle.toString() + ".description", Arrays.asList("&7Coming soon", "&cComing soon"));
        manager.getConfig().set(particle.toString() + ".permission", "particle." + particle.toString().toLowerCase());
        manager.getConfig().set(particle.toString() + ".icon", Material.COOKIE.toString());
        shouldSave = true;
      }
    }
    if (shouldSave) {
      manager.saveConfig();
    }

    for (final ParticlePack particle : ParticlePack.values()) {

      final TrailConfig pconfig = new TrailConfig(particle);

      final String displayname = ChatColor.translateAlternateColorCodes('&', manager.getConfig().getString(particle.toString() + ".name"));
      final List<String> description = manager.getConfig().getStringList(particle.toString() + ".description")
          .stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());

      pconfig.setDisplayname(displayname);
      pconfig.setPermission(manager.getConfig().getString(particle.toString() + ".permission"));

      Material material = Material.COOKIE;
      if (EnumUtils.isValidEnum(Material.class, manager.getConfig().getString(particle.toString() + ".name"))) {
        material = Material.valueOf(manager.getConfig().getString(particle.toString() + ".name"));
      }

      pconfig.setIcon(new ItemBuilder(material).name(displayname).lore(description).build());

      particleConfigs.put(particle, pconfig);
    }
  }

  @EventHandler
  public void setMoving(final PlayerMoveEvent event) {
    if (!activeEffects.containsKey(event.getPlayer().getUniqueId())) {
      return;
    }
    if (UtilMath.offset(event.getFrom(), event.getTo()) <= 0.0D) {
      return;
    }
    lastMove.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
  }

  @EventHandler
  public void removeData(final PlayerQuitEvent e) {
    lastMove.remove(e.getPlayer().getUniqueId());
    if (activeEffects.containsKey(e.getPlayer().getUniqueId())) {
      activeEffects.remove(e.getPlayer().getUniqueId());
    }
  }

  public boolean hasActiveTrail(final Player player) {
    return activeEffects.containsKey(player.getUniqueId());
  }

  public void activate(final Player player, final ParticlePack particle) {
    try {
      final Constructor<? extends IParticleEffect> cons = particle.getParticleEffectClass().getConstructor(Player.class);
      final Object[] para = new Object[]{player};

      final IParticleEffect effect = cons.newInstance(para);

      activeEffects.put(player.getUniqueId(), effect);
      player.sendMessage(F.main("Partikel", "Du hast " + F.name(getParticleConfig(particle).getDisplayname()) + "§2 aktiviert."));
    } catch (final Exception ex) {
      ex.printStackTrace();
    }
  }

  public void deactivate(final Player player) {
    player.sendMessage(F.main("Partikel", "Du hast deine Leuchtspur §4deaktiviert."));
    activeEffects.remove(player.getUniqueId());

  }

  public TrailConfig getParticleConfig(final ParticlePack particle) {
    return particleConfigs.get(particle);
  }

  public boolean isMoving(final Player player) {
    if (!lastMove.containsKey(player.getUniqueId())) {
      return false;
    }
    return !UtilTime.isElapsed(lastMove.get(player.getUniqueId()) + 500L);
  }

  public void openTrailMenu(final Player player) {
    SmartInventory.builder().provider(new TrailGUI(this)).title("Leuchtspuren").size(6, 9).build().open(player);
  }

  @Override
  public void run() {
    for (final IParticleEffect effect : activeEffects.values()) {
      effect.update(isMoving(effect.getPlayer()));
    }
  }
}