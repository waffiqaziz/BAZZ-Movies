package com.waffiq.bazz_movies.core.network.testutils

import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.RatingsItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AvatarItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AvatarTMDbResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.GravatarResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.GenresItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.ProductionCountriesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew.MovieTvCastItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew.MovieTvCrewItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates.ReleaseDatesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates.ReleaseDatesItemValueResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.ContentRatingsItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.CreatedByItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.LastEpisodeToAirResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.NetworksItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.ProductionCompaniesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.SeasonsItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.SpokenLanguagesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.videomedia.VideoItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.watchproviders.CountryProviderDataResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.watchproviders.ProviderResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CastItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CrewItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ProfilesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.KnownForItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.StatedResponse

@Suppress("LargeClass")
object DataDumpManager {

  val movieDump1 = ResultItemResponse(
    id = 278,
    title = "The Shawshank Redemption",
    originalTitle = "The Shawshank Redemption",
    originalName = "The Shawshank Redemption",
    overview = """
      Imprisoned in the 1940s for the double murder of his wife and her lover, upstanding banker 
      Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills 
      to work for an amoral warden. During his long stretch in prison, Dufresne comes to be 
      admired by the other inmates -- including an older prisoner named Red -- for his integrity 
      and unquenchable sense of hope.
    """.trimIndent(),
    genreIds = listOf(18, 80),
    posterPath = "/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg",
    backdropPath = "/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg",
    popularity = 174.998,
    voteAverage = 8.708f,
    voteCount = 27450,
    releaseDate = "1994-09-23",
    adult = false,
    originalLanguage = "en",
    video = false
  )

  val movieDump2 = ResultItemResponse(
    id = 238,
    title = "The Godfather",
    originalTitle = "The Godfather",
    overview = """
      Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime 
      family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his 
      life, his youngest son, Michael steps in to take care of the would-be killers, launching a 
      campaign of bloody revenge.
    """.trimIndent(),
    genreIds = listOf(18, 80),
    posterPath = "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
    backdropPath = "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
    popularity = 220.965,
    voteAverage = 8.7f,
    voteCount = 20834,
    releaseDate = "1972-03-14",
    adult = false,
    originalLanguage = "en",
    video = false
  )

  val movieDump3 = ResultItemResponse(
    id = 240,
    title = "The Godfather Part II",
    originalTitle = "The Godfather Part II",
    overview = """
      In the continuing saga of the Corleone crime family, a young Vito Corleone grows up in Sicily 
      and in 1910s New York. In the 1950s, Michael Corleone attempts to expand the family business 
      into Las Vegas, Hollywood and Cuba.
    """.trimIndent(),
    genreIds = listOf(18, 80),
    posterPath = "/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg",
    backdropPath = "/kGzFbGhp99zva6oZODW5atUtnqi.jpg",
    popularity = 88.883,
    voteAverage = 8.6f,
    voteCount = 12577,
    releaseDate = "1974-12-20",
    adult = false,
    originalLanguage = "en",
    video = false
  )

  val movieDump4 = ResultItemResponse(
    id = 424,
    title = "Schindler's List",
    originalTitle = "Schindler's List",
    overview = """
      The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the 
      Nazis while they worked as slaves in his factory during World War II.
    """.trimIndent(),
    genreIds = listOf(18, 36, 10752),
    posterPath = "/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg",
    backdropPath = "/zb6fM1CX41D9rF9hdgclu0peUmy.jpg",
    popularity = 90.95,
    voteAverage = 8.6f,
    voteCount = 16001,
    releaseDate = "1993-12-15",
    adult = false,
    originalLanguage = "en",
    video = false
  )

  val movieDump5 = ResultItemResponse(
    id = 389,
    title = "12 Angry Men",
    originalTitle = "12 Angry Men",
    overview = """
      The defense and the prosecution have rested and the jury is filing into the jury room to 
      decide if a young Spanish-American is guilty or innocent of murdering his father. What begins 
      as an open and shut case soon becomes a mini-drama of each of the jurors' prejudices and 
      preconceptions about the trial, the accused, and each other.
    """.trimIndent(),
    genreIds = listOf(18),
    posterPath = "/ow3wq89wM8qd5X7hWKxiRfsFf9C.jpg",
    backdropPath = "/bxgTSUenZDHNFerQ1whRKplrMKF.jpg",
    popularity = 58.538,
    voteAverage = 8.5f,
    voteCount = 8775,
    releaseDate = "1957-04-10",
    adult = false,
    originalLanguage = "en",
    video = false
  )

  val movieDump6 = ResultItemResponse(
    id = 823219,
    title = "Flow",
    originalTitle = "Straume",
    overview = """
      A solitary cat, displaced by a great flood, finds refuge on a boat with various species and 
      must navigate the challenges of adapting to a transformed world together.
    """.trimIndent(),
    posterPath = "/imKSymKBK7o73sajciEmndJoVkR.jpg",
    mediaType = "movie",
    adult = false,
    originalLanguage = "lv",
    genreIds = listOf(16, 14, 12),
    popularity = 1109.247,
    releaseDate = "2024-01-30",
    video = false,
    voteAverage = 8.4f,
    voteCount = 558
  )

