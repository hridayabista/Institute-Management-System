package com.ims.form;

import com.ims.connection.DBConnection;
import com.ims.payment.computerPayment;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import jnafilechooser.api.JnaFileChooser;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;

import javax.swing.ImageIcon;
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
public class TestPrep extends javax.swing.JPanel {

    /**
     *
     */
    public DefaultTableModel model;

    JnaFileChooser ch;

//    computerPayment computerPayment = new computerPayment();
    /**
     *
     */
    public TestPrep() {
        initComponents();
        getStudentsData();
        getCourseData();
        getCourses();
        txt_courseFee.setEditable(false);
        getDueStudents();
        gettimeData();

//        fromDate.setDateFormatString("yyyy-mm-dd");
//        toDate.setDateFormatString("yyyy-mm-dd");
//        showData("", "");
    }

    /**
     *
     */
    public void getStudentsData() {

        int id, courseFee, discount, firstPaymentAmount, secondPaymentAmount, thirdPaymentAmount, fourthPaymentAmount, dueAmount, paidAmount, totalAmount;
        String name, courseName = null, dob, studentContact, email = null, parentName, parentContact, address, remarks, time;
        Date admissionDate, firstPaymentDate, secondPaymentDate, thirdPaymentDate, fourthPaymentDate;

        try {

            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select id, date, name, course, fee, discount, firstpayment_amount_date, firstpayment_amount, secondpayment_amount_date, secondpayment_amount, "
                    + "thirdpayment_amount_date, thirdpayment_amount, fourthpayment_amount_date, fourthpayment_amount, dob, contact, email, parents_name, parents_contact,"
                    + " address, due_amount, total_paid_amount, total_amount, time, remarks from testprepstudents");

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
                time = rs.getString("time");

                Object[] obj = {id, admissionDate, name, courseName, courseFee, discount, firstPaymentDate, firstPaymentAmount, secondPaymentDate,
                    secondPaymentAmount, thirdPaymentDate, thirdPaymentAmount, fourthPaymentDate, fourthPaymentAmount, dob, studentContact, email, parentName,
                    parentContact, address, dueAmount, paidAmount, totalAmount, remarks, time
                };

                model = (DefaultTableModel) testprepTable.getModel();
                model.addRow(obj);

                int totalPaidAmount = 0, totalDueAmount = 0, amount = 0;

                for (int i = 0; i < testprepTable.getRowCount(); i++) {
                    int amt = (Integer) testprepTable.getValueAt(i, 22);
                    amount += amt;

                    int totalamt = (Integer) testprepTable.getValueAt(i, 21);
                    totalPaidAmount += totalamt;

                    int totalDueAmt = (Integer) testprepTable.getValueAt(i, 20);
                    totalDueAmount += totalDueAmt;
                }

                txt_totalAmount.setText(String.valueOf(amount));
                txt_totalPaidAmount.setText(String.valueOf(totalPaidAmount));
                txt_totalDueAmount.setText(String.valueOf(totalDueAmount));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void getDueStudents() {
        int dueStudents = 0;
        int totalStudents = 0;

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) AS due_students FROM testprepstudents WHERE due_amount > 0";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                dueStudents = rs.getInt("due_students");
                txt_totalDueStudents.setText(String.valueOf(dueStudents));
            }

