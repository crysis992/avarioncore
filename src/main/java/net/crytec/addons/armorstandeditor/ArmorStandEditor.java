/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.armorstandeditor.ArmorStandEditor can not be copied and/or distributed without the express
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

package net.crytec.addons.armorstandeditor;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.UUID;
import net.crytec.addons.Addon;
import net.crytec.addons.armorstandeditor.data.ArmorStandAxis;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.libs.commons.utils.UtilPlayer;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.RayTraceResult;

public class ArmorStandEditor extends Addon implements  Listener {

	private HashMap<UUID, ArmorStandData> data;
	
	@Override
	protected void onEnable() {
		data = Maps.newHashMap();
		getPlugin().getCommandManager().registerCommand(new ArmorStandEditorCommand(this));
		
		Bukkit.getPluginManager().registerEvents(this, getPlugin());
		Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), () -> {
			
			for (final ArmorStandData data : data.values()) {

				final String msg = "§b>> §8§lArmorstand Editor §b<< "
						+ "§b§lAchse: " + data.getAxis().getDisplayname()
						+ " §b§lBearbeite: " + data.getMode().getDisplayname();
				data.getPlayer().sendActionBar(msg);
			}
			
		}, 20L, 25L);
		
	}

	@Override
	protected void onDisable() {
		
	}

	@Override
	public String getModuleName() {
		return "ArmorStandEditor";
	}

	public boolean isInEditor(final Player player) {
		return data.containsKey(player.getUniqueId());
	}
	
	public ArmorStandData getEditor(final Player player) {
		return data.get(player.getUniqueId());
	}
	
	public ArmorStandData removeEditor(final Player player) {
		return data.remove(player.getUniqueId());
	}
	
	public boolean link(final Player player) {
		final RayTraceResult result = player.getWorld().rayTraceEntities(player.getLocation(), player.getLocation().getDirection(), 2, 8, e -> (e instanceof ArmorStand) );
		
		if (result == null || result.getHitEntity() == null) {
			player.sendMessage(F.error("Du musst einen Armorstand anschauen."));
			return false;
		}

		data.put(player.getUniqueId(), new ArmorStandData(player, (ArmorStand) result.getHitEntity()));
		player.sendMessage(F.main("Armorstand", "Du bearbeitest nun den angegebenen Armorstand. Nutze " + F.name("/ase end") + " um den Editor zu verlassen."));
		return true;
	}
	
	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		if (data.containsKey(event.getPlayer().getUniqueId())) {
			data.remove(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void swapAxis(final PlayerAnimationEvent event) {
		if (!data.containsKey(event.getPlayer().getUniqueId())) return;
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && event.getPlayer().isSneaking()) {
			final ArmorStandData data = this.data.get(event.getPlayer().getUniqueId());
			data.setAxis(getNextEnum(data.getAxis()));
			event.getPlayer().sendTitle("", "§7" + data.getAxis().getDisplayname(), 0, 30, 0);
			UtilPlayer.playSound(event.getPlayer(), Sound.BLOCK_COMPARATOR_CLICK, 0.5F, 0.45F);
		}
	}
	
	@EventHandler
	public void onInteract(final EntityDamageByEntityEvent event) {
		
		if (!(event.getDamager() instanceof Player) && !(event.getEntity() instanceof ArmorStand)) return;
		
		final Player player = (Player) event.getDamager();
		
		if (!data.containsKey(player.getUniqueId())) return;
		
		SmartInventory.builder().provider(new ArmorStandGUI(data.get(player.getUniqueId()))).title("Armorstand Edtior").size(6).build().open(player);
		event.setCancelled(true);
	}
	
	private ArmorStandAxis getNextEnum(final ArmorStandAxis e) {
		  final int index = e.ordinal();
		  int nextIndex = index + 1;
		  final ArmorStandAxis[] axis = ArmorStandAxis.values();
		  nextIndex %= axis.length;
		  return axis[nextIndex];
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemHeldChange(final PlayerItemHeldEvent event) {
		final Player player = event.getPlayer();
		if (!data.containsKey(player.getUniqueId())) return;
		
		final ArmorStandData data = this.data.get(player.getUniqueId());
		
		final int previousSlot = event.getPreviousSlot();
		final int newSlot = event.getNewSlot();
		boolean reverse = previousSlot - newSlot > 0;
		if (((previousSlot == 0) && (newSlot == 8)) || ((previousSlot == 8) && (newSlot == 0))) {
			reverse = !reverse;
		}
		
		if (!reverse) {
			data.editPosition(true);
		} else {
			data.editPosition(false);
		}
	}
}