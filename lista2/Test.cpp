#include <iostream>
#include <stdlib.h>
#include <string.h>
using namespace std;

class WierszTrojkataPascala {
	int n;

	public:
	WierszTrojkataPascala(int _n) {
		n = _n;
	}

	int wspolczynnik(int m) {
		if (m < 0 || m > n) {
			cout << "liczba spoza zakresu" << endl;
			return -1;
		}
		double wynik = 1;
		for (int i = 1; i <= m; i++) {
			wynik = wynik * (n - i + 1) / i;
		}
		return (int)wynik;
	}
};

bool isNumber(char * text){
    int i=0;
    if(text[i]=='-')
        i = 1;
    for(; i< strlen(text); i++){
        if(!(text[i]>='0' && text[i]<='9'))
            return false;
    }
    return true;
}

int main(int argc,char* argv[]){
    if(!isNumber(argv[1])){
        cout << "Nieprawidlowy numer wiersza"<<endl;
        return 0;
    }
    int n = atoi(argv[1]);
    if(n<0){
        cout << "Nieprawidlowy numer wiersza"<<endl;
        return 0;
    }
    WierszTrojkataPascala* wiersz = new WierszTrojkataPascala(n);
    for (int i = 2; i < argc; i++) {
        cout << argv[i] <<" - ";
        if(!isNumber(argv[i])){
            cout << "nieprawidlowa dana" << endl;
            continue;
        }
        int m = atoi(argv[i]);
        int wspolczynnik = wiersz->wspolczynnik(m);
        if(wspolczynnik!=-1)
            cout << wspolczynnik << endl;;
    }
}