  val movieDump7 = ResultItemResponse(
    id = 402431,
    title = "Wicked",
    originalTitle = "Wicked",
    overview = """
      In the land of Oz, ostracized and misunderstood green-skinned Elphaba is forced to share a 
      room with the popular aristocrat Glinda at Shiz University, and the two's unlikely friendship 
      is tested as they begin to fulfill their respective destinies as Glinda the Good and the 
      Wicked Witch of the West.
    """.trimIndent(),
    posterPath = "/2E1x1qcHqGZcYuYi4PzVZjzg8IV.jpg",
    mediaType = "movie",
    adult = false,
    originalLanguage = "en",
    genreIds = listOf(18, 10749, 14),
    popularity = 2440.1,
    releaseDate = "2024-11-20",
    video = false,
    voteAverage = 7.3f,
    voteCount = 1071
  )

  val tvShowDump1 = ResultItemResponse(
    id = 93405,
    name = "Squid Game",
    originalTitle = null,
    overview = """
      Hundreds of cash-strapped players accept a strange invitation to compete in children's games. 
      Inside, a tempting prize awaits — with deadly high stakes.
    """.trimIndent(),
    posterPath = "/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg",
    mediaType = "tv",
    adult = false,
    originalLanguage = "ko",
    genreIds = listOf(10759, 9648, 18),
    popularity = 10243.452,
    firstAirDate = "2021-09-17",
    voteAverage = 7.842f,
    voteCount = 14755,
    originCountry = listOf("KR")
  )

  val tvShowDump2 = ResultItemResponse(
    id = 69316,
    title = "Eternal Love",
    originalTitle = "三生三世十里桃花",
    overview = """
      Three hundred years ago, Bai Qian stood on the Zhu Xian Terrace, turned around and jumped off 
      without regret. Ye Hua stood by the bronze mirror to witness with his own eyes her death. 
      Three hundred years later, in the East Sea Dragon Palace, the two meet unexpectedly. Another 
      lifetime another world, after suffering betrayal Bai Qian no longer feels anything, yet she 
      can't seem to comprehend Ye Hua's actions. Three lives three worlds, her and him, are they 
      fated to love again?
    """.trimIndent(),
    posterPath = "/c7Pfx7dQiRsi1rU8N9s05gHnAkI.jpg",
    mediaType = "tv",
    adult = false,
    originalLanguage = "zh",
    genreIds = listOf(18, 10765),
    popularity = 249.411,
    firstAirDate = "2017-01-30",
    voteAverage = 7.6f,
    voteCount = 48
  )

  val tvShowDump3 = ResultItemResponse(
    id = 46437,
    title = "Kingdom",
    originalTitle = "キングダム",
    overview = """
      In the Warring States Period of ancient China (475–221 BCE), Shin and Hyou are war-orphans in 
      the kingdom of Qin. They dream of one day proving themselves on the battlefield. One day, 
      however, Hyou is taken to the palace by a minister. Winding up on the losing side of a 
      power-struggle, Hyou manages to return to the village, barely alive. Shin then meets a boy who 
      closely resembles Hyou, Ei Sei. For now he is the king of Qin; later he will become the emperor 
      Shi Huangdi.
    """.trimIndent(),
    posterPath = "/dehuJJkKo50nYvCYppigrWejqLe.jpg",
    mediaType = "tv",
    adult = false,
    originalLanguage = "ja",
    genreIds = listOf(10759, 16, 18),
    popularity = 1222.313,
    firstAirDate = "2012-06-04",
    voteAverage = 7.8f,
    voteCount = 38
  )

  val knownForItemResponseDump1 = KnownForItemResponse(
    backdropPath = "/zZKmEmRDc7ZjFucPm2UGNgDRlDc.jpg",
    id = 353486,
    title = "Jumanji: Welcome to the Jungle",
    originalTitle = "Jumanji: Welcome to the Jungle",
    overview = """
      Four teenagers in detention discover an old video game console with a game they’ve never heard 
      of. When they decide to play, they are immediately sucked into the jungle world of Jumanji in 
      the bodies of their avatars. They’ll have to complete the adventure of their lives filled with 
      fun, thrills and danger or be stuck in the game forever!
    """.trimIndent(),
    posterPath = "/pSgXKPU5h6U89ipF7HBYajvYt7j.jpg",
    mediaType = "movie",
    adult = false,
    originalLanguage = "en",
    genreIds = listOf(12, 35, 14),
    popularity = 65.364,
    releaseDate = "2017-12-09",
    video = false,
    voteAverage = 6.823,
    voteCount = 13648
  )

  val knownForItemResponseDump2 = KnownForItemResponse(
    backdropPath = "/zTxHf9iIOCqRbxvl8W5QYKrsMLq.jpg",
    id = 512200,
    title = "Jumanji: The Next Level",
    originalTitle = "Jumanji: The Next Level",
    overview = """
      As the gang return to Jumanji to rescue one of their own, they discover that nothing is as 
      they expect. The players will have to brave parts unknown and unexplored in order to escape 
      the world’s most dangerous game.
    """.trimIndent(),
    posterPath = "/4kh9dxAiClS2GMUpkRyzGwpNWWX.jpg",
    mediaType = "movie",
    adult = false,
    originalLanguage = "en",
    genreIds = listOf(12, 35, 14),
    popularity = 92.641,
    releaseDate = "2019-12-04",
    firstAirDate = "2019-12-04",
    originCountry = listOf("US"),
    originalName = "Jumanji: The Next Level",
    name = "Jumanji: The Next Level",
    video = false,
    voteAverage = 6.9,
    voteCount = 8692
  )

