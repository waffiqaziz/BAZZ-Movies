# BAZZ Movies

A mobile application that provides a comprehensive movie catalog using the TMDB API. BAZZ Movies
offers users the ability to discover, explore, and manage their personal watchlist with a focus on
usability and seamless integration with TMDB. Available exclusively
on [Google Play Store](https://play.google.com/store/apps/details?id=com.bazz.bazz_movies)

## Introduction

This is a non-commercial project designed to showcase movie and TV show. The application is intended
for personal and educational use only.

## Table of Contents (Optional)

- [Installation](#installation)
- [Licenses and Usage](#licenses-and-usage)
- [Features](#features)

## Installation

- Ensure Android Studio is installed.
- Clone the project.
- **Optional** add `google-service.json` file
  from [Firebase Crashlytic](https://firebase.google.com/docs/crashlytics) on `./app` root folder.
- Create or edit `local.properties` file, and add API key without any sign.

```kotlin  
API_KEY = { TMDB_API_KEY }
API_KEY_OMDb = { OBDb_API_KEY }  
```

- Build the project using `Make project` option or by pressing `Ctrl + F9`.

## Licenses and Usage

This project is available for use under the following conditions:

### Non-Commercial Use

The application and its associated assets are provided for non-commercial purposes only. Its free to
use, modify, and explore the code and resources for personal, educational, or research purposes, but
its prohibited for any commercial activities or in a commercial context.

### Third-Party Libraries

Several third-party libraries is used and licensed under their respective licenses

**TMDB API**

- Description: API service that provide movie, TV show or actor images and/or data. This application
  uses the TMDB API to fetch movie and TV show data.
- Attribution: This project uses the TMDB APIs but is not endorsed, certified, or otherwise approved
  by TMDB.
  > > This project already provide attribution to TMDB, specifically using the TMDB logo
  in [About Page](app/src/main/res/layout/activity_about.xml). Any changes are permitted as long as
  provide TMDB attributes.
- Terms of Use: By using this project, you agree to comply with
  the [TMDB API Terms of Use](https://www.themoviedb.org/api-terms-of-use).
- License: Use of the TMDB API is governed by its
  own [Terms of Use](https://www.themoviedb.org/api-terms-of-use), and not by an open-source
  license.
- [TMDB Link](https://www.themoviedb.org/)

TMDB reserves all rights not expressly granted under the Terms of Use.

**OMDb API**

- Description: RESTful web service to obtain movie information.
- Usage: This project is intended for non-commercial purposes only and is not affiliated with OMDb
  API in any official capacity.
- [Term of Use](https://www.omdbapi.com/legal.htm)
- [OMDb API Link](https://www.omdbapi.com/)

**GLide**

- Description: Fast and efficient image loading library for Android.
- [General License by Google](/licences/General-Google-License.txt)
- [GLide Github](https://github.com/bumptech/glide)

#### Libraries under MIT License

**ExpandableTextView**

- An expandable Android TextView written in Kotlin
- [Copyright (c) 2021 Glailton Costa](/licences/MIT-LICENSE-ExpandableTextView.txt)
- [ExpandableTextView Github](https://github.com/glailton/ExpandableTextView)

#### Libraries under Apache 2.0 License

**Country Code Picker (CCP)**

- Android library which provides an easy way to search and select country or country phone code for
  the telephone number.
- [License Text](/licences/Apache-2.0-LICENSE.txt)
- [Country Code Picker Github](https://github.com/hbb20/CountryCodePickerProject)

**okhttp**

- HTTP client for the JVM, Android, and GraalVM.
- [License Text](/licences/Apache-2.0-LICENSE.txt)
- [okhttp Github](https://github.com/square/okhttp)

**retrofit**

- A type-safe HTTP client for Android and the JVM.
- [License Text](/licences/Apache-2.0-LICENSE.txt)
- [retrofit Github](https://github.com/square/retrofit)

**moshi**

- A modern JSON library for Kotlin and Java..
- [License Text](/licences/Apache-2.0-LICENSE.txt)
- [moshi Github](https://github.com/square/moshi)

#### Free to Use Library

**[Country API](https://country.is/)**
An open-source geolocation API that finds a user's country from their IP address. The API is free to
use. It does not require an API key or impose usage quotas.

### Fonts Used

These fonts are licensed under the [Open Font License](https://openfontlicense.org/).

- **[Nunito Sans]**(https://fonts.google.com/specimen/Nunito+Sans) - Designed by Vernon Adams,
  Jacques Le Bailly, Manvel Shmavonyan, Alexei Vanyashin
- **[Exo Font]**(https://fonts.google.com/specimen/Exo) - Designed by Natanael Gama, Robin Mientjes

## Badges

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-green)

## Features

- Integrated with TMDB account
- Save favorites and watchlist on local
- Swipe action for easy to organize between favorite and watchlist
- Guest session (no need login)
- Search movies, tv-series, and actors

Feel free to adjust or expand as needed!
