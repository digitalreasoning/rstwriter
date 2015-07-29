package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The LiteralBlock class is a representation of the reStructuredText's literal block. None of the text contained in this
 * block will be processed by the parser. This block can consist of multiple lines, blank lines, and any text. As this
 * text is unprocessed, escaping for the purposes of this library is also unnecessary.
 */
public class LiteralBlock implements RstBodyElement {
    private String text;

    /**
     * creates a literal block with the text in the parameter String
     * @param str the literal block's content text
     */
    public LiteralBlock(String str){
        text = "::\n\n";
        addText(str);
    }

    /**
     * Adds a line of text to the end literal block and appends a new line character to the end
     * @param line the text to be added to the literal block
     * @return this literal block with the line added
     */
    public LiteralBlock addLine(String line){
        text += Utils.INDENT + line.replaceAll("\t", Utils.INDENT) + "\n";
        return this;
    }

    /**
     * Adds the specified text to the end of the literal block
     * @param text the text to be added to the literal block
     * @return this literal block with the text added
     */
    public LiteralBlock addText(String text){
        String[] lines = text.split("\n");
        for(String line : lines){
            this.text += Utils.INDENT + line.replaceAll("\t", Utils.INDENT) + "\n";
        }
        return this;
    }

    @Override
    public String write(){
        return text;
    }
}
