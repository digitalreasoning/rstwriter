package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.Table;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by creynolds on 7/15/15.
 */
public class TableTest {

    @Test
    public void simpleTest(){
        String[][][] tables = {
                {{"a", "b", "c"}, {"d", "e", "f"}, {"g", "h", "i"}},
                {{"aa", "b", "c"}, {"d", "ee", "f"}, {"", "", ""}, {"g", "h", "ii"}},
                {{"a", "", "c"}, {"d", "", "f"}, {"g", "", "i"}},
                {{"a", "bbbbb", "c"}, {"d", "e", "f"}, {"g", "h", "i"}},
                {{"aaa", "bbb", "ccc"}, {"dd", "e", "ffff"}},
                {{"a", "b"}, {"c", "d"}, {"e", "f"}, {"g", "h"}},
                {{"a"}}};
        String[] resultTables = {
                "+---+---+---+\n| a | b | c |\n+---+---+---+\n| d | e | f |\n+---+---+---+\n| g | h | i |\n+---+---+---+\n" ,
                "+----+----+----+\n| aa | b  | c  |\n+----+----+----+\n| d  | ee | f  |\n+----+----+----+\n|    |    |    |\n+----+----+----+\n| g  | h  | ii |\n+----+----+----+\n" ,
                "+---+--+---+\n| a |  | c |\n+---+--+---+\n| d |  | f |\n+---+--+---+\n| g |  | i |\n+---+--+---+\n" ,
                "+---+-------+---+\n| a | bbbbb | c |\n+---+-------+---+\n| d | e     | f |\n+---+-------+---+\n| g | h     | i |\n+---+-------+---+\n" ,
                "+-----+-----+------+\n| aaa | bbb | ccc  |\n+-----+-----+------+\n| dd  | e   | ffff |\n+-----+-----+------+\n"  ,
                "+---+---+\n| a | b |\n+---+---+\n| c | d |\n+---+---+\n| e | f |\n+---+---+\n| g | h |\n+---+---+\n",
                "+---+\n| a |\n+---+\n"};
        for(int i = 0; i<tables.length; i++){
            assertEquals("Simple Table # " + i + "fail", resultTables[i], new Table(tables[i]).write());
        }
    }
    
    @Test
    public void borderTest(){
        String[][] table = {{"a", "b", "c"}, {"d", "e", "f"}, {"g", "h", "i"}};
                
        int both = Table.BORDER_BOTH;
        int right = Table.BORDER_RIGHT;
        int below = Table.BORDER_BELOW;
        int[][][] borders = {
                null,
                {{both, both, both}, {both, both, both}, {both, both, both}},
                {{both, below, both}, {both, below, both}, {both, below, both}},
                {{both, right, both}, {both, right, both}, {both, right, both}},
                {{right, both, below}, {right, both, below}, {right, both, below}},
                {{below, below, below}, {below, below, below}, {below, below, below}},
                {{right, right, right}, {right, right, right}, {right, right, right}},
        };
        String[] resultTables = {
                "+---+---+---+\n| a | b | c |\n+---+---+---+\n| d | e | f |\n+---+---+---+\n| g | h | i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b | c |\n+---+---+---+\n| d | e | f |\n+---+---+---+\n| g | h | i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b   c |\n+---+---+---+\n| d | e   f |\n+---+---+---+\n| g | h   i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b | c |\n+---+   +---+\n| d | e | f |\n+---+   +---+\n| g | h | i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b | c |\n+   +---+---+\n| d | e | f |\n+   +---+---+\n| g | h | i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a   b   c |\n+---+---+---+\n| d   e   f |\n+---+---+---+\n| g   h   i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b | c |\n+   +   +   +\n| d | e | f |\n+   +   +   +\n| g | h | i |\n+---+---+---+\n"
        };
        for(int i = 0; i<borders.length; i++){
            assertEquals("Fail borders table #" + i, resultTables[i], new Table(table, borders[i]).write());
        }
    }

