package com.digitalreasoning.rstwriter;

import java.util.Iterator;

/**
 * The RstElement interface represents Headings and Transitions, or anything that can't be nested in a body element but can
 * be nested in content. Headings can be nested inside Headings, and Transitions can be nested inside Headings. All content
 * is placed in an RstFile. This interface is used primarily to differentiate between RstBodyElements and other content.
 * @see RstBodyElement
 */
interface RstElement {

    /**
     * Writes the reStructuredText represented by this element
     * @return the reStructuredText representation of this element
     */
    String write();
}
