package com.ims.payment;

import com.ims.connection.DBConnection;
import static com.ims.form.Computer.computerTable;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author _Spoidey-
 */
public class computerPayment extends javax.swing.JFrame { 

    String chooseSection, chooseCourse, chooseStudent;

    byte[] studentImage;
    
    int mouseX, mouseY;
    

    public computerPayment() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        populateCourseData();
        AutoCompleteDecorator.decorate(txt_chooseStudent);
        
        
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

    public void getStudentsData() {

        DefaultTableModel model;

        int id, courseFee, discount, firstPaymentAmount, secondPaymentAmount, thirdPaymentAmount, fourthPaymentAmount, dueAmount, paidAmount, totalAmount;
        String name, courseName = null, dob, studentContact, email, parentName, parentContact, address, remarks;
        Date admissionDate, firstPaymentDate, secondPaymentDate, thirdPaymentDate, fourthPaymentDate;

        try {

            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select id, date, name, course, fee, discount, firstpayment_amount_date, firstpayment_amount, secondpayment_amount_date, secondpayment_amount, "
                    + "thirdpayment_amount_date, thirdpayment_amount, fourthpayment_amount_date, fourthpayment_amount, dob, contact, email, parents_name, parents_contact,"
                    + " address, due_amount, total_paid_amount, total_amount, remarks from computerstudents");

            String course = txt_chooseCourse.getSelectedItem().toString();

            while (rs.next()) {

                id = rs.getInt("id");
                admissionDate = rs.getDate("date");
                name = rs.getString("name");
                courseName = rs.getString("course");
                courseFee = rs.getInt("fee");
                discount = rs.getInt("discount");
                firstPaymentDate = rs.getDate("firstpayment_amount_date");
                firstPaymentAmount = rs.getInt("firstpayment_amount");
                secondPaymentDate = rs.getDate("secondpayment_amount_date");
                secondPaymentAmount = rs.getInt("secondpayment_amount");
                thirdPaymentDate = rs.getDate("thirdpayment_amount_date");
                thirdPaymentAmount = rs.getInt("thirdpayment_amount");
                fourthPaymentDate = rs.getDate("fourthpayment_amount_date");
                fourthPaymentAmount = rs.getInt("fourthpayment_amount");
                dob = rs.getString("dob");
                studentContact = rs.getString("contact");
                email = rs.getString("email");
                parentName = rs.getString("parents_name");
                parentContact = rs.getString("parents_contact");
                address = rs.getString("address");
                dueAmount = rs.getInt("due_amount");
                paidAmount = rs.getInt("total_paid_amount");
                totalAmount = rs.getInt("total_amount");
                remarks = rs.getString("remarks");

                Object[] obj = {id, admissionDate, name, courseName, courseFee, discount, firstPaymentDate, firstPaymentAmount, secondPaymentDate,
                    secondPaymentAmount, thirdPaymentDate, thirdPaymentAmount, fourthPaymentDate, fourthPaymentAmount, dob, studentContact, email, parentName,
                    parentContact, address, dueAmount, paidAmount, totalAmount, remarks
                };

                model = (DefaultTableModel) computerTable.getModel();
                model.addRow(obj);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void populateCourseData() {

        txt_chooseStudent.removeAllItems();
        txt_chooseCourse.removeAllItems();

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT course_name FROM courses WHERE choose_section = 'Computer'");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String courseName = rs.getString("course_name");
                txt_chooseCourse.addItem(courseName);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void populateStudentData(String selectedCourse) {

        txt_chooseStudent.removeAllItems();

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT name FROM computerstudents WHERE course = ?");

            pst.setString(1, selectedCourse);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String studentName = rs.getString("name");
                txt_chooseStudent.addItem(studentName);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAmountData(String selectedStudent) {

        int paidAmount = 0, dueAmount = 0, totalAmount = 0;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT total_paid_amount, due_amount, total_amount FROM computerstudents WHERE name = ? ");

            pst.setString(1, selectedStudent);

            ResultSet rs = pst.executeQuery();

            // Clear previous data from text fields
            txt_paidAmount.setText("");
            txt_dueAmount.setText("");
            txt_amount.setText("");

            while (rs.next()) {
                // Retrieve data for each student from each table
                paidAmount = rs.getInt("total_paid_amount");
                dueAmount = rs.getInt("due_amount");
                totalAmount = rs.getInt("total_amount");

                // Display the data in your text fields
                // You might need to append data if you expect multiple rows for a student
                txt_paidAmount.setText(txt_paidAmount.getText() + String.valueOf(paidAmount) + "\n");
                txt_dueAmount.setText(txt_dueAmount.getText() + String.valueOf(dueAmount) + "\n");
                txt_amount.setText(txt_amount.getText() + String.valueOf(totalAmount) + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean insertPayment() {
        boolean isInsertData = false;
        int paidAmount;
        String selectedStudent = (String) txt_chooseStudent.getSelectedItem();

        // Validate if a student is selected
        if (selectedStudent == null || selectedStudent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student.");
            return isInsertData; // Return false, as no student is selected
        }

        try {
            paidAmount = Integer.parseInt(txt_paymentAmount.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid payment amount.");
            return isInsertData; // Return false, as payment amount is not valid
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Check if connection is successful
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
                return isInsertData; // Return false, as connection failed
            }

            // Prepare the SQL statement to retrieve payment information
            String sql = "SELECT firstpayment_amount, secondpayment_amount, thirdpayment_amount, fourthpayment_amount, due_amount, total_paid_amount FROM computerstudents WHERE name = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, selectedStudent);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        int firstpayment = rs.getInt("firstpayment_amount");
                        int secondpayment = rs.getInt("secondpayment_amount");
                        int thirdpayment = rs.getInt("thirdpayment_amount");
                        int fourthpayment = rs.getInt("fourthpayment_amount");
                        int totalPaidAmount = rs.getInt("total_paid_amount");
                        int dueAmount = rs.getInt("due_amount");

                        if (firstpayment == 0) {
                            // Insert first payment
                            isInsertData = insertPayment(conn, selectedStudent, paidAmount, "firstpayment_amount", totalPaidAmount, dueAmount);
                        } else if (secondpayment == 0 && firstpayment > 0) {
                            // Insert second payment
                            isInsertData = insertPayment(conn, selectedStudent, paidAmount, "secondpayment_amount", totalPaidAmount, dueAmount);
                        } else if (thirdpayment == 0 && secondpayment > 0 && firstpayment > 0) {
                            // Insert third payment
                            isInsertData = insertPayment(conn, selectedStudent, paidAmount, "thirdpayment_amount", totalPaidAmount, dueAmount);
                        } else if (fourthpayment == 0 && thirdpayment > 0 && secondpayment > 0 && firstpayment > 0) {
                            // Insert fourth payment
                            isInsertData = insertPayment(conn, selectedStudent, paidAmount, "fourthpayment_amount", totalPaidAmount, dueAmount);
                        } else if (rs.getInt("due_amount") == 0) {
                            JOptionPane.showMessageDialog(this, "All payments have been cleared.");
                        } else {
                            JOptionPane.showMessageDialog(this, "All payments have been cleared.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Student not found in the database.");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }

        return isInsertData;
    }

    // Helper method to insert payment into the database
    private boolean insertPayment(Connection conn, String selectedStudent, int paidAmount, String paymentColumn, int totalPaidAmount, int dueAmount) throws SQLException {
        boolean isInsertData = false;

        // Prepare the update query to update payment and calculate total paid amount and due amount
        String updateQuery = "UPDATE computerstudents SET " + paymentColumn + " = ?, " + paymentColumn + "_date = ?, total_paid_amount = ?, due_amount = ? WHERE name = ?";

        try (PreparedStatement st = conn.prepareStatement(updateQuery)) {
            // Update payment and other columns
            st.setInt(1, paidAmount);
            st.setDate(2, new java.sql.Date(System.currentTimeMillis())); // Assuming current date
            st.setInt(3, totalPaidAmount + paidAmount); // Updated total paid amount
            st.setInt(4, dueAmount - paidAmount); // Updated due amount
            st.setString(5, selectedStudent);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                isInsertData = true;
            } else {
                isInsertData = false;
            }
        }

        return isInsertData;
    }

//    public boolean insertNewPayment() {
//        
//        boolean isInserted = false;
//        
//        String studentName = txt_chooseStudent.getSelectedItem().toString();
//        int paymentAmount = Integer.parseInt(txt_paymentAmount.getText());
//        Date date = new Date();
//        java.sql.Date todayDate = new java.sql.Date(date.getTime());
//        
//        try {
//            
//            Connection conn = DBConnection.getConnection();
//            String sql = "insert into payments (student_name, payment_amount, payment_date) values (?, ?, ?)";
//            PreparedStatement pst = conn.prepareStatement(sql);
//            
//            pst.setString(1, studentName);
//            pst.setInt(2, paymentAmount);
//            pst.setDate(3, todayDate);
//            
//            
//            int rowCount = 0;
//            
//            if(rowCount > 0) {
//                isInserted = true;
//            } else {
//                isInserted = false;
//            }
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        return isInserted;
//    }

    

    public void clearPayment() {

        txt_chooseCourse.setSelectedItem(-1);
        txt_chooseStudent.setSelectedIndex(-1);
        txt_paymentAmount.setText("0");
        txt_paidAmount.setText("0");
        txt_dueAmount.setText("0");
        txt_amount.setText("0");
        txt_studentImg.setIcon(null);

    }

    public void clearTable() {

        DefaultTableModel model = (DefaultTableModel) computerTable.getModel();
        model.setRowCount(0);

    }

    public void getInvoiceData(String selectedStudent) {

        String name = "name", address = "address", course = "course", wordsAmount = "amount in words";
        int paidAmount = 0, dueAmount = 0, totalAmount = 0, discountAmount = 0, fee = 0, reciptNo = 0;
        String paidAmt = null, dueAmt = null, totalAmt = null, disAmt = null, courseFee = null, reciptNum = null;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT name, address, course, fee, discount, due_amount, total_amount FROM computerstudents WHERE name = ? "
            );

            pst.setString(1, selectedStudent);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                name = rs.getString("name");
                address = rs.getString("address");
                course = rs.getString("course");
                fee = rs.getInt("fee");
                discountAmount = rs.getInt("discount");
                totalAmount = rs.getInt("total_amount");
                paidAmount = Integer.parseInt(txt_paymentAmount.getText());
                dueAmount = rs.getInt("due_amount");

                courseFee = String.valueOf(fee);
                disAmt = String.valueOf(discountAmount);
                totalAmt = String.valueOf(totalAmount);
                paidAmt = String.valueOf(paidAmount);
                dueAmt = String.valueOf(dueAmount);

                reciptNo += 1;
                reciptNum = String.valueOf(reciptNo);

            }


        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Payment data displayed failed..!!");
        }

    }

    
    
    
    
    public void getInvoiceData() {
        
        
        
        
        
    }
    
    
    
    
    private String convertToWords(String amount) {
        // Your implementation of converting the amount to words goes here
        // This is just a placeholder implementation
        return "Placeholder implementation: " + amount;
    }



    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new com.ims.swing.PanelBorder();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_paymentAmount = new app.bolivia.swing.JCTextField();
        payBtn = new javax.swing.JButton();
        txt_chooseCourse = new rojerusan.RSComboMetro();
        jLabel8 = new javax.swing.JLabel();
        txt_chooseStudent = new rojerusan.RSComboMetro();
        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txt_studentImg = new javax.swing.JLabel();
        txt_paidAmount = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_dueAmount = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txt_amount = new javax.swing.JButton();
        panelHeader = new com.ims.swing.PanelBorder();
        closeBtn = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        setFocusTraversalPolicyProvider(true);
        setUndecorated(true);

        panelBorder1.setBackground(new java.awt.Color(204, 204, 204));
        panelBorder1.setOpaque(true);
        panelBorder1.setPreferredSize(new java.awt.Dimension(710, 400));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 0, 204));
        jLabel3.setText("Payment Amount");

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Computer  Payment");

        txt_paymentAmount.setBackground(new java.awt.Color(255, 255, 255));
        txt_paymentAmount.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 2, 1, new java.awt.Color(102, 0, 102)));
        txt_paymentAmount.setForeground(new java.awt.Color(0, 0, 0));
        txt_paymentAmount.setCaretColor(new java.awt.Color(0, 0, 0));
        txt_paymentAmount.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txt_paymentAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_paymentAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_paymentAmountActionPerformed(evt);
            }
        });
        txt_paymentAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_paymentAmountKeyPressed(evt);
            }
        });

        payBtn.setBackground(new java.awt.Color(0, 102, 0));
        payBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        payBtn.setForeground(new java.awt.Color(255, 255, 255));
        payBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/pay.png"))); // NOI18N
        payBtn.setText("PAYMENT");
        payBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payBtnActionPerformed(evt);
            }
        });

        txt_chooseCourse.setBackground(new java.awt.Color(0, 0, 204));
        txt_chooseCourse.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        txt_chooseCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_chooseCourseActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 0, 204));
        jLabel8.setText("Select Course");

        txt_chooseStudent.setBackground(new java.awt.Color(0, 0, 204));
        txt_chooseStudent.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        txt_chooseStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_chooseStudentActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 0, 204));
        jLabel9.setText("Select Student");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));

        txt_studentImg.setForeground(new java.awt.Color(204, 204, 204));
        txt_studentImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_studentImg, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_studentImg, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txt_paidAmount.setBackground(new java.awt.Color(0, 153, 102));
        txt_paidAmount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_paidAmount.setForeground(new java.awt.Color(255, 255, 255));
        txt_paidAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        txt_paidAmount.setText("0");
        txt_paidAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_paidAmountActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 0, 204));
        jLabel5.setText("Paid Amount");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 0, 204));
        jLabel6.setText("Due Amount");

        txt_dueAmount.setBackground(new java.awt.Color(204, 0, 0));
        txt_dueAmount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_dueAmount.setForeground(new java.awt.Color(255, 255, 255));
        txt_dueAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        txt_dueAmount.setText("0");
        txt_dueAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dueAmountActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 0, 204));
        jLabel7.setText("Amount");

        txt_amount.setBackground(new java.awt.Color(255, 102, 153));
        txt_amount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_amount.setForeground(new java.awt.Color(255, 255, 255));
        txt_amount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        txt_amount.setText("0");
        txt_amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_amountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jLabel4))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txt_amount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_paidAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                                .addGap(77, 77, 77)
                                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_dueAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)))
                            .addComponent(txt_paymentAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_chooseCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_chooseStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(payBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(2, 2, 2)
                                .addComponent(txt_paidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(2, 2, 2)
                                .addComponent(txt_dueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(2, 2, 2)
                        .addComponent(txt_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(txt_chooseCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_chooseStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txt_paymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(payBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelHeader.setBackground(new java.awt.Color(102, 102, 255));

        closeBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        closeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/close.png"))); // NOI18N
        closeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeBtnMouseClicked(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/pay.png"))); // NOI18N

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 565, Short.MAX_VALUE)
                .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(closeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(672, 775));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void payBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payBtnActionPerformed
        if (insertPayment() == true) {
            JOptionPane.showMessageDialog(this, "Payment Successful..!! 😊😊 THANK YOU..!!");
            clearTable();
            getStudentsData();
            String selectedStudentName = (String) txt_chooseStudent.getSelectedItem();
            getInvoiceData(selectedStudentName);
            clearPayment();
        } else {
            JOptionPane.showMessageDialog(this, "Payment Failed..!!! 😒😒 Please Try Again..!");
        }
    }//GEN-LAST:event_payBtnActionPerformed

    private void txt_chooseCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_chooseCourseActionPerformed
        String selectedStudent = (String) txt_chooseCourse.getSelectedItem();
        populateStudentData(selectedStudent);
    }//GEN-LAST:event_txt_chooseCourseActionPerformed

    private void txt_chooseStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_chooseStudentActionPerformed

        String selectedStudent = (String) txt_chooseStudent.getSelectedItem();
        getAmountData(selectedStudent);

        try {

            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT student_img FROM computerstudents WHERE name = ? "
            );

            pst.setString(1, selectedStudent);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                InputStream input = rs.getBinaryStream("student_img");
                if (input != null) {
                    BufferedImage image = ImageIO.read(input);
                    if (image != null) {
                        txt_studentImg.setSize(250, 230);
                        Image scaledImage = image.getScaledInstance(txt_studentImg.getWidth(), txt_studentImg.getHeight(), Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        txt_studentImg.setIcon(imageIcon);
                    } else {
                        // Handle case where the image cannot be read
                        System.err.println("Failed to read the image.");
                    }
                } else {
                    // Handle case where no image is found for the selected student
                    // For example, set a default image or clear the icon
                    txt_studentImg.setIcon(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_txt_chooseStudentActionPerformed


    private void txt_paymentAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_paymentAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_paymentAmountActionPerformed

    private void txt_paidAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_paidAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_paidAmountActionPerformed

    private void txt_dueAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dueAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dueAmountActionPerformed

    private void txt_amountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_amountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_amountActionPerformed

    private void txt_paymentAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_paymentAmountKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            txt_paymentAmount.setEditable(false);
        } else {
            txt_paymentAmount.setEditable(true);
        }
    }//GEN-LAST:event_txt_paymentAmountKeyPressed

    private void closeBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeBtnMouseClicked
        this.dispose();
    }//GEN-LAST:event_closeBtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(computerPayment.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(computerPayment.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(computerPayment.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(computerPayment.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new computerPayment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel closeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private com.ims.swing.PanelBorder panelBorder1;
    private com.ims.swing.PanelBorder panelHeader;
    private javax.swing.JButton payBtn;
    private javax.swing.JButton txt_amount;
    private static rojerusan.RSComboMetro txt_chooseCourse;
    private static rojerusan.RSComboMetro txt_chooseStudent;
    private javax.swing.JButton txt_dueAmount;
    private javax.swing.JButton txt_paidAmount;
    private static app.bolivia.swing.JCTextField txt_paymentAmount;
    private javax.swing.JLabel txt_studentImg;
    // End of variables declaration//GEN-END:variables
}
