# REELTRACK

A Spring Boot + Java 21 movie journal using TMDB and an H2 database.

## Run
1. Install Java 21 and Maven.
2. Open this folder (the one containing pom.xml) in a terminal.
3. Set your TMDB v3 API key in `src/main/resources/application.properties` as `tmdb.api-key=YOUR_KEY`.
4. Run `mvn spring-boot:run`.
5. Visit http://localhost:8080.

## Features included
- Account registration/login with BCrypt password hashing and session-based authentication.
- Trending, popular, now-playing, top-rated, search and genre/year discovery.
- Movie details, cast, trailer links and India watch-provider information where TMDB supplies it.
- Per-user persistent Watchlist and Watched collections using H2.
- Movie ratings and written reviews, including updating an existing review.
- Create, view and share public playlists; delete playlists.
- Personal profile statistics.

Movie metadata and availability depend on TMDB API responses and your API key. This project is an educational starter, not a production-hardened social network.
