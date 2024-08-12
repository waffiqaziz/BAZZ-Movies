# BAZZ Movies

## Mobile App Movie Catalog
Elevate your movie experience with BAZZ Movies. Discover, favorite, and curate your personal watchlist with ease, ensuring you never miss a cinematic gem. Explore detailed information about each film and effortlessly add them to your favorites or watchlist, transforming the way you enjoy movies on the go. Get it exclusively on [Play Store](https://play.google.com/store/apps/details?id=com.bazz.bazz_movies)

## Table of Contents (Optional)
- [Installation](#installation)
- [Credits](#credits)
- [Features](#features)

## Installation
- Ensure Android Studio is installed.
- Clone the project.
- **Optional** add `google-service.json` file from [Firebase Crashlytic](https://firebase.google.com/docs/crashlytics) on ./app root folder.
- Create or edit `local.properties` file, and add API key without any sign.
```kotlin  
API_KEY= {TMDB_API_KEY}  
API_KEY_OMDb= {OBDb_API_KEY}  
```
- Build the project using the "Make project" option or by pressing Ctrl + F9.

## Credits
This project makes use of third parties, which are licensed under their respective open source licenses
- **[TMDB API]**(https://www.themoviedb.org/) - API service that provide movie, TV show or actor images and/or data.
- **[OMDb API]**(https://www.omdbapi.com/) - RESTful web service to obtain movie information.
- **[Country API]**(https://country.is/) - An open-source geolocation API that finds a user's country from their IP address.
- **[ExpandableTextView]**(https://github.com/glailton/ExpandableTextView) - An expandable Android TextView written in Kotlin
- **[Country Code Picker (CCP)]**(https://github.com/hbb20/CountryCodePickerProject) - 
- **[glide]**(https://bumptech.github.io/glide/) - Fast and efficient image loading library for Android.
- **[okhttp]**(https://github.com/square/okhttp) - HTTP client for the JVM, Android, and GraalVM.
- **[Retrofit]**(https://github.com/square/retrofit) - A type-safe HTTP client for Android and the JVM
- **[Moshi]**(https://github.com/square/moshi) - A modern JSON library for Kotlin and Java.
- **[Nunito Sans]**(https://fonts.google.com/specimen/Nunito+Sans) - Designed by Vernon Adams, Jacques Le Bailly, Manvel Shmavonyan, Alexei Vanyashin
- **[Paradroid Font]**(https://www.myfonts.com/collections/paradroid-font-the-northern-block) - A modern humanist sans-serif font created by The Northern Block
  
## Badges
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-green)

## Features
- Integrated with TMDB account
- Save favorites and watchlist on local
- Swipe action for easy to organize between favorite and watchlist
- Guest session (no need login)
- Search movies, tv-series, and actors


*This project uses the TMDB API but not endorsed or certified by TMDB.*
