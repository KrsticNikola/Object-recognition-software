/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compareImages.util;

/**
 *
 * @author nik
 */
public class Matrix {

    int[][] data;
    int x, y, columns, rows;

    public Matrix(int[][] data) {
        this(data, 0, 0, data.length, data[0].length);
    }

    private Matrix(int[][] data, int x, int y, int columns, int rows) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.columns = columns;
        this.rows = rows;
    }

    public void setSubMatrix(int x, int y, int columns, int rows) {
//        this.data = data;
        this.x = x;
        this.y = y;
        this.columns = columns;
        this.rows = rows;

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (int i = y; i < x + rows; i++) {
            for (int j = x; j < x + columns; j++) {
                sb.append(data[i][j]).append(" ");
            }

            sb.append("\n");
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    public int[][] getData() {
//        int[][] datat = new int[columns][rows];
        int[][] datat = new int[rows][columns];
//        try {

//            System.out.println("\n Columns: " + columns);
//            System.out.println("Rows: " + rows);
//            System.out.println("------------");
            int tmpColumns = 0;
            int tmpRows = 0;
            for (int i = x; i < x + rows; i++) {
//                System.out.println("row: " + i);
                for (int j = y; j < y + columns; j++) {
//                sb.append(data[i][j]).append(" ");
//                    System.out.println("column: " + j);
                    datat[tmpRows][tmpColumns] = data[j][i];
                    tmpColumns++;
                }
                tmpColumns = 0;
                tmpRows++;
//            sb.append("\n");
            }
            return datat;
//        } catch (ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
//        }
//        return datat;

    }

}
