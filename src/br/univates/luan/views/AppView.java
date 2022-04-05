package br.univates.luan.views;

import javax.swing.*;
import java.awt.event.*;

public class AppView extends JDialog {
    private JPanel contentPane;
    private JButton sairButton;
    private JButton iniciarButton;
    private JTextField inputsTextField;
    private JRadioButton v1RadioButton;
    private JRadioButton v2RadioButton;
    private JRadioButton v3RadioButton;
    private JRadioButton v4RadioButton;
    private JRadioButton v5RadioButton;
    private JList letrasUsadasList;
    private JLabel vidasLabel;
    private JLabel letrasUsadasLabel;
    private JPanel letrasUsadasPanel;
    private JPanel vidasPanel;
    private JPanel vidasLetrasPanel;
    private JPanel palavraPanel;
    private JLabel palavraLabel;
    private JPanel iniciarESairPanel;
    private JButton buttonOK;
    private JButton buttonCancel;

    public AppView() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        AppView dialog = new AppView();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
