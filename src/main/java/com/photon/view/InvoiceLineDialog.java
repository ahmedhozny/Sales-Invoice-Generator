package com.photon.view;

import com.photon.controller.Controller;
import com.photon.model.DraftInvoiceHeader;
import com.photon.model.InvoiceLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Create a dialog to add new or edit existing invoice lines.
 */
public class InvoiceLineDialog extends JDialog {
    public static final int ADD_FRAME = 0;
    public static final int UPDATE_FRAME = 1;

    private final int frameType;

    /** Declaration of needed containers */
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JLabel itemNumberKeyLabel;
    private JLabel descriptionLabel;
    private JLabel priceLabel;
    private JLabel countLabel;
    private JLabel invoiceNumberValueLabel;
    private JPanel lineNumberPanel;
    private JPanel descriptionPanel;
    private JPanel pricePanel;
    private JPanel countPanel;
    private JPanel buttonsPanel;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField countField;

    /** Holds raw data of InvoiceLine*/
    private String[] data = new String[4];

    public InvoiceLineDialog(Controller listener, int frameType, DraftInvoiceHeader invoice){
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

        itemNumberKeyLabel.setText("Item no.");

        invoiceNumberValueLabel.setText(data[0]);

        GroupLayout lineNumberPanelLayout = new GroupLayout(lineNumberPanel);
        lineNumberPanel.setLayout(lineNumberPanelLayout);
        lineNumberPanelLayout.setHorizontalGroup(
                lineNumberPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(lineNumberPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(itemNumberKeyLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(invoiceNumberValueLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(270, Short.MAX_VALUE))
        );
        lineNumberPanelLayout.setVerticalGroup(
                lineNumberPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(lineNumberPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(itemNumberKeyLabel, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(invoiceNumberValueLabel))
        );

        getContentPane().add(lineNumberPanel);

        descriptionLabel.setText("Description");
        descriptionField.setText(data[1]);

        GroupLayout descriptionPanelLayout = new GroupLayout(descriptionPanel);
        descriptionPanel.setLayout(descriptionPanelLayout);
        descriptionPanelLayout.setHorizontalGroup(
                descriptionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(descriptionPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(descriptionLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(descriptionField, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(120, Short.MAX_VALUE))
        );
        descriptionPanelLayout.setVerticalGroup(
                descriptionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(descriptionPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(descriptionLabel, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(descriptionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(descriptionPanel);

        priceLabel.setText("Price per unit");
        priceField.setText(data[2]);
        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Character.isLetter(e.getKeyChar())){
                    e.consume();
                }
                try {
                    Double.parseDouble(priceField.getText()+ e.getKeyChar());
                }catch (NumberFormatException ex){
                    e.consume();
                }
            }
        });

        GroupLayout pricePanelLayout = new GroupLayout(pricePanel);
        pricePanel.setLayout(pricePanelLayout);
        pricePanelLayout.setHorizontalGroup(
                pricePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pricePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(priceLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(priceField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(270, Short.MAX_VALUE))
        );
        pricePanelLayout.setVerticalGroup(
                pricePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pricePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(priceLabel, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(priceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(pricePanel);

        countLabel.setText("Count");

        countField.setText(data[3]);
        countField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Character.isLetter(e.getKeyChar())){
                    e.consume();
                }
                try {
                    Integer.parseInt(countField.getText()+ e.getKeyChar());
                }catch (NumberFormatException ex){
                    e.consume();
                }
            }
        });

        GroupLayout countPanelLayout = new GroupLayout(countPanel);
        countPanel.setLayout(countPanelLayout);
        countPanelLayout.setHorizontalGroup(
                countPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(countPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(countLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(countField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(270, Short.MAX_VALUE))
        );
        countPanelLayout.setVerticalGroup(
                countPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(countPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(countLabel, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addComponent(countField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(countPanel);

        addUpdateButton.setText(frameType == UPDATE_FRAME ? "Update" : "Add");
        addUpdateButton.addActionListener(listener);
        addUpdateButton.setActionCommand(Controller.ITEM_FRAME_OKAY);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(listener);
        cancelButton.setActionCommand(Controller.ITEM_FRAME_CANCEL);

        GroupLayout buttonsPanelLayout = new GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
                buttonsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(buttonsPanelLayout.createSequentialGroup()
                                .addGap(140, 140, 140)
                                .addComponent(addUpdateButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                                .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addGap(140, 140, 140))
        );
        buttonsPanelLayout.setVerticalGroup(
                buttonsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(buttonsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(addUpdateButton)
                                        .addComponent(cancelButton))
                                .addContainerGap())
        );

        getContentPane().add(buttonsPanel);

        this.setLocationRelativeTo(null);

    }

    public String[] getData(){
        this.updateData();
        return data;
    }

    public void updateData(){
        data[1] = descriptionField.getText();
        data[2] = priceField.getText();
        data[3] = countField.getText();
    }

    /** Initialises all variables */
    public void init(){
        lineNumberPanel = new JPanel();
        itemNumberKeyLabel = new JLabel();
        invoiceNumberValueLabel = new JLabel();
        descriptionPanel = new JPanel();
        descriptionLabel = new JLabel();
        descriptionField = new JTextField();
        pricePanel = new JPanel();
        priceLabel = new JLabel();
        priceField = new JTextField();
        countPanel = new JPanel();
        countLabel = new JLabel();
        countField = new JTextField();
        buttonsPanel = new JPanel();
        addUpdateButton = new JButton();
        cancelButton = new JButton();
    }

    public int getFrameType() {
        return frameType;
    }
}
