package Client.View;

import com.sun.javaws.util.JfxHelper;

import javax.swing.*;

public class ClientView extends JFrame {
    private JPanel panelPerson;
    private JLabel labelPessoa;
    private JLabel labelName;
    private JLabel labelPhone;
    private JLabel labelAddress;
    private JLabel labelAddressNumber;
    private JLabel labelNeighborhood;
    private JLabel labelCity;
    private JLabel labelState;
    private JLabel labelZipCode;
    private JTextField fieldPhone;
    private JTextField fieldName;
    private JTextField fieldId;
    private JLabel labelId;
    private JTextField fieldAddress;
    private JTextField fieldNumberAddress;
    private JTextField fieldNeighborhood;
    private JTextField fieldCity;
    private JTextField fieldState;
    private JTextField fieldZipCode;
    private JButton btSalvar;
    private JButton btFechar;

    public ClientView(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelPerson);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        fieldId.disable();
    }

    public static void main(String[] args){
        JFrame frame = new ClientView("ClientView");

    }

}
