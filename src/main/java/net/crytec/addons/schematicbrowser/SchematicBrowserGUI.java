/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.schematicbrowser.SchematicBrowserGUI can not be copied and/or distributed without the express
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

package net.crytec.addons.schematicbrowser;

import java.io.File;
import java.util.ArrayList;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.Pagination;
import net.crytec.inventoryapi.api.SlotIterator;
import net.crytec.inventoryapi.api.SlotIterator.Type;
import net.crytec.inventoryapi.api.SlotPos;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

public class SchematicBrowserGUI implements InventoryProvider {

	private final SchematicBrowser plugin;
	private final File currentFolder;
	
	public SchematicBrowserGUI(final SchematicBrowser plugin, final File folder) {
		this.plugin = plugin;
		currentFolder = folder;
	}
	

	@Override
	public void init(final Player player, final InventoryContent contents) {

		final Pagination pagination = contents.pagination();
		final ArrayList<ClickableItem> items = new ArrayList<>();
		
		if (!currentFolder.exists()) player.closeInventory();

		final File[] subfolders = currentFolder.listFiles(File::isDirectory);
		if (subfolders == null) return;

		for (final File sf : subfolders) {
			items.add(ClickableItem.of(new ItemBuilder(Material.CHEST).name("§fOrdner: " + sf.getName()).build(), e -> SmartInventory.builder().title("Schematic Browser").size(5, 9).provider(new SchematicBrowserGUI(plugin, sf)).build().open(player)));
		}

		final File[] files = currentFolder.listFiles(f -> !f.isDirectory());
		if (files == null) return;

		for (final File file : files) {
			final long size = FileUtils.sizeOf(file);
			String defaulttext = "§aOk";

			if (size > 30000) {
				defaulttext = "§c§lServer könnte crashen";
			} else if (size > 10000) {
				defaulttext = "§6Server könnte laggen.";
			}

			items.add(ClickableItem.of(
					new ItemBuilder(Material.PAPER)
					.name("§6" + file.getName())
					.lore("§7Size: " + FileUtils.byteCountToDisplaySize(size))
					.lore("§7Info: " + defaulttext).build(),
					e -> {
						UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
						final String debug = plugin.getSchematicFolder().toURI().relativize(file.toURI()).getPath();
						Bukkit.dispatchCommand(player, "/schem load " + debug);
						player.closeInventory();
					}));

		}

		ClickableItem[] c = new ClickableItem[items.size()];
		c = items.toArray(c);
		pagination.setItems(c);
		pagination.setItemsPerPage(27);

		if (items.size() > 0 && !pagination.isLast()) {
			contents.set(4, 7, ClickableItem.of(new ItemBuilder(Material.MAP).name("§f§lSeite vor").build(), e -> {
				contents.getHost().open(player, pagination.next().getPage());
				UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
			}));
		}

		if (!pagination.isFirst()) {
			contents.set(4, 1, ClickableItem.of(new ItemBuilder(Material.MAP).name("§f§lSeite zurück").build(), e -> {
				contents.getHost().open(player, pagination.previous().getPage());
				UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
			}));
		}
		
		if (contents.properties().containsKey("file")) {
			contents.set(4, 4, ClickableItem.of(new ItemBuilder(Material.MAP).name("§f§lZurück zum Hauptmenü").build(), e -> {
				SmartInventory.builder().title("Schematic Browser").size(5, 9).provider(new SchematicBrowserGUI(plugin, plugin.getSchematicFolder())).build().open(player);
				UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
			}));
		}

		SlotIterator slotIterator = contents.newIterator(Type.HORIZONTAL, SlotPos.of(0));
		slotIterator = slotIterator.allowOverride(false);
		pagination.addToIterator(slotIterator);

	}

}
