package com.digitalreasoning.rstwriter.bodyelement;

import java.util.List;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The AutoList class represents an automatically generated list. In reStructuredText, automatically generated lists
 * include {@link BulletList}s, {@link NumberedList}s, {@link AlphabeticList}s, and {@link RomanNumeralList}s. Furthermore,
 * this class powers the {@link LineBlock}. List items begin with a predefined marker (* for bullet lists, #. for
 * enumerated lists) and may contain text (with or without inline markup) or any other body element.
 */
class AutoList implements RstBodyElement {
    protected String text;
    private String marker;
    private String start;
    private String align;
    private int numItems = 0;

    /**
     * Creates an AutoList with items defined by the parameter String. Items are separated by '\n' characters. The type of
     * this list will determine which symbol will begin each item of the list
     * @param str a list of items separated by '\n'
     * @param marker the normal symbol that begins an item
     * @param start the symbol that begins the first item to determine the list type (alphabetic, numerical, roman numerical)
     */
    protected AutoList(String str, String marker, String start){
        String[] lines = str.split("\n");
        this.marker = marker;
        this.start = start;
        this.align = "";
        for(int i = 0; i<this.marker.length() +1; i++){
            align += " ";
        }
        text = "";
        for(String line: lines){
            if(!line.equals("")) {
                text += getSymbol() + " " + line + "\n";
                numItems++;
            }
        }
    }

    /**
     * Creates an AutoList from the specified list and line markers
     * @param list items to be added
     * @param marker beginning of each item's line
     * @param start beginning of the first item's line to determine list type
     */
    protected AutoList(List<String> list, String marker, String start){
        this("", marker, start);
        addItems(list);
    }


    /**
     * Creates an AutoList with items defined by the parameter String. Items are separated by '\n' characters. The type of
     * this list will determine which symbol will begin each item of the list
     * @param str a list of items separated by '\n'
     * @param marker the normal symbol that begins an item
     */
    protected AutoList(String str, String marker){
        this(str, marker, marker);
    }

    /**
     * Adds an item to this list, and processes it for inline markup
     * @param str the item to be added
     * @param inlines optional arguments specifying inline markup
     * @return this list with the item added
     */
    protected AutoList addItem(String str, Inline... inlines){
        String toAdd = processItem(str, inlines);
        text += getSymbol() + " " + toAdd + "\n";
        numItems++;
        return this;
    }

    /**
     * Adds an item to this list
     * @param element the item to be added
     * @return this list with the item added
     */
    protected AutoList addItem(RstBodyElement element){
        String[] lines = element.write().split("\n");
        if(lines.length > 0){
            numItems++;
            text += getSymbol() + " " + lines[0] + "\n";
            for(int i = 1; i<lines.length; i++)
                text += align + lines[i] + "\n";
        }
        return this;
    }

    /**
     * Adds all items to the list
     * @param list items to be added
     * @return this AutoList with the items added
     */
    protected AutoList addItems(List<String> list){
        for(String item : list){
            addItem(item);
        }
        return this;
    }

    /**
     * Adds a sub-list given as a parameter; the sub-list is indented and added after the current item. Subsequent additions
     * to this list will be placed after the sub-list.
     * @param list the sub-list to be added
     * @return this list with the sub-list added
     */
    protected AutoList addSubList(AutoList list){
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
    protected AutoList addSubList(String list){
        return addSubList(new AutoList(list, marker, start));
    }

    @Override
    public String write() {
        return bugCheck(text);
    }

    @Override
    public String toString(){
        return write();
    }

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
        if(numItems > 0){
            return marker;
        }
        else{
            return start;
        }
    }

    //This bug happens in a few parsers I've tried. If used on the right side of a field list, with a single item in each
    //the main list and sublist, the parser will combine them to a single list. This function corrects for this with a
    //formatted comment
    private String bugCheck(String str){
        if(numItems == 1){
            int index = str.indexOf("\n\n" + Utils.INDENT);
            if(index != -1){
                int newLine = str.indexOf("\n", index+ ("\n\n" + Utils.INDENT).length());
                if(newLine == str.length()-2 && str.charAt(newLine+1) == '\n'){
                    return str + ".. Formatting\n";
                }
            }
        }
        return str;
    }
}
