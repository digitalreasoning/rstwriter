package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The BulletList class represents an automated bullet list. In reStructuredText, automated bullet lists are possible
 * by starting each item with a "*".
 */
public class BulletList extends AutoList {

    /**
     * creates an bullet list from the parameter String: items are separated by '\n'.
     * @param str a String of list items separated by '\n'
     */
    public BulletList(String str){
        super(str, "*");
    }

    /**
     * Adds an item to this list, and processes it for inline markup
     * @param str the item to be added
     * @param inlines optional arguments specifying inline markup
     * @return this list with the item added
     */
    public BulletList addItem(String str, Inline... inlines){
        super.addItem(str, inlines);
        return this;
    }

    /**
     * Adds an item to this list
     * @param bodyElement the item to be added
     * @return this list with the item added
     */
    public BulletList addItem(RstBodyElement bodyElement){
        super.addItem(bodyElement);
        return this;
    }

    /**
     * Adds a sub-list given as a parameter; the sub-list is indented and added after the current item. Subsequent additions
     * to this list will be placed after the sub-list.
     * @param list the sub-list to be added
     * @return this list with the sub-list added
     */
    public BulletList addSubList(AutoList list){
        super.addSubList(list);
        return this;
    }

    /**
     * Adds a bullet sub-list created from the parameter; the sub-list is indented and added after the current item.
     * Subsequent additions to this list will be placed after the sub-list.
     * @param list the sub-list to be added, with items separated by the character '\n'
     * @return this list with the sub-list added
     */
    public BulletList addSubList(String list){
        super.addSubList(list);
        return this;
    }
}
