package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;

/**
 * The Contents directive makes a table of contents at the specified place in the files using the names of the file's
 * {@link com.digitalreasoning.rstwriter.Heading}s. This directive takes a title argument and a depth option but no content.
 */
public class Contents implements Directive {
    private BaseDirective directive;
    private String title = "";
    private int depth = 2;

    /**
     * creates an empty table of contents directive
     */
    public Contents(){
        directive = new BaseDirective("contents");
    }

    /**
     * Sets the title of this table of contents
     * @param title the desired title
     * @return this table of contents with the title set
     */
    public Contents setTitle(String title){
        this.title = title;
        return this;
    }

    /**
     * Sets the maximum Heading nesting depth this table of contents will consider
     * @param depthLevel the maximum nesting level of headings to appear in the table of contents
     * @return this table of contents with the depth set
     */
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
