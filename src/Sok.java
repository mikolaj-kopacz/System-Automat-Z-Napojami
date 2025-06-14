import java.math.BigDecimal;

public class Sok extends Napoj {
    private String rodzajOwocu;

    public Sok(int id, String nazwa, BigDecimal cena, int pojemnoscMl, String rodzajOwocu) {
        super(id, nazwa, cena, pojemnoscMl);
        this.rodzajOwocu = rodzajOwocu;
    }

    @Override
    public String getSzczegolowyOpis() {
        return String.format("Sok owocowy: %s\nSmak: %s\nPojemność: %d ml\nCena: %.2f zł",
                nazwa, rodzajOwocu, pojemnoscMl, cena);
    }
}