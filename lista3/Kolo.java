public class Kolo extends Figura {
    private int promien;

    @Override
    public double Obwod() {
        return 2*Math.PI*promien;
    }

    @Override
    public double Pole() {
        return Math.PI*promien*promien;
    }

    public Kolo(int promien) throws Exception{
        if(promien<=0)
            throw new Exception("Niedodatni promien!");
        this.promien = promien;
    }
}
