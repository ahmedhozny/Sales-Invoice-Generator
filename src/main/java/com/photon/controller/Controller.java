package com.photon.controller;

import com.photon.Main;
import com.photon.model.InvoiceHeader;
import com.photon.model.InvoiceHeaderDraft;
import com.photon.model.InvoiceLine;
import com.photon.view.DisplayedFrame;
import com.photon.view.InvoiceLineDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;

public class Controller implements ActionListener, MouseListener{
    public static final String FILE_LOAD = "100";
    public static final String FILE_SAVE = "101";
    public static final String BUTTON_NEW_INVOICE = "200";
    public static final String BUTTON_DELETE_INVOICE = "201";
    public static final String BUTTON_INVOICE_SAVE = "300";
    public static final String BUTTON_INVOICE_CANCEL = "301";

    public static final String TEXT_FIELD_DATE_UPDATE = "400";
    public static final String TEXT_FIELD_CUSTOMER_UPDATE = "401";
    public static final String BUTTON_ADD_ITEM = "402";
    public static final String BUTTON_DELETE_ITEM = "403";

    public static final String ITEM_FRAME_OKAY = "500";
    public static final String ITEM_FRAME_CANCEL = "501";

    private int currentSelectedInvoice = -1;

    private int currentSelectedItem = -1;

