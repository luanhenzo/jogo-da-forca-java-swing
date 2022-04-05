package br.univates.luan.views;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
    private JPanel vidasPanel;
    private JPanel vidasLetrasPanel;
    private JPanel palavraPanel;
    private JLabel palavraLabel;
    private JPanel iniciarESairPanel;
    private JScrollPane letrasUsadasScrollPane;

    private String palavraSecreta;
    private ArrayList<Character> letrasHabilitadas;
    private ArrayList<Character> letrasUsadas;
    private int vidasRestantes;
    private DefaultListModel<Character> letrasUsadasListModel;

    private boolean stringLettersAndHifens(String s) {
        boolean isStringJustLettersAndHifens = true;
        for (Character ch : s.toCharArray()) {
            if (!((ch >= 65 && ch <= 90) ||
                  (ch >= 97 && ch <= 122) ||
                  (ch == 45))) {
                isStringJustLettersAndHifens = false;
                break;
            }
        }
        return isStringJustLettersAndHifens;
    }

    public AppView() {
        vidasRestantes = 5;
        letrasUsadas = new ArrayList<>();
        letrasHabilitadas = new ArrayList<>();
        setContentPane(contentPane);
        setModal(true);
        palavraLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        pack();

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (getWidth() / 2);
        int y = (screenSize.height / 2) - (getHeight() / 2);
        setLocation(x, y);

        letrasUsadasListModel = new DefaultListModel();
        letrasUsadasList.setModel(letrasUsadasListModel);

        iniciarButton.addActionListener(e -> onIniciar());
        sairButton.addActionListener(e -> onSair());

        // Limitar o input a 1 caráctere na hora de chutar.
        inputsTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (iniciarButton.getText().equals("Chutar")) {
                    if (inputsTextField.getText().length() >= 1) {
                        e.consume();
                    }
                }
            }
        });

        // Transformar todas as letras em caixa alta.
        inputsTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int pos = inputsTextField.getCaretPosition();
                inputsTextField.setText(inputsTextField.getText().toUpperCase());
                inputsTextField.setCaretPosition(pos);
            }
        });

        // Habilitar o botão de inciar ou botão de chute somente quando o input está preenchido e não possue números.
        inputsTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                iniciarButton.setEnabled(
                        !(inputsTextField.getText().isBlank()) &&
                         (stringLettersAndHifens(inputsTextField.getText())) &&
                        !(letrasUsadas.contains(inputsTextField.getText().toCharArray()[0])));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                iniciarButton.setEnabled(
                        !(inputsTextField.getText().isBlank()) &&
                         (stringLettersAndHifens(inputsTextField.getText())) &&
                        !(letrasUsadas.contains(inputsTextField.getText().toCharArray()[0])));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                iniciarButton.setEnabled(
                        !(inputsTextField.getText().isBlank()) &&
                         (stringLettersAndHifens(inputsTextField.getText())) &&
                        !(letrasUsadas.contains(inputsTextField.getText().toCharArray()[0])));
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onSair();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSair();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private String iniciarPalavraSecreta(String palavraSecreta) {
        String palavraSecretaEditada = "";

        for (char ch : palavraSecreta.toCharArray()) {
            if (ch != ' ' && ch != '-') {
                palavraSecretaEditada += "_ ";
                if (!letrasHabilitadas.contains(ch)) {
                    letrasHabilitadas.add(ch);
                }
            } else if (ch == '-'){
                palavraSecretaEditada += "- ";
            } else {
                palavraSecretaEditada += "  ";
            }
        }

        return palavraSecretaEditada;
    }

    private void finalizarJogo() {
        palavraSecreta = "";
        vidasRestantes = 5;
        palavraLabel.setText("JOGO DA FORCA");
        letrasUsadasListModel.removeAllElements();
        letrasUsadas.clear();
        letrasHabilitadas.clear();
        v1RadioButton.setSelected(true);
        v2RadioButton.setSelected(true);
        v3RadioButton.setSelected(true);
        v4RadioButton.setSelected(true);
        v5RadioButton.setSelected(true);
        iniciarButton.setText("Iniciar");
        sairButton.setText("Sair");
    }

    private void reduzirVida() {
        vidasRestantes--;
        switch (vidasRestantes) {
            case 4:
                v5RadioButton.setSelected(false);
                break;
            case 3:
                v4RadioButton.setSelected(false);
                break;
            case 2:
                v3RadioButton.setSelected(false);
                break;
            case 1:
                v2RadioButton.setSelected(false);
                break;
            case 0:
                v1RadioButton.setSelected(false);
                break;
        }
    }

    private void fazerUmChute(Character letra) {
        if (letrasHabilitadas.contains(letra)) {
            letrasHabilitadas.remove(letra);

            char[] palavraLabelAntiga = palavraLabel.getText().toCharArray();
            char[] palavraSecretaCharArray = palavraSecreta.replaceAll(" ", "").toCharArray();

            int index = 0;
            for (int i = 0; i < palavraLabelAntiga.length; i++) {
                if (palavraLabelAntiga[i] == '_') {
                    if (palavraSecretaCharArray[index] == letra) {
                        palavraLabelAntiga[i] = letra;
                    }
                    index++;
                } else if ((palavraLabelAntiga[i] >= 65 && palavraLabelAntiga[i] <= 90) ||
                            palavraLabelAntiga[i] >= 97 && palavraLabelAntiga[i] <= 122 ||
                            palavraLabelAntiga[i] == '-') {
                    index++;
                }
            }

            String palavraLabelAtualizada = "";
            for (Character ch : palavraLabelAntiga) {
                palavraLabelAtualizada += ch;
            }
            palavraLabel.setText(palavraLabelAtualizada);
        } else {
            JOptionPane.showMessageDialog(this, letra + " não está na palavra!");
            reduzirVida();
        }
        inputsTextField.setText("");
        letrasUsadasListModel.addElement(letra);
        letrasUsadas.add(letra);
        if (letra >= 91 && letra <= 122) {
            letrasUsadas.add((char) (letra - 32));
        } else if (letra >= 65 && letra <= 90){
            letrasUsadas.add((char) (letra + 32));
        }
        pack();
    }

    private void onIniciar() {
        if (iniciarButton.getText().equals("Iniciar")) {
            palavraSecreta = inputsTextField.getText();
            inputsTextField.setText("");
            palavraLabel.setText(iniciarPalavraSecreta(palavraSecreta));
            iniciarButton.setText("Chutar");
            sairButton.setText("Desistir");
            pack();
        } else if (iniciarButton.getText().equals("Chutar")) {
            char letraChutada = inputsTextField.getText().toCharArray()[0];
            fazerUmChute(letraChutada);
            if (vidasRestantes == 0) {
                JOptionPane.showMessageDialog(this, "Todas as vidas foram usadas. Você perdeu.");
                JOptionPane.showMessageDialog(this, "A palavra era \"" + palavraSecreta + "\".");
                finalizarJogo();
            }
            if (letrasHabilitadas.size() == 0) {
                JOptionPane.showMessageDialog(this, "Você venceu! Palavra era \"" + palavraSecreta + "\". Parabéns!");
                finalizarJogo();
            }
        }
    }

    private void onSair() {
        if (sairButton.getText().equals("Sair")) {
            dispose();
        } else if (sairButton.getText().equals("Desistir")) {
            int isDesistente = JOptionPane.showConfirmDialog(this, "Tem certeza que quer desistir?", "Desistir do jogo?", JOptionPane.YES_NO_OPTION);
            if (isDesistente == 0) {
                JOptionPane.showMessageDialog(this, "Você desistiu. A palavra era \"" + palavraSecreta + "\".");
                finalizarJogo();
            }
        }
    }

    public static void main(String[] args) {
        AppView dialog = new AppView();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
