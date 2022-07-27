package com.photon.view;

import com.photon.controller.Controller;
import com.photon.model.InvoiceHeaderDraft;
import com.photon.model.InvoiceLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InvoiceLineDialog extends JDialog {
    public static final int ADD_FRAME = 0;
    public static final int UPDATE_FRAME = 1;

    private int frameType;

    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;

    private String[] data = new String[4];

    public InvoiceLineDialog(Controller listener, int frameType, InvoiceHeaderDraft invoice){
        super(DisplayedFrame.getInstance(), frameType == 1 ? "Update existing item" : "Add new item", true);
        if (frameType == UPDATE_FRAME){
            InvoiceLine item = invoice.getInvoiceLine(listener.getSelectedItem());
            data = new String[]{String.valueOf(listener.getSelectedItem() + 1), item.getDescription(),
                    String.valueOf(item.getPrice()), String.valueOf(item.getCount())};
        }else{
            data[0] = String.valueOf(invoice.getNumberOfLines() + 1);
        }

        this.frameType = frameType;
        this.init();
        this.setSize(500, 500);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                listener.actionPerformed(new ActionEvent(e.getWindow(), ActionEvent.ACTION_PERFORMED, Controller.ITEM_FRAME_CANCEL));
            }
        });

        setPreferredSize(new Dimension(500, 500));
        getContentPane().setLayout(new GridLayout(5, 1));

        jLabel1.setText("Item no.");

        jLabel6.setText(data[0]);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(270, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(jLabel6))
        );

        getContentPane().add(jPanel1);

        jLabel2.setText("Description");
        jTextField1.setText(data[1]);

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(120, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel2);

        jLabel3.setText("Price per unit");
        jTextField2.setText(data[2]);
        jTextField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Character.isLetter(e.getKeyChar())){
                    e.consume();
                }
                try {
                    Double.parseDouble(jTextField2.getText()+ e.getKeyChar());
                }catch (NumberFormatException ex){
                    e.consume();
                }
            }
        });

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(270, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel3);

        jLabel4.setText("Count");

        jTextField3.setText(data[3]);
        jTextField3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Character.isLetter(e.getKeyChar())){
                    e.consume();
                }
                try {
                    Integer.parseInt(jTextField3.getText()+ e.getKeyChar());
                }catch (NumberFormatException ex){
                    e.consume();
                }
            }
        });

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(270, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel4);

        /*jLabel5.setText("Total");

        jLabel7.setText("");

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(170, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(jLabel7))
        );

        getContentPane().add(jPanel5);*/

        jButton1.setText(frameType == UPDATE_FRAME ? "Update" : "Add");
        jButton1.addActionListener(listener);
        jButton1.setActionCommand(Controller.ITEM_FRAME_OKAY);

        jButton2.setText("Cancel");
        jButton2.addActionListener(listener);
        jButton2.setActionCommand(Controller.ITEM_FRAME_CANCEL);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(140, 140, 140)
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                                .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addGap(140, 140, 140))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
                                .addContainerGap())
        );

        getContentPane().add(jPanel6);

        this.setLocationRelativeTo(null);

    }

    public String[] getData(){
        this.updateData();
        return data;
    }

    public void updateData(){
        data[1] = jTextField1.getText();
        data[2] = jTextField2.getText();
        data[3] = jTextField3.getText();
    }

    public void init(){
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel6 = new JLabel();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        jTextField1 = new JTextField();
        jPanel3 = new JPanel();
        jLabel3 = new JLabel();
        jTextField2 = new JTextField();
        jPanel4 = new JPanel();
        jLabel4 = new JLabel();
        jTextField3 = new JTextField();
        jPanel5 = new JPanel();
        jLabel5 = new JLabel();
        jLabel7 = new JLabel();
        jPanel6 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();
    }

    public int getFrameType() {
        return frameType;
    }
}
