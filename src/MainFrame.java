import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private JPanel rootPanel;
    private JLabel statusLabel;
    private JPanel panelDolny;
    private JButton adminButton;
    private JPanel panelPrawy;
    private JPanel panelGlowny;
    private JPanel panelProdukty;
    private JPanel panelDetali;
    private JLabel labelZdjecie;
    private JTextArea textOpis;
    private JButton koszykButton;
    private JButton zaplacButton;
    private JButton dodajDoKoszykaButton;
    private JLabel cenaLabel;
    private JLabel kosztLabel;
    private JLabel liczbaProduktowLabel;

    private List<Produkt> koszyk = new ArrayList<>();
    private BigDecimal kosztCalkowity = BigDecimal.ZERO;

    public MainFrame() {
        this.setContentPane(rootPanel);
        this.setTitle("Automat z Napojami");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        stworzPrzyciskiProduktow();

        panelPrawy.setPreferredSize(new Dimension(170, 0));
        panelDetali.setPreferredSize(new Dimension(250, 0));

        this.pack();
        this.setMinimumSize(new Dimension(this.getWidth(), 600));
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        adminButton.addActionListener(e -> {
            AdminFrame adminFrame = new AdminFrame();
            adminFrame.setVisible(true);
            this.dispose();
        });

        adminButton.setVisible(false);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SHIFT) {
                adminButton.setVisible(true);
            } else if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_SHIFT) {
                adminButton.setVisible(false);
            }
            return false;
        });

        dodajDoKoszykaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajDoKoszyka();
            }
        });
        koszykButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                KoszykFrame koszykFrame = new KoszykFrame(koszyk, MainFrame.this);
                koszykFrame.setVisible(true);
            }
        });
        zaplacButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (kosztCalkowity != BigDecimal.ZERO) {
                    setVisible(false);
                    ZaplacFrame zaplacFrame = new ZaplacFrame(MainFrame.this, kosztCalkowity);
                    zaplacFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "Wybierz produkt, aby dodać do koszyka!");
                }
            }
        });
    }

    private void stworzPrzyciskiProduktow() {
        panelProdukty.removeAll();
        panelProdukty.setLayout(new GridLayout(0, 3, 10, 10));

        List<Produkt> produkty = BazaDanych.pobierzWszystkieProdukty();

        if (produkty.isEmpty()) {
            panelProdukty.add(new JLabel("Błąd bazy danych lub brak produktów."));
        }

        for (Produkt produkt : produkty) {
            //<html> dla przelaczenia do nastepnej linii
            JButton productButton = new JButton("<html><center>" + produkt.getNazwa() + "<br>" + produkt.getCena().toPlainString() + " zł</center></html>");
            productButton.setFont(new Font("Arial", Font.BOLD, 16));
            productButton.setFocusable(false);

            productButton.addActionListener(e -> {
                statusLabel.setText("Wybrano: " + produkt.getNazwa());
                labelZdjecie.setText("<html><center><i>Obrazek dla<br>" + produkt.getNazwa() + "</i></center></html>");
                textOpis.setText(produkt.getSzczegolowyOpis());
                cenaLabel.setText("Cena: " + produkt.getCena().toPlainString() + " zł");
            });
            panelProdukty.add(productButton);
        }
        panelProdukty.revalidate();
        panelProdukty.repaint();
    }

    private void dodajDoKoszyka() {
        Produkt wybranyProdukt = null;
        for (Produkt produkt : BazaDanych.pobierzWszystkieProdukty()) {
            if (produkt.getNazwa().equals(statusLabel.getText().replace("Wybrano: ", ""))) {
                wybranyProdukt = produkt;
                break;
            }
        }

        if (wybranyProdukt != null) {
            koszyk.add(wybranyProdukt);
            kosztCalkowity = kosztCalkowity.add(wybranyProdukt.getCena());
            aktualizujEtykiety();
            JOptionPane.showMessageDialog(this, "Dodano do koszyka: " + wybranyProdukt.getNazwa());
        } else {
            JOptionPane.showMessageDialog(this, "Wybierz produkt, aby dodać do koszyka!");
        }
    }

    public void aktualizujEtykiety() {
        kosztCalkowity = BigDecimal.ZERO;
        for (Produkt produkt : koszyk) {
            kosztCalkowity = kosztCalkowity.add(produkt.getCena());
        }
        liczbaProduktowLabel.setText("Liczba produktów: " + koszyk.size());
        kosztLabel.setText("Koszt: " + kosztCalkowity.toPlainString() + " zł");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}