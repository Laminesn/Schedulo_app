package edu.fit.schedulo.app.objs.course;

import org.apache.commons.collections4.bidimap.TreeBidiMap;

/**
 * Provides a map between the character object for each uppercase
 * letter of the alphabet and the index of that letter in the
 * alphabet. The index is 0-based, so 'A' is 0, 'B' is 1, etc.
 *
 * @author Joshua Sheldon
 */
public class AlphabetMapping {

    /**
     * The map itself, eagerly initialized.
     */
    private static final TreeBidiMap<Character, Short> alphabetMap = generateAlphabetMap();

    /**
     * The value returned by <code>getAlphabetIndex</code> if a
     * capital letter of the English alphabet is not in the
     * alphabet map for some reason.
     */
    public static final short ERROR_INDEX = 26;

    /**
     * @return The map between uppercase letters of the alphabet
     * and their 0-based index in the alphabet.
     */
    private static TreeBidiMap<Character, Short> generateAlphabetMap() {

        TreeBidiMap<Character, Short> map = new TreeBidiMap<>();

        for (short i = 0; i < 26; i++) {
            map.put((char) ('A' + i), i);
        }

        return map;

    }

    /**
     * Gets the 0-based index of the given letter in the alphabet.
     * If the letter is not a capital letter of the English alphabet,
     * an <code>IllegalArgumentException</code> is thrown.
     *
     * @param letter The letter to get the index of.
     * @return The 0-based index of the letter in the alphabet,
     * or <code>ERROR_INDEX</code> if for some reason a
     * valid letter is not in the map.
     */
    public static short getAlphabetIndex(char letter) {

        if (letter < 'A' || letter > 'Z') {
            throw new IllegalArgumentException("Letter must be between 'A' and 'Z'.");
        }

        Short letterIndex = alphabetMap.get(letter);

        if (letterIndex == null) {
            return ERROR_INDEX;
        } else {
            return letterIndex;
        }

    }

    /**
     * Gets the letter of the alphabet at the given 0-based index.
     * If the index is not between 0 and 25, an
     * <code>IllegalArgumentException</code> is thrown.
     *
     * @param index The 0-based index of the letter to get.
     * @return The letter of the alphabet at the given index,
     * or '?' if the index is the <code>ERROR_INDEX</code>.
     */
    public static char getAlphabetLetter(short index) {

        if ((index < 0 || index > 25) && index != ERROR_INDEX) {
            throw new IllegalArgumentException("Index must be between 0 and 25.");
        }

        if (index != ERROR_INDEX) {
            return alphabetMap.getKey(index);
        } else {
            return '?';
        }

    }

}
