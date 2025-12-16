/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Connections.connect;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asus
 */
public class Proses_ARAS extends javax.swing.JFrame {

    private Connection conn = new connect().connect();

    /**
     * Creates new form Proses_SAW
     */
    public Proses_ARAS() {
        initComponents();
        this.setLocationRelativeTo(null);

    }

//    private void tampilTabelX() {
//        try {
//   
//            List<String> kriteriaList = new ArrayList<>();
//            Statement stKriteria = conn.createStatement();
//            ResultSet rsKriteria = stKriteria.executeQuery("SELECT id_kriteria, nama_kriteria FROM kriteria ORDER BY id_kriteria");
//
//            StringBuilder sqlSelect = new StringBuilder("SELECT a.nama_alternatif");
//            while (rsKriteria.next()) {
//                int idKriteria = rsKriteria.getInt("id_kriteria");
//                String namaKriteria = rsKriteria.getString("nama_kriteria");
//                kriteriaList.add(namaKriteria);
//
//              
//                sqlSelect.append(", MAX(CASE WHEN n.id_kriteria = ").append(idKriteria)
//                        .append(" THEN n.nilai END) AS `").append(namaKriteria).append("`");
//            }
//            sqlSelect.append(" FROM nilai_alternatif n JOIN alternatif a ON n.id_alternatif = a.id_alternatif GROUP BY a.nama_alternatif");
//
//        
//            List<String> kolomHeader = new ArrayList<>();
//            kolomHeader.add("Nama Alternatif");
//            kolomHeader.addAll(kriteriaList);
//
//            DefaultTableModel model = new DefaultTableModel(null, kolomHeader.toArray());
//            tableX.setModel(model);
//
//        
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(sqlSelect.toString());
//
//            while (rs.next()) {
//                List<Object> dataRow = new ArrayList<>();
//                dataRow.add(rs.getString("nama_alternatif"));
//                for (String kriteria : kriteriaList) {
//                    dataRow.add(rs.getObject(kriteria)); 
//                }
//                model.addRow(dataRow.toArray());
//            }
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Gagal tampil Tabel X Dinamis: " + e.getMessage());
//        }
//    }
    private void tampilTabelX() {
        try {
            List<String> kriteriaList = new ArrayList<>();
            List<Integer> idKriteriaList = new ArrayList<>();

            // Ambil daftar kriteria
            Statement stKriteria = conn.createStatement();
            ResultSet rsKriteria = stKriteria.executeQuery(
                    "SELECT id_kriteria, nama_kriteria FROM kriteria ORDER BY id_kriteria"
            );

            StringBuilder sqlSelect = new StringBuilder(
                    "SELECT a.nama_alternatif"
            );

            while (rsKriteria.next()) {
                int idKriteria = rsKriteria.getInt("id_kriteria");
                String namaKriteria = rsKriteria.getString("nama_kriteria");

                idKriteriaList.add(idKriteria);
                kriteriaList.add(namaKriteria);

                sqlSelect.append(", MAX(CASE WHEN n.id_kriteria = ")
                        .append(idKriteria)
                        .append(" THEN n.nilai END) AS `")
                        .append(namaKriteria)
                        .append("`");
            }

            sqlSelect.append(
                    " FROM nilai_alternatif n "
                    + " JOIN alternatif a ON n.id_alternatif = a.id_alternatif "
                    + " GROUP BY a.nama_alternatif "
                    + " ORDER BY a.nama_alternatif"
            );

            // Header tabel
            List<String> kolomHeader = new ArrayList<>();
            kolomHeader.add("Nama Alternatif");
            kolomHeader.addAll(kriteriaList);

            DefaultTableModel model = new DefaultTableModel(null, kolomHeader.toArray());
            tableX.setModel(model);

            // Eksekusi query
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sqlSelect.toString());

            while (rs.next()) {
                List<Object> dataRow = new ArrayList<>();
                dataRow.add(rs.getString("nama_alternatif"));

                for (String kriteria : kriteriaList) {
                    dataRow.add(rs.getObject(kriteria));
                }

                model.addRow(dataRow.toArray());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Gagal menampilkan Matriks Keputusan (X): " + e.getMessage()
            );
        }
    }

    private void tampilTabelSolusiIdeal() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nama Kriteria");
            model.addColumn("Tipe");
            model.addColumn("Nilai Solusi Ideal (A0)");

            tabelsolusiideal.setModel(model);

            String sql
                    = "SELECT k.nama_kriteria, k.tipe_kriteria, s.nilai_a0 "
                    + "FROM solusi_ideal s "
                    + "JOIN kriteria k ON k.id_kriteria = s.id_kriteria "
                    + "ORDER BY k.id_kriteria";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nama_kriteria"),
                    rs.getString("tipe_kriteria"),
                    rs.getDouble("nilai_a0")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menampilkan solusi ideal: " + e.getMessage());
        }
    }
