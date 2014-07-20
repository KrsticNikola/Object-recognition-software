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
public class CompareParams {

    private int rowsNmbr;
    private int columnsNmbr;
    private int sensitivity;
    private int stabilizer;



    public CompareParams() {
        autoSetParameters();
    }

    public CompareParams(int rowsNmbr, int columnsNmbr, int sensitivity, int stabilizer) {
        this.rowsNmbr = rowsNmbr;
        this.columnsNmbr = columnsNmbr;
        this.sensitivity = sensitivity;
        this.stabilizer = stabilizer;
    }


    public int getRowsNmbr() {
        return rowsNmbr;
    }

    public void setRowsNmbr(int rowsNmbr) {
        this.rowsNmbr = rowsNmbr;
    }

    public int getColumnsNmbr() {
        return columnsNmbr;
    }

    public void setColumnsNmbr(int columnsNmbr) {
        this.columnsNmbr = columnsNmbr;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public int getStabilizer() {
        return stabilizer;
    }

    public void setStabilizer(int stabilizer) {
        this.stabilizer = stabilizer;
    }

    //defoult parametri
    private void autoSetParameters() {
        this.rowsNmbr = 400;
        this.columnsNmbr = 400;
        this.sensitivity = 4500;
        this.stabilizer = 50;
    }

  




}
