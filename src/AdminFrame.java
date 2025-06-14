import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminFrame extends JFrame {
    private JPanel rootPanel;
    private JPanel adminPanel;
    private JButton powrotButton;
    private JButton wylaczButton;
    private JButton transakcjeButton;
    private JButton inwentarzButton;
    private JButton finanseButton;
    private JLabel finanseLabel;
    private JLabel inwentarzLabel;
    private JLabel wylaczLabel;
    private JLabel transakcjeLabel;

    public AdminFrame() {
        super("Admin Panel");
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);


        powrotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
        inwentarzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InwentarzFrame inwentarzFrame = new InwentarzFrame();
                inwentarzFrame.setVisible(true);
            }
        });
        wylaczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
