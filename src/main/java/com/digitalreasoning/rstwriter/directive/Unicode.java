package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;

/**
 * The Unicode directive is used exclusively in substitution definitions and replaces the identifier with the character
 * designated by a given Unicode character code
 */
public class Unicode implements Directive {
    private BaseDirective directive;

    /**
     * Creates a unicode directive with the given character code
     * @param replacement a string representation of the character codes separated by spaces. Codes can be decimal
     *                    or hexadecimal. Text will be left as is.
     */
    public Unicode(String replacement){
        directive = new BaseDirective("unicode");
        directive.addArgument(replacement);
    }

    @Override
    public String write(){
        return directive.write();
    }
}
