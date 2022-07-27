package com.photon.model;

import java.util.Date;

/**
 * Draft version of InvoiceHeader made for application to make changes on. This makes InvoiceHeader untouched until changes are applied
 */
public class DraftInvoiceHeader extends InvoiceHeader {

    public DraftInvoiceHeader(InvoiceHeader invoiceHeader){
        super(invoiceHeader);
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNum = invoiceNumber;
    }

    public void setDate(Date date) {
        this.invoiceDate = date;
    }

    public void setCustomer(String customer) {
        this.customerName = customer;
    }

    public void addInvoiceLine(InvoiceLine invoiceLine){
        this.lines.add(invoiceLine);
    }

    public void deleteInvoiceLine(int index){
        this.lines.remove(index);
    }

    @Override
    public DraftInvoiceHeader getDraft() {
        return this;
    }
}