            String sql2 = "SELECT COUNT(*) AS total_students FROM testprepstudents WHERE id > 0";
            PreparedStatement pst2 = conn.prepareStatement(sql2);
            ResultSet rs2 = pst2.executeQuery();
            if (rs2.next()) {
                totalStudents = rs2.getInt("total_students");
                txt_students.setText(String.valueOf(totalStudents));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void getCourseData() {

        String courseName;

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from courses where choose_section = 'Test Preparation'");

            while (rs.next()) {
                courseName = rs.getString("course_name");
                txt_courseName.addItem(courseName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void getCourses() {

        String courseName;

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from courses where choose_section = 'Test Preparation'");

            while (rs.next()) {
                courseName = rs.getString("course_name");
                txt_courseData.addItem(courseName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
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

    /**
     *
     */
    public void gettimeData() {

        try {

            Connection conn = DBConnection.getConnection();
            String sql = "select student_time from addstudenttime";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String timeData = rs.getString("student_time");
                txt_time.addItem(timeData);
                txt_times.addItem(timeData);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     *
     * @param selectedFile
     * @return
     */
    public boolean insertData(File selectedFile) {
        boolean isInserted = false;

        String studentName, sectionName, courseName, dob, studentContact, email = null, address, parentName, parentContact, remarks, time;
        int courseFee, discount = 0, dueAmount, totalAmount;
        byte[] studentImage = null;
        Date date;

        try (Connection conn = DBConnection.getConnection()) {

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

            if (selectedFile != null) {
                studentImage = new byte[(int) selectedFile.length()];
                FileInputStream fis = new FileInputStream(selectedFile);
                fis.read(studentImage);
                fis.close();
            } else {
                // If image is not selected, set studentImage to null or an empty byte array
                studentImage = null; // or studentImage = new byte[0];
            }

            String sql = "INSERT INTO testprepstudents (date, name, course, fee, discount, dob, contact, email, parents_name, parents_contact,"
                    + "address, due_amount, total_amount, remarks, student_img, time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setDate(1, entryDate);
                pst.setString(2, studentName);
                pst.setString(3, courseName);
                pst.setInt(4, courseFee);
                pst.setInt(5, discount);
                pst.setString(6, studentContact);
                pst.setString(7, email.isEmpty() ? null : email); // Check if email is empty
                pst.setString(8, dob);
                pst.setString(9, parentName);
                pst.setString(10, parentContact);
                pst.setString(11, address);
                pst.setInt(12, dueAmount);
                pst.setInt(13, totalAmount);
                pst.setString(14, remarks);
                if (selectedFile != null) {
                    pst.setBytes(15, studentImage);
                } else {
                    pst.setNull(15, Types.BLOB);
                }
                pst.setString(16, time);

                int rowCount = pst.executeUpdate();

                if (rowCount > 0) {
                    isInserted = true;
                }
            } catch (SQLException e) {
                // Log or handle the exception appropriately
                e.printStackTrace();

            }

        } catch (Exception e) {
            // Log or handle the exception appropriately
            e.printStackTrace();
            isInserted = false;
        }

        return isInserted;
    }

    /**
     *
     * @return
     */
    public int getSelectId() {
        int selectedId = -1;

        int selectedRow = testprepTable.getSelectedRow();

        if (selectedRow != -1) {
            selectedId = (int) testprepTable.getValueAt(selectedRow, 0);
        }
        return selectedId;
    }

    /**
     *
     * @param selectedFile
     * @return
     */
    public boolean updateStudentsData(File selectedFile) {
        boolean isupdated = false;

        int courseFee, discountAmount = 0, dueAmount, totalAmount;
        String name, section, courseName = null, dob, studentContact, email, parentName, parentContact, address, remarks, time;
        byte[] studentImage;

        try {

            name = txt_studentName.getText();
            courseName = txt_courseName.getSelectedItem().toString();
            courseFee = Integer.parseInt(txt_courseFee.getText());
            discountAmount = Integer.parseInt(txt_discountAmount.getText());
            dob = txt_dob.getText();
            studentContact = txt_contact.getText();
            email = txt_email.getText();
            parentName = txt_parentName.getText();
            parentContact = txt_parentContact.getText();
            address = txt_address.getText();
            totalAmount = courseFee - discountAmount;
            dueAmount = totalAmount;
            remarks = txt_remarks.getText();
            time = txt_time.getSelectedItem().toString();

            studentImage = new byte[(int) selectedFile.length()];
            FileInputStream fis = new FileInputStream(selectedFile);
            fis.read(studentImage);
            fis.close();

            int selectedId = getSelectId();

            Connection conn = DBConnection.getConnection();

            String sql = "update testprepstudents set name = ?, course = ?, fee = ?, discount = ?, dob = ?, contact = ?, parents_name = ?, "
                    + "parents_contact = ?, address = ?, email = ?, due_amount = ?, total_amount = ?, remarks = ?, student_img = ?, time = ? where id = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, courseName);
            pst.setInt(3, courseFee);
            pst.setInt(4, discountAmount);
            pst.setString(5, dob);
            pst.setString(6, studentContact);
            pst.setString(7, parentName);
            pst.setString(8, parentContact);
            pst.setString(9, address);
            pst.setString(10, email);
            pst.setInt(11, dueAmount); // Set due amount here
            pst.setInt(12, totalAmount); // Set total amount here
            pst.setString(13, remarks);
            pst.setBytes(14, studentImage);
            pst.setString(15, time);
            pst.setInt(16, selectedId);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isupdated = true;
            } else {
                isupdated = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isupdated;
    }

    /**
     *
     * @return
     */
    public boolean deleteStudentsData() {

        boolean isdeleted = false;

        try {

            int getSelectedId = getSelectId();

            Connection con = DBConnection.getConnection();
            String sql = "DELETE from testprepstudents where id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, getSelectedId);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isdeleted = true;
            } else {
                isdeleted = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isdeleted;

    }

    /**
     *
     */
    public void clearData() {
        txt_studentName.setText("");
        txt_courseName.setSelectedIndex(0);
        txt_discountAmount.setText("");
        txt_dob.setText("");
        txt_contact.setText("");
        txt_email.setText("");
        txt_address.setText("");
        txt_parentName.setText("");
        txt_parentContact.setText("");
        txt_remarks.setText("");
        txt_time.setSelectedIndex(0);
        txt_studentImg.setIcon(null);

    }

    /**
     *
     */
    public void clearTable() {

        DefaultTableModel model = (DefaultTableModel) testprepTable.getModel();
        model.setRowCount(0);

    }

    public void getFilterData() {

        String chooseCourse, chooseTime;

        chooseCourse = txt_courseData.getSelectedItem().toString();
        chooseTime = txt_times.getSelectedItem().toString();

        try {

            Connection conn = DBConnection.getConnection();
            String sql = "select id, date, name, course, fee, discount, firstpayment_amount_date, firstpayment_amount, secondpayment_amount_date, secondpayment_amount, "
                    + "thirdpayment_amount_date, thirdpayment_amount, fourthpayment_amount_date, fourthpayment_amount, dob, contact, email, parents_name, parents_contact,"
                    + " address, due_amount, total_paid_amount, total_amount, time, remarks from testprepstudents where course = 'chooseCourse' , time = 'chooseTime'";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("id");
                Date admissionDate = rs.getDate("date");
                String name = rs.getString("name");
                String courseName = rs.getString("course");
                int courseFee = rs.getInt("fee");
                int discount = rs.getInt("discount");
                Date firstPaymentDate = rs.getDate("firstpayment_amount_date");
                int firstPaymentAmount = rs.getInt("firstpayment_amount");
                Date secondPaymentDate = rs.getDate("secondpayment_amount_date");
                int secondPaymentAmount = rs.getInt("secondpayment_amount");
                Date thirdPaymentDate = rs.getDate("thirdpayment_amount_date");
                int thirdPaymentAmount = rs.getInt("thirdpayment_amount");
                Date fourthPaymentDate = rs.getDate("fourthpayment_amount_date");
                int fourthPaymentAmount = rs.getInt("fourthpayment_amount");
                String dob = rs.getString("dob");
                String studentContact = rs.getString("contact");
                String email = rs.getString("email");
                String parentName = rs.getString("parents_name");
                String parentContact = rs.getString("parents_contact");
                String address = rs.getString("address");
                int dueAmount = rs.getInt("due_amount");
                int paidAmount = rs.getInt("total_paid_amount");
                int totalAmount = rs.getInt("total_amount");
                String remarks = rs.getString("remarks");
                String time = rs.getString("time");

                Object[] obj = {id, admissionDate, name, courseName, courseFee, discount, firstPaymentDate, firstPaymentAmount, secondPaymentDate,
                    secondPaymentAmount, thirdPaymentDate, thirdPaymentAmount, fourthPaymentDate, fourthPaymentAmount, dob, studentContact, email, parentName,
                    parentContact, address, dueAmount, paidAmount, totalAmount, remarks, time
                };

                model = (DefaultTableModel) testprepTable.getModel();
                model.addRow(obj);

                double remainAmount = dueAmount / 2;

                int rowIndex = model.getRowCount() - 1;

                // Check condition for due amount and set background color accordingly
                if (remainAmount >= dueAmount) {
                    // Set background color to black for the entire row
                    for (int i = 0; i < model.getColumnCount(); i++) {
                        testprepTable.getColumnModel().getColumn(i).getCellRenderer().getTableCellRendererComponent(testprepTable, null, false, false, rowIndex, i).setBackground(Color.BLACK);
                    }
                }

                int totalPaidAmount = 0, totalDueAmount = 0, totalDueStudents = 0, amount = 0;

                for (int i = 0; i < testprepTable.getRowCount(); i++) {
                    int amt = (Integer) testprepTable.getValueAt(i, 22);
                    amount += amt;

                    int totalamt = (Integer) testprepTable.getValueAt(i, 21);
                    totalPaidAmount += totalamt;

                    int totalDueAmt = (Integer) testprepTable.getValueAt(i, 20);
                    totalDueAmount += totalDueAmt;
                }

                txt_totalAmount.setText(String.valueOf(totalAmount));
                txt_totalPaidAmount.setText(String.valueOf(totalPaidAmount));
                txt_totalDueAmount.setText(String.valueOf(totalDueAmount));

            }

        } catch (Exception e) {

        }

    }

    /**
     *
     * @param file
     */
    public void openfile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void showData(String d1, String d2) {
//        try {
//            Connection conn = DBConnection.getConnection();
//            PreparedStatement pst;
//            ResultSet rs;
//
//            if (d1.equals("") || d2.equals("")) {
//                pst = conn.prepareStatement("select * from computerstudents");
//            } else {
//                pst = conn.prepareStatement("select * from computerstudents where date between ? and ?");
//                pst.setString(1, d1);
//                pst.setString(2, d2);
//            }
//
//            rs = pst.executeQuery();
//
//            DefaultTableModel model = (DefaultTableModel) computerTable.getModel();
//            Object[] row;
//
//            while (rs.next()) {
//                row = new Object[23];
//                row[0] = rs.getInt(1);
//                row[1] = rs.getBlob(2); // Assuming the second column is a DATE type
//                row[2] = rs.getString(3);
//                row[3] = rs.getString(4);
//                row[4] = rs.getInt(5);
//                row[5] = rs.getString(6);
//                row[6] = rs.getString(7);
//                row[7] = rs.getString(8);
//                row[8] = rs.getInt(9);
//                row[9] = rs.getString(10);
//                row[10] = rs.getString(11);
//                row[11] = rs.getString(12);
//                row[12] = rs.getInt(13);
//                row[13] = rs.getString(14);
//                row[14] = rs.getString(15);
//                row[15] = rs.getString(16);
//                row[16] = rs.getInt(17);
//                row[17] = rs.getString(18);
//                row[18] = rs.getString(19);
//                row[19] = rs.getString(20);
//                row[20] = rs.getInt(21);
//                row[21] = rs.getString(22);
//                row[22] = rs.getString(23);
//
//                model.addRow(row);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt_search = new app.bolivia.swing.JCTextField();
        txt_totalAmount = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_totalDueAmount = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txt_totalDueStudents = new javax.swing.JButton();
        txt_totalPaidAmount = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        testprepTable = new rojeru_san.complementos.RSTableMetro();
        panelBorder1 = new com.ims.swing.PanelBorder();
        txt_studentName = new app.bolivia.swing.JCTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_courseName = new rojerusan.RSComboMetro();
        txt_selectimgbtn = new javax.swing.JButton();
        panelBorder2 = new com.ims.swing.PanelBorder();
        txt_studentImg = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txt_dob = new app.bolivia.swing.JCTextField();
        txt_contact = new app.bolivia.swing.JCTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txt_email = new app.bolivia.swing.JCTextField();
        jLabel21 = new javax.swing.JLabel();
        txt_address = new app.bolivia.swing.JCTextField();
        txt_remarks = new app.bolivia.swing.JCTextField();
        jLabel22 = new javax.swing.JLabel();
        txt_parentContact = new app.bolivia.swing.JCTextField();
        jLabel23 = new javax.swing.JLabel();
        txt_parentName = new app.bolivia.swing.JCTextField();
        jLabel24 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        payBtn = new javax.swing.JButton();
        txt_paidAmount = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        txt_dueAmount = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        txt_Amount = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txt_discountAmount = new app.bolivia.swing.JCTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_courseFee = new app.bolivia.swing.JCTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txt_time = new javax.swing.JComboBox<>();
        txt_courseData = new rojerusan.RSComboMetro();
        txt_students = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txt_times = new rojerusan.RSComboMetro();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(242, 246, 253));
        setPreferredSize(new java.awt.Dimension(1111, 1000));

        txt_search.setBackground(new java.awt.Color(255, 255, 255));
        txt_search.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 0, 0)));
        txt_search.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_search.setPhColor(new java.awt.Color(0, 0, 51));
        txt_search.setPlaceholder("Search Students");
        txt_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchActionPerformed(evt);
            }
        });
        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchKeyReleased(evt);
            }
        });

