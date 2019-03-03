import java.util.*;

public class dzielniki {

	public static int div(int n){
		for(int i=n-1;i>0;i--)
		{
			if(n%i==0)
				return i;
		}
		return 1;
	}

	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			try{
				int n = Integer.parseInt(args[i]);
				System.out.println(div(n));
			}
			catch (NumberFormatException ex){
				System.out.println(args[i] + " nie jest liczba calkowita");
			}
		}
	}
}