package com.digitalreasoning.rstwriter;

import static org.junit.Assert.*;

import com.digitalreasoning.rstwriter.bodyelement.*;
import org.junit.Test;
/**
 * Created by creynolds on 7/14/15.
 */
public class AutoListTest {

    private static final String INDENT = "    ";
    @Test
    public void listParseTest(){
         String text0 = "element0\nelement1\nelement2";
         String text1 = "this\nlist\nhas\na blank\non the end\n";
         String text2 = "\nthis\nlist\nhas\na blank\nat the beginning";
        
        String result0 = "* element0\n* element1\n* element2\n";
        BulletList parseBullet = RstBodyElement.bulletList(text0);
        assertEquals("parseBullet", result0, parseBullet.write());

        String result1 = "* this\n* list\n* has\n* a blank\n* on the end\n";
        parseBullet = RstBodyElement.bulletList(text1);
        assertEquals("parseBullet", result1, parseBullet.write());

        String result2 = "* this\n* list\n* has\n* a blank\n* at the beginning\n";
        parseBullet = RstBodyElement.bulletList(text2);
        assertEquals("parseBullet", result2, parseBullet.write());

        result0 = result0.replaceAll("\\*", "#.");
        result1 = result1.replaceAll("\\*", "#.");
        result2 = result2.replaceAll("\\*", "#.");

        NumberedList parseNumber = RstBodyElement.numberedList(text0);
        assertEquals("parseNumber", result0, parseNumber.write());
        parseNumber = RstBodyElement.numberedList(text1);
        assertEquals("parseNumber", result1, parseNumber.write());
        parseNumber = RstBodyElement.numberedList(text2);
        assertEquals("parseNumber", result2, parseNumber.write());

        result0 = result0.replaceAll("#\\.", "|");
        result1 = result1.replaceAll("#\\.", "|");
        result2 = result2.replaceAll("#\\.", "|");

        LineBlock parseBlock = RstBodyElement.lineBlock(text0);
        assertEquals("parseBlock", result0, parseBlock.write());
        parseBlock = RstBodyElement.lineBlock(text1);
        assertEquals("parseBlock", result1, parseBlock.write());
        parseBlock = RstBodyElement.lineBlock(text2);
        assertEquals("parseBlock", result2, parseBlock.write());
    }

    @Test
    public void listMethodTest(){
        String baseList = "parse\nthis\nlist";
        BulletList bullets = RstBodyElement.bulletList(baseList).addItem("element0").addItem("element1");
        NumberedList numbers = RstBodyElement.numberedList(baseList).addItem("element0").addItem("element1");
        LineBlock block = RstBodyElement.lineBlock(baseList).addLine("element0").addLine("element1");
        String result = "% parse\n% this\n% list\n% element0\n% element1\n";

        assertEquals("addItems", result.replaceAll("%", "*"), bullets.write());
        assertEquals("addItems", result.replaceAll("%", "#."), numbers.write());
        assertEquals("addItems", result.replaceAll("%", "|"), block.write());

        String subList = "\nsublist\ntesting";
        bullets = RstBodyElement.bulletList(baseList).addSubList(subList).addItem("element0").addItem("element1");
        numbers = RstBodyElement.numberedList(baseList).addSubList(subList).addItem("element0").addItem("element1");
        result = "% parse\n% this\n% list\n\n"+ INDENT +"% sublist\n"+ INDENT +"% testing\n\n% element0\n% element1\n";
        assertEquals("sublist middle", result.replaceAll("%", "*"), bullets.write());
        assertEquals("sublist middle", result.replaceAll("%", "#."), numbers.write());

        bullets = RstBodyElement.bulletList(baseList).addItem("element0").addItem("element1").addSubList(subList);
        numbers = RstBodyElement.numberedList(baseList).addItem("element0").addItem("element1").addSubList(subList);
        result = "% parse\n% this\n% list\n% element0\n% element1\n\n"+ INDENT +"% sublist\n"+ INDENT +"% testing\n\n";
        assertEquals("sublist end", result.replaceAll("%", "*"), bullets.write());
        assertEquals("sublist end", result.replaceAll("%", "#."), numbers.write());

        bullets = RstBodyElement.bulletList("").addSubList(subList).addItem("element0").addItem("element1");
        numbers = RstBodyElement.numberedList("").addSubList(subList).addItem("element0").addItem("element1");
        result = "\n"+ INDENT +"% sublist\n"+ INDENT +"% testing\n\n% element0\n% element1\n";
        assertEquals("sublist beginning", result.replaceAll("%", "*"), bullets.write());
        assertEquals("sublist beginning", result.replaceAll("%", "#."), numbers.write());
    }

