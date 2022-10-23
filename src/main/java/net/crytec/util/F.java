/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.F can not be copied and/or distributed without the express
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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class F {

  private static final ChatColor name = ChatColor.YELLOW;
  private static final ChatColor normal = ChatColor.GRAY;
  private static final ChatColor element = ChatColor.LIGHT_PURPLE;

  public static String name(final String message) {
    return name + message + normal;
  }

  public static String main(final String body, final String message) {
    return ChatColor.BLUE + body + "> " + normal + message;
  }

  public static String elem(final String element) {
    return F.element + element + normal;
  }

  public static String tf(final boolean mode) {
    return mode ? ChatColor.GREEN + "aktiviert" : ChatColor.DARK_RED + "deaktiviert";
  }

  public static String ctf(final boolean mode, final String t, final String f) {
    return mode ? ChatColor.GREEN + t : ChatColor.DARK_RED + f;
  }

  public static String error(final String message) {
    return main(ChatColor.DARK_RED + "Fehler", message);
  }

  public static void message(final String body, final String message, final Player player) {
    player.sendMessage(main(body, message));
  }

  public static String format(final Iterable<?> objects, final String separator, final String ifEmpty) {
    if (!objects.iterator().hasNext()) {
      return ifEmpty;
    }
    final StringBuilder sb = new StringBuilder();
    objects.spliterator().forEachRemaining(con -> sb.append(con.toString()).append(separator));
    return sb.toString().substring(0, (sb.toString().length() - separator.length()));
  }

  public static void message(final String body, final String message, final Player... players) {
    for (final Player player : players) {
      message(body, message, player);
    }
  }

  public static void broadcast(final String body, final String message) {
    Bukkit.getOnlinePlayers().forEach(player -> message(body, message, player));
  }

  public static String getPercentageBar(final double current, final double max, final int size, final String segment) {
    final StringBuilder builder = new StringBuilder();
    int lows = (int) (size * ((1D / max) * current));
    int highs = size - lows;
    builder.append("§a");
    while (lows > 0) {
      builder.append(segment);
      lows--;
    }
    builder.append("§c");
    while (highs > 0) {
      builder.append(segment);
      highs--;
    }
    return builder.toString();
  }

}
