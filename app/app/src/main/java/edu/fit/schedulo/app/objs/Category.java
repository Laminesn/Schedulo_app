package edu.fit.schedulo.app.objs;

/**
 * Categories for events, or causes for a particular mood report.
 *
 * @author Joshua Sheldon
 */
public enum Category {
    ACADEMIC,
    HEALTH,
    OTHER,
    PERSONAL,
    SOCIAL,
    SPIRITUAL,
    WORK;

    @Override
    public String toString() {
        switch (this) {
            case ACADEMIC:
                return "Academic";
            case HEALTH:
                return "Health";
            case OTHER:
                return "Other";
            case PERSONAL:
                return "Personal";
            case SOCIAL:
                return "Social";
            case SPIRITUAL:
                return "Spiritual";
            case WORK:
                return "Work";
            default:
                return "Unknown";
        }
    }

}
