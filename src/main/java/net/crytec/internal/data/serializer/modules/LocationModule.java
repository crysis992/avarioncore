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
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationModule extends SimpleModule {


  public LocationModule() {
    super(PackageVersion.VERSION);
    addSerializer(Location.class, new LocationSerializer());
    addDeserializer(Location.class, new LocationDeserializer());
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


  public static class LocationSerializer extends StdSerializer<Location> {

    public LocationSerializer() {
      this(null);
    }

    protected LocationSerializer(final Class<Location> t) {
      super(t);
    }

    @Override
    public void serialize(final Location location, final JsonGenerator jsonGenerator, final SerializerProvider serializer) throws IOException {

      jsonGenerator.writeStartObject();

      final Location center = location.toCenterLocation();

      jsonGenerator.writeNumberField("x", center.getBlockX());
      jsonGenerator.writeNumberField("y", center.getBlockY());
      jsonGenerator.writeNumberField("z", center.getBlockZ());

      jsonGenerator.writeNumberField("pitch", center.getPitch());
      jsonGenerator.writeNumberField("yaw", center.getYaw());

      jsonGenerator.writeStringField("world", center.getWorld().getName());

      jsonGenerator.writeEndObject();

    }
  }

  public static class LocationDeserializer extends StdDeserializer<Location> {

    public LocationDeserializer() {
      this(null);
    }

    protected LocationDeserializer(final Class<?> vc) {
      super(vc);
    }

    @Override
    public Location deserialize(final JsonParser parser, final DeserializationContext deserializer) throws IOException, JsonProcessingException {

      final ObjectCodec codec = parser.getCodec();
      final JsonNode node = codec.readTree(parser);

      final World world = Bukkit.getWorld(node.get("world").asText());

      if (world == null) {
        deserializer.reportInputMismatch(World.class, "Failed to deserialize object - World is missing");
        return getNullValue(deserializer);
      }

      return new Location(world, node.get("x").asInt(), node.get("y").asInt(), node.get("z").asInt(), (float) node.get("yaw").asDouble(0), (float) node.get("pitch").asDouble(0));
    }
  }
}
