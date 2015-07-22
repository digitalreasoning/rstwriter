package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.LinkDefinition;
import com.digitalreasoning.rstwriter.bodyelement.SubstitutionDefinition;

/**
 * Created by creynolds on 7/17/15.
 */
public class Inline {
    private String text;

    public Inline(String text){
        this.text = text;
    }

    public String write(){
        return text;
    }

    @Override
    public String toString(){
        return text;
    }

    public static Inline italics(String str){
        return new Inline("*" + str.replaceAll("\\*", "\\*") + "*");
    }

    public static Inline bold(String str){
        return new Inline("**" + str.replaceAll("\\*\\*", "\\**") + "**");
    }

    public static Inline literal(String str){
        return new Inline("``" + str.replaceAll("``", "\\``") + "``");
    }

    public static Inline link(String str){
        return new Inline("`" + str.replaceAll("`", "\\`") + "`_");
    }

    public static Inline link(String str, String url){
        return new Inline("`" + str.replaceAll("`", "\\`") + " <" + url + ">`_");
    }

    public static Inline link(String str, String url, Heading.Builder builder){
        builder.addDefinition(new LinkDefinition(str, url));
        return new Inline("`" + str.replaceAll("`", "\\`") + "`_");
    }

    public static Inline link(String str, String url, RstFile.Builder builder){
        builder.addDefinition(new LinkDefinition(str, url));
        return new Inline("`" + str.replaceAll("`", "\\`") + "`_");
    }

    public static Inline substitution(String str){
        return new Inline("|" + str.replaceAll("\\|", "\\|") + "|");
    }

    public static Inline substitution(String str, Directive d, Heading.Builder builder){
        builder.addDefinition(new SubstitutionDefinition(str, d));
        return substitution(str);
    }

    public static Inline substitution(String str, Directive d, RstFile.Builder builder){
        builder.addDefinition(new SubstitutionDefinition(str, d));
        return substitution(str);
    }

    public static Inline subscript(String str){
        return role("subscript", str);
    }

    public static Inline superscript(String str){
        return role("superscript", str);
    }

    public static Inline role(String role, String str){
        return new Inline(":" + role + ":`" + str.replaceAll("`", "\\`") + "`");
    }

}
