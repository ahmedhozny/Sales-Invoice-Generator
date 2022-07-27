package com.photon.view;

import com.photon.controller.Controller;
import com.photon.controller.FileOperations;
import com.photon.model.InvoiceHeader;
import com.photon.model.InvoiceHeaderDraft;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.File;
import java.net.URISyntaxException;
import java.text.ParseException;

public class DisplayedFrame extends JFrame{

    private static DisplayedFrame instance;
    private Controller listener;

    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JButton jButton6;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JMenu jMenu1;
    private JMenuBar jMenuBar1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTable jTable1;
    private JTable jTable2;
    private JTextField jTextField1;
    private JTextField jTextField2;

    public static DisplayedFrame getInstance(){
        return instance;
    }

    public String getDateTextField(){
        return jTextField1.getText();
    }

    public String getCustomerTextField(){
        return jTextField2.getText();
    }

    public void updateInvoicesTable(){
        jTable1.setModel(new InvoicesTableModel());
    }

    public void selectInvoicesRow(int row){
        jTable1.changeSelection(row,0,false, false);
    }

    public void updateLinesTable(InvoiceHeaderDraft invoice){
        if (invoice == null){
            jLabel3.setText("");
            jTextField1.setText("");
            jTextField2.setText("");
            jLabel8.setText("");
            jTable2.setModel(new InvoiceLinesTableModel(null));
            return;
        }
        jLabel3.setText(String.valueOf(invoice.getInvoiceNumber()));
        jTextField1.setText(invoice.getFormattedDate());
        jTextField2.setText(invoice.getCustomer());
        jLabel8.setText(String.valueOf(invoice.getTotalPrice()));
        jTable2.setModel(new InvoiceLinesTableModel(invoice));
    }

    public DisplayedFrame() {
        instance = this;
        this.init();
        this.setTitle("My Hypermarket");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 2, 20, 0));


        jTable1.setModel(new DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "No.", "Date", "Customer", "Total"
                }
        ) {
            final Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            final boolean[] canEdit = new boolean [] {
                    false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getSelectionModel().addListSelectionListener(e -> this.listener.invoiceRowChanged(e));
        jTable1.setName("invoices");
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Create New Invoice");
        jButton1.setActionCommand(Controller.BUTTON_NEW_INVOICE);
        jButton1.addActionListener(listener);
        jButton2.setText("Delete Invoice");
        jButton2.setActionCommand(Controller.BUTTON_DELETE_INVOICE);
        jButton2.addActionListener(listener);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(125, 125, 125)
                                                .addComponent(jButton1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton2))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 452, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 561, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
                                .addContainerGap())
        );

        getContentPane().add(jPanel1);

        jTextField1.addActionListener(this.listener);

        jTextField1.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                JTextField textField= (JTextField) e.getComponent();
                if (!textField.getText().isEmpty()) {
                    try {
                        InvoiceHeader.dateFormat.parse(textField.getText());
                        for (ActionListener listener : textField.getActionListeners()) {
                            listener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, Controller.TEXT_FIELD_DATE_UPDATE));
                        }
                    } catch (ParseException ex) {
                        e.getComponent().requestFocusInWindow();
                    }
                }
            }
        });

        jTextField1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                JTextField textField= (JTextField) e.getComponent();
                for (FocusListener listener:textField.getFocusListeners()) {
                    listener.focusLost(new FocusEvent(e.getComponent(), FocusEvent.FOCUS_LOST));
                }
            }
        });

        jTextField2.addActionListener(this.listener);

        jTextField2.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                JTextField textField= (JTextField) e.getComponent();
                for (ActionListener listener:textField.getActionListeners()){
                    listener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, Controller.TEXT_FIELD_CUSTOMER_UPDATE));
                }
            }
        });

        jTextField2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                JTextField textField= (JTextField) e.getComponent();
                for (FocusListener listener:textField.getFocusListeners()) {
                    listener.focusLost(new FocusEvent(e.getComponent(), FocusEvent.FOCUS_LOST));
                }
            }
        });

        jLabel6.setText("Invoice Items:");

        jLabel7.setText("Invoice Total:");

        jLabel8.setText(null);

        jTable2.setModel(new DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "No.", "Item Name", "Item Price", "Count", "Item Total"
                }
        ) {
            final Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
            };
            final boolean[] canEdit = new boolean [] {
                    false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.getSelectionModel().addListSelectionListener(e -> this.listener.lineRowChanged(e));
        jTable2.addMouseListener(listener);
        jScrollPane2.setViewportView(jTable2);

        jLabel2.setText("Invoice Number:");

        jLabel3.setText(null);

        jLabel4.setText("Invoice Date:");

        jLabel5.setText("Customer Name:");

        jButton3.setText("Save");
        jButton3.addActionListener(this.listener);
        jButton3.setActionCommand(Controller.BUTTON_INVOICE_SAVE);
        jButton4.setText("Cancel");
        jButton4.addActionListener(this.listener);
        jButton4.setActionCommand(Controller.BUTTON_INVOICE_CANCEL);

        jButton5.setText("+");
        jButton5.addActionListener(this.listener);
        jButton5.setActionCommand(Controller.BUTTON_ADD_ITEM);
        jButton6.setText("-");
        jButton6.addActionListener(this.listener);
        jButton6.setActionCommand(Controller.BUTTON_DELETE_ITEM);

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel6)
                                                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel2)
                                                                .addComponent(jLabel4)
                                                                .addComponent(jLabel5)
                                                                .addComponent(jLabel7)))
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(49, 49, 49)
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel8)
                                                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addComponent(jButton3)
                                                                                .addGap(38, 38, 38)
                                                                                .addComponent(jButton4))
                                                                        .addComponent(jLabel3)))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jButton5)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jButton6))))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(16, 16, 16)
                                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jButton5)
                                        .addComponent(jButton6))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton3)
                                        .addComponent(jButton4))
                                .addContainerGap())
        );

        getContentPane().add(jPanel2);

        jMenu1.setText("File");

        jMenuItem1.setText("Load File");
        jMenuItem1.addActionListener(listener);
        jMenuItem1.setActionCommand(Controller.FILE_LOAD);
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Save File");
        jMenuItem2.addActionListener(listener);
        jMenuItem2.setActionCommand(Controller.FILE_SAVE);
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();

        this.add(jPanel2);
        this.setExtendedState(MAXIMIZED_BOTH);

        try {
            FileOperations invoices = new FileOperations(getClass().getClassLoader().getResource("com.photon/InvoiceHeader.csv").toURI().getPath());
            FileOperations lines = new FileOperations(getClass().getClassLoader().getResource("com.photon/InvoiceLine.csv").toURI().getPath());
            InvoiceHeader.reconstructInvoices(invoices.readFile(), lines.readFile());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        this.updateInvoicesTable();
    }

    private void init(){
        listener = new Controller();

        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jPanel2 = new JPanel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jTextField1 = new JTextField();
        jLabel5 = new JLabel();
        jTextField2 = new JTextField();
        jButton3 = new JButton();
        jButton4 = new JButton();
        jButton5 = new JButton();
        jButton6 = new JButton();
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jMenuItem1 = new JMenuItem();
        jMenuItem2 = new JMenuItem();
    }
}