        txt_totalAmount.setBackground(new java.awt.Color(51, 0, 153));
        txt_totalAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_totalAmount.setForeground(new java.awt.Color(255, 255, 255));
        txt_totalAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        txt_totalAmount.setText("0");
        txt_totalAmount.setMaximumSize(new java.awt.Dimension(50, 31));
        txt_totalAmount.setMinimumSize(new java.awt.Dimension(31, 31));
        txt_totalAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalAmountActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 153));
        jLabel1.setText("Total Amount");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 51, 0));
        jLabel2.setText("TotalDue");

        txt_totalDueAmount.setBackground(new java.awt.Color(153, 51, 0));
        txt_totalDueAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_totalDueAmount.setForeground(new java.awt.Color(255, 255, 255));
        txt_totalDueAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        txt_totalDueAmount.setText("0");
        txt_totalDueAmount.setMaximumSize(new java.awt.Dimension(50, 31));
        txt_totalDueAmount.setMinimumSize(new java.awt.Dimension(31, 31));
        txt_totalDueAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalDueAmountActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(0, 102, 102));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("Due No.");

        txt_totalDueStudents.setBackground(new java.awt.Color(0, 102, 102));
        txt_totalDueStudents.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_totalDueStudents.setForeground(new java.awt.Color(153, 255, 255));
        txt_totalDueStudents.setText("0");
        txt_totalDueStudents.setMaximumSize(new java.awt.Dimension(50, 31));
        txt_totalDueStudents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalDueStudentsActionPerformed(evt);
            }
        });

        txt_totalPaidAmount.setBackground(new java.awt.Color(0, 102, 51));
        txt_totalPaidAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_totalPaidAmount.setForeground(new java.awt.Color(153, 255, 153));
        txt_totalPaidAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/rs.png"))); // NOI18N
        txt_totalPaidAmount.setText("0");
        txt_totalPaidAmount.setMaximumSize(new java.awt.Dimension(50, 31));
        txt_totalPaidAmount.setMinimumSize(new java.awt.Dimension(31, 31));
        txt_totalPaidAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalPaidAmountActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 51));
        jLabel4.setText("Total Paid");

        jLabel5.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel5.setText("DASHBOARD / TEST PREPARATION");

        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(204, 204, 204)));
        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        testprepTable.setAutoCreateRowSorter(true);
        testprepTable.setForeground(new java.awt.Color(255, 255, 255));
        testprepTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S.N", "Admission Date", "Student Name", "Course Name", "Course Fee", "Discount ", "1st payment Date", "1st payment amt", "2nd payment date", "2nd payment amt", "3rd payment date", "3rd payment amt", "4th payment date", "4th payment amt", "Student Contact", "Student Email", "Student DOB", "Parent's Name", "Parent's Contact", "Address", "Due Amount", "Paid Amount", "Amount", "Remarks", "time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        testprepTable.setAutoResizeMode(0);
        testprepTable.setColorBackgoundHead(new java.awt.Color(0, 102, 51));
        testprepTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        testprepTable.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        testprepTable.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        testprepTable.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        testprepTable.setRowHeight(40);
        testprepTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                testprepTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(testprepTable);
        if (testprepTable.getColumnModel().getColumnCount() > 0) {
            testprepTable.getColumnModel().getColumn(0).setMinWidth(50);
            testprepTable.getColumnModel().getColumn(0).setMaxWidth(100);
            testprepTable.getColumnModel().getColumn(1).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(1).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(2).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(2).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(3).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(3).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(4).setMinWidth(150);
            testprepTable.getColumnModel().getColumn(4).setMaxWidth(400);
            testprepTable.getColumnModel().getColumn(5).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(5).setMaxWidth(400);
            testprepTable.getColumnModel().getColumn(6).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(6).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(7).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(7).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(8).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(8).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(9).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(9).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(10).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(10).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(11).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(11).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(12).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(12).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(13).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(13).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(14).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(14).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(15).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(15).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(16).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(16).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(17).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(17).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(18).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(18).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(19).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(19).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(20).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(20).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(21).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(21).setMaxWidth(500);
            testprepTable.getColumnModel().getColumn(22).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(22).setMaxWidth(200);
            testprepTable.getColumnModel().getColumn(23).setMinWidth(200);
            testprepTable.getColumnModel().getColumn(23).setMaxWidth(700);
            testprepTable.getColumnModel().getColumn(24).setMinWidth(100);
            testprepTable.getColumnModel().getColumn(24).setMaxWidth(300);
        }

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));
        panelBorder1.setPreferredSize(new java.awt.Dimension(3000, 936));

        txt_studentName.setBackground(new java.awt.Color(255, 255, 255));
        txt_studentName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_studentName.setForeground(new java.awt.Color(0, 0, 51));
        txt_studentName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 0, 51));
        jLabel8.setText("Enter Student Name");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 0, 51));
        jLabel11.setText("Select Course");

        txt_courseName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_courseName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_courseNameActionPerformed(evt);
            }
        });

        txt_selectimgbtn.setBackground(new java.awt.Color(51, 51, 51));
        txt_selectimgbtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_selectimgbtn.setForeground(new java.awt.Color(255, 255, 255));
        txt_selectimgbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/image.png"))); // NOI18N
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
                .addComponent(txt_studentImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 51));
        jLabel14.setText("Enter DOB");

        txt_dob.setBackground(new java.awt.Color(255, 255, 255));
        txt_dob.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_dob.setForeground(new java.awt.Color(0, 0, 51));
        txt_dob.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_contact.setBackground(new java.awt.Color(255, 255, 255));
        txt_contact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_contact.setForeground(new java.awt.Color(0, 0, 51));
        txt_contact.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_contact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_contactKeyPressed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 0, 51));
        jLabel15.setText("Enter Contact");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 0, 51));
        jLabel20.setText("Enter Email");

        txt_email.setBackground(new java.awt.Color(255, 255, 255));
        txt_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_email.setForeground(new java.awt.Color(0, 0, 51));
        txt_email.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 0, 51));
        jLabel21.setText("Enter Address");

        txt_address.setBackground(new java.awt.Color(255, 255, 255));
        txt_address.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_address.setForeground(new java.awt.Color(0, 0, 51));
        txt_address.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txt_remarks.setBackground(new java.awt.Color(255, 255, 255));
        txt_remarks.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_remarks.setForeground(new java.awt.Color(0, 0, 51));
        txt_remarks.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 0, 51));
        jLabel22.setText("Enter Remarks");

        txt_parentContact.setBackground(new java.awt.Color(255, 255, 255));
        txt_parentContact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_parentContact.setForeground(new java.awt.Color(0, 0, 51));
        txt_parentContact.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_parentContact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_parentContactKeyPressed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 0, 51));
        jLabel23.setText("Parent's Contact");

        txt_parentName.setBackground(new java.awt.Color(255, 255, 255));
        txt_parentName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 2, new java.awt.Color(153, 153, 255)));
        txt_parentName.setForeground(new java.awt.Color(0, 0, 51));
        txt_parentName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_parentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_parentNameActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 0, 51));
        jLabel24.setText("Parent's Name");

        addBtn.setBackground(new java.awt.Color(0, 102, 102));
        addBtn.setForeground(new java.awt.Color(255, 255, 255));
        addBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/add.png"))); // NOI18N
        addBtn.setText("ADD STUDENTS");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        deleteBtn.setBackground(new java.awt.Color(153, 0, 0));
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/delete.png"))); // NOI18N
        deleteBtn.setText("DELETE STUDENTS");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        updateBtn.setBackground(new java.awt.Color(153, 153, 0));
        updateBtn.setForeground(new java.awt.Color(255, 255, 255));
        updateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/update.png"))); // NOI18N
        updateBtn.setText("UPDATE STUDENTS");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        payBtn.setBackground(new java.awt.Color(0, 102, 51));
        payBtn.setForeground(new java.awt.Color(255, 255, 255));
        payBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/pay.png"))); // NOI18N
        payBtn.setText("PAY");
        payBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payBtnActionPerformed(evt);
            }
        });

        txt_paidAmount.setBackground(new java.awt.Color(51, 102, 0));
        txt_paidAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_paidAmount.setForeground(new java.awt.Color(255, 255, 255));
        txt_paidAmount.setText("0");
        txt_paidAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_paidAmountActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 0, 51));
        jLabel25.setText("Paid Amount");

        txt_dueAmount.setBackground(new java.awt.Color(255, 51, 51));
        txt_dueAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_dueAmount.setForeground(new java.awt.Color(255, 255, 255));
        txt_dueAmount.setText("0");
        txt_dueAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dueAmountActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 0, 51));
        jLabel26.setText("Due Amount");

        txt_Amount.setBackground(new java.awt.Color(153, 102, 255));
        txt_Amount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_Amount.setForeground(new java.awt.Color(255, 255, 255));
        txt_Amount.setText("0");
        txt_Amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_AmountActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 0, 51));
        jLabel27.setText("Total Amount");

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

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 0, 51));
        jLabel16.setText("Enter Discount");

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

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 0, 51));
        jLabel17.setText("Course Fee");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 0, 51));
        jLabel28.setText("Select Time");

        txt_time.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_time.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_timeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_courseName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_studentName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_selectimgbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_dob, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_discountAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_courseFee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_time, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(50, 50, 50)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_address, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_parentName, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_parentContact, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(txt_remarks, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_contact, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_paidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_dueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(payBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_paidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_dueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(17, 17, 17)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_parentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_parentContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(txt_selectimgbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(txt_studentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(txt_courseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_courseFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_discountAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_dob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_time, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_remarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(133, 133, 133))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                        .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(63, 63, 63)))
                .addComponent(payBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        txt_courseData.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL" }));
        txt_courseData.setColorArrow(new java.awt.Color(102, 102, 0));
        txt_courseData.setColorBorde(new java.awt.Color(0, 0, 255));
        txt_courseData.setColorFondo(new java.awt.Color(153, 153, 153));
        txt_courseData.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N

        txt_students.setBackground(new java.awt.Color(102, 0, 51));
        txt_students.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_students.setForeground(new java.awt.Color(153, 255, 255));
        txt_students.setText("0");
        txt_students.setMaximumSize(new java.awt.Dimension(50, 31));
        txt_students.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_studentsActionPerformed(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(102, 0, 51));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 0, 51));
        jLabel6.setText("Std No.");

        txt_times.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));
        txt_times.setColorArrow(new java.awt.Color(102, 102, 0));
        txt_times.setColorBorde(new java.awt.Color(0, 0, 255));
        txt_times.setColorFondo(new java.awt.Color(153, 153, 153));
        txt_times.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N
        txt_times.setName(""); // NOI18N

        jButton1.setBackground(new java.awt.Color(204, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(102, 102, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/search.png"))); // NOI18N
        jButton1.setText("search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBorder1, 479, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_courseData, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_times, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_search, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_students, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_totalDueStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(4, 4, 4))
                            .addComponent(txt_totalDueAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_totalPaidAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(jLabel4))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_totalAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(jLabel1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_courseData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_times, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txt_totalPaidAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(8, 8, 8)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txt_totalAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_students, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_totalDueAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_totalDueStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_totalAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalAmountActionPerformed

    private void txt_totalDueAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalDueAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalDueAmountActionPerformed

    private void txt_totalPaidAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalPaidAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalPaidAmountActionPerformed

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked

    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void testprepTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_testprepTableMouseClicked
        int rowNo = testprepTable.getSelectedRow();
        TableModel model = testprepTable.getModel();

        txt_studentName.setText(model.getValueAt(rowNo, 2).toString());

        String courseName = model.getValueAt(rowNo, 3).toString();
        txt_courseName.setSelectedItem(courseName);

        txt_dob.setText(model.getValueAt(rowNo, 16).toString());
        txt_contact.setText(model.getValueAt(rowNo, 14).toString());
        txt_email.setText(model.getValueAt(rowNo, 15).toString());
        txt_parentName.setText(model.getValueAt(rowNo, 17).toString());
        txt_parentContact.setText(model.getValueAt(rowNo, 18).toString());
        txt_address.setText(model.getValueAt(rowNo, 19).toString());
        txt_remarks.setText(model.getValueAt(rowNo, 23).toString());
        txt_paidAmount.setText(model.getValueAt(rowNo, 21).toString());
        txt_dueAmount.setText(model.getValueAt(rowNo, 20).toString());
        txt_Amount.setText(model.getValueAt(rowNo, 22).toString());
        txt_time.setSelectedItem(model.getValueAt(rowNo, 23).toString());

        try {

            Connection conn = DBConnection.getConnection();
            String sql = "SELECT student_img FROM testprepstudents WHERE id = ?"; // Assuming student_id is the primary key of your table
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, model.getValueAt(rowNo, 0).toString()); // Assuming student_id is in the first column
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                InputStream input = rs.getBinaryStream("student_img");
                if (input != null) {
                    BufferedImage image = ImageIO.read(input);
                    if (image != null) {
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
            } else {
                // Handle case where no student is found with the given ID
                System.err.println("No student found with the given ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_testprepTableMouseClicked


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


    private void txt_parentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_parentNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_parentNameActionPerformed

    private void txt_totalDueStudentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalDueStudentsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalDueStudentsActionPerformed

    private void txt_parentContactKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_parentContactKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            txt_parentContact.setEditable(false);
        } else {
            txt_parentContact.setEditable(true);
        }
    }//GEN-LAST:event_txt_parentContactKeyPressed

    private void txt_contactKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_contactKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            txt_contact.setEditable(false);
        } else {
            txt_contact.setEditable(true);
        }
    }//GEN-LAST:event_txt_contactKeyPressed

    private void txt_paidAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_paidAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_paidAmountActionPerformed

    private void txt_dueAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dueAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dueAmountActionPerformed

    private void txt_AmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_AmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_AmountActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        File selectedFile = ch.getSelectedFile();
        boolean isInserted = false;

        // Check if a file is selected
        if (selectedFile != null) {
            isInserted = insertData(selectedFile);
        } else {
            // File is not selected, proceed with inserting data without an image
            isInserted = insertData(null);
        }

        if (isInserted) {
            // Data inserted successfully
            JOptionPane.showMessageDialog(null, "Student added successfully!");
            clearTable();
            getStudentsData();
            getDueStudents();
            clearData();
        } else {
            // Data insertion failed
            JOptionPane.showMessageDialog(null, "Failed to add student. Please make sure required fields are filled.");
        }
    }//GEN-LAST:event_addBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        boolean isUpdated = updateStudentsData(ch.getSelectedFile());

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("select discount from testprepstudents");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int discountAmount = rs.getInt("discount");

                if (discountAmount > 0) {
                    JOptionPane.showMessageDialog(this, "Discount cannot be modified or changed..!!", "Error", JOptionPane.ERROR_MESSAGE);
                    // No need to proceed with updating the data if discount amount is available
                    return;
                }
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while checking discount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isUpdated) {
            // Data inserted successfully
            JOptionPane.showMessageDialog(null, "Student Update successfully!");
            clearTable();
            getStudentsData();
            getDueStudents();
            clearData();
        } else {
            // Data insertion failed
            JOptionPane.showMessageDialog(null, "Failed to Update student.");
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed

        if (deleteStudentsData() == true) {
            JOptionPane.showMessageDialog(this, "Student Deleted Successfully..");;
            clearTable();
            getStudentsData();
            getDueStudents();
            clearData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed To Delete Student..!!");
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void payBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payBtnActionPerformed
        computerPayment payment = new computerPayment();
        payment.setVisible(true);
    }//GEN-LAST:event_payBtnActionPerformed

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

    private void txt_courseFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_courseFeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_courseFeeActionPerformed

    private void txt_courseFeeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_courseFeeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_courseFeeKeyPressed

    private void txt_courseNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_courseNameActionPerformed
        getCourseFee();
    }//GEN-LAST:event_txt_courseNameActionPerformed

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed

    }//GEN-LAST:event_txt_searchActionPerformed

    /**
     *
     * @param str
     */
    public void search(String str) {
        if (str == null) {
            return;
        }
        model = (DefaultTableModel) testprepTable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        testprepTable.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }

    private void txt_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyReleased

        try {
            String searchStudent = txt_search.getText();
            search(searchStudent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_txt_searchKeyReleased

    private void txt_studentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentsActionPerformed

    private void txt_timeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_timeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_timeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        getFilterData();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private com.ims.swing.PanelBorder panelBorder1;
    private com.ims.swing.PanelBorder panelBorder2;
    private javax.swing.JButton payBtn;
    public static rojeru_san.complementos.RSTableMetro testprepTable;
    private javax.swing.JButton txt_Amount;
    private app.bolivia.swing.JCTextField txt_address;
    private app.bolivia.swing.JCTextField txt_contact;
    private rojerusan.RSComboMetro txt_courseData;
    private app.bolivia.swing.JCTextField txt_courseFee;
    private rojerusan.RSComboMetro txt_courseName;
    private app.bolivia.swing.JCTextField txt_discountAmount;
    private app.bolivia.swing.JCTextField txt_dob;
    private javax.swing.JButton txt_dueAmount;
    private app.bolivia.swing.JCTextField txt_email;
    private javax.swing.JButton txt_paidAmount;
    private app.bolivia.swing.JCTextField txt_parentContact;
    private app.bolivia.swing.JCTextField txt_parentName;
    private app.bolivia.swing.JCTextField txt_remarks;
    private app.bolivia.swing.JCTextField txt_search;
    private javax.swing.JButton txt_selectimgbtn;
    private javax.swing.JLabel txt_studentImg;
    private app.bolivia.swing.JCTextField txt_studentName;
    private javax.swing.JButton txt_students;
    private javax.swing.JComboBox<String> txt_time;
    private rojerusan.RSComboMetro txt_times;
    private javax.swing.JButton txt_totalAmount;
    private javax.swing.JButton txt_totalDueAmount;
    private javax.swing.JButton txt_totalDueStudents;
    private javax.swing.JButton txt_totalPaidAmount;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables

}
