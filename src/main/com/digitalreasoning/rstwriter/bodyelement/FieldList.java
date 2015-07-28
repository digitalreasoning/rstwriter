package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The FieldList class is a paired list of fields and their values. Fields must be at least one word and cannot contain
 * line breaks. The values, or definitions, may be empty, single or multi-lined, or body elements themselves. A 
 * reStructuredText representation of a FieldList looks like:
 * {@code
 * :Name: John Doe
 * :Address: 123 Main Street
 * :Schedule: +---+---+---+
 *            | M | T | W |
 *            +---+---+---+
 * }
 * 
 * The list order is determined by the order in which fields are added.
 */
public class FieldList extends PairedList {

    /**
     * Creates an empty field list
     */
    public FieldList(){
        super(Utils.INDENT);
    }

    /**
     * Adds a field/definition pair to the list.
     * @param field an information field
     * @param definition the value to assign to the field. The definition is processed for inline markup
     * @param inlines optional arguments specifying inline markup in the definition
     * @return this list with the field/definition added
     * @throws IllegalArgumentException if field is empty or contains new line characters ('\n')
     */
    public FieldList addItem(String field, String definition, Inline... inlines){
        if(field.equals("")) throw new IllegalArgumentException("Empty field names not allowed");
        if(field.contains("\n")) throw new IllegalArgumentException("Field name can't contain new line");
        super.addItem(":" + field.replaceAll(":", "\\:") + ":", definition, inlines);
        return this;
    }

    /**
     * Adds a field/definition pair to the list.
     * @param field the field being defined
     * @param definition the value to assign to the field.
     * @return this list with the field/definition added
     * @throws IllegalArgumentException if field is empty or contains new line characters ('\n')
     */
    public FieldList addItem(String field, RstBodyElement definition){
        if(field.equals("")) throw new IllegalArgumentException("Empty field names not allowed");
        if(field.contains("\n")) throw new IllegalArgumentException("Field name can't contain new line");
        super.addItem(":"+ field.replaceAll(":", "\\:") + ":", definition);
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
