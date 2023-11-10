# My Garden App with Clean Architecture & Compose

A gardening app which helps user to manages watering need of plants. In app user can add any number
of plants in their garden from a huge cloud database (backed by Perenual). App tell user the last time water is given to
a plant.

## Screenshots
| Mode  | Garden                                        | Plants                                        | Plant Details                                       |
|-------|-----------------------------------------------|-----------------------------------------------|-----------------------------------------------------|
| Light | <img src="screenshot/garden.jpg" width="250"> | <img src="screenshot/plants.jpg" width="250"> | <img src="screenshot/plant_detail.jpg" width="250"> |

## Features

1. **Offline-first**: The app can be accessed even without an internet connection.
2. **Pagination**: Efficiently loads large amounts of data to improve the user experience.
3. **Search functionality**: Allows users to quickly find specific information within the app.
4. **Auto Sync**: Uses both NetworkConnectivityStream and WorkManager to ensure data is always
   up-to-date.

## Clean Architecture

Clean architecture promotes separation of concerns, making the code loosely coupled. This results in
a more testable and flexible code.

![](screenshot/clean_arch.png)

The core principles of the clean approach can be summarized as followed:

1. The application code is separated into layers - These layers define the separation of concerns
   inside the code base.
2. The layers follow a strict dependency rule - Each layer can only interact with the layers below
   it.
3. As we move toward the bottom layer — the code becomes generic - The bottom layers dictate
   policies and rules, and the upper layers dictate implementation details such as the database,
   networking manager, and UI.

The 3 modules of architecture are :

* __Presentation__: Layer with the Android Framework, the MVVM pattern and the DI module. Depends on
  the domain to access the use cases and on di, to inject dependencies.
* __Domain__: Layer with the business logic. Contains the use cases, in charge of calling the
  correct repository or data member.
* __Data__: Layer with the responsibility of selecting the proper data source for the domain layer.
  It contains the implementations of the repositories declared in the domain layer. It may, for
  example, check if the data in a database is up to date, and retrieve it from service if it’s not.

# Architecture Layers

The app uses clean architecture with MVVM(Model View View Model) design pattern. MVVM provides
better separation of concern, easier testing, lifecycle awareness, etc.

![](screenshot/MVVM_Flow.png)

## Folder Structure
High level folder structure looks like below
![Folder Structure](screenshot/folder_structure.png)

## Built With 🛠

- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android
  development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous
  and more..
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) -
  A cold asynchronous data stream that sequentially emits values and completes normally or with an
  exception.
- [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - A live data
  replacement.

- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) -
  Collection of libraries that help you design robust, testable, and maintainable apps.
    - [Paging3](https://kotlinlang.org/) - Load and display small chunks of data at a time.
    - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects
      that notify views when the underlying database changes.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores
      UI-related data that isn't destroyed on UI changes.
    - [SavedStateHandle](https://developer.android.com/reference/androidx/lifecycle/SavedStateHandle) -
      A handle to saved state passed down to androidx.lifecycle.ViewModel.
    - [Navigation Components](https://developer.android.com/guide/navigation/navigation-getting-started) -
      Navigate fragments easier.
    - [Room](https://developer.android.google.cn/jetpack/androidx/releases/room) - Persistence
      library provides an abstraction layer over SQLite to allow for more robust database access
      while harnessing the full power of SQLite.
    - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) -
      Schedule deferrable, asynchronous tasks

- [Dependency Injection](https://developer.android.com/training/dependency-injection)
    - [Hilt](https://dagger.dev/hilt) - Easier way to incorporate Dagger DI into Android
      application.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Mockito](https://github.com/mockito/mockito) - For Mocking and Unit Testing
- [ktlint](https://github.com/pinterest/ktlint/blob/master/README.md#installation) - For code
  styling

## Requirements

https://perenual.com/

### Perenual API key

App uses the [Perenual API](https://perenual.com) to fetch plants. To use the API, you will need to obtain a free developer API key. See the
[Perenual API Documentation](https://perenual.com/docs/api) for instructions.

Once you have the key, add this line to the `gradle.properties` file, either in your user home
directory (usually `~/.gradle/gradle.properties` on Linux and Mac) or in the project's root folder:

```
api_key=<your Perenual access key>
```

### Unsplash API key

App uses the [Unsplash API](https://unsplash.com/developers) to load pictures on the gallery
screen. To use the API, you will need to obtain a free developer API key. See the
[Unsplash API Documentation](https://unsplash.com/documentation) for instructions.

Once you have the key, add this line to the `gradle.properties` file, either in your user home
directory (usually `~/.gradle/gradle.properties` on Linux and Mac) or in the project's root folder:

```
unsplash_access_key=<your Unsplash access key>
```

The app is still usable without an API key, though you won't be able to navigate to the gallery
screen.



Additional resources
--------------------
https://github.com/android/sunflower

