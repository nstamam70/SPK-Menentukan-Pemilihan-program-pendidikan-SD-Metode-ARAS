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

    private DefaultTableModel tabmode;
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
    private void tampilMatriks() {
        try {
            List<String> kriteriaList = new ArrayList<>();
            List<Integer> idKriteriaList = new ArrayList<>();

            // Ambil daftar kriteria
            Statement stKriteria = conn.createStatement();
            ResultSet rsKriteria = stKriteria.executeQuery(
                    "SELECT id_kriteria, nama_kriteria FROM kriteria ORDER BY id_kriteria"
            );

            StringBuilder sqlSelect = new StringBuilder(
                    "SELECT a.id_alternatif, a.nama_alternatif"
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
                    + " GROUP BY a.id_alternatif, a.nama_alternatif "
                    + " ORDER BY a.id_alternatif"
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

    private void tampilTabelSolusiIdealTernormalisasi() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nama Kriteria");
            model.addColumn("Tipe");
            model.addColumn("A0 Ternormalisasi");

            a0_normalisasi.setModel(model);

            DecimalFormat df = new DecimalFormat("#0.0000");

            String sql
                    = "SELECT k.nama_kriteria, k.tipe_kriteria, s.a0_ternormalisasi "
                    + "FROM solusi_ideal s "
                    + "JOIN kriteria k ON k.id_kriteria = s.id_kriteria "
                    + "ORDER BY k.id_kriteria";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nama_kriteria"),
                    rs.getString("tipe_kriteria"),
                    df.format(rs.getDouble("a0_ternormalisasi"))
                });
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal menampilkan solusi ideal: " + e.getMessage()
            );
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

    private void hitungDanTampilkanNormalisasiARAS() {
        try {

            /* ================================
           1. Ambil tipe kriteria
           ================================ */
            Map<Integer, String> tipe = new HashMap<>();

            Statement stK = conn.createStatement();
            ResultSet rsK = stK.executeQuery(
                    "SELECT id_kriteria, tipe_kriteria FROM kriteria"
            );

            while (rsK.next()) {
                tipe.put(
                        rsK.getInt("id_kriteria"),
                        rsK.getString("tipe_kriteria").toLowerCase()
                );
            }

            /* ================================
           2. Hitung penyebut (Σxij + A0)
           ================================ */
            Map<Integer, Double> penyebut = new HashMap<>();

            Statement stP = conn.createStatement();
            ResultSet rsP = stP.executeQuery(
                    "SELECT n.id_kriteria, n.nilai, s.nilai_a0 "
                    + "FROM nilai_alternatif n "
                    + "JOIN solusi_ideal s ON s.id_kriteria = n.id_kriteria"
            );

            while (rsP.next()) {
                int idK = rsP.getInt("id_kriteria");
                double nilai = rsP.getDouble("nilai");
                double a0 = rsP.getDouble("nilai_a0");

                double nilaiHitung = tipe.get(idK).equals("cost")
                        ? (1.0 / nilai)
                        : nilai;

                penyebut.put(idK, penyebut.getOrDefault(idK, 0.0) + nilaiHitung);
            }

            Statement stA0 = conn.createStatement();
            ResultSet rsA0 = stA0.executeQuery(
                    "SELECT id_kriteria, nilai_a0 FROM solusi_ideal"
            );

            while (rsA0.next()) {
                int idK = rsA0.getInt("id_kriteria");
                double a0 = rsA0.getDouble("nilai_a0");

                double a0Hitung = tipe.get(idK).equals("cost")
                        ? (1.0 / a0)
                        : a0;

                penyebut.put(idK, penyebut.get(idK) + a0Hitung);
            }

            /* ================================
           3. Bersihkan tabel ARAS
           ================================ */
            Statement stClear = conn.createStatement();
            stClear.executeUpdate("DELETE FROM aras");

            /* ================================
           4. Simpan normalisasi alternatif
           ================================ */
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO aras (id_penilaian, id_alternatif, id_kriteria, nilai_normalisasi) "
                    + "VALUES (?, ?, ?, ?)"
            );

            Statement stN = conn.createStatement();
            ResultSet rsN = stN.executeQuery(
                    "SELECT id_penilaian, id_alternatif, id_kriteria, nilai FROM nilai_alternatif"
            );

            while (rsN.next()) {
                int idPenilaian = rsN.getInt("id_penilaian");
                int idAlt = rsN.getInt("id_alternatif");
                int idK = rsN.getInt("id_kriteria");
                double nilai = rsN.getDouble("nilai");

                double nilaiHitung = tipe.get(idK).equals("cost")
                        ? (1.0 / nilai)
                        : nilai;

                double rij = nilaiHitung / penyebut.get(idK);

                ps.setInt(1, idPenilaian);
                ps.setInt(2, idAlt);
                ps.setInt(3, idK);
                ps.setDouble(4, rij);
                ps.executeUpdate();
            }

            /* ================================
           5. Simpan A0 ternormalisasi
           ================================ */
            PreparedStatement psA0 = conn.prepareStatement(
                    "UPDATE solusi_ideal SET a0_ternormalisasi = ? WHERE id_kriteria = ?"
            );

            ResultSet rsA0Final = stA0.executeQuery(
                    "SELECT id_kriteria, nilai_a0 FROM solusi_ideal"
            );

            while (rsA0Final.next()) {
                int idK = rsA0Final.getInt("id_kriteria");
                double a0 = rsA0Final.getDouble("nilai_a0");

                double a0Hitung = tipe.get(idK).equals("cost")
                        ? (1.0 / a0)
                        : a0;

                psA0.setDouble(1, a0Hitung / penyebut.get(idK));
                psA0.setInt(2, idK);
                psA0.executeUpdate();
            }

            /* ================================
           6. TAMPILKAN TABEL NORMALISASI
           (DIPERBAIKI, TANPA METHOD BARU)
           ================================ */
            List<String> kriteriaList = new ArrayList<>();

            Statement stKT = conn.createStatement();
            ResultSet rsKT = stKT.executeQuery(
                    "SELECT id_kriteria, nama_kriteria FROM kriteria ORDER BY id_kriteria"
            );

            StringBuilder sql = new StringBuilder("SELECT a.nama_alternatif");

            while (rsKT.next()) {
                int idK = rsKT.getInt("id_kriteria");
                String nama = rsKT.getString("nama_kriteria");
                kriteriaList.add(nama);

                sql.append(", MAX(CASE WHEN ar.id_kriteria = ")
                        .append(idK)
                        .append(" THEN ar.nilai_normalisasi END) AS `")
                        .append(nama).append("`");
            }

            sql.append(
                    " FROM aras ar "
                    + " JOIN alternatif a ON ar.id_alternatif = a.id_alternatif "
                    + " GROUP BY a.id_alternatif, a.nama_alternatif "
                    + " ORDER BY a.id_alternatif"
            );

            DefaultTableModel model
                    = new DefaultTableModel(
                            new Object[]{"Alternatif"}, 0
                    );

            for (String k : kriteriaList) {
                model.addColumn(k);
            }

            tabelnormalisasi.setModel(model);

            Statement stT = conn.createStatement();
            ResultSet rsT = stT.executeQuery(sql.toString());

            while (rsT.next()) {
                List<Object> row = new ArrayList<>();
                row.add(rsT.getString("nama_alternatif"));

                for (String k : kriteriaList) {
                    row.add(String.format("%.4f", rsT.getDouble(k)));
                }

                model.addRow(row.toArray());
            }

            JOptionPane.showMessageDialog(this, "Normalisasi ARAS berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error normalisasi: " + e.getMessage());
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
        a0 = new javax.swing.JButton();
        tampilkan_matriks = new javax.swing.JButton();
        saw_kembali = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableSi = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        hitung_a0 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelnormalisasi = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        a0_normalisasi = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        Si = new javax.swing.JButton();
        Ki = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DWIKII NIH BOS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1074, Short.MAX_VALUE)
                .addContainerGap())
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

        a0.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        a0.setText("Tampilkan A0 ");
        a0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a0ActionPerformed(evt);
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

        a0_normalisasi.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(a0_normalisasi);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Solusi Ideal A0 Ternormalisasi");

        Si.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Si.setText("Si");
        Si.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SiActionPerformed(evt);
            }
        });

        Ki.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Ki.setText("Ki");
        Ki.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(tampilkan_matriks)
                                    .addGap(7, 7, 7)
                                    .addComponent(hitung_a0)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(aras_normalisasi, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(a0)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(Si, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(Ki, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(saw_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addGap(10, 10, 10))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel3))
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(a0, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saw_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hitung_a0, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aras_normalisasi, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Si, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Ki, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1080, 780));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tampilkan_matriksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilkan_matriksActionPerformed
        // TODO add your handling code here:
        tampilMatriks();
    }//GEN-LAST:event_tampilkan_matriksActionPerformed

    private void saw_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saw_kembaliActionPerformed
        // TODO add your handling code here:
        new Menu().setVisible(true);
        dispose();
    }//GEN-LAST:event_saw_kembaliActionPerformed

    private void aras_normalisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aras_normalisasiActionPerformed
        hitungDanTampilkanNormalisasiARAS();
    }//GEN-LAST:event_aras_normalisasiActionPerformed

    private void a0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a0ActionPerformed
        tampilTabelSolusiIdealTernormalisasi();
    }//GEN-LAST:event_a0ActionPerformed

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

    private void SiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SiActionPerformed
        // TODO add your handling code here:
        try {
            // 1. Bersihkan nilai Si sebelumnya
            Statement stClear = conn.createStatement();
            stClear.executeUpdate("UPDATE aras SET nilai_si = NULL");

            // 2. Hitung Si = Σ (rij * bobot)
            String sqlSi
                    = "SELECT ar.id_alternatif, SUM(ar.nilai_normalisasi * k.bobot_kriteria) AS nilai_si "
                    + "FROM aras ar "
                    + "JOIN kriteria k ON k.id_kriteria = ar.id_kriteria "
                    + "GROUP BY ar.id_alternatif";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sqlSi);

            // 3. Simpan Si ke tabel ARAS
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE aras SET nilai_si = ? WHERE id_alternatif = ?"
            );

            Map<Integer, Double> nilaiSiMap = new LinkedHashMap<>();

            while (rs.next()) {
                int idAlt = rs.getInt("id_alternatif");
                double si = rs.getDouble("nilai_si");

                nilaiSiMap.put(idAlt, si);

                ps.setDouble(1, si);
                ps.setInt(2, idAlt);
                ps.executeUpdate();
            }

            // 4. Tampilkan ke tabel Si
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Alternatif");
            model.addColumn("Nilai Si");

            tableSi.setModel(model);

            String sqlNama
                    = "SELECT DISTINCT a.id_alternatif, a.nama_alternatif, ar.nilai_si "
                    + "FROM aras ar JOIN alternatif a ON a.id_alternatif = ar.id_alternatif "
                    + "GROUP BY a.id_alternatif, a.nama_alternatif, ar.nilai_si "
                    + "ORDER BY ar.nilai_si DESC";

            ResultSet rsT = st.executeQuery(sqlNama);

            while (rsT.next()) {
                model.addRow(new Object[]{
                    rsT.getString("nama_alternatif"),
                    String.format("%.4f", rsT.getDouble("nilai_si"))
                });
            }

            JOptionPane.showMessageDialog(this, "Perhitungan Si berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Si: " + e.getMessage());
        }
    }//GEN-LAST:event_SiActionPerformed

    private void KiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KiActionPerformed
        // TODO add your handling code here:
        try {
            // 1. Ambil nilai S0 (Si milik A0)
            String sqlS0
                    = "SELECT SUM(a0_ternormalisasi * k.bobot_kriteria) AS s0 "
                    + "FROM solusi_ideal s JOIN kriteria k ON k.id_kriteria = s.id_kriteria";

            Statement st = conn.createStatement();
            ResultSet rsS0 = st.executeQuery(sqlS0);

            double s0 = 0;
            if (rsS0.next()) {
                s0 = rsS0.getDouble("s0");
            }

            if (s0 == 0) {
                JOptionPane.showMessageDialog(this, "Nilai S0 = 0, hitung Si dulu!");
                return;
            }

            // 2. Bersihkan hasil akhir
            st.executeUpdate("DELETE FROM hasil_akhir");

            // 3. Hitung Ki dan simpan
            String sqlKi
                    = "SELECT DISTINCT id_alternatif, nilai_si FROM aras";

            ResultSet rs = st.executeQuery(sqlKi);

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hasil_akhir (id_alternatif, nilai_ki) VALUES (?, ?)"
            );

            while (rs.next()) {
                int idAlt = rs.getInt("id_alternatif");
                double si = rs.getDouble("nilai_si");
                double ki = si / s0;

                ps.setInt(1, idAlt);
                ps.setDouble(2, ki);
                ps.executeUpdate();
            }

            // 4. Tampilkan ranking
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Alternatif");
            model.addColumn("Nilai Ki");
            model.addColumn("Ranking");

            tablePrefrensi.setModel(model);

            String sqlRank
                    = "SELECT a.nama_alternatif, h.nilai_ki "
                    + "FROM hasil_akhir h JOIN alternatif a ON a.id_alternatif = h.id_alternatif "
                    + "ORDER BY h.nilai_ki DESC";

            ResultSet rsR = st.executeQuery(sqlRank);

            int rank = 1;
            while (rsR.next()) {
                model.addRow(new Object[]{
                    rsR.getString("nama_alternatif"),
                    String.format("%.4f", rsR.getDouble("nilai_ki")),
                    rank++
                });
            }

            JOptionPane.showMessageDialog(this, "Perhitungan Ki & Ranking berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Ki: " + e.getMessage());
        }
    }//GEN-LAST:event_KiActionPerformed

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
    private javax.swing.JButton Ki;
    private javax.swing.JButton Si;
    private javax.swing.JButton a0;
    private javax.swing.JTable a0_normalisasi;
    private javax.swing.JButton aras_normalisasi;
    private javax.swing.JButton hitung_a0;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JButton saw_kembali;
    private javax.swing.JTable tabelnormalisasi;
    private javax.swing.JTable tabelsolusiideal;
    private javax.swing.JTable tablePrefrensi;
    private javax.swing.JTable tableSi;
    private javax.swing.JTable tableX;
    private javax.swing.JButton tampilkan_matriks;
    // End of variables declaration//GEN-END:variables
}
