package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/16/15.
 */
public class BulletList extends AutoList {

    public BulletList(String str){
        super(str, "*");
    }

    public BulletList addItem(String str, Inline... inlines){
        super.addItem(str, inlines);
        return this;
    }

    public BulletList addItem(RstBodyElement element){
        super.addItem(element);
        return this;
    }

    public BulletList addSubList(AutoList list){
        super.addSubList(list);
        return this;
    }

    public BulletList addSubList(String list){
        super.addSubList(list);
        return this;
    }
}
