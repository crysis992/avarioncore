/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.events.TreeEvent can not be copied and/or distributed without the express
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

package net.crytec.addons.timber.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import net.crytec.addons.timber.treefall.TreeChecker;

/**
 * Abstract tree event containing tree's blocks and broke block
 */
public abstract class TreeEvent extends PlayerEvent {
	
	protected final TreeChecker treeChecker;
	protected final Block broke;
	
    public TreeEvent(Player who, TreeChecker treeChecker, Block broke) {
        super(who);
        this.treeChecker = treeChecker;
        this.broke = broke;
    }

    /**
     * Get the tree checker
     * 
     * @return tree checker for the tree
     */
    public TreeChecker getTreeChecker() {
        return treeChecker;
    }

    /**
     * Get the initial block broke by player
     * 
     * @return block broke by player
     */
    public Block getBroke() {
        return broke;
    }
    
}
