import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KoszykFrame extends JFrame {
    private JPanel rootPanel;
    private JLabel koszykLabel;
    private JButton usunButton;
    private JButton powrotButton;
    private JTable table1;
    private MainFrame mainFrame; // dla pokazania okna po kliknieciu powrotButton


    public KoszykFrame(List<Produkt> koszyk, MainFrame mainFrame) {
        super("Koszyk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setLocationRelativeTo(null);
        setSize(400,400);

        String[] columnNames = {"Nazwa", "Cena"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Produkt produkt : koszyk) {
            Object[] row = {produkt.getNazwa(), produkt.getCena().toPlainString() + " zł"};
            tableModel.addRow(row);
        }

        table1.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(table1);
        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(usunButton);
        buttonPanel.add(powrotButton);

        rootPanel.add(buttonPanel, BorderLayout.SOUTH);


        powrotButton.addActionListener(e -> {
            dispose();
            mainFrame.setVisible(true);
        });

        usunButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                koszyk.remove(selectedRow);
                ((DefaultTableModel) table1.getModel()).removeRow(selectedRow);
            mainFrame.aktualizujEtykiety();
            }
        });
    }
}