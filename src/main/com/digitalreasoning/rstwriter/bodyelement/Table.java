package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by creynolds on 7/16/15.
 */
public class Table implements RstBodyElement {
    public static final int BORDER_BOTH = 0;
    public static final int BORDER_RIGHT = 1;
    public static final int BORDER_BELOW = 2;
    public static final int BORDER_NONE = 3;
    private String text;
    private int[] columnWidth, rowHeight;

    public Table(String[][] table){
        this(table, null);
    }

    public Table(String[][] table, int[][] borderSpec){
        int[][] borders;
        if(borderSpec == null){
            borders = getDefaultBorderSpec(table);
        }
        else{
            borders = borderSpec;
        }
        validate(table, borders);
        initialize(table);
        text = buildTable(table, borders);
    }

    public static Builder builder(){ return new Builder(); }
    private void validate(String[][] table, int[][] borders){
        if(table[0].length <= 1){
            throw new IllegalArgumentException("Table must have at least 2 columns");
        }
        if(table.length != borders.length){
            throw new IllegalArgumentException("Table and borderSpec must have the same dimensions");
        }
        int numColumns = table[0].length;
        for(int i = 0; i<table.length; i++){
            if(table[i].length != numColumns){
                throw new IllegalArgumentException("Table must be rectangular");
            }
            if(borders[i].length != numColumns){
                throw new IllegalArgumentException("BorderSpec must be rectangular");
            }
        }
        for(int i = 0; i<borders.length; i++){
            for(int j = 0; j<borders[0].length; j++){
                if(borders[i][j] == BORDER_BELOW){
                    checkBorderBelow(borders, i, j);
                }
                else if(borders[i][j] == BORDER_NONE){
                    checkBorderNone(borders, i, j);
                }
            }
        }
    }

    private void checkBorderBelow(int[][] borders, int row, int col){
        if(col == borders[0].length-1) return;
        if(borders[row][col+1] == BORDER_RIGHT) {
            throw new IllegalArgumentException("Cell (" + row + ", " + (col + 1) + ") illegally bordered");
        }
        if(row!=0){
            if(borders[row-1][col] == BORDER_RIGHT){
                throw new IllegalArgumentException("Cell (" + row + ", " + col + ") illegally bordered");
            }
            if((borders[row-1][col+1] == BORDER_RIGHT) &&
                    (borders[row-1][col] != BORDER_NONE)){
                throw new IllegalArgumentException("Cell (" + row + ", " + (col+1) + ") illegally bordered");
            }
        }
    }

    private void checkBorderNone(int[][] borders, int row, int col){
        if(col == borders[0].length-1 || row == borders.length-1) return;
        if(borders[row][col+1] == BORDER_BELOW || borders[row][col+1] == BORDER_BOTH){
            throw new IllegalArgumentException("Cell (" + row + ", " + col + ") is illegally bordered");
        }
    }

    private void initialize(String[][] table){
        columnWidth = new int[table[0].length];
        rowHeight = new int[table.length];
        Arrays.fill(columnWidth, 0);
        for(int row = 0; row<table.length; row++){
            int maxRowHeight = 0;
            for(int col = 0; col<table[0].length; col++){
                if(table[row][col] == null) continue;
                String[] comps = table[row][col].split("\n");
                if(comps.length > maxRowHeight){
                    maxRowHeight = comps.length;
                }
                int maxLen = getMaxLength(comps);
                if(maxLen > columnWidth[col]) columnWidth[col] = maxLen;
            }
            rowHeight[row] = maxRowHeight;
        }
    }

    private int getMaxLength(String[] arr){
        int i = 0;
        for(String str: arr){
            if(str.length() > i){
                i = str.length();
            }
        }
        return i+2;
    }

    private String buildTable(String[][] table, int[][] borders){
        String build = buildBorder(null);
        for(int row = 0; row<table.length; row++){
            build += formatRow(row, table[row], borders[row]);
            if(row == table.length-1){
                build += buildBorder(null);
            }
            else {
                build += buildBorder(borders[row]);
            }
        }
        return build;
    }

