package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;

/**
 * Created by creynolds on 7/20/15.
 */
public class Unicode implements Directive {
    private BaseDirective directive;

    public Unicode(String replacement){
        directive = new BaseDirective("unicode");
        directive.addArgument(replacement);
    }

    @Override
    public String write(){
        return directive.write();
    }
}
