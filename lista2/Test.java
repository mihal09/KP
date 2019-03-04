import java.util.*;

public class Test {
	public static void main(String[] args) {
		try{
			int n = Integer.parseInt(args[0]);
			if(n<0){
				System.out.println("Nieprawidłowy numer wiersza");
				return;
			}
			WierszTrojkataPascala wiersz = new WierszTrojkataPascala(n);
			for (int i = 1; i < args.length; i++) {
				try{
					System.out.print(args[i] +" - ");
					int m = Integer.parseInt(args[i]);
					int wspolczynnik = wiersz.wspolczynnik(m);
					if(wspolczynnik!=-1)
						System.out.println(wspolczynnik);
				}
				catch(Exception e){
					System.out.println("nieprawidlowa dana");
				}
			}
		}
		catch (Exception e) {
			System.out.println("Nieprawidlowy numer wiersza");
		}
	}
}
