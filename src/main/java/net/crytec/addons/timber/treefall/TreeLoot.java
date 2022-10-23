/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.treefall.TreeLoot can not be copied and/or distributed without the express
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

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import net.crytec.addons.timber.utils.TreeConverter;

class TreeLoot {
    
    static Random random = new Random();
    
    static void dropTreeLoot(BlockData blockData, Location location, boolean hasSilkTouch) {
        World world = location.getWorld();
        Material originalMaterial = blockData.getMaterial();
        Material material = TreeConverter.convertLeavesToSapling(originalMaterial);
        
        if (originalMaterial.equals(Material.AIR) || originalMaterial.equals(Material.CAVE_AIR) || originalMaterial.equals(Material.VOID_AIR))
            return;
        
        if (hasSilkTouch) { // No bonus loot for silk touch
            world.dropItem(location, new ItemStack(TreeConverter.convertWoodToLog(originalMaterial), 1));
            return;
        }
        
        switch (material) {
        case VINE:
        case MUSHROOM_STEM:
            break;
            
        case BROWN_MUSHROOM_BLOCK:
        case RED_MUSHROOM_BLOCK:
            boolean isRed = material.equals(Material.RED_MUSHROOM_BLOCK);
            int numToDrop = Math.max(0, random.nextInt(10) - 7); // 80% chance to drop nothing, 10% chance for 1, 10% chance for 2
            if (numToDrop != 0)
                world.dropItem(location, new ItemStack(isRed ? Material.RED_MUSHROOM : Material.BROWN_MUSHROOM, numToDrop));
            break;
            
        case ACACIA_SAPLING:
        case BIRCH_SAPLING:
        case SPRUCE_SAPLING:
            boolean dropChance = random.nextInt(20) == 0; // 1/20 chance to drop sapling
            if (dropChance)
                world.dropItem(location, new ItemStack(material, 1));
            break;
            
        case JUNGLE_SAPLING:
            boolean jungleDropChance = random.nextInt(40) == 0; // 1/40 chance to drop sapling
            if (jungleDropChance)
                world.dropItem(location, new ItemStack(material, 1));
            break;
            
        case DARK_OAK_SAPLING:
        case OAK_SAPLING:
            boolean oakDropChance = random.nextInt(20) == 0; // 1/20 chance to drop sapling
            if (oakDropChance)
                world.dropItem(location, new ItemStack(material, 1));
            
            boolean appleDropChance = random.nextInt(200) == 0; // 1/200 chance to drop apple
            if (appleDropChance)
                world.dropItem(location, new ItemStack(Material.APPLE, 1));
            break;
            
        default:
            Material dropMaterial = TreeConverter.convertWoodToLog(material);
            world.dropItem(location, new ItemStack(dropMaterial, 1));
            break;
        }
    }


}
