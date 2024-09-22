package com.ims.others;

import com.ims.add.AddDailyEntry;
import com.ims.connection.DBConnection;
import static com.ims.form.Computer.computerTable;
import static com.ims.others.Counseling.counsellorTable;
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

/**
 *
 * @author _Spoidey-
 */
public class DailyEntry extends javax.swing.JFrame {

    DefaultTableModel model;
    int mouseX, mouseY;

    public DailyEntry() {
        initComponents();
        setDailyEntryData();
        getServiceData();
        
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

    public void setDailyEntryData() {

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();
            ResultSet rs;
            rs = st.executeQuery("select * from dailyentry");

            while (rs.next()) {

                int id = rs.getInt("id");
                String customerName = rs.getString("customer_name");
                String reciverName = rs.getString("reciver_name");
                String entryDate = rs.getString("entry_date");
                String contact = rs.getString("contact");
                String address = rs.getString("address");
                String service_name = rs.getString("service_name");
                int charge = rs.getInt("service_charge");
                int paidAmount = rs.getInt("paid_amount");
                int dueAmount = rs.getInt("due_amount");

                Object[] obj = {id, customerName, reciverName, entryDate, contact, address, service_name, charge, paidAmount, dueAmount};
                model = (DefaultTableModel) dailyEntryTable.getModel();
                model.addRow(obj);
                
                int totalDueAmount = 0;
                int totalAmount = 0;
                
                for(int i = 0; i < dailyEntryTable.getRowCount(); i++) {
                    
                    int totalamount = (Integer) dailyEntryTable.getValueAt(i, 7);
                    totalAmount += totalamount;
                    
                    int dueamount = (Integer) dailyEntryTable.getValueAt(i, 9);
                    totalDueAmount += dueamount;
                    
                }
                
                jButton7.setText(String.valueOf(totalAmount));
                txt_totalDueAmount.setText(String.valueOf(dueAmount));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    
    public void getServiceData() {
        
        String serviceName;
        
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "select service_name from adddailyentry";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()) {
                serviceName = rs.getString("service_name");
                txt_serviceName.addItem(serviceName);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    

    public boolean addDailyEntry() {

        boolean isDailyEntered = false;
        String customerName = txt_Name.getText();
        String reciverName = txt_reciverName.getText();
        Date date = new Date();
        java.sql.Date queryDate = new java.sql.Date(date.getTime());
        String contact = txt_contact.getText();
        String address = txt_address.getText();
        String serviceName = txt_serviceName.getSelectedItem().toString();
        int charge = Integer.parseInt(txt_serviceCharge.getText());
        int paidAmount = Integer.parseInt(txt_paidAmount.getText());
        int dueAmount = charge - paidAmount;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "insert into dailyentry (customer_name, reciver_name, entry_date, contact, address, service_name, service_charge, paid_amount, due_amount) "
                    + "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, customerName);
            pst.setString(2, reciverName);
            pst.setDate(3, queryDate);
            pst.setString(4, contact);
            pst.setString(5, address);
            pst.setString(6, serviceName);
            pst.setInt(7, charge);
            pst.setInt(8, paidAmount);
            pst.setInt(9, dueAmount);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {

                isDailyEntered = true;

            } else {

                isDailyEntered = false;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return isDailyEntered;

    }
    
    public int getSelectId() {
        int selectedId = -1;
        
        int selectedRow = dailyEntryTable.getSelectedRow();
        
        if(selectedRow != -1) {
            selectedId = (int) dailyEntryTable.getValueAt(selectedRow, 0);
        }
        return selectedId;
    }

    public boolean updateDailyEntry() {
        boolean isDailyUpdated = false;

        String customerName = txt_Name.getText();
        String reciverName = txt_reciverName.getText();
        Date date = new Date();
        java.sql.Date queryDate = new java.sql.Date(date.getTime());
        String contact = txt_contact.getText();
        String address = txt_address.getText();
        String serviceName = txt_serviceName.getSelectedItem().toString();
        int charge = Integer.parseInt(txt_serviceCharge.getText());
        int paidAmount = Integer.parseInt(txt_paidAmount.getText());
        int dueAmount = 0;
        
        int selectedId = getSelectId();

        try {
            Connection con = DBConnection.getConnection();

            String sql = "update dailyentry set customer_name = ?, reciver_name = ? , entry_date = ?, contact = ?, address = ?, service_name = ?,"
                    + "service_charge = ?, paid_amount = ?, due_amount = ? where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, customerName);
            pst.setString(2, reciverName);
            pst.setDate(3, queryDate);
            pst.setString(4, contact);
            pst.setString(5, address);
            pst.setString(6, serviceName);
            pst.setInt(7, charge);
            pst.setInt(10, selectedId);

            

            ResultSet rs = pst.executeQuery("select paid_amount, due_amount from dailyentry");
            if (rs.next()) {
                
                int amount = Integer.parseInt(txt_paidAmount.getText());
                dueAmount = rs.getInt("due_amount") - amount;
                
                
                
                paidAmount = paidAmount + amount;
                
                pst.setInt(8, paidAmount);
                pst.setInt(9, dueAmount);
               
            }

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isDailyUpdated = true;
            } else {
                isDailyUpdated = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDailyUpdated;
    }

    public boolean deleteDailyEntry() {
        boolean isDailyDeleted = false;
        int selectedId = getSelectId();

        try {

            Connection con = DBConnection.getConnection();
            String sql = "delete from dailyentry where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, selectedId);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isDailyDeleted = true;
            } else {
                isDailyDeleted = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDailyDeleted;
    }

    public void clearDailyEntry() {

        txt_Name.setText("");
        txt_reciverName.setText("");
        txt_contact.setText("");
        txt_address.setText("");
        txt_serviceName.setSelectedIndex(0);
        txt_serviceCharge.setText("");
        txt_paidAmount.setText("");

    }

    public void clearDailyTable() {

        DefaultTableModel model = (DefaultTableModel) dailyEntryTable.getModel();
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
        txt_Name = new app.bolivia.swing.JCTextField();
        signupTitle16 = new javax.swing.JLabel();
        signupTitle20 = new javax.swing.JLabel();
        txt_reciverName = new app.bolivia.swing.JCTextField();
        signupTitle12 = new javax.swing.JLabel();
        txt_contact = new app.bolivia.swing.JCTextField();
        signupTitle15 = new javax.swing.JLabel();
        signupTitle21 = new javax.swing.JLabel();
        txt_serviceCharge = new app.bolivia.swing.JCTextField();
        addBtn = new necesario.RSMaterialButtonCircle();
        updateBtn = new necesario.RSMaterialButtonCircle();
        deleteBtn = new necesario.RSMaterialButtonCircle();
        signupTitle13 = new javax.swing.JLabel();
        txt_address = new app.bolivia.swing.JCTextField();
        txt_paidAmount = new app.bolivia.swing.JCTextField();
        signupTitle23 = new javax.swing.JLabel();
        txt_serviceName = new rojerusan.RSComboMetro();
        AddDailyEntryBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        dailyEntryTable = new rojeru_san.complementos.RSTableMetro();
        jLabel3 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        txt_totalDueAmount = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        searchBtn = new app.bolivia.swing.JCTextField();
        printBtn = new javax.swing.JButton();
        exportBtn = new javax.swing.JButton();
        billBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DAILY ENTRY PAGE");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 550, Short.MAX_VALUE)
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
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        txt_Name.setBackground(new java.awt.Color(255, 255, 255));
        txt_Name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_Name.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_Name.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_Name.setPhColor(new java.awt.Color(204, 204, 204));
        txt_Name.setPlaceholder(" ");
        txt_Name.setSelectionColor(new java.awt.Color(204, 204, 204));
        txt_Name.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_NameFocusLost(evt);
            }
        });
        txt_Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_NameActionPerformed(evt);
            }
        });

        signupTitle16.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle16.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle16.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle16.setText("Customer Name");

        signupTitle20.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle20.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle20.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle20.setText("Reciver Name");

        txt_reciverName.setBackground(new java.awt.Color(255, 255, 255));
        txt_reciverName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_reciverName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_reciverName.setPhColor(new java.awt.Color(204, 204, 204));
        txt_reciverName.setPlaceholder(" ");
        txt_reciverName.setSelectionColor(new java.awt.Color(204, 204, 204));
        txt_reciverName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_reciverNameFocusLost(evt);
            }
        });
        txt_reciverName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_reciverNameActionPerformed(evt);
            }
        });

        signupTitle12.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle12.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle12.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle12.setText("Customer Contact");

        txt_contact.setBackground(new java.awt.Color(255, 255, 255));
        txt_contact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_contact.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_contact.setPhColor(new java.awt.Color(204, 204, 204));
        txt_contact.setSelectionColor(new java.awt.Color(204, 204, 204));
        txt_contact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_contactFocusLost(evt);
            }
        });
        txt_contact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_contactActionPerformed(evt);
            }
        });

        signupTitle15.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle15.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle15.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle15.setText("Service Name");

        signupTitle21.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle21.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle21.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle21.setText("Service Charge");

        txt_serviceCharge.setBackground(new java.awt.Color(255, 255, 255));
        txt_serviceCharge.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_serviceCharge.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_serviceCharge.setPhColor(new java.awt.Color(204, 204, 204));
        txt_serviceCharge.setSelectionColor(new java.awt.Color(204, 204, 204));
        txt_serviceCharge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_serviceChargeFocusLost(evt);
            }
        });
        txt_serviceCharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_serviceChargeActionPerformed(evt);
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

        signupTitle13.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle13.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle13.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle13.setText("Customer Address");

        txt_address.setBackground(new java.awt.Color(255, 255, 255));
        txt_address.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_address.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_address.setPhColor(new java.awt.Color(204, 204, 204));
        txt_address.setSelectionColor(new java.awt.Color(204, 204, 204));
        txt_address.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_addressFocusLost(evt);
            }
        });
        txt_address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_addressActionPerformed(evt);
            }
        });

        txt_paidAmount.setBackground(new java.awt.Color(255, 255, 255));
        txt_paidAmount.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        txt_paidAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_paidAmount.setPhColor(new java.awt.Color(204, 204, 204));
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

        signupTitle23.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle23.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        signupTitle23.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle23.setText("Paid Amount");

        AddDailyEntryBtn.setBackground(new java.awt.Color(102, 102, 255));
        AddDailyEntryBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        AddDailyEntryBtn.setForeground(new java.awt.Color(255, 255, 255));
        AddDailyEntryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/add.png"))); // NOI18N
        AddDailyEntryBtn.setText("Add Daily Entry");
        AddDailyEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddDailyEntryBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder2Layout = new javax.swing.GroupLayout(panelBorder2);
        panelBorder2.setLayout(panelBorder2Layout);
        panelBorder2Layout.setHorizontalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(signupTitle23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_paidAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addComponent(signupTitle16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupTitle20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_reciverName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupTitle12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_contact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupTitle15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupTitle21, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addComponent(txt_serviceCharge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_address, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupTitle13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_serviceName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddDailyEntryBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        panelBorder2Layout.setVerticalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(signupTitle16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(signupTitle20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_reciverName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(signupTitle12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(signupTitle13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(signupTitle15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_serviceName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(signupTitle21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_serviceCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(signupTitle23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_paidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(AddDailyEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        dailyEntryTable.setAutoCreateRowSorter(true);
        dailyEntryTable.setForeground(new java.awt.Color(255, 255, 255));
        dailyEntryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S.N", "Customer Name", "Reciver Name", "Entry Date", "Contact", "Address", "Service", "Charge", "Paid Amount", "Due Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dailyEntryTable.setAutoResizeMode(0);
        dailyEntryTable.setColorBackgoundHead(new java.awt.Color(51, 153, 0));
        dailyEntryTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        dailyEntryTable.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        dailyEntryTable.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        dailyEntryTable.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        dailyEntryTable.setRowHeight(40);
        dailyEntryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dailyEntryTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(dailyEntryTable);
        if (dailyEntryTable.getColumnModel().getColumnCount() > 0) {
            dailyEntryTable.getColumnModel().getColumn(0).setMinWidth(70);
            dailyEntryTable.getColumnModel().getColumn(0).setMaxWidth(150);
            dailyEntryTable.getColumnModel().getColumn(1).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(1).setMaxWidth(500);
            dailyEntryTable.getColumnModel().getColumn(2).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(2).setMaxWidth(500);
            dailyEntryTable.getColumnModel().getColumn(3).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(3).setMaxWidth(500);
            dailyEntryTable.getColumnModel().getColumn(4).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(4).setMaxWidth(500);
            dailyEntryTable.getColumnModel().getColumn(5).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(5).setMaxWidth(500);
            dailyEntryTable.getColumnModel().getColumn(6).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(6).setMaxWidth(500);
            dailyEntryTable.getColumnModel().getColumn(7).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(7).setMaxWidth(500);
            dailyEntryTable.getColumnModel().getColumn(8).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(8).setMaxWidth(500);
            dailyEntryTable.getColumnModel().getColumn(9).setMinWidth(200);
            dailyEntryTable.getColumnModel().getColumn(9).setMaxWidth(500);
        }

        jLabel3.setBackground(new java.awt.Color(0, 102, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 153, 51));
        jLabel3.setText("Total Amount");

        jButton7.setBackground(new java.awt.Color(0, 102, 0));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton7.setForeground(new java.awt.Color(153, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        jButton7.setText("0");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        txt_totalDueAmount.setBackground(new java.awt.Color(255, 102, 102));
        txt_totalDueAmount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txt_totalDueAmount.setForeground(new java.awt.Color(153, 255, 255));
        txt_totalDueAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        txt_totalDueAmount.setText("0");
        txt_totalDueAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalDueAmountActionPerformed(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(255, 102, 102));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 102, 102));
        jLabel6.setText("Total Due Amont");

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

        printBtn.setText("print");
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });

        exportBtn.setText("export");
        exportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportBtnActionPerformed(evt);
            }
        });

        billBtn.setText("Bill");

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_totalDueAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(printBtn)
                                .addGap(18, 18, 18)
                                .addComponent(exportBtn)
                                .addGap(18, 18, 18)
                                .addComponent(billBtn)
                                .addGap(414, 414, 414)
                                .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(printBtn)
                            .addComponent(exportBtn)
                            .addComponent(billBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jButton7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txt_totalDueAmount))))
                    .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1578, 984));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void dailyEntryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dailyEntryTableMouseClicked
        int rowNo = dailyEntryTable.getSelectedRow();
        TableModel model = dailyEntryTable.getModel();

        txt_Name.setText(model.getValueAt(rowNo, 1).toString());
        txt_reciverName.setText(model.getValueAt(rowNo, 2).toString());
        txt_contact.setText(model.getValueAt(rowNo, 4).toString());
        txt_address.setText(model.getValueAt(rowNo, 5).toString());
        String serviceName = model.getValueAt(rowNo, 3).toString();
        txt_serviceName.setSelectedItem(serviceName);
        txt_serviceCharge.setText(model.getValueAt(rowNo, 7).toString());
        txt_paidAmount.setText(model.getValueAt(rowNo, 8).toString());


    }//GEN-LAST:event_dailyEntryTableMouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked

    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void txt_NameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_NameFocusLost

    }//GEN-LAST:event_txt_NameFocusLost

    private void txt_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_NameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_NameActionPerformed

    private void txt_reciverNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_reciverNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_reciverNameFocusLost

    private void txt_reciverNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_reciverNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_reciverNameActionPerformed

    private void txt_contactFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_contactFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_contactFocusLost

    private void txt_contactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_contactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_contactActionPerformed

    private void txt_serviceChargeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_serviceChargeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_serviceChargeFocusLost

    private void txt_serviceChargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_serviceChargeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_serviceChargeActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (updateDailyEntry()== true) {
            JOptionPane.showMessageDialog(this, "Updated Successfully...");
            clearDailyTable();
            setDailyEntryData();
            clearDailyEntry();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Update...!!");
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (deleteDailyEntry() == true) {
            JOptionPane.showMessageDialog(this, "Deleted Successfully...");
            clearDailyTable();
            setDailyEntryData();
            clearDailyEntry();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Delete...!!");
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txt_addressFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_addressFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_addressFocusLost

    private void txt_addressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_addressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_addressActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        if (addDailyEntry()== true) {
            JOptionPane.showMessageDialog(this, "Added Successfully...");
            clearDailyTable();
            setDailyEntryData();
            clearDailyEntry();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Add...!!");
        }
    }//GEN-LAST:event_addBtnActionPerformed

    private void txt_totalDueAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalDueAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalDueAmountActionPerformed

    private void txt_paidAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_paidAmountFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_paidAmountFocusLost

    private void txt_paidAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_paidAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_paidAmountActionPerformed

    private void AddDailyEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddDailyEntryBtnActionPerformed
        AddDailyEntry addDailyEntry = new AddDailyEntry();
        addDailyEntry.setVisible(true);
    }//GEN-LAST:event_AddDailyEntryBtnActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        MessageFormat header = new MessageFormat("Daily Entries Details");
        MessageFormat footer = new MessageFormat("Page: ({0,number,integer})");
        try {
            PrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
            set.add(OrientationRequested.PORTRAIT);
            dailyEntryTable.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, set, true);
            JOptionPane.showConfirmDialog(null, "\n" + "Printed successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\n" + "Failed" + "\n" + e);
        }
    }//GEN-LAST:event_printBtnActionPerformed

    private void exportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportBtnActionPerformed
        try {

            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(this);
            File saveFile = jFileChooser.getSelectedFile();

            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Daily Entry");

                Row rowCol = sheet.createRow(0);

                for (int i = 0; i < dailyEntryTable.getColumnCount(); i++) {
                    Cell cell = rowCol.createCell(i);
                    cell.setCellValue(dailyEntryTable.getColumnName(i));
                }

                for (int j = 0; j < dailyEntryTable.getRowCount(); j++) {
                    Row row = sheet.createRow(j);
                    for (int k = 0; k < dailyEntryTable.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (dailyEntryTable.getValueAt(j, k) != null) {
                            cell.setCellValue(dailyEntryTable.getValueAt(j, k).toString());
                        }
                    }
                }
                FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                wb.write(out);
                wb.close();
                out.close();
                openfile(saveFile.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Error to get data in Excel File");
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }//GEN-LAST:event_exportBtnActionPerformed

    
    public void search(String str) {
        if (str == null) {
            return;
        }
        model = (DefaultTableModel) dailyEntryTable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        dailyEntryTable.setRowSorter(trs);
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
            java.util.logging.Logger.getLogger(DailyEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DailyEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DailyEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DailyEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DailyEntry().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddDailyEntryBtn;
    private necesario.RSMaterialButtonCircle addBtn;
    private javax.swing.JButton billBtn;
    public static rojeru_san.complementos.RSTableMetro dailyEntryTable;
    private necesario.RSMaterialButtonCircle deleteBtn;
    private javax.swing.JButton exportBtn;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private com.ims.swing.PanelBorder panelBorder2;
    private com.ims.swing.PanelBorder panelHeader;
    private javax.swing.JButton printBtn;
    private app.bolivia.swing.JCTextField searchBtn;
    private javax.swing.JLabel signupTitle12;
    private javax.swing.JLabel signupTitle13;
    private javax.swing.JLabel signupTitle15;
    private javax.swing.JLabel signupTitle16;
    private javax.swing.JLabel signupTitle20;
    private javax.swing.JLabel signupTitle21;
    private javax.swing.JLabel signupTitle23;
    private app.bolivia.swing.JCTextField txt_Name;
    private app.bolivia.swing.JCTextField txt_address;
    private app.bolivia.swing.JCTextField txt_contact;
    private app.bolivia.swing.JCTextField txt_paidAmount;
    private app.bolivia.swing.JCTextField txt_reciverName;
    private app.bolivia.swing.JCTextField txt_serviceCharge;
    private rojerusan.RSComboMetro txt_serviceName;
    private javax.swing.JButton txt_totalDueAmount;
    private necesario.RSMaterialButtonCircle updateBtn;
    // End of variables declaration//GEN-END:variables

    private void openfile(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
