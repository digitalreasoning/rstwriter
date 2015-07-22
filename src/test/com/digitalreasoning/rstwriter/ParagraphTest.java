package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.Paragraph;
import static com.digitalreasoning.rstwriter.Inline.*;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by creynolds on 7/13/15.
 */
public class ParagraphTest {
    
    @Test
    public void simpleTest(){
        final String text0 = "Test text text test";
        final String text1 = "Mary had a \nlittle lamb";
        final String text2 = "";
        Paragraph p0 = new Paragraph("").addText(text0);
        assertEquals(text0 +"\n", p0.write());

        Paragraph p1 = new Paragraph(text1);
        assertEquals(text1+"\n", p1.write());

        Paragraph p2 = new Paragraph("").addText(text2);
        assertEquals(text2+"\n", p2.write());

        Paragraph p3 = new Paragraph(text0).addText(text1).addText(text2);
        assertEquals(text0+text1+text2+"\n", p3.write());

        Paragraph p4 = new Paragraph(text1).addText("\n\n").addText(text0);
        assertEquals(text1 + "\n\n" + text0 + "\n", p4.write());
    }

    @Test
    public void inlineTest(){
        String[] texts = {
                "inline $I number 1", "inline test number $I", "inline t$It number 3", "$I test number 4",
                "inline $It number 5", "inli$I test number 6"
        };

        String[] results = {
                "inline # number 1\n", "inline test number #\n", "inline t\\ #\\ t number 3\n", "# test number 4\n",
                "inline #\\ t number 5\n", "inli\\ # test number 6\n"
        };
        Inline[] inlines = {bold("inline"), italics("inline"), subscript("inline"), substitution("inline"),
                role("role", "inline")};
        String[] replacement = {"**inline**", "*inline*", ":subscript:`inline`", "|inline|", ":role:`inline`"};
        for(int i = 0; i<texts.length; i++){
            for(int j = 0; j<inlines.length; j++) {
                String result = results[i].replaceAll("#", replacement[j]);
                assertEquals(inlines[j].write(), result, new Paragraph(texts[i], inlines[j]).write());
            }
        }

        assertEquals("Escape test", "escape $ test\n", new Paragraph("escape $$ test").write());
        assertEquals("Escape test", "escape $$ test\n", new Paragraph("escape $$$$ test").write());
        assertEquals("Escape test", "escape $I test\n", new Paragraph("escape $$I test").write());
    }

    @Test
    public void inlineErrorTest(){
        try{
            new Paragraph("test $I");
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new Paragraph("test $I", bold(""), bold(""));
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new Paragraph("test $$", bold(""));
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new Paragraph("test $I $I", bold(""), bold(""), bold(""));
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new Paragraph("test $$I $$I", bold(""), bold(""));
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new Paragraph("", bold(""));
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new Paragraph("abcd$");
            fail();
        }catch(IllegalArgumentException e){}
    }

}
