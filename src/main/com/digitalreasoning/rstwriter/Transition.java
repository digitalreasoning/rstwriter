package com.digitalreasoning.rstwriter;

/**
 * A Transition is simply a horizontal bar that can be useful as an informal separation in content. Transitions can be nested
 * inside Headings, but not body elements.
 */
public class Transition implements RstElement {
    @Override
    public String write(){
        return "------\n";
    }
}
