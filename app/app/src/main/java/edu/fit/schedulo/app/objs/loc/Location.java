package edu.fit.schedulo.app.objs.loc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Blank interface class that is used by the Event class to indicate
 * a location. Currently no common functionality between
 * <code>OnCampusLocation</code> and <code>OffCampusLocation</code>
 * because they're so different, but could be added later.
 *
 * @author Joshua Sheldon
 */
@JsonDeserialize(using = LocationDeserializer.class)
public interface Location {
}
