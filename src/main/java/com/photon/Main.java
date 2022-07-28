package com.photon;

import com.photon.model.InvoiceHeader;
import com.photon.view.DisplayedFrame;

import javax.management.InstanceAlreadyExistsException;
import java.text.SimpleDateFormat;

public class Main {

    /**
     * Starts frame and initialises required field
     */
    public static void main(String[] args) {
        InvoiceHeader.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        InvoiceHeader.dateFormat.setLenient(false);
        try {
            new DisplayedFrame().setVisible(true);
        } catch (InstanceAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Outputs initially loaded invoices
     */
    public static void test(){
        for (int i = 0; i < InvoiceHeader.countInvoices(); i++) {
            InvoiceHeader invoice = InvoiceHeader.getInvoiceByIndex(i);
            String str = invoice.getInvoiceNumber() + System.lineSeparator() + "{"
                    + System.lineSeparator() + invoice.getFormattedDate() + ", " + invoice.getCustomer() + System.lineSeparator();
            for (int j = 0; j < invoice.getNumberOfLines(); j++) {
                str += invoice.getInvoiceLine(j).toString() + System.lineSeparator();
            }

            str += "}" + System.lineSeparator();
            System.out.println(str);
        }
    }
}
