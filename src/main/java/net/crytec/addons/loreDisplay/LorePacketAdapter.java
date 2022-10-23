/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.loreDisplay.LorePacketAdapter can not be copied and/or distributed without the express
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

package net.crytec.addons.loreDisplay;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.tr7zw.changeme.nbtapi.NBTItem;
import java.util.List;
import java.util.Optional;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.inventory.meta.ItemMeta;

public class LorePacketAdapter extends PacketAdapter {

  private final static String NBT_KEY = "descriptionID";
  private final LoreDisplayAddon addon;

  public LorePacketAdapter(final LoreDisplayAddon addon) {
    super(addon.getPlugin(), PacketType.Play.Server.SET_SLOT, /* PacketType.Play.Server.WINDOW_ITEMS,*/ PacketType.Play.Client.SET_CREATIVE_SLOT);
    this.addon = addon;
  }

  @Override
  public void onPacketReceiving(final PacketEvent event) {
    if (event.getPacketType() == PacketType.Play.Client.SET_CREATIVE_SLOT) {
      final PacketContainer packet = event.getPacket();

      packet.getItemModifier().modify(0, (item) -> {
        final NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(NBT_KEY)) {
          return item;
        }

        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(null);
        meta.setLore(null);
        item.setItemMeta(meta);
        return item;
      });
      event.setPacket(packet);
    }
  }

  @Override
  public void onPacketSending(final PacketEvent event) {
    if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
      final PacketContainer packet = event.getPacket();
      packet.getItemModifier().modify(0, (item) -> {
        final NBTItem nbt = new NBTItem(item);

        if (!nbt.hasKey(NBT_KEY)) {
          return item;
        }

        final Optional<Pair<String, List<String>>> optinfo = addon.getInfo(nbt.getString(NBT_KEY));
        if (!optinfo.isPresent()) {
          return item;
        }

        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(optinfo.get().getLeft());
        meta.setLore(optinfo.get().getRight());

        item.setItemMeta(meta);
        return item;
      });
      event.setPacket(packet);
    }

//		if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
//			System.out.println("WINDOW ITEMS PACKET SENT");
//			PacketContainer packet = event.getPacket().deepClone();
//			List<ItemStack> items = packet.getItemListModifier().read(0);
//			
//			items.forEach(i -> System.out.println(i.getType()));
//			
//			Iterator<ItemStack> iter = items.iterator();
//			
//			while (iter.hasNext()) {
//				ItemStack item = iter.next();
//				if (item == null || item.getType() == Material.AIR) continue;
//				NBTItem nbt = new NBTItem(item);
//				
//				if (!nbt.hasKey(NBT_KEY)) continue;
//				
//				Optional<Pair<String, List<String>>> optinfo = addon.getInfo(nbt.getString(NBT_KEY));
//				
//				if (!optinfo.isPresent()) continue;
//				
//				ItemMeta meta = item.getItemMeta();
//				
//				meta.setDisplayName(optinfo.get().getLeft());
//				meta.setLore(optinfo.get().getRight());
//				
//				item.setItemMeta(meta);
//			}
//			
//			packet.getItemListModifier().write(0, items);
//			event.setPacket(packet);
//		}
  }

}
