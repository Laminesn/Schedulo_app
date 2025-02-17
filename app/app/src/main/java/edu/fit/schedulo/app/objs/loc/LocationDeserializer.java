package edu.fit.schedulo.app.objs.loc;

import android.location.Address;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Locale;

/**
 * Deserializer for the <code>Location</code> interface.
 *
 * @author Joshua Sheldon
 */
public class LocationDeserializer extends StdDeserializer<Location> {

    public LocationDeserializer() {
        super(Location.class);
    }

    @Override
    public Location deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectNode root = jsonParser.getCodec().readTree(jsonParser);
        Class<? extends Location> locationType = getLocationType(root);

        if (locationType == null) {
            throw new IOException("Unable to determine location type from JSON");
        }

        if (locationType.equals(OnCampusLocation.class)) {
            return deserializeOnCampusLocation(root);
        } else if (locationType.equals(VirtualLocation.class)) {
            return deserializeVirtualLocation(root);
        } else if (locationType.equals(OffCampusLocation.class)) {
            return deserializeOffCampusLocation(root);
        }

        throw new IOException("Unsupported location type");
    }

    private OnCampusLocation deserializeOnCampusLocation(ObjectNode node) {
        // Parse the required fields from the node
        String bldgCode = node.get("bldgCode").asText();
        short roomNumber = (short) node.get("roomNumber").asInt();
        return new OnCampusLocation(bldgCode, roomNumber);
    }

    private VirtualLocation deserializeVirtualLocation(ObjectNode node) {
        String link = node.get("link").asText();
        return new VirtualLocation(link);
    }

    private OffCampusLocation deserializeOffCampusLocation(ObjectNode node) {
        // TODO STUB
        return new OffCampusLocation(new Address(Locale.ENGLISH));
    }

    /**
     * Determines the type of location from the JSON object fields.
     *
     * @param locationJSON The JSON object to determine the type of.
     * @return The class of the location type, or <code>null</code>
     * if the type could not be determined.
     */
    private static Class<? extends Location> getLocationType(ObjectNode locationJSON) {
        if (locationJSON.has("bldgCode")) {
            return OnCampusLocation.class;
        } else if (locationJSON.has("link")) {
            return VirtualLocation.class;
        } else if (locationJSON.has("address")) {
            return OffCampusLocation.class;
        }
        return null;
    }

}
