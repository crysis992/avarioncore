/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.chairs.Chairs can not be copied and/or distributed without the express
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

package net.crytec.addons.chairs;

import com.destroystokyo.paper.MaterialSetTag;
import net.crytec.addons.Addon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class Chairs extends Addon {

  //  public List<ChairBlock> allowedBlocks = new ArrayList<>();
  public MaterialSetTag SIT_BLOCKS;

  public boolean autoRotate = true;
//  public boolean ignoreIfBlockInHand = false;
//  public double distance = 2;
//  public int maxChairWidth = 3;


  private PlayerSitData psitdata;

  public PlayerSitData getPlayerSitData() {
    return psitdata;
  }

  @Override
  public void onEnable() {
    psitdata = new PlayerSitData();
    Bukkit.getServer().getPluginManager().registerEvents(new TrySitEventListener(this), getPlugin());
    Bukkit.getServer().getPluginManager().registerEvents(new TryUnsitEventListener(this), getPlugin());

    SIT_BLOCKS = new MaterialSetTag(new NamespacedKey(getPlugin(), "chairs"));
    SIT_BLOCKS.add(Material.ACACIA_STAIRS, Material.BIRCH_STAIRS, Material.BRICK_STAIRS, Material.SANDSTONE_STAIRS, Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_STAIRS,
        Material.STONE_BRICK_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.OAK_STAIRS, Material.DARK_OAK_STAIRS, Material.RED_SANDSTONE_STAIRS);
  }

  @Override
  public void onDisable() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      if (psitdata.isSitting(player)) {
        psitdata.unsitPlayer(player);
      }
    }
  }

  @Override
  public String getModuleName() {
    return "Chairs";
  }
}