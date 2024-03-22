/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aplikasikasir_ujikom;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import java.util.Date;
/**
 *
 * @author OWNER
 */
public class FormPenjualan extends javax.swing.JFrame {
    Connection konek;
    PreparedStatement pst,pst2;
    ResultSet rst;
    int inputstok, inputstok2, inputharga, inputjumlah, kurangistok, tambahstok;
    String harga, idproduk, idprodukpenjualan, iddetail, jam, tanggal, sub_total;
    /**
     * Creates new form FormPenjualan
     */
    public FormPenjualan() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
       konek = Koneksi.koneksiDB();
       detail();
       tampilJam();
       autonumber();
       penjumlahan();
    }
    
     public void tampilJam(){
    Thread clock=new Thread(){
        public void run(){
            for(;;){
                Calendar cal=Calendar.getInstance();
                SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
                txtJam.setText(format.format(cal.getTime()));
                 txtTanggal.setText(format2.format(cal.getTime()));
                
            try { sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FormPenjualan.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      };
    clock.start();
    }
    
 private void simpan(){
        String tgl=txtTanggal.getText();
        String jam=txtJam.getText();
      try {
            String sql="insert into penjualan (PenjualanID,DetailID,TanggalPenjualan,JamPenjualan,TotalHarga) value (?,?,?,?,?)";
            pst=konek.prepareStatement(sql);
            pst.setString(1, txtIdPenjualan.getText());
            pst.setString(2, iddetail);
            pst.setString(3, tgl);
            pst.setString(4, jam);
            pst.setString(5, txtTotal.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Tersimpan");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            }
    }
    
    private void total(){
    int total, bayar, kembali;
        total= Integer.parseInt(txtBayar.getText());
        bayar= Integer.parseInt(txtTotal.getText());
        kembali=total-bayar;
        String ssub=String.valueOf(kembali);
        txtKembalian.setText(ssub);
    }
    
    public void clsr(){
    txtJumlah.setText("");
    //jdis.setText("");
    }
    
    public void cari(){
    try {
        String sql="select * from produk where ProdukID LIKE '%"+txtIdProduk.getText()+"%'";
        pst=konek.prepareStatement(sql);
        rst=pst.executeQuery();
        tblProduk.setModel(DbUtils.resultSetToTableModel(rst));
       } catch (Exception e){ JOptionPane.showMessageDialog(null, e);} 
    }
    
    public void kurangi_stok(){
    int qty;
    qty=Integer.parseInt(txtJumlah.getText());
    kurangistok=inputstok-qty;
    }
    
    private void subtotal(){
    int jumlah, sub;
         jumlah= Integer.parseInt(txtJumlah.getText());
         sub=(jumlah*inputharga);
         sub_total=String.valueOf(sub);     
    }
    
    public void tambah_stok(){
    tambahstok=inputjumlah+inputstok2;
        try {
        String update="update produk set Stok='"+tambahstok+"' where ProdukID='"+idproduk+"'";
        pst2=konek.prepareStatement(update);
        pst2.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);}
    }
    
    public void ambil_stock(){
    try {
    String sql="select * from produk where ProdukID='"+idproduk+"'";
    pst=konek.prepareStatement(sql);
    rst=pst.executeQuery();
    if (rst.next()) {    
    String stok=rst.getString(("Stok"));
    inputstok2= Integer.parseInt(stok);
    }
    }catch (Exception e) {
    JOptionPane.showMessageDialog(null, e);}
    }
    
    public void penjumlahan(){
        int totalBiaya = 0;
        int subtotal;
        DefaultTableModel dataModel = (DefaultTableModel) tblPenjualan.getModel();
        int jumlah = tblPenjualan.getRowCount();
        for (int i=0; i<jumlah; i++){
        subtotal = Integer.parseInt(dataModel.getValueAt(i, 4).toString());
        totalBiaya += subtotal;
        }
        txtTotal.setText(String.valueOf(totalBiaya));
    }
    
    public void autonumber(){
    try{
        String sql = "SELECT MAX(RIGHT(PenjualanID,3)) AS NO FROM penjualan";
        pst=konek.prepareStatement(sql);
        rst=pst.executeQuery();
        while (rst.next()) {
                if (rst.first() == false) {
                    txtIdPenjualan.setText("IDP001");
                } else {
                    rst.last();
                    int auto_id = rst.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int NomorJual = no.length();
                    for (int j = 0; j < 3 - NomorJual; j++) {
                        no = "0" + no;
                    }
                    txtIdPenjualan.setText("IDP" + no);
                }
            }
        rst.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);}
    }
    
    public void detail(){
    try {
        String Kode_detail=txtIdPenjualan.getText();
        String KD="D"+Kode_detail;
        String sql="select * from detailpenjualan where DetailID='"+KD+"'";
        pst=konek.prepareStatement(sql);
        rst=pst.executeQuery();
        tblPenjualan.setModel(DbUtils.resultSetToTableModel(rst));
       } catch (Exception e){ 
           JOptionPane.showMessageDialog(null, e);} 
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtIdProduk = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduk = new javax.swing.JTable();
        txtIdPenjualan = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPenjualan = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        txtJam = new javax.swing.JTextField();
        txtTanggal = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        txtBayar = new javax.swing.JTextField();
        txtKembalian = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnBayar = new javax.swing.JButton();
        btnBaru = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("TRANSAKSI PENJUALAN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(221, 221, 221)
                .addComponent(jLabel1)
                .addContainerGap(239, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 740, 104);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Masukan Id Produk");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(27, 163, 106, 16);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Id Penjualan");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(27, 377, 67, 16);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Data Transaksi");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(27, 435, 80, 16);
        getContentPane().add(txtIdProduk);
        txtIdProduk.setBounds(27, 191, 126, 22);

        btnCari.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCari.setText("CARI");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        getContentPane().add(btnCari);
        btnCari.setBounds(179, 191, 72, 23);

        tblProduk.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProdukMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduk);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(27, 232, 452, 110);
        getContentPane().add(txtIdPenjualan);
        txtIdPenjualan.setBounds(133, 374, 126, 22);

        tblPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPenjualan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPenjualanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPenjualan);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(27, 487, 452, 110);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Jumlah");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(539, 265, 50, 16);
        getContentPane().add(txtJumlah);
        txtJumlah.setBounds(525, 287, 64, 22);
        getContentPane().add(txtJam);
        txtJam.setBounds(533, 122, 75, 35);
        getContentPane().add(txtTanggal);
        txtTanggal.setBounds(643, 122, 64, 35);
        getContentPane().add(txtTotal);
        txtTotal.setBounds(150, 650, 110, 22);
        getContentPane().add(txtBayar);
        txtBayar.setBounds(150, 710, 110, 22);
        getContentPane().add(txtKembalian);
        txtKembalian.setBounds(150, 770, 110, 22);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Total");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(30, 650, 40, 16);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Bayar");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(30, 710, 40, 16);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Kembalian");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(30, 770, 60, 16);

        btnBayar.setText("BAYAR");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBayar);
        btnBayar.setBounds(400, 660, 72, 23);

        btnBaru.setText("BARU");
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });
        getContentPane().add(btnBaru);
        btnBaru.setBounds(530, 660, 72, 23);

        btnTambah.setIcon(new javax.swing.ImageIcon("C:\\Users\\OWNER\\Downloads\\icons8-plus-48.png")); // NOI18N
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        getContentPane().add(btnTambah);
        btnTambah.setBounds(620, 263, 54, 40);

        btnHapus.setIcon(new javax.swing.ImageIcon("C:\\Users\\OWNER\\Downloads\\hapus.png")); // NOI18N
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        getContentPane().add(btnHapus);
        btnHapus.setBounds(520, 530, 40, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
      total();
        simpan();
        autonumber();
        detail();
        txtTotal.setText("");
        txtBayar.setText("");
        txtKembalian.setText("");
        txtIdProduk.setText("");
        cari();
        
// TODO add your handling code here:
    }//GEN-LAST:event_btnBayarActionPerformed

    private void tblProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdukMouseClicked
