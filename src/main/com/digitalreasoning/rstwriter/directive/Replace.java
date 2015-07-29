package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;

/**
 * The Replace directive is used exclusively in substitution definitions and replaces the identifier with the specified content.
 * @see com.digitalreasoning.rstwriter.bodyelement.SubstitutionDefinition
 */
public class Replace implements Directive {

    private BaseDirective directive;

    /**
     * Creates a replace directive with the specified replacement
     * @param replacement the text to replace the identifier
     */
    public Replace(String replacement){
        directive = new BaseDirective("replace");
        directive.addArgument(replacement);
    }

    @Override
    public String write(){
        return directive.write();
    }
}