  val knownForItemResponseDump3 = KnownForItemResponse(
    backdropPath = "/mMoG4nPSDupXIXOwVvpexZY2W0N.jpg",
    id = 254128,
    title = "San Andreas",
    originalTitle = "San Andreas",
    overview = """
      In the aftermath of a massive earthquake in California, a rescue-chopper pilot makes a 
      dangerous journey across the state in order to rescue his estranged daughter.
    """.trimIndent(),
    posterPath = "/2Gfjn962aaFSD6eST6QU3oLDZTo.jpg",
    mediaType = "movie",
    adult = false,
    originalLanguage = "en",
    genreIds = listOf(28, 18, 53),
    popularity = 79.25,
    releaseDate = "2015-05-27",
    video = false,
    voteAverage = 6.2,
    voteCount = 8557
  )

  val personDump1 = ResultsItemSearchResponse(
    id = 18918,
    name = "Dwayne Johnson",
    originalName = "Dwayne Johnson",
    overview = "Biography",
    mediaType = "person",
    adult = false,
    popularity = 102.851,
    knownForDepartment = "Acting",
    profilePath = "/kuqFzlYMc2IrsOyPznMd1FroeGq.jpg",
    listKnownFor = listOf(
      knownForItemResponseDump1,
      knownForItemResponseDump2,
      knownForItemResponseDump3
    )
  )

  val movieSearchDump = ResultsItemSearchResponse(
    id = 19995,
    title = "Avatar",
    originalTitle = "Avatar",
    overview = """
      In the 22nd century, a paraplegic Marine is dispatched to the moon Pandora on a unique 
      mission, but becomes torn between following orders and protecting an alien civilization.
    """.trimIndent(),
    posterPath = "/kyeqWdyUXW608qlYkRqosgbbJyK.jpg",
    backdropPath = "/vL5LR6WdxWPjLPFRLe133jXWsh5.jpg",
    mediaType = "movie",
    adult = false,
    originalLanguage = "en",
    listGenreIds = listOf(28, 12, 14, 878),
    popularity = 185.268,
    releaseDate = "2009-12-15",
    firstAirDate = "2009-12-15",
    listOriginCountry = listOf("US"),
    video = false,
    voteAverage = 7.583,
    voteCount = 31734.0,
  )

  val movieTvCastItemResponseDump = MovieTvCastItemResponse(
    castId = 13,
    character = "Momo",
    gender = 1,
    creditId = "6638f569ae38430122ca1143",
    knownForDepartment = "Acting",
    originalName = "Alexa Goodall",
    popularity = 3.822,
    name = "Alexa Goodall",
    profilePath = "/39Pk0wdjD2TC4QgnrODxWD8bubH.jpg",
    id = 3771374,
    adult = false,
    order = 0
  )

  val movieTvCrewItemResponseDump = MovieTvCrewItemResponse(
    gender = 0,
    creditId = "64fc09ebf85958011ca070b4",
    knownForDepartment = "Visual Effects",
    originalName = "Frank Schlegel",
    popularity = 0.001,
    name = "Frank Schlegel",
    profilePath = null,
    id = 3014542,
    adult = false,
    department = "Visual Effects",
    job = "VFX Supervisor"
  )

  val movieTvCreditsResponseDump1 = MovieTvCreditsResponse(
    id = 1175842,
    cast = listOf(
      movieTvCastItemResponseDump,
      MovieTvCastItemResponse(
        castId = 14,
        character = "",
        gender = 2,
        creditId = "6638f58a873f00012804079f",
        knownForDepartment = "Acting",
        originalName = "Martin Freeman",
        popularity = 47.068,
        name = "Martin Freeman",
        profilePath = "/kx61L8oImUOJjQqwgCmEQ5kC3El.jpg",
        id = 7060,
        adult = false,
        order = 1
      ),
      MovieTvCastItemResponse(
        castId = 15,
        character = "",
        gender = 1,
        creditId = "6638f5975a469001223eb49a",
        knownForDepartment = "Acting",
        originalName = "Laura Haddock",
        popularity = 35.612,
        name = "Laura Haddock",
        profilePath = "/6kRUvA7N3pMzznG9PdiqCOvxhGQ.jpg",
        id = 209578,
        adult = false,
        order = 2
      )
    ),
    crew = listOf(
      movieTvCrewItemResponseDump,
      MovieTvCrewItemResponse(
        gender = 2,
        creditId = "64fc0a08f8595800adc950df",
        knownForDepartment = "Directing",
        originalName = "Christian Ditter",
        popularity = 2.975,
        name = "Christian Ditter",
        profilePath = "/o8waU0Go2YEgq6YFcDQf5dph5zP.jpg",
        id = 63820,
        adult = false,
        department = "Directing",
        job = "Director"
      ),
      MovieTvCrewItemResponse(
        gender = 2,
        creditId = "64fc0a2af85958013a8d8b8a",
        knownForDepartment = "Production",
        originalName = "Christian Becker",
        popularity = 1.915,
        name = "Christian Becker",
        profilePath = "/pM326e8d4mFMXLE3BBHqUQlr9jI.jpg",
        id = 4930,
        adult = false,
        department = "Production",
        job = "Producer"
      )
    )
  )