private void tampilTabelNormalisasi() {
    try {
        // Formatter 4 angka di belakang koma
        DecimalFormat df = new DecimalFormat("#.####");

        // 1. Ambil daftar kriteria
        List<Integer> idKriteriaList = new ArrayList<>();
        List<String> namaKriteriaList = new ArrayList<>();

        Statement stK = conn.createStatement();
        ResultSet rsK = stK.executeQuery(
            "SELECT id_kriteria, nama_kriteria FROM kriteria ORDER BY id_kriteria");

        StringBuilder sql = new StringBuilder("SELECT a.nama_alternatif");

        while (rsK.next()) {
            int id = rsK.getInt("id_kriteria");
            String nama = rsK.getString("nama_kriteria");

            idKriteriaList.add(id);
            namaKriteriaList.add(nama);

            sql.append(", MAX(CASE WHEN ar.id_kriteria = ")
               .append(id)
               .append(" THEN ar.nilai_normalisasi END) AS `")
               .append(nama).append("`");
        }

        sql.append(" FROM aras ar ")
           .append("JOIN alternatif a ON a.id_alternatif = ar.id_alternatif ")
           .append("GROUP BY a.nama_alternatif");

        // 2. Header tabel
        List<String> kolom = new ArrayList<>();
        kolom.add("Alternatif");
        kolom.addAll(namaKriteriaList);

        DefaultTableModel model = new DefaultTableModel(null, kolom.toArray());
        tabelnormalisasi.setModel(model);

        // 3. Eksekusi query
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql.toString());

        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            row.add(rs.getString("nama_alternatif"));

            for (String k : namaKriteriaList) {
                double nilai = rs.getDouble(k);
                row.add(df.format(nilai)); // ← PEMBULATAN DI SINI
            }

            model.addRow(row.toArray());
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Gagal tampil normalisasi: " + e.getMessage());
    }
}

//    private void tampilTabelNormalisasi() {
//    try {
//        
//        
//        // 1. Ambil daftar kriteria
//        List<Integer> idKriteriaList = new ArrayList<>();
//        List<String> namaKriteriaList = new ArrayList<>();
//
//        Statement stK = conn.createStatement();
//        ResultSet rsK = stK.executeQuery(
//            "SELECT id_kriteria, nama_kriteria FROM kriteria ORDER BY id_kriteria");
//
//        StringBuilder sql = new StringBuilder("SELECT a.nama_alternatif");
//        
//
//        while (rsK.next()) {
//            int id = rsK.getInt("id_kriteria");
//            String nama = rsK.getString("nama_kriteria");
//
//            idKriteriaList.add(id);
//            namaKriteriaList.add(nama);
//
//            sql.append(", MAX(CASE WHEN ar.id_kriteria = ")
//               .append(id)
//               .append(" THEN ar.nilai_normalisasi END) AS `")
//               .append(nama).append("`");
//        }
//
//        sql.append(" FROM aras ar ")
//           .append("JOIN alternatif a ON a.id_alternatif = ar.id_alternatif ")
//           .append("GROUP BY a.nama_alternatif");
//
//        // 2. Header tabel
//        List<String> kolom = new ArrayList<>();
//        kolom.add("Alternatif");
//        kolom.addAll(namaKriteriaList);
//
//        DefaultTableModel model = new DefaultTableModel(null, kolom.toArray());
//        tabelnormalisasi.setModel(model);
//
//        // 3. Eksekusi query
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery(sql.toString());
//
//        while (rs.next()) {
//            List<Object> row = new ArrayList<>();
//            row.add(rs.getString("nama_alternatif"));
//
//            for (String k : namaKriteriaList) {
//                row.add(rs.getDouble(k));
//            }
//
//            model.addRow(row.toArray());
//        }
//
//    } catch (Exception e) {
//        JOptionPane.showMessageDialog(this,
//            "Gagal tampil normalisasi: " + e.getMessage());
//    }
//}

