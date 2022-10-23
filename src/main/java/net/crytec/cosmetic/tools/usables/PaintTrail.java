/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.tools.usables.PaintTrail can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.tools.usables;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import net.crytec.AvarionCore;
import net.crytec.cosmetic.tools.ItemTool;
import net.crytec.libs.commons.utils.UtilMath;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PaintTrail extends ItemTool {

  private static final List<Material> colors = Arrays.stream(Material.values())
      .filter(mat -> !mat.isLegacy())
      .filter(mat -> mat.toString().contains("TERRACOTTA") && !mat.toString().contains("GLAZED"))
      .collect(Collectors.toList());

  public PaintTrail() {
    super("§dFarbexplosion", 160, Material.ORANGE_DYE);
  }

  @Override
  protected void onActivate(final PlayerInteractEvent event) {
    new Trail(event.getPlayer()).runTaskTimer(AvarionCore.getPlugin(), 1, 5);
  }

  @Override
  public boolean canEquip(final Player player) {
    return true;
  }

  private class Trail extends BukkitRunnable {

    private static final int maxRuns = 20;

    public Trail(final Player player) {
			entities = Sets.newHashSet();
      this.player = player;
    }

    private final HashSet<Item> entities;
    private final Player player;
    private int ticks = 0;

    @Override
    public void run() {

      if (ticks > maxRuns) {
				entities.forEach(item -> {
          if (!item.isDead()) {
            item.remove();
          }
        });
        super.cancel();
        return;
      }

      final Location location = player.getLocation().add(0.0D, 2.0D, 0.0D);
      final Item item = player.getWorld().dropItem(location, new ItemBuilder(colors.get(ThreadLocalRandom.current().nextInt(0, colors.size() - 1))).amount(1).build());
      item.setPickupDelay(Integer.MAX_VALUE);
      item.setVelocity(new Vector(UtilMath.random(-0.1F, 0.1F), 0.4F, UtilMath.random(-0.1F, 0.1F)));
			player.getWorld().playSound(location, Sound.ENTITY_CHICKEN_EGG, 0.1F, 1.0F);
			entities.add(item);
			ticks++;
    }

  }

}
