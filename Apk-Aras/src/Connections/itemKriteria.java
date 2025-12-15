/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Connections;

/**
 *
 * @author USER
 */
public class itemKriteria {

    private int idKriteria;
    private String namaKriteria;

    public itemKriteria(int idKriteria, String namaKriteria) {
        this.idKriteria = idKriteria;
        this.namaKriteria = namaKriteria;
    }

    public int getIdKriteria() {
        return idKriteria;
    }

    public String getNamaKriteria() {
        return namaKriteria;
    }

    @Override
    public String toString() {
        return idKriteria + ". " + namaKriteria;
    }
}
