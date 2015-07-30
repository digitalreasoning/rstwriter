package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.LinkDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The ContentBase class is a container for the information added by the programmer. It takes RstElements
 * as arguments in its methods. It writes any RstBodyElements or Transitions to a string immediately and stores headings 
 * to be placed after the initial content.
 */
class ContentBase implements RstElement{
    /**
     * The heading title. If this content base represents a file, its value isn't used
     */
    private String title;
    private ArrayList<RstElement> elements;
    private boolean isFile = false;
    /**
     * The characters and order of precedence in the borders correspond to Sphinx's recommended list at
     * <a href="http://sphinx-doc.org/rest.html#sections"></a>, followed by arbitrary alternatives
     */
    private static final char[] borders = {'#', '*', '=', '-', '^', '"', // <- recommended
            '\'', ':', '.', '/', ';', '\\', ',', '`', '[', '{', '(', '<', '+', '_', '$', '%', '&', '@', '?', '!', ']', '}', ')', '>'}; //alternatives
    /**
     * the Heading level. 0 is highest. Levels 1 and above are nested headings.
     */
    private int level;
    /**
     * set of link targets for this heading
     */
    private ArrayList<String> linkTargets;
    /**
     * list of definitions to be included at the end of this heading/file
     */
    private ArrayList<Definition> definitions;

    protected ContentBase(String name){
        this(name, 0);
    }

    protected ContentBase(String name, int level){
        this.title = name;
        this.level = level;
        this.elements = new ArrayList<>();
        this.linkTargets = new ArrayList<>();
        this.definitions = new ArrayList<>();
    }

    protected ContentBase(ContentBase cb){
        this(cb.title, cb.level);
        cb.elements.forEach(this.elements::add);
        this.definitions.addAll(cb.definitions);
        this.linkTargets.addAll(cb.linkTargets);
    }

    protected void add(RstElement element){
        if(element instanceof ContentBase || element instanceof ElementBox){
            elements.add(element);
        }
        else
        {
            elements.add(new ElementBox(element));
        }
    }

    protected void addLinkTarget(LinkDefinition linkDefinition){
        if(isFile)
            throw new UnsupportedOperationException("Link targets aren't supported at the RstFile level");
        String str = linkDefinition.write();
        if(!linkTargets.contains(str))
            linkTargets.add(str);
    }

    protected void addDefinition(Definition def){
        definitions.add(def);
    }

    protected void isFile(){ isFile = true; }

    protected String getTitle(){ return title; }

    @Override
    public String write(){
        String returnString = "";
        for(String target : linkTargets){
            returnString += target;
        }
        if(!linkTargets.isEmpty()){
            returnString += "\n";
        }

        if(!isFile){
            returnString += border(title);
        }
        for(RstElement element : elements){
            if(element instanceof ContentBase)
            {
                ContentBase cb = (ContentBase) element;
                cb.level = this.level + 1;
                returnString += cb.write();
            }
            else{
                returnString += element.write() + "\n";
            }
        }
        for(Definition d : definitions){
            returnString += d.write();
        }
        if(!definitions.isEmpty())
        {
            returnString += "\n";
        }

        return returnString;
    }

    private String border(String str){
        String line = "";
        for(int i = 0; i < str.length(); i++){
            line += borders[level];
        }
        String s = "";
        if(level <= 1){
            s += line + "\n";
        }
        s += title + "\n" + line + "\n\n";
        return s;
    }

    class ElementBox implements RstElement{
        private String text;

        private ElementBox(RstElement element){
            text = element.write();
        }

        @Override
        public String write(){
            return text;
        }
    }

}
