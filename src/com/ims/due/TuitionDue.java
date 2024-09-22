package com.ims.due;

import com.ims.connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

public class TuitionDue extends javax.swing.JFrame {

    public TuitionDue() {
        initComponents();
        dueTuitionData();
    }

    public void dueTuitionData() {

        Date admissionDate;
        String studentName, courseName;
        int id, courseFee, discount, paidAmount, dueAmount, totalAmount;

        try {

            Connection conn = DBConnection.getConnection();
            String sql = "select due_amount from tuitionstudents where due_amount > 0";
            PreparedStatement checkPst = conn.prepareStatement(sql);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                int due_amount = rs.getInt("due_amount");

                if (due_amount > 0) {

                    String duesql = "select id, date, name, course, fee, discount, total_paid_amount, due_amount, total_amount from tuitionstudents";
                    PreparedStatement pst = conn.prepareStatement(duesql);
                    ResultSet duers = pst.executeQuery();

                    if (duers.next()) {
                        id = duers.getInt("id");
                        admissionDate = duers.getDate("date");
                        studentName = duers.getString("name");
                        courseName = duers.getString("course");
                        courseFee = duers.getInt("fee");
                        discount = duers.getInt("discount");
                        paidAmount = duers.getInt("total_paid_amount");
                        dueAmount = duers.getInt("due_amount");
                        totalAmount = duers.getInt("total_amount");

                        Object[] obj = {
                            id, admissionDate, studentName, courseName, courseFee, discount, paidAmount, dueAmount, totalAmount
                        };
                        DefaultTableModel model = (DefaultTableModel) tuitionDueTable.getModel();
                        model.addRow(obj);
                    }

                }
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        close_btn = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tuitionDueTable = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DUE COMPUTER STUDENTS");

        close_btn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/close.png"))); // NOI18N
        close_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close_btnMouseClicked(evt);
            }
        });
        close_btn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                close_btnKeyPressed(evt);
            }
        });

        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(204, 204, 204)));
        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        tuitionDueTable.setAutoCreateRowSorter(true);
        tuitionDueTable.setForeground(new java.awt.Color(255, 255, 255));
        tuitionDueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S.N", "Admission Date", "Student Name", "Course Name", "Course Fee", "Discount", "Paid Amount", "Due Amount", "Total Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tuitionDueTable.setAutoResizeMode(0);
        tuitionDueTable.setColorBackgoundHead(new java.awt.Color(0, 102, 51));
        tuitionDueTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tuitionDueTable.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        tuitionDueTable.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tuitionDueTable.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tuitionDueTable.setRowHeight(40);
        tuitionDueTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tuitionDueTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tuitionDueTable);
        if (tuitionDueTable.getColumnModel().getColumnCount() > 0) {
            tuitionDueTable.getColumnModel().getColumn(0).setMinWidth(100);
            tuitionDueTable.getColumnModel().getColumn(0).setMaxWidth(150);
            tuitionDueTable.getColumnModel().getColumn(1).setMinWidth(200);
            tuitionDueTable.getColumnModel().getColumn(1).setMaxWidth(700);
            tuitionDueTable.getColumnModel().getColumn(2).setMinWidth(200);
            tuitionDueTable.getColumnModel().getColumn(2).setMaxWidth(700);
            tuitionDueTable.getColumnModel().getColumn(3).setMinWidth(200);
            tuitionDueTable.getColumnModel().getColumn(3).setMaxWidth(700);
            tuitionDueTable.getColumnModel().getColumn(4).setMinWidth(200);
            tuitionDueTable.getColumnModel().getColumn(4).setMaxWidth(700);
            tuitionDueTable.getColumnModel().getColumn(5).setMinWidth(200);
            tuitionDueTable.getColumnModel().getColumn(5).setMaxWidth(700);
            tuitionDueTable.getColumnModel().getColumn(6).setMinWidth(200);
            tuitionDueTable.getColumnModel().getColumn(6).setMaxWidth(700);
            tuitionDueTable.getColumnModel().getColumn(7).setMinWidth(200);
            tuitionDueTable.getColumnModel().getColumn(7).setMaxWidth(300);
            tuitionDueTable.getColumnModel().getColumn(8).setMinWidth(200);
            tuitionDueTable.getColumnModel().getColumn(8).setMaxWidth(700);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(close_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 22, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(close_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        setSize(new java.awt.Dimension(1549, 895));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void close_btnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_close_btnKeyPressed

    }//GEN-LAST:event_close_btnKeyPressed

    private void close_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close_btnMouseClicked
        this.dispose();
    }//GEN-LAST:event_close_btnMouseClicked

    private void tuitionDueTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tuitionDueTableMouseClicked

    }//GEN-LAST:event_tuitionDueTableMouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked

    }//GEN-LAST:event_jScrollPane2MouseClicked

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
            java.util.logging.Logger.getLogger(TuitionDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TuitionDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TuitionDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TuitionDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TuitionDue().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel close_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    public static rojeru_san.complementos.RSTableMetro tuitionDueTable;
    // End of variables declaration//GEN-END:variables
}
