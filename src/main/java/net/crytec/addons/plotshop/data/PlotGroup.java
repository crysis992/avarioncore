/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.plotshop.data.PlotGroup can not be copied and/or distributed without the express
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

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PlotGroup {

  CITY_BEGINNER("Stadt", 1, 0, false, "plot.stadt"),
  SHOP("Shop", 1, 30, true, "plot.shop");

  @Getter
  private final String displayname;
  @Getter
  private final int purchaseLimit;
  @Getter
  private final int rentDays;
  @Getter
  private final boolean autoReset;
  @Getter
  private final String permission;
}