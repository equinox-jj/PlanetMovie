# PlanetMovie
PlanetMovie is a Movie and Tv Series Catalog android apps. This application can find a Popular Movie or Tv Series, what currently playing On Cinema or currently playing on Tv, Save Movie or Tv Series to Favorite, and Search some Movie or Tv Series. this apps is written in Kotlin using MVVM(Model-View-ViewModel) Architecture, Kotlin Coroutines to Perform Asynchronous Operation, Retrofit to Communicate with REST API, Android Jetpack Library such as Room, LiveData, Hilt, Navigation Component, DataStore.

# Preview Apps:
![Image Apps](https://github.com/equinox-jj/PlanetMovie/blob/main/docs/apps_img.jpg)

# Dependencies:
* [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)
* [Room Database](https://developer.android.com/training/data-storage/room)
* [Dagger-Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Coroutines](https://developer.android.com/kotlin/coroutines?gclsrc=ds&gclsrc=ds)
* [Data Store](https://developer.android.com/topic/libraries/architecture/datastore?gclsrc=ds&gclsrc=ds)
* [Image Coil](https://github.com/coil-kt/coil)
* [Retrofit2](https://square.github.io/retrofit/)

# Project Setup:
You need a [Movie TMDB API KEY](https://developers.themoviedb.org/3/getting-started/introduction) to make the project work.
And then put your API_KEY in [util/Constant.kt](https://github.com/equinox-jj/PlanetMovie/blob/main/app/src/main/java/com/planetmovie/util/Constant.kt)
```
const val API_KEY = "[ PUT YOUR API KEY HERE ]"
```

# Download APK:
* [PlanetMovie.apk](https://github.com/equinox-jj/PlanetMovie/raw/main/docs/PlanetMovie.apk)
