package Backup.View;

import Client.View.PersonTableModel;
import Models.Person;
import Threads.HotReloadTables.BackupUpdateView;
import javax.swing.*;
import java.util.List;

public class ListPersonView extends JFrame{
    private JTable tablePerson;
    private JPanel panel1;
    private JButton atualizarButton;

    public ListPersonView(String title, List<Person> persons) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel1);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        tablePerson.setRowSelectionAllowed(true);

        initializeTable(persons);

        atualizarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                atualizarButtonMouseClicked(evt);
            }
        });
        new BackupUpdateView(this);
    }

    private void initializeTable(List<Person> persons){
        tablePerson.setModel(new PersonTableModel());

        if (persons != null && persons.size() > 0){
            tablePerson.setModel(new PersonTableModel(persons));
        }
    }

    public void refreshTable(){
        tablePerson.setModel(new PersonTableModel());

        if (Backup.Main.list != null && Backup.Main.list.size() > 0){
            tablePerson.setModel(new PersonTableModel(Backup.Main.list));
        }
    }

    private void atualizarButtonMouseClicked(java.awt.event.MouseEvent evt) {
        refreshTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ListPersonView listPersonView = new ListPersonView("Listagem "+Backup.Main.routerEnum.description, Backup.Main.list);
//            new BackupUpdateView(listPersonView);
        });
    }
}
