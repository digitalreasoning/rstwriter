package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;
import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.bodyelement.Paragraph;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The Admonition class is an implementation of many of the reStructuredText "Admonition" directives. Those defined in this
 * class are attention, caution, danger, error, hint, important, note, tip, and warning. These directives take no
 * arguments or options.
 */
public class Admonition implements Directive {

    /**
     * Type specifies which of the admonition directives will be used in this case
     */
    public enum Type{
        ATTENTION("attention"),
        CAUTION("caution"),
        DANGER("danger"),
        ERROR("error"),
        HINT("hint"),
        IMPORTANT("important"),
        NOTE("note"),
        TIP("tip"),
        WARNING("warning");
        
        private String value;
        
        Type(String value){
            this.value = value;
        }
        
        public String getValue(){ return value; }
    }
    
    private BaseDirective directive;

    /**
     * Creates an empty admonition directive of the given type
     * @param type the type of admonition to be used
     */
    public Admonition(Type type){
        directive = new BaseDirective(type.getValue());
    }

    /**
     * Creates an admonition of the given type with the given text
     * @param type the type of admonition to be used
     * @param content the text contained in the admonition (will be processed for inline markup)
     * @param inlines optional arguments specifying inline markup in the text
     */
    public Admonition(Type type, String content, Inline... inlines){
        directive = new BaseDirective(type.getValue());
        directive.addContent(new Paragraph(content, inlines));
    }

    /**
     * Adds a paragraph to the content of this admonition
     * @param content the text contained in the admonition (will be processed for inline markup)
     * @param inlines optional arguments specifying inline markup in the text
     * @return this admonition with the paragraph added to the content
     */
    public Admonition addParagraph(String content, Inline... inlines){
        directive.addContent(new Paragraph(content, inlines));
        return this;
    }

    /**
     * Adds the body element to the content of this directive
     * @param element the element to be added to the content of the admonition
     * @return this admonition with the element added
     */
    public Admonition addContent(RstBodyElement element){
        directive.addContent(element);
        return this;
    }

    @Override
    public String write(){
        return directive.write();
    }
}
