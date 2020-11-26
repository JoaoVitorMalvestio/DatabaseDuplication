package Client.View;

import Client.Main;
import Models.Person;

import javax.swing.*;

public class PersonView extends JFrame {
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

    public PersonView(String title, Person person) {
        super(title);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(panelPerson);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        fieldId.disable();

        clearPerson();

        if (person.getId() != null){
            setPerson(person);
        }

        btSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btSalvarMouseClicked(evt);
            }
        });
        btFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btFecharMouseClicked(evt);
            }
        });
    }

    private void clearPerson(){
        fieldId.setText("");
        fieldName.setText("");
        fieldAddress.setText("");
        fieldCity.setText("");
        fieldNeighborhood.setText("");
        fieldNumberAddress.setText("");
        fieldPhone.setText("");
        fieldState.setText("");
        fieldZipCode.setText("");
    }

    private void setPerson(Person person){
        fieldId.setText(person.getId().toString());
        fieldName.setText(person.getName());
        fieldAddress.setText(person.getAddress());
        fieldCity.setText(person.getCity());
        fieldNeighborhood.setText(person.getNeighborhood());
        fieldNumberAddress.setText(person.getNumberAddress());
        fieldPhone.setText(person.getPhone());
        fieldState.setText(person.getState());
        fieldZipCode.setText(person.getZipCode());
    }

    private Person getPersonFromView(){
        Person person = new Person();

        person.setId(null);

        if (!fieldId.getText().equals("")){
            person.setId(Integer.parseInt(fieldId.getText()));
        }

        person.setName(fieldName.getText());
        person.setAddress(fieldAddress.getText());
        person.setCity(fieldCity.getText());
        person.setNeighborhood(fieldNeighborhood.getText());
        person.setNumberAddress(fieldNumberAddress.getText());
        person.setPhone(fieldPhone.getText());
        person.setState(fieldState.getText());
        person.setZipCode(fieldZipCode.getText());

        return person;
    }

    private void btSalvarMouseClicked(java.awt.event.MouseEvent evt) {
        Person person = getPersonFromView();

        if (person.getId() == null){
            Main.createPerson(person);
            JOptionPane.showMessageDialog(null,"Pessoa adicionada com sucesso!");
        }
        else {
            Main.updatePerson(person);
            JOptionPane.showMessageDialog(null,"Pessoa atualizada com sucesso!");
        }

        this.setVisible(false);
    }

    private void btFecharMouseClicked(java.awt.event.MouseEvent evt) {
        this.setVisible(false);
    }
}

