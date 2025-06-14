import java.math.BigDecimal;

public class Woda extends Napoj {
    public Woda(int id, String nazwa, BigDecimal cena, int pojemnoscMl) {
        super(id, nazwa, cena, pojemnoscMl);
    }

    @Override
    public String getSzczegolowyOpis() {
        return String.format("Woda: %s\nPojemność: %d ml\nCena: %.2f zł",
                nazwa, pojemnoscMl, cena);
    }
}