/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.PermissionRegistrar can not be copied and/or distributed without the express
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

package net.crytec.util;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionRegistrar {

	public static Permission addPermission(String perm) {
		return addPermission(perm, "No description provided", PermissionDefault.OP);
	}

	public static Permission addPermission(String perm, String description) {
		return addPermission(perm, description, PermissionDefault.OP);
	}

	public static Permission addPermission(String perm, String description, PermissionDefault defaultValue) {
		try {
		Permission permission = new Permission(perm, description, defaultValue);
		Bukkit.getPluginManager().addPermission(permission);
		return permission;
		} catch(IllegalArgumentException ex) {
			return Bukkit.getPluginManager().getPermission(perm);
		}
	}
}