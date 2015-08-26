${project.name} v${project.version} 
--------------

Here we can place deployment information, change logs etc.

=============================================================

Stappen om de applicatie lokaal te testen met postgres
-- Installeer postgres lokaal
!! let op: de user 'postgres' wordt aangemaakt en daarbij wordt een wachtwoord voor deze user gevraagd.
!! Dit wachtwoord zul je nog vaak nodig hebben.
!! Ik heb gekozen voor het wachtwoord 'p0stgr3s'

-- Installeer indien gewenst pgAdmin3 lokaal voor een grafische user interface op je database

-- Maak in een cmd terminal een unix en een postgres user 'bacchus' aan: de applicatie verwacht de user 'bacchus' met als wachtwoord 'B@cch45'
# sudo -s
# useradd -m -s /bin/bash bacchus  (mac: via Systeemvoorkeuren gebruiker 'bacchus' toevoegen)
# sudo -u postgres createuser -P bacchus

-- Maak een postgres database voor de user bacchus aan: de applicatie verwacht de database 'bacchusdb'
# sudo -u postgres createdb -O bacchus bacchusdb

-- Specificeer de tabellen etc. in de database mbv het create script.
-- Het script kan steeds opnieuw uitgevoerd worden omdat in het begin alles verwijderd wordt.
-- Let op: omdat bacchus de eigenaar moet zijn van de tabellen, moet dit script ook als user bacchus uitgevoerd worden.
# sudo -u bacchus psql bacchusdb < <<path naar file>>/000_create_schema.sql

als het commando psql niet herkend wordt, zorg dan dat de locatie van dat commando in je PATH variabele staat.
dat kan door bv in het bestand .bash_profile in de homedirectory van de user postgres toe te voegen:
export PATH=$PATH:/Library/PostgreSQL/9.4/bin/

-- Applicatie lokaal draaien met gebruik van tomcat en de lokale postgres database (het profile demo-data zorgt dat je database gevuld wordt):
# mvn clean install tomcat7:run -Dspring.profiles=test,postgres,demo-data
-- Let Op. Als de database al gevuld is het profile demo-data niet weer gebruiken:
# mvn clean install tomcat7:run -Dspring.profiles=test,postgres

Kijk bij het opstarten van de applicatie in de log of de tabellen goed gevonden worden.

=============================================================
Let op!
Als je in de applicatie wijzigingen gaat aanbrengen die invloed hebben op de database, moet je ook het script src/main/db/000_create_schema.sql aanpassen.
Door bovenstaande procedure te volgen kun je testen of het script 000_create_schema.sql goed is.
Nadat je met de spring profile 'demo-data' je database gevuld hebt, moet je een nieuwe dump maken van je database die je weer kunt gebruiken om demodata op de
 VPS te zetten.
-- maken van een database dump:
# sudo -u postgres pg_dump bacchusdb -c > src/main/db/bacchus_dump.sql


=============================================================

Extra basis info voor Postgres

-- Je kunt in de database kijken met een commandline interface.
# sudo -u bacchus psql bacchusdb
-- Je kunt nu allerlei 'select' statements uitvoeren
-- Met het commando \d zie je welke tabellen er zijn
-- Met het commando \q verlaat je de postgres commandline interface.

-- Om de database compleet te verwijderen
# sudo -u postgres dropdb bacchusdb

-- Om de database user te verwijderen
# sudo -u postgres dropuser bacchus

Als je de database en/of de user verwijderd, moet je die wel weer opnieuw aanmaken
# sudo -u postgres createuser -P bacchus
# sudo -u postgres createdb -O bacchus bacchusdb

=============================================================

Stappen om de applicatie te deployen op een VPS
<versie> : de versie die je wilt deployen
<lokaal path> : het pad waar jouw zipfile staat. 'mvn clean install' maakt de zip aan in de 'target' map
<user>: jouw account op de vps, zoals dat door ons aan jou is toegekend
<nr>: het nummer van jouw group zoals dat het onderdeel is van jullie vps

Lokaal uitvoeren:
-- Met het commando 'mvn clean install' wordt er in de target directory een bacchus-<versie>.zip bestand gemaakt.
-- Kopieer in een cmd terminal het zip bestand naar de server.
# scp <lokaal path>/bacchus-<versie>.zip <user>@hhs-bacchus-<nr>.42.nl:/home/<user>

-- Log in op de server
# ssh <user>@hhs-bacchus-<nr>.42.nl

-- Verplaats de zip file naar een map in oude_releases en pak hem daar uit
# cd /opt/bacchus/oude_releases
# mkdir bacchus-<versie>
# cd bacchus-<versie>
# mv /home/<user>/bacchus-<versie>.zip .
# unzip bacchus-<versie>.zip

-- Voordat je de applicatie gaat deployen, moet deze eerst verwijderd en tomcat gestopt worden.
-- Verwijder eerst de oude war:
# cd /opt/bacchus/base/webapps
# rm -f api.war
-- Wacht een paar seconden totdat de submap api ook weg is. Dat kun je zien met het commando:
# ll
-- Als de submap api weg is kan tomcat gestopt worden:
# sudo -u root bacchus stop

-- Controleer of de applicatie gestopt is
# ps aux | grep bacchus
(als het goed is zie je nu maar één regel: het grep commando zelf)

-- Eventueel kun je de database verwijderen en opnieuw aanmaken
# sudo -u postgres dropdb bacchusdb
# sudo -u postgres createdb -O bacchus bacchusdb


-- Indien er databasewijzigingen zijn moet je deze eerst uitvoeren op de database
-- Dit script ruimt eerst de bestaande tabellen op.
------ Hoeft niet, zit in dump  # sudo -u bacchus psql bacchusdb < db/000_create_schema.sql

-- In plaats van het volledig opnieuw aanmaken van alle tabellen kun je ook een
-- aanvullend script maken dat wijzigingen uitvoert op je tabellen of nieuwe tabellen toevoegt.


-- Vul de testdata weer
-- Dit script maakt eerst de bestaande tabellen leeg.
# sudo -u postgres psql bacchusdb < db/bacchus_dump.sql


-- Kopieer de nieuwe war als api.war (dit zorgt voor de juiste context 'api' in de applicatie url)
# cd /opt/bacchus/base/webapps
# cp /opt/bacchus/oude_releases/bacchus-<versie>/bacchus-<versie>.war api.war

-- Pas indien nodig de configuratie aan. Deze vind je in /opt/bacchus/base/conf

-- Start bacchus weer op
# sudo -u root bacchus start

-- tomcat zal nu je api.war uitpakken in een submap /opt/bacchus/base/webapps/api

-- Kijk in de logfiles of het opstarten goed gaat en of er tijden het uitvoeren van de applicatie niets fout gaat.
-- De logfiles vind je in /opt/bacchus/base/logs

De applicatie kun je testen in een browser via de url https://hhs-bacchus-<nr>.42.nl

=============================================================