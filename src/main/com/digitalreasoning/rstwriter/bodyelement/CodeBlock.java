package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/16/15.
 */
public class CodeBlock implements RstBodyElement {
    private String text;

    public CodeBlock(String str){
        text = "::\n\n";
        addCode(str);
    }

    public CodeBlock addLine(String line){
        text += Constants.INDENT + line.replaceAll("\t", Constants.INDENT) + "\n";
        return this;
    }

    public CodeBlock addCode(String code){
        String[] lines = code.split("\n");
        for(String line : lines){
            text += Constants.INDENT + line.replaceAll("\t", Constants.INDENT) + "\n";
        }
        return this;
    }

    @Override
    public String write(){
        return text;
    }
}
