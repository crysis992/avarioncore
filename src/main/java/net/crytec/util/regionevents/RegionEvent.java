/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.regionevents.RegionEvent can not be copied and/or distributed without the express
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
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public abstract class RegionEvent extends PlayerEvent {

  private static final HandlerList handlerList = new HandlerList();

  private final ProtectedRegion region;
  private final MovementWay movement;
  public Event parentEvent;

  public RegionEvent(final ProtectedRegion region, final Player player, final MovementWay movement, final Event parent) {
    super(player);
    this.region = region;
    this.movement = movement;
    parentEvent = parent;
  }

  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  public ProtectedRegion getRegion() {
    return region;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public MovementWay getMovementWay() {
    return movement;
  }

  /**
   * retrieves the event that has been used to create this event
   *
   * @return -
   * @see PlayerMoveEvent
   * @see PlayerTeleportEvent
   * @see PlayerQuitEvent
   * @see PlayerKickEvent
   * @see PlayerJoinEvent
   * @see PlayerRespawnEvent
   */
  public Event getParentEvent() {
    return parentEvent;
  }
}
