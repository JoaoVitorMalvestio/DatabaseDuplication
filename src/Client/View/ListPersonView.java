package Client.View;

import Client.Main;
import Models.Person;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class ListPersonView extends JFrame{
    private JTable tablePerson;
    private JPanel panel1;
    private JButton removerButton;
    private JButton alterarButton;
    private JButton atualizarButton;
    List<Person> persons;

    public ListPersonView(String title, List<Person> persons) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel1);
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
        tablePerson.setModel(new PersonTableModel());

        if (persons != null && persons.size() > 0){
            tablePerson.setModel(new PersonTableModel(persons));
        }
    }

    private void refreshTable(){
        /*String personsString = Main.getListPerson();
        List<Person> persons = Person.stringToList(personsString);*/

        persons = new ArrayList<Person>();

        tablePerson.setModel(new PersonTableModel());

        if (persons != null && persons.size() > 0){
            tablePerson.setModel(new PersonTableModel(persons));
        }
    }

    private void removerButtonMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedLine = tablePerson.getSelectedRow();

        if (selectedLine == -1) return;

        int option = JOptionPane.showConfirmDialog(null, "Deseja remover a pessoa '" + persons.get(selectedLine).getName() + "'?");

        if (option == 0){
            //Main.removePerson(persons.get(selectedLine).getId());
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
