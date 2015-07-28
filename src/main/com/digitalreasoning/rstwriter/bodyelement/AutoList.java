package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The AutoList class represents an automatically generated list. In reStructuredText, automatically generated lists
 * include {@link BulletList}s, {@link NumberedList}s, {@link AlphabeticList}s, and {@link RomanNumeralList}s. Furthermore,
 * this class powers the {@link UnmarkedList}. List items begin with a predefined marker (* for bullet lists, #. for
 * enumerated lists) and may contain text (with or without inline markup) or any other body element.
 */
class AutoList implements RstBodyElement {
    protected String text;
    private String marker;
    private String start;
    private String align;
    private boolean hasItem = false;

    /**
     * Creates an AutoList with items defined by the parameter String. Items are separated by '\n' characters. The type of
     * this list will determine which symbol will begin each item of the list
     * @param str a list of items separated by '\n'
     * @param marker the normal symbol that begins an item
     * @param start the symbol that begins the first item to determine the list type (alphabetic, numerical, roman numerical)
     */
    public AutoList(String str, String marker, String start){
        String[] lines = str.split("\n");
        this.marker = marker;
        this.start = start;
        this.align = "";
        for(int i = 0; i<this.marker.length() +1; i++) align += " ";
        text = "";
        for(String line: lines){
            if(!line.equals("")) {
                text += getSymbol() + " " + line + "\n";
            }
        }
    }

    /**
     * Creates an AutoList with items defined by the parameter String. Items are separated by '\n' characters. The type of
     * this list will determine which symbol will begin each item of the list
     * @param str a list of items separated by '\n'
     * @param marker the normal symbol that begins an item
     */
    public AutoList(String str, String marker){
        this(str, marker, marker);
    }

    /**
     * Adds an item to this list, and processes it for inline markup
     * @param str the item to be added
     * @param inlines optional arguments specifying inline markup
     * @return this list with the item added
     */
    public AutoList addItem(String str, Inline... inlines){
        String toAdd = processItem(str, inlines);
        text += getSymbol() + " " + toAdd + "\n";
        return this;
    }

    /**
     * Adds an item to this list
     * @param element the item to be added
     * @return this list with the item added
     */
    public AutoList addItem(RstBodyElement element){
        String[] lines = element.write().split("\n");
        if(lines.length > 0){
            text += getSymbol() + " " + lines[0] + "\n";
            for(int i = 1; i<lines.length; i++)
                text += align + lines[i] + "\n";
        }
        return this;
    }

    /**
     * Adds a sub-list given as a parameter; the sub-list is indented and added after the current item. Subsequent additions
     * to this list will be placed after the sub-list.
     * @param list the sub-list to be added
     * @return this list with the sub-list added
     */
    public AutoList addSubList(AutoList list){
        if(list.text.equals("")) return this;
        text += "\n";
        String[] items = list.text.split("\n");
        for(String line : items){
            text += Utils.INDENT + line + "\n";
        }
        text += "\n";
        return this;
    }

    /**
     * Adds an sub-list with the same type as this list created from the parameter; the sub-list is indented and added
     * after the current item. Subsequent additions to this list will be placed after the sub-list.
     * @param list the sub-list to be added, with items separated by the character '\n'
     * @return this list with the sub-list added
     */
    public AutoList addSubList(String list){
        return addSubList(new AutoList(list, marker, start));
    }

    @Override
    public String write() { return text; }

    @Override
    public String toString(){ return text; }

    private String processItem(String str, Inline... inlines){
        String process = new Paragraph(str, inlines).getText();
        while(process.startsWith("\n")){
            process = process.substring(1);
        }
        while(process.endsWith("\n")){
            process = process.substring(0, process.length()-1);
        }
        String[] arr = process.split("\n");
        if(arr.length == 1){
            return process;
        }
        else{
            String returnString = arr[0];
            for(int i = 1; i<arr.length; i++){
                if(arr[i].trim().length() != 0)
                    returnString += "\n\n" + align + arr[i].trim();
            }
            return returnString;
        }
    }

    private String getSymbol(){
        if(hasItem){
            return marker;
        }
        else{
            hasItem = true;
            return start;
        }
    }
}
