/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.internal.data.serializer.modules.LocationModule can not be copied and/or distributed without the express
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

package net.crytec.internal.data.serializer.modules;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.IOException;
import net.minecraft.server.v1_15_R1.MojangsonParser;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemStackModule extends SimpleModule {


  public ItemStackModule() {
    super(PackageVersion.VERSION);
    addSerializer(ItemStack.class, new ItemStackSerializer());
    addDeserializer(ItemStack.class, new ItemStackDeserializer());
  }

  @Override
  public void setupModule(final SetupContext context) {
    super.setupModule(context);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public boolean equals(final Object o) {
    return this == o;
  }


  public static class ItemStackSerializer extends StdSerializer<ItemStack> {

    public ItemStackSerializer() {
      this(null);
    }

    protected ItemStackSerializer(final Class<ItemStack> t) {
      super(t);
    }

    @Override
    public void serialize(final ItemStack item, final JsonGenerator gen, final SerializerProvider serializer) throws IOException {
      gen.writeStartObject();

      if (item == null) {
        gen.writeStringField("itemdata", "empty");
      } else {
        final net.minecraft.server.v1_15_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        final NBTTagCompound tag = new NBTTagCompound();
        nms.save(tag);
        gen.writeStringField("itemdata", tag.toString());
      }
      gen.writeEndObject();
    }
  }

  public static class ItemStackDeserializer extends StdDeserializer<ItemStack> {

    public ItemStackDeserializer() {
      this(null);
    }


    protected ItemStackDeserializer(final Class<?> vc) {
      super(vc);
    }

    @Override
    public ItemStack deserialize(final JsonParser parser, final DeserializationContext deserializer) throws IOException, JsonProcessingException {

      final ObjectCodec codec = parser.getCodec();
      final JsonNode node = codec.readTree(parser);

      final JsonNode itemString = node.get("itemdata");

      final String data = itemString.asText();

      if (data == null || data.isEmpty()) {
        return new ItemStack(Material.AIR);
      }
      try {
        final NBTTagCompound comp = MojangsonParser.parse(data);
        final net.minecraft.server.v1_15_R1.ItemStack cis = net.minecraft.server.v1_15_R1.ItemStack.a(comp);
        return CraftItemStack.asBukkitCopy(cis);
      } catch (final CommandSyntaxException ex) {
        ex.printStackTrace();
        return new ItemStack(Material.AIR);
      }
    }
  }
}
