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

        UnmarkedList parseUnmarked = RstBodyElement.unmarkedList(text0);
        assertEquals("parseUnmarked", result0, parseUnmarked.write());
        parseUnmarked = RstBodyElement.unmarkedList(text1);
        assertEquals("parseUnmarked", result1, parseUnmarked.write());
        parseUnmarked = RstBodyElement.unmarkedList(text2);
        assertEquals("parseUnmarked", result2, parseUnmarked.write());
    }

    @Test
    public void listMethodTest(){
        String baseList = "parse\nthis\nlist";
        BulletList bullets = RstBodyElement.bulletList(baseList).addItem("element0").addItem("element1");
        NumberedList numbers = RstBodyElement.numberedList(baseList).addItem("element0").addItem("element1");
        UnmarkedList unmarked = RstBodyElement.unmarkedList(baseList).addItem("element0").addItem("element1");
        String result = "% parse\n% this\n% list\n% element0\n% element1\n";

        assertEquals("addItems", result.replaceAll("%", "*"), bullets.write());
        assertEquals("addItems", result.replaceAll("%", "#."), numbers.write());
        assertEquals("addItems", result.replaceAll("%", "|"), unmarked.write());

        String subList = "\nsublist\ntesting";
        bullets = RstBodyElement.bulletList(baseList).addSubList(subList).addItem("element0").addItem("element1");
        numbers = RstBodyElement.numberedList(baseList).addSubList(subList).addItem("element0").addItem("element1");
        unmarked = RstBodyElement.unmarkedList(baseList).addSubList(subList).addItem("element0").addItem("element1");
        result = "% parse\n% this\n% list\n\n"+ INDENT +"% sublist\n"+ INDENT +"% testing\n\n% element0\n% element1\n";
        assertEquals("sublist middle", result.replaceAll("%", "*"), bullets.write());
        assertEquals("sublist middle", result.replaceAll("%", "#."), numbers.write());
        assertEquals("sublist middle", result.replaceAll("%", "|"), unmarked.write());

        bullets = RstBodyElement.bulletList(baseList).addItem("element0").addItem("element1").addSubList(subList);
        numbers = RstBodyElement.numberedList(baseList).addItem("element0").addItem("element1").addSubList(subList);
        unmarked = RstBodyElement.unmarkedList(baseList).addItem("element0").addItem("element1").addSubList(subList);
        result = "% parse\n% this\n% list\n% element0\n% element1\n\n"+ INDENT +"% sublist\n"+ INDENT +"% testing\n\n";
        assertEquals("sublist end", result.replaceAll("%", "*"), bullets.write());
        assertEquals("sublist end", result.replaceAll("%", "#."), numbers.write());
        assertEquals("sublist end", result.replaceAll("%", "|"), unmarked.write());

        bullets = RstBodyElement.bulletList("").addSubList(subList).addItem("element0").addItem("element1");
        numbers = RstBodyElement.numberedList("").addSubList(subList).addItem("element0").addItem("element1");
        unmarked = RstBodyElement.unmarkedList("").addSubList(subList).addItem("element0").addItem("element1");
        result = "\n"+ INDENT +"% sublist\n"+ INDENT +"% testing\n\n% element0\n% element1\n";
        assertEquals("sublist beginning", result.replaceAll("%", "*"), bullets.write());
        assertEquals("sublist beginning", result.replaceAll("%", "#."), numbers.write());
        assertEquals("sublist beginning", result.replaceAll("%", "|"), unmarked.write());
    }

    @Test
    public void interchangeableListTest(){
        String bulletList = "bullet\nlistB";
        String numberedList = "numbered\nlistN";
        String unmarkedList = "unmarked\nlistU";

        BulletList bullets = RstBodyElement.bulletList(bulletList);
        NumberedList numbers = RstBodyElement.numberedList(numberedList);
        bullets.addSubList(numbers).addItem("added");
        String result = "* bullet\n* listB\n\n"+ INDENT +"#. numbered\n"+ INDENT +"#. listN\n\n* added\n";
        assertEquals("Bullets with numbers", result, bullets.write());

        bullets = RstBodyElement.bulletList(bulletList);
        UnmarkedList unmarked = RstBodyElement.unmarkedList(unmarkedList);
        bullets.addSubList(unmarked).addItem("added");
        result = "* bullet\n* listB\n\n"+ INDENT +"| unmarked\n"+ INDENT +"| listU\n\n* added\n";
        assertEquals("Bullets with unmarked", result, bullets.write());

        bullets = RstBodyElement.bulletList(bulletList);
        numbers = RstBodyElement.numberedList(numberedList);
        unmarked = RstBodyElement.unmarkedList(unmarkedList);

        unmarked.addSubList(numbers.addSubList(bullets).addItem("addedN")).addItem("addedU");
        result = "| unmarked\n| listU\n\n"+ INDENT +"#. numbered\n"+ INDENT +"#. listN\n"+ INDENT +"\n"+ INDENT + INDENT +"* bullet\n"+ INDENT + INDENT +"* listB\n"+ INDENT +"\n"+ INDENT +"#. addedN\n\n| addedU\n";
        assertEquals("all three", result, unmarked.write());
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

        UnmarkedList unmarked = RstBodyElement.unmarkedList("working\nwith items\nthat span multiple lines");
        unmarked.addItem("A standard\nvery long item").addItem("\nbegins with newline").addItem("ends with newline\n")
                .addItem("has multiple consecutive\n\nnewlines").addItem("has weird\n  spacing").addItem("\n");
        String unmarkedResult = "| working\n| with items\n| that span multiple lines"
                + "\n| A standard\n| very long item"
                + "\n| \n| begins with newline"
                + "\n| ends with newline\n| "
                + "\n| has multiple consecutive\n| \n| newlines"
                + "\n| has weird\n|   spacing"
                + "\n| \n| \n";
        assertEquals("Bad unmarked list formatting", unmarkedResult, unmarked.write());

        String numberedresult = "#. numbered\n#. list\n#. testing\n\n%%%\n#. nesting\n";

        bulletResult = INDENT + bulletResult.replaceAll("\n", "\n"+ INDENT);
        bulletResult = bulletResult.substring(0, bulletResult.length()-INDENT.length());
        NumberedList numberedList = RstBodyElement.numberedList("numbered\nlist\ntesting");
        numberedList.addSubList(bullet).addItem("nesting");
        assertEquals("Failed nesting bullets",
                numberedresult.replace("%%%", bulletResult), numberedList.write());

        unmarkedResult = INDENT + unmarkedResult.replaceAll("\n", "\n"+ INDENT);
        unmarkedResult = unmarkedResult.substring(0, unmarkedResult.length()-INDENT.length());
        numberedList = RstBodyElement.numberedList("numbered\nlist\ntesting");
        numberedList.addSubList(unmarked).addItem("nesting");
        assertEquals("Failed nesting unmarked",
                numberedresult.replace("%%%", unmarkedResult), numberedList.write());
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
}
