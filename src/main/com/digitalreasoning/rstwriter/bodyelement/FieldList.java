package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/16/15.
 */
public class FieldList extends PairedList {

    public FieldList(){
        super(Constants.INDENT);
    }

    public FieldList addItem(String field, String definition, Inline... inlines){
        if(field.equals("")) throw new IllegalArgumentException("Empty field names not allowed");
        if(field.contains("\n")) throw new IllegalArgumentException("Field name can't contain new line");
        super.addItem(":" + field+ ":", definition, inlines);
        return this;
    }

    public FieldList addItem(String field, RstBodyElement definition){
        if(field.equals("")) throw new IllegalArgumentException("Empty field names not allowed");
        if(field.contains("\n")) throw new IllegalArgumentException("Field name can't contain new line");
        super.addItem(":"+ field + ":", definition);
        return this;
    }

    @Override
    public String write() {
        try{
            return super.write();
        }catch(IllegalStateException e){
            throw new IllegalStateException(e.getMessage().replace("left", "fields").replace("right", "definitions"));
        }
    }
}
