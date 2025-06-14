import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class ZaplacFrame extends JFrame {
    private JPanel rootPanel;
    private JPanel panelGorny;
    private JPanel panelDolny;
    private JButton anulujButton;
    private JPanel panelGlowny;
    private JPanel kwotaDoZaplatyPanel;
    private JLabel kwotaDoZaplatyLabel;
    private JLabel kwotaLabel;
    private JButton zaplacKartaButton;
    private MainFrame mainFrame;

    public ZaplacFrame(MainFrame mainFrame, BigDecimal kwota) {
        super("Płatność");
        this.mainFrame = mainFrame;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(rootPanel);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        kwotaLabel.setText(kwota.toPlainString() + " zł");
        kwotaLabel.setFont(new Font("Arial", Font.BOLD, 28));

        anulujButton.addActionListener(e -> {
            dispose();
            mainFrame.setVisible(true);
        });

        zaplacKartaButton.addActionListener(e -> {
            symulujPlatnoscKarta();
        });
    }

    private void symulujPlatnoscKarta() {
        // "zablokowanie" przyciskow
        zaplacKartaButton.setEnabled(false);
        anulujButton.setEnabled(false);

        Timer timer = new Timer(2500, e -> { // opoznienie wykonania akcji
            JOptionPane.showMessageDialog(
                    this,
                    "Płatność zakończona pomyślnie!",
                    "Sukces",
                    JOptionPane.INFORMATION_MESSAGE
            );



            dispose();
            mainFrame.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
        timer.setRepeats(false); //
        timer.start();
    }
}