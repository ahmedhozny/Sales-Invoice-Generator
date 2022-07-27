package com.photon.view;

import com.photon.model.InvoiceHeader;
import com.photon.model.InvoiceLine;

import javax.swing.table.AbstractTableModel;

/**
 * Prepares the representation needed for an invoice lines
 */
public class InvoiceLinesTableModel extends AbstractTableModel{

    private final InvoiceHeader invoiceHeader;

    public InvoiceLinesTableModel(InvoiceHeader invoiceHeader){
        this.invoiceHeader = invoiceHeader;
    }

    @Override
    public String getColumnName(int column) {
        return new String [] {"No.", "Item Name", "Item Price", "Count", "Item Total"}[column];
    }

    @Override
    public int getRowCount() {
        int size = 0;
        if (this.invoiceHeader != null)
            size = this.invoiceHeader.getNumberOfLines();
        return size;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine item = this.invoiceHeader.getDraft().getInvoiceLine(rowIndex);
        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> item.getDescription();
            case 2 -> item.getPrice();
            case 3 -> item.getCount();
            case 4 -> item.totalPrice();
            default -> null;
        };
    }
}
