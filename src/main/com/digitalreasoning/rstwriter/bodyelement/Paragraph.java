package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;
import static com.digitalreasoning.rstwriter.bodyelement.Utils.inlineParse;

/**
 * Created by creynolds on 7/13/15.
 */
public class Paragraph implements RstBodyElement {
    private String text;

    public Paragraph(String str){
        this(str, null);
    }

    public Paragraph(String str, Inline... inlines){
        text = inlineParse(str, inlines);
    }

    public Paragraph addText(String str, Inline... inlines){
        text += inlineParse(str, inlines);
        return this;
    }

    protected String getText(){
        return text;
    }

    @Override
    public String write(){
        return text + "\n";
    }


}
