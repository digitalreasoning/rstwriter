package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The AlphabeticList class represents an automatically enumerated alphabetic list. In reStructuredText, automatically
 * enumerated lists are possible by defining the first enumeration and in each following item using "#." in lieu of
 * manual enumerations. This class takes advantage of this and defines the first letter and follows with the automatic
 * symbol for the rest.
 */
public class AlphabeticList extends EnumeratedList{

    /**
     * creates an alphabetic list from the parameter String: items are separated by '\n'. By default, capital letters will
     * be used.
     * @param str a String of list items separated by '\n'
     */
    public AlphabeticList(String str){
        super(str, 'A');
    }

    /**
     * creates an alphabetic list from the parameter String: items are separated by '\n'. The option of capital letters or
     * lowercase letters is provided as well.
     * @param str a String of list items separated by '\n'
     * @param uppercase true for capital letters as enumerations, false for lowercase letters
     */
    public AlphabeticList(String str, boolean uppercase){
        super(str, uppercase ? 'A' : 'a');
    }

    /**
     * Adds an item to this list, and processes it for inline markup
     * @param str the item to be added
     * @param inlines optional arguments specifying inline markup
     * @return this list with the item added
     */
    public AlphabeticList addItem(String str, Inline... inlines){
        super.addItem(str, inlines);
        return this;
    }

    /**
     * Adds an item to this list
     * @param bodyElement the item to be added
     * @return this list with the item added
     */
    public AlphabeticList addItem(RstBodyElement bodyElement){
        super.addItem(bodyElement);
        return this;
    }

    /**
     * Adds a sub-list given as a parameter; the sub-list is indented and added after the current item. Subsequent additions
     * to this list will be placed after the sub-list.
     * @param list the sub-list to be added
     * @return this list with the sub-list added
     */
    public AlphabeticList addSubList(AutoList list){
        super.addSubList(list);
        return this;
    }

    /**
     * Adds an alphabetic sub-list created from the parameter; the sub-list is indented and added after the current item.
     * Subsequent additions to this list will be placed after the sub-list.
     * @param list the sub-list to be added, with items separated by the character '\n'
     * @return this list with the sub-list added
     */
    public AlphabeticList addSubList(String list){
        super.addSubList(list);
        return this;
    }
}