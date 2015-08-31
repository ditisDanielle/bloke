Hier volgen instructies om een nieuwe versie te deployen op de VPS.  
Delen tekst die tussen < > staan dienen vervangen te worden met de betreffende waarde. Zo staat < nr > voor team nummer.

Maak een war aan met het volgende commando

    mvn clean install

Hierna zul je in de target folder van het project een .zip en een .war bestand zien staan. De .zip bevat ook database scripts. Indien je geen wijzigingen hebt gemaakt aan de database dan volstaat het om hier de .war te gebruiken in plaats van de .zip. Een van deze bestanden moet eerst naar de VPS gekopieerd worden. Gebruik hiervoor het volgende commando: (vervang in deze stap en opkomende stappen .zip met .war indien je de .war gebruikt)

	scp <filename.zip> <username>@hhs-bacchus-<nr>.42.nl:/home/<username>

Log nu in op de server

    ssh <user>@hhs-bacchus-<nr>.42.nl
Voordat we deze nieuwe versie gaan deployen willen we de versie eerst in de oude_releases folder plaatsen. Dit doen we om een centrale plek te hebben met alle versies, zo kunnen we eventueel ook met minimale moeite een versie terug gaan indien iets niet goed blijkt te gaan. Let op dat op deze manier een versie teruggaan een stuk complexer wordt wanneer er databasewijzigingen zijn gemaakt. Eerst gaan we op de server naar de oude_releases map, daarna maken we een nieuwe folder aan voor deze versie van de applicatie en via het laatste commando verplaatsen we de zip file naar de gemaakte folder. Gebruik het unzip commando enkel als je de .zip file gebruikt, bij de .war is de unzip niet nodig. 

    cd /opt/bacchus/oude_releases
    mkdir versie-<yyyy>-<mm>-<dd>
    cd versie-<yyyy>-<mm>-<dd>
    mv /home/<user>/<filename.zip> .
    unzip <filename.zip>
Nu we de huidige versie bij de oude_releases hebben gezet is het tijd om de huidige versie ook werkelijk te gaan vervangen. Kijk eerst of de applicatie op dit moment draait:

	ps aux | grep bacchus

Hiermee zie je alle processen waarin de naam Bacchus voorkomt, hier zul je onder andere processen zien voor de connectie met de database en een regel met java. Deze regel met java is de draaiende tomcat applicatie.

Als de applicatie draait is het tijd om het te stoppen. 

	sudo -u root bacchus stop
Controleer nu of de applicatie ook werkelijk is gestopt via hetzelfde commando als eerst.

	ps aux | grep bacchus
Als de applicatie gestopt is zul je maar een enkele regel zien staan (namelijk het grep commando zelf). Zodra de applicatie volledig is gestopt kunnen we de oude .war verwijderen.

	cd /opt/bacchus/base/webapps
	rm -rf api
	rm -f api.war
	ls -al (gebruik deze om te controleren dat de folder leeg is)
	
Eventueel kun je ook de database droppen en opnieuw aanmaken via de volgende commando's (kijk voor meer informatie in de confluence documentatie):

	sudo -u postgres dropdb bacchusdb
	sudo -u postgres createdb -O bacchus bacchusdb

In het geval dat je databasewijzigingen hebt gemaakt zijn er twee opties. Ten eerste kan je lokaal een database dump maken en het bacchus_dump.sql script opnieuw draaien op de server, deze dropped eerst alle tabellen en voert daarna opnieuw de test data in. Ten tweede kan je zelf een script schrijven om modificaties te maken aan tabellen of nieuwe tabellen toe te voegen. Kijk voor meer informatie in de confluence documentatie.

	sudo -u postgres psql bacchusdb < db/bacchus_dump.sql
Nu is het tijd om de nieuwe .war op de juiste plaats te zetten. We renamen de .war naar api.war om te zorgen voor de juiste context 'api' in de applicatie URL. Verder gebruiken we het 'chown' commando om ervoor te zorgen dat andere mensen binnen jouw groep later ook een nieuwe api.war kunnen plaatsen.

	cd /opt/bacchus/base/webapps
	cp /opt/bacchus/oude_releases/<filename>/bacchus-<versie>.war api.war 
	chown <username>:bacchus api.war

Controleer via *ls -al* dat de map volledig leeg is buiten de api.war file.
Indien nodig kan je de configuratie aanpassen in */opt/bacchus/base/conf*
Nu kunnen we Bacchus starten via het volgende commando

	sudo -u root bacchus start
Tomcat zal nu de api.war uitpakken in een submap */opt/bacchus/base/webapps/api*

Controleer eerst wederom met hetzelfde commando of het proces loopt:

	ps aux | grep bacchus
	
Kijk daarna in de logfiles of het opstarten goed gaat en tijdens de uitvoering van de applicatie geen fouten optreden, deze logfiles zijn te vinden in */opt/bacchus/base/logs*, lees deze bestanden via de volgende instructies:

	cd /opt/bacchus/base/logs
	
	# bekijken kan met:
	less <bestandsnaam>
	pagina voorwaarts: f
	pagina terug: b
	naar einde: G
	naar begin: g
	less stoppen: q
  
	# bestand volgen terwijl het loopt:
	tail -f <bestandsnaam>
	stoppen met ctrl-c

De applicatie kun je testen in een browser via de url https://hhs-bacchus-< nr >.42.nl
Sluit aan het eind je shell op de VPS af via het volgende commando

	exit

=============================================================


