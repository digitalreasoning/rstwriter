package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Definition;
import com.digitalreasoning.rstwriter.Directive;

/**
 * Created by creynolds on 7/20/15.
 */
public class SubstitutionDefinition implements Definition {
    private String text;

    public SubstitutionDefinition(String target, Directive replacement){
        text = ".. |" + target + "| " + replacement.write().substring(".. ".length());
    }

    @Override
    public String write(){
        return text;
    }
}
