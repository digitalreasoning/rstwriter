package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;
import static com.digitalreasoning.rstwriter.bodyelement.Utils.inlineParse;

/**
 * The Paragraph class is a representation of a reStructuredText paragraph. Its content is completely text-based and
 * supports inline markup. Paragraphs can be multi-lined provided that they don't contain any blank lines, or more
 * specifically two consecutive new line characters ('\n').
 */
public class Paragraph implements RstBodyElement {
    private String text;

    /**
     * Creates a new paragraph whose text is the parameter String. WARNING: This text is processed, so the text must be
     * properly escaped. See {@link Inline} for details.
     * @param str the text of the paragraph. The text will be processed for inline markup
     * @param inlines optional arguments specifying inline markup in the text
     */
    public Paragraph(String str, Inline... inlines){
        text = inlineParse(str, inlines);
    }

    /**
     * Adds text to the end of this Paragraph.
     * @param str the text to add to the end of the paragraph. This text will be processed for inline markup
     * @param inlines optional arguments specifying inline markup in the text
     * @return this Paragraph with the text added
     */
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
