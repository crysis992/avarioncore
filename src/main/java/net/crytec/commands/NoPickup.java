/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.NoPickup can not be copied and/or distributed without the express
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

package net.crytec.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("nopickup|np")
@Description("Verhindert das aufsammeln von herumliegenden Items.")
@CommandPermission("ct.nopickup")
public class NoPickup extends BaseCommand {

	@Default
	public void disablePickup(final Player sender) {
		if (sender.getCanPickupItems()) {
			sender.setCanPickupItems(false);
			sender.sendMessage(F.main("Info", "Du sammelst ab jetzt keine Items mehr auf."));
		} else {
			sender.setCanPickupItems(true);
			sender.sendMessage(F.main("Info", "Du kannst nun wieder Items aufsammeln."));
		}
	}
}