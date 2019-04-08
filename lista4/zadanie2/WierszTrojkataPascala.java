import java.util.ArrayList;

public class WierszTrojkataPascala {
	int n;

	public WierszTrojkataPascala(int n) {
		this.n = n;
	}

	int wspolczynnik(int m) {
		if (m < 0 || m > n) {
			System.out.println("liczba spoza zakresu");
			return -1;
		}
		int wynik = 1;
		for (int i = 1; i <= m; i++) {
			wynik = wynik * (n - i + 1) / i;
		}
		return wynik;
	}

	ArrayList<Integer> getWiersz(){
		ArrayList<Integer> wiersz = new ArrayList<>();
		for(int i=0; i<=n; i++){
			wiersz.add(wspolczynnik(i));
		}
		return wiersz;
	};

}
