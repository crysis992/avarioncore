/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.regionevents.RegionLeftEvent can not be copied and/or distributed without the express
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

package net.crytec.util.regionevents;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class RegionLeftEvent extends RegionEvent {

  /**
   * creates a new RegionLeftEvent
   *
   * @param region   the region the player has left
   * @param player   the player who triggered the event
   * @param movement the type of movement how the player left the region
   * @param parent
   */
  public RegionLeftEvent(final ProtectedRegion region, final Player player, final MovementWay movement, final Event parent) {
    super(region, player, movement, parent);
  }
}
