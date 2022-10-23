/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.manager.PlotBoard can not be copied and/or distributed without the express
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

import java.util.List;
import net.crytec.addons.plotshop.data.Plot;
import net.crytec.libs.protocol.scoreboard.api.Entry;
import net.crytec.libs.protocol.scoreboard.api.EntryBuilder;
import net.crytec.libs.protocol.scoreboard.api.ScoreboardHandler;
import org.bukkit.entity.Player;

public class PlotBoard implements ScoreboardHandler {

  private final Plot plot;

  public PlotBoard(final Plot plot) {
    this.plot = plot;
    if (plot.getProtectedRegionID() == null) {
      throw new IllegalArgumentException("Plot has an invalid region identifier!");
    }
  }

  @Override
  public String getTitle(final Player player) {
    return plot.getProtectedRegionID();
  }

  @Override
  public List<Entry> getEntries(final Player player) {
    return new EntryBuilder().blank()
        .next("§fPreis: §e" + plot.getPrice())
        .next("§fGruppe: §e" + plot.getPlotGroup().getDisplayname())
        .blank()
        .next("§7Tippe §e/plot kaufen")
        .next("§7um dieses Grundstück zu")
        .next("§7kaufen.")
        .build();
  }
}
