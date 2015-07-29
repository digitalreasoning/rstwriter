package com.digitalreasoning.rstwriter.bodyelement;

import java.util.Map;

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
     * creates an field list from the provided map. Items are checked for correct syntax before being added. Order is 
     * determined by the map's foreach implementation. Fields will be the map's keys, definitions will be the map's values
     * @param map key value pairs to be added as items to the list
     * @throws IllegalArgumentException if field is empty, contains white space, or doesn't start with '-' or '/'
     */
    public FieldList(Map<String, String> map){
        super(Utils.INDENT);
        addItems(map);
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

    /**
     * adds the items to the field list. The order of items will be determined by the map's foreach implementation. The
     * map's keys will be the left elements, values will be right
     * @param map items to add to the list
     * @return this field list with the items added
     */
    public FieldList addItems(Map<String, String> map){
        for(Map.Entry<String, String> entry : map.entrySet()){
            addItem(entry.getKey(), entry.getValue());
        }
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
