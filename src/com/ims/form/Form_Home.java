package com.ims.form;

import com.ims.add.AddCourse;
import com.ims.add.AddStudents;
import com.ims.add.AddTimeTable;
import com.ims.connection.DBConnection;
import com.ims.due.ComputerDue;
import com.ims.due.LanguageDue;
import com.ims.due.TestPrepDue;
import com.ims.due.TuitionDue;
import com.ims.model.Model_Card;
import com.ims.others.DailyEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;

public class Form_Home extends javax.swing.JPanel {

    int totalIncome = 0, totalExpenses = 0, netIncome = 0, dailyIncome = 0, dailyExpenses = 0, DailyNetIncome = 0;
    int monthlyIncome = 0, monthlyExpenses = 0, monthlyNetExpenses = 0;
    int dueComputer = 0, dueTuition = 0, dueLanguage = 0, dueTestPrep = 0;
    int computerIncome = 0, tuitionIncome = 0, languageIncome = 0, testprepIncome = 0, servicesIncome = 0, dailyentryIncome = 0;
    int dailyComputerIncome = 0, dailyTuitionIncome = 0, dailyLanguageIncome = 0, dailyTesprepIncome = 0, dailyServicesIncome = 0, todayentryIncome = 0;
    int expenses = 0, staffsexpenses = 0;

    public Form_Home() {
        initComponents();
        dailyIncomeExpenses();
        totalIncomeExpenses();
        dueStudentReports();
        monthlyIncomeExpenses();
    }

