/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.Playtime can not be copied and/or distributed without the express
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

package net.crytec.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import net.crytec.libs.commons.utils.UtilTime;
import net.crytec.util.F;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

@CommandAlias("spielzeit")
public class Playtime extends BaseCommand {

  private final long lastUpdate;
  private final Gson gson;
  private final LinkedHashSet<String> ranklist;

  public Playtime() {
		ranklist = Sets.newLinkedHashSet();
		lastUpdate = 0;
		gson = new Gson();
		update(null);
  }

  @Default
  public void getPlaytime(final Player issuer) {
    final String val = UtilTime.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(issuer.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20));
    issuer.sendMessage(F.main("Info", "Deine gesamte Spielzeit auf diesem Server beträgt " + F.elem(ChatColor.AQUA + val)));
  }

  @Subcommand("top")
  public void getPlaytimeTop(final Player issuer) {
		update(issuer);
  }


  private void update(final Player player) {
//		final File statsFolder = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "stats");
//
//		if (lastUpdate + TimeUnit.SECONDS.toMillis(10) > System.currentTimeMillis()) {
//			sendList(player);
//			return;
//		}
//
//		AvarionCore.getTaskChainFactory().newChain()
//		.asyncFirst( () -> {
//			LinkedHashMap<String, Integer> ranking = Maps.newLinkedHashMap();
//
//			for (File file : Files.fileTreeTraverser().breadthFirstTraversal(statsFolder)) {
//				if (!file.isFile()) continue;
//
//				try {
//				JsonObject obj = gson.fromJson(new FileReader(file), JsonObject.class);
//				int time = obj.get("stats").getAsJsonObject().get("minecraft:custom").getAsJsonObject().get("minecraft:play_one_minute").getAsInt();
//				OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(file.getName().substring(0, file.getName().length() - 5)));
//				ranking.put(op.getName(), time);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			return ranking;
//		}).async( (ranking) -> {
//			ranklist.clear();
//			ranking.entrySet().stream()
//			.sorted(Entry.comparingByValue(Comparator.reverseOrder())).limit(15).forEachOrdered( a -> {
//				ranklist.add(new StringBuilder()
//						.append(ChatColor.YELLOW)
//						.append(a.getKey())
//						.append(ChatColor.GRAY)
//						.append(" mit ")
//						.append(ChatColor.AQUA)
//						.append(UtilTime.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(a.getValue() / 20)))
//						.toString());
//			});
//			return null;
//		}).syncLast( (t) -> {
//					if (player != null) {
//						sendList(player);
//					}
//			lastUpdate = System.currentTimeMillis();
//					return;
//		}).execute();
  }

  private void sendList(final Player player) {
    int r = 1;
    player.sendMessage("§7§m§l=========== §r§l" + (char) 36936 + " Top 15 Spielzeit " + (char) 36936 + "§7§m§l===========");
    player.sendMessage("");
    for (final String line : ranklist) {
      player.sendMessage(new StringBuilder().append(ChatColor.GRAY).append('#').append(ChatColor.GOLD).append(r).append(' ').append(line).toString());
      r++;
    }
    player.sendMessage("");
    player.sendMessage("§7§m§l========================================");

  }

}
