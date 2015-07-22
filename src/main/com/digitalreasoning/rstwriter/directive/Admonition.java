package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;
import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.bodyelement.Paragraph;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/17/15.
 */
public class Admonition implements Directive {

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
    
    public Admonition(Type type){
        directive = new BaseDirective(type.getValue());
    }

    public Admonition(Type type, String content, Inline... inlines){
        directive = new BaseDirective(type.getValue());
        directive.addContent(new Paragraph(content, inlines));
    }

    public Admonition addParagraph(String content, Inline... inlines){
        directive.addContent(new Paragraph(content, inlines));
        return this;
    }

    public Admonition addContent(RstBodyElement element){
        directive.addContent(element);
        return this;
    }

    @Override
    public String write(){
        return directive.write();
    }
}
