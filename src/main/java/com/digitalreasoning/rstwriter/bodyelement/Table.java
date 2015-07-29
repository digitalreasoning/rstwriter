package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * The Table class is a representation of the reStructuredText table structure. The class always generates a so-called
 * "grid table", and never a so-called "simple table". Tables must be rectangular. Nesting is supported in body elements,
 * so any cell of the table can contain nothing, text, or any number of body elements. Also, borders aren't necessary in
 * all cases. The Table class provides several constants to help modify the drawn borders on a cell. By default, all
 * borders are drawn on every cell, and it's only possible to modify the right and bottom borders for a single cell. Cell
 * borders may be eliminated on the right and bottom given that:
 * 1) the tables outer borders are intact
 * 2) all contiguous cells whose borders have been removed form a rectangle
 *
 * Examples of reStructuredText tables:
 * +---+---+---+
 * | a | b | c |   legal
 * +---+---+---+
 *
 * +---+---+---+
 * | a | b     |   legal
 * +---+---+---+
 *
 * +---+---+---+
 * | a | b | c |
 * +---+   +---+   legal
 * | a | b | c |
 * +---+---+---+
 *
 * +---+---+---+
 * | a | b   c |
 * +---+       +   legal
 * | a | b   c |
 * +---+---+---+
 *
 * +---+---+---+
 * | a | b | c |
 * +---+   +---+   illegal
 * | a | b   c |
 * +---+---+---+
 *
 * The Table class provides a Builder class as well as constructors to allow different styles of instantiation.
 */
public class Table implements RstBodyElement {
    /**
     * Borders will be drawn on both the right and bottom of the specified cell
     */
    public static final int BORDER_BOTH = 0;
    /**
     * A border will only be drawn on the right side of the specified cell (not the bottom)
     */
    public static final int BORDER_RIGHT = 1;
    /**
     * A border will only be drawn on the bottom of the specified cell (not on the right side)
     */
    public static final int BORDER_BELOW = 2;
    /**
     * Neither the right nor bottom borders will be drawn on this cell.
     */
    public static final int BORDER_NONE = 3;
    private String text;
    private int[] columnWidth, rowHeight;

    /**
     * creates a table with the values specified in the array argument. When creating the table, the array is interpreted
     * as array[row][col], with (0,0) at the top left. The array must be rectangular.
     * @param table the array representation of the desired table.
     * @throws IllegalArgumentException for any non-rectangular or null array
     */
    public Table(String[][] table){
        this(table, null);
    }

    /**
     * creates a table with the values specified in the array argument. When creating the table, the array is interpreted
     * as array[row][col], with (0,0) at the top left. The array must be rectangular. The borders of the array are
     * specified in the second parameter array, with each element corresponding to the cell value in the first parameter
     * array. The arrays must have the same dimensions.
     * @param table the array representation of the desired table.
     * @param borderSpec an array specifying the borders of the cells
     * @throws IllegalArgumentException for any non-rectangular or null array, or if table and borderSpec don't have the
     *          same dimensions
     */
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

    /**
     * returns an empty Table Builder
     * @return an empty Table Builder
     */
    public static Builder builder(){
        return new Builder();
    }

