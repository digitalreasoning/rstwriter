package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/16/15.
 */
public class RomanNumeralList extends EnumeratedList{
    public RomanNumeralList(String str){ super(str, 'I'); }
    public RomanNumeralList(String str, boolean uppercase){ super(str, uppercase ? 'I' : 'i'); }

    public RomanNumeralList addItem(String str, Inline... inlines){
        super.addItem(str, inlines);
        return this;
    }

    public RomanNumeralList addItem(RstBodyElement element){
        super.addItem(element);
        return this;
    }

    public RomanNumeralList addSubList(AutoList list){
        super.addSubList(list);
        return this;
    }

    public RomanNumeralList addSubList(String list){
        super.addSubList(list);
        return this;
    }
}
