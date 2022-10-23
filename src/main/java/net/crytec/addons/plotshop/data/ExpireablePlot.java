/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.data.ExpireablePlot can not be copied and/or distributed without the express
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

package net.crytec.addons.plotshop.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import net.crytec.libs.commons.utils.UtilTime;
import org.bukkit.World;

public class ExpireablePlot extends Plot {

  private long expireDate = 0;

  public ExpireablePlot(final World world, final ProtectedRegion region, final PlotGroup plotGroup) {
    super(world, region, plotGroup);
  }

  @JsonCreator
  public ExpireablePlot(final UUID world, final String protectedRegionID, final PlotGroup plotGroup) {
    super(world, protectedRegionID, plotGroup);
  }

  public boolean isAboutToExpire() {
    if (getOwner() == null) {
      return false;
    }
    final long hours = LocalDateTime.now().until(UtilTime.millisToLocalDate(expireDate), ChronoUnit.HOURS);
    return (hours < 48);
  }

  public int getExpireHours() {
    return (int) LocalDateTime.now().until(UtilTime.millisToLocalDate(expireDate), ChronoUnit.HOURS);
  }

  public boolean isExpired() {
    final LocalDateTime expireTime = UtilTime.millisToLocalDate(expireDate);
    return LocalDateTime.now().isAfter(UtilTime.millisToLocalDate(expireDate));
  }

  @JsonIgnore
  public String getExpireDateFormatted() {
    if (isExpired()) {
      return "Abgelaufen";
    } else {
      return UtilTime.when(expireDate);
    }
  }

  public long getExpireDate() {
    return expireDate;
  }

  public void setExpireDate(final long expireDate) {
    this.expireDate = expireDate;
  }

}
