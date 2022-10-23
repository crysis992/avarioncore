/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.armorstandeditor.ArmorStandGUI can not be copied and/or distributed without the express
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

package net.crytec.addons.armorstandeditor;

import net.crytec.addons.armorstandeditor.data.ArmorStandAxis;
import net.crytec.addons.armorstandeditor.data.ArmorStandEditMode;
import net.crytec.addons.armorstandeditor.data.ArmorStandOptions;
import net.crytec.inventoryapi.anvil.AnvilGUI;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ArmorStandGUI implements InventoryProvider {
	
	public ArmorStandGUI(final ArmorStandData data) {
		this.data = data;
	}
	
	private final ArmorStandData data;

	@Override
	public void init(final Player player, final InventoryContent contents) {
		
		// Axis
		contents.set(SlotPos.of(0, 0), ClickableItem.of(new ItemBuilder(Material.BLUE_WOOL).name(ArmorStandAxis.X.getDisplayname()).build(),
				e -> data.setAxis(ArmorStandAxis.X)));
		contents.set(SlotPos.of(0, 1), ClickableItem.of(new ItemBuilder(Material.RED_WOOL).name(ArmorStandAxis.Y.getDisplayname()).build(),
				e -> data.setAxis(ArmorStandAxis.Y)));
		contents.set(SlotPos.of(0, 2), ClickableItem.of(new ItemBuilder(Material.GREEN_WOOL).name(ArmorStandAxis.Z.getDisplayname()).build(),
				e -> data.setAxis(ArmorStandAxis.Z)));
		
		
		// Body
		
		contents.set(SlotPos.of(1, 1), ClickableItem.of(new ItemBuilder(Material.LEATHER_HELMET).name(ArmorStandEditMode.HEAD.getDisplayname()).build(),
				e -> data.setMode(ArmorStandEditMode.HEAD)));
		
		contents.set(SlotPos.of(2, 1), ClickableItem.of(new ItemBuilder(Material.LEATHER_CHESTPLATE).name(ArmorStandEditMode.BODY.getDisplayname()).build(),
				e -> data.setMode(ArmorStandEditMode.BODY)));
		
		contents.set(SlotPos.of(2, 0), ClickableItem.of(new ItemBuilder(Material.STICK).name(ArmorStandEditMode.LEFT_ARM.getDisplayname()).build(),
				e -> data.setMode(ArmorStandEditMode.LEFT_ARM)));
		
		contents.set(SlotPos.of(2, 2), ClickableItem.of(new ItemBuilder(Material.STICK).name(ArmorStandEditMode.RIGHT_ARM.getDisplayname()).build(),
				e -> data.setMode(ArmorStandEditMode.RIGHT_ARM)));
		
		contents.set(SlotPos.of(3, 0), ClickableItem.of(new ItemBuilder(Material.LEATHER_LEGGINGS).name(ArmorStandEditMode.LEFT_LEG.getDisplayname()).build(),
				e -> data.setMode(ArmorStandEditMode.LEFT_LEG)));
		
		contents.set(SlotPos.of(3, 2), ClickableItem.of(new ItemBuilder(Material.LEATHER_LEGGINGS).name(ArmorStandEditMode.RIGHT_LEG.getDisplayname()).build(),
				e -> data.setMode(ArmorStandEditMode.RIGHT_LEG)));
		
		contents.set(SlotPos.of(0, 6), ClickableItem.of(new ItemBuilder(Material.COMPASS).name(ArmorStandEditMode.ROTATE.getDisplayname()).build(),
				e -> data.setMode(ArmorStandEditMode.ROTATE)));
		
		contents.set(SlotPos.of(0, 7), ClickableItem.of(new ItemBuilder(Material.MINECART).name(ArmorStandEditMode.ENTITY.getDisplayname()).build(),
				e -> data.setMode(ArmorStandEditMode.ENTITY)));
		
		
		// Options
		
		contents.set(SlotPos.of(3, 5), ClickableItem.of(new ItemBuilder(ArmorStandOptions.DISPLAYNAME.getIcon()).name(ArmorStandOptions.DISPLAYNAME.getDisplayname())
				.build(),
				e -> {
					player.closeInventory();
					if (e.getClick() == ClickType.RIGHT) {
						data.getEntity().setCustomName(null);
						data.getEntity().setCustomNameVisible(false);
						return;
					}
					new AnvilGUI(player, "Name.." , (p, name) -> {
						data.getEntity().setCustomName(name);
						data.getEntity().setCustomNameVisible(true);
						return null;
					});
				}));
		
		contents.set(SlotPos.of(3, 7), ClickableItem.of(new ItemBuilder(ArmorStandOptions.BASEPLATE.getIcon()).name(ArmorStandOptions.BASEPLATE.getDisplayname())
				.lore("§6Aktuell: " + F.tf(data.getEntity().hasBasePlate()))
				.build(),
				e -> {
					data.toggleOption(ArmorStandOptions.BASEPLATE);
					player.closeInventory();
				} ));
		
		contents.set(SlotPos.of(3, 8), ClickableItem.of(new ItemBuilder(ArmorStandOptions.ARMS.getIcon()).name(ArmorStandOptions.ARMS.getDisplayname())
				.lore("§6Aktuell: " + F.tf(data.getEntity().hasArms()))
				.build(),
				e -> {
					data.toggleOption(ArmorStandOptions.ARMS);
					player.closeInventory();
				} ));
		
		contents.set(SlotPos.of(4, 7), ClickableItem.of(new ItemBuilder(ArmorStandOptions.GRAVITY.getIcon()).name(ArmorStandOptions.GRAVITY.getDisplayname())
				.lore("§6Aktuell: " + F.tf(data.getEntity().hasGravity()))
				.build(),
				e -> {
					data.toggleOption(ArmorStandOptions.GRAVITY);
					player.closeInventory();
				}));
		
		contents.set(SlotPos.of(4, 8), ClickableItem.of(new ItemBuilder(ArmorStandOptions.SIZE.getIcon()).name(ArmorStandOptions.SIZE.getDisplayname())
				.lore("§6Aktuell: " + F.ctf(data.getEntity().isSmall(), "Klein", "Groß"))
				.build(),
				e -> {
					data.toggleOption(ArmorStandOptions.SIZE);
					player.closeInventory();
				} ));
		
		contents.set(SlotPos.of(5, 7), ClickableItem.of(new ItemBuilder(ArmorStandOptions.VISIBILLITY.getIcon()).name(ArmorStandOptions.VISIBILLITY.getDisplayname())
				.lore("§6Aktuell: " + F.tf(data.getEntity().isVisible()))
				.build(),
				e -> {
					data.toggleOption(ArmorStandOptions.VISIBILLITY);
					player.closeInventory();
				} ));
		
		
	}
}