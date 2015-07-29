package com.digitalreasoning.rstwriter;

/**
 * A Directive is a kind of body element that changes the behavior of the parser by adding non-textual data,
 * invoking substitutions, modifying parser settings, etc. By defining new Directives in a reStructuredText parser, it's
 * possible to customize a brand of reStructuredText. Role and Directive definition are the two common ways of making such
 * extensions. This interface is used by Directives and is allows Directives to be used for more special purposes, such as
 * substitution syntax. This interface is the only necessary inheritance required to define a new Directive, but the
 * {@link com.digitalreasoning.rstwriter.directive.BaseDirective} class is also provided to offer the simple functionality
 * necessary for a successful Directive class.
 * @see com.digitalreasoning.rstwriter.directive.BaseDirective
 */
public interface Directive extends RstBodyElement {
}
