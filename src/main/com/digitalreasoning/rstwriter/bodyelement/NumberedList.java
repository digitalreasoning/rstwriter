package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/16/15.
 */
public class NumberedList extends EnumeratedList {

    public NumberedList(String str){
        super(str, '#');
    }

    public NumberedList addItem(String str, Inline... inlines){
        super.addItem(str, inlines);
        return this;
    }

    public NumberedList addItem(RstBodyElement element){
        super.addItem(element);
        return this;
    }

    public NumberedList addSubList(AutoList list){
        super.addSubList(list);
        return this;
    }

    public NumberedList addSubList(String list){
        super.addSubList(list);
        return this;
    }
}
