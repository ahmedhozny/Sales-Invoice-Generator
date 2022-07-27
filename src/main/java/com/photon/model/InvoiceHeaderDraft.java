package com.photon.model;

import java.util.Date;

public class InvoiceHeaderDraft extends InvoiceHeader {

    public InvoiceHeaderDraft(InvoiceHeader invoiceHeader){
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
    public InvoiceHeaderDraft getDraft() {
        return this;
    }
}
