package com.photon;

import com.photon.model.InvoiceHeader;
import com.photon.view.DisplayedFrame;

import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) {
        InvoiceHeader.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        InvoiceHeader.dateFormat.setLenient(false);
        new DisplayedFrame().setVisible(true);
    }

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
