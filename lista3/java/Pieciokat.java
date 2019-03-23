public class Pieciokat extends Figura {
    private int bok;

    public Pieciokat(int bok) throws Exception{
        this.nazwa = "Pieciokat";
        if(bok<=0)
            throw new Exception("Niedodatni bok!");
        this.bok = bok;
    }

    @Override
    public double Obwod() {
        return bok*5;
    }

    @Override
    public double Pole() {
        return 5 / 4.0 * (bok*bok) / Math.tan(Math.PI / 5);
    }

}
