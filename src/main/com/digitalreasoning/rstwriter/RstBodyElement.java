package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.*;

/**
 * Created by creynolds on 7/16/15.
 */
public interface RstBodyElement extends RstElement{

    static UnmarkedList unmarkedList(String str){
        return new UnmarkedList(str);
    }

    static BulletList bulletList(String str){
        return new BulletList(str);
    }

    static NumberedList numberedList(String str){ return new NumberedList(str); }

    static UnmarkedList unmarkedList(){
        return new UnmarkedList("");
    }

    static BulletList bulletList(){
        return new BulletList("");
    }

    static NumberedList numberedList(){ return new NumberedList(""); }

    static AlphabeticList alphabeticList(String str){ return new AlphabeticList(str); }

    static AlphabeticList alphabeticList(){ return new AlphabeticList(""); }

    static RomanNumeralList romanNumeralList(String str){ return new RomanNumeralList(str); }

    static RomanNumeralList romanNumeralList(){ return new RomanNumeralList(""); }

    static DefinitionList definitionList(){ return new DefinitionList(); }

    static FieldList fieldList(){ return new FieldList(); }

    static OptionList optionList(){ return new OptionList(); }

    static Table table(String[][] str){ return new Table(str); }
    
    static Table.Builder tableBuilder() { return Table.builder(); }

    static CodeBlock codeBlock(String str){ return new CodeBlock(str); }
    
}
