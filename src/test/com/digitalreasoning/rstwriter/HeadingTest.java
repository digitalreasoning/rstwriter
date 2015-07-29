package com.digitalreasoning.rstwriter;


import com.digitalreasoning.rstwriter.bodyelement.LinkDefinition;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by creynolds on 7/15/15.
 */
public class HeadingTest {

    @Test
    public void borderTest(){
        Heading.Builder h0 = Heading.builder("Heading0");
        Heading.Builder h1 = Heading.builder("Heading1");
        Heading.Builder h2 = Heading.builder("Heading2");
        Heading.Builder h3 = Heading.builder("Heading3");
        Heading.Builder h4 = Heading.builder("Heading4");

        Heading h = h0.addSubHeading(h1.addSubHeading(h2.addSubHeading(
                h3.addSubHeading(h4.build()).build()).build()).build()).build();

        String result = "########\nHeading0\n########\n\n********\nHeading1\n********\n\nHeading2\n========\n\n" +
                "Heading3\n--------\n\nHeading4\n^^^^^^^^\n\n";
        assertEquals("Basic Border fail", result, h.write());

         h0 = Heading.builder("Heading0");
         h1 = Heading.builder("Heading1");
         h2 = Heading.builder("Heading2");
         h3 = Heading.builder("Heading3");
         h4 = Heading.builder("Heading4");

        h = h0.addSubHeading(h1.addSubHeading(h2.build()).build())
                .addSubHeading(h3.addSubHeading(h4.build()).build()).build();
        result = "########\nHeading0\n########\n\n********\nHeading1\n********\n\nHeading2\n========\n\n"+
                "********\nHeading3\n********\n\nHeading4\n========\n\n";
        assertEquals("Tree-like border fail", result, h.write());

        h0 = Heading.builder("Heading0");
        h1 = Heading.builder("Heading1");
        h2 = Heading.builder("Heading2");
        h3 = Heading.builder("Heading3");
        h4 = Heading.builder("Heading4");
        h = h0.addSubHeading(h1.addSubHeading(h2.addSubHeading(h3.build()).build()).build())
                .addSubHeading(h4.build()).build();
        result  = "########\nHeading0\n########\n\n********\nHeading1\n********\n\nHeading2\n========\n\n" +
                "Heading3\n--------\n\n********\nHeading4\n********\n\n";
        assertEquals("Asymmetric border fail", result, h.write());
    }

    @Test
    public void contentTest(){
        Heading h = Heading.builder("TopHeading").addParagraph("Intro for this heading").addParagraph("Second paragraph")
                .openSubHeading("First subHeading")
                    .addParagraph("There's information here")
                    .addBodyElement(RstBodyElement.bulletList("point1\npoint2\npoint3"))
                .closeSubHeading().openSubHeading("Second subHeading")
                    .openSubHeading("Sub subHeading")
                        .addParagraph("Some detailed info")
                    .closeSubHeading()
                .closeSubHeading().build();
        String result = "##########\nTopHeading\n##########\n\n"
                + "Intro for this heading\n\nSecond paragraph\n\n"
                    + "****************\nFirst subHeading\n****************\n\n"
                        + "There's information here\n\n* point1\n* point2\n* point3\n\n"
                    + "*****************\nSecond subHeading\n*****************\n\n"
                        + "Sub subHeading\n==============\n\n"
                            + "Some detailed info\n\n";
        assertEquals("content failed", result, h.write());
    }

    @Test
    public void errorTest(){
        try{
            Heading.Builder h = Heading.builder("name");
            h.openSubHeading("name");
            h.build();
            fail("Unclosed subheading fail");
        }catch(IllegalStateException e){
        }

        try{
            Heading.builder("name").closeSubHeading();
            fail("Unopened subheading fail");
        }catch(IllegalStateException e){
        }

        try{
            Heading.builder("name").openSubHeading("heading").openSubHeading("heading").closeSubHeading().build();
            fail("unclosed subheading, multiple opens");
        }catch(IllegalStateException e){
        }
    }

    @Test
    public void linkTargetTest(){
        Heading.Builder builder = Heading.builder("Heading").addParagraph("Paragraph").addLinkTarget("target");
        String[] lines = builder.build().write().split("\n");
        assertEquals("1", lines[0], ".. _target: ");
        assertEquals("2", lines[1], "");
        assertTrue("3",lines[2].startsWith("#"));

        builder.addLinkTarget("other").openSubHeading("heading").addLinkTarget("inner").closeSubHeading();
        lines = builder.build().write().split("\n");
        assertEquals("4", lines[0], ".. _target: ");
        assertEquals("5", lines[1], ".. _other: ");
        assertEquals("6", lines[2], "");
        assertTrue("7", lines[3].startsWith("#"));
        assertEquals("8", lines[9], ".. _inner: ");
        assertTrue("9", lines.length == 14); //there are 14 newLine characters, but the last two only produce 1 element in split
    }

    @Test
    public void definitionTest(){
        Heading.Builder builder = Heading.builder("Heading").addParagraph("paragraph")
                .addDefinition(new LinkDefinition("target", "dest"));
        String[] lines = builder.build().write().split("\n");
        assertTrue(lines[0].startsWith("#"));
        assertEquals("1def target", ".. _target: dest", lines[6]);

        builder.openSubHeading("subheading").addDefinition(new LinkDefinition("second", "destination")).closeSubHeading();
        lines = builder.build().write().split("\n");
        assertTrue(lines[0].startsWith("#"));
        assertEquals("def second", ".. _second: destination", lines[10]);
        assertEquals("2def target", ".. _target: dest", lines[12]);
    }
}
