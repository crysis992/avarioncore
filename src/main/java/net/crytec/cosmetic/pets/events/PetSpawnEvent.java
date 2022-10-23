/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.pets.events.PetSpawnEvent can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.pets.events;

import net.crytec.cosmetic.pets.Pet;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetSpawnEvent extends Event implements Cancellable {

  private static final HandlerList handlers = new HandlerList();

  private boolean cancelled;
  private final Player owner;
  private final Pet pet;
  private final World world;

  public PetSpawnEvent(final Player owner, final Pet pet) {
    this.owner = owner;
		world = owner.getWorld();
    this.pet = pet;
  }

  @Override
	public boolean isCancelled() {
    return cancelled;
  }

  public Pet getPet() {
    return pet;
  }

  public Player getPetOwner() {
    return owner;
  }

  public World getWorld() {
    return world;
  }

  @Override
	public void setCancelled(final boolean bln) {
		cancelled = bln;
  }

  @Override
	public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
