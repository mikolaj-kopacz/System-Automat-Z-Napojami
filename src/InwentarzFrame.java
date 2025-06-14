import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InwentarzFrame extends JFrame {
    private JPanel rootPanel;
    private JTable table1;
    private JButton dodajButton;
    private JButton usunButton;
    private JButton edytujButton;
    private JButton powrotButton;

    public InwentarzFrame() {
        super("Inwentarz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setSize(600, 600);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nazwa", "Cena", "Typ", "Ilość", "Rozmiar (ml)"}, 0);
        table1.setModel(model);

        List<Object[]> produkty = pobierzProduktyZBazy();
        for (Object[] produkt : produkty) {
            model.addRow(produkt);
        }

        usunButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0);
                if (usunProduktZBazy(id)) {
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(InwentarzFrame.this, "Produkt usunięty");
                } else {
                    JOptionPane.showMessageDialog(InwentarzFrame.this, "Błąd przy usuwaniu produktu");
                }
            } else {
                JOptionPane.showMessageDialog(InwentarzFrame.this, "Wybierz produkt do usunięcia");
            }
        });

        edytujButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0);
                String nazwa = (String) model.getValueAt(selectedRow, 1);
                BigDecimal cena = new BigDecimal((String) model.getValueAt(selectedRow, 2));
                String typ = (String) model.getValueAt(selectedRow, 3); // Dodaj to
                int ilosc = (int) model.getValueAt(selectedRow, 4);

                dispose();
                EdytujFrame edytujFrame = new EdytujFrame(id, nazwa, typ, cena, ilosc);
                edytujFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(InwentarzFrame.this, "Wybierz produkt do edycji");
            }
        });

        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DodajFrame dodajFrame = new DodajFrame();
                dodajFrame.setVisible(true);
            }
        });
        powrotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdminFrame adminFrame = new AdminFrame();
                adminFrame.setVisible(true);
            }
        });
    }

    private boolean usunProduktZBazy(int id) {
        String dbUrl = "jdbc:sqlite:automat.db";
        String sql = "DELETE FROM produkty WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas usuwania z bazy: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private List<Object[]> pobierzProduktyZBazy() {
        List<Object[]> produkty = new ArrayList<>();
        String dbUrl = "jdbc:sqlite:automat.db";

        String sql = "SELECT p.id, p.nazwa, p.cena, p.typ_produktu, p.ilosc, dn.pojemnosc_ml " +
                "FROM produkty p " +
                "LEFT JOIN detale_napoje dn ON p.id = dn.id_produktu";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nazwa = rs.getString("nazwa");
                String cena = rs.getBigDecimal("cena").toPlainString();
                String typProduktu = rs.getString("typ_produktu");
                int ilosc = rs.getInt("ilosc");
                int pojemnoscMl = rs.getInt("pojemnosc_ml");

                produkty.add(new Object[]{id, nazwa, cena, typProduktu, ilosc, pojemnoscMl});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych z bazy", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return produkty;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InwentarzFrame().setVisible(true));
    }
}