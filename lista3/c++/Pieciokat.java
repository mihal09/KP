public class Pieciokat extends Figura {
    private int bok;

    @Override
    public double Obwod() {
        return bok*5;
    }

    @Override
    public double Pole() {
        return 5 / 4.0 * (bok*bok) / Math.tan(Math.PI / 5);
    }

    public Pieciokat(int bok) throws Exception{
        if(bok<=0)
            throw new Exception("Niedodatni bok!");
        this.bok = bok;
    }
}
