/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
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
public class AddCourse extends javax.swing.JFrame {

    String chooseSection, course_name;
    int courseID, courseFee, discountAmt;
    DefaultTableModel model;
    
   
    public AddCourse() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        setComputerCourse();
    }
    
//    Setting Computer Courses
    public void setComputerCourse() {
        
        try {
            

            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from courses");
            
            
            while(rs.next()) {
                int courseID = rs.getInt("id");
                String sectionName = rs.getString("choose_section");
                String courseName = rs.getString("course_name");
                int courseFee = rs.getInt("course_fee");
                
                Object[] obj = {courseID, sectionName, courseName, courseFee};
                model = (DefaultTableModel) courseTable.getModel();
                model.addRow(obj);
            }
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        
    }
    
//    To Add Computer Courses
    public boolean addComputerCourse() {
        
        boolean isAdded = false;
        courseID = Integer.parseInt(txt_courseID.getText());
        chooseSection = txt_chooseSection.getSelectedItem().toString();
        course_name = txt_courseName.getText();
        courseFee = Integer.parseInt(txt_coursefeeAmt.getText());

        
        try {
            
            Connection con = DBConnection.getConnection();
            
            String sql = "insert into courses values(?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, courseID);
            pst.setString(2, chooseSection);
            pst.setString(3, course_name);
            pst.setInt(4, courseFee);

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
    public void clearComputerCourseTable() {
        
        DefaultTableModel model = (DefaultTableModel) courseTable.getModel();
        model.setRowCount(0);
        
    }
    
//  To Update Computer Courses    
    public boolean updateComputerCourse() {
        
        boolean isUpdated = false;
        courseID = Integer.parseInt(txt_courseID.getText());
        chooseSection = txt_chooseSection.getSelectedItem().toString();
        course_name = txt_courseName.getText();
        courseFee = Integer.parseInt(txt_coursefeeAmt.getText());
        
        try {
            
            Connection con = DBConnection.getConnection();
            String sql = "update courses set choose_section = ?, course_name = ?, course_fee = ? where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, chooseSection);
            pst.setString(2, course_name);
            pst.setInt(3, courseFee);
            pst.setInt(4, courseID);
            
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
    public boolean deleteCoumputerCourse() {
        
        boolean isDeleted = false;
        courseID = Integer.parseInt(txt_courseID.getText());
        
        try {
            
            Connection con = DBConnection.getConnection();
            String sql = "delete from courses where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, courseID);
            
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
        
       
        txt_courseID.setText("");
        txt_chooseSection.setSelectedIndex(0);
        txt_courseName.setText("");
        txt_coursefeeAmt.setText("");
        
        
    
        
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
        txt_courseName = new app.bolivia.swing.JCTextField();
        courseName = new javax.swing.JLabel();
        txt_coursefeeAmt = new app.bolivia.swing.JCTextField();
        jLabel4 = new javax.swing.JLabel();
        addComputerCourseBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        courseTable = new rojeru_san.complementos.RSTableMetro();
        updateComputerCourseBtn = new javax.swing.JButton();
        deleteComputerCourseBtn = new javax.swing.JButton();
        courseName1 = new javax.swing.JLabel();
        txt_courseID = new app.bolivia.swing.JCTextField();
        txt_chooseSection = new rojerusan.RSComboMetro();
        courseName2 = new javax.swing.JLabel();

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
        jLabel2.setText("ADD COURSE");

        txt_courseName.setBackground(new java.awt.Color(255, 255, 255));
        txt_courseName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 1, new java.awt.Color(0, 102, 255)));
        txt_courseName.setForeground(new java.awt.Color(0, 0, 153));
        txt_courseName.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N

        courseName.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        courseName.setForeground(new java.awt.Color(0, 0, 0));
        courseName.setText("Enter Course Name");

        txt_coursefeeAmt.setBackground(new java.awt.Color(255, 255, 255));
        txt_coursefeeAmt.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 1, new java.awt.Color(0, 102, 255)));
        txt_coursefeeAmt.setForeground(new java.awt.Color(0, 0, 153));
        txt_coursefeeAmt.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Enter Fee Ammount");

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

        courseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Section", "Course Name", "Course Fee"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        courseTable.setColorBackgoundHead(new java.awt.Color(51, 102, 0));
        courseTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        courseTable.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        courseTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        courseTable.setRowHeight(40);
        courseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                courseTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(courseTable);
        if (courseTable.getColumnModel().getColumnCount() > 0) {
            courseTable.getColumnModel().getColumn(0).setMinWidth(50);
            courseTable.getColumnModel().getColumn(0).setMaxWidth(100);
            courseTable.getColumnModel().getColumn(1).setMinWidth(200);
            courseTable.getColumnModel().getColumn(1).setMaxWidth(600);
            courseTable.getColumnModel().getColumn(2).setMinWidth(150);
            courseTable.getColumnModel().getColumn(2).setMaxWidth(500);
            courseTable.getColumnModel().getColumn(3).setMinWidth(150);
            courseTable.getColumnModel().getColumn(3).setMaxWidth(400);
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
        courseName1.setText("Enter Course ID");

        txt_courseID.setBackground(new java.awt.Color(255, 255, 255));
        txt_courseID.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 1, new java.awt.Color(0, 102, 255)));
        txt_courseID.setForeground(new java.awt.Color(0, 0, 153));
        txt_courseID.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N

        txt_chooseSection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Computer", "Tuition", "Language", "Test Preparation" }));
        txt_chooseSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_chooseSectionActionPerformed(evt);
            }
        });

        courseName2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        courseName2.setForeground(new java.awt.Color(0, 0, 0));
        courseName2.setText("Choose Section");

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
                .addGap(50, 50, 50)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(addComputerCourseBtn)
                        .addGap(5, 5, 5)
                        .addComponent(updateComputerCourseBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteComputerCourseBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_courseID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(courseName1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                            .addComponent(txt_courseName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_coursefeeAmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(courseName, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                            .addComponent(txt_chooseSection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(courseName2, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                        .addGap(57, 57, 57)
                        .addComponent(jScrollPane1)
                        .addGap(50, 50, 50))))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(courseName1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_courseID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(courseName2)
                        .addGap(4, 4, 4)
                        .addComponent(txt_chooseSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(courseName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_courseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_coursefeeAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addComputerCourseBtn)
                    .addComponent(updateComputerCourseBtn)
                    .addComponent(deleteComputerCourseBtn))
                .addGap(27, 27, 27))
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
        if(addComputerCourse()== true) {
            JOptionPane.showMessageDialog(this, "Course Added Sucessfully.");
            clearComputerCourseTable();
            setComputerCourse();
            clearAllInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed To Add Course..!!");
        }
    }//GEN-LAST:event_addComputerCourseBtnActionPerformed

    private void courseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseTableMouseClicked
        int rowNo = courseTable.getSelectedRow();
        TableModel model = courseTable.getModel();
        
        txt_courseID.setText(model.getValueAt(rowNo, 0).toString());
        txt_chooseSection.setSelectedItem(model.getValueAt(rowNo, 1).toString());
        txt_courseName.setText(model.getValueAt(rowNo, 2).toString());
        txt_coursefeeAmt.setText(model.getValueAt(rowNo, 3).toString());
        
    }//GEN-LAST:event_courseTableMouseClicked

    private void panelBorder1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelBorder1MouseClicked
        
    }//GEN-LAST:event_panelBorder1MouseClicked

    private void updateComputerCourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateComputerCourseBtnActionPerformed
     
        if(updateComputerCourse()== true) {
            JOptionPane.showMessageDialog(this, "Course Updated Sucessfully.");
            clearComputerCourseTable();
            setComputerCourse();
            clearAllInputFields();
            
        } else {
            JOptionPane.showMessageDialog(this, "Failed To Update Course...!!");
        }
    }//GEN-LAST:event_updateComputerCourseBtnActionPerformed

    private void deleteComputerCourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteComputerCourseBtnActionPerformed
        if(deleteCoumputerCourse() == true) {
            JOptionPane.showMessageDialog(this, "Course Deleted Successfully.");
            clearComputerCourseTable();
            setComputerCourse();
            clearAllInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Delete Course...!!");
        }
    }//GEN-LAST:event_deleteComputerCourseBtnActionPerformed

    private void txt_chooseSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_chooseSectionActionPerformed
        String selectedValue = txt_chooseSection.getSelectedItem().toString();
  
    }//GEN-LAST:event_txt_chooseSectionActionPerformed

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
            java.util.logging.Logger.getLogger(AddCourse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddCourse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddCourse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddCourse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddCourse().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addComputerCourseBtn;
    private javax.swing.JLabel courseName;
    private javax.swing.JLabel courseName1;
    private javax.swing.JLabel courseName2;
    private rojeru_san.complementos.RSTableMetro courseTable;
    private javax.swing.JButton deleteComputerCourseBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.ims.swing.PanelBorder panelBorder1;
    private rojerusan.RSComboMetro txt_chooseSection;
    private app.bolivia.swing.JCTextField txt_courseID;
    private app.bolivia.swing.JCTextField txt_courseName;
    private app.bolivia.swing.JCTextField txt_coursefeeAmt;
    private javax.swing.JButton updateComputerCourseBtn;
    // End of variables declaration//GEN-END:variables
}
