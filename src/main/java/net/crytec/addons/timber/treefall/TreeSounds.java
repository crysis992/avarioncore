/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.treefall.TreeSounds can not be copied and/or distributed without the express
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

package net.crytec.addons.timber.treefall;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;

class TreeSounds {

	static void tipOverNoise(Location location) {

		location.getWorld().playSound(location, Sound.BLOCK_CHEST_OPEN, 3F, 0.1F);
	}

	static void fallNoise(FallingBlock fallingBlock) {
		if (fallingBlock.getTicksLived() < 20)
			fallingBlock.getWorld().playSound(fallingBlock.getLocation(), Sound.BLOCK_ANVIL_FALL, 3F, 0.1F);
		else
			fallingBlock.getWorld().playSound(fallingBlock.getLocation(), Sound.BLOCK_WOOD_FALL, 3F, 0.1F);

	}

}
