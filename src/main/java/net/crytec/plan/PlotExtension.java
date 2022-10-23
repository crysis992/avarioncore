/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.plan.PlotExtension can not be copied and/or distributed without the express
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

package net.crytec.plan;

import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.ElementOrder;
import com.djrapitops.plan.extension.FormatType;
import com.djrapitops.plan.extension.annotation.NumberProvider;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.annotation.TabInfo;
import com.djrapitops.plan.extension.annotation.TableProvider;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import com.djrapitops.plan.extension.icon.Icon;
import com.djrapitops.plan.extension.table.Table;
import net.crytec.AvarionCore;
import net.crytec.addons.plotshop.PlotShopAddon;
import net.crytec.addons.plotshop.manager.PlotManager;

@TabInfo(
    tab = "Grundstücke",
    iconName = "tag",
    iconFamily = Family.SOLID,
    elementOrder = {ElementOrder.VALUES, ElementOrder.TABLE, ElementOrder.GRAPH}

)
@PluginInfo(color = Color.GREEN, name = "Grundstücke", iconName = "archway", iconFamily = Family.SOLID)
public class PlotExtension implements DataExtension {


  private final PlotManager manager;

  public PlotExtension() {
    manager = ((PlotShopAddon) AvarionCore.getPlugin().getAddonManager().getAddon(PlotShopAddon.class)).getManager();
  }

  @Override
  public CallEvents[] callExtensionMethodsOn() {
    return new CallEvents[]{
        CallEvents.SERVER_EXTENSION_REGISTER,
        CallEvents.SERVER_PERIODICAL,
    };
  }


  @NumberProvider(
      text = "Grundstücke",
      description = "Anzal aller Grundstücke.",
      format = FormatType.NONE,
      iconFamily = Family.SOLID,
      iconName = "home"
  )
  public long getTotalPlots() {
    return manager.getPlotMap().size();
  }

  @NumberProvider(
      text = "Gruppen",
      description = "Anzal aller Gruppen.",
      format = FormatType.NONE,
      iconFamily = Family.SOLID,
      iconName = "home"
  )

  @TableProvider(tableColor = Color.ORANGE)
  public Table freePlotOverview() {
    final Table.Factory table = Table.builder()
        .columnOne("Gruppe", Icon.called("list").of(Family.SOLID).build())
        .columnTwo("Gesamt", Icon.called("lock").of(Family.SOLID).build())
        .columnThree("Frei", Icon.called("lock-open").of(Family.SOLID).build());

    return table.build();
  }


}
