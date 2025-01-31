
package com.ims.add;

import com.ims.connection.DBConnection;
import java.sql.ResultSet;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author _Spoidey-
 */
public class AddTimeTable extends javax.swing.JFrame {

    String studentTime;
    int ID;
    DefaultTableModel model;
    
   
    public AddTimeTable() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        setDailyEntry();
    }
    
//    Setting Computer Courses
    public void setDailyEntry() {
        
        try {
            

            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from addstudenttime");
            
            
            while(rs.next()) {
                ID = rs.getInt("id");
                studentTime = rs.getString("student_time");
              
                Object[] obj = {ID, studentTime};
                model = (DefaultTableModel) dailyEntryTable.getModel();
                model.addRow(obj);
            }
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        
    }
    
//    To Add Computer Courses
    public boolean addDailyEntry() {
        
        boolean isAdded = false;
        ID = Integer.parseInt(txt_ID.getText());
        studentTime = txt_timeName.getText();
        
        
        try {
            
            Connection con = DBConnection.getConnection();
            
            String sql = "insert into addstudenttime values(?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
           
            pst.setInt(1, ID);
            pst.setString(2, studentTime);

            int rowCount = pst.executeUpdate();
            

            if(rowCount > 0) {

                isAdded = true;

            } else {

                isAdded = false;

            }
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
        return isAdded;
        
    }
    
//    To Clear Table
    public void clearDailyEntryTable() {
        
        DefaultTableModel model = (DefaultTableModel) dailyEntryTable.getModel();
        model.setRowCount(0);
        
    }
    
//  To Update Computer Courses    
    public boolean updateDailyEntry() {
        
        boolean isUpdated = false;
        ID = Integer.parseInt(txt_ID.getText());
        studentTime = txt_timeName.getText();
        
        try {
            
            Connection con = DBConnection.getConnection();
            String sql = "update addstudentTime set student_time = ? where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, studentTime);
            pst.setInt(2, ID);

            
            int rowCount = pst.executeUpdate();
            
            if(rowCount > 0) {
                
                isUpdated = true;
                
            } else {
                
                isUpdated = false;
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return isUpdated;
        
    }
    
    
//    To Delete Computer Course
    public boolean deleteDailyEntry() {
        
        boolean isDeleted = false;
        ID = Integer.parseInt(txt_ID.getText());
        
        try {
            
            Connection con = DBConnection.getConnection();
            String sql = "delete from addstudenttime where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, ID);
            
            int rowCount = pst.executeUpdate();
            
            if(rowCount > 0) {
                isDeleted = true;
            } else {
                isDeleted = false;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return isDeleted;
        
        
    }
    
//    To clear all input fields
    public void clearAllInputFields() {
        
       
        txt_ID.setText("");
        txt_timeName.setText("");
  
  
        
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new com.ims.swing.PanelBorder();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_timeName = new app.bolivia.swing.JCTextField();
        courseName = new javax.swing.JLabel();
        addComputerCourseBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dailyEntryTable = new rojeru_san.complementos.RSTableMetro();
        updateComputerCourseBtn = new javax.swing.JButton();
        deleteComputerCourseBtn = new javax.swing.JButton();
        courseName1 = new javax.swing.JLabel();
        txt_ID = new app.bolivia.swing.JCTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));
        panelBorder1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelBorder1MouseClicked(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/close.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ADD STUDENT TIME");

        txt_timeName.setBackground(new java.awt.Color(255, 255, 255));
        txt_timeName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 1, new java.awt.Color(0, 102, 255)));
        txt_timeName.setForeground(new java.awt.Color(0, 0, 153));
        txt_timeName.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N

        courseName.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        courseName.setForeground(new java.awt.Color(0, 0, 0));
        courseName.setText("Enter Service Name");

        addComputerCourseBtn.setBackground(new java.awt.Color(0, 102, 51));
        addComputerCourseBtn.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        addComputerCourseBtn.setForeground(new java.awt.Color(255, 255, 255));
        addComputerCourseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/add.png"))); // NOI18N
        addComputerCourseBtn.setText("Add");
        addComputerCourseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addComputerCourseBtnActionPerformed(evt);
            }
        });

        dailyEntryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Student Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dailyEntryTable.setColorBackgoundHead(new java.awt.Color(51, 102, 0));
        dailyEntryTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        dailyEntryTable.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        dailyEntryTable.setFuenteFilas(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        dailyEntryTable.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        dailyEntryTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        dailyEntryTable.setRowHeight(40);
        dailyEntryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dailyEntryTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(dailyEntryTable);
        if (dailyEntryTable.getColumnModel().getColumnCount() > 0) {
            dailyEntryTable.getColumnModel().getColumn(0).setMinWidth(50);
            dailyEntryTable.getColumnModel().getColumn(0).setMaxWidth(100);
            dailyEntryTable.getColumnModel().getColumn(1).setMinWidth(500);
            dailyEntryTable.getColumnModel().getColumn(1).setMaxWidth(800);
        }

        updateComputerCourseBtn.setBackground(new java.awt.Color(153, 153, 0));
        updateComputerCourseBtn.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        updateComputerCourseBtn.setForeground(new java.awt.Color(255, 255, 255));
        updateComputerCourseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/update.png"))); // NOI18N
        updateComputerCourseBtn.setText("Update");
        updateComputerCourseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateComputerCourseBtnActionPerformed(evt);
            }
        });

        deleteComputerCourseBtn.setBackground(new java.awt.Color(153, 0, 51));
        deleteComputerCourseBtn.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        deleteComputerCourseBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteComputerCourseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/delete.png"))); // NOI18N
        deleteComputerCourseBtn.setText("Delete");
        deleteComputerCourseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteComputerCourseBtnActionPerformed(evt);
            }
        });

        courseName1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        courseName1.setForeground(new java.awt.Color(0, 0, 0));
        courseName1.setText("Enter Time ID");

        txt_ID.setBackground(new java.awt.Color(255, 255, 255));
        txt_ID.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 1, new java.awt.Color(0, 102, 255)));
        txt_ID.setForeground(new java.awt.Color(0, 0, 153));
        txt_ID.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        txt_ID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_IDKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                .addGap(0, 221, Short.MAX_VALUE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(216, 216, 216))))
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(addComputerCourseBtn)
                        .addGap(5, 5, 5)
                        .addComponent(updateComputerCourseBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteComputerCourseBtn))
                    .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txt_ID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_timeName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(courseName, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(courseName1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(courseName1)
                        .addGap(5, 5, 5)
                        .addComponent(txt_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(courseName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addComputerCourseBtn)
                            .addComponent(updateComputerCourseBtn)
                            .addComponent(deleteComputerCourseBtn)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(110, 110, 110))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void addComputerCourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addComputerCourseBtnActionPerformed
        if(addDailyEntry()== true) {
            JOptionPane.showMessageDialog(this, "Service Added Sucessfully.");
            clearDailyEntryTable();
            setDailyEntry();
            clearAllInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed To Add Service..!!");
        }
    }//GEN-LAST:event_addComputerCourseBtnActionPerformed

    private void dailyEntryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dailyEntryTableMouseClicked
        int rowNo = dailyEntryTable.getSelectedRow();
        TableModel model = dailyEntryTable.getModel();
        
        txt_ID.setText(model.getValueAt(rowNo, 0).toString());
        txt_timeName.setText(model.getValueAt(rowNo, 1).toString());
        
    }//GEN-LAST:event_dailyEntryTableMouseClicked

    private void panelBorder1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelBorder1MouseClicked
        
    }//GEN-LAST:event_panelBorder1MouseClicked

    private void updateComputerCourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateComputerCourseBtnActionPerformed
     
        if(updateDailyEntry()== true) {
            JOptionPane.showMessageDialog(this, "Service Updated Sucessfully.");
            clearDailyEntryTable();
            setDailyEntry();
            clearAllInputFields();
            
        } else {
            JOptionPane.showMessageDialog(this, "Failed To Update Service...!!");
        }
    }//GEN-LAST:event_updateComputerCourseBtnActionPerformed

    private void deleteComputerCourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteComputerCourseBtnActionPerformed
        if(deleteDailyEntry()== true) {
            JOptionPane.showMessageDialog(this, "Service Deleted Successfully.");
            clearDailyEntryTable();
            setDailyEntry();
            clearAllInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Delete Service...!!");
        }
    }//GEN-LAST:event_deleteComputerCourseBtnActionPerformed

    private void txt_IDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_IDKeyPressed
        char c = evt.getKeyChar();
        if(Character.isLetter(c)) {
            txt_ID.setEditable(false);
        } else {
            txt_ID.setEditable(true);
        }
    }//GEN-LAST:event_txt_IDKeyPressed

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
            java.util.logging.Logger.getLogger(AddTimeTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddTimeTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddTimeTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddTimeTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddTimeTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addComputerCourseBtn;
    private javax.swing.JLabel courseName;
    private javax.swing.JLabel courseName1;
    private rojeru_san.complementos.RSTableMetro dailyEntryTable;
    private javax.swing.JButton deleteComputerCourseBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.ims.swing.PanelBorder panelBorder1;
    private app.bolivia.swing.JCTextField txt_ID;
    private app.bolivia.swing.JCTextField txt_timeName;
    private javax.swing.JButton updateComputerCourseBtn;
    // End of variables declaration//GEN-END:variables
}
