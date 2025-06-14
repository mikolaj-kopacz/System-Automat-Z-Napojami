import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DodajFrame extends JFrame {
    private JPanel rootPanel;
    private JButton dodajButton;
    private JButton anulujButton;
    private JTextField nazwaField;
    private JTextField cenaField;
    private JTextField iloscField;
    private JTextField rozmiarField;
    private JComboBox<String> comboBox1;

    public DodajFrame() {
        super("Dodaj Nowy Produkt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setLocationRelativeTo(null);
        setSize(400, 300);


        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InwentarzFrame inwentarzFrame = new InwentarzFrame();
                inwentarzFrame.setVisible(true);
            }
        });

        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nazwa = nazwaField.getText();
                BigDecimal cena = new BigDecimal(cenaField.getText());
                String typ = (String) comboBox1.getSelectedItem();
                int ilosc = Integer.parseInt(iloscField.getText());
                int rozmiar = Integer.parseInt(rozmiarField.getText());

                boolean success = dodajProduktDoBazy(nazwa, cena, typ, ilosc, rozmiar);
                if (success) {
                    JOptionPane.showMessageDialog(DodajFrame.this, "Produkt został dodany");
                    dispose();
                    InwentarzFrame inwentarzFrame = new InwentarzFrame();
                    inwentarzFrame.setVisible(true);
                }
            }
        });
    }

    private boolean czyProduktIstnieje(String nazwa, BigDecimal cena, int rozmiar) {
        String dbUrl = "jdbc:sqlite:automat.db";
        String sql = "SELECT COUNT(*) FROM produkty p " +
                "JOIN detale_napoje dn ON p.id = dn.id_produktu " +
                "WHERE p.nazwa = ? AND p.cena = ? AND dn.pojemnosc_ml = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nazwa);
            pstmt.setBigDecimal(2, cena);
            pstmt.setInt(3, rozmiar);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Produkt już istnieje w bazie danych");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean dodajProduktDoBazy(String nazwa, BigDecimal cena, String typ, int ilosc, int rozmiar) {
        if (czyProduktIstnieje(nazwa, cena, rozmiar)) {
            return false; // Produkt istnieje
        }

        String dbUrl = "jdbc:sqlite:automat.db";
        String sqlProdukty = "INSERT INTO produkty (nazwa, cena, typ_produktu, ilosc) VALUES (?, ?, ?, ?)";
        String sqlDetaleNapoje = "INSERT INTO detale_napoje (id_produktu, pojemnosc_ml) VALUES (last_insert_rowid(), ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmtProdukty = conn.prepareStatement(sqlProdukty);
             PreparedStatement pstmtDetale = conn.prepareStatement(sqlDetaleNapoje)) {

            conn.setAutoCommit(false); // Rozpocznij transakcję

            pstmtProdukty.setString(1, nazwa);
            pstmtProdukty.setBigDecimal(2, cena);
            pstmtProdukty.setString(3, typ);
            pstmtProdukty.setInt(4, ilosc);
            pstmtProdukty.executeUpdate();

            pstmtDetale.setInt(1, rozmiar);
            pstmtDetale.executeUpdate();

            conn.commit(); // Zatwierdź transakcję
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Błąd SQL
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DodajFrame().setVisible(true));
    }
}