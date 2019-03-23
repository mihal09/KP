public class Szesciokat extends Figura {
    private int bok;

    @Override
    public double Obwod() {
        return bok*6;
    }

    @Override
    public double Pole() {
        return 6 / 4.0 * (bok*bok) / Math.tan(Math.PI / 6);
    }

    public Szesciokat(int bok) throws Exception{
        if(bok<=0)
            throw new Exception("Niedodatni bok!");
        this.bok = bok;
    }
}
