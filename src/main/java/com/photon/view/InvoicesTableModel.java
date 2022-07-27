package com.photon.view;

import com.photon.model.InvoiceHeader;

import javax.swing.table.AbstractTableModel;

/**
 * Prepares the representation needed for an invoice headers
 */
public class InvoicesTableModel extends AbstractTableModel {

    @Override
    public String getColumnName(int column) {
        return new String[]{"No.", "Date", "Customer", "Total"}[column];
    }

    @Override
    public int getRowCount() {
        return InvoiceHeader.countInvoices();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader invoiceHeader = InvoiceHeader.getInvoiceByIndex(rowIndex);
        return switch (columnIndex) {
            case 0 -> invoiceHeader.getInvoiceNumber();
            case 1 -> invoiceHeader.getFormattedDate();
            case 2 -> invoiceHeader.getCustomer();
            case 3 -> invoiceHeader.getTotalPrice();
            default -> null;
        };
    }
}