  val movieTvCreditsResponseDump2 = MovieTvCreditsResponse(
    id = 246,
    cast = listOf(
      MovieTvCastItemResponse(
        adult = false,
        gender = 2,
        id = 60251,
        knownForDepartment = "Acting",
        name = "Zach Tyler Eisen",
        originalName = "Zach Tyler Eisen",
        popularity = 10.086,
        profilePath = "/ifHd2Yoovlvu6FFEIxUsXVyrYUf.jpg",
        character = "Aang (voice)",
        creditId = "5253483519c29579400de740",
        order = 0
      ),
      MovieTvCastItemResponse(
        adult = false,
        gender = 1,
        id = 52404,
        knownForDepartment = "Acting",
        name = "Mae Whitman",
        originalName = "Mae Whitman",
        popularity = 65.903,
        profilePath = "/oy89WyETM8T3Z4aRJb2EnmqZ7A3.jpg",
        character = "Katara (voice)",
        creditId = "5253483519c29579400de75e",
        order = 1
      ),
      MovieTvCastItemResponse(
        adult = false,
        gender = 2,
        id = 60230,
        knownForDepartment = "Acting",
        name = "Jack De Sena",
        originalName = "Jack De Sena",
        popularity = 12.877,
        profilePath = "/i9VlMsPol6XIicnRZRiwmYSyE4P.jpg",
        character = "Sokka (voice)",
        creditId = "5253483719c29579400de85c",
        order = 2
      ),
    ),
    crew = listOf(
      MovieTvCrewItemResponse(
        adult = false,
        gender = 0,
        id = 1447300,
        knownForDepartment = "Crew",
        name = "Dao Le",
        originalName = "Dao Le",
        popularity = 2.219,
        profilePath = null,
        creditId = "553fcf2e92514132cb000266",
        department = "Editing",
        job = "Editorial Manager"
      ),
      MovieTvCrewItemResponse(
        adult = false,
        gender = 2,
        id = 1450350,
        knownForDepartment = "Writing",
        name = "Heiko von Drengenberg",
        originalName = "Heiko von Drengenberg",
        popularity = 1.422,
        profilePath = null,
        creditId = "5523e3659251416d4a00474b",
        department = "Directing",
        job = "Layout"
      ),
      MovieTvCrewItemResponse(
        adult = false,
        gender = 2,
        id = 1190518,
        knownForDepartment = "Production",
        name = "Bryan Konietzko",
        originalName = "Bryan Konietzko",
        popularity = 2.821,
        profilePath = "/5lPnGvtATjmPakR96dqPW3v4u8q.jpg",
        creditId = "5253483e19c29579400dea3a",
        department = "Production",
        job = "Executive Producer"
      ),
    )
  )

  val omdbDetailsResponseDump = OMDbDetailsResponse(
    metascore = "67",
    boxOffice = "$684,075,767",
    website = "N/A",
    imdbRating = "7.5",
    imdbVotes = "519,728",
    ratings = listOf(
      RatingsItemResponse(
        value = "7.5/10",
        source = "Internet Movie Database"
      ),
      RatingsItemResponse(
        value = "76%",
        source = "Rotten Tomatoes"
      ),
      RatingsItemResponse(
        value = "67/100",
        source = "Metacritic"
      )
    ),
    runtime = "192 min",
    language = "English",
    rated = "PG-13",
    production = "N/A",
    released = "16 Dec 2022",
    imdbID = "tt1630029",
    plot = """
      Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a 
      familiar threat returns to finish what was previously started, Jake must work with Neytiri 
      and the army of the Na'vi race to protect their home.
    """.trimIndent(),
    director = "James Cameron",
    title = "Avatar: The Way of Water",
    actors = "Sam Worthington, Zoe Saldana, Sigourney Weaver",
    response = "True",
    type = "movie",
    awards = "Won 1 Oscar. 75 wins & 152 nominations total",
    dVD = "N/A",
    year = "2022",
    poster = "https://m.media-amazon.com/images/M/poster@._V1_SX300.jpg",
    country = "United States",
    genre = "Action, Adventure, Fantasy",
    writer = "James Cameron, Rick Jaffa, Amanda Silver"
  )

  val videoItemMovieResponseDump1 = VideoItemResponse(
    site = "YouTube",
    size = 1080,
    iso31661 = "US",
    name = "'Oppenheimer' Wins Best Cinematography | 96th Oscars (2024)",
    official = true,
    id = "66187a326f31af01639a3ff5",
    publishedAt = "2024-04-11T19:00:07.000Z",
    type = "Featurette",
    iso6391 = "en",
    key = "O_hKC3gRvzw"
  )

  val videoItemMovieResponseDump2 = VideoItemResponse(
    site = "YouTube",
    size = 1080,
    iso31661 = "US",
    name = "'Oppenheimer' Wins Best Film Editing | 96th Oscars (2024)",
    official = true,
    id = "66187a3af9aa47017d33e12c",
    publishedAt = "2024-04-10T19:00:06.000Z",
    type = "Featurette",
    iso6391 = "en",
    key = "YkPvQ2hqnMY"
  )

  val videoItemMovieResponseDump3 = VideoItemResponse(
    site = "YouTube",
    size = 1080,
    iso31661 = "US",
    name = "Cillian Murphy | Best Actor in a Leading Role | Oscars 2024 Press Room Speech",
    official = true,
    id = "65ef60451b7c590162b0e52d",
    publishedAt = "2024-03-11T17:25:06.000Z",
    type = "Featurette",
    iso6391 = "en",
    key = "EJdFH02RnFQ"
  )

  val videoMovieResponseDump = VideoResponse(
    id = 872585,
    results = listOf(
      videoItemMovieResponseDump1,
      videoItemMovieResponseDump2,
      videoItemMovieResponseDump3
    )
  )

  val videoItemTvResponseDump1 = VideoItemResponse(
    site = "YouTube",
    size = 1080,
    iso31661 = "US",
    name = "JUJUTSU KAISEN Opening | Kaikai Kitan by Eve",
    official = true,
    id = "6748cbebc9a236e51cf3c408",
    publishedAt = "2020-11-20T19:00:02.000Z",
    type = "Opening Credits",
    iso6391 = "en",
    key = "GwaRztMaoY0"
  )

