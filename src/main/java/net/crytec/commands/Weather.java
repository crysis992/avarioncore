/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Weather can not be copied and/or distributed without the express
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
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import java.util.Random;
import net.crytec.util.F;
import org.bukkit.entity.Player;

@CommandAlias("weather")
@CommandPermission("ct.weather")
@Description("Erlabut es dir das Wetter zu ändern.")
public class Weather extends BaseCommand {

	@Default
	public void sendHelp(final CommandIssuer issuer, final CommandHelp help) {
		help.showHelp(issuer);
	}
	
	@Subcommand("sun|clear")
	public void setSun(final Player issuer) {
		final int duration = (300 + new Random().nextInt(600)) * 20;
		issuer.getWorld().setWeatherDuration(duration);
		issuer.getWorld().setThunderDuration(duration);
		issuer.getWorld().setThundering(false);
		issuer.getWorld().setStorm(false);
		issuer.sendMessage(F.main("Wetter", "In der Welt §6" + issuer.getWorld().getName() + "§7 scheint nun die §6Sonne§7."));
	}
	
	@Subcommand("rain")
	public void setRaining(final Player issuer) {
		final int duration = (300 + new Random().nextInt(600)) * 20;
		issuer.getWorld().setWeatherDuration(duration);
		issuer.getWorld().setThunderDuration(duration);
		issuer.getWorld().setThundering(false);
		issuer.getWorld().setStorm(true);
		issuer.sendMessage(F.main("Wetter", "In der Welt §6" + issuer.getWorld().getName() + " regnet§7 es nun."));
	}
	
	@Subcommand("thunder|storm")
	public void setStorm(final Player issuer) {
		final int duration = (300 + new Random().nextInt(600)) * 20;
		issuer.getWorld().setWeatherDuration(duration);
		issuer.getWorld().setThunderDuration(duration);
		issuer.getWorld().setThundering(true);
		issuer.getWorld().setStorm(true);
		issuer.sendMessage(F.main("Wetter", "In der Welt §6" + issuer.getWorld().getName() + " stürmt§7 es nun."));
	}
}