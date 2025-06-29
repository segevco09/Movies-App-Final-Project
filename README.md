# 🎬 Your Movies Choice – Android Movie App

Welcome to **Your Movies Choice**! This modern Android application empowers users to discover, search, and manage their favorite movies with a beautiful, intuitive interface. Built with the latest Android technologies and best practices, it's designed for a seamless and enjoyable movie browsing experience.

---

## 🏗️ Architecture & Patterns

- **MVVM Architecture**: Ensures a clean separation of UI, business logic, and data layers for maintainability and scalability.
- **Repository Pattern**: Centralizes data operations, abstracting local and remote sources.
- **Dependency Injection**: Powered by Hilt for modular, testable code.
- **Reactive UI**: Uses LiveData and ViewModel for seamless, lifecycle-aware UI updates.

---

## 🗄️ Local Persistence with Room

- **Room Database**: Stores movie data locally for offline access and fast loading.
- **Entities & DAOs**: Well-defined Movie entity and DAO interfaces for robust CRUD operations.
- **Migrations**: Handles schema changes gracefully, ensuring data integrity across updates.

---

## 🌐 Networking & Data

- **Retrofit**: Fetches movie data from [The Movie Database (TMDB)](https://www.themoviedb.org/) REST API.
- **Gson**: Efficient JSON parsing.
- **OkHttp**: Network logging for easy debugging.
- **Coroutines**: Kotlin coroutines for smooth, non-blocking async operations.

---

## 🎥 YouTube Trailer Integration

- **YouTube Player**: Seamlessly plays official movie trailers within the app using [Pierfrancesco Soffritti's Android YouTube Player](https://github.com/Pierfrancescosoffritti/android-youtube-player).
- **Dynamic Fetching**: Trailers are fetched and played directly from YouTube based on the selected movie.

---

## 🌍 Multi-Language Support

- **Localization**: Full support for English and Hebrew (auto-detected via device settings).
- **Resource Files**: All user-facing strings are localized for a native experience.

---

## ✨ Features

- **Browse Popular & Upcoming Movies**: Always up-to-date with the latest from TMDB.
- **Favorites**: Mark and manage your favorite movies.
- **Edit Movie Details**: Update movie info for your personal collection.
- **Search & Sort**: Powerful search and multiple sort options (rating, date, etc.).
- **Movie Details**: Rich details, high-res posters, and instant trailer playback.
- **Offline Support**: Access your favorite even without internet.
- **Modern UI**: Material Design, smooth navigation, and custom animations.

---

## 🛠️ Key Technologies & Libraries

| Technology/Library | Purpose |
|--------------------|---------|
| **Kotlin** | Modern, concise programming language |
| **MVVM, LiveData, ViewModel** | Architecture & reactive UI |
| **Room** | Local database persistence |
| **Retrofit, Gson, OkHttp** | Networking & API integration |
| **Hilt** | Dependency injection |
| **Coroutines** | Asynchronous operations |
| **Glide** | Image loading |
| **YouTube Player** | Trailer playback |
| **Navigation Component** | Seamless navigation |
| **ViewBinding** | Type-safe UI code |
| **Material Components** | Modern UI/UX |
| **Parcelize** | Efficient data passing |

---

## 📱 Video

https://www.youtube.com/watch?v=1XTdK7yjMXk

---

## 🌐 Language & Localization

- **English** (default)
- **Hebrew** (auto-detected)

---

## 🚀 Getting Started

1. **Clone the repository**
2. **Add your TMDB API key** to `gradle.properties` as `TMDB_API_KEY=your_api_key`
3. **Build and run** on Android Studio (minSdk 24+)

---

## 👩‍💻 Developers

- Segev Cohen
- Yuval Itzhak
- Matan Zror

---

## 🏆 Project Highlights

- **Modern Android stack**: MVVM, Room, Hilt, Coroutines, Navigation, and more.
- **Clean, scalable codebase**: Easy to extend and maintain.
- **Production-ready features**: Offline support, localization, and robust error handling.
- **Beautiful UI/UX**: Material Design, smooth transitions, and responsive layouts.
- **Real-world API integration**: Demonstrates practical skills in RESTful networking and third-party SDKs.

---

## 📄 License

This project is for demonstration and educational purposes.