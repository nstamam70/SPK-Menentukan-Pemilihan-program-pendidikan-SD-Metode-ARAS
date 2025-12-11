/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import Connections.connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//import connection.itemSiswa;
//import connection.itemKriteria;

/**
 *
 * @author Asus
 */
public class Nilai extends javax.swing.JFrame {

    private Connection conn = new connect().connect();
    private DefaultTableModel tabmode;

    /**
     * Creates new form Nilai
     */
    public Nilai() {
        initComponents();
        datatable();
        this.setLocationRelativeTo(null);
        new AlternatifDropdown().loadAlternatifToComboBox(dn_nama);
        new KriteriaDropdown().loadKriteriaToComboBox(dn_kriteria);
        autoKodeNilai();
        dn_cari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                cariNilai(dn_cari.getText());
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                cariNilai(dn_cari.getText());
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                cariNilai(dn_cari.getText());
            }
        });
    }

    private void clear() {
        dn_kode.setText("");
        dn_nama.setSelectedIndex(0);
        dn_kriteria.setSelectedIndex(0);
        dn_nilai.setText("");
    }

    private void active() {
        dn_kode.setEnabled(true);
        dn_nama.setEnabled(true);
        dn_kriteria.setEnabled(true);
        dn_nilai.setEnabled(true);
        dn_kode.requestFocus();
    }

    protected void datatable() {
        Object[] clcis = {"Kode", "Nama Siswa", "Nama Kriteria", "Nilai"};
        tabmode = new DefaultTableModel(null, clcis);
        tabmode.setRowCount(0);
        tablenilai.setModel(tabmode);
        String sql = "SELECT n.kode, a.nama_siswa, k.nama_kriteria, n.nilai "
                + "FROM nilai_siswa n "
                + "JOIN alternatif a ON n.id_siswa = a.id_siswa "
                + "JOIN kriteria k ON n.id_kriteria = k.id_kriteria";

        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String a = hasil.getString("kode");
                String b = hasil.getString("nama_siswa");
                String c = hasil.getString("nama_kriteria");
                String d = hasil.getString("nilai");

                String[] data = {a, b, c, d};
                tabmode.addRow(data);
            }
        } catch (Exception e) {
        }
    }

    public class AlternatifDropdown {

        public void loadAlternatifToComboBox(JComboBox comboBox) {
            comboBox.removeAllItems();
            try {
                String sql = "SELECT id_siswa, nama_siswa FROM alternatif";
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int idSiswa = rs.getInt("id_siswa");
                    String namaSiswa = rs.getString("nama_siswa");
//                    comboBox.addItem(new itemSiswa(idSiswa, namaSiswa)); // ← langsung masukkan objek Item!
                }

                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal memuat Nama Alternatif: " + e.getMessage());
            }
        }
    }

    public class KriteriaDropdown {

        public void loadKriteriaToComboBox(JComboBox comboBox) {
            comboBox.removeAllItems();
            try {
                // Tampilkan semua kriteria tanpa filter
                String sql = "SELECT id_kriteria, nama_kriteria FROM kriteria";
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int idKriteria = rs.getInt("id_kriteria");
                    String namaKriteria = rs.getString("nama_kriteria");
//                    comboBox.addItem(new itemKriteria(idKriteria, namaKriteria));
                }

                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal memuat Nama Kriteria: " + e.getMessage());
            }
        }
    }

    private void autoKodeNilai() {
        try {
            String sql = "SELECT MAX(kode) FROM nilai_siswa";
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                String kode = rs.getString(1);

                if (kode == null || kode.length() < 2) {
                    dn_kode.setText("N001");
                } else {
                    int no = Integer.parseInt(kode.substring(1)) + 1;
                    String kodeBaru = String.format("N%03d", no);
                    dn_kode.setText(kodeBaru);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal generate kode nilai: " + e.getMessage());
        }
    }

    private void cariNilai(String keyword) {
        Object[] clcis = {"Kode", "Nama Siswa", "Nama Kriteria", "Nilai"};
        tabmode = new DefaultTableModel(null, clcis);
        tablenilai.setModel(tabmode);

        String sql = "SELECT n.kode, a.nama_siswa, k.nama_kriteria, n.nilai "
                + "FROM nilai_siswa n "
                + "JOIN alternatif a ON n.id_siswa = a.id_siswa "
                + "JOIN kriteria k ON n.id_kriteria = k.id_kriteria "
                + "WHERE a.nama_siswa LIKE ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String a = rs.getString("kode");
                String b = rs.getString("nama_siswa");
                String c = rs.getString("nama_kriteria");
                String d = rs.getString("nilai");

                String[] data = {a, b, c, d};
                tabmode.addRow(data);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Pencarian gagal: " + e.getMessage());
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dn_kode = new javax.swing.JTextField();
        dn_simpan = new javax.swing.JButton();
        dn_ubah = new javax.swing.JButton();
        dn_hapus = new javax.swing.JButton();
        dn_kembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablenilai = new javax.swing.JTable();
        dn_kriteria = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        dn_cari = new javax.swing.JTextField();
        dn_nama = new javax.swing.JComboBox<>();
        dn_nilai = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Data Nilai");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1068, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Kode Nilai");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nama Siswa");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Kriteria");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nilai");

        dn_kode.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        dn_simpan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dn_simpan.setText("Simpan");
        dn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dn_simpanActionPerformed(evt);
            }
        });

        dn_ubah.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dn_ubah.setText("Ubah");
        dn_ubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dn_ubahActionPerformed(evt);
            }
        });

        dn_hapus.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dn_hapus.setText("Hapus");
        dn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dn_hapusActionPerformed(evt);
            }
        });

        dn_kembali.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dn_kembali.setText("Kembali");
        dn_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dn_kembaliActionPerformed(evt);
            }
        });

        tablenilai.setModel(new javax.swing.table.DefaultTableModel(
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
        tablenilai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablenilaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablenilai);

        dn_kriteria.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Cari Nama");

        dn_cari.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        dn_nama.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        dn_nilai.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(66, 66, 66)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(dn_simpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dn_ubah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dn_hapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dn_kembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(347, 347, 347))
                    .addComponent(dn_cari)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dn_nama, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dn_kode, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dn_kriteria, javax.swing.GroupLayout.Alignment.LEADING, 0, 823, Short.MAX_VALUE)
                            .addComponent(dn_nilai, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(29, 29, 29))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(dn_kode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(dn_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(dn_kriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(dn_nilai, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(dn_hapus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(dn_ubah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dn_simpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dn_kembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(dn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1070, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dn_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dn_kembaliActionPerformed
        // TODO add your handling code here:
        new Menu().setVisible(true);
        dispose();
    }//GEN-LAST:event_dn_kembaliActionPerformed

    private void dn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dn_simpanActionPerformed
        String kode = dn_kode.getText();
//        itemSiswa siswa = (itemSiswa) dn_nama.getSelectedItem();
//        itemKriteria kriteria = (itemKriteria) dn_kriteria.getSelectedItem();
        String nilai = dn_nilai.getText();

        String sql = "INSERT INTO nilai_siswa (kode, id_siswa, id_kriteria, nilai) VALUES (?,?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, kode);
//            stat.setInt(2, siswa.getIdSiswa());      // ⬅️ pakai ID
//            stat.setInt(3, kriteria.getIdKriteria());   // ⬅️ pakai ID
            stat.setString(4, nilai);

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            clear();
            dn_kode.requestFocus();
            datatable();
            autoKodeNilai();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan " + e);
        }
    }//GEN-LAST:event_dn_simpanActionPerformed

    private void dn_ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dn_ubahActionPerformed
        String kode = dn_kode.getText();
//        itemSiswa siswa = (itemSiswa) dn_nama.getSelectedItem();
//        itemKriteria kriteria = (itemKriteria) dn_kriteria.getSelectedItem();
        String nilai = dn_nilai.getText();

        String sql = "UPDATE nilai_siswa SET id_siswa=?, id_kriteria=?, nilai=? WHERE kode=?";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
//            stat.setInt(1, siswa.getIdSiswa());
//            stat.setInt(2, kriteria.getIdKriteria());
            stat.setString(3, nilai);
            stat.setString(4, kode);

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
            clear();
            dn_kode.requestFocus();
            dn_simpan.setVisible(true);
            datatable();
             autoKodeNilai();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah " + e);
        }
    }//GEN-LAST:event_dn_ubahActionPerformed

    private void dn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dn_hapusActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "hapus", "Konfirmasi Dialog", JOptionPane.YES_NO_CANCEL_OPTION);
        if (ok == 0) {
            String sql = "delete from nilai_siswa where kode='" + dn_kode.getText() + "'";
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "data berhasi dihapus");;
                clear();
                dn_kode.requestFocus();
                datatable();
                dn_simpan.setVisible(true);
                 autoKodeNilai();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data gagal dihapus" + e);
            }
        }
    }//GEN-LAST:event_dn_hapusActionPerformed

    private void tablenilaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablenilaiMouseClicked
        int clc = tablenilai.getSelectedRow();
        String a = tabmode.getValueAt(clc, 0).toString();
        String b = tabmode.getValueAt(clc, 1).toString();
        String c = tabmode.getValueAt(clc, 2).toString();
        String d = tabmode.getValueAt(clc, 3).toString();

        dn_kode.setText(a);
        dn_nama.setSelectedIndex(0);
        dn_kriteria.setSelectedIndex(0);
        dn_nilai.setText(d);

        dn_simpan.setVisible(false);
    }//GEN-LAST:event_tablenilaiMouseClicked

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
            java.util.logging.Logger.getLogger(Nilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Nilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Nilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Nilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Nilai().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField dn_cari;
    private javax.swing.JButton dn_hapus;
    private javax.swing.JButton dn_kembali;
    private javax.swing.JTextField dn_kode;
    private javax.swing.JComboBox<String> dn_kriteria;
    private javax.swing.JComboBox<String> dn_nama;
    private javax.swing.JTextField dn_nilai;
    private javax.swing.JButton dn_simpan;
    private javax.swing.JButton dn_ubah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablenilai;
    // End of variables declaration//GEN-END:variables
}
