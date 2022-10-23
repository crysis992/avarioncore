/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.arrowtrails.ArrowTrailTask can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.arrowtrails;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitTask;

import net.crytec.AvarionCore;

public class ArrowTrailTask implements Runnable {

	private final Arrow arrow;
	private final ArrowTrailData data;
	private final World world;
	private final BukkitTask task;

	public ArrowTrailTask(Arrow arrow, ArrowTrailData data) {
		this.world = arrow.getWorld();
		this.arrow = arrow;
		this.data = data;
		this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(AvarionCore.getPlugin(), this, 1, 5);
	}

	@Override
	public void run() {
		if (this.arrow == null || this.arrow.isDead() || this.arrow.isInBlock() || this.arrow.isOnGround()) {
			this.task.cancel();
			return;
		}
		this.world.spawnParticle(data.getParticle(), this.arrow.getLocation(), data.getAmount(), 0, 0, 0, 0);
	}
}