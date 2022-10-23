/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.paintingswitch.PaintingData can not be copied and/or distributed without the express
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

package net.crytec.addons.paintingswitch;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

public class PaintingData {

	  public static Map<String, PaintingData> playerSettings = new HashMap<String, PaintingData>();
	  public boolean clicked = false;
	  public Block block = null;
	  public Painting painting = null;
	  public Painting previousPainting = null;
	  public Location location = null;
	  
	public static PaintingData getSettings(Player player) {
		PaintingData settings = (PaintingData) playerSettings.get(player.getName());
		if (settings == null) {
			playerSettings.put(player.getName(), new PaintingData());
			settings = (PaintingData) playerSettings.get(player.getName());
		}
		return settings;
	}
	  
	public static void clear(String playerName) {
		playerSettings.get(playerName).painting = null;
		playerSettings.get(playerName).block = null;
		playerSettings.get(playerName).clicked = false;
		playerSettings.get(playerName).location = null;
	}
	  
	public static void clear(Player player) {
		clear(player.getName());
	}
	
}
