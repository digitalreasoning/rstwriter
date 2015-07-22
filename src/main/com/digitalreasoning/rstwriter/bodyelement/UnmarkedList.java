package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/16/15.
 */
public class UnmarkedList extends AutoList{
    public UnmarkedList(String str){
        super(str, "|");
    }

    public UnmarkedList addItem(String str, Inline... inlines){
        String toAdd = new Paragraph(str, inlines).getText();
        toAdd = toAdd.replaceAll("\n", "\n| ");
        super.text += "| " + toAdd + "\n";
        return this;
    }

    public UnmarkedList addItem(RstBodyElement element){
        super.addItem(element);
        return this;
    }

    public UnmarkedList addSubList(AutoList list){
        super.addSubList(list);
        return this;
    }

    public UnmarkedList addSubList(String list){
        super.addSubList(list);
        return this;
    }

}
