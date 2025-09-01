<div align="center">

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="docs/images/bazz-movies-light.svg">
  <source media="(prefers-color-scheme: light)" srcset="docs/images/bazz-movies.svg">
  <img alt="BAZZ Movies" src="docs/images/bazz-movies.svg">
</picture>

### Movies Catalog Android App

#### [**Website**][WEB] • [**Play Store**][PLAY-STORE] • [**FAQ**][FAQ]

---

<!-- Badge -->
<!-- Release & Activity -->
[![Release][BADGE-RELEASE]][RELEASE]
[![Change Log][BADGE-CHANGE-LOG]][CHANGE-LOG]
[![Activity][BADGE-ACTIVITY]][ACTIVITY]
[![Issue][BADGE-ISSUE]][ISSUE]
[![Average time to resolve an issue][BADGE_IS_MAINTAINED]][IS_MAINTAINED]


<!-- Android SDK -->
[![Android SDK][BADGE-ANDROID-SDK]][ANDROID-SDK]
[![View System][BADGE-UI]][UI]
[![Hilt][BADGE-HILT]][HILT]
[![Room][BADGE-ROOM]][ROOM]
[![Coroutines][BADGE-COROUTINES]][COROUTINES]

<!-- Build & Testing -->
[![Build][BADGE-BUILD]][BUILD]
[![Testing][BADGE-TESTING]][TESTING]
[![QodeQL][BADGE-CODEQL]][CODEQL]

<!-- Code Quality -->
[![Codecov][BADGE-CODECOV]][CODECOV]
[![Sonarqube][BADGE-SONARQUBE]][SONARQUBE]
[![Codacy][BADGE-CODACY]][CODACY]
[![Qlty][BADGE-QLTY]][QLTY]

<!-- Repository Info -->
[![OpenSSF][BADGE-OPENSSF]][OPENSSF]
[![License][BADGE-LICENSE]][LICENSE]
[![Repo Size][BADGE-SIZE]][SIZE]
[![Contributions][BADGE-CONTRIBUTION]][CONTRIBUTION]

