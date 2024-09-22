package com.ims.add;

import com.ims.connection.DBConnection;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import jnafilechooser.api.JnaFileChooser;

public class AddStudents extends javax.swing.JFrame {

    JnaFileChooser ch;

    public AddStudents() {
        initComponents();
        getSectionData();
        gettimeData();
    }

    public void getSectionData() {
        String chooseSection;
        Set<String> uniqueSections = new HashSet<>(); // Using a Set to store unique sections
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from courses");

            while (rs.next()) {
                chooseSection = rs.getString("choose_section");
                uniqueSections.add(chooseSection);
            }

            // Clear existing items in the JComboBox
            txt_sectionName.removeAllItems();

            // Add unique items to the JComboBox
            for (String section : uniqueSections) {
                txt_sectionName.addItem(section);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void populateCourseData(String selectedSection) {

        txt_courseName.removeAllItems();

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT course_name FROM courses WHERE choose_section = ?");
            pst.setString(1, selectedSection);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String courseName = rs.getString("course_name");
                txt_courseName.addItem(courseName);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getCourseFee() {

        try {
            String selectedCourse = (String) txt_courseName.getSelectedItem();

            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("select course_fee from courses where course_name = ?");
            pst.setString(1, selectedCourse);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txt_courseFee.setText(rs.getString("course_fee"));
            } else {
                txt_courseFee.setText("Fee not Found");
            }

        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch course fee");

        }

    }

    public void gettimeData() {

        try {

            Connection conn = DBConnection.getConnection();
            String sql = "select student_time from addstudenttime";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String timeData = rs.getString("student_time");
                txt_time.addItem(timeData);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public boolean insertData(File selectedFile) {

        boolean isInserted = false;

        String studentName, sectionName, courseName, dob, studentContact, email, address, parentName, parentContact, remarks, time;
        int courseFee, discount, dueAmount, totalAmount;
        byte[] studentImage;
        Date date;

        try {

            Connection conn = DBConnection.getConnection();
            sectionName = txt_sectionName.getSelectedItem().toString();

            date = new Date();
            java.sql.Date entryDate = new java.sql.Date(date.getTime());
            studentName = txt_studentName.getText();
            courseName = txt_courseName.getSelectedItem().toString();
            courseFee = Integer.parseInt(txt_courseFee.getText());
            discount = Integer.parseInt(txt_discountAmount.getText());
            dob = txt_dob.getText();
            studentContact = txt_contact.getText();
            email = txt_email.getText();
            address = txt_address.getText();
            parentContact = txt_parentContact.getText();
            parentName = txt_parentName.getText();
            totalAmount = courseFee - discount;
            dueAmount = totalAmount;
            remarks = txt_remarks.getText();
            time = txt_time.getSelectedItem().toString();

            studentImage = new byte[(int) selectedFile.length()];
            FileInputStream fis = new FileInputStream(selectedFile);
            fis.read(studentImage);
            fis.close();

            if (sectionName.equals("Computer")) {

                String sql = "insert into computerstudents (date, name, course, fee, discount, dob, contact, email, parents_name, parents_contact,"
                        + "address, due_amount, total_amount, remarks, student_img, time) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setDate(1, entryDate);
                pst.setString(2, studentName);
                pst.setString(3, courseName);
                pst.setInt(4, courseFee);
                pst.setInt(5, discount);
                pst.setString(6, studentContact);
                pst.setString(7, email);
                pst.setString(8, dob);
                pst.setString(9, parentName);
                pst.setString(10, parentContact);
                pst.setString(11, address);
                pst.setInt(12, dueAmount);
                pst.setInt(13, totalAmount);
                pst.setString(14, remarks);
                pst.setBytes(15, studentImage);
                pst.setString(16, time);

                int rowCount = pst.executeUpdate();

                if (rowCount > 0) {
                    isInserted = true;
                } else {
                    isInserted = false;
                }

            } else if (sectionName.equals("Tuition")) {
                String sql = "insert into tuitionstudents (date, name, course, fee, discount, dob, contact, email, parents_name, parents_contact,"
                        + "address, due_amount, total_amount, remarks, student_img, time) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setDate(1, entryDate);
                pst.setString(2, studentName);
                pst.setString(3, courseName);
                pst.setInt(4, courseFee);
                pst.setInt(5, discount);
                pst.setString(6, studentContact);
                pst.setString(7, email);
                pst.setString(8, dob);
                pst.setString(9, parentName);
                pst.setString(10, parentContact);
                pst.setString(11, address);
                pst.setInt(12, dueAmount);
                pst.setInt(13, totalAmount);
                pst.setString(14, remarks);
                pst.setBytes(15, studentImage);
                pst.setString(16, time);

                int rowCount = pst.executeUpdate();

                if (rowCount > 0) {
                    isInserted = true;
                } else {
                    isInserted = false;
                }
            } else if (sectionName.equals("Language")) {
                String sql = "insert into languagestudents (date, name, course, fee, discount, dob, contact, email, parents_name, parents_contact,"
                        + "address, due_amount, total_amount, remarks, student_img, time) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setDate(1, entryDate);
                pst.setString(2, studentName);
                pst.setString(3, courseName);
                pst.setInt(4, courseFee);
                pst.setInt(5, discount);
                pst.setString(6, studentContact);
                pst.setString(7, email);
                pst.setString(8, dob);
                pst.setString(9, parentName);
                pst.setString(10, parentContact);
                pst.setString(11, address);
                pst.setInt(12, dueAmount);
                pst.setInt(13, totalAmount);
                pst.setString(14, remarks);
                pst.setBytes(15, studentImage);
                pst.setString(16, time);

                int rowCount = pst.executeUpdate();

                if (rowCount > 0) {
                    isInserted = true;
                } else {
                    isInserted = false;
                }
            } else if (sectionName.equals("Test Preparation")) {
                String sql = "insert into testprepstudents (date, name, course, fee, discount, dob, contact, email, parents_name, parents_contact,"
                        + "address, due_amount, total_amount, remarks, student_img, time) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setDate(1, entryDate);
                pst.setString(2, studentName);
                pst.setString(3, courseName);
                pst.setInt(4, courseFee);
                pst.setInt(5, discount);
                pst.setString(6, studentContact);
                pst.setString(7, email);
                pst.setString(8, dob);
                pst.setString(9, parentName);
                pst.setString(10, parentContact);
                pst.setString(11, address);
                pst.setInt(12, dueAmount);
                pst.setInt(13, totalAmount);
                pst.setString(14, remarks);
                pst.setBytes(15, studentImage);
                pst.setString(16, time);

                int rowCount = pst.executeUpdate();

                if (rowCount > 0) {
                    isInserted = true;
                } else {
                    isInserted = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isInserted;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new com.ims.swing.PanelBorder();
        jLabel1 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txt_studentName = new app.bolivia.swing.JCTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_contact = new app.bolivia.swing.JCTextField();
        jLabel20 = new javax.swing.JLabel();
        txt_email = new app.bolivia.swing.JCTextField();
        txt_courseName = new rojerusan.RSComboMetro();
        jLabel11 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_courseFee = new app.bolivia.swing.JCTextField();
        txt_address = new app.bolivia.swing.JCTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txt_parentName = new app.bolivia.swing.JCTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_discountAmount = new app.bolivia.swing.JCTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_dob = new app.bolivia.swing.JCTextField();
        jLabel23 = new javax.swing.JLabel();
        txt_parentContact = new app.bolivia.swing.JCTextField();
        jLabel22 = new javax.swing.JLabel();
        txt_remarks = new app.bolivia.swing.JCTextField();
        txt_selectimgbtn = new javax.swing.JButton();
        panelBorder2 = new com.ims.swing.PanelBorder();
        txt_studentImg = new javax.swing.JLabel();
        txt_sectionName = new rojerusan.RSComboMetro();
        jLabel12 = new javax.swing.JLabel();
        AddStudentBtn = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txt_time = new javax.swing.JComboBox<>();
        addStudentTime_btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelBorder1.setPreferredSize(new java.awt.Dimension(100, 915));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/close.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 0, 51));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("ADD STUDENT");

        txt_studentName.setBackground(new java.awt.Color(255, 255, 255));
        txt_studentName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_studentName.setForeground(new java.awt.Color(0, 0, 51));
        txt_studentName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 0, 51));
        jLabel8.setText("Enter Student Name");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 0, 51));
        jLabel15.setText("Enter Contact");

        txt_contact.setBackground(new java.awt.Color(255, 255, 255));
        txt_contact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_contact.setForeground(new java.awt.Color(0, 0, 51));
        txt_contact.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_contact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_contactKeyPressed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 0, 51));
        jLabel20.setText("Enter Email");