  val videoItemTvResponseDump2 = VideoItemResponse(
    site = "YouTube",
    size = 1080,
    iso31661 = "US",
    name = "Official Trailer 3 [Subtitled]",
    official = true,
    id = "5f7430d0156cc70036480af6",
    publishedAt = "2020-09-30T07:00:08.000Z",
    type = "Trailer",
    iso6391 = "en",
    key = "VpO6APNqY1c"
  )

  val videoItemTvResponseDump3 = VideoItemResponse(
    site = "YouTube",
    size = 1080,
    iso31661 = "US",
    name = "Official Trailer [Subtitled]",
    official = true,
    id = "5f34511555c926003351bca3",
    publishedAt = "2020-08-12T16:30:05.000Z",
    type = "Trailer",
    iso6391 = "en",
    key = "pkKu9hLT-t8"
  )

  val watchProviderResponseDump = WatchProvidersResponse(
    id = 1234567890,
    results = mapOf(
      "US" to CountryProviderDataResponse(
        link = "https://www.link.com/us/movie/example-movie",
        ads = listOf(
          ProviderResponse(
            logoPath = "/logo6.png",
            providerId = 6,
            providerName = "WeTV",
            displayPriority = 3
          )
        ),
        flatrate = listOf(
          ProviderResponse(
            logoPath = "/logo1.png",
            providerId = 1,
            providerName = "Netflix",
            displayPriority = 10
          ),
          ProviderResponse(
            logoPath = "/logo2.png",
            providerId = 2,
            providerName = "Disney+",
            displayPriority = 20
          )
        ),
        rent = listOf(
          ProviderResponse(
            logoPath = "/logo3.png",
            providerId = 3,
            providerName = "Amazon Prime",
            displayPriority = 15
          )
        ),
        buy = listOf(
          ProviderResponse(
            logoPath = "/logo4.png",
            providerId = 4,
            providerName = "Google Play",
            displayPriority = 5
          )
        ),
        free = null
      ),
      "ID" to CountryProviderDataResponse(
        link = "https://www.link.com/id/movie/example-movie",
        ads = null,
        buy = listOf(
          ProviderResponse(
            logoPath = "/logo5.png",
            providerId = 5,
            providerName = "Vidio",
            displayPriority = 8
          )
        ),
        flatrate = null,
        rent = null,
        free = listOf(
          ProviderResponse(
            logoPath = "/logo6.png",
            providerId = 6,
            providerName = "WeTV",
            displayPriority = 12
          )
        )
      )
    )
  )

  val videoTvResponseDump = VideoResponse(
    id = 95479,
    results = listOf(videoItemTvResponseDump1, videoItemTvResponseDump2, videoItemTvResponseDump3)
  )

  val belongsToCollectionDeadpoolDump = BelongsToCollectionResponse(
    id = 448150,
    name = "Deadpool Collection",
    posterPath = "/30c5jO7YEXuF8KiWXLg9m28GWDA.jpg",
    backdropPath = "/dTq7mGyAR5eAydR532feWfjJjzm.jpg"
  )

  val productionCompaniesItemResponseDump = ProductionCompaniesItemResponse(
    logoPath = "/hUzeosd33nzE5MCNsZxCGEKTXaQ.png",
    name = "Marvel Studios",
    id = 420,
    originCountry = "US"
  )

  val detailMovieResponseDump = DetailMovieResponse(
    originalLanguage = "en",
    imdbId = "tt6263850",
    video = false,
    title = "Deadpool & Wolverine",
    backdropPath = "/lD4mhKoiaXpKrtBEjACeWgz7w0O.jpg",
    revenue = 1338073645,
    listGenresItemResponse = listOf(
      GenresItemResponse(id = 28, name = "Action"),
      GenresItemResponse(id = 35, name = "Comedy"),
      GenresItemResponse(id = 878, name = "Science Fiction")
    ),
    popularity = 856.096,
    releaseDatesResponse = ReleaseDatesResponse(
      listReleaseDatesItemResponse = listOf(
        ReleaseDatesItemResponse(
          iso31661 = "AD",
          listReleaseDateItemValueResponse = listOf(
            ReleaseDatesItemValueResponse(
              releaseDate = "2024-11-12T00:00:00.000Z",
              note = "Disney+",
              type = 4
            )
          )
        ),
        ReleaseDatesItemResponse(
          iso31661 = "AE",
          listReleaseDateItemValueResponse = listOf(
            ReleaseDatesItemValueResponse(releaseDate = "2024-07-25T00:00:00.000Z")
          )
        )
      )
    ),
    listProductionCountriesItemResponse = listOf(
      ProductionCountriesItemResponse(iso31661 = "US", name = "United States of America")
    ),
    id = 533535,
    voteCount = 6283,
    budget = 200000000,
    overview = """
      A listless Wade Wilson toils away in civilian life with his days as the morally flexible 
      mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade 
      must reluctantly suit-up again with an even more reluctant Wolverine.
    """.trimIndent(),
    originalTitle = "Deadpool & Wolverine",
    runtime = 128,
    posterPath = "/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg",
    listSpokenLanguagesItemResponse = listOf(
      SpokenLanguagesItemResponse(name = "English", iso6391 = "en", englishName = "English")
    ),
    listProductionCompaniesItemResponse = listOf(
      productionCompaniesItemResponseDump,
      ProductionCompaniesItemResponse(
        logoPath = "/hx0C1XcSxGgat8N62GpxoJGTkCk.png",
        name = "Maximum Effort",
        id = 104228,
        originCountry = "US"
      )
    ),
    releaseDate = "2024-07-24",
    voteAverage = 7.7,
    belongsToCollectionResponse = belongsToCollectionDeadpoolDump,
    tagline = "Come together.",
    adult = false,
    homepage = "https://www.marvel.com/movies/deadpool-and-wolverine",
    status = "Released"
  )

