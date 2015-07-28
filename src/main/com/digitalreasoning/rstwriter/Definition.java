package com.digitalreasoning.rstwriter;

/**
 * A Definition is a body element that defines the variables introduced in the generated reStructuredText. Such variables
 * include links, substitutions, and footnotes. These Definitions may be placed anywhere in a document as long as their
 * identifiers are unique. By the convention of this library, for readability purposes Definitions are placed at the end
 * of the body they are defined in. In other words, they are found at the end of a Heading, subheading, or RstFile. However,
 * Definitions can be placed anywhere, so there are two options:
 * 1) use the {@code addDefinition} methods in the {@code Heading} and {@code RstFile} builders to follow
 *    this library's convention
 * 2) use the {@code addRstBodyElement} methods in the {@code Heading} and {@code RstFile} builders to place
 *    them in a desired location
 */
public interface Definition extends RstBodyElement {
}
