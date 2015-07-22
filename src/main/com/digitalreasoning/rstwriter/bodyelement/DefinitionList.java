package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/16/15.
 */
public class DefinitionList extends PairedList {
    public DefinitionList(){
        super("\n" + Constants.INDENT);
    }

    public DefinitionList addItem(String term, String definition, Inline... inlines){
        super.addItem(correctTerm(term, null), definition, inlines);
        return this;
    }

    public DefinitionList addItem(String term, String[] classifiers, String definition, Inline... inlines){
        super.addItem(correctTerm(term, classifiers), definition, inlines);
        return this;
    }

    public DefinitionList addItem(String term, RstBodyElement definition){
        super.addItem(correctTerm(term, null), definition);
        return this;
    }

    public DefinitionList addItem(String term, String[] classifiers, RstBodyElement definition){
        super.addItem(correctTerm(term, classifiers), definition);
        return this;
    }

    private String correctTerm(String term, String[] classifiers){
        validate(term);
        String build = term;
        if(classifiers != null) {
            for (String str : classifiers) {
                validate(str);
                build += " : " + str;
            }
        }
        return build;
    }

    private void validate(String str){
        if(str.equals("")) throw new IllegalArgumentException("Empty terms not allowed");
        if(str.contains("\n")) throw new IllegalArgumentException("Terms can't contain new lines");
    }

    @Override
    public String write(){
        try{
            return super.write();
        }catch(IllegalStateException e){
            throw new IllegalStateException(e.getMessage().replace("left", "terms").replace("right", "definitions"));
        }
    }

}
