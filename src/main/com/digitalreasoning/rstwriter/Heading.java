package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.LinkDefinition;
import com.digitalreasoning.rstwriter.bodyelement.Paragraph;

/**
 * Created by creynolds on 7/13/15.
 */
public class Heading implements RstElement {
    private ContentBase content;

    public Heading(ContentBase content){
        this.content = content;
    }

    public ContentBase getContentBase(){
        return content;
    }

    public static Builder builder(String name){
        return new Builder(name);
    }

    @Override
    public String write(){
        return content.write();
    }

    public static class Builder{
        private ContentBase contentBase;
        private Heading.Builder parent;
        private boolean hasOpenSubheading = false;

        Builder(String name){
            contentBase = new ContentBase(name);
            parent = null;
        }

        private Builder(String name, Heading.Builder parent){
            contentBase = new ContentBase(name);
            this.parent = parent;
        }

        public Builder addParagraph(String text, Inline... inlines){
            contentBase.add(new Paragraph(text, inlines));
            return this;
        }

        public Builder addBodyElement(RstBodyElement element){
            contentBase.add(element);
            return this;
        }

        public Builder addDirective(Directive d){
            contentBase.add(d);
            return this;
        }

        public Builder addLinkTarget(String name){
            contentBase.addLinkTarget(new LinkDefinition(name, ""));
            return this;
        }

        public Builder addDefinition(Definition def){
            contentBase.addDefinition(def);
            return this;
        }

        public Builder addTransition(){
            contentBase.add(new Transition());
            return this;
        }

        public Builder addSubHeading(Heading h){
            contentBase.add(h.getContentBase());
            return this;
        }

        public Builder openSubHeading(String name){
            if(hasOpenSubheading){
                throw new IllegalStateException("Subheading already open in this builder");
            }
            hasOpenSubheading = true;
            return new Heading.Builder(name, this);
        }

        public Builder closeSubHeading(){
            if(parent == null){
                throw new IllegalStateException("This subheading doesn't have a parent.");
            }
            parent.hasOpenSubheading = false;
            parent.addSubHeading(this.build());
            return parent;
        }

        public Heading build(){
            if(hasOpenSubheading){
                throw new IllegalStateException("This builder has an open subheading.");
            }
            if(parent != null && parent.hasOpenSubheading){
                throw new UnsupportedOperationException("Calling build() on an open subheading not supported");
            }
            return new Heading(contentBase);
        }
    }
}
