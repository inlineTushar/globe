# Globe: App to see counties of the world

# Tech Stack
    - Language: Kotlin
    - Architecture: Clean Code + MVVM + Multi module + Koin (DI)
    - Asynchrony: Coroutines + Flows
    - Network: Retrofit + Moshi
    - Local Storage: Realm DB
    - Test: Junit 5 + Flow Turbine (Viewmodels and repositories are unit tested)

# Features
    - Offline first - Requires network during first time launch and you always have your countries saved into the app.
    - Proactively looks for network and fetch the list when network is established.
    - Flags are backed by unicode (retrieved from API) and used as placeholder until the image flag is loaded and cached.
    - Search by country name