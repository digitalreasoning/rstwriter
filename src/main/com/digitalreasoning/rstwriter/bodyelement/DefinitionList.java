package com.digitalreasoning.rstwriter.bodyelement;

import java.util.Map;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The DefinitionList class represents a list of terms (with or without classifiers) and their definitions, as defined in
 * reStructuredText's body elements. Terms cannot be empty or span more than one line. Definitions may be empty, multi-lined,
 * or even contain multiple body elements.  The reStructuredText representation looks like:
 * {@code
 * term
 *     definition
 * }
 *
 * Terms may also have extra pieces of information attached known as classifiers. In reStructuredText:
 * {@code
 * term : classifier : classifier : ...
 *     definition
 * }
 *
 * The term order in the list is determined entirely by their order of addition into the list.
 */
public class DefinitionList extends PairedList {

    /**
     * Creates an empty definition list
     */
    public DefinitionList(){
        super("\n" + Utils.INDENT);
    }

    /**
     * creates an definition list from the provided map. Items are checked for correct syntax before being added. Order is 
     * determined by the map's foreach implementation. Definitions will be the map's keys, definitions will be the map's values
     * @param map key value pairs to be added as items to the list
     * @throws IllegalArgumentException if definition is empty, contains white space, or doesn't start with '-' or '/'
     */
    public DefinitionList(Map<String, String> map){
        super(Utils.INDENT);
        addItems(map);
    }
    
    /**
     * Adds a term/definition pair to the list.
     * @param term the term being defined
     * @param definition the definition of the term. The definition is processed for inline markup
     * @param inlines optional arguments specifying inline markup in the definition
     * @return this list with the term/definition added
     * @throws IllegalArgumentException if term is empty or contains new line characters ('\n')
     */
    public DefinitionList addItem(String term, String definition, Inline... inlines){
        super.addItem(correctTerm(term, null), definition, inlines);
        return this;
    }

    /**
     * Adds a (term:classifiers)/definition pair to the list.
     * @param term the term being defined
     * @param classifiers array of classifier words/phrases to provide more information about the term
     * @param definition the definition of the term. The definition is processed for inline markup
     * @param inlines optional arguments specifying inline markup in the definition
     * @return this list with the term/definition added
     * @throws IllegalArgumentException if term or classifiers are empty or contain new line characters ('\n')
     */
    public DefinitionList addItem(String term, String[] classifiers, String definition, Inline... inlines){
        super.addItem(correctTerm(term, classifiers), definition, inlines);
        return this;
    }

    /**
     * Adds a term/definition pair to the list.
     * @param term the term being defined
     * @param definition the definition of the term.
     * @return this list with the term/definition added
     * @throws IllegalArgumentException if term is empty or contains new line characters ('\n')
     */
    public DefinitionList addItem(String term, RstBodyElement definition){
        super.addItem(correctTerm(term, null), definition);
        return this;
    }

    /**
     * Adds a (term:classifiers)/definition pair to the list.
     * @param term the term being defined
     * @param classifiers array of classifier words/phrases to provide more information about the term
     * @param definition the definition of the term.
     * @return this list with the term/definition added
     * @throws IllegalArgumentException if term or classifiers are empty or contain new line characters ('\n')
     */
    public DefinitionList addItem(String term, String[] classifiers, RstBodyElement definition){
        super.addItem(correctTerm(term, classifiers), definition);
        return this;
    }

    /**
     * adds the items to the definition list. The order of items will be determined by the map's foreach implementation. The
     * map's keys will be the left elements, values will be right
     * @param map items to add to the list
     * @return this definition list with the items added
     */
    public DefinitionList addItems(Map<String, String> map){
        for(Map.Entry<String, String> entry : map.entrySet()){
            addItem(entry.getKey(), entry.getValue());
        }
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
