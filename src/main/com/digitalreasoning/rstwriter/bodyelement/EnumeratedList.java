package com.digitalreasoning.rstwriter.bodyelement;

/**
 * Created by creynolds on 7/16/15.
 */
class EnumeratedList extends AutoList{
    public EnumeratedList(String str, char start){
        super(str, "#.", start + ".");
    }
}
