/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.TrailGUI can not be copied and/or distributed without the express
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

import lombok.AllArgsConstructor;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class TrailGUI implements InventoryProvider {

  private final TrailManager addon;

  private final ItemStack disable = new ItemBuilder(Material.BARRIER).name("§cDeaktivieren").build();

  @Override
  public void init(final Player player, final InventoryContent contents) {

//		contents.fillRow(4, ClickableItem.empty(border));

    contents.set(SlotPos.of(5, 4), ClickableItem.of(disable, e -> {
      if (!addon.hasActiveTrail(player)) {
        player.sendMessage(F.main("Partikel", "Du hast aktuell keine Leuchtspur aktiviert."));
        return;
      }
			addon.deactivate(player);
    }));

    for (final ParticlePack particle : ParticlePack.values()) {

      final TrailConfig pconfig = addon.getParticleConfig(particle);

      contents.add(ClickableItem.of(pconfig.getIcon(), e -> {

        if (!player.hasPermission(pconfig.getPermission())) {
          player.sendMessage(F.main("Partikel", "Du musst dir diesen Effekt erst kaufen."));
          return;
        }
				addon.activate(player, particle);
      }));
    }
  }
}