  val lastEpisodeToAirResponse1 = LastEpisodeToAirResponse(
    id = 5732896,
    name = "Episode 12",
    overview = """
      Sa-eon abandons everything he's worked for, leaving Hee-joo completely alone. But after a clue 
      arrives, she embarks on a journey to reunite with him.
    """.trimIndent(),
    voteAverage = 0.0,
    voteCount = 0,
    airDate = "2025-01-04",
    episodeNumber = 12,
    episodeType = "finale",
    productionCode = "",
    runtime = 67,
    seasonNumber = 1,
    showId = 253905,
    stillPath = "/9gvIN5sjFV3EbAasl1nhfMqwv8Z.jpg"
  )

  val createdByItemResponseDump = CreatedByItemResponse(
    gender = 1,
    creditId = "675abc88ccf4df966822ca59",
    name = "Kim Ji-woon",
    profilePath = null,
    id = 2349392
  )

  val networksItemResponseDump = NetworksItemResponse(
    logoPath = "/pOSCKaZhndUFYtxHXjQOV6xJi1s.png",
    name = "MBC",
    id = 97,
    originCountry = "KR"
  )

  val seasonsItemResponseDump = SeasonsItemResponse(
    airDate = "2024-11-22",
    overview = "Overview",
    episodeCount = 12,
    name = "When the Phone Rings",
    seasonNumber = 1,
    id = 392789,
    posterPath = "/glWP5Y7CVeqrOjJpLckQjuLFjQJ.jpg"
  )

  val spokenLanguagesItemResponseDump = SpokenLanguagesItemResponse(
    englishName = "Korean",
    iso6391 = "ko",
    name = "한국어/조선말"
  )

  val contentRatingsItemResponseDump = ContentRatingsItemResponse(
    descriptors = listOf("this is description"),
    iso31661 = "SG",
    rating = "PG13"
  )

  val contentRatingsResponseDump = ContentRatingsResponse(
    contentRatingsItemResponse = listOf(
      ContentRatingsItemResponse(
        iso31661 = "SG",
        rating = "PG13"
      ),
      ContentRatingsItemResponse(
        iso31661 = "KR",
        rating = "15"
      )
    )
  )

  val detailTvResponseDump = DetailTvResponse(
    originalLanguage = "ko",
    numberOfEpisodes = 12,
    networksResponse = listOf(networksItemResponseDump),
    type = "Miniseries",
    backdropPath = "/2vtI9xzD6qpDzY9m8kV67QY0qfM.jpg",
    genres = listOf(
      GenresItemResponse(name = "Drama", id = 18),
      GenresItemResponse(name = "Mystery", id = 9648)
    ),
    popularity = 394.215,
    productionCountriesResponse = listOf(
      ProductionCountriesItemResponse(
        iso31661 = "KR",
        name = "South Korea"
      )
    ),
    id = 253905,
    numberOfSeasons = 1,
    voteCount = 141,
    firstAirDate = "2024-11-22",
    overview = """
      A rising politician and his mute wife's tense marriage begins to unravel after a call from a 
      kidnapper turns their lives upside down.
    """.trimIndent(),
    seasonsResponse = listOf(seasonsItemResponseDump),
    languages = listOf("ko"),
    createdByResponse = listOf(createdByItemResponseDump),
    lastEpisodeToAirResponse = lastEpisodeToAirResponse1,
    posterPath = "/glWP5Y7CVeqrOjJpLckQjuLFjQJ.jpg",
    originCountry = listOf("KR"),
    spokenLanguagesResponse = listOf(spokenLanguagesItemResponseDump),
    productionCompaniesResponse = listOf(
      ProductionCompaniesItemResponse(
        id = 36225,
        logoPath = "/haNfBy6ZS1a1teY9JXNHHkFhZEj.png",
        name = "Bon Factory",
        originCountry = "KR"
      ),
      ProductionCompaniesItemResponse(
        id = 140692,
        logoPath = "/blHOMOg29qc8GmSaiOWYNsxe0vC.png",
        name = "Baram Pictures",
        originCountry = "KR"
      )
    ),
    originalName = "지금 거신 전화는",
    voteAverage = 8.4,
    name = "When the Phone Rings",
    tagline = "Their love hangs by a thread... until a stranger picks up the phone.",
    episodeRunTime = listOf(70),
    contentRatingsResponse = contentRatingsResponseDump,
    adult = false,
    nextEpisodeToAir = null,
    inProduction = false,
    lastAirDate = "2025-01-04",
    homepage = "https://program.imbc.com/WhenThePhoneRings",
    status = "Ended"
  )

  val externalIdResponseDump = ExternalIdResponse(
    imdbId = "tt0417299",
    freebaseMid = "/m/05h95s",
    tvdbId = 74852,
    freebaseId = "/en/avatar_the_last_airbender",
    id = 246,
    twitterId = null,
    tvrageId = 2680,
    facebookId = "avatarthelastairbender",
    instagramId = "avatarthelastairbender"
  )

