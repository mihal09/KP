package zadanie2;

import java.util.ArrayList;

public class TrojkatPascala {
    int n;
    public ArrayList<ArrayList<Integer>> wiersze = new ArrayList<>();
    public ArrayList<String> trojkatWiersze;

    public TrojkatPascala(int n) {
        this.n = n;
        trojkatWiersze = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            String linijka = "";
            WierszTrojkataPascala wiersz = new WierszTrojkataPascala(i);
            wiersze.add(wiersz.getWiersz());
            for (int element : wiersz.getWiersz()) {
                linijka += element + "  ";
            }
            trojkatWiersze.add(linijka);
        }
    }


    public int getSize() {
        return n;
    }

    public ArrayList<ArrayList<Integer>> getWiersze() {
        return wiersze;
    }

    public static void main(String[] args) {
        int n;
        TrojkatPascala t = new TrojkatPascala(5);
        System.out.print(t.toString());
    }
}
