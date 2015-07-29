package com.digitalreasoning.rstwriter.bodyelement;

import java.util.List;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The LineBlock class represents the literal line break feature in reStructuredText. The literal line blocks preserve all
 * line breaks and indentations in the text.
 */
public class LineBlock implements RstBodyElement {
    private AutoList autoList;
    public LineBlock(String str){
        autoList = new AutoList(str, "|");
    }

    public LineBlock addLine(String str, Inline... inlines){
        String toAdd = new Paragraph(str, inlines).getText();
        toAdd = toAdd.replaceAll("\n", "\n| ");
        autoList.text += "| " + toAdd + "\n";
        return this;
    }

    public LineBlock addLine(RstBodyElement element){
        autoList.addItem(element);
        return this;
    }

    @Override
    public String write(){
        return autoList.write();
    }

}
