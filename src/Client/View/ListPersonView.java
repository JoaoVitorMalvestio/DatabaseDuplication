package Client.View;

import Models.Person;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ListPersonView extends JFrame{
    private JTable tablePerson;
    private JPanel panelList;
    private JButton removerButton;
    private JButton alterarButton;
    private JButton atualizarButton;
    List<Person> persons;

    public ListPersonView(String title, List<Person> persons) {
        super(title);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(panelList);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        tablePerson.setRowSelectionAllowed(true);

        this.persons = persons;

        initializeTable(persons);

        removerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removerButtonMouseClicked(evt);
            }
        });

        alterarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                alterarButtonMouseClicked(evt);
            }
        });

        atualizarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                atualizarButtonMouseClicked(evt);
            }
        });
    }

    private void initializeTable(List<Person> persons){
        if (persons != null && persons.size() > 0){
            tablePerson.setModel(new PersonTableModel(persons));
        }
    }

    private void refreshTable(){
        if (persons != null && persons.size() > 0){
            tablePerson.setModel(new PersonTableModel(Person.stringToList(Client.Main.getListPerson())));
        }
    }

    private void removerButtonMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedLine = tablePerson.getSelectedRow();

        if (selectedLine == -1) return;

        int option = JOptionPane.showConfirmDialog(null, "Deseja remover a pessoa '" + persons.get(selectedLine).getName() + "'?");

        if (option == 0){
            Client.Main.removePerson(persons.get(selectedLine).getId());
            refreshTable();
        }

    }

    private void alterarButtonMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedLine = tablePerson.getSelectedRow();

        if (selectedLine == -1) return;

        new PersonView("Alterar pessoa", persons.get(selectedLine));
    }

    private void atualizarButtonMouseClicked(java.awt.event.MouseEvent evt) {
        refreshTable();
    }


}
