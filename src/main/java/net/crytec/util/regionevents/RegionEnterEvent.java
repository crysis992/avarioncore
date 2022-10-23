/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.regionevents.RegionEnterEvent can not be copied and/or distributed without the express
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
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * event that is triggered before a player enters a WorldGuard region, can be cancelled sometimes
 */

public class RegionEnterEvent extends RegionEvent implements Cancellable {

  private boolean cancelled, cancellable;

  /**
   * creates a new RegionEnterEvent
   *
   * @param region   the region the player is entering
   * @param player   the player who triggered the event
   * @param movement the type of movement how the player enters the region
   * @param parent
   */
  public RegionEnterEvent(final ProtectedRegion region, final Player player, final MovementWay movement, final Event parent) {
    super(region, player, movement, parent);
    cancelled = false;
    cancellable = true;

    if (movement == MovementWay.SPAWN || movement == MovementWay.DISCONNECT) {
      cancellable = false;
    }
  }

  /**
   * sets whether this event should be cancelled when the event is cancelled the player will not be able to move into the region
   *
   * @param cancelled true if the player should be stopped from moving into the region
   */
  @Override
  public void setCancelled(final boolean cancelled) {
    if (!cancellable) {
      return;
    }

    this.cancelled = cancelled;
  }

  /**
   * retrieves whether this event will be cancelled/has been cancelled by any plugin
   *
   * @return true if this event will be cancelled and the player will be stopped from moving
   */
  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * sometimes you can not cancel an event, i.e. if a player entered a region by spawning inside of it
   *
   * @return true, if you can cancel this event
   */
  public boolean isCancellable() {
    return cancellable;
  }

  protected void setCancellable(final boolean cancellable) {
    this.cancellable = cancellable;

    if (!this.cancellable) {
      cancelled = false;
    }
  }
}