  val statedResponseDump1 = StatedResponse(
    id = 12345,
    favorite = true,
    ratedResponse = RatedResponse.Value(10.0),
    watchlist = false,
  )

  val statedResponseDump2 = StatedResponse(
    id = 544321,
    favorite = false,
    ratedResponse = RatedResponse.Unrated,
    watchlist = true,
  )

  val personResponseDump = DetailPersonResponse(
    alsoKnownAs = listOf(
      "ฮีธ เลดเจอร์",
      "ヒース・レジャー",
      "히스 레저",
      "希斯·萊傑",
      "هيث ليدجر",
      "ჰიტ ლეჯერი",
      "希斯·莱杰",
      "Heath Andrew Ledger"
    ),
    birthday = "1979-04-04",
    gender = 2,
    imdbId = "nm0005132",
    knownForDepartment = "Acting",
    profilePath = "/AdWKVqyWpkYSfKE5Gb2qn8JzHni.jpg",
    biography = """
        Heath Andrew Ledger (April 4, 1979 – January 22, 2008) was an Australian actor and music 
        video director. After playing roles in several Australian television and film productions 
        during the 1990s, Ledger moved to the United States in 1998 to develop his film career 
        further. His work consisted of twenty films, including 10 Things I Hate About You (1999), 
        The Patriot (2000), A Knight's Tale (2001), Monster's Ball (2001), Lords of Dogtown (2005), 
        Brokeback Mountain (2005), Candy (2006), I'm Not There (2007), The Dark Knight (2008), and 
        The Imaginarium of Doctor Parnassus (2009), the latter two being posthumous releases. He 
        also produced and directed music videos and aspired to be a film director. For his portrayal 
        of Ennis Del Mar in Brokeback Mountain, Ledger won the New York Film Critics Circle Award 
        for Best Actor and the Best International Actor Award from the Australian Film Institute; 
        he was the first actor to win the latter award posthumously.
    """.trimIndent(),
    deathday = "2008-01-22",
    placeOfBirth = "Perth, Western Australia, Australia",
    popularity = 29.537f,
    name = "Heath Ledger",
    id = 1810,
    adult = false,
    homepage = null
  )

  val profileItemResponseDump = ProfilesItemResponse(
    aspectRatio = 0.667,
    filePath = "/83fLAMMb1LGT8YZ4dgRI0fti3az.jpg",
    voteAverage = 5.25f,
    voteCount = 8,
    width = 736,
    iso6391 = "en",
    height = 1104
  )

  val imagePersonResponseDump = ImagePersonResponse(
    id = 1878952,
    profiles = listOf(
      profileItemResponseDump,
      ProfilesItemResponse(
        aspectRatio = 0.667,
        filePath = "/p6oz3tNG1U6mGKdMz3VGw2OGro3.jpg",
        voteAverage = 3.334f,
        voteCount = 1,
        width = 961,
        iso6391 = null,
        height = 1441
      ),
      ProfilesItemResponse(
        aspectRatio = 0.667,
        filePath = "/uNKQMNWJGVrFQfF93ik1SAQ9qhB.jpg",
        voteAverage = 2.278f,
        voteCount = 3,
        width = 300,
        iso6391 = null,
        height = 450
      ),
      ProfilesItemResponse(
        aspectRatio = 0.667,
        filePath = "/dMUXkMfkTEw3qrJisk0mQCQxmBI.jpg",
        voteAverage = 2.066f,
        voteCount = 5,
        width = 600,
        iso6391 = null,
        height = 900
      ),
      ProfilesItemResponse(
        aspectRatio = 0.667,
        filePath = "/iJ1ekhu73bCRkpggLiKQh6MoHi8.jpg",
        voteAverage = 1.75f,
        voteCount = 2,
        width = 900,
        iso6391 = null,
        height = 1350
      ),
      ProfilesItemResponse(
        aspectRatio = 0.667,
        filePath = "/cFBkErXLX1cIzv7R3y9d5DY7bwF.jpg",
        voteAverage = 1.434f,
        voteCount = 5,
        width = 683,
        iso6391 = null,
        height = 1024
      ),
      ProfilesItemResponse(
        aspectRatio = 0.666,
        filePath = "/pO4WoAdrhQR0RB9viBWKHkNOhAL.jpg",
        voteAverage = 0.166f,
        voteCount = 2,
        width = 581,
        iso6391 = null,
        height = 872
      )
    )
  )

  val castItemResponseDump = CastItemResponse(
    firstAirDate = "2022-10-10",
    adult = false,
    backdropPath = "/nH6hPhJq3EEv9CnBZgXU3IQnpJo.jpg",
    genreIds = listOf(12, 53, 878),
    id = 74,
    originalLanguage = "en",
    episodeCount = 1,
    originalTitle = "War of the Worlds",
    overview = """
      Ray Ferrier is a divorced dockworker and less-than-perfect father. Soon after his ex-wife and 
      her new husband drop off his teenage son and young daughter for a rare weekend visit, a strange 
      and powerful lightning storm touches down.
    """.trimIndent(),
    popularity = 50.065,
    posterPath = "/6Biy7R9LfumYshur3YKhpj56MpB.jpg",
    originCountry = listOf("US"),
    releaseDate = "2005-06-13",
    title = "War of the Worlds",
    video = false,
    name = "Name",
    voteAverage = 6.5f,
    voteCount = 8409,
    character = "Ray Ferrier",
    creditId = "52fe4213c3a36847f800226b",
    order = 0,
    mediaType = "movie",
    originalName = "War of the Worlds"
  )

