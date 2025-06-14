import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;

public class EdytujFrame extends JFrame {
    private JPanel rootPanel;
    private JLabel nazwaLabel;
    private JLabel typLabel;
    private JTextField cenaTextField;
    private JTextField iloscTextField;
    private JButton zapiszButton;
    private JButton anulujButton;

    private int produktId;

    public EdytujFrame(int id, String nazwa, String typ, BigDecimal cena, int ilosc) {
        super("Edytuj Produkt");
        this.produktId = id;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setLocationRelativeTo(null);
        setSize(400, 300);

        nazwaLabel.setText(nazwa);
        typLabel.setText(typ);
        cenaTextField.setText(cena.toPlainString());
        iloscTextField.setText(String.valueOf(ilosc));

        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InwentarzFrame inwentarzFrame = new InwentarzFrame();
                inwentarzFrame.setVisible(true);
            }
        });

        zapiszButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BigDecimal updatedCena = new BigDecimal(cenaTextField.getText());
                int updatedIlosc = Integer.parseInt(iloscTextField.getText());

                if (aktualizujProduktWBazie(produktId, updatedCena, updatedIlosc)) {
                    JOptionPane.showMessageDialog(EdytujFrame.this, "Produkt został zaktualizowany");
                    dispose();
                    new InwentarzFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(EdytujFrame.this, "Błąd podczas aktualizacji produktu");
                }
            }
        });
    }

    private boolean aktualizujProduktWBazie(int id, BigDecimal cena, int ilosc) {
        String dbUrl = "jdbc:sqlite:automat.db";
        String sql = "UPDATE produkty SET cena = ?, ilosc = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, cena);
            pstmt.setInt(2, ilosc);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EdytujFrame(1, "Przykład", "GAZOWANY", BigDecimal.valueOf(10), 10).setVisible(true));
    }
}