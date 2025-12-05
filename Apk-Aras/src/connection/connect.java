/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class connect {

    private Connection koneksi;

    public Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Berhasil Koneksi");
        } catch (ClassNotFoundException ex) {
            System.out.println("Gagal Koneksi " + ex);
        }
        String url = "jdbc:mysql://localhost:3306/spk_arruhama";
        try {
            koneksi = DriverManager.getConnection(url, "root", "");
            System.out.println("Berhasil Koneksi Database");
        } catch (SQLException ex) {
            System.out.println("Gagal Koneksi Database" + ex);
        }
        return koneksi;
    }

}
