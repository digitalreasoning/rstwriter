package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;

/**
 * Created by creynolds on 7/21/15.
 */
public class Contents implements Directive {
    private BaseDirective directive;
    private String title = "";
    private int depth = 2;

    public Contents(){
        directive = new BaseDirective("contents");
    }

    public Contents setTitle(String title){
        this.title = title;
        return this;
    }

    public Contents setDepth(int depthLevel){
        this.depth = depthLevel;
        return this;
    }

    @Override
    public String write(){
        directive.addArgument(title);
        directive.addOption("depth", "" + depth);
        return directive.write();
    }
}
