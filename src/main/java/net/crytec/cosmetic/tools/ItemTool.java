/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.tools.ItemTool can not be copied and/or distributed without the express
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

import lombok.Getter;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public abstract class ItemTool {


  public ItemTool(final String name, final int cooldown, final Material icon) {
    this.cooldown = cooldown;
		displayname = name;

    this.icon = new ItemBuilder(icon).name(displayname).build();
  }

  private final String displayname;
  private final ItemStack icon;
  @Getter
  private final int cooldown;

  public void handleInteract(final PlayerInteractEvent event) {
    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }
    if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (event.getItem() == null || !event.getItem().isSimilar(icon)) {
      return;
    }
    if (event.getPlayer().hasCooldown(event.getMaterial())) {
      return;
    }

		onActivate(event);
    event.getPlayer().setCooldown(icon.getType(), cooldown);
  }

  protected abstract void onActivate(PlayerInteractEvent event);

  public abstract boolean canEquip(Player player);

  public final ClickableItem getGUIRepresenter() {

    return new ClickableItem(icon.clone(), e -> {

      final Player player = (Player) e.getWhoClicked();
      if (player.getInventory().getItem(8).getType() == Material.FIREWORK_STAR) {
				unequip(player);
      } else {
				applyTool(player);
      }
    });
  }

  private void applyTool(final Player player) {
    if (!canEquip(player)) {
      return;
    }
    player.getInventory().setItem(8, icon.clone());
    player.sendMessage(F.main("§aTool", "Du hast " + F.name(displayname) + " ausgerüstet."));
    UtilPlayer.playSound(player, Sound.ENTITY_ITEM_PICKUP);
  }

  private void unequip(final Player player) {
    player.getInventory().setItem(8, new ItemBuilder(Material.FIREWORK_STAR).name("§7Kein Tool ausgewählt").build());
    player.sendMessage(F.main("§aTool", "Du hast " + F.name(displayname) + " abgelegt."));
    UtilPlayer.playSound(player, Sound.ENTITY_HORSE_ARMOR, 1, 0.45F);
  }
}
