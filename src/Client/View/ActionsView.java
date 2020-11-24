package Client.View;

import javax.swing.*;

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
    }
}
