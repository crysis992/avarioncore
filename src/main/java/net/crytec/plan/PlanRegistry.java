/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.plan.PlanRegistry can not be copied and/or distributed without the express
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

import com.djrapitops.plan.extension.ExtensionService;
import net.crytec.AvarionCore;
import org.bukkit.Bukkit;

public class PlanRegistry {

  private final AvarionCore plugin;

  public PlanRegistry(final AvarionCore core) {
    plugin = core;
  }

  public void init() {
    //TODO Implement isEnabled Method to AddonManager
//    if (AvarionCore.getPlugin().getAddonManager().isEnabled("PlotShop")) {
//      ExtensionService.getInstance().register(new PlotExtension());
//			plugin.getLogger().info("Enabed PlotShop statistics");
//
//    }

    if (Bukkit.getPluginManager().getPlugin("CTCconomy") != null) {
      ExtensionService.getInstance().register(new CorePlayerExtension());
      plugin.getLogger().info("Enabed Vault & Coreplayer statistics");
    }

    if (Bukkit.getPluginManager().getPlugin("Jobs") != null) {
      ExtensionService.getInstance().register(new JobsData());
      plugin.getLogger().info("Enabed Jobs statistics");
    }

    // Optional<Caller> caller =
    // ExtensionService.getInstance().register(acore);

  }

}
