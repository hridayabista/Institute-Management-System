package com.ims.others;

import com.ims.connection.DBConnection;
import static com.ims.form.Computer.computerTable;
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
public class Counseling extends javax.swing.JFrame {

    DefaultTableModel model;
    int mouseX, mouseY;

    /**
     * Creates new form DailyEntry
     */
    public Counseling() {
        initComponents();
        setCounselingData();

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

    public void setCounselingData() {

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();
            ResultSet rs;
            rs = st.executeQuery("select * from counseling");

            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String date = rs.getString("date");
                String contact = rs.getString("contact");
                String address = rs.getString("address");
                String reason = rs.getString("reason");

                Object[] obj = {id, name, date, contact, address, reason};
                model = (DefaultTableModel) counsellorTable.getModel();
                model.addRow(obj);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean addCounsellors() {

        boolean isEntered = false;
        String name = counsellorName.getText();
        Date date = new Date();
        java.sql.Date queryDate = new java.sql.Date(date.getTime());
        String contact = counsellorContact.getText();
        String address = counsellorAdress.getText();
        String reason = counselingReason.getText();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "insert into counseling (name, date, contact, address, reason) "
                    + "values(?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setDate(2, queryDate);
            pst.setString(3, contact);
            pst.setString(4, address);
            pst.setString(5, reason);

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

        int selectedRow = counsellorTable.getSelectedRow();

        if (selectedRow != -1) {
            selectedId = (int) counsellorTable.getValueAt(selectedRow, 0);
        }
        return selectedId;
    }

    public boolean updateCounsellors() {
        boolean isUpdated = false;

        String name = counsellorName.getText();
        String contact = counsellorContact.getText();
        String address = counsellorAdress.getText();
        String reason = counselingReason.getText();

        try {
            Connection con = DBConnection.getConnection();

            int getSelectedId = getSelectId();

            String sql = "update counseling set name = ?, contact = ?, address = ?, reason = ? where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, name);
            pst.setString(2, contact);
            pst.setString(3, address);
            pst.setString(4, reason);
            pst.setInt(5, getSelectedId);

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

    public boolean deleteCounselors() {
        boolean isDeleted = false;
        String name = counsellorName.getText();
        String contact = counsellorContact.getText();

        try {

            Connection con = DBConnection.getConnection();
            String sql = "delete from counseling where name = ? AND contact = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, name);
            pst.setString(2, contact);

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

    public void clearCounselorsData() {

        counsellorName.setText("");
        counsellorContact.setText("");
        counsellorAdress.setText("");
        counselingReason.setText("");

    }

    public void clearCounsellorTable() {

        DefaultTableModel model = (DefaultTableModel) counsellorTable.getModel();
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
        counsellorName = new app.bolivia.swing.JCTextField();
        signupTitle16 = new javax.swing.JLabel();
        signupTitle12 = new javax.swing.JLabel();
        counsellorContact = new app.bolivia.swing.JCTextField();
        signupTitle15 = new javax.swing.JLabel();
        counsellorAdress = new app.bolivia.swing.JCTextField();
        signupTitle21 = new javax.swing.JLabel();
        counselingReason = new app.bolivia.swing.JCTextField();
        addBtn = new necesario.RSMaterialButtonCircle();
        updateBtn = new necesario.RSMaterialButtonCircle();
        deleteBtn = new necesario.RSMaterialButtonCircle();
        jScrollPane2 = new javax.swing.JScrollPane();
        counsellorTable = new rojeru_san.complementos.RSTableMetro();
        searchBtn = new app.bolivia.swing.JCTextField();
        printBtn = new javax.swing.JButton();
        exportBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("COUNSELING PAGE");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 370, Short.MAX_VALUE)
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

        counsellorName.setBackground(new java.awt.Color(255, 255, 255));
        counsellorName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        counsellorName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        counsellorName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        counsellorName.setPhColor(new java.awt.Color(204, 204, 204));
        counsellorName.setPlaceholder(" ");
        counsellorName.setSelectionColor(new java.awt.Color(204, 204, 204));
        counsellorName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                counsellorNameFocusLost(evt);
            }
        });
        counsellorName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                counsellorNameActionPerformed(evt);
            }
        });

