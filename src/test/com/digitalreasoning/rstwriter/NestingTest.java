package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.BulletList;
import com.digitalreasoning.rstwriter.bodyelement.Table;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by creynolds on 7/21/15.
 */
public class NestingTest {

    @Test
    public void tableNesting(){
        Table t = Table.builder().addCell("cell").addCell(new BulletList("item\nitem2\nitem3")).nextRow()
                .addCell("cell").addCell("cell").build();
        String[] tableString = t.write().split("\n");
        assertTrue(tableString.length == 7);
        assertTrue(tableString[1].contains("* item"));
        assertTrue(tableString[2].contains("* item2"));
        assertTrue(tableString[3].contains("* item3"));
        int length = tableString[0].length();
        for(String str : tableString){
            assertTrue(length == str.length());
        }
        int indMiddle = tableString[0].indexOf('+', 1);
        assertTrue(indMiddle == tableString[1].indexOf('|', 1));
        assertTrue(indMiddle == tableString[2].indexOf('|', 1));
        assertTrue(indMiddle == tableString[3].indexOf('|', 1));
    }

    @Test
    public void listNesting(){
        BulletList list = RstBodyElement.bulletList();
        list.addItem("item").addItem("item2");
        Table t = RstBodyElement.tableBuilder().addCell("cell").addCell("cell").nextRow().addCell("cell").addCell("cell").build();
        list.addItem(t).addItem("item3");
        String[] listString = list.write().split("\n");
        assertTrue(listString.length == 8);
        assertEquals(listString[0], "* item");
        assertEquals(listString[1], "* item2");
        assertTrue(listString[2].startsWith("* "));
        int length = listString[2].length();
        for(int i = 3; i<7; i++){
            assertTrue(listString[i].length() == length);
            assertFalse(listString[i].startsWith("*"));
        }
        assertEquals(listString[7], "* item3");
    }
}
