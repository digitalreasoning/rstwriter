package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/16/15.
 */
class AutoList implements RstBodyElement {
    protected String text;
    private String marker;
    private String start;
    private String align;
    private boolean hasItem = false;

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

    public AutoList(String str, String marker){
        this(str, marker, marker);
    }

    public AutoList addItem(String str, Inline... inlines){
        String toAdd = processItem(str, inlines);
        text += getSymbol() + " " + toAdd + "\n";
        return this;
    }

    public AutoList addItem(RstBodyElement element){
        String[] lines = element.write().split("\n");
        if(lines.length > 0){
            text += getSymbol() + " " + lines[0] + "\n";
            for(int i = 1; i<lines.length; i++)
                text += align + lines[i] + "\n";
        }
        return this;
    }

    public AutoList addSubList(AutoList list){
        if(list.text.equals("")) return this;
        text += "\n";
        String[] items = list.text.split("\n");
        for(String line : items){
            text += Constants.INDENT + line + "\n";
        }
        text += "\n";
        return this;
    }

    public AutoList addSubList(String list){
        return addSubList(new AutoList(list, marker));
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
