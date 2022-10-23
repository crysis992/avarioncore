/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.armorstandeditor.ArmorStandEditorCommand can not be copied and/or distributed without the express
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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.util.F;
import org.bukkit.entity.Player;


@CommandAlias("ase")
@CommandPermission("ct.asedit")
public class ArmorStandEditorCommand extends BaseCommand {
	
	private final ArmorStandEditor addon;
	
	public ArmorStandEditorCommand(final ArmorStandEditor addon) {
		this.addon = addon;
	}

	
	@Default
	public void createLink(final Player player) {
		addon.link(player);
	}
	
	@Subcommand("menu")
	public void openGUI(final Player player) {
		if (addon.isInEditor(player)) {
			SmartInventory.builder().provider(new ArmorStandGUI(addon.getEditor(player))).title("Armorstand Edtior").size(6).build().open(player);
		}
	}
	
	@Subcommand("end|done|exit")
	public void exitEditing(final Player player) {
		addon.removeEditor(player);
		player.sendMessage(F.main("Editor", "Armorstand Editor wurde beendet."));
	}
}