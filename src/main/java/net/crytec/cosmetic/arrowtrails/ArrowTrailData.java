/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.arrowtrails.ArrowTrailData can not be copied and/or distributed without the express
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

import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class ArrowTrailData {

	private ItemStack icon;
	private String name;
	private String permission;
	private Particle particle;
	private int amount;
	
	public ArrowTrailData(ItemStack icon, String name, String permission, Particle particle, int amount) {
		this.icon = icon;
		this.name = name;
		this.permission = permission;
		this.particle = particle;
		this.amount = amount;
	}
	
	public ItemStack getIcon() {
		return this.icon;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	public Particle getParticle() {
		return this.particle;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
}
