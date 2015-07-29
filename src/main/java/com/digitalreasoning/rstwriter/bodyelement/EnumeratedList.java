package com.digitalreasoning.rstwriter.bodyelement;

import java.util.List;

/**
 * The EnumeratedList class is a representation of automatically enumerated lists in reStructuredText. These include
 * alphabetic, numbered, and Roman numeral lists. To create an automatically enumerated list, all that's necessary is
 * to start the first item with an enumeration ("A.", "i.", "1.", etc.) and start the rest with "#.". If the first item
 * starts with "#.", a numbered list is automatically used.
 */
class EnumeratedList extends AutoList{
    /**
     * creates an automatically enumerated list with the specified starting character
     * @param str list of items separated by '\n' characters
     * @param start the starting enumeration's type (e.g. 'a', 'I', '1')
     */
    public EnumeratedList(String str, char start){
        super(str, "#.", start + ".");
    }

    /**
     * creates an automatically enumerated list with the specified elements and starting character
     * @param list the items to be added
     * @param start the starting enumeration's type (e.g. 'a', 'I', '1')
     */
    public EnumeratedList(List<String> list, char start){
        super(list, "#.", start + ".");
    }
}
