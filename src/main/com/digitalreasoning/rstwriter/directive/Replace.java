package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;

/**
 * Created by creynolds on 7/20/15.
 */
public class Replace implements Directive {

    private BaseDirective directive;

    public Replace(String replacement){
        directive = new BaseDirective("replace");
        directive.addArgument(replacement);
    }

    @Override
    public String write(){
        return directive.write();
    }
}
