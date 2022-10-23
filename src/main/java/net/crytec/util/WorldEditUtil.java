/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.WorldEditUtil can not be copied and/or distributed without the express
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.extent.transform.BlockTransformExtent;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;

import net.crytec.AvarionCore;

public class WorldEditUtil {

	
	public static final JavaPlugin plugin = JavaPlugin.getPlugin(AvarionCore.class);
	

	@SuppressWarnings({"unused", "deprecation"})
	public static boolean restoreRegionBlocks(File rawFile, Region region, World pworld) {
		File file = null;
		ClipboardFormat format = null;
		for (ClipboardFormat formatOption : ClipboardFormats.getAll()) {
			for (String extension : formatOption.getFileExtensions()) {
				if (new File(rawFile.getAbsolutePath() + "." + extension).exists()) {
					file = new File(rawFile.getAbsolutePath() + "." + extension);
					format = formatOption;
				}
			}
		}
		if(file == null) {
			plugin.getLogger().info("Schematic file does not exist: " + rawFile.getAbsolutePath());
			return false;
		}

		com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(pworld);
		
		
		
		EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
		editSession.enableQueue();
		// Get the origin and size of the region

		BlockVector3 origin = BlockVector3.at(region.getMinimumPoint().getBlockX(), region.getMinimumPoint().getBlockY(), region.getMinimumPoint().getBlockZ());
		
		// Read the schematic and paste it into the world
		try(Closer closer = Closer.create()) {
			FileInputStream fis = closer.register(new FileInputStream(file));
			BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
			ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);
			if (clipboardFormat == null) {
				plugin.getLogger().warning("WorldEdit could not detect format type of the schematic file:" + file.getAbsolutePath() + ", try updating WorldEdit");
				return false;
			}
			ClipboardReader reader = clipboardFormat.getReader(bis);

			//WorldData worldData = world.getWorldData();
			LocalSession session = new LocalSession(WorldEdit.getInstance().getConfiguration());
			Clipboard clipboard = reader.read();

			clipboard.setOrigin(clipboard.getMinimumPoint());
			ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
			session.setBlockChangeLimit(-1);
			session.setClipboard(clipboardHolder);

			// Build operation
			BlockTransformExtent extent = new BlockTransformExtent(clipboardHolder.getClipboard(), clipboardHolder.getTransform());
			ForwardExtentCopy copy = new ForwardExtentCopy(extent, clipboard.getRegion(), clipboard.getOrigin(), editSession, origin);
			copy.setCopyingEntities(false);
			copy.setTransform(clipboardHolder.getTransform());
			Operations.completeLegacy(copy);
		} catch(MaxChangedBlocksException e) {
			plugin.getLogger().warning("exceeded the block limit while restoring schematic");
			return false;
		} catch(IOException e) {
			plugin.getLogger().warning("An error occured while restoring schematic");
			return false;
		} catch (Exception e) {
			plugin.getLogger().warning("crashed during restore");
			return false;
		}

		editSession.flushSession();
		return true;
	}

	
	public static boolean saveRegionBlocks(Region region, File file, World bworld) {
		ClipboardFormat format = ClipboardFormats.findByAlias("sponge");
		if(format == null) {
			// Sponge format does not exist, try to select another one
			for(ClipboardFormat otherFormat : ClipboardFormats.getAll()) {
				format = otherFormat;
			}
			if(format == null) {
				plugin.getLogger().warning("Cannot find a format to save a schematic in, no available formats!");
				return false;
			}
		}

		file = new File(file.getAbsolutePath() + "." + format.getPrimaryFileExtension());
		com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(bworld);
		
		EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);

		// Create a clipboard
		CuboidRegion selection = new CuboidRegion(world, region.getMinimumPoint(), region.getMaximumPoint());
		
		BlockArrayClipboard clipboard = new BlockArrayClipboard(selection);
		clipboard.setOrigin(region.getMinimumPoint());
		ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(world, region.getMinimumPoint(), region.getMaximumPoint()), clipboard, region.getMinimumPoint());
		try {
			Operations.completeLegacy(copy);
		} catch(MaxChangedBlocksException e) {
			plugin.getLogger().warning("Exceeded the block limit while saving schematic");
			return false;
		}

		try(Closer closer = Closer.create()) {
			FileOutputStream fos = closer.register(new FileOutputStream(file));
			BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
			ClipboardWriter writer = closer.register(format.getWriter(bos));
			writer.write(clipboard);
		} catch(IOException e) {
			plugin.getLogger().warning("An error occured while saving schematic, enable debug to see the complete stacktrace");
			return false;
		} catch (Exception e) {
			plugin.getLogger().warning("crashed during save");
			return false;
		}
		return true;
	}
}
