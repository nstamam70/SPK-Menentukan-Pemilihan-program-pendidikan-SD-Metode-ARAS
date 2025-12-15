/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Connections;

/**
 *
 * @author USER
 */
public class itemAlternatif {
    private int idAlternatif;
    private String namaAlternatif;

    public itemAlternatif(int idAlternatif, String namaAlternatif) {
        this.idAlternatif = idAlternatif;
        this.namaAlternatif = namaAlternatif;
    }

    public int getIdAlternatif() {
        return idAlternatif;
    }

    public String getNamaAlternatif() {
        return namaAlternatif;
    }

    @Override
    public String toString() {
        return idAlternatif + ". " + namaAlternatif; 
    }
}