    public void dailyIncomeExpenses() {
        try {
            Connection conn = DBConnection.getConnection();

            // Get today's date
            Date today = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            // Calculating Daily Income
            String dailyIncomeQuery = "SELECT SUM(total_paid_amount) AS daily_income FROM ("
                    + "SELECT total_paid_amount FROM computerstudents WHERE date = ? "
                    + "UNION ALL "
                    + "SELECT total_paid_amount FROM tuitionstudents WHERE date = ? "
                    + "UNION ALL "
                    + "SELECT total_paid_amount FROM languagestudents WHERE date = ? "
                    + "UNION ALL "
                    + "SELECT total_paid_amount FROM testprepstudents WHERE date = ? "
                    + "UNION ALL "
                    + "SELECT paid_amount FROM dailyentry WHERE entry_date = ? "
                    + "UNION ALL "
                    + "SELECT paid_amount FROM services WHERE date = ?) AS daily_income_table";
            PreparedStatement incomeStatement = conn.prepareStatement(dailyIncomeQuery);
            incomeStatement.setString(1, formatDate.format(today));
            incomeStatement.setString(2, formatDate.format(today));
            incomeStatement.setString(3, formatDate.format(today));
            incomeStatement.setString(4, formatDate.format(today));
            incomeStatement.setString(5, formatDate.format(today));
            incomeStatement.setString(6, formatDate.format(today));
            ResultSet incomeResultSet = incomeStatement.executeQuery();
            int dailyIncome = 0;
            if (incomeResultSet.next()) {
                dailyIncome = incomeResultSet.getInt("daily_income");
            }

            // Calculating Daily Expenses
            String dailyExpensesQuery = "SELECT SUM(amount) AS daily_expenses FROM expenses WHERE date = ?";
            PreparedStatement expensesStatement = conn.prepareStatement(dailyExpensesQuery);
            expensesStatement.setString(1, formatDate.format(today));
            ResultSet expensesResultSet = expensesStatement.executeQuery();
            int dailyExpenses = 0;
            if (expensesResultSet.next()) {
                dailyExpenses = expensesResultSet.getInt("daily_expenses");
            }

            int dailyNetIncome = dailyIncome - dailyExpenses;

            card1.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/daily-income.png")), "Daily Income", "Rs. " + dailyIncome));
            card2.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/daily-expenses.png")), "Daily Expenses", "Rs. " + dailyExpenses));
            card3.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/daily-net-income.png")), "Daily Net Income", "Rs. " + dailyNetIncome));

            incomeResultSet.close();
            expensesResultSet.close();
            incomeStatement.close();
            expensesStatement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Monthly Income & Expenses
    public void monthlyIncomeExpenses() {
        try {
            Connection conn = DBConnection.getConnection();

            // Calculate start and end dates for the current month
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1); // Set to first day of the month
            Date startDate = calendar.getTime();
            calendar.add(Calendar.MONTH, 1); // Add one month
            calendar.add(Calendar.DATE, -1); // Subtract one day to get last day of the month
            Date endDate = calendar.getTime();

            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            // Calculating Monthly Income
            String monthlyIncomeQuery = "SELECT SUM(total_paid_amount) AS monthly_income FROM ("
                    + "SELECT total_paid_amount FROM computerstudents WHERE date BETWEEN ? AND ? "
                    + "UNION ALL "
                    + "SELECT total_paid_amount FROM tuitionstudents WHERE date BETWEEN ? AND ? "
                    + "UNION ALL "
                    + "SELECT total_paid_amount FROM languagestudents WHERE date BETWEEN ? AND ? "
                    + "UNION ALL "
                    + "SELECT total_paid_amount FROM testprepstudents WHERE date BETWEEN ? AND ? "
                    + "UNION ALL "
                    + "SELECT paid_amount FROM dailyentry WHERE entry_date BETWEEN ? AND ? "
                    + "UNION ALL "
                    + "SELECT paid_amount FROM services WHERE date BETWEEN ? AND ?) AS monthly_income_table";
            PreparedStatement incomeStatement = conn.prepareStatement(monthlyIncomeQuery);
            incomeStatement.setString(1, formatDate.format(startDate));
            incomeStatement.setString(2, formatDate.format(endDate));
            incomeStatement.setString(3, formatDate.format(startDate));
            incomeStatement.setString(4, formatDate.format(endDate));
            incomeStatement.setString(5, formatDate.format(startDate));
            incomeStatement.setString(6, formatDate.format(endDate));
            incomeStatement.setString(7, formatDate.format(startDate));
            incomeStatement.setString(8, formatDate.format(endDate));
            incomeStatement.setString(9, formatDate.format(startDate));
            incomeStatement.setString(10, formatDate.format(endDate));
            incomeStatement.setString(11, formatDate.format(startDate));
            incomeStatement.setString(12, formatDate.format(endDate));
            ResultSet incomeResultSet = incomeStatement.executeQuery();
            int monthlyIncome = 0;
            if (incomeResultSet.next()) {
                monthlyIncome = incomeResultSet.getInt("monthly_income");
            }

            // Calculating Monthly Expenses
            String monthlyExpensesQuery = "SELECT SUM(amount) AS monthly_expenses FROM expenses WHERE date BETWEEN ? AND ?";
            PreparedStatement expensesStatement = conn.prepareStatement(monthlyExpensesQuery);
            expensesStatement.setString(1, formatDate.format(startDate));
            expensesStatement.setString(2, formatDate.format(endDate));
            ResultSet expensesResultSet = expensesStatement.executeQuery();
            int monthlyExpenses = 0;
            if (expensesResultSet.next()) {
                monthlyExpenses = expensesResultSet.getInt("monthly_expenses");
            }

            int monthlyNetIncome = monthlyIncome - monthlyExpenses;

            card4.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/daily-income.png")), "Monthly Income", "Rs. " + monthlyIncome));
            card5.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/daily-expenses.png")), "Monthly Expenses", "Rs. " + monthlyExpenses));
            card6.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/daily-net-income.png")), "Monthly Net Income", "Rs. " + monthlyNetIncome));

            incomeResultSet.close();
            expensesResultSet.close();
            incomeStatement.close();
            expensesStatement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Due Students Reports
    public void dueStudentReports() {
        try {
            Connection conn = DBConnection.getConnection();

            // Calculating Due Reports
            String computerDueQuery = "select count(*) as due_computer from computerstudents where due_amount > 0";
            PreparedStatement computerDueStatement = conn.prepareStatement(computerDueQuery);
            ResultSet computerDueResultSet = computerDueStatement.executeQuery();
            if (computerDueResultSet.next()) {
                dueComputer = computerDueResultSet.getInt("due_computer");
            }

            String tuitionDueQuery = "select count(*) as due_tuition from tuitionstudents where due_amount > 0";
            PreparedStatement tuitionDueStatement = conn.prepareStatement(tuitionDueQuery);
            ResultSet tuitionDueResultSet = tuitionDueStatement.executeQuery();
            if (tuitionDueResultSet.next()) {
                dueTuition = tuitionDueResultSet.getInt("due_tuition");
            }

            String languageDueQuery = "select count(*) as due_language from languagestudents where due_amount > 0";
            PreparedStatement languageDueStatement = conn.prepareStatement(languageDueQuery);
            ResultSet languageDueResultSet = languageDueStatement.executeQuery();
            if (languageDueResultSet.next()) {
                dueLanguage = languageDueResultSet.getInt("due_language");
            }

            String testPrepDueQuery = "select count(*) as due_testprep from testprepstudents where due_amount > 0";
            PreparedStatement testprepDueStatement = conn.prepareStatement(testPrepDueQuery);
            ResultSet testprepDueResultSet = testprepDueStatement.executeQuery();
            if (testprepDueResultSet.next()) {
                dueTestPrep = testprepDueResultSet.getInt("due_testprep");
            }

            card7.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/2.png")), "Due Computer", "" + dueComputer));
            card8.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/3.png")), "Due Tuition", "" + dueTuition));
            card9.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/4.png")), "Due Language", "" + dueLanguage));
            card10.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/5.png")), "Due Test Prep", "" + dueTestPrep));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Total Income & Expenses
    public void totalIncomeExpenses() {
        try {
            Connection conn = DBConnection.getConnection();
            // Calculating Total Income
            String computerQuery = "select sum(total_paid_amount) from computerstudents";
            PreparedStatement computerStatement = conn.prepareStatement(computerQuery);
            ResultSet computerResult = computerStatement.executeQuery();
            if (computerResult.next()) {
                computerIncome = computerResult.getInt(1);
                totalIncome += computerIncome;
            }

            String tuitionQuery = "select sum(total_paid_amount) from tuitionstudents";
            PreparedStatement tuitionStatement = conn.prepareStatement(tuitionQuery);
            ResultSet tuitionResult = tuitionStatement.executeQuery();
            if (tuitionResult.next()) {
                tuitionIncome = tuitionResult.getInt(1);
                totalIncome += tuitionIncome;
            }

            String languageQuery = "select sum(total_paid_amount) from languagestudents";
            PreparedStatement languageStatement = conn.prepareStatement(languageQuery);
            ResultSet languageResult = languageStatement.executeQuery();
            if (languageResult.next()) {
                languageIncome = languageResult.getInt(1);
                totalIncome += languageIncome;
            }

            String testprepQuery = "select sum(total_paid_amount) from testprepstudents";
            PreparedStatement testprepStatement = conn.prepareStatement(testprepQuery);
            ResultSet testprepResult = testprepStatement.executeQuery();
            if (testprepResult.next()) {
                testprepIncome = testprepResult.getInt(1);
                totalIncome += testprepIncome;
            }

            String servicesQuery = "select sum(paid_amount) from services";
            PreparedStatement servicesStatement = conn.prepareStatement(servicesQuery);
            ResultSet servicesResult = servicesStatement.executeQuery();
            if (servicesResult.next()) {
                servicesIncome = servicesResult.getInt(1);
                totalIncome += servicesIncome;
            }

            String dailyentryQuery = "select sum(paid_amount) from dailyentry";
            PreparedStatement dailyentryStatement = conn.prepareStatement(dailyentryQuery);
            ResultSet dailyentryResult = dailyentryStatement.executeQuery();
            if (dailyentryResult.next()) {
                dailyentryIncome = dailyentryResult.getInt(1);
                totalIncome += dailyentryIncome;
            }

            // Calculating Total Expenses
            String staffExpensesQuery = "select sum(total_amount) from staffexpenses";
            PreparedStatement staffsExpensesStatement = conn.prepareStatement(staffExpensesQuery);
            ResultSet staffsExpensesResult = staffsExpensesStatement.executeQuery();
            if (staffsExpensesResult.next()) {
                staffsexpenses = staffsExpensesResult.getInt(1);
                totalExpenses += staffsexpenses;
            }

            String expensesQuery = "select sum(amount) from expenses";
            PreparedStatement expensesStatement = conn.prepareStatement(expensesQuery);
            ResultSet expensesResult = expensesStatement.executeQuery();
            if (expensesResult.next()) {
                expenses = expensesResult.getInt(1);
                totalExpenses += expenses;
            }

            // Calculating Total Net Income or Loss;
            netIncome = totalIncome - totalExpenses;

            card11.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/income.png")), "Total Income", "Rs. " + totalIncome));
            card12.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/expense.png")), "Total Expenses", "Rs. " + totalExpenses));
            card13.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/ims/icon/daily-net-income.png")), "Total Net Income", "Rs. " + netIncome));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelShadow1 = new com.ims.swing.PanelShadow();
        card1 = new com.ims.component.Card();
        card2 = new com.ims.component.Card();
        card3 = new com.ims.component.Card();
        jLabel1 = new javax.swing.JLabel();
        card5 = new com.ims.component.Card();
        card4 = new com.ims.component.Card();
        card6 = new com.ims.component.Card();
        jLabel4 = new javax.swing.JLabel();
        card7 = new com.ims.component.Card();
        card8 = new com.ims.component.Card();
        card9 = new com.ims.component.Card();
        card10 = new com.ims.component.Card();
        jLabel3 = new javax.swing.JLabel();
        panelBorder2 = new com.ims.swing.PanelBorder();
        card11 = new com.ims.component.Card();
        card12 = new com.ims.component.Card();
        card13 = new com.ims.component.Card();
        jLabel6 = new javax.swing.JLabel();
        panelBorder1 = new com.ims.swing.PanelBorder();
        addStudentBtn = new javax.swing.JButton();
        addCourseBtn = new javax.swing.JButton();
        addDailyEntryBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        addTimeBTn = new javax.swing.JButton();

        setBackground(new java.awt.Color(242, 246, 253));
        setForeground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1087, 850));

        panelShadow1.setBackground(new java.awt.Color(51, 51, 51));
        panelShadow1.setOpaque(true);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Monthly Income & Expenses Report");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Due Students Report");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        card7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card7MouseClicked(evt);
            }
        });

        card8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card8MouseClicked(evt);
            }
        });

        card9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card9MouseClicked(evt);
            }
        });

        card10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card10MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Daily Income & Expenses Report");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        panelBorder2.setBackground(new java.awt.Color(153, 153, 255));

        card11.setBackground(new java.awt.Color(255, 204, 204));

        card12.setBackground(new java.awt.Color(255, 204, 204));

        card13.setBackground(new java.awt.Color(255, 204, 204));

        javax.swing.GroupLayout panelBorder2Layout = new javax.swing.GroupLayout(panelBorder2);
        panelBorder2.setLayout(panelBorder2Layout);
        panelBorder2Layout.setHorizontalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(75, 75, 75))
        );
        panelBorder2Layout.setVerticalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(card11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(card12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(card13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Total Income & Expenses");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelShadow1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelShadow1Layout.createSequentialGroup()
                                .addComponent(card7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(card8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(card9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(28, 28, 28)
                                .addComponent(card10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelShadow1Layout.createSequentialGroup()
                                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelShadow1Layout.createSequentialGroup()
                                        .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(39, 39, 39)
                                        .addComponent(card5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(card6, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelShadow1Layout.createSequentialGroup()
                                        .addComponent(card1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(card2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(card3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(50, 50, 50)
                                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelShadow1Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 699, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelShadow1Layout.createSequentialGroup()
                        .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addComponent(jLabel4))
                    .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(card10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        addStudentBtn.setBackground(new java.awt.Color(51, 102, 255));
        addStudentBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addStudentBtn.setForeground(new java.awt.Color(255, 255, 255));
        addStudentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/add.png"))); // NOI18N
        addStudentBtn.setText("Add Student");
        addStudentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentBtnActionPerformed(evt);
            }
        });

        addCourseBtn.setBackground(new java.awt.Color(255, 102, 102));
        addCourseBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addCourseBtn.setForeground(new java.awt.Color(255, 255, 255));
        addCourseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/add1.png"))); // NOI18N
        addCourseBtn.setText("Add Course");
        addCourseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseBtnActionPerformed(evt);
            }
        });

        addDailyEntryBtn.setBackground(new java.awt.Color(153, 153, 153));
        addDailyEntryBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addDailyEntryBtn.setForeground(new java.awt.Color(255, 255, 255));
        addDailyEntryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ims/icon/update.png"))); // NOI18N
        addDailyEntryBtn.setText("Daily Entry");
        addDailyEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDailyEntryBtnActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("sansserif", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 0, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("ADMIN DASHBOARD");
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        addTimeBTn.setBackground(new java.awt.Color(51, 102, 0));
        addTimeBTn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addTimeBTn.setForeground(new java.awt.Color(255, 255, 255));
        addTimeBTn.setText("Add time");
        addTimeBTn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTimeBTnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addCourseBtn)
                .addGap(18, 18, 18)
                .addComponent(addStudentBtn)
                .addGap(18, 18, 18)
                .addComponent(addDailyEntryBtn)
                .addGap(31, 31, 31)
                .addComponent(addTimeBTn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudentBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addCourseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addDailyEntryBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTimeBTn))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(panelShadow1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addDailyEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDailyEntryBtnActionPerformed
        DailyEntry dailyEntry = new DailyEntry();
        dailyEntry.setVisible(true);
    }//GEN-LAST:event_addDailyEntryBtnActionPerformed

    private void addCourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseBtnActionPerformed
        AddCourse addComputerCourse = new AddCourse();
        addComputerCourse.setVisible(true);
    }//GEN-LAST:event_addCourseBtnActionPerformed

    private void addStudentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentBtnActionPerformed
        AddStudents addStudents = new AddStudents();
        addStudents.setVisible(true);
    }//GEN-LAST:event_addStudentBtnActionPerformed

    private void card7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_card7MouseClicked
        ComputerDue computerDue = new ComputerDue();
        computerDue.setVisible(true);
    }//GEN-LAST:event_card7MouseClicked

    private void card8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_card8MouseClicked
        TuitionDue tuitionDue = new TuitionDue();
        tuitionDue.setVisible(true);
    }//GEN-LAST:event_card8MouseClicked

    private void card9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_card9MouseClicked
        LanguageDue languageDue = new LanguageDue();
        languageDue.setVisible(true);
    }//GEN-LAST:event_card9MouseClicked

    private void card10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_card10MouseClicked
        TestPrepDue testPrepDue = new TestPrepDue();
        testPrepDue.setVisible(true);
    }//GEN-LAST:event_card10MouseClicked

    private void addTimeBTnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTimeBTnActionPerformed
        AddTimeTable addTimeTable = new AddTimeTable();
        addTimeTable.setVisible(true);
    }//GEN-LAST:event_addTimeBTnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCourseBtn;
    private javax.swing.JButton addDailyEntryBtn;
    private javax.swing.JButton addStudentBtn;
    private javax.swing.JButton addTimeBTn;
    private com.ims.component.Card card1;
    private com.ims.component.Card card10;
    private com.ims.component.Card card11;
    private com.ims.component.Card card12;
    private com.ims.component.Card card13;
    private com.ims.component.Card card2;
    private com.ims.component.Card card3;
    private com.ims.component.Card card4;
    private com.ims.component.Card card5;
    private com.ims.component.Card card6;
    private com.ims.component.Card card7;
    private com.ims.component.Card card8;
    private com.ims.component.Card card9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private com.ims.swing.PanelBorder panelBorder1;
    private com.ims.swing.PanelBorder panelBorder2;
    private com.ims.swing.PanelShadow panelShadow1;
    // End of variables declaration//GEN-END:variables
}