    private String buildBorder(int[] borderTest){
        String border = "+";
        for(int col = 0; col < columnWidth.length; col++){
            for(int i = 0; i<columnWidth[col]; i++) {
                if(borderTest != null && (borderTest[col] == BORDER_RIGHT || borderTest[col] == BORDER_NONE))
                    border += " ";
                else
                    border += "-";  //the default
            }
            if(borderTest != null && borderTest[col] == BORDER_NONE){
                border += " ";
            }
            else {
                border += "+";
            }
        }
        return border + "\n";
    }

    private String formatRow(int rowNum, String[] row, int[] borderTest) {
        if(rowHeight[rowNum] == 1) return writeLine(row, borderTest);
        else{
            String build = "";
            String[][] data = new String[rowHeight[rowNum]][row.length];
            for(int column = 0; column<row.length; column++){
                String[] lines = row[column].split("\n");
                for(int j = 0; j<rowHeight[rowNum]; j++){
                    if(j < lines.length){
                        data[j][column] = lines[j];
                    }
                    else{
                        data[j][column] = "";
                    }
                }
            }
            for(String[] dataRow : data){
                build += writeLine(dataRow, borderTest);
            }
            return build;
        }
    }

    private String writeLine(String[] row, int[] borderTest){
        String line = "|";
        for(int i = 0; i<row.length; i++){
            String str = row[i] == null ? "  " : " " + row[i];
            line += str;
            for(int space = str.length(); space<columnWidth[i]; space++){
                line += " ";
            }
            if(i < row.length-1 && (borderTest[i] == BORDER_BELOW || borderTest[i] == BORDER_NONE)) {
                line += " ";
            }
            else{
                line += "|";  //the default
            }
        }
        return line + "\n";
    }

    @Override
    public String write(){
        return text;
    }

    @Override
    public String toString(){ return text; }

    public static int[][] getDefaultBorderSpec(String[][] table){
        int[][] borderSpec = new int[table.length][table[0].length];
        for(int[] arr : borderSpec)
            Arrays.fill(arr, BORDER_BOTH);
        return borderSpec;
    }

    public static class Builder{
        private LinkedList<String[]> rowList;
        private LinkedList<String> currentRow;
        private LinkedList<int[]> borderList;
        private LinkedList<Integer> borderRow;

        public Builder(){
            rowList = new LinkedList<>();
            currentRow = new LinkedList<>();
            borderList = new LinkedList<>();
            borderRow = new LinkedList<>();
        }

        public Builder addCell(String str, Inline... inlines){
            currentRow.add(new Paragraph(str, inlines).getText());
            borderRow.add(BORDER_BOTH);
            return this;
        }

        public Builder addCell(String str, int borderType, Inline... inlines){
            currentRow.add(new Paragraph(str, inlines).getText());
            borderRow.add(borderType);
            return this;
        }

        public Builder addCell(RstBodyElement element){
            currentRow.add(element.write());
            borderRow.add(BORDER_BOTH);
            return this;
        }

        public Builder addCell(RstBodyElement element, int borderType){
            currentRow.add(element.write());
            borderRow.add(borderType);
            return this;
        }

        public Builder nextRow(){
            if(!rowList.isEmpty()){
                if(currentRow.size() != rowList.get(0).length){
                    throw new IllegalArgumentException("Table must be rectangular." +
                            "\nRequired cells: " + rowList.get(0).length + "\nProvided cells: " + currentRow.size());
                }
            }
            rowList.add(currentRow.toArray(new String[currentRow.size()]));
            currentRow.clear();
            int[] row = new int[borderRow.size()];
            for(int i = 0; i<row.length; i++){
                row[i] = borderRow.get(i);
            }
            borderList.add(row);
            borderRow.clear();
            return this;
        }

        public Table build(){
            if(!currentRow.isEmpty())
                nextRow();
            String[][] table = new String[rowList.size()][rowList.get(0).length];
            int[][] borders = new int[borderList.size()][borderList.get(0).length];
            return new Table(rowList.toArray(table), borderList.toArray(borders));
        }
    }
}
