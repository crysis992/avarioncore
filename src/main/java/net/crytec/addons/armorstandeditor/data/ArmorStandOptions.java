/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.armorstandeditor.data.ArmorStandOptions can not be copied and/or distributed without the express
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

package net.crytec.addons.armorstandeditor.data;

import org.bukkit.Material;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArmorStandOptions {

	BASEPLATE("Bodenplatte", Material.STONE_SLAB),
	GRAVITY("Schwerkraft", Material.SAND),
	ARMS("Arme", Material.STICK),
	VISIBILLITY("Sichtbarkeit", Material.BARRIER),
	DISPLAYNAME("Name", Material.NAME_TAG),
	SIZE("Größe", Material.PUFFERFISH);
	
	@Getter
	private final String displayname;
	@Getter
	private final Material icon;

}