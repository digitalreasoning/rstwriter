package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.LinkDefinition;
import com.digitalreasoning.rstwriter.bodyelement.SubstitutionDefinition;

/**
 * The Inline class defines all kinds of inline markup supported by reStructuredText. Examples include **bold**, *italics*,
 * ``literal``, and `interpreted text`. The Inline class is useful for its static methods, which take Strings and return
 * Inline objects that represent the fully-escaped and marked-up reStructuredText. The methods in this library that
 * accept String and Inline parameters process text and inject marked-up text where desired. To use this processing,
 * instead of the text to be marked up, use $I, and then include an Inline argument that represents the text you want.
 * This use case looks like this for a method {@code foo(String str, Inline... inlines)}:
 * {@code foo("$I is the String that will use the $I markup capability", Inline.bold("This"), Inline.literal("Inline"));}
 * The result becomes:
 * "**This** is the String that will use the ``Inline`` markup capability"
 *
 * Note: the Inline arguments must match the order of the $I in the text. Also, because the '$' triggers the text processing,
 * to correctly include a '$' in the processed String is to escape it with another '$'. For instance:
 * foo("The chocolate bar cost me $$1.25")
 * produces:
 * "The chocolate bar cost me $1.25"
 *
 * Too many or too few Inline arguments will cause IllegalArgumentExceptions, as will improper use of the '$' character.
 *
 * Direct instantiation of the Inline class isn't allowed, but the static methods cover the possible uses of inline markup.
 * Roles can also be used through the Inline class. See the {@code role} function for more information.
 */
public class Inline {
    private String text;

    private Inline(String text){
        this.text = text;
    }

    public String write(){
        return text;
    }

    @Override
    public String toString(){
        return text;
    }

    /**
     * Surrounds the parameter String with *'s and escapes the String if necessary. The single * corresponds to italics or
     * "emphasis" styling in reStructuredText.
     * @param str the String to be marked up
     * @return an Inline object representing the marked-up/escaped String
     */
    public static Inline italics(String str){
        return new Inline("*" + str.replaceAll("\\*", "\\*") + "*");
    }

    /**
     * Surrounds the parameter String with **'s and escapes the String if necessary. The ** corresponds to boldface or
     * "strong" styling in reStructuredText.
     * @param str the String to be marked up
     * @return an Inline object representing the marked-up/escaped String
     */
    public static Inline bold(String str){
        return new Inline("**" + str.replaceAll("\\*\\*", "\\**") + "**");
    }

    /**
     * Surrounds the parameter String with ``s and escapes the String if necessary. The `` corresponds to "literal"
     * styling in reStructuredText.
     * @param str the String to be marked up
     * @return an Inline object representing the marked-up/escaped String
     */
    public static Inline literal(String str){
        return new Inline("``" + str.replaceAll("``", "\\``") + "``");
    }

    /**
     * Surrounds the parameter String with `s, adds an underscore to the end and escapes the String if necessary.
     * The reStructuredText link syntax is given as: {@code `link-name`_} and points to either a Heading with the name "link-name"
     * or to the destination defined in the definition of "link-name".
     * @param name the name of the link
     * @return an Inline object representing the marked-up/escaped String
     * @see LinkDefinition
     */
    public static Inline link(String name){
        return new Inline("`" + name.replaceAll("`", "\\`") + "`_");
    }

    /**
     * Surrounds the parameter String with `s, adds the destination in &lt; &gt; and an underscore to the end and escapes the 
     * String if necessary. The reStructuredText link syntax is given as: {@code `link-name &lt;destination&gt;`_} It produces a
     * link with text "link-name" that points to destination.
     * @param name the name of the link
     * @param url the destination of the link
     * @return an Inline object representing the marked-up/escaped String
     */
    public static Inline link(String name, String url){
        return new Inline("`" + name.replaceAll("`", "\\`") + " <" + url + ">`_");
    }

    /**
     * Surrounds the parameter String with `s, adds an underscore to the end and escapes the String if necessary. Also 
     * creates a link definition with the name and url arguments in the Heading represented by the builder argument.
     * The reStructuredText link syntax is given as: {@code `link-name`_} and points to the destination defined in the definition
     * of "link-name"
     * @param name the name of the link
     * @param url the destination of the link
     * @param builder the Heading builder to which the definition of the link will be added
     * @return an Inline object representing the marked-up/escaped String
     * @see LinkDefinition
     */
    public static Inline link(String name, String url, Heading.Builder builder){
        builder.addDefinition(new LinkDefinition(name, url));
        return new Inline("`" + name.replaceAll("`", "\\`") + "`_");
    }

