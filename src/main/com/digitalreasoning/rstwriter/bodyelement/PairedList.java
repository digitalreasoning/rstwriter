package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

import java.util.LinkedList;

/**
 * Created by creynolds on 7/16/15.
 */
class PairedList implements RstBodyElement {
    protected LinkedList<String> leftList;
    protected LinkedList<String> rightList;
    protected String division;
    
    public PairedList(String division){
        leftList = new LinkedList<>();
        rightList = new LinkedList<>();
        this.division = division;
    }
    
    public PairedList addItem(String left, String right, Inline... inlines){
        leftList.add(left); 
        rightList.add(new Paragraph(right, inlines).getText());
        return this;
    }
    
    public PairedList addItem(String left, RstBodyElement right){
        leftList.add(left);
        rightList.add(right.write());
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
            row += alignmentAdjustment(left) + lines[i] + "\n";
        }
        return row;
    }

    private String alignmentAdjustment(String left){
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
