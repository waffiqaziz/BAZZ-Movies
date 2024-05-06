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
- [TMDB API](https://www.themoviedb.org/)
- [OMDb API](https://www.omdbapi.com/)
- [Country API](https://country.is/)
- [ExpandableTextView](https://github.com/glailton/ExpandableTextView)
- [Country Code Picker (CCP)](https://github.com/hbb20/CountryCodePickerProject)
- [glide](https://bumptech.github.io/glide/)
- [okhttp](https://github.com/square/okhttp)
- [Retrofit](https://github.com/square/retrofit)
- [Moshi](https://github.com/square/moshi)
- [Nunito Sans](https://fonts.google.com/specimen/Nunito+Sans)
- [Paradroid Font](https://www.myfonts.com/collections/paradroid-font-the-northern-block)

## Badges
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-green)

## Features
- Integrated with TMDB account
- Save favorites and watchlist on local
- Swipe action for easy to organize between favorite and watchlist
- Guest session (no need login)
- Search movies, tv-series, and actors


*This project uses the TMDB API but not endorsed or certified by TMDB.*