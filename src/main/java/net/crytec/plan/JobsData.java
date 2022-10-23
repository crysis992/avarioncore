/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.plan.JobsData can not be copied and/or distributed without the express
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

package net.crytec.plan;

import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.annotation.Tab;
import com.djrapitops.plan.extension.annotation.TableProvider;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import com.djrapitops.plan.extension.icon.Icon;
import com.djrapitops.plan.extension.table.Table;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.dao.JobsDAOData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import net.crytec.libs.commons.utils.UtilMath;


@PluginInfo(color = Color.BROWN, name = "Berufe", iconName = "suitcase", iconFamily = Family.SOLID)
public class JobsData implements DataExtension {

  @Override
  public CallEvents[] callExtensionMethodsOn() {
    return new CallEvents[]{
        CallEvents.PLAYER_JOIN,
        CallEvents.PLAYER_LEAVE,
        CallEvents.SERVER_EXTENSION_REGISTER,
    };
  }


  @TableProvider(tableColor = Color.BROWN)
  @Tab(value = "Berufe")
  public Table getJobsOverview() {
    final List<JobsDAOData> allJobs = Jobs.getDBManager().getDB().getAllJobs()
        .values().stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    final Table.Factory table = Table.builder()
        .columnOne("Job", Icon.called("suitcase").build())
        .columnTwo("Arbeiter", Icon.called("users").build())
        .columnThree("Gesamtlevel", Icon.called("plus").build())
        .columnFour("Durchschnittliches Level", Icon.called("plus").build());

    if (allJobs.isEmpty()) {
      return table.build();
    }

    final Map<String, Integer> workers = new HashMap<>();
    final Map<String, Long> totals = new HashMap<>();
    for (final JobsDAOData data : allJobs) {
      final String job = data.getJobName();
      final int level = data.getLevel();
      workers.put(job, workers.getOrDefault(job, 0) + 1);
      totals.put(job, totals.getOrDefault(job, 0L) + level);
    }

    final List<String> order = new ArrayList<>(workers.keySet());
    Collections.sort(order);

    for (final String job : order) {
      final int amountOfWorkers = workers.getOrDefault(job, 0);
      final long totalLevel = totals.getOrDefault(job, 0L);
      table.addRow(
          job,
          amountOfWorkers,
          totalLevel,
          amountOfWorkers != 0 ? UtilMath.unsafeRound(totalLevel * 1.0 / amountOfWorkers, 2) : "-"

      );
    }

    return table.build();
  }

  @TableProvider(tableColor = Color.BROWN)
  @Tab(value = "Berufe")
  public Table getPlayerJobs(final UUID uuid) {
    final List<JobsDAOData> playersJobs = Jobs.getDBManager().getDB().getAllJobs(null, uuid);

    final Table.Factory table = Table.builder()
        .columnOne("Job", Icon.called("suitcase").build())
        .columnTwo("Level", Icon.called("users").build());

    for (final JobsDAOData job : playersJobs) {
      table.addRow(job.getJobName(), job.getLevel());
    }
    if (playersJobs.isEmpty()) {
      table.addRow("No Jobs");
    }
    return table.build();
  }
}