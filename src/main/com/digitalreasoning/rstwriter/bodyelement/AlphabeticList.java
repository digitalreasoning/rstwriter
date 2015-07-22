package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;

/**
 * Created by creynolds on 7/16/15.
 */
public class AlphabeticList extends EnumeratedList{
    public AlphabeticList(String str){ super(str, 'A'); }
    public AlphabeticList(String str, boolean uppercase){ super(str, uppercase ? 'A' : 'a'); }

    public AlphabeticList addItem(String str, Inline... inlines){
        super.addItem(str, inlines);
        return this;
    }

    public AlphabeticList addSubList(AutoList list){
        super.addSubList(list);
        return this;
    }

    public AlphabeticList addSubList(String list){
        super.addSubList(list);
        return this;
    }
}