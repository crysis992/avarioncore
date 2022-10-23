/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Teleport.ChunkTeleport can not be copied and/or distributed without the express
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

package net.crytec.commands.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.crytec.libs.commons.utils.UtilBlock;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@CommandAlias("chunktp")
@CommandPermission("ct.chunktp")
public class ChunkTeleport extends BaseCommand {


  @Default
  public void teleportToChunk(final Player sender, final int x, final int z) {
    final Chunk chunk = sender.getWorld().getChunkAt(x, z);
    final Location location = chunk.getBlock(8, 64, 8).getLocation();

    final Block block = UtilBlock.getHighest(location.getWorld(), location.getBlockX(), location.getBlockZ());
    sender.teleport(block.getLocation().clone().add(0, 1, 0));

    UtilPlayer.playSound(sender, Sound.ENTITY_ENDERMAN_TELEPORT);
    sender.sendMessage(F.main("Teleport", "Du wurdest zu Chunk " + F.elem(String.valueOf(x)) + " " + F.elem(String.valueOf(z)) + " teleportiert."));
  }
}