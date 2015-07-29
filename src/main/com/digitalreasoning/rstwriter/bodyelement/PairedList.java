package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

import java.util.LinkedList;
import java.util.Map;

/**
 * The PairedList class is the background worker for the lists that contain left and right elements, such as the
 * {@link DefinitionList}, the {@link OptionList}, and the {@link FieldList}.
 */
class PairedList implements RstBodyElement {
    protected LinkedList<String> leftList;
    protected LinkedList<String> rightList;
    protected String division;

    /**
     * creates an empty paired list with the specified division mechanism between left and right elements
     * @param division the text to place between left and right elements
     */
    public PairedList(String division){
        leftList = new LinkedList<>();
        rightList = new LinkedList<>();
        this.division = division;
    }

    /**
     * creates a paired list with the specified division mechanism and the items designated by the map. Order of items
     * will be determined by the map's foreach ordering. The map's keys will be the left elements, values will be right
     * @param division the text to place between left and right elements
     * @param map items to be added to the list
     */
    public PairedList(String division, Map<String, String> map){
        this(division);
        addItems(map);
    }
    /**
     * Adds an item to this list. The right element will be processed for inline markup
     * @param left the left element in the item to be added, e.g. a term
     * @param right the right element in the item to be added, e.g. a definition (this String will be processed for inline markup)
     * @param inlines optional arguments specifying inline markup in the right element
     * @return this paired list with the item added
     */
    public PairedList addItem(String left, String right, Inline... inlines){
        leftList.add(left); 
        rightList.add(new Paragraph(right, inlines).getText());
        return this;
    }

    /**
     * Adds an item to this list.
     * @param left the left element in the item to be added, e.g. a term
     * @param right the right element in the item to be added, e.g. a definition
     * @return this paired list with the item added
     */
    public PairedList addItem(String left, RstBodyElement right){
        leftList.add(left);
        rightList.add(right.write());
        return this;
    }

    /**
     * adds the items to the paired list. The order of items will be determined by the map's foreach implementation. The
     * map's keys will be the left elements, values will be right
     * @param map items to add to the list
     * @return this paired list with the items added
     */
    public PairedList addItems(Map<String, String> map){
        for(Map.Entry<String, String> entry : map.entrySet()){
            addItem(entry.getKey(), entry.getValue());
        }
        return this;
    }
    
    @Override
    public String write(){
        if(leftList.size() != rightList.size()){
            throw new IllegalStateException("Unequal numbers of left and right");
        }
        String text = "";
        for(int i = 0; i < leftList.size(); i++){
            text += writeRow(leftList.get(i), rightList.get(i));
        }
        return text;
    }


    private String writeRow(String left, String right){
        String row = left + division;
        String[] lines = right.split("\n");
        if(lines.length == 0) return "";
        row += lines[0] + "\n";
        for(int i = 1; i<lines.length; i++){
            row += adjustAlignment(left) + lines[i] + "\n";
        }
        return row;
    }

    //corrects for multi-lined definitions
    private String adjustAlignment(String left){
        String spaces = "";
        int newLine = division.indexOf('\n');
        if(newLine == -1){
            for(int i = 0; i<left.length(); i++){
                spaces += " ";
            }
            spaces += division;
        }
        else{
            spaces += division.substring(newLine+1);
        }
        return spaces;
    }
}
