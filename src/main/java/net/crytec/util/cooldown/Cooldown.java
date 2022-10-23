/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 11.12.19, 14:25	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.cooldown.Cooldown can not be copied and/or distributed without the express
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

package net.crytec.util.cooldown;

import java.time.Instant;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

public class Cooldown {

  //When the last test occurred
  private long lastTested;

  //The cooldown duration in millis
  private final long timeout;

  public Cooldown(final long amount, final TimeUnit unit) {
    timeout = unit.toMillis(amount);
    lastTested = 0;
  }

  /**
   * Creates a cooldown lasting a specified amount of time
   *
   * @param amount the amount of time
   * @param unit   the unit of time
   * @return a new cooldown
   */
  @Nonnull
  static Cooldown of(final long amount, @Nonnull final TimeUnit unit) {
    return new Cooldown(amount, unit);
  }

  /**
   * Returns true if the cooldown is not active, and then resets the timer
   *
   * <p>If the cooldown is currently active, the timer is <strong>not</strong> reset.</p>
   *
   * @return true if the cooldown is not active
   */
  public boolean test() {
    if (!testSilently()) {
      return false;
    }

    reset();
    return true;
  }

  /**
   * Returns the elapsed time in milliseconds since the cooldown was last reset, or since creation time
   *
   * @return the elapsed time
   */
  public long elapsed() {
    return Instant.now().toEpochMilli() - getLastTested().orElse(0);
  }

  /**
   * Returns true if the cooldown is not active
   *
   * @return true if the cooldown is not active
   */
  public boolean testSilently() {
    return elapsed() > getTimeout();
  }

  /**
   * Resets the cooldown
   */
  public void reset() {
    setLastTested(Instant.now().toEpochMilli());
  }

  /**
   * Gets the time in milliseconds until the cooldown will become inactive.
   *
   * <p>If the cooldown is not active, this method returns <code>0</code>.</p>
   *
   * @return the time in millis until the cooldown will expire
   */
  public long remainingMillis() {
    final long diff = elapsed();
    return diff > getTimeout() ? 0L : getTimeout() - diff;
  }

  /**
   * Gets the time until the cooldown will become inactive.
   *
   * <p>If the cooldown is not active, this method returns <code>0</code>.</p>
   *
   * @param unit the unit to return in
   * @return the time until the cooldown will expire
   */
  public long remainingTime(final TimeUnit unit) {
    return Math.max(0L, unit.convert(remainingMillis(), TimeUnit.MILLISECONDS));
  }

  public OptionalLong getLastTested() {
    return lastTested == 0 ? OptionalLong.empty() : OptionalLong.of(lastTested);
  }

  public void setLastTested(final long time) {
    if (time <= 0) {
      lastTested = 0;
    } else {
      lastTested = time;
    }
  }

  public long getTimeout() {
    return timeout;
  }

  @Override
  public Cooldown clone() {
    return new Cooldown(timeout, TimeUnit.MILLISECONDS);
  }

}
