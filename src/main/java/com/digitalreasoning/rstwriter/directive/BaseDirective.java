package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;
import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.bodyelement.Paragraph;
import com.digitalreasoning.rstwriter.RstBodyElement;
import com.digitalreasoning.rstwriter.bodyelement.FieldList;

/**
 * The BaseDirective class is a template for the creation of a custom directive class to use alongside this library. It
 * provides capabilities to manage the various requirements of directives and format them properly, allowing custom
 * directive classes to forward their information without special formatting needs. Using the BaseDirective class is
 * unnecessary to properly create a directive class. The {@link Directive} interface is all that is necessary. Directive
 * structure in reStructuredText looks like this:
 * {@code
 * .. directiveType:: arguments on the first line
 *    :option: def
 *    :option: def
 *    ... can't have blank lines
 *
 *    content begins after a blank line + indent
 *    content continues
 *
 *    content continues provided it's indented, even after a blank line
 *
 * ends upon a non-indented line
 * }
 */
public class BaseDirective implements Directive {
    private String directiveType;
    private String arguments;
    private FieldList options;
    private String content;
    private static final String INDENT = "    ";

    /**
     * Creates a directive with the name provided by the parameter
     * @param directiveType the name of the directive
     */
    public BaseDirective(String directiveType){
        this.directiveType = directiveType;
        arguments = "";
        options = new FieldList();
        content = "";
    }

    /**
     * Adds an argument to the directive
     * @param arg the argument to be added
     * @return this BaseDirective with the argument added
     */
    public BaseDirective addArgument(String arg){
        arguments += arg;
        return this;
    }

    /**
     * Adds an option to this directive
     * @param option the option name
     * @param value the value assigned to the option (will be processed for inline markup)
     * @param inlines optional arguments used to specify inline markup in the value
     * @return this BaseDirective with the option added
     */
    public BaseDirective addOption(String option, String value, Inline... inlines){
        options.addItem(option, new Paragraph(value, inlines));
        return this;
    }

    /**
     * Adds an option to this directive
     * @param option the option name
     * @param value the value assigned to the option
     * @return this BaseDirective with the option added
     */
    public BaseDirective addOption(String option, RstBodyElement value){
        options.addItem(option, value);
        return this;
    }

    /**
     * Removes any pre-existing options and replaces them with the given FieldList
     * @param options the list of options to assign to the directive
     * @return this BaseDirective with options set to the given list
     */
    public BaseDirective setOptions(FieldList options){
        this.options = options;
        return this;
    }

    /**
     * Adds the specified text to the end of the content section of this directive
     * @param str the text to be added (will be processed for inline markup)
     * @param inlines optional arguments specifying inline markup in the text
     * @return this BaseDirective with the text added to the content
     */
    public BaseDirective addContent(String str, Inline... inlines){
        return addContent(new Paragraph(str, inlines));
    }
    /**
     * Adds the element to the end of the content section of this directive
     * @param element the element to be added to the content
     * @return this BaseDirective with the element added to the content
     */
    public BaseDirective addContent(RstBodyElement element){
        if(content.length() != 0){
            while(!content.endsWith("\n\n")){
                content += "\n";
            }
        }
        content += element.write() + "\n";
        return this;
    }

    @Override
    public String write(){
        return ".. " + directiveType + ":: " + writeBlock();
    }

    private String writeBlock(){
        String text = "";
        String[] args = arguments.split("\n");
        if(args.length == 1){
            text += arguments + "\n";
        }
        else {
            for (String arg : args) {
                if (!arg.equals(""))
                    text += INDENT + arg + "\n";
            }
        }
        String[] optns = options.write().split("\n");
        for(String opt : optns){
            if(!opt.equals(""))
                text += INDENT + opt + "\n";
        }
        if(!content.trim().equals("")) {
            text += "\n";
            String[] contents = content.split("\n");
            for (String con : contents) {
                text += INDENT + con + "\n";
            }
        }
        return text;
    }

    protected static String escapeString(String str){
        return str.replaceAll("\\$", "\\$\\$");
    }
}
