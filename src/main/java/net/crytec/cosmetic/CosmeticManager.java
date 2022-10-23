/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.CosmeticManager can not be copied and/or distributed without the express
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

package net.crytec.cosmetic;

import java.io.File;
import java.io.IOException;
import lombok.Getter;
import net.crytec.AvarionCore;
import net.crytec.cosmetic.arrowtrails.ArrowTrail;
import net.crytec.cosmetic.particle.TrailManager;
import net.crytec.cosmetic.pets.PetManager;
import net.crytec.cosmetic.tools.ItemToolManager;
import net.crytec.cosmetic.tools.usables.PaintTrail;
import org.bukkit.configuration.file.YamlConfiguration;

public class CosmeticManager {

  @Getter
  private final YamlConfiguration config;

  public CosmeticManager(final AvarionCore plugin) {

    final File file = new File(plugin.getDataFolder(), "cosmetics.yml");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (final IOException ex) {
        ex.printStackTrace();
      }
    }

		config = YamlConfiguration.loadConfiguration(file);

		itemToolManager = new ItemToolManager();
		trailManager = new TrailManager(this);
		petManager = new PetManager(this);
		arrowTrailManager = new ArrowTrail(this);
		getItemToolManager().registerTool(new PaintTrail());
    plugin.getCommandManager().registerCommand(new CosmeticCommand(this));
  }

  @Getter
  private final ItemToolManager itemToolManager;
  @Getter
  private final TrailManager trailManager;
  @Getter
  private final ArrowTrail arrowTrailManager;
  @Getter
  private final PetManager petManager;

  public void saveConfig() {
    final File file = new File(AvarionCore.getPlugin().getDataFolder(), "cosmetics.yml");
    try {
			config.save(file);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
