package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;

/**
 * Created by creynolds on 7/14/15.
 */
class Utils
{
    private Utils(){}

    public static final String INDENT = "    ";

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
