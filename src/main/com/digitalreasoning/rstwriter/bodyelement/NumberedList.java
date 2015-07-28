package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The NumberedList class represents an automatically enumerated numbered list. In reStructuredText, automatically
 * enumerated lists are possible by defining the first enumeration and in each following item using "#." in lieu of
 * manual enumerations. This class takes advantage of reStructuredText's default behavior and defines all items with
 * "#.", resulting in a numbered list.
 */
public class NumberedList extends EnumeratedList {

    /**
     * creates a numbered list from the parameter String: items are separated by '\n'.
     * @param str a String of list items separated by '\n'
     */
    public NumberedList(String str){
        super(str, '#');
    }

    /**
     * Adds an item to this list, and processes it for inline markup
     * @param str the item to be added
     * @param inlines optional arguments specifying inline markup
     * @return this list with the item added
     */
    public NumberedList addItem(String str, Inline... inlines){
        super.addItem(str, inlines);
        return this;
    }

    /**
     * Adds an item to this list
     * @param bodyElement the item to be added
     * @return this list with the item added
     */
    public NumberedList addItem(RstBodyElement bodyElement){
        super.addItem(bodyElement);
        return this;
    }

    /**
     * Adds a sub-list given as a parameter; the sub-list is indented and added after the current item. Subsequent additions
     * to this list will be placed after the sub-list.
     * @param list the sub-list to be added
     * @return this list with the sub-list added
     */
    public NumberedList addSubList(AutoList list){
        super.addSubList(list);
        return this;
    }

    /**
     * Adds a numbered sub-list created from the parameter; the sub-list is indented and added after the current item.
     * Subsequent additions to this list will be placed after the sub-list.
     * @param list the sub-list to be added, with items separated by the character '\n'
     * @return this list with the sub-list added
     */
    public NumberedList addSubList(String list){
        super.addSubList(list);
        return this;
    }
}
