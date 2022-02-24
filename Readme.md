# Aplikacja do wysyłania informacji w modelu asynchronicznym
Instrukcja konfiguracji środowiska systemowego dla aplikacji. Potrzebne nam będzie narzędzie o nazwie Docker aby uruchomić poprawnie serwisy oraz infrastrukturę.

Wymagania systemowe aby uruchomic system mikroserwisowy na podstawie docker'a.
Nalezy miec (minimalne wymagania):
-   64-bit procesor
-   4GB system RAM.
-   Obsługa wirtualizacji sprzętu na poziomie systemu BIOS musi być włączona w ustawieniach systemu BIOS.

## Docker engine
Wchodzimy na stronę dockera, najlepiej zainstalować wersję Docker Desktop, jeśli nie jesteśmy wstanie jej zainstalować potrzebna będzie instalacja manualna - rozdział **Docker engine manual**
```
https://docs.docker.com/get-docker/
```

Następnie przechodzimy do głównego folderu używając powłoki systemowej czyli:

Windows: cmd - Wiersz poleceń CMD
Linux: terminal
MacOS: terminal

Powinniśmy widzieć i być w folderze składającym się z tych folderów i plików docker-compose.
![enter image description here](https://i.imgur.com/p8zAoQY.png)

Docker Desktop, zainstalował nam w pełni wszystkie pakiety potrzebne do zarzadzania dockerem. Pamietajmy aby Docker Desktop byl uruchomiony w tle oraz po instalacji wymagane jest aby zrestartowac komputer. Jeśli chcemy korzystać w pełni z dockeryzowanych aplikacji wpisujemy po prostu komende

```
docker-compose up --build
```
lub (ponizsza komenda nie bedzie nam wypisywac logów z poszczególnych serwisów)
```
docker-compose up -d --build
```
następnie powinniśmy zobaczyć takie logi:
![enter image description here](https://i.imgur.com/ulxBind.png)

to oznacza ze wszystkie serwisy zostaly zbudowane.
Nastepnie powinnismy widziec przy uzyciu komendy 
```
docker ps
```
taki wynik:
![enter image description here](https://i.imgur.com/fEj4NY4.png)

serwisy teraz dzialaja po dockerowej sieci wewnetrznej. Natomiast sa dostepne z poziomu terminala oraz przegladarki internetowej.

![enter image description here](https://i.imgur.com/2cQNX8E.png)

Jak widzimy strona internetowa aplikacji czyli real-time-chat-frontend dziala na porcie 3000.

## Uruchamianie systemu z poziomu terminala/intellij idea

Zalozenia aplikacyjne:
https://chocolatey.org/install - opcjonalnie, zarzadza pakietami na systemie windows 
https://brew.sh/index_pl - opcjonalne(polecane), zarzadza pakietami na systemie MacOS
https://sdkman.io/ - dla unixowych systemow

Gradle wersja 7.3.3
https://gradle.org/install/
https://community.chocolatey.org/packages/gradle
Wersja java JDK 11
https://www.openlogic.com/openjdk-downloads
Node wersja 17.4.0
https://nodejs.org/en/download/current/
https://nodejs.org/en/blog/release/v17.4.0/ ( na samym dole sa linki do pobrania na kazdy system )

Aplikacje mozna uruchomic rowniez recznie, wystarczy uruchomic podstawowe aplikacje infrastrukturalne takie jak postgres, kafka oraz zookeeper korzystajac z docker-compose-local. Komenda (pamietajmy, musimy byc w glownym folderze tam gdzie istnieje ten plik 'docker-compose-local.yml' ):
```
docker-compose -f docker-compose-local.yml up -d --build
```
Nastepnie z poziomu intellija, importujemy aplikacje real-time-chat oraz real-time-chat-db oraz frontend. Nalezy pamietac o poprzednim zainstalowaniu odpowiedniej wersji Gradle, poniewaz czasami intellij moze pobrac za stara lub zbyt nowa wersje. Nalezy z tym uwazac. Po zainstalowaniu pakietow przez Intellija ustawiamy w aplikacji (real-time-chat oraz real-time-chat-db) taka konfiguracje:
![enter image description here](https://i.imgur.com/g8InqF4.png)

Ustawiamy active profiles na local, lub dodajemy do VM options
```
-Dspring.profiles.active=local
```
**UWAGA: APLIKACJA REAL-TIME-CHAT** **- ABY URUCHOMIĆ LOKALNIE APLIKACJE REAL-TIME-CHAT NALEŻY WEJŚĆ W KLASE MAIN W REAL-TIME-CHAT PROJEKT ORAZ ZAKOMENTOWAĆ LINIJKI 26-31 (METODA admin())**

I klikamy RUN.(Intellij prawy gorny rog)

### Aplikacje backendowe przy uzyciu terminala

Wstepnie nalezy sprawdzic czy mamy gradle oraz java dodany do glownego PATH'a tak aby moc korzystac z nich podczas wpisywania komend w konsoli (terminal/CMD).

Nastepnie nalezy wejsc do kazdego folderu (real-time-chat-db oraz real-time-chat) i wpisujemy komende ```gradle build```

![enter image description here](https://i.imgur.com/En9tj3M.png)

Tak zbudowany projekt, posiada w sobie w pelni zbudowanego jara.
Nastepnie z uzyciem komendy (bedac w folderze danego projektu) wpisujemy zaleznie od tego co sie w tym folderze znajduje.
```
java -jar build/libs/real-time-chat-db-1.0-SNAPSHOT.jar --spring.profiles.active=local
```

Dla aplikacji real-time-chat (bez db), odpowiedni inny wygenerowany jar.

```
java -jar build/libs/real-time-chat-1.0-SNAPSHOT.jar --spring.profiles.active=local
```

Powinnismy po odpaleniu tych metod zobaczyc odpalajacy sie serwer tomcat wraz z kontekstem springa.
![enter image description here](https://i.imgur.com/oVDWh0o.png)


### Aplikacja frontendowa
Przy aplikacji frontendowej, nalezy zainstalowac node w wersji 17.4.0.
Skorzystac z package managera YARN lub NPM i wykonac w glownym folderze odpowiednio komende
```yarn``` lub ``` npm install ```
Powinnismy uzyskac mniej wiecej taki wynik (tutaj pakiety juz sa zainstalowane).
![enter image description here](https://i.imgur.com/0d06BZi.png)

nastepnie wpisujemy komende ```yarn start``` lub ```npm start```

![enter image description here](https://i.imgur.com/RmUfytw.png)



## Docker engine manual

Przy instalacji docker engine, bez uzycia docker desktop nalezy pamietac o instalacji dodatkowego pakietu zewnetrzengo docker-compose.

Tutaj instalacja docker'a na systemy unixowe np ubuntu
https://docs.docker.com/engine/install/ubuntu/

Tutaj sa instrukcje jak zainstalowac docker-compose na rozne systemy.
https://docs.docker.com/compose/install/

Do zainstalowania tego na MacOS
nie wystarczy samo brew. Polecam ten sposob autora: [Susam Pal](https://stackoverflow.com/users/303363/susam-pal)
https://stackoverflow.com/questions/40523307/brew-install-docker-does-not-include-docker-engine

Rowniez dockera mozemy zainstalowac na windowsie korzystajac z chocolatey
https://community.chocolatey.org/packages/docker-engine
https://community.chocolatey.org/packages/docker-desktop

W razie problemów kontakt - karolpiasecki21@gmail.com
