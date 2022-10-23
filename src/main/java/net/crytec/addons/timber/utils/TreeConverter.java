/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.utils.TreeConverter can not be copied and/or distributed without the express
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

package net.crytec.addons.timber.utils;

import org.bukkit.Material;

import com.google.common.collect.ImmutableMap;

public class TreeConverter {
    
	private static final ImmutableMap<Material, Material> woodToLog = new ImmutableMap.Builder<Material, Material>()
	        .put(Material.ACACIA_WOOD, Material.ACACIA_LOG)
	        .put(Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_ACACIA_LOG)
	        .put(Material.BIRCH_WOOD, Material.BIRCH_LOG)
	        .put(Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_BIRCH_LOG)
	        .put(Material.DARK_OAK_WOOD, Material.DARK_OAK_LOG)
	        .put(Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_DARK_OAK_LOG)
	        .put(Material.JUNGLE_WOOD, Material.JUNGLE_LOG)
	        .put(Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_JUNGLE_LOG)
	        .put(Material.OAK_WOOD, Material.OAK_LOG)
	        .put(Material.STRIPPED_OAK_WOOD, Material.STRIPPED_OAK_LOG)
	        .put(Material.SPRUCE_WOOD, Material.SPRUCE_LOG)
	        .put(Material.STRIPPED_SPRUCE_WOOD, Material.STRIPPED_SPRUCE_LOG)
			.build();
	
	private static final ImmutableMap<Material, Material> logToLeaf = new ImmutableMap.Builder<Material, Material>()
	        .put(Material.ACACIA_LOG, Material.ACACIA_LEAVES)
	        .put(Material.ACACIA_WOOD, Material.ACACIA_LEAVES)
	        .put(Material.STRIPPED_ACACIA_LOG, Material.ACACIA_LEAVES)
	        .put(Material.STRIPPED_ACACIA_WOOD, Material.ACACIA_LEAVES)
	        .put(Material.BIRCH_LOG, Material.BIRCH_LEAVES)
	        .put(Material.BIRCH_WOOD, Material.BIRCH_LEAVES)
	        .put(Material.STRIPPED_BIRCH_LOG, Material.BIRCH_LEAVES)
	        .put(Material.STRIPPED_BIRCH_WOOD, Material.BIRCH_LEAVES)
	        .put(Material.DARK_OAK_LOG, Material.DARK_OAK_LEAVES)
	        .put(Material.DARK_OAK_WOOD, Material.DARK_OAK_LEAVES)
	        .put(Material.STRIPPED_DARK_OAK_LOG, Material.DARK_OAK_LEAVES)
	        .put(Material.STRIPPED_DARK_OAK_WOOD, Material.DARK_OAK_LEAVES)
	        .put(Material.JUNGLE_LOG, Material.JUNGLE_LEAVES)
	        .put(Material.JUNGLE_WOOD, Material.JUNGLE_LEAVES)
	        .put(Material.STRIPPED_JUNGLE_LOG, Material.JUNGLE_LEAVES)
	        .put(Material.STRIPPED_JUNGLE_WOOD, Material.JUNGLE_LEAVES)
	        .put(Material.OAK_LOG, Material.OAK_LEAVES)
	        .put(Material.OAK_WOOD, Material.OAK_LEAVES)
	        .put(Material.STRIPPED_OAK_LOG, Material.OAK_LEAVES)
	        .put(Material.STRIPPED_OAK_WOOD, Material.OAK_LEAVES)
	        .put(Material.SPRUCE_LOG, Material.SPRUCE_LEAVES)
	        .put(Material.SPRUCE_WOOD, Material.SPRUCE_LEAVES)
	        .put(Material.STRIPPED_SPRUCE_LOG, Material.SPRUCE_LEAVES)
	        .put(Material.STRIPPED_SPRUCE_WOOD, Material.SPRUCE_LEAVES)
	        .put(Material.MUSHROOM_STEM, Material.MUSHROOM_STEM)
			.build();
	
	
	private static final ImmutableMap<Material, Material> leavesToSapling = new ImmutableMap.Builder<Material, Material>()
	        .put(Material.ACACIA_LEAVES, Material.ACACIA_SAPLING)
	        .put(Material.BIRCH_LEAVES, Material.BIRCH_SAPLING)
	        .put(Material.DARK_OAK_LEAVES, Material.DARK_OAK_SAPLING)
	        .put(Material.JUNGLE_LEAVES, Material.JUNGLE_SAPLING)
	        .put(Material.OAK_LEAVES, Material.OAK_SAPLING)
	        .put(Material.SPRUCE_LEAVES, Material.SPRUCE_SAPLING)
			.build();
	
	
	public static Material convertLeavesToSapling(Material material) {
		return leavesToSapling.getOrDefault(material, material);
	}
	
	
    public static Material convertLogToLeaf(Material material) {
        return logToLeaf.get(material);
    }
	
    public static Material convertWoodToLog(Material material) {
        return woodToLog.getOrDefault(material, material);
    }

}
