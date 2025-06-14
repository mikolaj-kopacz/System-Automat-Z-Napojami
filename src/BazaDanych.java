import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class BazaDanych {

    // sciezka bazy danych
    private static final String DB_URL = "jdbc:sqlite:automat.db";

    public static List<Produkt> pobierzWszystkieProdukty() {
        List<Produkt> produkty = new ArrayList<>();
        String sql = "SELECT p.id, p.nazwa, p.cena, p.typ_produktu, " +
                "dn.pojemnosc_ml, ds.czy_slodzony, ds.rodzaj_owocu " +
                "FROM produkty p " +
                "LEFT JOIN detale_napoje dn ON p.id = dn.id_produktu " +
                "LEFT JOIN detale_specyficzne ds ON p.id = ds.id_produktu";


        //polaczenie z baza danych
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            while (rs.next()) {
                int id = rs.getInt("id");
                String nazwa = rs.getString("nazwa");
                BigDecimal cena = rs.getBigDecimal("cena");
                String typProduktu = rs.getString("typ_produktu");
                int pojemnoscMl = rs.getInt("pojemnosc_ml");

                switch (typProduktu) {
                    case "GAZOWANY":
                        boolean czySlodzony = rs.getBoolean("czy_slodzony");
                        produkty.add(new NapojGazowany(id, nazwa, cena, pojemnoscMl, czySlodzony));
                        break;
                    case "SOK":
                        String rodzajOwocu = rs.getString("rodzaj_owocu");
                        produkty.add(new Sok(id, nazwa, cena, pojemnoscMl, rodzajOwocu));
                        break;
                    case "WODA":
                        produkty.add(new Woda(id, nazwa, cena, pojemnoscMl));
                        break;
                    default:
                        System.err.println("Nieznany typ produktu w bazie: " + typProduktu);
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produkty;
    }
    }