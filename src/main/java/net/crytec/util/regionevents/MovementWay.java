/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.regionevents.MovementWay can not be copied and/or distributed without the express
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

/**
 * describes the way how a player left/entered a region
 */
public enum MovementWay {
  /**
   * this way is used if a player entered/left a region by walking
   */
  MOVE,
  /**
   * this way is used if a player teleported into a region / out of a region
   */
  TELEPORT,
  /**
   * this way is used if a player spawned in a region
   */
  SPAWN,
  /**
   * this way is used if a player is inside a vehicle
   */
  RIDE,
  /**
   * this way is used if a player changes the world.
   */
  WORLD_CHANGE,
  /**
   * this way is used if a player left a region by disconnecting
   */
  DISCONNECT
}
