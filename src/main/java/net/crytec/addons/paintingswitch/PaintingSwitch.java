/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.paintingswitch.PaintingSwitch can not be copied and/or distributed without the express
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

package net.crytec.addons.paintingswitch;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.crytec.addons.Addon;
import net.crytec.util.F;
import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BlockIterator;

public class PaintingSwitch extends Addon implements Listener {


	@Override
	protected void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, getPlugin());
	}
	
	@Override
	protected void onDisable() {
	}

	@Override
	public String getModuleName() {
		return "PaintingSwitch";
	}

	private boolean canModifyPainting(final Player player, final Entity e) {
		final RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		final LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
		return query.testBuild(BukkitAdapter.adapt(e.getLocation()), lp, Flags.BLOCK_PLACE);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onHangingPlace(final HangingPlaceEvent event) {
		if (event.isCancelled()) {
			return;
		}
		final Player player = event.getPlayer();
		final PaintingData settings = PaintingData.getSettings(player);
		if ((settings.previousPainting != null) && ((event.getEntity() instanceof Painting))) {
			final Painting painting = (Painting) event.getEntity();
			if (!painting.setArt(settings.previousPainting.getArt())) {
				final Art[] art = Art.values();
				int count = new Random().nextInt(Art.values().length - 1);
				final int tempCount = count;
				count--;
				if (count == -1) {
					count = 0;
				}
				while (!painting.setArt(art[count])) {
					if (count == 0) {
						count = art.length - 1;
					} else {
						count--;
					}
					if (count == tempCount) {
						break;
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}
		final Entity entity = event.getRightClicked();
		if (entity instanceof Painting) {
			final Player player = event.getPlayer();
			if (canModifyPainting(player, entity)) {

				final Set<Map.Entry<String, PaintingData>> keys = PaintingData.playerSettings.entrySet();

				for (final Map.Entry<String, PaintingData> set : keys) {
					final String playerName = (String) set.getKey();
					if ((PaintingData.playerSettings.get(playerName).painting != null) && (PaintingData.playerSettings.get(playerName).painting.getEntityId() == entity.getEntityId()) && (!playerName.equals(player.getName()))) {
						player.sendMessage(F.error(F.name(playerName) + " bearbeitet dieses Bild grade."));
						return;
					}
				}
				final PaintingData settings = PaintingData.getSettings(player);

				settings.block = getTargetBlock(player, null, 20);
				settings.painting = ((Painting) entity);
				settings.location = player.getLocation();
				if (settings.clicked) {
					player.sendMessage(F.main("Painting", "Bild wurde gesetzt."));
					PaintingData.clear(player);
				} else {
					player.sendMessage(F.main("Painting", "Scrolle mit deinem Mausrad um das Bild zu ändern."));
					settings.clicked = true;
				}
			} else {
				player.sendMessage(F.main("Painting", "Dieses Bild ist gesichert!"));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(final PlayerMoveEvent event) {
		if (event.isCancelled()) {
			return;
		}
		final Player player = event.getPlayer();
		final PaintingData settings = PaintingData.getSettings(player);
		try {
			if ((settings.block != null) && (settings.location != null) && (settings.clicked) && (hasPlayerMovedSignificantly(event))) {
				player.sendMessage(F.main("Painting", "Bild wurde gesetzt."));
				PaintingData.clear(player);
			}
		} catch (final Exception ignored) {
		}
	}

	private boolean hasPlayerMovedSignificantly(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final PaintingData settings = PaintingData.getSettings(player);
		int oldPlayerPosX = Math.abs(settings.location.getBlockX() + 100);
		int oldPlayerPosY = Math.abs(settings.location.getBlockY() + 100);
		int oldPlayerPosZ = Math.abs(settings.location.getBlockZ() + 100);
		int newPlayerPosX = Math.abs(event.getTo().getBlockX() + 100);
		int newPlayerPosY = Math.abs(event.getTo().getBlockY() + 100);
		int newPlayerPosZ = Math.abs(event.getTo().getBlockZ() + 100);
		if (oldPlayerPosX < newPlayerPosX) {
			final int temp = oldPlayerPosX;
			oldPlayerPosX = newPlayerPosX;
			newPlayerPosX = temp;
		}
		if (oldPlayerPosY < newPlayerPosY) {
			final int temp = oldPlayerPosY;
			oldPlayerPosY = newPlayerPosY;
			newPlayerPosY = temp;
		}
		if (oldPlayerPosZ < newPlayerPosZ) {
			final int temp = oldPlayerPosZ;
			oldPlayerPosZ = newPlayerPosZ;
			newPlayerPosZ = temp;
		}
		final int oldPlayerYaw = (int) Math.abs(settings.location.getYaw());
		final int newPlayerYaw = (int) Math.abs(player.getLocation().getYaw());
		final int oldPlayerPitch = (int) settings.location.getPitch();
		final int newPlayerPitch = (int) player.getLocation().getPitch();
		if ((hasYawChangedSignificantly(oldPlayerYaw, newPlayerYaw)) || (hasPitchChangedSignificantly(oldPlayerPitch, newPlayerPitch))) {
			if (!settings.block.equals(getTargetBlock(player, null, 15))) {
				return true;
			}
		}
		if (((newPlayerYaw <= 315) && (newPlayerYaw >= 225)) || ((newPlayerYaw <= 135) && (newPlayerYaw >= 45) && ((oldPlayerPosX % newPlayerPosX > 7) || (oldPlayerPosY % newPlayerPosY > 2) || (oldPlayerPosZ % newPlayerPosZ > 2)))) {
			if (!settings.block.equals(getTargetBlock(player, null, 15))) {
				return true;
			}
		}
		if (((newPlayerYaw < 45) || (newPlayerYaw > 315) || ((newPlayerYaw < 225) && (newPlayerYaw > 135))) && ((oldPlayerPosX % newPlayerPosX > 2) || (oldPlayerPosY % newPlayerPosY > 2) || (oldPlayerPosZ % newPlayerPosZ > 7))) {
			return !settings.block.equals(getTargetBlock(player, null, 15));
		}
		return false;
	}

	private Block getTargetBlock(final LivingEntity entity, HashSet<Material> transparent, final int maxDistance) {
		Block target = entity.getEyeLocation().getBlock();
		final Location eyeLoc = entity.getEyeLocation();
		if (transparent == null) {
			transparent = new HashSet<>();
			transparent.add(Material.AIR);
		}
		try {
			final BlockIterator lineOfSight = new BlockIterator(entity.getWorld(), eyeLoc.toVector(), entity.getLocation().getDirection(), 0.0D, maxDistance);
			while (lineOfSight.hasNext()) {
				final Block toTest = lineOfSight.next();
				if (!transparent.contains(toTest.getType())) {
					return target;
				}
				target = toTest;
			}
		} catch (final Exception ignored) {
		}
		return target;
	}

	private boolean hasPitchChangedSignificantly(int oldPlayerPitch, int newPlayerPitch) {
		if (oldPlayerPitch < newPlayerPitch) {
			final int temp = oldPlayerPitch;
			oldPlayerPitch = newPlayerPitch;
			newPlayerPitch = temp;
		}
		return oldPlayerPitch - newPlayerPitch > 30;
	}

	private boolean hasYawChangedSignificantly(int oldYaw, int newYaw) {
		oldYaw = Math.abs(oldYaw) + 360;
		newYaw = Math.abs(newYaw) + 360;
		if (oldYaw < newYaw) {
			final int temp = oldYaw;
			oldYaw = newYaw;
			newYaw = temp;
		}
		return oldYaw % newYaw > 30;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemHeldChange(final PlayerItemHeldEvent event) {
		final Player player = event.getPlayer();
		final PaintingData settings = PaintingData.getSettings(player);
		final int previousSlot = event.getPreviousSlot();
		final int newSlot = event.getNewSlot();
		boolean reverse = previousSlot - newSlot > 0;
		if (((previousSlot == 0) && (newSlot == 8)) || ((previousSlot == 8) && (newSlot == 0))) {
			reverse = !reverse;
		}
		if ((settings.clicked) && (settings.painting != null) && (settings.block != null) && (!reverse)) {
			final Painting painting = settings.painting;
			final Art[] art = Art.values();
			final int currentID = painting.getArt().ordinal();
			if (currentID == art.length - 1) {
				int count = 0;
				while (!painting.setArt(art[count])) {
					if (count == art.length - 1) {
						break;
					}
					count++;
				}
			} else {
				int count = painting.getArt().ordinal();
				final int tempCount = count;
				count++;
				while (!painting.setArt(art[count])) {
					if (count == art.length - 1) {
						count = 0;
					} else {
						count++;
					}
					if (count == tempCount) {
						break;
					}
				}
			}
			settings.previousPainting = painting;
		} else if ((settings.clicked) && (settings.painting != null) && (settings.block != null) && (reverse)) {
			final Painting painting = settings.painting;
			final Art[] art = Art.values();
			final int currentID = painting.getArt().ordinal();
			if (currentID == 0) {
				int count = art.length - 1;
				while (!painting.setArt(art[count])) {
					count--;
					if (count == 0) {
						break;
					}
				}
			} else {
				int count = painting.getArt().ordinal();
				final int tempCount = count;
				count--;
				while (!painting.setArt(art[count])) {
					if (count == 0) {
						count = art.length - 1;
					} else {
						count--;
					}
					if (count == tempCount) {
						break;
					}
				}
			}
			settings.previousPainting = painting;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPaintingBreak(final HangingBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		final Set<Map.Entry<String, PaintingData>> keys = PaintingData.playerSettings.entrySet();
		final Player player;
		if ((event instanceof HangingBreakByEntityEvent)) {
			final HangingBreakByEntityEvent entityBreakEvent = (HangingBreakByEntityEvent) event;
			if ((entityBreakEvent.getRemover() instanceof Player)) {
				player = (Player) entityBreakEvent.getRemover();
				final PaintingData settings = PaintingData.getSettings(player);
				if ((settings.painting != null) && (settings.painting.getEntityId() == event.getEntity().getEntityId())) {
					PaintingData.clear(player);
				} else {
					for (final Map.Entry<String, PaintingData> set : keys) {
						final String playerName = (String) set.getKey();
						if ((PaintingData.playerSettings.get(playerName).painting != null) && (PaintingData.playerSettings.get(playerName).painting.getEntityId() == event.getEntity().getEntityId())
									&& (!playerName.equals(player.getName()))) {
							player.sendMessage(F.error("Dieses Bild wird grade von " + F.name((String) set.getKey()) + " bearbeitet."));
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		} else {
			for (final Map.Entry<String, PaintingData> set : keys) {
				final String playerName = (String) set.getKey();
				if ((PaintingData.playerSettings.get(playerName).painting != null) && (PaintingData.playerSettings.get(playerName).painting.getEntityId() == event.getEntity().getEntityId())) {
					PaintingData.clear(playerName);
					return;
				}
			}
		}
	}

}
