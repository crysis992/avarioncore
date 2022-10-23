/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.pets.PetGUI can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.pets;

import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import net.crytec.util.PlayerChatInput;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class PetGUI implements InventoryProvider {

  private final PetManager manager;

  public PetGUI(final PetManager manager) {
    this.manager = manager;
  }

  @Override
  public void init(final Player player, final InventoryContent contents) {

    for (final PetType type : PetType.values()) {

      final PetConfig pcfg = manager.getPetConfig(type);

      final ItemBuilder icon = new ItemBuilder(pcfg.getIcon().clone());

      if (player.hasPermission(pcfg.getPermission()) || player.hasPermission("pet.all")) {

        icon.lore("");
        icon.lore("§eLinksklick §fum das Pet zu rufen.");
        icon.lore("§eRechtsklick §fum den Standardnamen");
        icon.lore("§fzu ändern.");
      } else {
        icon.lore("§7Du hast dieses Pet");
        icon.lore("§7noch §cnicht§7 freigeschaltet.");
      }

      contents.add(ClickableItem.of(icon.build(), event -> {

        if (!player.hasPermission(pcfg.getPermission())) {
          return;
        }

        if (event.getClick() == ClickType.LEFT) {
          if (manager.hasPet(player)) {
            manager.deletePet(player);
          }
          manager.createNewPet(player, type);
        }

      }));
    }

    if (manager.hasPet(player)) {
      contents.set(SlotPos.of(5, 1), ClickableItem.of(new ItemBuilder(Material.BARRIER).name("§cPet löschen").build(), e -> manager.deletePet(player)));

      contents.set(SlotPos.of(5, 3), ClickableItem.of(new ItemBuilder(Material.GLOWSTONE_DUST).name("§ePet Leuchten").build(), e -> {
        final Entity ent = manager.getPet(player).getEntity();
        if (ent.isGlowing()) {
          ent.setGlowing(false);
          player.sendMessage(F.main("Pet", "Dein Pet leuchtet nun nicht mehr."));
        } else {
          ent.setGlowing(true);
          player.sendMessage(F.main("Pet", "Dein Pet leuchtet nun"));
        }

      }));
      contents.set(SlotPos.of(5, 5), ClickableItem.of(new ItemBuilder(Material.NAME_TAG).name("§aPet umbenennen").build(), e -> {
        player.closeInventory();
        player.sendMessage(F.main("Info", "Bitte gebe den neuen Namen in den Chat ein:"));
        PlayerChatInput.get(player, name -> {
          manager.getPet(player).setName(name);
        });
      }));

      if (manager.getPet(player).isHidden()) {
        contents.set(SlotPos.of(5, 7), ClickableItem.of(new ItemBuilder(Material.LIME_DYE).name("§ePet anzeigen").lore("§7Zeige dein Pet wieder an").build(), e -> {
          manager.showPet(player);
          reopen(player, contents);
        }));
      } else {
        contents.set(SlotPos.of(5, 7), ClickableItem.of(new ItemBuilder(Material.GRAY_DYE).name("§ePet verbergen").lore("§7Verberge dein Pet").build(), e -> {
          manager.hidePet(player);
          reopen(player, contents);
        }));
      }
    }
  }
}