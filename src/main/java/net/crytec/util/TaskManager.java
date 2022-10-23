/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.TaskManager can not be copied and/or distributed without the express
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

package net.crytec.util;

import com.google.common.base.Preconditions;
import net.crytec.AvarionCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class TaskManager {

  private static AvarionCore plugin;

  protected TaskManager(final AvarionCore plugin) {
    TaskManager.plugin = plugin;
  }

  @NotNull
  public static BukkitTask runTask(final Runnable runnable) {
    return runTask(runnable, false);
  }

  @NotNull
  public static BukkitTask runTask(final Runnable runnable, final boolean async) {
    Preconditions.checkNotNull(runnable, "Runnable cannot be null");
    return async ? Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable) : Bukkit.getScheduler().runTask(plugin, runnable);
  }

  @NotNull
  public static BukkitTask schedule(final Runnable runnable, final int delay, final int interval, final int repeatitions) {
    Preconditions.checkNotNull(runnable, "Runnable cannot be null");
    return new RepeatingTask(runnable, repeatitions).runTaskTimer(plugin, delay, interval);
  }

  @NotNull
  public static BukkitTask runTaskLater(final Runnable runnable, final long delay) {
    Preconditions.checkNotNull(runnable, "Runnable cannot be null");
    return runTaskLater(runnable, false, delay);
  }

  @NotNull
  public static BukkitTask runTaskLater(final Runnable runnable, final boolean async, final long delay) {
    Preconditions.checkNotNull(runnable, "Runnable cannot be null");
    return async ? Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay) : Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
  }

  @NotNull
  public static BukkitTask scheduleAsync(final Runnable runnable, final int delay, final int interval, final int repeatitions) {
    Preconditions.checkNotNull(runnable, "Runnable cannot be null");
    return new RepeatingTask(runnable, repeatitions).runTaskTimerAsynchronously(plugin, delay, interval);
  }

  @NotNull
  public static BukkitTask schedule(final ConditionalRunnable runnable, final int delay, final int interval, final int repeatitions) {
    Preconditions.checkNotNull(runnable, "ConditionalRunnable cannot be null");
    return new RepeatTask(runnable, repeatitions).runTaskTimer(plugin, delay, interval);
  }

  @NotNull
  public static BukkitTask scheduleAsync(final ConditionalRunnable runnable, final int delay, final int interval, final int repeatitions) {
    Preconditions.checkNotNull(runnable, "ConditionalRunnable cannot be null");
    return new RepeatTask(runnable, repeatitions).runTaskTimerAsynchronously(plugin, delay, interval);
  }

  private static class RepeatTask extends BukkitRunnable {

    private final ConditionalRunnable runnable;
    private int repeat;

    public RepeatTask(final ConditionalRunnable runnable, final int repeat) {
      this.runnable = runnable;
      this.repeat = repeat;
    }

    @Override
    public void run() {

      if (runnable.canCancel()) {
        cancel();
        runnable.onCancel();
        return;
      }

      runnable.run();

      if (repeat == -1) return;
      else
        repeat--;

      if (repeat <= 0) {
        cancel();
        runnable.onCancel();
        runnable.onRunout();
      }
    }
  }

  public interface ConditionalRunnable extends Runnable {
    @Override
    void run();
    boolean canCancel();
    default void onCancel() {}
    default void onRunout() {}
  }

  private static class RepeatingTask extends BukkitRunnable {

    private final Runnable runnable;
    private int repeat;

    public RepeatingTask(final Runnable runnable, final int repeat) {
      this.runnable = runnable;
      this.repeat = repeat;
    }

    @Override
    public void run() {
      runnable.run();

      if (repeat == -1) return;
      else
        repeat--;

      if (repeat <= 0) {
        cancel();
      }
    }
  }

}
