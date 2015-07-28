package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Definition;
import com.digitalreasoning.rstwriter.Directive;

/**
 * The SubstitutionDefinition class defines a definition for an inline substitution in the content of an {@link com.digitalreasoning.rstwriter.RstFile} or
 * {@link com.digitalreasoning.rstwriter.Heading}. The definition can be placed anywhere in a reStructuredText file but the identifier (the text inside the ||)
 * can't be duplicated. A definition is composed of the identifier and a directive defining the replacement:
 * {@code .. |identifier| directiveName:: directiveContent}
 *
 * If this class is used in conjunction with the Heading or RstFile Builders, the default behavior is to add the definition
 * at the end of the content contained in said Heading or RstFile. However, the definitions can be placed anywhere. So,
 * when adding defintions, the two options are:
 * 1) use the {@code addDefinition} methods to use the default behavior and place the definition at the end of the content
 * 2) use the {@code addRstBodyElement} methods to write the definition in a specific place
 */
public class SubstitutionDefinition implements Definition {
    private String text;

    /**
     * Creates a substitution definition with identifier {@code target} and replacement directive {@code replacement}.
     * @param target the identifier to be replaced
     * @param replacement the Directive specifying the replacement strategy
     */
    public SubstitutionDefinition(String target, Directive replacement){
        text = ".. |" + target + "| " + replacement.write().substring(".. ".length());
    }

    @Override
    public String write(){
        return text;
    }
}
