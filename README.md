# BAZZ Movies

![Header](/assets/images/featured-picture.png)

BAZZ Movies is an Android app build with Kotlin, offering comprehensive movie and TV catalog. BAZZ
Movies allows users to discover, explore, and manage their favorite and watchlist with ease.

This project is a non-commercial application for showcasing movies and TV shows, intended for
personal and educational use as part of my portfolio.

*BAZZ Movies use TMDB API, but not endorsed, certified, or approved by TMDB*

## Features

- Integrated with TMDB account
- Save favorites and watchlist on local
- Swipe action for easy to organize between favorite and watchlist
- Guest session (no need to login)
- Search movies, tv-series, and actors
- Detailed information

## Download

<a href="https://play.google.com/store/apps/details?id=com.bazz.bazz_movies" target="_blank">
<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" width=200  alt="Get BAZZ Movies on Google Play"/>
</a>

## Installation

1. Install Android Studio.
2. Clone the project.
3. (Optional)
   Configure [Crashlytics](https://firebase.google.com/docs/crashlytics/get-started?platform=android)
   and [Analytics](https://firebase.google.com/docs/analytics/get-started?platform=android)
4. Get your api on [TMDB](https://developer.themoviedb.org/docs/getting-started)
   and [OMDb](https://www.omdbapi.com/apikey.aspx).
5. Create or edit `local.properties` and put your API keys:
   ```properties
   API_KEY = { TMDB_API_KEY }
   API_KEY_OMDb = { OMDB_API_KEY }
   ```
6. Build the project (`Ctrl + F9`).
7. Run with your virtual or phone devices

## Licenses and Usage

This application is released under the [MIT License](LICENSE).

[![license](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

### Third-Party Libraries

1. TMDB API: Provides movie and TV data.
   See [TMDB Terms](https://www.themoviedb.org/api-terms-of-use).
2. OMDb API: RESTful service for movie info. See [OMDb Terms](https://www.omdbapi.com/legal.htm).
3. GLide: Image loading library - [GitHub](https://github.com/bumptech/glide).
4. ExpandableTextView: An expandable Android TextView (MIT
   License) - [GitHub](https://github.com/glailton/ExpandableTextView).
5. Country Picker Android: (Apache
   2.0) - [GitHub](https://github.com/waffiqaziz/country-picker-android).
6. okhttp, retrofit, moshi: (Apache 2.0) - [GitHub](https://github.com/square).
7. country: IP-to-country geolocation API - [Github](https://github.com/hakanensari/country).

### Full License Texts

- [BAZZ Movies License](LICENSE)
- [ExpandableTextView License](/licences/MIT-LICENSE-ExpandableTextView.txt)
- [Apache 2.0 License](/licences/Apache-2.0-LICENSE.txt)
- [Glide License](/licences/General-Google-License.txt)

### Fonts

Licensed under [Open Font License](https://openfontlicense.org/):

- [Nunito Sans](https://fonts.google.com/specimen/Nunito+Sans)
- [Exo Font](https://fonts.google.com/specimen/Exo)
