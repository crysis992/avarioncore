/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.schematicbrowser.SchematicBrowser can not be copied and/or distributed without the express
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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.sk89q.worldedit.WorldEdit;
import java.io.File;
import lombok.Getter;
import net.crytec.AvarionCore;
import net.crytec.addons.Addon;
import net.crytec.inventoryapi.SmartInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SchematicBrowser extends Addon {

	@Getter
	private File schematicFolder;

	@Override
	public void onEnable() {
		AvarionCore.getPlugin().getCommandManager().registerCommand(new SchematicBrowserCommand(this));
		
		final File saveDir = new File(WorldEdit.getInstance().getConfiguration().saveDir);
		if (saveDir.exists()) {
			schematicFolder = saveDir;
			log("Using WorldEdit's schematic directory with path: " + saveDir.getAbsolutePath());
		} else {
			final File fallback = new File(Bukkit.getWorldContainer() + File.separator + "plugins" + File.separator + "WorldEdit" + File.separator + "schematics");
			if (!fallback.exists()) {
				fallback.mkdirs();
			}
			schematicFolder = fallback;
			log("Using fallback schematic directory.");
		}
	}
	
	@Override
	protected void onDisable() { }

	@Override
	public String getModuleName() {
		return "SchematicBrowser";
	}

	@CommandAlias("schematicbrowser|sb")
	@CommandPermission("ct.schematicbrowser")
	public static class SchematicBrowserCommand extends BaseCommand {

		private final SchematicBrowser addon;
		
		public SchematicBrowserCommand(final SchematicBrowser plugin) {
			addon = plugin;
		}
		
		@Default
		public void accept(final Player sender) {
			SmartInventory.builder().title("Schematic Browser").size(5, 9).provider(new SchematicBrowserGUI(addon, addon.getSchematicFolder())).build().open(sender);
		}
	}
}
