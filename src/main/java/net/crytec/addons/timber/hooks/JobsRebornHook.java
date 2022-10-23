/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.hooks.JobsRebornHook can not be copied and/or distributed without the express
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

package net.crytec.addons.timber.hooks;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.JobsPlayer;

import net.crytec.addons.timber.utils.TreeConverter;

public class JobsRebornHook implements TimberHook {

    @Override
    public void apply(Player player, HashSet<Block> treeBlocks) throws Exception {
        if (player.getGameMode().equals(GameMode.CREATIVE)) 
            return;
        
        // Replicate the same code that Jobs Reborn uses
        JobsPlayer jPlayer = Jobs.getPlayerManager().getJobsPlayer(player);
        if (jPlayer == null) 
            return;
        Set<Block> logs = treeBlocks.stream().filter(x -> TreeConverter.convertWoodToLog(x.getType()).name().endsWith("LOG")).collect(Collectors.toSet());
        for (Block log : logs) {
            BlockActionInfo bInfo = new BlockActionInfo(log, ActionType.BREAK);
            Jobs.action(jPlayer, bInfo, log);
        }
    }

}
