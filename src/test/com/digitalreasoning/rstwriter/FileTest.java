package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.AlphabeticList;
import com.digitalreasoning.rstwriter.bodyelement.BulletList;
import com.digitalreasoning.rstwriter.bodyelement.FieldList;
import com.digitalreasoning.rstwriter.bodyelement.Table;
import com.digitalreasoning.rstwriter.directive.Admonition;
import com.digitalreasoning.rstwriter.directive.Contents;
import com.digitalreasoning.rstwriter.directive.Image;
import com.digitalreasoning.rstwriter.directive.Replace;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by creynolds on 7/21/15.
 */
public class FileTest {

    @Test
    public void simpleFileTest(){
        RstFile.Builder file = RstFile.builder("tmp");
        file.addParagraph("Paragraph paragraph");
        file.addDirective(new Contents().setDepth(1));
        Heading h = Heading.builder("name").addParagraph("Paragraph")
                .addBodyElement(RstBodyElement.romanNumeralList("item\nitem2\nitem3"))
                .addLinkTarget("link").build();
        file.addHeading(h);
        String str = file.build().write();
        String[] lines = str.split("\n");
        assertTrue(lines[0].startsWith("P"));
        assertTrue(lines[1].equals(""));
        assertEquals(lines[2], ".. contents:: ");

        assertEquals(lines[5], ".. _link: ");
        assertTrue(lines[7].startsWith("#") && lines[7].endsWith("#"));
        assertEquals(lines[8], "name");
        assertTrue(lines[9].startsWith("#"));

        assertEquals(lines[11], "Paragraph");
        assertTrue(lines[13].startsWith("I. "));
        assertEquals(lines[15], "#. item3");

    }

    @Test
    public void writtenFileTest(){
        String filename = "bigfile";
        RstFile.Builder f = RstFile.builder(filename);
        Heading.Builder heading = Heading.builder("Heading");
        Heading.Builder top2 = Heading.builder("Top 2");
        f.addParagraph("Opening content. No big\ndeal");
        f.addBodyElement(
                RstBodyElement.bulletList("bullets\nbullets")
                        .addSubList(RstBodyElement.alphabeticList("alphabet\nalphabet\nalphabet3"))
                .addItem("bullets after"));

        String longParagraph = "Here's a paragraph. It has a lot of text but no line breaks. However, it $I have a little bit of $I markup. It should pretty $I test the generator.";
        heading.addParagraph(longParagraph, Inline.bold("does"),
                Inline.literal("inline"), Inline.substitution("thr", new Replace("thoroughly"), heading));
        BulletList bullets = RstBodyElement.bulletList().addItem("maybe we should")
                .addItem("have $I bullets", Inline.superscript("some")).addItem("with inline too");
        heading.addBodyElement(bullets);
        Heading.Builder subHeading = Heading.builder("subheading");
        subHeading.addDirective(new Image("picture.jpeg").setAlignment(Image.Alignment.LEFT)
                .setWidth(200).setScale(40).setAlternateText("text"));
        Heading.Builder subsub = Heading.builder("subsub");
        subsub.addParagraph("We'll add an inline $I in here", Inline.link("link", "destination.com", subsub));
        subsub.openSubHeading("subsubsub").openSubHeading("quadruple").addLinkTarget("triple").addLinkTarget("the")
                .addLinkTarget("fun").addParagraph("Here's some content").closeSubHeading().addLinkTarget("subsubsub link")
                .closeSubHeading();
        subHeading.addSubHeading(subsub.build()).openSubHeading("second subsub")
                .addBodyElement(RstBodyElement.codeBlock("Added a code block").addLine("with a new line!")
                        .addCode("See how this custom line break we\nadded does"))
                .addBodyElement(RstBodyElement.unmarkedList("And for\ngood measure\nan unmarked").addItem("list"))
        .closeSubHeading();
        heading.addSubHeading(subHeading.build());

        FieldList fieldList = RstBodyElement.fieldList()
                .addItem("field", "list").addItem("test", "cause why not?");
        Table t = Table.builder().addCell("a").addCell("b").addCell("c").nextRow()
                .addCell("z", Table.BORDER_BELOW).addCell("y").addCell(new Admonition(Admonition.Type.NOTE, "NOTE"))
                .build();
        fieldList.addItem("table", t).addItem("another", "field");
        top2.addBodyElement(fieldList).addTransition().addParagraph("That's a pretty $I test, I'd say.",
                Inline.role("custom", "good"));

        f.addHeading(heading.build()).addHeading(top2.build());

        try {
            String outDir = "out/test/";
            FileWriter file = new FileWriter(f.build());
            file.writeTo(new File(outDir));
            diff(outDir + filename + ".rst", "src/test/sample-rst");
            new File(outDir + filename + ".rst").delete();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void diff(String file1, String file2){
        try{
            Scanner f1 = new Scanner(new File(file1));
            Scanner f2 = new Scanner(new File(file2));
            boolean identical = true;
            while(f1.hasNextLine() && f2.hasNextLine()){
                String line1 = f1.nextLine();
                String line2 = f2.nextLine();
                if(!line1.equals(line2)){
                    System.out.println(file1 + ":\t" + line1);
                    System.out.println(file2 + ":\t" + line2);
                    System.out.println();
                    identical = false;
                }
            }
            while(f1.hasNextLine()){
                System.out.println(file1 + ":\t" + f1.nextLine());
                identical = false;
            }
            while(f2.hasNextLine()){
                System.out.println(file1 + ":\t" + f2.nextLine());
                identical = false;
            }
            if(!identical)
                fail("Files not identical");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