    private InvoiceLineDialog itemFrameInstance = null;

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case FILE_LOAD -> fileOpener();
            case FILE_SAVE -> fileSaver();
            case BUTTON_NEW_INVOICE -> createNewInvoice();
            case BUTTON_DELETE_INVOICE -> deleteInvoice();
            case TEXT_FIELD_DATE_UPDATE -> updateInvoiceDate();
            case TEXT_FIELD_CUSTOMER_UPDATE -> updateInvoiceCustomer();
            case BUTTON_ADD_ITEM -> addInvoiceItem();
            case BUTTON_DELETE_ITEM -> deleteInvoiceItem();
            case ITEM_FRAME_OKAY, ITEM_FRAME_CANCEL -> itemFrameClose(e.getActionCommand());
            case BUTTON_INVOICE_SAVE -> saveInvoiceChanges();
            case BUTTON_INVOICE_CANCEL -> cancelInvoiceChanges();

        }
    }

    public void invoiceRowChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        currentSelectedInvoice = ((DefaultListSelectionModel) e.getSource()).getAnchorSelectionIndex();
        if (currentSelectedInvoice >= 0) {
            System.out.println("Invoice selection changed. Index: " + currentSelectedInvoice);
            DisplayedFrame.getInstance().updateLinesTable(InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft());
        }
    }

    public void lineRowChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        currentSelectedItem = ((DefaultListSelectionModel) e.getSource()).getAnchorSelectionIndex();
        if (currentSelectedItem >= 0 && currentSelectedInvoice >= 0) System.out.println("Invoice line selection changed. Index: " + currentSelectedItem);
    }

    private void fileOpener(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Files ending with .csv", "csv"));
        fileChooser.setDialogTitle("Select Invoices file...");
        int result = fileChooser.showOpenDialog(DisplayedFrame.getInstance());
        if (result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getPath();
            if (!path.toLowerCase().endsWith(".csv")) path = path + ".csv";
            FileOperations invoices_file = new FileOperations(path);
            fileChooser.setDialogTitle("Select Lines file...");
            result = fileChooser.showOpenDialog(DisplayedFrame.getInstance());
            if (result == JFileChooser.APPROVE_OPTION){
                path = fileChooser.getSelectedFile().getPath();
                if (!path.toLowerCase().endsWith(".csv")) path = path + ".csv";
                FileOperations lines_file = new FileOperations(path);
                if (InvoiceHeader.countInvoices() != 0 && JOptionPane.showConfirmDialog(DisplayedFrame.getInstance(),
                        "Existing invoices will be removed. All unsaved work will be lost. Are you sure?", "Import confirmation", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION){
                    return;
                }
                String[][] invoices = invoices_file.readFile();
                String[][] lines = lines_file.readFile();
                InvoiceHeader.reconstructInvoices(invoices, lines);
                System.out.println("Invoices headers and lines files are opened and their content are currently being displayed.");
                DisplayedFrame.getInstance().updateInvoicesTable();
            }
        }
    }

    private void fileSaver(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Files ending with .csv", "csv"));
        fileChooser.setDialogTitle("Select Invoices file...");
        fileChooser.setSelectedFile(new File("com.photon/InvoiceHeader.csv"));
        int result = fileChooser.showSaveDialog(DisplayedFrame.getInstance());
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getPath();
            if (!path.toLowerCase().endsWith(".csv")) path = path + ".csv";
            FileOperations invoices_file = new FileOperations(path);
            invoices_file.writeFile(InvoiceHeader.disassembleInvoices());
            fileChooser.setDialogTitle("Select Lines file...");
            fileChooser.setSelectedFile(new File("com.photon/InvoiceLine.csv"));
            result = fileChooser.showSaveDialog(DisplayedFrame.getInstance());
            if (result == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile().getPath();
                if (!path.toLowerCase().endsWith(".csv")) path = path + ".csv";
                FileOperations lines_file = new FileOperations(path);
                lines_file.writeFile(InvoiceHeader.disassembleLines());
                System.out.println("Invoices headers and lines files have been saved in csv files.");
            }
        }
    }

    private void createNewInvoice(){
        InvoiceHeader invoice = new InvoiceHeader();
        InvoiceHeader.addInvoice(invoice);
        System.out.println("New invoice header created. Invoice number: " + invoice.getInvoiceNumber());
        DisplayedFrame.getInstance().updateInvoicesTable();
        DisplayedFrame.getInstance().selectInvoicesRow(InvoiceHeader.countInvoices() - 1);
    }

    private void deleteInvoice(){
        if (currentSelectedInvoice == -1) return;
        InvoiceHeader invoice = InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice);
        if (JOptionPane.showConfirmDialog(
                DisplayedFrame.getInstance(), "Invoice number" + invoice.getInvoiceNumber()
                        + " will be removed from list with all invoice lines assigned." + System.lineSeparator()
                        + "Date: " + invoice.getFormattedDate() + System.lineSeparator() + "Customer: " + invoice.getCustomer() + System.lineSeparator()
                        + "THIS CANNOT BE UNDONE. ARE YOU SURE??",
                "Invoice " + invoice.getInvoiceNumber() + " deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            InvoiceHeader.removeInvoice(currentSelectedInvoice);
            System.out.println("Invoice removed. Invoice number: " + invoice.getInvoiceNumber());
            DisplayedFrame.getInstance().updateInvoicesTable();
            DisplayedFrame.getInstance().updateLinesTable(null);
            DisplayedFrame.getInstance().selectInvoicesRow(-1);
        }
    }

    private void updateInvoiceDate(){
        int invoiceIndex = currentSelectedInvoice;
        if (invoiceIndex == -1) return;
        InvoiceHeaderDraft invoice = InvoiceHeader.getInvoiceByIndex(invoiceIndex).getDraft();
        try {
            invoice.setDate(InvoiceHeader.dateFormat.parse(DisplayedFrame.getInstance().getDateTextField()));
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(DisplayedFrame.getInstance(),"Please enter the date in this format: yyyy-MM-dd",
                    "Bad date entered", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DisplayedFrame.getInstance().selectInvoicesRow(invoiceIndex);
    }

    private void updateInvoiceCustomer(){
        int invoiceIndex = currentSelectedInvoice;
        if (invoiceIndex == -1) return;
        InvoiceHeaderDraft invoice = InvoiceHeader.getInvoiceByIndex(invoiceIndex).getDraft();
        invoice.setCustomer(DisplayedFrame.getInstance().getCustomerTextField());
        DisplayedFrame.getInstance().selectInvoicesRow(invoiceIndex);
    }

    private void addInvoiceItem(){
        if (currentSelectedInvoice == -1) return;
        itemFrameInstance = new InvoiceLineDialog(this, InvoiceLineDialog.ADD_FRAME, InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft());
        itemFrameInstance.setVisible(true);
    }

    private void updateInvoiceItem(){
        if (currentSelectedInvoice == -1) return;
        itemFrameInstance = new InvoiceLineDialog(this, InvoiceLineDialog.UPDATE_FRAME, InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft());
        itemFrameInstance.setVisible(true);
    }

    private void deleteInvoiceItem(){
        if (currentSelectedInvoice == -1 || currentSelectedItem == -1) return;
        InvoiceHeaderDraft invoice = InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft();
        if (JOptionPane.showConfirmDialog(DisplayedFrame.getInstance(),
                "Please confirm the deletion of " + invoice.getInvoiceLine(currentSelectedItem).toString() + System.lineSeparator() + "You may undone this later", "Delete invoice line", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            System.out.println("Draft invoice number: " + invoice.getInvoiceNumber() + " . item deleted: " + invoice.getInvoiceLine(currentSelectedItem));
            invoice.deleteInvoiceLine(currentSelectedItem);
            DisplayedFrame.getInstance().updateLinesTable(invoice);
        }
    }

    private void itemFrameClose(String action){
        if (action.equals(ITEM_FRAME_OKAY)){
            this.itemFrameInstance.setVisible(false);
            String[] data= this.itemFrameInstance.getData();
            InvoiceHeaderDraft invoice = InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft();
            if (this.itemFrameInstance.getFrameType() == InvoiceLineDialog.UPDATE_FRAME){
                InvoiceLine line = invoice.getInvoiceLine(Integer.parseInt(data[0]) - 1);
                line.setDescription(data[1].isEmpty() ? line.getDescription() : data[1]);
                line.setPrice(data[2].isEmpty() ? line.getPrice() : Double.parseDouble(data[2]));
                line.setCount(data[3].isEmpty() ? line.getCount() : Integer.parseInt(data[3]));
                System.out.println("Draft invoice number: " + invoice.getInvoiceNumber() + " . item updated: " + invoice.getInvoiceLine(currentSelectedItem));
            }else{
                data[1] = data[1].isEmpty() ? "Item " + data[0] : data[1];
                data[2] = data[2].isEmpty() ? "0" : data[2];
                data[3] = data[3].isEmpty() ? "0" : data[3];
                InvoiceLine line = new InvoiceLine(data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]));
                invoice.addInvoiceLine(line);
                System.out.println("Draft invoice number: " + invoice.getInvoiceNumber() + " . item added: " + line);
            }
            DisplayedFrame.getInstance().updateLinesTable(invoice);
        }

        this.itemFrameInstance.dispose();
        this.itemFrameInstance = null;
    }

    private void saveInvoiceChanges(){
        int invoiceIndex = currentSelectedInvoice;
        if (currentSelectedInvoice == -1) return;
        InvoiceHeader invoice = InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice);
        if (JOptionPane.showConfirmDialog(DisplayedFrame.getInstance(),
                "All changes made to invoice " + invoice.getInvoiceNumber() + " will be saved." + System.lineSeparator() + "This cannot be undone!", "Delete invoice line", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
            if (!DisplayedFrame.getInstance().getDateTextField().isEmpty()) updateInvoiceDate();
            invoice.applyDraft();
            System.out.println("Invoice changes have been updated. Invoice number: " + invoice.getInvoiceNumber());
            DisplayedFrame.getInstance().updateInvoicesTable();
            DisplayedFrame.getInstance().selectInvoicesRow(invoiceIndex);
        }

    }

    private void cancelInvoiceChanges(){
        if (currentSelectedInvoice == -1) return;
        InvoiceHeader invoice = InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice);
        invoice.rebuildDraft();
        System.out.println("Invoice changes are cancelled. Invoice number: " + invoice.getInvoiceNumber());
        DisplayedFrame.getInstance().updateLinesTable(InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft());
    }

    public int getSelectedInvoice() {
        return currentSelectedInvoice;
    }

    public int getSelectedItem() {
        return currentSelectedItem;
    }

    @Override
    public void mousePressed(MouseEvent e){
        if (e.getClickCount() == 2)
            updateInvoiceItem();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
