# Elokuvakirjasto

Elokuvarekisteri, jossa ylläpitäjä hallitsee elokuvia, ohjaajia, näyttelijöitä ja genrejä.
Kuka tahansa voi selata ja hakea elokuvia kirjautumatta.

**Julkaistu sovellus:** https://elokuvakirjasto-hu0l.onrender.com
(Ilmainen Render-palvelu nukahtaa, joten ensimmäinen avaus voi kestää jonkin aikaa.)

## Toiminnot
- Elokuvien selaus ja haku (nimi, ohjaaja, näyttelijä, genre), avoin kaikille
- Elokuvien, ohjaajien, näyttelijöiden ja genrejen lisäys, muokkaus ja poisto (vain ylläpitäjä)
- Kirjautuminen Spring Securityllä, salasanat BCrypt-salattuina
- REST-rajapinta: `/rest/movies`, `/rest/movies/{id}`

## Tekniikat
Spring Boot (MVC), Thymeleaf, JPA + H2, Spring Security, Bean Validation, REST (JSON),
JUnit-testit (smoke-, web layer- ja repository-testit), Docker, Render.

## Tietomalli
Movie *—1 Director, Movie *—1 Genre, Movie *—* Actor (liitostaulu movie_actor), AppUser (kirjautuminen).

## Ajaminen paikallisesti
./mvnw spring-boot:run
Sovellus aukeaa osoitteessa http://localhost:8080. Ylläpitäjän tunnus on `admin`;
salasana luetaan ympäristömuuttujasta `ADMIN_PASSWORD` (oletus paikallisesti `admin123`).