try {
    int row=tblProduk.getSelectedRow();
    String tabel_klik=(tblProduk.getModel().getValueAt(row, 0).toString());
    String sql="select * from produk where ProdukID='"+tabel_klik+"'";
    pst=konek.prepareStatement(sql);
    rst=pst.executeQuery();
    if (rst.next()) {
    idproduk=rst.getString(("ProdukID"));    
    String stok=rst.getString(("Stok"));
    inputstok= Integer.parseInt(stok);
    harga=rst.getString(("Harga"));
    inputharga= Integer.parseInt(harga);
    }
}catch (Exception e) {
    JOptionPane.showMessageDialog(null, e);
}

    // TODO add your handling code here:
    }//GEN-LAST:event_tblProdukMouseClicked

    private void tblPenjualanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPenjualanMouseClicked
try {
    int row=tblPenjualan.getSelectedRow();
    idprodukpenjualan=(tblPenjualan.getModel().getValueAt(row, 1).toString());
    String sql="select * from detailpenjualan where ProdukID='"+idprodukpenjualan+"'";
    pst=konek.prepareStatement(sql);
    rst=pst.executeQuery();
    if (rst.next()) {   
    String jumlah=rst.getString(("JumlahProduk"));
    inputjumlah= Integer.parseInt(jumlah);
    }
}catch (Exception e) {
    JOptionPane.showMessageDialog(null, e);
}
ambil_stock();
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPenjualanMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
subtotal();
        kurangi_stok();
        try {
            String Kode_detail=txtIdPenjualan.getText();
            iddetail="D"+Kode_detail;
            String sql="insert into detailpenjualan (DetailID,ProdukID,Harga,JumlahProduk,Subtotal) value (?,?,?,?,?)";
            String update="update   produk set Stok='"+kurangistok+"' where ProdukID='"+idproduk+"'"; 
            pst=konek.prepareStatement(sql);
            pst2=konek.prepareStatement(update);
            pst.setString(1, iddetail);
            pst.setString(2, idproduk);
            pst.setString(3, harga);
            pst.setString(4, txtJumlah.getText());
            pst.setString(5, sub_total);
            pst.execute();
            pst2.execute();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            }
        detail();
        penjumlahan();
        cari();
        clsr();     
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
try {
            String sql="delete from detailpenjualan where ProdukID=?";
            pst=konek.prepareStatement(sql);
            pst.setString(1, idprodukpenjualan);
            pst.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        detail();
        penjumlahan();
        tambah_stok();
        cari();
// TODO add your handling code here:
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
cari();        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariActionPerformed
    
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
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPenjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaru;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblPenjualan;
    private javax.swing.JTable tblProduk;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtIdPenjualan;
    private javax.swing.JTextField txtIdProduk;
    private javax.swing.JTextField txtJam;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtTanggal;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
