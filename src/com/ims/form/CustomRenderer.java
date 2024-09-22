/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ims.form;

import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 *
 * @author _Spoidey-
 */
public class CustomRenderer extends DefaultTableCellRenderer {

     // Column indices for desired amount and total amount
    private static final int DESIRED_AMOUNT_COLUMN_INDEX = 22;
    private static final int TOTAL_AMOUNT_COLUMN_INDEX = 24;

    // Constructor to initialize the renderer
    public CustomRenderer() {
    }

    // Override getTableCellRendererComponent method
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Check if the current column index matches the desired amount column index
        if (column == DESIRED_AMOUNT_COLUMN_INDEX) {
            // Get the values of desired amount and total amount
            int desiredAmount = Integer.parseInt(Computer.computerTable.getValueAt(row, DESIRED_AMOUNT_COLUMN_INDEX).toString());
            int totalAmount = Integer.parseInt(Computer.computerTable.getValueAt(row, TOTAL_AMOUNT_COLUMN_INDEX).toString());

            // Check the value of the cell and set background color accordingly
            if (desiredAmount == 0) {
                cellComponent.setBackground(new Color(51, 102, 0)); // Desired color for condition
            } else if (desiredAmount == totalAmount) {
                cellComponent.setBackground(new Color(51, 51, 5)); // Desired color for condition
            } else if (desiredAmount > totalAmount / 2) {
                cellComponent.setBackground(new Color(204, 102, 0)); // Desired color for condition
            } else {
                cellComponent.setBackground(new Color(153, 102, 255)); // Desired color for condition
            }
        } else {
            // Set default background color for other columns
            cellComponent.setBackground(table.getBackground());
        }

        return cellComponent;
    }
    
}
