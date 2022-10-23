/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 11.12.19, 14:28	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.cooldown.CooldownMap can not be copied and/or distributed without the express
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
import javax.annotation.Nonnull;

public class CooldownMap<T> {

  private final Cooldown base;
  private final LoadingCache<T, Cooldown> cache;

  CooldownMap(final Cooldown base) {
    this.base = base;
    cache = CacheBuilder.newBuilder()
        // remove from the cache 10 seconds after the cooldown expires
        .expireAfterAccess(base.getTimeout() + 10000L, TimeUnit.MILLISECONDS)
        .build(new CacheLoader<>() {
          @Override
          public Cooldown load(@Nonnull final T key) {
            return base.clone();
          }
        });
  }

  public Cooldown getBase() {
    return base;
  }

  public Cooldown get(@Nonnull final T key) {
    Objects.requireNonNull(key, "key");
    return cache.getUnchecked(key);
  }

  public void put(@Nonnull final T key, @Nonnull final Cooldown cooldown) {
    Objects.requireNonNull(key, "key");
    Preconditions.checkArgument(cooldown.getTimeout() == base.getTimeout(), "different timeout");
    cache.put(key, cooldown);
  }

  public Map<T, Cooldown> getAll() {
    return cache.asMap();
  }

}