        signupTitle16.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle16.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        signupTitle16.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle16.setText("Visitor Name");

        signupTitle12.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle12.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        signupTitle12.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle12.setText("Visitor Contact");

        counsellorContact.setBackground(new java.awt.Color(255, 255, 255));
        counsellorContact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        counsellorContact.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        counsellorContact.setPhColor(new java.awt.Color(204, 204, 204));
        counsellorContact.setSelectionColor(new java.awt.Color(204, 204, 204));
        counsellorContact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                counsellorContactFocusLost(evt);
            }
        });
        counsellorContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                counsellorContactActionPerformed(evt);
            }
        });

        signupTitle15.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle15.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        signupTitle15.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle15.setText("Visitor Address");

        counsellorAdress.setBackground(new java.awt.Color(255, 255, 255));
        counsellorAdress.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        counsellorAdress.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        counsellorAdress.setPhColor(new java.awt.Color(204, 204, 204));
        counsellorAdress.setSelectionColor(new java.awt.Color(204, 204, 204));
        counsellorAdress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                counsellorAdressFocusLost(evt);
            }
        });
        counsellorAdress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                counsellorAdressActionPerformed(evt);
            }
        });

        signupTitle21.setBackground(new java.awt.Color(255, 255, 255));
        signupTitle21.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        signupTitle21.setForeground(new java.awt.Color(102, 102, 255));
        signupTitle21.setText("Reason for Visiting");

        counselingReason.setBackground(new java.awt.Color(255, 255, 255));
        counselingReason.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 153, 255)));
        counselingReason.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        counselingReason.setPhColor(new java.awt.Color(204, 204, 204));
        counselingReason.setSelectionColor(new java.awt.Color(204, 204, 204));
        counselingReason.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                counselingReasonFocusLost(evt);
            }
        });
        counselingReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                counselingReasonActionPerformed(evt);
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

        javax.swing.GroupLayout panelBorder2Layout = new javax.swing.GroupLayout(panelBorder2);
        panelBorder2.setLayout(panelBorder2Layout);
        panelBorder2Layout.setHorizontalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(signupTitle16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(counsellorName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupTitle12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(counsellorContact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupTitle15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(counsellorAdress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupTitle21, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addComponent(counselingReason, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        panelBorder2Layout.setVerticalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(signupTitle16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(counsellorName, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(signupTitle12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(counsellorContact, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(signupTitle15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(counsellorAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(signupTitle21, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(counselingReason, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77))
        );

        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(204, 204, 204)));
        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        counsellorTable.setAutoCreateRowSorter(true);
        counsellorTable.setForeground(new java.awt.Color(255, 255, 255));
        counsellorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S.N", "Name", "Date", "Contact", "Address", "Reason"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        counsellorTable.setAutoResizeMode(0);
        counsellorTable.setColorBackgoundHead(new java.awt.Color(51, 153, 0));
        counsellorTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        counsellorTable.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        counsellorTable.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        counsellorTable.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        counsellorTable.setRowHeight(40);
        counsellorTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                counsellorTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(counsellorTable);
        if (counsellorTable.getColumnModel().getColumnCount() > 0) {
            counsellorTable.getColumnModel().getColumn(0).setMinWidth(70);
            counsellorTable.getColumnModel().getColumn(0).setMaxWidth(150);
            counsellorTable.getColumnModel().getColumn(1).setMinWidth(200);
            counsellorTable.getColumnModel().getColumn(1).setMaxWidth(500);
            counsellorTable.getColumnModel().getColumn(2).setMinWidth(200);
            counsellorTable.getColumnModel().getColumn(2).setMaxWidth(500);
            counsellorTable.getColumnModel().getColumn(3).setMinWidth(200);
            counsellorTable.getColumnModel().getColumn(3).setMaxWidth(500);
            counsellorTable.getColumnModel().getColumn(4).setMinWidth(200);
            counsellorTable.getColumnModel().getColumn(4).setMaxWidth(500);
            counsellorTable.getColumnModel().getColumn(5).setMinWidth(200);
            counsellorTable.getColumnModel().getColumn(5).setMaxWidth(500);
        }

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

        printBtn.setText("PRINT");
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });

        exportBtn.setText("EXPORT");
        exportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelBorder2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2)
                                .addGap(6, 6, 6))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(exportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(350, 350, 350)
                                .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBorder2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(printBtn)
                            .addComponent(exportBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 761, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1398, 898));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void counsellorTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_counsellorTableMouseClicked
        int rowNo = counsellorTable.getSelectedRow();
        TableModel model = counsellorTable.getModel();

        counsellorName.setText(model.getValueAt(rowNo, 1).toString());
        counsellorContact.setText(model.getValueAt(rowNo, 3).toString());
        counsellorAdress.setText(model.getValueAt(rowNo, 4).toString());
        counselingReason.setText(model.getValueAt(rowNo, 5).toString());

    }//GEN-LAST:event_counsellorTableMouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked

    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void counsellorNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_counsellorNameFocusLost

    }//GEN-LAST:event_counsellorNameFocusLost

    private void counsellorNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_counsellorNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_counsellorNameActionPerformed

    private void counsellorContactFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_counsellorContactFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_counsellorContactFocusLost

    private void counsellorContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_counsellorContactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_counsellorContactActionPerformed

    private void counsellorAdressFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_counsellorAdressFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_counsellorAdressFocusLost

    private void counsellorAdressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_counsellorAdressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_counsellorAdressActionPerformed

    private void counselingReasonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_counselingReasonFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_counselingReasonFocusLost

    private void counselingReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_counselingReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_counselingReasonActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (updateCounsellors() == true) {
            JOptionPane.showMessageDialog(this, "Updated Successfully...");
            clearCounsellorTable();
            setCounselingData();
            clearCounselorsData();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Update...!!");
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (deleteCounselors() == true) {
            JOptionPane.showMessageDialog(this, "Deleted Successfully...");
            clearCounsellorTable();
            setCounselingData();
            clearCounselorsData();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Delete...!!");
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        if (addCounsellors() == true) {
            JOptionPane.showMessageDialog(this, "Added Successfully...");
            clearCounsellorTable();
            setCounselingData();
            clearCounselorsData();

        } else {
            JOptionPane.showMessageDialog(this, "Failed To Add...!!");
        }
    }//GEN-LAST:event_addBtnActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        MessageFormat header = new MessageFormat("Visitors Details");
        MessageFormat footer = new MessageFormat("Page: ({0,number,integer})");
        try {
            PrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
            set.add(OrientationRequested.PORTRAIT);
            counsellorTable.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, set, true);
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
                Sheet sheet = wb.createSheet("Visitors");

                Row rowCol = sheet.createRow(0);

                for (int i = 0; i < counsellorTable.getColumnCount(); i++) {
                    Cell cell = rowCol.createCell(i);
                    cell.setCellValue(counsellorTable.getColumnName(i));
                }

                for (int j = 0; j < counsellorTable.getRowCount(); j++) {
                    Row row = sheet.createRow(j);
                    for (int k = 0; k < counsellorTable.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (counsellorTable.getValueAt(j, k) != null) {
                            cell.setCellValue(counsellorTable.getValueAt(j, k).toString());
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
        model = (DefaultTableModel) counsellorTable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        counsellorTable.setRowSorter(trs);
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
            java.util.logging.Logger.getLogger(Counseling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Counseling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Counseling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Counseling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Counseling().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private necesario.RSMaterialButtonCircle addBtn;
    private app.bolivia.swing.JCTextField counselingReason;
    private app.bolivia.swing.JCTextField counsellorAdress;
    private app.bolivia.swing.JCTextField counsellorContact;
    private app.bolivia.swing.JCTextField counsellorName;
    public static rojeru_san.complementos.RSTableMetro counsellorTable;
    private necesario.RSMaterialButtonCircle deleteBtn;
    private javax.swing.JButton exportBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private com.ims.swing.PanelBorder panelBorder2;
    private com.ims.swing.PanelBorder panelHeader;
    private javax.swing.JButton printBtn;
    private app.bolivia.swing.JCTextField searchBtn;
    private javax.swing.JLabel signupTitle12;
    private javax.swing.JLabel signupTitle15;
    private javax.swing.JLabel signupTitle16;
    private javax.swing.JLabel signupTitle21;
    private necesario.RSMaterialButtonCircle updateBtn;
    // End of variables declaration//GEN-END:variables

    private void openfile(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