[<img src="https://i.postimg.cc/tJJ4sBkd/Thumbnail-Blur.png" width="550"/>](https://i.postimg.cc/tJJ4sBkd/Thumbnail-Blur.png)

</div>

## 🚀 About

BAZZ Movies is an Android app build with Kotlin, offering comprehensive movie and TV catalog. BAZZ
Movies allows users to discover, explore, and manage their favorite and watchlist with ease.

This project is a non-commercial application for showcasing movies and TV shows, intended for
personal and educational use as part of my portfolio.

**_BAZZ Movies use TMDB API, but not endorsed, certified, or approved by TMDB_**

## 🌟 Features

- Integrated with [TMDB](https://themoviedb.org/) account
- Save favorites and watchlist on local
- Swipe action for easy to organize between favorite and watchlist
- Guest session (no need to login)
- Search movies, tv-series, and actors
- Detailed information

## ⬇️ Download

BAZZ Movies available downloaded via Google Play Store

<a href="https://play.google.com/store/apps/details?id=com.bazz.bazz_movies" target="_blank">
<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" width=200  alt="Get BAZZ Movies on Google Play"/>
</a>

## 📝 Project Setup Instructions

1. **Install Android Studio**

   - Download and install [Android Studio](https://developer.android.com/studio).
   - Minimum required version: **Narwhal Feature Drop | 2025.1.2**.

2. **Install JDK 21**

   Ensure that **JDK 21** is installed and [properly configured in your environment](https://developer.android.com/build/jdks#jdk-config-in-studio).

3. **Clone the Project**

   Clone this repository to your local machine using Git.

4. **(Optional) Set up Firebase Services**

   - [Crashlytics](https://firebase.google.com/docs/crashlytics/get-started?platform=android)
   - [Analytics](https://firebase.google.com/docs/analytics/get-started?platform=android)

5. **Obtain API Keys**

   - [TMDB](https://developer.themoviedb.org/docs/getting-started)
   - [OMDb](https://www.omdbapi.com/apikey.aspx)

6. **Configure API Keys**

   Create or update your `local.properties` file in the project root and add the following lines:

   ```properties
   TMDB_API_KEY = { TMDB_API_KEY }
   OMDB_API_KEY = { OMDB_API_KEY }
   ```

7. **Build the Project**

   Use **Ctrl + F9** (or `Build > Make Project`) in Android Studio.

8. **Run the Project**

   Launch the project on either an emulator or a connected physical device.


## 🛠️ Architecture

**BAZZ Movies** app follows the
[official architecture guidance](https://developer.android.com/topic/architecture)
and is described in detail in the
[BAZZ Movies modularization](docs/BAZZMoviesModularization.md).


## 🧪 Testing

Please read this [page](/docs/BAZZMoviesTesting.md).

## 🤝 How to Contribute

Please read this [page](CONTRIBUTING.md).

## 📜 Licenses and Usage

This application is released under the [Apache Version 2.0 License](LICENSE).

```txt
Copyright (C) 2024-2025 Waffiq Aziz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

### Note

- BAZZ Movies uses third-party API to function.
- Developers must secure their own API key and adhere to the API's terms of service.

### Third-Party Libraries

1. TMDB API: Provides movie and TV data.
   See [TMDB Terms](https://www.themoviedb.org/api-terms-of-use).
2. OMDb API: RESTful service for movie info. See [OMDb Terms](https://www.omdbapi.com/legal.htm).
3. GLide: Image loading library - [GitHub](https://github.com/bumptech/glide).
4. ExpandableTextView: An expandable Android
   TextView - [GitHub](https://github.com/glailton/ExpandableTextView).
5. Shimmer Android: Library shimmering
   effects - [GitHub](https://github.com/facebookarchive/shimmer-android)
6. Country Picker Android: (Apache
   2.0) - [GitHub](https://github.com/waffiqaziz/country-picker-android).
7. okhttp, retrofit, moshi: (Apache 2.0) - [GitHub](https://github.com/square).
8. country: IP-to-country geolocation API - [Github](https://github.com/hakanensari/country).

### Fonts

Licensed under [Open Font License](https://openfontlicense.org/):

- [Nunito Sans](https://fonts.google.com/specimen/Nunito+Sans)
- [Exo Font](https://fonts.google.com/specimen/Exo)

<!-- LINK -->

[WEB]: https://waffiqaziz.github.io/bazzmovies
[PLAY-STORE]: https://play.google.com/store/apps/details?id=com.bazz.bazz_movies
[FAQ]: https://docs.google.com/document/d/1HNrj5i3Rnpr50Ldwgfz5ODpaJoWF17TXIop7xwtXkiU/edit?usp=sharing

[BADGE-ANDROID-SDK]: https://img.shields.io/badge/Android%20SDK-23%20→%2036-brightgreen
[BADGE-UI]: https://img.shields.io/badge/View%20Binding-using-green?logo=android
[BADGE-HILT]: https://img.shields.io/badge/Hilt-DI-blue?logo=dagger
[BADGE-ROOM]: https://img.shields.io/badge/Room-DB-FF6F00?logo=android
[BADGE-COROUTINES]: https://img.shields.io/badge/Coroutines-supported-009688
[ANDROID-SDK]: https://developer.android.com/about/versions
[UI]: https://developer.android.com/topic/libraries/view-binding
[HILT]: https://dagger.dev/hilt/
[ROOM]: https://developer.android.com/training/data-storage/room
[COROUTINES]: https://developer.android.com/kotlin/coroutines

[BADGE-RELEASE]: https://img.shields.io/github/v/release/waffiqaziz/BAZZ-Movies
[BADGE-CHANGE-LOG]:https://img.shields.io/badge/change%20log-%E2%96%A4-yellow.svg
[BADGE-ACTIVITY]: https://img.shields.io/github/commit-activity/m/waffiqaziz/BAZZ-Movies
[BADGE-ISSUE]: https://img.shields.io/github/issues/waffiqaziz/BAZZ-Movies
[BADGE_IS_MAINTAINED]: https://isitmaintained.com/badge/resolution/waffiqaziz/BAZZ-Movies.svg
[BADGE-BUILD]: https://github.com/waffiqaziz/BAZZ-Movies/actions/workflows/build.yml/badge.svg
[BADGE-TESTING]: https://github.com/waffiqaziz/BAZZ-Movies/actions/workflows/android_test.yml/badge.svg
[BADGE-CODEQL]: https://github.com/waffiqaziz/BAZZ-Movies/actions/workflows/codeql.yml/badge.svg
[BADGE-CODECOV]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/graph/badge.svg?token=4SV6Z18HKZ
[BADGE-SONARQUBE]: https://sonarcloud.io/api/project_badges/measure?project=waffiqaziz_BAZZ-Movies&metric=alert_status
[BADGE-CODACY]: https://app.codacy.com/project/badge/Grade/58305a0496ad44a4bd73f2cc052269ff
[BADGE-QLTY]: https://qlty.sh/badges/81837a70-f262-4ee0-818a-549e78248a72/maintainability.svg
[BADGE-OPENSSF]: https://www.bestpractices.dev/projects/10186/badge
[BADGE-LICENSE]: https://img.shields.io/github/license/waffiqaziz/BAZZ-Movies
[BADGE-SIZE]: https://img.shields.io/github/repo-size/waffiqaziz/BAZZ-Movies
[BADGE-CONTRIBUTION]: https://img.shields.io/badge/contributions-welcome-9EDF9C.svg

[RELEASE]: https://github.com/waffiqaziz/BAZZ-Movies/releases
[CHANGE-LOG]: https://github.com/waffiqaziz/BAZZ-Movies/releases
[ACTIVITY]: https://github.com/waffiqaziz/BAZZ-Movies/pulse
[ISSUE]: https://github.com/waffiqaziz/BAZZ-Movies/issues
[BUILD]: https://github.com/waffiqaziz/BAZZ-Movies/actions/workflows/build.yml
[TESTING]: https://github.com/waffiqaziz/BAZZ-Movies/actions/workflows/android_test.yml
[IS_MAINTAINED]: http://isitmaintained.com/project/waffiqaziz/BAZZ-Movies
[CODEQL]: https://github.com/waffiqaziz/BAZZ-Movies/actions/workflows/codeql.yml
[CODECOV]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies
[SONARQUBE]: https://sonarcloud.io/project/overview?id=waffiqaziz_BAZZ-Movies
[CODACY]: https://app.codacy.com/gh/waffiqaziz/BAZZ-Movies/dashboard
[QLTY]: https://qlty.sh/gh/waffiqaziz/projects/BAZZ-Movies
[OPENSSF]: https://www.bestpractices.dev/projects/10186
[LICENSE]: https://www.apache.org/licenses/LICENSE-2.0
[SIZE]: #
[CONTRIBUTION]: https://github.com/waffiqaziz/BAZZ-Movies/blob/main/CONTRIBUTING.md
