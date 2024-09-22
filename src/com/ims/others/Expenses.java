package com.ims.others;

import com.ims.connection.DBConnection;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Date;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Expenses extends javax.swing.JFrame {

    DefaultTableModel model;
    int mouseX, mouseY;
    
    public Expenses() {
        initComponents();
        setServiceData();
        panelHeader.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        panelHeader.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                // Get the current location of the frame
                Point frameLocation = getLocation();
                // Calculate new location based on mouse movement
                int newX = frameLocation.x + e.getX() - mouseX;
                int newY = frameLocation.y + e.getY() - mouseY;
                // Set the new location for the frame
                setLocation(newX, newY);
            }
        });
    }

    public void setServiceData() {

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();
            ResultSet rs;
            rs = st.executeQuery("select * from expenses");

            while (rs.next()) {

                int id = rs.getInt("id");
                String paidTo = rs.getString("paid_to");
                String date = rs.getString("date");
                int amount = rs.getInt("amount");
                String remarks = rs.getString("remarks");
                

                Object[] obj = {id, paidTo, date, amount, remarks};
                model = (DefaultTableModel) expensesTable.getModel();
                model.addRow(obj);

            }
            
            int totalexpensesamount = 0;
            for(int i = 0; i < expensesTable.getRowCount(); i++) {
                int amt = (Integer) expensesTable.getValueAt(i, 3);
                totalexpensesamount += amt;
            }
            
            txt_totalExpenses.setText(String.valueOf(totalexpensesamount));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean addServices() {

        boolean isEntered = false;
        String paidTo = txt_paidTo.getText();
        Date date = new Date();
        java.sql.Date queryDate = new java.sql.Date(date.getTime());
        int amount = Integer.parseInt(txt_paidAmount.getText());
        String remarks = txt_remarks.getText();
        
      

        try {

            Connection con = DBConnection.getConnection();

            String sql = "insert into expenses (paid_to, date, amount, remarks) "
                    + "values(?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, paidTo);
            pst.setDate(2, queryDate);
            pst.setInt(3, amount);
            pst.setString(4, remarks);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {

                isEntered = true;

            } else {

                isEntered = false;

            }
            
            
            

        } catch (Exception e) {

            e.printStackTrace();

        }

        return isEntered;

    }
    
    
    public int getSelectId() {
        int selectedId = -1;
        
        int selectedRow = expensesTable.getSelectedRow();
        
        if(selectedRow != -1) {
            selectedId = (int) expensesTable.getValueAt(selectedRow, 0);
        }
        return selectedId;
    }

    public boolean updateServices() {
        boolean isUpdated = false;

        String paidTo = txt_paidTo.getText();
        int amount = Integer.parseInt(txt_paidAmount.getText());
        String remarks = txt_remarks.getText();

        try {
            Connection con = DBConnection.getConnection();
            int getSelectedId = getSelectId();
            String sql = "update expenses set paid_to = ?, amount = ?, remarks = ?"
                    + "where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, paidTo);
            pst.setInt(2, amount);
            pst.setString(3, remarks);
            pst.setInt(4, getSelectedId);
           

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isUpdated = true;
            } else {
                isUpdated = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    public boolean deleteServices() {
        boolean isDeleted = false;
        String name = txt_paidTo.getText();
        

        try {

            Connection con = DBConnection.getConnection();
            int getSelectedId = getSelectId();
            String sql = "delete from expenses where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setInt(1, getSelectedId);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isDeleted = true;
            } else {
                isDeleted = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    public void clearServicesData() {

        txt_paidTo.setText("");
        txt_paidAmount.setText("");
        txt_remarks.setText("");

    }

    public void clearServicesTable() {

        DefaultTableModel model = (DefaultTableModel) expensesTable.getModel();
        model.setRowCount(0);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new com.ims.swing.PanelBorder();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        panelBorder2 = new com.ims.swing.PanelBorder();
        txt_paidTo = new app.bolivia.swing.JCTextField();
        signupTitle16 = new javax.swing.JLabel();
        signupTitle20 = new javax.swing.JLabel();
        txt_paidAmount = new app.bolivia.swing.JCTextField();
        addBtn = new necesario.RSMaterialButtonCircle();
        updateBtn = new necesario.RSMaterialButtonCircle();
        deleteBtn = new necesario.RSMaterialButtonCircle();
        signupTitle23 = new javax.swing.JLabel();
        txt_remarks = new app.bolivia.swing.JCTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        expensesTable = new rojeru_san.complementos.RSTableMetro();
        txt_totalExpenses = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        searchBtn = new app.bolivia.swing.JCTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("EXPENSES PAGE");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/close.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/backarrow.png"))); // NOI18N
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 456, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(244, 244, 244)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        txt_paidTo.setBackground(new java.awt.Color(255, 255, 255));
        txt_paidTo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_paidTo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_paidTo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_paidTo.setPhColor(new java.awt.Color(204, 204, 204));
        txt_paidTo.setPlaceholder(" ");
        txt_paidTo.setSelectionColor(new java.awt.Color(204, 204, 204));
        txt_paidTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_paidToFocusLost(evt);
            }
        });
        txt_paidTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_paidToActionPerformed(evt);
            }
        });

        signupTitle16.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle16.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle16.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle16.setText("Paid for / Paid To");

        signupTitle20.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle20.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle20.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle20.setText("Paid Amount");

        txt_paidAmount.setBackground(new java.awt.Color(255, 255, 255));
        txt_paidAmount.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_paidAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_paidAmount.setPhColor(new java.awt.Color(204, 204, 204));
        txt_paidAmount.setPlaceholder(" ");
        txt_paidAmount.setSelectionColor(new java.awt.Color(204, 204, 204));
        txt_paidAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_paidAmountFocusLost(evt);
            }
        });
        txt_paidAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_paidAmountActionPerformed(evt);
            }
        });

        addBtn.setBackground(new java.awt.Color(102, 204, 0));
        addBtn.setText("ADD");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        updateBtn.setBackground(new java.awt.Color(204, 204, 0));
        updateBtn.setText("UPDATE");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        deleteBtn.setBackground(new java.awt.Color(204, 51, 0));
        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        signupTitle23.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle23.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle23.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle23.setText("Remarks");

        txt_remarks.setBackground(new java.awt.Color(255, 255, 255));
        txt_remarks.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_remarks.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_remarks.setPhColor(new java.awt.Color(204, 204, 204));
        txt_remarks.setSelectionColor(new java.awt.Color(204, 204, 204));
        txt_remarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_remarksFocusLost(evt);
            }
        });
        txt_remarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_remarksActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder2Layout = new javax.swing.GroupLayout(panelBorder2);
        panelBorder2.setLayout(panelBorder2Layout);
        panelBorder2Layout.setHorizontalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                        .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(signupTitle16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_paidTo, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                        .addComponent(signupTitle20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_paidAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(signupTitle23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_remarks, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        panelBorder2Layout.setVerticalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(signupTitle16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_paidTo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(signupTitle20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_paidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(signupTitle23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_remarks, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(204, 204, 204)));
        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        expensesTable.setAutoCreateRowSorter(true);
        expensesTable.setForeground(new java.awt.Color(255, 255, 255));
        expensesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S.N", "Paid for / Paid to", "Paid Date", "Paid Amount", "Remarks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expensesTable.setAutoResizeMode(0);
        expensesTable.setColorBackgoundHead(new java.awt.Color(51, 153, 0));
        expensesTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        expensesTable.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        expensesTable.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        expensesTable.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        expensesTable.setRowHeight(40);
        expensesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expensesTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(expensesTable);
        if (expensesTable.getColumnModel().getColumnCount() > 0) {
            expensesTable.getColumnModel().getColumn(0).setMinWidth(70);
            expensesTable.getColumnModel().getColumn(0).setMaxWidth(150);
            expensesTable.getColumnModel().getColumn(1).setMinWidth(300);
            expensesTable.getColumnModel().getColumn(1).setMaxWidth(500);
            expensesTable.getColumnModel().getColumn(2).setMinWidth(300);
            expensesTable.getColumnModel().getColumn(2).setMaxWidth(500);
            expensesTable.getColumnModel().getColumn(3).setMinWidth(300);
            expensesTable.getColumnModel().getColumn(3).setMaxWidth(500);
            expensesTable.getColumnModel().getColumn(4).setMinWidth(300);
            expensesTable.getColumnModel().getColumn(4).setMaxWidth(500);
        }

        txt_totalExpenses.setBackground(new java.awt.Color(255, 102, 102));
        txt_totalExpenses.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txt_totalExpenses.setForeground(new java.awt.Color(153, 255, 255));
        txt_totalExpenses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        txt_totalExpenses.setText("0");
        txt_totalExpenses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalExpensesActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 102, 102));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 0, 0));
        jLabel4.setText("Total Expenses Amount");

        searchBtn.setBackground(new java.awt.Color(255, 255, 255));
        searchBtn.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 0, 0)));
        searchBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        searchBtn.setPhColor(new java.awt.Color(0, 0, 51));
        searchBtn.setPlaceholder("Search Name....");
        searchBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchBtnKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelBorder2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 874, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_totalExpenses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(14, 14, 14))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(600, 600, 600)
                                        .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jScrollPane2))
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_totalExpenses))
                    .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1478, 943));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void expensesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expensesTableMouseClicked
        int rowNo = expensesTable.getSelectedRow();
        TableModel model = expensesTable.getModel();
        
        txt_paidTo.setText(model.getValueAt(rowNo, 1).toString());
        txt_paidAmount.setText(model.getValueAt(rowNo, 3).toString());
        txt_remarks.setText(model.getValueAt(rowNo, 4).toString());
        

    }//GEN-LAST:event_expensesTableMouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked

    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void txt_totalExpensesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalExpensesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalExpensesActionPerformed

    private void txt_remarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_remarksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_remarksActionPerformed

    private void txt_remarksFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_remarksFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_remarksFocusLost

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (deleteServices() == true) {
            JOptionPane.showMessageDialog(this, "Deleted Successfully...");
            clearServicesTable();
            setServiceData();
            clearServicesData();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Delete...!!");
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (updateServices() == true) {
            JOptionPane.showMessageDialog(this, "Updated Successfully...");
            clearServicesTable();
            setServiceData();
            clearServicesData();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Updated...!!");
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        if (addServices()== true) {
            JOptionPane.showMessageDialog(this, "Added Successfully...");
            clearServicesTable();
            setServiceData();
            clearServicesData();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Add...!!");
        }
    }//GEN-LAST:event_addBtnActionPerformed

    private void txt_paidAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_paidAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_paidAmountActionPerformed

    private void txt_paidAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_paidAmountFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_paidAmountFocusLost

    private void txt_paidToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_paidToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_paidToActionPerformed

    private void txt_paidToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_paidToFocusLost

    }//GEN-LAST:event_txt_paidToFocusLost

     public void search(String str) {
        if (str == null) {
            return;
        }
        model = (DefaultTableModel) expensesTable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        expensesTable.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }
    
    private void searchBtnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBtnKeyReleased
        try {
            String searchStudent = searchBtn.getText();
            search(searchStudent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_searchBtnKeyReleased

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
            java.util.logging.Logger.getLogger(Expenses.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Expenses.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Expenses.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Expenses.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new Expenses().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private necesario.RSMaterialButtonCircle addBtn;
    private necesario.RSMaterialButtonCircle deleteBtn;
    public static rojeru_san.complementos.RSTableMetro expensesTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private com.ims.swing.PanelBorder panelBorder2;
    private com.ims.swing.PanelBorder panelHeader;
    private app.bolivia.swing.JCTextField searchBtn;
    private javax.swing.JLabel signupTitle16;
    private javax.swing.JLabel signupTitle20;
    private javax.swing.JLabel signupTitle23;
    private app.bolivia.swing.JCTextField txt_paidAmount;
    private app.bolivia.swing.JCTextField txt_paidTo;
    private app.bolivia.swing.JCTextField txt_remarks;
    private javax.swing.JButton txt_totalExpenses;
    private necesario.RSMaterialButtonCircle updateBtn;
    // End of variables declaration//GEN-END:variables

    private void openfile(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