//    private void simpanDanTampilkanNormalisasiSAW() {
//        try {
//            Statement stClear = conn.createStatement();
//            stClear.executeUpdate("DELETE FROM saw");
//
//            Map<Integer, Float> nilaiMaxPerKriteria = new HashMap<>();
//            Statement stMax = conn.createStatement();
//            ResultSet rsMax = stMax.executeQuery("SELECT id_kriteria, MAX(nilai) AS max_nilai FROM nilai_siswa GROUP BY id_kriteria");
//            while (rsMax.next()) {
//                int idKriteria = rsMax.getInt("id_kriteria");
//                float maxNilai = rsMax.getFloat("max_nilai");
//                nilaiMaxPerKriteria.put(idKriteria, maxNilai);
//            }
//
//            String sql = "SELECT * FROM nilai_siswa";
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(sql);
//
//            String insertSql = "INSERT INTO saw (id_siswa, id_penilaian, id_kriteria, nilai_normalisasi) VALUES (?, ?, ?, ?)";
//            PreparedStatement pstInsert = conn.prepareStatement(insertSql);
//
//            while (rs.next()) {
//                int idSiswa = rs.getInt("id_siswa");
//                int idPenilaian = rs.getInt("id_penilaian");
//                int idKriteria = rs.getInt("id_kriteria");
//                float nilai = rs.getFloat("nilai");
//
//                float max = nilaiMaxPerKriteria.getOrDefault(idKriteria, 1f);
//                float normalisasi = (max == 0) ? 0 : nilai / max;
//
//                pstInsert.setInt(1, idSiswa);
//                pstInsert.setInt(2, idPenilaian);
//                pstInsert.setInt(3, idKriteria);
//                pstInsert.setFloat(4, normalisasi);
//                pstInsert.addBatch();
//            }
//
//            pstInsert.executeBatch();
//
//            List<String> kriteriaList = new ArrayList<>();
//            Statement stKriteria = conn.createStatement();
//            ResultSet rsKriteria = stKriteria.executeQuery("SELECT id_kriteria, nama_kriteria FROM kriteria ORDER BY id_kriteria");
//
//            Map<Integer, String> mapKriteria = new HashMap<>();
//            while (rsKriteria.next()) {
//                int id = rsKriteria.getInt("id_kriteria");
//                String nama = rsKriteria.getString("nama_kriteria");
//                mapKriteria.put(id, nama);
//                kriteriaList.add(nama);
//            }
//
//            List<String> kolomHeader = new ArrayList<>();
//            kolomHeader.add("Nama Siswa");
//            kolomHeader.addAll(kriteriaList);
//            DefaultTableModel model = new DefaultTableModel(null, kolomHeader.toArray());
//            tabelsolusiideal.setModel(model);
//
//            String sqlTampil = "SELECT a.nama_siswa, s.id_kriteria, s.nilai_normalisasi "
//                    + "FROM saw s JOIN alternatif a ON s.id_siswa = a.id_siswa ORDER BY a.nama_siswa, s.id_kriteria";
//
//            Statement stTampil = conn.createStatement();
//            ResultSet rsTampil = stTampil.executeQuery(sqlTampil);
//
//            Map<String, Map<String, Float>> dataMap = new LinkedHashMap<>();
//            while (rsTampil.next()) {
//                String namaSiswa = rsTampil.getString("nama_siswa");
//                int idKriteria = rsTampil.getInt("id_kriteria");
//                float nilaiNorm = rsTampil.getFloat("nilai_normalisasi");
//
//                String namaKriteria = mapKriteria.get(idKriteria);
//                dataMap.putIfAbsent(namaSiswa, new HashMap<>());
//                dataMap.get(namaSiswa).put(namaKriteria, nilaiNorm);
//            }
//
//            for (String nama : dataMap.keySet()) {
//                List<Object> baris = new ArrayList<>();
//                baris.add(nama);
//                for (String kriteria : kriteriaList) {
//                    Float nilai = dataMap.get(nama).getOrDefault(kriteria, 0f);
//                    baris.add(String.format("%.6f", nilai));
//                }
//                model.addRow(baris.toArray());
//            }
//
//            JOptionPane.showMessageDialog(null, "Normalisasi disimpan dan ditampilkan.");
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Gagal simpan/tampil normalisasi: " + e.getMessage());
//        }
//    }

    private void hitungDanTampilkanSkorAkhirSAW() {
        try {

            Map<Integer, Float> bobotPerKriteria = new HashMap<>();
            Statement stBobot = conn.createStatement();
            ResultSet rsBobot = stBobot.executeQuery("SELECT id_kriteria, bobot_kriteria FROM kriteria");
            while (rsBobot.next()) {
                int idKriteria = rsBobot.getInt("id_kriteria");
                float bobot = rsBobot.getFloat("bobot_kriteria");
                bobotPerKriteria.put(idKriteria, bobot);
            }

            String sql = "SELECT s.id_siswa, a.nama_siswa, s.id_kriteria, s.nilai_normalisasi "
                    + "FROM saw s JOIN alternatif a ON s.id_siswa = a.id_siswa "
                    + "ORDER BY s.id_siswa, s.id_kriteria";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            Map<Integer, Float> skorAkhirPerSiswa = new LinkedHashMap<>();
            Map<Integer, String> namaSiswaMap = new LinkedHashMap<>();

            while (rs.next()) {
                int idSiswa = rs.getInt("id_siswa");
                String namaSiswa = rs.getString("nama_siswa");
                int idKriteria = rs.getInt("id_kriteria");
                float nilaiNorm = rs.getFloat("nilai_normalisasi");
                float bobot = bobotPerKriteria.getOrDefault(idKriteria, 0f);
                float nilaiBobot = nilaiNorm * bobot;

                skorAkhirPerSiswa.put(idSiswa, skorAkhirPerSiswa.getOrDefault(idSiswa, 0f) + nilaiBobot);
                namaSiswaMap.put(idSiswa, namaSiswa);
            }

            List<Map.Entry<Integer, Float>> listSkor = new ArrayList<>(skorAkhirPerSiswa.entrySet());
            listSkor.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));

            String[] kolom = {"Rangking", "Nama Siswa", "Skor Akhir"};
            DefaultTableModel model = new DefaultTableModel(null, kolom);
            tablePrefrensi.setModel(model);
            Statement clearSt = conn.createStatement();
            clearSt.executeUpdate("DELETE FROM hasil_akhir");

            PreparedStatement ps = conn.prepareStatement("INSERT INTO hasil_akhir (id_siswa, skor_akhir) VALUES (?, ?)");

            int no = 1;
            for (Map.Entry<Integer, Float> entry : listSkor) {
                int id = entry.getKey();
                String nama = namaSiswaMap.get(id);
                float skor = entry.getValue();
                model.addRow(new Object[]{no++, nama, String.format("%.4f", skor)});
                ps.setInt(1, id);
                ps.setFloat(2, skor);
                ps.executeUpdate();
            }

            ps.close();
            JOptionPane.showMessageDialog(null, "Skor akhir berhasil dihitung, ditampilkan, dan disimpan.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal hitung/simpan skor akhir: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableX = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelsolusiideal = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablePrefrensi = new javax.swing.JTable();
        aras_normalisasi = new javax.swing.JButton();
        saw_perhitungan = new javax.swing.JButton();
        tampilkan_matriks = new javax.swing.JButton();
        saw_kembali = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableSi = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        hitung_a0 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelnormalisasi = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Proses Penilaian Siswa Terbaik");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, 60));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        tableX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableX);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Matriks ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Solusi Ideal A0");

        tabelsolusiideal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tabelsolusiideal);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nilai Utilitas & Perangkingan");

        tablePrefrensi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tablePrefrensi);

        aras_normalisasi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        aras_normalisasi.setText("Hitung Normalisasi");
        aras_normalisasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aras_normalisasiActionPerformed(evt);
            }
        });

        saw_perhitungan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        saw_perhitungan.setText("Hitung Perangkingan");
        saw_perhitungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saw_perhitunganActionPerformed(evt);
            }
        });

        tampilkan_matriks.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tampilkan_matriks.setText("Tampilkan Matriks");
        tampilkan_matriks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkan_matriksActionPerformed(evt);
            }
        });

        saw_kembali.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        saw_kembali.setText("Kembali");
        saw_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saw_kembaliActionPerformed(evt);
            }
        });

        tableSi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tableSi);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nilai Optimal Si");

        hitung_a0.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        hitung_a0.setText("Solusi Ideal A0");
        hitung_a0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hitung_a0ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Matriks Normalisasi");

        tabelnormalisasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tabelnormalisasi);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(tampilkan_matriks)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hitung_a0)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aras_normalisasi, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saw_perhitungan, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saw_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tampilkan_matriks, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saw_perhitungan, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saw_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hitung_a0, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aras_normalisasi, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1080, 780));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tampilkan_matriksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilkan_matriksActionPerformed
        // TODO add your handling code here:
        tampilTabelX();
    }//GEN-LAST:event_tampilkan_matriksActionPerformed

    private void saw_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saw_kembaliActionPerformed
        // TODO add your handling code here:
        new Menu().setVisible(true);
        dispose();
    }//GEN-LAST:event_saw_kembaliActionPerformed

    private void aras_normalisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aras_normalisasiActionPerformed
        try {
            Statement st = conn.createStatement();

            // 1. Hitung normalisasi alternatif
            st.executeUpdate(
                    "REPLACE INTO aras (id_alternatif, id_kriteria, nilai_normalisasi) "
                    + "SELECT n.id_alternatif, n.id_kriteria, "
                    + "(CASE WHEN k.tipe_kriteria='benefit' THEN n.nilai ELSE (1/n.nilai) END) / "
                    + "(SELECT SUM(CASE WHEN k2.tipe_kriteria='benefit' THEN n2.nilai ELSE (1/n2.nilai) END) + s.nilai_a0 "
                    + " FROM nilai_alternatif n2 "
                    + " JOIN kriteria k2 ON k2.id_kriteria=n2.id_kriteria "
                    + " JOIN solusi_ideal s ON s.id_kriteria=n2.id_kriteria "
                    + " WHERE n2.id_kriteria=n.id_kriteria) "
                    + "FROM nilai_alternatif n "
                    + "JOIN kriteria k ON k.id_kriteria=n.id_kriteria "
                    + "JOIN solusi_ideal s ON s.id_kriteria=n.id_kriteria"
            );

            // 2. Update A0 ternormalisasi
            st.executeUpdate(
                    "UPDATE solusi_ideal s JOIN ( "
                    + "SELECT n.id_kriteria, SUM(CASE WHEN k.tipe_kriteria='benefit' THEN n.nilai ELSE (1/n.nilai) END) total "
                    + "FROM nilai_alternatif n JOIN kriteria k ON k.id_kriteria=n.id_kriteria GROUP BY n.id_kriteria "
                    + ") t ON t.id_kriteria=s.id_kriteria "
                    + "SET s.a0_ternormalisasi = s.nilai_a0 / (t.total + s.nilai_a0)"
            );

            tampilTabelNormalisasi();

            JOptionPane.showMessageDialog(this,
                    "Normalisasi berhasil dihitung dan ditampilkan");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal normalisasi: " + e.getMessage());
        }
    }//GEN-LAST:event_aras_normalisasiActionPerformed

    private void saw_perhitunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saw_perhitunganActionPerformed
        hitungDanTampilkanSkorAkhirSAW();
    }//GEN-LAST:event_saw_perhitunganActionPerformed

    private void saw_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saw_simpanActionPerformed
        // TODO add your handling code here:
