package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.Paragraph;

/**
 * Created by creynolds on 7/13/15.
 */
public class RstFile {
    private ContentBase content;

    public RstFile(ContentBase contentBase){
        this.content = contentBase;
        this.content.isFile();
    }

    public String write(){
       return content.write();
    }

    public ContentBase getContentBase(){
        return content;
    }

    public static Builder builder(String fileName){
        return new Builder(fileName);
    }


    public static class Builder {
        private ContentBase contentBase;
        
        public Builder(String fileName){
            contentBase = new ContentBase(fileName, -1);
        }

        public Builder addParagraph(String text, Inline... inlines){
            contentBase.add(new Paragraph(text, inlines));
            return this;
        }

        public Builder addBodyElement(RstBodyElement s){
            contentBase.add(s);
            return this;
        }

        public Builder addDirective(Directive d){
            contentBase.add(d);
            return this;
        }

        public Builder addDefinition(Definition element){
            contentBase.addDefinition(element);
            return this;
        }

        public Builder addHeading(Heading h){
            contentBase.add(h.getContentBase());
            return this;
        }

        public Builder addTransition(){
            contentBase.add(new Transition());
            return this;
        }

        public RstFile build(){
            return new RstFile(contentBase);
        }
    }
}
