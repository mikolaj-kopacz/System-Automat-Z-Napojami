import java.math.BigDecimal;

public abstract class Napoj extends Produkt {
    protected int pojemnoscMl;

    public Napoj(int id, String nazwa, BigDecimal cena, int pojemnoscMl) {
        super(id, nazwa, cena);
        this.pojemnoscMl = pojemnoscMl;
    }

    public int getPojemnoscMl() {
        return pojemnoscMl;
    }
}