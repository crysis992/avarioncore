/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.healthbar.HealthIndicatorUser can not be copied and/or distributed without the express
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

package net.crytec.addons.healthbar;

import net.crytec.AvarionCore;
import net.crytec.libs.commons.utils.UtilMath;
import net.crytec.libs.commons.utils.language.LanguageAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class HealthIndicatorUser {

  private static final NamespacedKey bossBarKey = new NamespacedKey(AvarionCore.getPlugin(), "healthIndicator");

  private final Player player;
  private long lastUpdate;
  private final BossBar bar;

  public HealthIndicatorUser(final Player player) {
    lastUpdate = System.currentTimeMillis();
    this.player = player;
    bar = Bukkit.createBossBar(bossBarKey, "Health", BarColor.WHITE, BarStyle.SOLID);
  }

  public void sendEntityInfo(final LivingEntity entity, final double damage) {
    bar.setProgress(getBarProgress(entity, damage));
    bar.setTitle(getStringValue(entity, damage));
    bar.addPlayer(player);

    lastUpdate = System.currentTimeMillis() + 3000L;
  }

  public void tick() {
    if (isElapsed()) {
      getBar().removePlayer(getPlayer());
    }
  }

  public Player getPlayer() {
    return player;
  }

  public BossBar getBar() {
    return bar;
  }

  private boolean isElapsed() {
    return System.currentTimeMillis() >= lastUpdate;
  }

  private String getStringValue(final LivingEntity entity, final double damage) {
    final double[] values = {entity.getHealth() - damage, entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()};
    if (values[0] <= 0) {
      return LanguageAPI.getEntityName(entity) + ChatColor.DARK_RED + " tot.";
    }

    return LanguageAPI.getEntityName(entity) + ": " + getHealthColor(bar.getProgress())
        + UtilMath.trim(2, values[0]) + "    §4§l- " + UtilMath.trim(1, damage);
  }

  private ChatColor getHealthColor(final double percentage) {
    if (percentage > 0.5) {
      return ChatColor.DARK_GREEN;
    } else if (percentage > 0.25) {
      return ChatColor.GOLD;
    }
    return ChatColor.DARK_RED;
  }

  private double getBarProgress(final LivingEntity entity, final double damage) {
    final double[] values = {entity.getHealth() - damage, entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()};
    final double procent = values[0] * 100.0 / values[1] / 100.0;
    return procent < 0 ? 0.0D : procent;
  }
}