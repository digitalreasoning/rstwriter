package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Definition;

/**
 * Created by creynolds on 7/20/15.
 */
public class LinkDefinition implements Definition {
    private String text;

    public LinkDefinition(String name, String definition){
        text = ".. _" + name.toLowerCase().trim() + ": " + definition;
    }

    @Override
    public String write(){
        return text + "\n";
    }
}
