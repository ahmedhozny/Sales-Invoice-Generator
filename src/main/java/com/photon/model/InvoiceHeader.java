package com.photon.model;

import com.photon.Main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Creates invoice headers objects.
 */
public class InvoiceHeader{

    /** Holds all invoices */
    private static final ArrayList<InvoiceHeader> INVOICE_HEADERS = new ArrayList<>();

    private static int nextInvoiceNumber = 1;

    /** Used to format data as requested ("dd/MM/yyyy") */
    public static SimpleDateFormat dateFormat;

    /** Data of each InvoiceHeaders Instance*/
    protected int invoiceNum;
    protected Date invoiceDate;
    protected String customerName;
    protected ArrayList<InvoiceLine> lines;

    /**
     * Holds another copy of InvoiceHeader instance.
     * Any changes made to an invoice should be set at DraftInvoiceHeader first before finally getting applied here
     */
    private DraftInvoiceHeader draftInvoiceHeader;

    //this.headers = lines[0].split(delimiter);
    //lines = Arrays.copyOfRange(lines, 1, lines.length);
    /** Reconstructs all invoices. Should be called when raw data is available*/
    public static void reconstructInvoices(String[][] invoices, String[][] lines){
        INVOICE_HEADERS.clear();
        for (String[] strings : invoices) {
            if (Objects.equals(strings[0], "")) continue;
            String[] row = new String[4];
            System.arraycopy(strings, 0, row, 0, strings.length);
            InvoiceHeader.addInvoice(new InvoiceHeader(Integer.parseInt(row[0]), row[1], row[2]));
        }

        //INVOICE_HEADERS.sort((o1, o2) -> {
        //    if (o1.getDate() == null) return 1;
        //    if (o2.getDate() == null) return -1;
        //    return o1.getDate().compareTo(o2.getDate());
        //});
        INVOICE_HEADERS.sort(Comparator.comparingInt(InvoiceHeader::getInvoiceNumber));

        for (String[] line: lines) {
            InvoiceHeader invoice = InvoiceHeader.getInvoiceByNumber(Integer.parseInt(line[0]));
            if (invoice != null) invoice.addInvoiceLine(new InvoiceLine(line[1], Double.parseDouble(line[2]), Integer.parseInt(line[3])));
        }

        nextInvoiceNumber = InvoiceHeader.INVOICE_HEADERS.get(InvoiceHeader.INVOICE_HEADERS.size() -1).getInvoiceNumber() + 1;

        for (InvoiceHeader invoiceHeader : INVOICE_HEADERS) invoiceHeader.rebuildDraft();

        // As requested. Calls test method to show loaded invoices
        Main.test();

    }

    /**
     * Returns InvoiceHeaders to its raw state.
     */
    public static String[][] disassembleInvoices(){
        String[][] content = new String[INVOICE_HEADERS.size()][4];
        for (int i = 0; i < INVOICE_HEADERS.size(); i++){
            InvoiceHeader invoiceHeader = INVOICE_HEADERS.get(i);
            String date = invoiceHeader.getFormattedDate() == null ? "" : invoiceHeader.getFormattedDate();
            String customer = invoiceHeader.getCustomer() == null ? "" : invoiceHeader.getCustomer();
            content[i] = new String[]{String.valueOf(invoiceHeader.getInvoiceNumber()), date, customer};
        }

        return content;
    }

    /**
     * Returns InvoiceLines to its raw state.
     */
    public static String[][] disassembleLines(){
        ArrayList<String[]> content = new ArrayList<>();
        for (InvoiceHeader invoiceHeader : INVOICE_HEADERS) {
            for (int j = 0; j < invoiceHeader.lines.size(); j++) {
                InvoiceLine item = invoiceHeader.lines.get(j);
                content.add(new String[]{String.valueOf(invoiceHeader.getInvoiceNumber()), item.getDescription(), String.valueOf(item.getPrice()),
                        String.valueOf(item.getCount())});
            }
        }

        return content.toArray(new String[][]{});
    }

    public static void addInvoice(InvoiceHeader invoiceHeader){
        INVOICE_HEADERS.add(invoiceHeader);
    }

