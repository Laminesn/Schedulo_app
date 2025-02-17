package edu.fit.schedulo.app.objs.mood;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.fit.schedulo.app.objs.Category;

/**
 * @author Zion Taylor
 * Modified by Joshua Sheldon
 */
public class MoodReport {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * The mood scale of the user.
     */
    private final short feelingScale;

    /**
     * The source of the mood value.
     */
    private final Category feelingSource;

    /**
     * The events that occurred to why user feels that way.
     */
    private final String journalEntry;

    /* ---------- CONSTRUCTORS ---------- */

    /**
     * Constructor for a mood report with all parameters.
     *
     * @param feelingScale  The mood scale of the user.
     * @param feelingSource The source of the mood value.
     * @param journalEntry  The events that occurred to why user feels that way.
     */
    @JsonCreator
    public MoodReport(@JsonProperty("feelingScale") short feelingScale,
                      @JsonProperty("feelingSource") Category feelingSource,
                      @JsonProperty("journalEntry") String journalEntry) {

        if (feelingScale < 0 || feelingScale > 10) {
            throw new IllegalArgumentException("Feeling scale must be between 0 and 10.");
        }

        if (feelingSource == null) {
            throw new IllegalArgumentException("Feeling source cannot be null.");
        }

        if (journalEntry == null || journalEntry.isEmpty()) {
            throw new IllegalArgumentException("Journal entry cannot be null or empty.");
        }

        this.feelingScale = feelingScale;
        this.feelingSource = feelingSource;
        this.journalEntry = journalEntry;
    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The mood scale of the user.
     */
    public short getFeelingScale() {
        return feelingScale;
    }

    /**
     * @return The source of the mood value.
     */
    public Category getFeelingSource() {
        return feelingSource;
    }

    /**
     * @return The events that occurred to why user feels that way.
     */
    public String getJournalEntry() {
        return journalEntry;
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof MoodReport)) {
            return false;
        }

        MoodReport otherReport = (MoodReport) other;

        return this.feelingScale == otherReport.getFeelingScale()
                && this.feelingSource.equals(otherReport.getFeelingSource())
                && this.journalEntry.equals(otherReport.getJournalEntry());

    }

}


