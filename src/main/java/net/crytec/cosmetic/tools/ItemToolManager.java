/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.tools.ItemToolManager can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.tools;

import com.google.common.collect.Sets;
import java.util.HashSet;
import lombok.Getter;
import net.crytec.AvarionCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemToolManager implements Listener {

  public ItemToolManager() {
    Bukkit.getPluginManager().registerEvents(this, AvarionCore.getPlugin());

		cosmeticTools = Sets.newHashSet();
		tickableTools = Sets.newHashSet();

    Bukkit.getScheduler().runTaskTimer(AvarionCore.getPlugin(), () -> tickableTools.forEach(TickableItemTool::onTick), 40, 3L);
  }

  @Getter
  private final HashSet<ItemTool> cosmeticTools;
  private final HashSet<TickableItemTool> tickableTools;

  public void registerTool(final ItemTool tool) {
    if (tool instanceof TickableItemTool) {
      final TickableItemTool tickable = (TickableItemTool) tool;
			tickableTools.add(tickable);
    }
		cosmeticTools.add(tool);
  }

  @EventHandler
  public void handleInteract(final PlayerInteractEvent event) {
		cosmeticTools.forEach(tool -> tool.handleInteract(event));
  }

}
