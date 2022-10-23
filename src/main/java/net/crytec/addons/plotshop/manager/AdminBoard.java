/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.manager.AdminBoard can not be copied and/or distributed without the express
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

package net.crytec.addons.plotshop.manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import net.crytec.addons.plotshop.data.ExpireablePlot;
import net.crytec.addons.plotshop.data.Plot;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.libs.protocol.scoreboard.api.Entry;
import net.crytec.libs.protocol.scoreboard.api.EntryBuilder;
import net.crytec.libs.protocol.scoreboard.api.ScoreboardHandler;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class AdminBoard implements ScoreboardHandler {

  private final Plot plot;
  private final OfflinePlayer owner;

  public AdminBoard(final Plot plot) {
    this.plot = plot;
    owner = Bukkit.getOfflinePlayer(plot.getOwner());
  }


  @Override
  public String getTitle(final Player player) {
    return plot.getProtectedRegionID() + " §cAdmininfo";
  }

  @Override
  public List<Entry> getEntries(final Player player) {

    final long days = Math.abs(Duration.between(LocalDateTime.now(), UtilTime.millisToLocalDate(owner.getLastLogin())).toDays());
    final ChatColor color;

    if (days < 10) {
      color = ChatColor.GREEN;
    } else if (days < 20) {
      color = ChatColor.YELLOW;
    } else if (days < 30) {
      color = ChatColor.GOLD;
    } else {
      color = ChatColor.RED;
    }

    return new EntryBuilder().blank()
        .next("§fPreis: §e" + plot.getPrice())
        .next("§fGruppe: §e" + plot.getPlotGroup().getDisplayname())
        .next("§fMietbar: " + F.tf(plot instanceof ExpireablePlot))
        .blank()
        .next("§fBesitzer: " + owner.getName())
        .next("§fRegion: " + plot.getProtectedRegionID())
        .blank()
        .next("§fGekauft am: " + plot.getPurchaseDateFormatted())
        .next("§fZuletzt Online: " + (owner.isOnline() ? "§aOnline" : (color + UtilTime.when(owner.getLastLogin()))))
        .blank()
        .build();
  }
}