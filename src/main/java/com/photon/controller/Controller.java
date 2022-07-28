package com.photon.controller;

import com.photon.InvalidFileTypeException;
import com.photon.model.InvoiceHeader;
import com.photon.model.DraftInvoiceHeader;
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
    /**
     * Protocol codes that are used as commands. That eases communications between displayed frame and its listeners
     */
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

    /** Current selected invoices */
    private int currentSelectedInvoice = -1;
    /** Current selected invoice line */
    private int currentSelectedItem = -1;
    /** Instance of current displayed frame for adding/update */
    private InvoiceLineDialog LineDialog = null;

    /**
     * Called when registered actions happens.
     */
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

    /**
     * Called when user selects invoices.
     */
    public void invoiceRowChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        currentSelectedInvoice = ((DefaultListSelectionModel) e.getSource()).getAnchorSelectionIndex();
        if (currentSelectedInvoice >= 0) {
            System.out.println("Invoice selection changed. Index: " + currentSelectedInvoice);
            DisplayedFrame.getInstance().updateLinesTable(InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft());
        }
    }

    /**
     * Called when user selects invoice line.
     */
    public void lineRowChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        currentSelectedItem = ((DefaultListSelectionModel) e.getSource()).getAnchorSelectionIndex();
        if (currentSelectedItem >= 0 && currentSelectedInvoice >= 0) System.out.println("Invoice line selection changed. Index: " + currentSelectedItem);
    }

    /**
     * Opens JFileChooser for user to choose file and gets all it's content later.
     */
    private void fileOpener(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Files ending with .csv", "csv"));
        fileChooser.setDialogTitle("Select Invoices file...");
        int result = fileChooser.showOpenDialog(DisplayedFrame.getInstance());
        if (result == JFileChooser.APPROVE_OPTION){
            String invoice_path = fileChooser.getSelectedFile().getPath();
            if (!invoice_path.toLowerCase().endsWith(".csv")) invoice_path = invoice_path + ".csv";
            fileChooser.setDialogTitle("Select Lines file...");
            result = fileChooser.showOpenDialog(DisplayedFrame.getInstance());
            if (result == JFileChooser.APPROVE_OPTION){
                String lines_path = fileChooser.getSelectedFile().getPath();
                if (!lines_path.toLowerCase().endsWith(".csv")) lines_path = lines_path + ".csv";
                try {
                    FileOperations invoices_file = new FileOperations(invoice_path);
                    FileOperations lines_file = new FileOperations(lines_path);
                    if (InvoiceHeader.countInvoices() != 0 && JOptionPane.showConfirmDialog(DisplayedFrame.getInstance(),
                            "Existing invoices will be removed. All unsaved work will be lost. Are you sure?", "Import confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                        return;
                    }
                    String[][] invoices = invoices_file.readFile();
                    String[][] lines = lines_file.readFile();
                    InvoiceHeader.reconstructInvoices(invoices, lines);
                    System.out.println("Invoices headers and lines files are opened and their content are currently being displayed.");
                    DisplayedFrame.getInstance().updateInvoicesTable();
                }catch (InvalidFileTypeException e) {
                    JOptionPane.showMessageDialog(DisplayedFrame.getInstance(), e.getMessage(), "Load operation failed.", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Opens JFileChooser for user to choose file to save content in.
     */
    private void fileSaver(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Files ending with .csv", "csv"));
        fileChooser.setDialogTitle("Select Invoices file...");
        fileChooser.setSelectedFile(new File("InvoiceHeader.csv"));
        int result = fileChooser.showSaveDialog(DisplayedFrame.getInstance());
        if (result == JFileChooser.APPROVE_OPTION) {
            String invoice_path = fileChooser.getSelectedFile().getPath();
            if (!invoice_path.toLowerCase().endsWith(".csv")) invoice_path = invoice_path + ".csv";
            fileChooser.setDialogTitle("Select Lines file...");
            fileChooser.setSelectedFile(new File("InvoiceLine.csv"));
            result = fileChooser.showSaveDialog(DisplayedFrame.getInstance());
            if (result == JFileChooser.APPROVE_OPTION) {
                String lines_path = fileChooser.getSelectedFile().getPath();
                if (!lines_path.toLowerCase().endsWith(".csv")) lines_path = lines_path + ".csv";
                try {
                    FileOperations invoices_file = new FileOperations(invoice_path);
                    FileOperations lines_file = new FileOperations(lines_path);
                    if ((invoices_file.isFile() || lines_file.isFile()) && JOptionPane.showConfirmDialog(DisplayedFrame.getInstance(),
                            "Files with same names already exist." + System.lineSeparator() +  "Do you want to replace them?", "Export confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                        return;
                    }
                    invoices_file.writeFile(InvoiceHeader.disassembleInvoices());
                    lines_file.writeFile(InvoiceHeader.disassembleLines());
                    System.out.println("Invoices headers and lines files have been saved in csv files.");
                    JOptionPane.showMessageDialog(DisplayedFrame.getInstance(), "Files saved.", null, JOptionPane.INFORMATION_MESSAGE);
                } catch (InvalidFileTypeException e) {
                    JOptionPane.showMessageDialog(DisplayedFrame.getInstance(), e.getMessage(), "Save operation failed", JOptionPane.ERROR_MESSAGE);
                }

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
        DraftInvoiceHeader invoice = InvoiceHeader.getInvoiceByIndex(invoiceIndex).getDraft();
        try{
            invoice.setDate(InvoiceHeader.dateFormat.parse(DisplayedFrame.getInstance().getDateTextField()));
        }catch (ParseException e) {
            JOptionPane.showMessageDialog(DisplayedFrame.getInstance(),"Please enter the date in this format: yyyy-MM-dd",
                    "Bad date entered", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DisplayedFrame.getInstance().selectInvoicesRow(invoiceIndex);
    }

    private void updateInvoiceCustomer(){
        int invoiceIndex = currentSelectedInvoice;
        if (invoiceIndex == -1) return;
        DraftInvoiceHeader invoice = InvoiceHeader.getInvoiceByIndex(invoiceIndex).getDraft();
        invoice.setCustomer(DisplayedFrame.getInstance().getCustomerTextField());
        DisplayedFrame.getInstance().selectInvoicesRow(invoiceIndex);
    }

    private void addInvoiceItem(){
        if (currentSelectedInvoice == -1) return;
        LineDialog = new InvoiceLineDialog(this, InvoiceLineDialog.ADD_FRAME, InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft());
        LineDialog.setVisible(true);
    }

    private void updateInvoiceItem(){
        if (currentSelectedInvoice == -1) return;
        LineDialog = new InvoiceLineDialog(this, InvoiceLineDialog.UPDATE_FRAME, InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft());
        LineDialog.setVisible(true);
    }

    private void deleteInvoiceItem(){
        if (currentSelectedInvoice == -1 || currentSelectedItem == -1) return;
        DraftInvoiceHeader invoice = InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft();
        if (JOptionPane.showConfirmDialog(DisplayedFrame.getInstance(),
                "Please confirm the deletion of " + invoice.getInvoiceLine(currentSelectedItem).toString() + System.lineSeparator() + "You may undone this later", "Delete invoice line", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            System.out.println("Draft invoice number: " + invoice.getInvoiceNumber() + " . item deleted: " + invoice.getInvoiceLine(currentSelectedItem));
            invoice.deleteInvoiceLine(currentSelectedItem);
            DisplayedFrame.getInstance().updateLinesTable(invoice);
        }
    }

    /**
     * Called when user finishes working on Invoice Line dialog. Adds new or updates existing invoice line accordingly
     */
    private void itemFrameClose(String action){
        if (action.equals(ITEM_FRAME_OKAY)){
            this.LineDialog.setVisible(false);
            String[] data= this.LineDialog.getData();
            DraftInvoiceHeader invoice = InvoiceHeader.getInvoiceByIndex(currentSelectedInvoice).getDraft();
            if (this.LineDialog.getFrameType() == InvoiceLineDialog.UPDATE_FRAME){
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

        this.LineDialog.dispose();
        this.LineDialog = null;
    }

    /**
     * Saves changes made in draft to the invoice
     */
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

    /**
     * Cancels all changes made by discarding all changes made to draft invoice
     */
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
