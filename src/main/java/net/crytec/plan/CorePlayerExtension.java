/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.plan.CorePlayerExtension can not be copied and/or distributed without the express
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
import com.djrapitops.plan.extension.annotation.BooleanProvider;
import com.djrapitops.plan.extension.annotation.Conditional;
import com.djrapitops.plan.extension.annotation.NumberProvider;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.annotation.Tab;
import com.djrapitops.plan.extension.annotation.TabInfo;
import com.djrapitops.plan.extension.annotation.TableProvider;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import com.djrapitops.plan.extension.icon.Icon;
import com.djrapitops.plan.extension.table.Table;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import net.crytec.internal.CorePlayer;
import net.crytec.internal.settings.HomeSetting;
import net.crytec.internal.settings.WaypointSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@TabInfo(
    tab = "ServerManager",
    iconName = "tag",
    iconFamily = Family.SOLID,
    elementOrder = {ElementOrder.VALUES, ElementOrder.TABLE, ElementOrder.GRAPH}

)
@PluginInfo(color = Color.GREEN, name = "Spielerinfo", iconName = "flask", iconFamily = Family.SOLID)
public class CorePlayerExtension implements DataExtension {

  @Override
  public CallEvents[] callExtensionMethodsOn() {
    return new CallEvents[]{
        CallEvents.PLAYER_JOIN,
        CallEvents.PLAYER_LEAVE,
        CallEvents.SERVER_EXTENSION_REGISTER,
    };
  }


  @BooleanProvider(
      text = "",
      conditionName = "hasUser",
      hidden = true
  )
  public boolean hasUser(final UUID playerUUID) {
    return Bukkit.getOfflinePlayer(playerUUID).hasPlayedBefore();
  }

  private Optional<CorePlayer> getUser(final UUID playerUUID) {
    return Optional.ofNullable(CorePlayer.get(playerUUID));
  }


  @TableProvider(tableColor = Color.LIGHT_BLUE)
  @Tab(value = "Homepunkte")
  public Table getHomeTable(final UUID playerUUID) {
    final Table.Factory table = Table.builder()
        .columnOne("Homepunkt", Icon.called("align-justify").of(Family.SOLID).build())
        .columnTwo("Welt", Icon.called("globe-europe").build())
        .columnThree("Koordinaten", Icon.called("chevron-down").build());

    final HomeSetting data = getUser(playerUUID).get().getData(HomeSetting.class);
    final HashMap<String, Location> homes = data.getHomes();

    for (final Entry<String, Location> home : homes.entrySet()) {
      final StringBuilder location = new StringBuilder();
      location.append("X: ").append(home.getValue().getBlockX());
      location.append("Y: ").append(home.getValue().getBlockY());
      location.append("Z: ").append(home.getValue().getBlockZ());
      table.addRow(home.getKey(), home.getValue().getWorld().getName(), location.toString());
    }

    return table.build();

  }

  @Conditional("hasUser")
  @NumberProvider(text = "Wegpunkte",

      iconName = "street-view",
      iconFamily = Family.SOLID,
      priority = 4,
      description = "Anzahl der Wegpunkte",
      iconColor = Color.LIGHT_GREEN
  )
  public long getWaypointAmount(final UUID playerUUID) {
    final WaypointSettings data = getUser(playerUUID).get().getData(WaypointSettings.class);
    return data.getWaypoints().size();
  }
}
