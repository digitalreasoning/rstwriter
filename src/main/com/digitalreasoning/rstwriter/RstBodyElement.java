package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.*;

/**
 * The RstBodyElement interface groups together all body elements defined in reStructuredText. It is an extension
 * of the RstElement interface because the body elements can exist alone inside a file. This interface also defines static
 * method for easy construction of many body elements.
 */
public interface RstBodyElement extends RstElement{

    /**
     * Returns an empty literal-line-break block
     * @return empty literal-line-break list
     */
    static UnmarkedList unmarkedList(){
        return new UnmarkedList("");
    }

    /**
     * Returns a literal-line-break block based on the parameter String
     * @param str a block of '\n' separated lines
     * @return a literal-line-break block with line breaks as defined in the parameter
     */
    static UnmarkedList unmarkedList(String str){
        return new UnmarkedList(str);
    }

    /**
     * Returns an empty bullet list
     * @return empty bullet list
     */
    static BulletList bulletList(){
        return new BulletList("");
    }

    /**
     * Returns a bullet list based on the parameter String. A '\n' character separates the bullet points
     * @param str bullet points separated by '\n'
     * @return a bullet list with bullet points as defined in the parameter
     */
    static BulletList bulletList(String str){
        return new BulletList(str);
    }

    /**
     * Returns an empty automatically numbered list
     * @return empty numbered list
     */
    static NumberedList numberedList(){ return new NumberedList(""); }

    /**
     * Returns a numbered list based on the parameter String. A '\n' character separates the items
     * @param str items separated by '\n'
     * @return a numbered list with items as defined in the parameter
     */
    static NumberedList numberedList(String str){ return new NumberedList(str); }

    /**
     * Returns an empty automatically numbered list
     * @return empty numbered list
     */
    static AlphabeticList alphabeticList(){ return new AlphabeticList(""); }

    /**
     * Returns a numbered list based on the parameter String. A '\n' character separates the items
     * @param str items separated by '\n'
     * @return a numbered list with items as defined in the parameter
     */
    static AlphabeticList alphabeticList(String str){ return new AlphabeticList(str); }

    /**
     * Returns an empty automatically numbered list
     * @return empty numbered list
     */
    static RomanNumeralList romanNumeralList(){ return new RomanNumeralList(""); }

    /**
     * Returns a numbered list based on the parameter String. A '\n' character separates the items
     * @param str items separated by '\n'
     * @return a numbered list with items as defined in the parameter
     */
    static RomanNumeralList romanNumeralList(String str){ return new RomanNumeralList(str); }

    /**
     * Returns an empty definition list
     * @return empty definition list
     */
    static DefinitionList definitionList(){ return new DefinitionList(); }

    /**
     * Returns an empty field list
     * @return empty field list
     */
    static FieldList fieldList(){ return new FieldList(); }

    /**
     * Returns an empty option list
     * @return empty option list
     */
    static OptionList optionList(){ return new OptionList(); }

    /**
     * Returns a table based on the parameter array. The array must be rectangular and have at least 2 columns. The table
     * is interpreted as array[row][col] with (0,0) at the top left; also, all borders are filled in.
     * @param str the array representing the desired table
     * @return an initialized Table object
     * @throws IllegalArgumentException for malformed tables
     * @see Table for restrictions on the array
     */
    static Table table(String[][] str){ return new Table(str); }

    /**
     * returns a new Table Builder
     * @return a new Table Builder
     */
    static Table.Builder tableBuilder() { return Table.builder(); }

    /**
     * returns a code block construct with the parameter String representing the code
     * @param str the code inside the code block
     * @return a code block containing the parameter
     */
    static CodeBlock codeBlock(String str){ return new CodeBlock(str); }
    
}