    /**
     * Surrounds the parameter String with `s, adds an underscore to the end and escapes the String if necessary. Also
     * creates a link definition with the name and url arguments in the RstFile represented by the builder argument.
     * The reStructuredText link syntax is given as: {@code `link-name`_} and points to the destination defined in the definition
     * of "link-name"
     * @param name the name of the link
     * @param url the destination of the link
     * @param builder the RstFile builder to which the definition of the link will be added
     * @return an Inline object representing the marked-up/escaped String
     * @see LinkDefinition
     */
    public static Inline link(String name, String url, RstFile.Builder builder){
        builder.addDefinition(new LinkDefinition(name, url));
        return new Inline("`" + name.replaceAll("`", "\\`") + "`_");
    }

    /**
     * Surrounds the parameter String with |'s and escapes the String if necessary. The | corresponds to substitution
     * syntax in reStructuredText. {@code |subst-name|} will be replaced by the definition of "subst-name" in the generated HTML
     * @param str the String to be marked up
     * @return an Inline object representing the marked-up/escaped String
     * @see SubstitutionDefinition
     */
    public static Inline substitution(String str){
        return new Inline("|" + str.replaceAll("\\|", "\\|") + "|");
    }

    /**
     * Surrounds the parameter String with |'s and escapes the String if necessary. Also creates a definition for the
     * parameter String with the directive paramter and places it in the Heading represented by the builder parameter.
     * The | corresponds to substitution syntax in reStructuredText. {@code |subst-name|} will be replaced by the
     * definition of "subst-name" created by this method
     * @param str the String to be substituted
     * @param directive the directive that defines the substitution
     * @param builder the Heading builder to which the definition of the substitution will be added
     * @return an Inline object representing the marked-up/escaped String
     * @see SubstitutionDefinition
     */
    public static Inline substitution(String str, Directive directive, Heading.Builder builder){
        builder.addDefinition(new SubstitutionDefinition(str, directive));
        return substitution(str);
    }

    /**
     * Surrounds the parameter String with |'s and escapes the String if necessary. Also creates a definition for the
     * parameter String with the directive parameter and places it in the RstFile represented by the builder parameter.
     * The | corresponds to substitution syntax in reStructuredText. {@code |subst-name|} will be replaced by the
     * definition of "subst-name" created by this method
     * @param str the String to be substituted
     * @param directive the directive that defines the substitution
     * @param builder the RstFile builder to which the definition of the substitution will be added
     * @return an Inline object representing the marked-up/escaped String
     * @see SubstitutionDefinition
     */
    public static Inline substitution(String str, Directive directive, RstFile.Builder builder){
        builder.addDefinition(new SubstitutionDefinition(str, directive));
        return substitution(str);
    }

    /**
     * Applies the subscript role to the parameter String and escapes the String if necessary. Role syntax in
     * reStructuredText looks like: {@code :subscript:`text`}
     * @param str the String to be marked up
     * @return an Inline object representing the marked-up/escaped String
     */
    public static Inline subscript(String str){
        return role("subscript", str);
    }

    /**
     * Applies the superscript role to the parameter String and escapes the String if necessary. Role syntax in
     * reStructuredText looks like: {@code :superscript:`text`}
     * @param str the String to be marked up
     * @return an Inline object representing the marked-up/escaped String
     */
    public static Inline superscript(String str){
        return role("superscript", str);
    }

    /**
     * Applies the parameter role to the parameter String and escapes the String if necessary. Role syntax in
     * reStructuredText looks like {@code :role-name:`text`}. This method is useful for roles defined in a reStructuredText
     * parser but not implemented in this library, or for invoking custom-defined roles.
     * @param str the String to be marked up
     * @return an Inline object representing the marked-up/escaped String
     * @see <a href="http://docutils.sourceforge.net/docs/ref/rst/roles.html"></a>
     */
    public static Inline role(String role, String str){
        return new Inline(":" + role + ":`" + str.replaceAll("`", "\\`") + "`");
    }

}
