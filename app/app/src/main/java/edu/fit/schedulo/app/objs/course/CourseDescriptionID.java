package edu.fit.schedulo.app.objs.course;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;

import edu.fit.schedulo.app.objs.academic_year.AcademicYear;
import edu.fit.schedulo.app.objs.academic_year.AcademicYears;

/**
 * A 6 byte ID for a course description. The first 2 bytes
 * are the start year of the academic year, the next 2 bytes
 * are the course prefix, and the last 2 bytes are the course code.
 * Internally stores a short array of 3 elements, which allows
 * for fast equality comparison and hashing.
 * <br><br>
 * THIS CLASS ASSUMES 3 CHARACTER COURSE PREFIXES
 *
 * @author Joshua Sheldon
 */
@JsonSerialize(using = CourseDescriptionIDSerializer.class)
public class CourseDescriptionID {

    /* ---------- INSTANCE VARIABLES ---------- */

    /**
     * Where all the course description ID data is stored.
     * The first element is the start year of the academic year,
     * the second element is the course prefix, and the third
     * element is the course code.
     */
    private final short[] id;

    /* ---------- CONSTRUCTORS ---------- */

    /**
     * Non-coupled constructor for CourseDescriptionID.
     *
     * @param startYear The start year of the academic year.
     * @param prefix    The course prefix.
     * @param code      The course code.
     */
    @JsonCreator
    public CourseDescriptionID(@JsonProperty("startYear") short startYear,
                               @JsonProperty("prefix") String prefix,
                               @JsonProperty("code") short code) {
        this.id = new short[3];
        this.id[0] = startYear;
        this.id[1] = convertPrefixToShort(prefix);
        this.id[2] = code;
    }

    /**
     * Easy constructor by passing in a CourseDescription object.
     *
     * @param description The CourseDescription object to get the ID from.
     */
    public CourseDescriptionID(CourseDescription description) {

        this.id = new short[3];
        this.id[0] = description.getCatalogYear().getStartYear();
        this.id[1] = convertPrefixToShort(description.getPrefix());
        this.id[2] = description.getCode();

    }

    /* ---------- PUBLIC METHODS ---------- */

    /**
     * @return The course code of this course description
     * (i.e. the 1002 in "CSE 1002").
     */
    public short getCode() {
        return this.id[2];
    }

    /**
     * @return The start year of the academic year
     * of the catalog in which this course description
     * was published.
     */
    public short getStartYear() {
        return this.id[0];
    }

    /**
     * @return The course prefix, encoded as a short.
     */
    public short getPrefixAsShort() {
        return this.id[1];
    }

    /**
     * Decompresses the course prefix characters
     * from the stored prefix short.
     *
     * @return The course prefix as a String.
     */
    public String getPrefixAsString() {

        StringBuilder prefix = new StringBuilder(3);

        for (int i = 2; i >= 0; i--) {
            // Extract each 5-bit segment by right-shifting and then applying a mask
            // to isolate the last 5 bits (0x1F = 0001 1111 in binary, which isolates 5 bits).
            short letterIndex = (short) ((id[1] >> (i * 5)) & 0x1F);

            // Convert the index back to a character
            char letter = AlphabetMapping.getAlphabetLetter(letterIndex);

            // Append the letter to the prefix
            prefix.append(letter);

        }

        return prefix.toString();

    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CourseDescriptionID)) {
            return false;
        }

        CourseDescriptionID otherID = (CourseDescriptionID) other;

        return this.id[0] == otherID.id[0] &&
                this.id[1] == otherID.id[1] &&
                this.id[2] == otherID.id[2];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.id);
    }

    @NonNull
    @Override
    public String toString() {
        AcademicYear year = AcademicYears.getInstance().getAcademicYear(this.id[0]);
        return getPrefixAsString() + " " + this.id[2] + " (" + year + ")";
    }

    /* ---------- PRIVATE METHODS ---------- */

    /**
     * Packages the prefix string as a 2 byte short.
     *
     * @param prefix The prefix string to convert to a short.
     * @return The prefix string as a short.
     */
    private short convertPrefixToShort(String prefix) {

        // Assuming prefix is valid because of the
        // restrictions in the CourseDescription
        // constructor

        // The idea here is that each letter in the course prefix
        // only has 25 possibilities, which can be represented with
        // 5 bits (2^5 = 32). A short is 16 bits. So, we can represent
        // a course code with the following format:
        // 0[5 bits for first letter][5 bits for second letter][5 bits for third letter]

        // Example:
        // CSE, C = 2, S = 18, E = 4
        // binary(2) = 00010
        // binary(18) = 10010
        // binary(4) = 00100
        // So our short is: 0 00010 10010 00100 (16 bits)
        // 2,628 in decimal

        if (prefix.length() != 3) {
            throw new IllegalArgumentException("CourseDescription with prefix that is not 3 " +
                    "characters long got to CourseDescriptionID!");
        }

        char[] letters = prefix.toCharArray();
        short result = 0; // Initialize result to 0, first bit should be 0

        for (int i = 0; i < 3; i++) {

            short letterIndex = AlphabetMapping.getAlphabetIndex(letters[i]);

            // |= is doing a bitwise OR operations with result
            // and the value of the right hand side

            // << is a left shift operation, so we're taking the
            // binary value of letterIndex and shifting it to the
            // left by a certain amount of bits.

            // For the first letter, we shift it 10 bits to the left
            // (5 bits * 2)
            // For the second letter, we shift it 5 bits to the left
            // (5 bits * 1)
            // For the third letter, we don't shift it at all
            result |= (letterIndex << ((2 - i) * 5));

        }

        return result;

    }

}
