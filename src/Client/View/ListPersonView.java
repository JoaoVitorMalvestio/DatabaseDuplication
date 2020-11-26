package Client.View;

import Models.Person;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class ListPersonView extends JFrame{
    private JTable tablePerson;
    private JPanel panel1;
    private JButton removerButton;
    private JButton alterarButton;

    public ListPersonView(String title, List<Person> persons) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel1);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        tablePerson.setRowSelectionAllowed(true);

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
    }

    private void initializeTable(List<Person> persons){
        tablePerson.setModel(new PersonTableModel());

        if (persons != null && persons.size() > 0){
            tablePerson.setModel(new PersonTableModel(persons));
        }
    }

    private void removerButtonMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedLine = tablePerson.getSelectedRow();

        if (selectedLine == -1) return;

        int option = JOptionPane.showConfirmDialog(null, "Deseja remover essa pessoa?");

        if (option == 0){
            System.out.println("Remove pessoa");
        }
    }

    private void alterarButtonMouseClicked(java.awt.event.MouseEvent evt) {
        new PersonView("Cadastrar nova pessoa", new Person());
    }
}
