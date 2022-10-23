/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.UtilManager can not be copied and/or distributed without the express
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

import lombok.Getter;
import net.crytec.AvarionCore;
import net.crytec.inventoryapi.InventoryAPI;
import net.crytec.libs.commons.utils.language.Language;
import net.crytec.libs.commons.utils.language.LanguageAPI;
import net.crytec.libs.protocol.ProtocolAPI;
import net.crytec.libs.protocol.npc.NpcAPI;
import net.crytec.libs.protocol.scoreboard.ScoreboardAPI;
import net.crytec.listener.WorldGuardRegionListener;

public class UtilManager {

  @Getter
  private final ScoreboardAPI scoreboardAPI;
  @Getter
  private final TaskManager taskManager;
  @Getter
  private final ProtocolAPI protocol;
  @Getter
  private final NpcAPI npcAPI;

  public UtilManager(final AvarionCore plugin) {

    new LanguageAPI(plugin, Language.DE_DE);
    new InventoryAPI(plugin);
    new WorldGuardRegionListener(plugin);
    taskManager = new TaskManager(plugin);
    scoreboardAPI = new ScoreboardAPI(plugin, false);
    protocol = new ProtocolAPI(plugin);
    npcAPI = new NpcAPI(plugin);

    new PlayerChatInput(plugin);
  }
}