//        DefaultTableModel modelNormalisasi = (DefaultTableModel) tableNormalisasi.getModel();
//        DefaultTableModel modelPeringkat = (DefaultTableModel) tablePrefrensi.getModel();
//
//        simpanHasilSAW(modelNormalisasi, modelPeringkat);
    }//GEN-LAST:event_saw_simpanActionPerformed

    private void hitung_a0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hitung_a0ActionPerformed
        try {
            // 1. Hitung & simpan solusi ideal
            String sqlHitung
                    = "REPLACE INTO solusi_ideal (id_kriteria, nilai_a0) "
                    + "SELECT k.id_kriteria, "
                    + "CASE "
                    + "WHEN k.tipe_kriteria = 'benefit' THEN MAX(n.nilai) "
                    + "ELSE MIN(n.nilai) END "
                    + "FROM kriteria k "
                    + "JOIN nilai_alternatif n ON n.id_kriteria = k.id_kriteria "
                    + "GROUP BY k.id_kriteria";

            Statement st = conn.createStatement();
            st.executeUpdate(sqlHitung);

            // 2. Tampilkan ke tabel
            tampilTabelSolusiIdeal();

            JOptionPane.showMessageDialog(this,
                    "Solusi Ideal (A0) berhasil dihitung dan ditampilkan");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Proses solusi ideal gagal: " + e.getMessage());
        }
    }//GEN-LAST:event_hitung_a0ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Proses_ARAS.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Proses_ARAS.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Proses_ARAS.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Proses_ARAS.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Proses_ARAS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aras_normalisasi;
    private javax.swing.JButton hitung_a0;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton saw_kembali;
    private javax.swing.JButton saw_perhitungan;
    private javax.swing.JTable tabelnormalisasi;
    private javax.swing.JTable tabelsolusiideal;
    private javax.swing.JTable tablePrefrensi;
    private javax.swing.JTable tableSi;
    private javax.swing.JTable tableX;
    private javax.swing.JButton tampilkan_matriks;
    // End of variables declaration//GEN-END:variables
}