  val crewItemResponseDump = CrewItemResponse(
    adult = false,
    backdropPath = "/z354BaTVzKj7E60WLzDoSmUuO4u.jpg",
    genreIds = listOf(18, 28, 10752),
    id = 616,
    originalLanguage = "en",
    originalTitle = "The Last Samurai",
    overview = "Nathan Algren is an American hired",
    popularity = 44.954f,
    posterPath = "/lsasOSgYI85EHygtT5SvcxtZVYT.jpg",
    releaseDate = "2003-12-05",
    title = "The Last Samurai",
    video = false,
    voteAverage = 7.6f,
    voteCount = 6884,
    creditId = "52fe425ec3a36847f8018e1f",
    department = "Production",
    job = "Producer",
    mediaType = "movie"
  )

  val combinedCreditResponseDump = CombinedCreditResponse(
    id = 500,
    cast = listOf(
      castItemResponseDump,
      CastItemResponse(
        adult = false,
        backdropPath = "/r1gLQFbpkWWLrOEPmpqzzMIUxxj.jpg",
        genreIds = listOf(878, 28, 53),
        id = 180,
        originalLanguage = "en",
        originalTitle = "Minority Report",
        overview = """
          John Anderton is a top 'Precrime' cop in the late-21st century, when technology can predict 
          crimes before they're committed. But Anderton becomes the quarry when another investigator 
          targets him for a murder charge.
        """.trimIndent(),
        popularity = 39.352,
        posterPath = "/qtgFcnwh9dAFLocsDk2ySDVS8UF.jpg",
        releaseDate = "2002-06-20",
        title = "Minority Report",
        video = false,
        voteAverage = 7.4f,
        voteCount = 8730,
        character = "Chief John Anderton [Pre-Crime]",
        creditId = "52fe4223c3a36847f8006f53",
        order = 0,
        mediaType = "movie"
      )
    ),
    crew = listOf(
      crewItemResponseDump,
      CrewItemResponse(
        adult = false,
        backdropPath = "/wwLufumafJojc59hgIamHyJSTO9.jpg",
        genreIds = listOf(878, 10749, 18, 14, 53, 9648),
        id = 1903,
        originalLanguage = "en",
        originalTitle = "Vanilla Sky",
        overview = """
          David Aames has it all: wealth, good looks and gorgeous women on his arm. But just as he 
          begins falling for the warmhearted Sofia, his face is horribly disfigured in a car accident. 
          That's just the beginning of his troubles as the lines between illusion and reality, between 
          life and death, are blurred.
        """.trimIndent(),
        popularity = 33.937f,
        posterPath = "/cAh2pCiNPftsY3aSqJuIOde7uWr.jpg",
        releaseDate = "2001-12-14",
        title = "Vanilla Sky",
        video = false,
        voteAverage = 6.8f,
        voteCount = 4152,
        creditId = "52fe4320c3a36847f803c339",
        department = "Production",
        job = "Producer",
        mediaType = "movie"
      ),
      CrewItemResponse(
        adult = false,
        backdropPath = "/5IxQYCNd0pU8wYDrvHiVIaWDWGX.jpg",
        genreIds = listOf(27, 9648, 53),
        id = 1933,
        originalLanguage = "en",
        originalTitle = "The Others",
        overview = """
          Grace is a religious woman who lives in an old house kept dark because her two children, Anne 
          and Nicholas, have a rare sensitivity to light. When the family begins to suspect the house 
          is haunted, Grace fights to protect her children at any cost in the face of strange events 
          and disturbing visions.
        """.trimIndent(),
        popularity = 42.581f,
        posterPath = "/p8g1vlTvpM6nr2hMMiZ1fUlKF0D.jpg",
        releaseDate = "2001-08-02",
        title = "The Others",
        video = false,
        voteAverage = 7.609f,
        voteCount = 6543,
        creditId = "52fe4323c3a36847f803d391",
        department = "Production",
        job = "Executive Producer",
        mediaType = "movie"
      )
    )
  )

  val externalIDPersonResponseDump = ExternalIDPersonResponse(
    id = 114253,
    freebaseMid = "/m/027xw9j",
    freebaseId = null,
    imdbId = "nm1375030",
    tvrageId = null,
    wikidataId = "Q7518724",
    facebookId = null,
    instagramId = null,
    tiktokId = null,
    twitterId = "simonfarnaby",
    youtubeId = null
  )

  val postResponseSuccessDump = PostFavoriteWatchlistResponse(
    statusCode = 200,
    statusMessage = "Success"
  )

  val ratePostResponseSuccessDump = PostResponse(
    success = true,
    statusCode = 200,
    statusMessage = "Successfully",
  )

  val authenticationResponseDump = AuthenticationResponse(
    success = true,
    expireAt = "expire_date",
    requestToken = "request_token"
  )

  val postResponseDump = PostResponse(true, 200, "Success")

  val createSessionResponseDump = CreateSessionResponse(
    success = true,
    sessionId = "session_id"
  )

  val accountDetailsResponse = AccountDetailsResponse(
    includeAdult = false,
    iso31661 = "ID",
    name = "YOUR_NAME",
    avatarItemResponse = AvatarItemResponse(
      gravatarResponse = GravatarResponse("gravatar_hash2"),
      avatarTMDbResponse = AvatarTMDbResponse("avatar_path")
    ),
    id = 543798538,
    iso6391 = "en",
    username = "USERNAME"
  )

  val countryIPResponseDump = CountryIPResponse(
    country = "ID",
    ip = "103.187.242.255"
  )
}
