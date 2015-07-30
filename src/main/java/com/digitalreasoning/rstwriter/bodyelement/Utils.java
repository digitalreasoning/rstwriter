package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;

/**
 * The Utils class provides two constants useful in determining output. Namely, the indent standard and the escape character.
 */
public class Utils
{
    private Utils(){}

    /**
     * The indent standard used in all classes. 4 spaces is the standard.
     */
    public static final String INDENT = "    ";

    /**
     * The escape character for inline markup.
     */
    public static final char ESCAPE = '$';

    protected static String inlineParse(String sequence, Inline... inlines){
        String build = "";
        char[] stream = sequence.toCharArray();
        boolean escapeMode = false;
        int inlineIndex = 0;
        for(int i = 0; i<stream.length; i++){
            if(escapeMode){
                if(stream[i] == 'I'){
                    if(inlines == null || inlineIndex >= inlines.length){
                        throw new IllegalArgumentException("Too few Inline arguments given");
                    }
                    build += inlines[inlineIndex].write();
                    inlineIndex++;
                    if(i < stream.length-1 && stream[i+1] != ' '){
                        build += "\\ ";
                    }
                    escapeMode = false;
                }
                else{
                    throw new IllegalArgumentException("Unrecognized escape sequence: " + ESCAPE + stream[i]);
                }
            }
            else{
                if(stream[i] == ESCAPE){
                    if(i == stream.length-1){
                        throw new IllegalArgumentException("Invalid escape at the end of the string: " + sequence);
                    }
                    else if(i < stream.length-1 && stream[i+1] == ESCAPE){
                        build += ESCAPE;
                        i++;
                        continue;
                    }
                    else if(i>0 && stream[i-1]!= ' '){
                        build += "\\ ";
                    }
                    escapeMode = true;
                }
                else{
                    build += stream[i];
                }
            }
        }
        if(inlines != null && inlineIndex != inlines.length){
            throw new IllegalArgumentException("Too many inline arguments given");
        }
        return build;
    }


}
