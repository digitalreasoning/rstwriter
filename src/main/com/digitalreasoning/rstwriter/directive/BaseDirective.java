package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;
import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.bodyelement.Paragraph;
import com.digitalreasoning.rstwriter.RstBodyElement;
import com.digitalreasoning.rstwriter.bodyelement.FieldList;

/**
 * Created by creynolds on 7/17/15.
 */
public class BaseDirective implements Directive {
    private String directiveType;
    private String arguments;
    private FieldList options;
    private String content;
    private static final String INDENT = "    ";

    public BaseDirective(String directiveType){
        this.directiveType = directiveType;
        arguments = "";
        options = new FieldList();
        content = "";
    }

    public BaseDirective addArgument(String arg){
        arguments += arg;
        return this;
    }

    public BaseDirective addOption(String option, String value, Inline... inlines){
        options.addItem(option, new Paragraph(value, inlines));
        return this;
    }

    public BaseDirective addOption(String option, RstBodyElement value){
        options.addItem(option, value);
        return this;
    }

    public BaseDirective setOptions(FieldList options){
        this.options = options;
        return this;
    }

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
