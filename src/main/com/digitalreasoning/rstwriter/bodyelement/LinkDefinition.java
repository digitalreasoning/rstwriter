package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Definition;

/**
 * The LinkDefinition class defines a definition for an inline link in the content of an {@link com.digitalreasoning.rstwriter.RstFile} or
 * {@link com.digitalreasoning.rstwriter.Heading}. The definition can be placed anywhere in a reStructuredText file but the identifier
 * (link name inside the ``_)can't be duplicated. A definition is composed of the identifier and a destination hyperlink or an
 * empty definition to specify internal links. A link definition looks like:
 * {@code .. _identifier: http://www.google.com}
 *
 * If this class is used in conjunction with the Heading or RstFile Builders, the default behavior is to add the definition
 * at the end of the content contained in said Heading or RstFile. However, the definitions can be placed anywhere. So,
 * when adding definitions, the two options are:
 * 1) use the {@code addDefinition} methods to use the default behavior and place the definition at the end of the content
 * 2) use the {@code addRstBodyElement} methods to write the definition in a specific place
 */
public class LinkDefinition implements Definition {
    private String text;

    /**
     * Creates a substitution definition with identifier {@code target} and replacement directive {@code replacement}.
     * @param name the identifier to be replaced
     * @param destination the destination this link points to (empty for an internal link target)
     */
    public LinkDefinition(String name, String destination){
        if(destination == null)
        {
            destination = "";
        }
        text = ".. _" + name.toLowerCase().trim() + ": " + destination;
    }

    @Override
    public String write(){
        return text + "\n";
    }
}