    public static void removeInvoice(int invoiceIndex){
        INVOICE_HEADERS.remove(invoiceIndex);
        if (INVOICE_HEADERS.isEmpty()) nextInvoiceNumber = 1;
        else nextInvoiceNumber = INVOICE_HEADERS.get(INVOICE_HEADERS.size() - 1).getInvoiceNumber() + 1;
    }

    public static int countInvoices(){
        return INVOICE_HEADERS.size();
    }

    public static InvoiceHeader getInvoiceByIndex(int index){
        return INVOICE_HEADERS.get(index);
    }

    public static InvoiceHeader getInvoiceByNumber(int invoiceNumber){
        for (InvoiceHeader i: INVOICE_HEADERS) {
            if (i.invoiceNum == invoiceNumber) return i;
        }
        return null;
    }

    /**
     * Called when new invoices is created for the first time.
     */
    public InvoiceHeader(){
        this.invoiceNum = InvoiceHeader.nextInvoiceNumber++;
        lines = new ArrayList<>();
        rebuildDraft();
    }

    /**
     * Copies another InvoiceHeader instance to this.
     */
    public InvoiceHeader(InvoiceHeader invoiceHeader){
        this.invoiceNum = invoiceHeader.invoiceNum;
        this.invoiceDate = invoiceHeader.invoiceDate;
        this.customerName = invoiceHeader.customerName;
        this.lines = new ArrayList<>(invoiceHeader.lines);
    }

    public InvoiceHeader(String date, String customer){
        this(InvoiceHeader.nextInvoiceNumber++, date, customer);
    }

    /**
     * Constructs invoices that have their data available from existing raw data
     */
    public InvoiceHeader(int invoiceNumber, String date, String customer){
        this.invoiceNum = invoiceNumber;
        if (date != null) {
            try {
                this.invoiceDate = dateFormat.parse(date);
            } catch (ParseException e) {
                this.invoiceDate = null;
            }
        }

        this.customerName = customer;
        lines = new ArrayList<>();
        //rebuildDraft();
    }

    /**
     * Applies draft copy to this invoice
     */
    public void applyDraft(){
        this.invoiceNum = draftInvoiceHeader.invoiceNum;
        this.invoiceDate = draftInvoiceHeader.invoiceDate;
        this.customerName = draftInvoiceHeader.customerName;
        this.lines = draftInvoiceHeader.lines;
        rebuildDraft();
    }

    /**
     * Creates a draft version of this invoice. Should be called if changes are discarded.
     */
    public void rebuildDraft(){
        this.draftInvoiceHeader = new DraftInvoiceHeader(this);
    }

    /**
     * Searches for invoice line using its index. Returns InvoiceLine if found, NULL if not.
     */
    public InvoiceLine getInvoiceLine(int index){
        if (index < lines.size()) return lines.get(index);
        return null;
    }

    public int getNumberOfLines(){
        return this.lines.size();
    }

    /**
     * Calculates total price of invoice
     */
    public double getTotalPrice() {
        double total = 0;
        for (InvoiceLine item : lines) {
            total += item.totalPrice();
        }

        return Math.round(total * 100) / 100.0;
    }

    public int getInvoiceNumber() {
        return invoiceNum;
    }

    /**
     * Returns a formatted version of InvoiceDate.
     */
    public String getFormattedDate(){
        if (this.invoiceDate == null) return null;
            return dateFormat.format(this.invoiceDate);
    }

    public Date getDate() {
        return invoiceDate;
    }

    protected void addInvoiceLine(InvoiceLine line){
        this.lines.add(line);
    }

    protected void deleteInvoiceLine(int index){
        this.lines.remove(index);
    }

    public String getCustomer() {
        return customerName;
    }

    public DraftInvoiceHeader getDraft() {
        return draftInvoiceHeader;
    }

    @Override
    public String toString() {
        return "InvoiceHeader{" +
                "invoiceNum=" + invoiceNum +
                ", invoiceDate=" + invoiceDate +
                ", customerName='" + customerName + '\'' +
                ", lines=" + lines +
                '}';
    }
}
