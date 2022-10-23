/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.timber.treefall.TreeEntityDamage can not be copied and/or distributed without the express
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

import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;

class TreeEntityDamage {

  static void runDamage(final FallingBlock fallingBlock) {

    for (final Entity entity : fallingBlock.getNearbyEntities(0.5, 0.5, 0.5)) {

      if (!(entity instanceof LivingEntity)) {
        continue;
      }

      ((LivingEntity) entity).damage(1);

    }

  }

}