    @Test
    public void errorTest(){
        int both = Table.BORDER_BOTH;
        int right = Table.BORDER_RIGHT;
        int below = Table.BORDER_BELOW;

        try{
            new Table(null).write();
            fail("Null table");
        }catch(NullPointerException e){}

        try{
            String[][] table = {null, null, null};
            new Table(table).write();
            fail("null rows");
        }catch(NullPointerException e){}

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", ""}};
            new Table(table).write();
            fail("Non-rectangular table");
        }catch(IllegalArgumentException e){}

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
            int[][] borders = {{0, 0, 0}, {0, 0}, {0, 0, 0}};
            new Table(table, borders).write();
            fail("Non-rectangular borders");
        }catch(IllegalArgumentException e){ }

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
            int[][] borders = {{0, 0, 0}, {0, 0, 0}};
            new Table(table, borders).write();
            fail("Tables have uneven number of rows");
        }catch(IllegalArgumentException e){ }

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
            int[][] borders = {{both, right, both}, {below, both, both}, {both, both, both}};
            new Table(table, borders).write();
            fail("Failed illegal bordering #1");
        }catch(IllegalArgumentException e){ }

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
            int[][] borders = {{both, both, both}, {both, right, both}, {below, both, both}};
            new Table(table, borders).write();
            fail("Failed illegal bordering #2");
        }catch(IllegalArgumentException e){ }

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
            int[][] borders = {{both, below, right}, {both, both, both}, {both, both, both}};
            new Table(table, borders).write();
            fail("Failed illegal bordering #3");
        }catch(IllegalArgumentException e){ }

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
            int[][] borders = {{both, both, both}, {below, right, both}, {both, both, both}};
            new Table(table, borders).write();
            fail("Failed illegal bordering #4");
        }catch(IllegalArgumentException e){ }

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
            int[][] borders = {{both, right, both}, {both, below, both}, {both, both, both}};
            new Table(table, borders).write();
            fail("Failed illegal bordering #5");
        }catch(IllegalArgumentException e){ }

        try{
            String[][] table = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
            int[][] borders = {{both, both, both}, {right, both, both}, {below, both, both}};
            new Table(table, borders).write();
            fail("Failed illegal bordering #6");
        }catch(IllegalArgumentException e){}

        try{
            Table t = Table.builder().addCell("").addCell("").addCell("").nextRow().addCell("").addCell("").nextRow().build();
        }catch (IllegalStateException e){}
    }

    @Test
    public void standardBuilderTest() {
        String[][][] tables = {
                {{"a", "b", "c"}, {"d", "e", "f"}, {"g", "h", "i"}},
                {{"aa", "b", "c"}, {"d", "ee", "f"}, {"", "", ""}, {"g", "h", "ii"}},
                {{"a", "", "c"}, {"d", "", "f"}, {"g", "", "i"}},
                {{"a", "bbbbb", "c"}, {"d", "e", "f"}, {"g", "h", "i"}},
                {{"aaa", "bbb", "ccc"}, {"dd", "e", "ffff"}},
                {{"a", "b"}, {"c", "d"}, {"e", "f"}, {"g", "h"}}};
        String[] resultTables = {
                "+---+---+---+\n| a | b | c |\n+---+---+---+\n| d | e | f |\n+---+---+---+\n| g | h | i |\n+---+---+---+\n",
                "+----+----+----+\n| aa | b  | c  |\n+----+----+----+\n| d  | ee | f  |\n+----+----+----+\n|    |    |    |\n+----+----+----+\n| g  | h  | ii |\n+----+----+----+\n",
                "+---+--+---+\n| a |  | c |\n+---+--+---+\n| d |  | f |\n+---+--+---+\n| g |  | i |\n+---+--+---+\n",
                "+---+-------+---+\n| a | bbbbb | c |\n+---+-------+---+\n| d | e     | f |\n+---+-------+---+\n| g | h     | i |\n+---+-------+---+\n",
                "+-----+-----+------+\n| aaa | bbb | ccc  |\n+-----+-----+------+\n| dd  | e   | ffff |\n+-----+-----+------+\n",
                "+---+---+\n| a | b |\n+---+---+\n| c | d |\n+---+---+\n| e | f |\n+---+---+\n| g | h |\n+---+---+\n"};
        for (int i = 0; i < tables.length; i++) {
            Table.Builder tb = Table.builder();
            for (String[] row : tables[i]) {
                for (String cell : row) {
                    tb.addCell(cell);
                }
                tb.nextRow();
            }
            assertEquals("Failed table #" + i, resultTables[i], tb.build().write());
        }
    }

    @Test
    public void bordersBuilderTest(){
        String[][] table = {{"a", "b", "c"}, {"d", "e", "f"}, {"g", "h", "i"}};

        int both = Table.BORDER_BOTH;
        int right = Table.BORDER_RIGHT;
        int below = Table.BORDER_BELOW;
        int[][][] borders = {
                {{both, both, both}, {both, both, both}, {both, both, both}},
                {{both, below, both}, {both, below, both}, {both, below, both}},
                {{both, right, both}, {both, right, both}, {both, right, both}},
                {{right, both, below}, {right, both, below}, {right, both, below}},
                {{below, below, below}, {below, below, below}, {below, below, below}},
                {{right, right, right}, {right, right, right}, {right, right, right}},
        };
        String[] resultTables= {
                "+---+---+---+\n| a | b | c |\n+---+---+---+\n| d | e | f |\n+---+---+---+\n| g | h | i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b   c |\n+---+---+---+\n| d | e   f |\n+---+---+---+\n| g | h   i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b | c |\n+---+   +---+\n| d | e | f |\n+---+   +---+\n| g | h | i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b | c |\n+   +---+---+\n| d | e | f |\n+   +---+---+\n| g | h | i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a   b   c |\n+---+---+---+\n| d   e   f |\n+---+---+---+\n| g   h   i |\n+---+---+---+\n" ,
                "+---+---+---+\n| a | b | c |\n+   +   +   +\n| d | e | f |\n+   +   +   +\n| g | h | i |\n+---+---+---+\n"
        };
        for (int i = 0; i < borders.length; i++) {
            Table.Builder tb = Table.builder();
            for (int row = 0; row < table.length; row++) {
                for (int col = 0; col < table[0].length; col++) {
                    tb.addCell(table[row][col], borders[i][row][col]);
                }
                tb.nextRow();
            }
            assertEquals("Failed table #" + i, resultTables[i], tb.build().write());
        }
    }
}
