/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 11.12.19, 14:31	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.cooldown.ComposedCooldownMap can not be copied and/or distributed without the express
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

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.annotation.Nonnull;

public class ComposedCooldownMap<I, O> {

  private final Cooldown base;
  private final LoadingCache<O, Cooldown> cache;
  private final Function<I, O> composeFunction;

  ComposedCooldownMap(final Cooldown base, final Function<I, O> composeFunction) {
    this.base = base;
    this.composeFunction = composeFunction;
    cache = CacheBuilder.newBuilder()
        // remove from the cache 10 seconds after the cooldown expires
        .expireAfterAccess(base.getTimeout() + 10000L, TimeUnit.MILLISECONDS)
        .build(new CacheLoader<>() {
          @Override
          public Cooldown load(@Nonnull final O key) {
            return base.clone();
          }
        });
  }

  @Nonnull
  public Cooldown getBase() {
    return base;
  }

  @Nonnull
  public Cooldown get(@Nonnull final I key) {
    Objects.requireNonNull(key, "key");
    return cache.getUnchecked(composeFunction.apply(key));
  }

  public void put(@Nonnull final O key, @Nonnull final Cooldown cooldown) {
    Objects.requireNonNull(key, "key");
    Preconditions.checkArgument(cooldown.getTimeout() == base.getTimeout(), "different timeout");
    cache.put(key, cooldown);
  }

  @Nonnull
  public Map<O, Cooldown> getAll() {
    return cache.asMap();
  }
}