    @Test
    public void interchangeableListTest(){
        String bulletList = "bullet\nlistB";
        String numberedList = "numbered\nlistN";
        String alphaList = "alpha\nlistA";

        BulletList bullets = RstBodyElement.bulletList(bulletList);
        NumberedList numbers = RstBodyElement.numberedList(numberedList);
        bullets.addSubList(numbers).addItem("added");
        String result = "* bullet\n* listB\n\n"+ INDENT +"#. numbered\n"+ INDENT +"#. listN\n\n* added\n";
        assertEquals("Bullets with numbers", result, bullets.write());

        bullets = RstBodyElement.bulletList(bulletList);
        AlphabeticList alpha = RstBodyElement.alphabeticList(alphaList);
        bullets.addSubList(alpha).addItem("added");
        result = "* bullet\n* listB\n\n"+ INDENT +"A. alpha\n"+ INDENT +"#. listA\n\n* added\n";
        assertEquals("Bullets with alpha", result, bullets.write());

        bullets = RstBodyElement.bulletList(bulletList);
        numbers = RstBodyElement.numberedList(numberedList);
        alpha = RstBodyElement.alphabeticList(alphaList);

        alpha.addSubList(numbers.addSubList(bullets).addItem("addedN")).addItem("addedA");
        result = "A. alpha\n#. listA\n\n"+ INDENT +"#. numbered\n"+ INDENT +"#. listN\n"+ INDENT +"\n"+ INDENT + INDENT +"* bullet\n"+ INDENT + INDENT +"* listB\n"+ INDENT +"\n"+
                 INDENT +"#. addedN\n\n#. addedA\n";
        assertEquals("all three", result, alpha.write());
    }

    @Test
    public void emptyListTest(){
        BulletList bullets = RstBodyElement.bulletList("");
        assertEquals("Empty list", "", bullets.write());

        NumberedList numbers = RstBodyElement.numberedList("Numbered\nlist").addSubList(bullets).addItem("test");
        String result = "#. Numbered\n#. list\n#. test\n";
        assertEquals("empty sublist", result, numbers.write());
    }

    @Test
    public void formattingTest(){
        BulletList bullet = RstBodyElement.bulletList("working\nwith items\nthat span multiple lines");
        bullet.addItem("A standard\nvery long item").addItem("\nbegins with newline").addItem("ends with newline\n")
                .addItem("has multiple consecutive\n\nnewlines").addItem("has weird\n  spacing").addItem("\n");
        String bulletResult = "* working\n* with items\n* that span multiple lines"
                + "\n* A standard\n\n  very long item"
                + "\n* begins with newline"
                + "\n* ends with newline"
                + "\n* has multiple consecutive\n\n  newlines"
                + "\n* has weird\n\n  spacing"
                + "\n* \n";
        assertEquals("Bad formatting", bulletResult, bullet.write());

        LineBlock block = RstBodyElement.lineBlock("working\nwith items\nthat span multiple lines");
        block.addLine("A standard\nvery long item").addLine("\nbegins with newline").addLine("ends with newline\n")
                .addLine("has multiple consecutive\n\nnewlines").addLine("has weird\n  spacing").addLine("\n");
        String blockResult = "| working\n| with items\n| that span multiple lines"
                + "\n| A standard\n| very long item"
                + "\n| \n| begins with newline"
                + "\n| ends with newline\n| "
                + "\n| has multiple consecutive\n| \n| newlines"
                + "\n| has weird\n|   spacing"
                + "\n| \n| \n";
        assertEquals("Bad block list formatting", blockResult, block.write());

        String numberedresult = "#. numbered\n#. list\n#. testing\n\n%%%\n#. nesting\n";

        bulletResult = INDENT + bulletResult.replaceAll("\n", "\n"+ INDENT);
        bulletResult = bulletResult.substring(0, bulletResult.length()-INDENT.length());
        NumberedList numberedList = RstBodyElement.numberedList("numbered\nlist\ntesting");
        numberedList.addSubList(bullet).addItem("nesting");
        assertEquals("Failed nesting bullets",
                numberedresult.replace("%%%", bulletResult), numberedList.write());
    }

    @Test
    public void auxiliaryListTest(){
        String items = "item0\nitem1\nitem2\nitem3";
        String[] alpha = new AlphabeticList(items).write().split("\n");
        String[] alpha2 = new AlphabeticList(items, false).write().split("\n");
        String[] rnl = new RomanNumeralList(items).write().split("\n");
        String[] rnl2 = new RomanNumeralList(items, false).write().split("\n");

        for(int i = 0; i<4; i++){
            String str = ". item" + i;
            assertEquals((i == 0 ? 'A' : '#') + str, alpha[i]);
            assertEquals((i == 0 ? 'a' : '#') + str, alpha2[i]);
            assertEquals((i == 0 ? 'I' : '#') + str, rnl[i]);
            assertEquals((i == 0 ? 'i' : '#') + str, rnl2[i]);
        }
    }

    @Test
    public void singleItemTest(){
        BulletList list = RstBodyElement.bulletList().addItem("item");
        String result = "* item\n";
        assertEquals("Failed singleItem", result, list.write());
        BulletList subList = new BulletList("single");
        list.addSubList(subList);
        result = "* item\n\n" + INDENT + "* single\n\n.. Formatting\n";
        assertEquals("Failed sublist", result, list.write());
    }
}