    //makes sure arguments are valid
    private void validate(String[][] table, int[][] borders){
        if(table.length != borders.length){
            throw new IllegalArgumentException("Table and borderSpec must have the same dimensions");
        }
        int numColumns = table[0].length;
        for(int i = 0; i<table.length; i++){
            if(table[i].length != numColumns){
                throw new IllegalArgumentException("Table must be rectangular");
            }
            if(borders[i].length != numColumns){
                throw new IllegalArgumentException("BorderSpec must be rectangular and have the same dimensions as Table");
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

    //makes sure the borders are legally defined
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

    //makes sure borders are legally defined
    private void checkBorderNone(int[][] borders, int row, int col){
        if(col == borders[0].length-1 || row == borders.length-1) return;
        if(borders[row][col+1] == BORDER_BELOW || borders[row][col+1] == BORDER_BOTH){
            throw new IllegalArgumentException("Cell (" + row + ", " + col + ") is illegally bordered");
        }
    }

    //configures the variables of the class for the table processing
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

    //gets the length of the longest String in the array
    private int getMaxLength(String[] arr){
        int i = 0;
        for(String str: arr){
            if(str.length() > i){
                i = str.length();
            }
        }
        return i+2;
    }

    //creates a table from the specified cell values and borders
    private String buildTable(String[][] table, int[][] borders){
        String build = buildRowBorder(null);
        for(int row = 0; row<table.length; row++){
            build += formatRow(row, table[row], borders[row]);
            if(row == table.length-1){
                build += buildRowBorder(null);
            }
            else {
                build += buildRowBorder(borders[row]);
            }
        }
        return build;
    }

    //creates the bottom border between rows
    private String buildRowBorder(int[] borderTest){
        String border = "+";
        for(int col = 0; col < columnWidth.length; col++){
            for(int i = 0; i<columnWidth[col]; i++) {
                if(borderTest != null && (borderTest[col] == BORDER_RIGHT || borderTest[col] == BORDER_NONE))
                {
                    border += " ";
                }
                else
                {
                    border += "-";  //the default
                }
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

    //writes out a row of data with the appropriate spacing and borders
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

    //writes a single line of a table row
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

    /**
     * Returns a fully initialized, properly-dimensioned array representing the default borders in every cell
     * @param table the table of cell values to base the border dimensions on
     * @return an array of BORDER_BOTH matching the dimensions of the given table
     */
    public static int[][] getDefaultBorderSpec(String[][] table){
        int[][] borderSpec = new int[table.length][table[0].length];
        for(int[] arr : borderSpec)
            Arrays.fill(arr, BORDER_BOTH);
        return borderSpec;
    }

    /**
     * The Builder class allows creation of Tables with body elements inside as well as an iterative creation strategy
     * as opposed to the all-at-once creation of the constructors. An instance may be obtained by calling the
     * {@code builder} function in the Table class or by directly instantiating one via the default constructor. In the
     * build process, cells are added from left to right, and rows are added top to bottom. Tables must be rectangular
     * and bordered properly (see examples in the {@link Table} class).
     */
    public static class Builder{
        private LinkedList<String[]> rowList;
        private LinkedList<String> currentRow;
        private LinkedList<int[]> borderList;
        private LinkedList<Integer> borderRow;

        /**
         * creates an empty Builder
         */
        public Builder(){
            rowList = new LinkedList<>();
            currentRow = new LinkedList<>();
            borderList = new LinkedList<>();
            borderRow = new LinkedList<>();
        }

        /**
         * Adds a cell to this table Builder
         * @param str the value to be placed in the cell (this will be processed for inline markup)
         * @param inlines optional arguments specifying inline markup in the cell value
         * @return this builder with the cell added
         */
        public Builder addCell(String str, Inline... inlines){
            currentRow.add(new Paragraph(str, inlines).getText());
            borderRow.add(BORDER_BOTH);
            return this;
        }

        /**
         * Adds a cell to this table Builder
         * @param str the value to be placed in the cell (this will be processed for inline markup)
         * @param borderType the bordering style for this cell
         * @param inlines optional arguments specifying inline markup in the cell value
         * @return this builder with the cell added
         */
        public Builder addCell(String str, int borderType, Inline... inlines){
            currentRow.add(new Paragraph(str, inlines).getText());
            borderRow.add(borderType);
            return this;
        }

        /**
         * Adds a cell to this table Builder
         * @param element the content to be placed in the cell
         * @return this builder with the cell added
         */
        public Builder addCell(RstBodyElement element){
            currentRow.add(element.write());
            borderRow.add(BORDER_BOTH);
            return this;
        }

        /**
         * Adds a cell to this table Builder
         * @param element the content to be placed in the cell
         * @param borderType the bordering style for this cell
         * @return this builder with the cell added
         */
        public Builder addCell(RstBodyElement element, int borderType){
            currentRow.add(element.write());
            borderRow.add(borderType);
            return this;
        }

        /**
         * Ends the current row and begins the next
         * @return this Builder starting a new row
         * @throws IllegalStateException if the row length doesn't match all previous row lengths (enforcing rectangularity)
         */
        public Builder nextRow(){
            if(!rowList.isEmpty()){
                if(currentRow.size() != rowList.get(0).length){
                    throw new IllegalStateException("Table must be rectangular." +
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

        /**
         * Ends the current row if necessary and creates a table from the provided data
         * @return a Table object with the provided data
         * @throws IllegalStateException if table isn't rectangular
         * @throws IllegalArgumentException if bordering is illegal
         */
        public Table build(){
            if(!currentRow.isEmpty())
                nextRow();
            String[][] table = new String[rowList.size()][rowList.get(0).length];
            int[][] borders = new int[borderList.size()][borderList.get(0).length];
            return new Table(rowList.toArray(table), borderList.toArray(borders));
        }
    }
}
