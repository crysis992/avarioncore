/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Bonemeal can not be copied and/or distributed without the express
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

package net.crytec.commands.administration;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.google.common.collect.Lists;
import io.netty.util.internal.ThreadLocalRandom;
import java.util.List;
import java.util.Set;
import net.crytec.libs.commons.utils.UtilBlock;
import net.crytec.util.F;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;

@CommandAlias("bonemeal")
@CommandPermission("ct.bonemeal")
public class Bonemeal extends BaseCommand {

  private final int max_radius = 35;

  private final List<Material> flowers = Lists.newArrayList();

  public Bonemeal() {
		flowers.add(Material.DANDELION);
		flowers.add(Material.BLUE_ORCHID);
		flowers.add(Material.RED_TULIP);
		flowers.add(Material.WHITE_TULIP);
		flowers.add(Material.FERN);
		flowers.add(Material.GRASS);
		flowers.add(Material.LARGE_FERN);
		flowers.add(Material.TALL_GRASS);
		flowers.add(Material.POPPY);
		flowers.add(Material.AZURE_BLUET);
		flowers.add(Material.OXEYE_DAISY);
		flowers.add(Material.LILAC);
  }

  @Subcommand("deletegrass")
  public void AntibonemealCommand(final Player sender, final int radius) {
    final Set<Block> blocks = UtilBlock.getInRadius(sender.getLocation(), radius, 4).keySet();

    int counter = 0;
    for (final Block block : blocks) {

      if (flowers.contains(block.getType())) {
        block.setType(Material.AIR, true);
        counter++;
      }
      block.getBlockKey();
//			if (block.getType() == Material.GRASS_BLOCK && block.getRelative(BlockFace.UP).getType() != Material.AIR) {
//
//				Block up = block.getRelative(BlockFace.UP);
//
//				if (flowers.contains(up.getType()) || up.getType() == Material.GRASS) {
//					up.setType(Material.AIR, true);
//					counter++;
//				}
//			}
    }
    sender.sendMessage(F.main("Admin", "Es wurden " + F.elem(String.valueOf(counter)) + " Blöcke aktualisiert."));
    return;
  }

  @Default
  public void bonemealArea(final Player sender, final int radius) {

    final Set<Block> blocks = UtilBlock.getInRadius(sender.getLocation(), radius, 4).keySet();

    int counter = 0;
    if (radius > max_radius) {
      sender.sendMessage(F.main("Admin", "Der Radius ist zu groß."));
      return;
    }

    for (final Block block : blocks) {

      if (block.getBlockData() instanceof Ageable) {

        final Ageable crop = (Ageable) block.getBlockData();
        crop.setAge(crop.getMaximumAge());

        block.setBlockData(crop, true);
        counter++;
      } else if (block.getType() == Material.GRASS_BLOCK && block.getRelative(BlockFace.UP).getType() == Material.AIR) {

        final int chance = ThreadLocalRandom.current().nextInt(0, 100);
        final Block up = block.getRelative(BlockFace.UP);
        if (chance < 80) {
          up.setType(Material.GRASS, true);
          counter++;
        } else {
          up.setType(flowers.get(ThreadLocalRandom.current().nextInt(flowers.size())), true);
          counter++;
        }
      }
    }

    sender.sendMessage(F.main("Admin", "Es wurden " + F.elem(String.valueOf(counter)) + " Blöcke aktualisiert."));
    return;
  }
}
