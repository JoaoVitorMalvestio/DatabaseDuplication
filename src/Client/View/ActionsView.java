package Client.View;

import Client.Main;
import Models.Person;

import javax.swing.*;
import java.util.List;

public class ActionsView extends JFrame {
    private JButton inserirNovaPessoaButton;
    private JButton listarPessoasButton;
    private JPanel panelAction;

    public ActionsView(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelAction);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        inserirNovaPessoaButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inserirNovaPessoaButtonMouseClicked(evt);
            }
        });

        listarPessoasButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listarPessoasButtonButtonMouseClicked(evt);
            }
        });
    }

    private void inserirNovaPessoaButtonMouseClicked(java.awt.event.MouseEvent evt) {
        new PersonView("Cadastrar nova pessoa", new Person());
    }

    private void listarPessoasButtonButtonMouseClicked(java.awt.event.MouseEvent evt) {
        String personsString = Main.getListPerson();

        List<Person> persons = Person.stringToList(personsString);

        new ListPersonView("Lista de pessoas cadastradas", persons);
    }
}