        txt_email.setBackground(new java.awt.Color(255, 255, 255));
        txt_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_email.setForeground(new java.awt.Color(0, 0, 51));
        txt_email.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_courseName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_courseName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_courseNameActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 0, 51));
        jLabel11.setText("Select Course");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 0, 51));
        jLabel17.setText("Course Fee");

        txt_courseFee.setBackground(new java.awt.Color(255, 255, 255));
        txt_courseFee.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_courseFee.setForeground(new java.awt.Color(0, 0, 51));
        txt_courseFee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_courseFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_courseFeeActionPerformed(evt);
            }
        });
        txt_courseFee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_courseFeeKeyPressed(evt);
            }
        });

        txt_address.setBackground(new java.awt.Color(255, 255, 255));
        txt_address.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_address.setForeground(new java.awt.Color(0, 0, 51));
        txt_address.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 0, 51));
        jLabel21.setText("Enter Address");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 0, 51));
        jLabel24.setText("Parent's Name");

        txt_parentName.setBackground(new java.awt.Color(255, 255, 255));
        txt_parentName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_parentName.setForeground(new java.awt.Color(0, 0, 51));
        txt_parentName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_parentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_parentNameActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 0, 51));
        jLabel16.setText("Enter Discount");

        txt_discountAmount.setBackground(new java.awt.Color(255, 255, 255));
        txt_discountAmount.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_discountAmount.setForeground(new java.awt.Color(0, 0, 51));
        txt_discountAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_discountAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_discountAmountActionPerformed(evt);
            }
        });
        txt_discountAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_discountAmountKeyPressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 51));
        jLabel14.setText("Enter DOB");

        txt_dob.setBackground(new java.awt.Color(255, 255, 255));
        txt_dob.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_dob.setForeground(new java.awt.Color(0, 0, 51));
        txt_dob.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 0, 51));
        jLabel23.setText("Parent's Contact");

        txt_parentContact.setBackground(new java.awt.Color(255, 255, 255));
        txt_parentContact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_parentContact.setForeground(new java.awt.Color(0, 0, 51));
        txt_parentContact.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_parentContact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_parentContactKeyPressed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 0, 51));
        jLabel22.setText("Enter Remarks");

        txt_remarks.setBackground(new java.awt.Color(255, 255, 255));
        txt_remarks.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_remarks.setForeground(new java.awt.Color(0, 0, 51));
        txt_remarks.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_selectimgbtn.setBackground(new java.awt.Color(51, 51, 51));
        txt_selectimgbtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_selectimgbtn.setForeground(new java.awt.Color(255, 255, 255));
        txt_selectimgbtn.setText("Select Image");
        txt_selectimgbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_selectimgbtnActionPerformed(evt);
            }
        });

        panelBorder2.setBackground(new java.awt.Color(204, 204, 204));

        txt_studentImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelBorder2Layout = new javax.swing.GroupLayout(panelBorder2);
        panelBorder2.setLayout(panelBorder2Layout);
        panelBorder2Layout.setHorizontalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_studentImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBorder2Layout.setVerticalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_studentImg, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addContainerGap())
        );

        txt_sectionName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_sectionName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_sectionNameActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 0, 51));
        jLabel12.setText("Select Section ");

        AddStudentBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        AddStudentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/add-student.png"))); // NOI18N
        AddStudentBtn.setText("ADD NEW STUDENTS");
        AddStudentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddStudentBtnActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 0, 51));
        jLabel18.setText("Select Time");

        txt_time.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        addStudentTime_btn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        addStudentTime_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/add1.png"))); // NOI18N
        addStudentTime_btn.setText("ADD STUDENT TIME");
        addStudentTime_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentTime_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_selectimgbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(AddStudentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(addStudentTime_btn))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_time, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_courseName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_studentName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_dob, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_discountAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_courseFee, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_sectionName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(50, 50, 50)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_parentContact, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_parentName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_remarks, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(panelBorder2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_selectimgbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(txt_studentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(txt_sectionName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(txt_courseName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_parentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_courseFee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(30, 30, 30)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_parentContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_remarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_discountAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_dob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_time, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddStudentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStudentTime_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1158, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1158, 927));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void txt_contactKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_contactKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            txt_contact.setEditable(false);
        } else {
            txt_contact.setEditable(true);
        }
    }//GEN-LAST:event_txt_contactKeyPressed

    private void txt_courseNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_courseNameActionPerformed
        getCourseFee();
    }//GEN-LAST:event_txt_courseNameActionPerformed

    private void txt_courseFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_courseFeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_courseFeeActionPerformed

    private void txt_courseFeeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_courseFeeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_courseFeeKeyPressed

    private void txt_parentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_parentNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_parentNameActionPerformed

    private void txt_discountAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_discountAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_discountAmountActionPerformed

    private void txt_discountAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_discountAmountKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            txt_discountAmount.setEditable(false);
        } else {
            txt_discountAmount.setEditable(true);
        }
    }//GEN-LAST:event_txt_discountAmountKeyPressed

    private void txt_parentContactKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_parentContactKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            txt_parentContact.setEditable(false);
        } else {
            txt_parentContact.setEditable(true);
        }
    }//GEN-LAST:event_txt_parentContactKeyPressed

    private void txt_selectimgbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_selectimgbtnActionPerformed
        ch = new JnaFileChooser();
        javax.swing.JFrame parentFrame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        boolean action = ch.showOpenDialog(parentFrame);
        if (action) {
            // Get the selected file path
            String selectedImagePath = ch.getSelectedFile().getAbsolutePath();

            // Create an ImageIcon from the selected image path
            ImageIcon originalIcon = new ImageIcon(selectedImagePath);

            // Get the original image from the ImageIcon
            Image originalImage = originalIcon.getImage();

            // Calculate the new width and height for the scaled image
            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);
            int newWidth, newHeight;

            if (originalWidth > originalHeight) {
                // Landscape image, so set width to 200 and scale height proportionally
                newWidth = 200;
                newHeight = (int) (((double) newWidth / originalWidth) * originalHeight);
            } else {
                // Portrait or square image, so set height to 200 and scale width proportionally
                newHeight = 200;
                newWidth = (int) (((double) newHeight / originalHeight) * originalWidth);
            }

            // Scale the original image to the new size
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            // Create a new ImageIcon from the scaled image
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Set the ImageIcon to the JLabel
            txt_studentImg.setIcon(scaledIcon);
        }
    }//GEN-LAST:event_txt_selectimgbtnActionPerformed

    private void txt_sectionNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_sectionNameActionPerformed
        String selectedCourse = (String) txt_sectionName.getSelectedItem();
        populateCourseData(selectedCourse);
    }//GEN-LAST:event_txt_sectionNameActionPerformed

    private void AddStudentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddStudentBtnActionPerformed
        // Call the insertData method and pass the selected file
        boolean isInserted = insertData(ch.getSelectedFile());

        if (isInserted) {
            // Data inserted successfully
            JOptionPane.showMessageDialog(null, "Student added successfully!");
        } else {
            // Data insertion failed
            JOptionPane.showMessageDialog(null, "Failed to add student.");
        }
    }//GEN-LAST:event_AddStudentBtnActionPerformed

    private void addStudentTime_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentTime_btnActionPerformed
        AddTimeTable addTimeTable = new AddTimeTable();
        addTimeTable.setVisible(true);
    }//GEN-LAST:event_addStudentTime_btnActionPerformed

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
            java.util.logging.Logger.getLogger(AddStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddStudents().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddStudentBtn;
    private javax.swing.JButton addStudentTime_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel8;
    private com.ims.swing.PanelBorder panelBorder1;
    private com.ims.swing.PanelBorder panelBorder2;
    private app.bolivia.swing.JCTextField txt_address;
    private app.bolivia.swing.JCTextField txt_contact;
    private app.bolivia.swing.JCTextField txt_courseFee;
    private rojerusan.RSComboMetro txt_courseName;
    private app.bolivia.swing.JCTextField txt_discountAmount;
    private app.bolivia.swing.JCTextField txt_dob;
    private app.bolivia.swing.JCTextField txt_email;
    private app.bolivia.swing.JCTextField txt_parentContact;
    private app.bolivia.swing.JCTextField txt_parentName;
    private app.bolivia.swing.JCTextField txt_remarks;
    private rojerusan.RSComboMetro txt_sectionName;
    private javax.swing.JButton txt_selectimgbtn;
    private javax.swing.JLabel txt_studentImg;
    private app.bolivia.swing.JCTextField txt_studentName;
    private javax.swing.JComboBox<String> txt_time;
    // End of variables declaration//GEN-END:variables
}
