# YV Store

An Android e-commerce application built with Jetpack Compose and Clean Architecture. YV Store allows users to browse products, manage a shopping cart, save delivery addresses, and place orders — all backed by Firebase and a local Room database cache.

## Table of Contents

- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Build Commands](#build-commands)
- [Running Tests](#running-tests)
- [Features](#features)
- [Design System](#design-system)
- [Database Schema](#database-schema)
- [Dependency Injection](#dependency-injection)

## Architecture

The app follows **Clean Architecture** with **feature-based packaging** in a single-module setup.

```
┌─────────────────────────────────────────────────┐
│                 Presentation                     │
│         ViewModels · Compose Screens             │
│            UI State · UI Events                  │
├─────────────────────────────────────────────────┤
│                   Domain                         │
│       Use Cases · Repository Interfaces          │
│              Entity Models                       │
├─────────────────────────────────────────────────┤
│                    Data                          │
│   Repository Impls · Data Sources · Mappers      │
├─────────────────────────────────────────────────┤
│                    Core                          │
│  Design System · Database · Network · Utilities  │
└─────────────────────────────────────────────────┘
```

**Data flow:**

```
Firebase Firestore ↔ Room (local cache) ↔ Repository ↔ Use Cases ↔ ViewModels → Compose UI
```

- **Kotlin Flows** for reactive data streams
- **StateFlow** for UI state management in ViewModels
- **Coroutines** for all async operations

## Tech Stack

| Category | Technology | Version |
|---|---|---|
| **Language** | Kotlin | 2.0.21 |
| **UI** | Jetpack Compose + Material 3 | BOM 2024.09.00 |
| **Navigation** | Navigation Compose | 2.9.6 |
| **DI** | Koin | 4.0.0 |
| **Remote** | Firebase Firestore & Auth | BOM 33.7.0 |
| **Local DB** | Room | 2.7.1 |
| **Preferences** | DataStore | 1.1.1 |
| **Async** | Kotlin Coroutines | 1.9.0 |
| **Image Loading** | Coil | 2.7.0 |
| **Serialization** | Kotlinx Serialization | 1.7.3 |
| **Annotation Processing** | KSP | 2.0.21-1.0.28 |
| **Build System** | Gradle (Kotlin DSL) | AGP 9.0.0 |

### Testing

| Library | Purpose |
|---|---|
| JUnit 4 | Test framework |
| MockK | Mocking |
| Google Truth | Assertions |
| Turbine | Flow testing |
| Robolectric | Android framework on JVM |
| Kotlinx Coroutines Test | Coroutine test utilities |

## Project Structure

```
app/src/main/java/org/neo/yvstore/
├── MainActivity.kt                 # Entry point with splash screen
├── MainViewModel.kt                # Auth state management
├── YVStoreApplication.kt           # Application class, Koin setup
│
├── core/
│   ├── cache/di/                   # DataStore preferences DI
│   ├── common/
│   │   ├── di/                     # Common utilities DI
│   │   ├── dispatcher/             # Coroutine dispatchers
│   │   └── util/                   # CurrencyFormatter, StringUtil, ExceptionHandler
│   ├── data/
│   │   ├── di/                     # Data layer DI
│   │   ├── mapper/                 # Core data mappers
│   │   ├── model/                  # Core data models
│   │   └── seeder/                 # ProductSeeder for dev data
│   ├── database/
│   │   ├── YVStoreDatabase.kt      # Room database (v3)
│   │   ├── dao/                    # ProductDao, CartItemDao, AddressDao
│   │   ├── di/                     # Database DI
│   │   └── model/                  # Room entities
│   ├── designSystem/
│   │   ├── color/                  # Custom color system with light/dark tokens
│   │   ├── theme/                  # YVStoreTheme, Material 3 integration
│   │   ├── typography/             # Poppins font family, type scale
│   │   └── util/                   # UI constants
│   ├── domain/
│   │   ├── manager/                # UserManager
│   │   ├── model/                  # User, Address, CartItem, Resource, ValidationResult
│   │   └── validator/              # Email, Name, Password validators
│   ├── network/
│   │   ├── di/                     # Network DI
│   │   └── utils/                  # Firebase Task extensions
│   └── ui/
│       ├── animations/             # Shared animations
│       └── component/              # Reusable Compose components
│           ├── button/
│           ├── card/
│           ├── dialog/
│           ├── divider/
│           ├── grid/
│           ├── image/
│           ├── input/
│           ├── navigation/
│           ├── progress/
│           ├── status/
│           ├── surface/
│           └── text/
│
├── features/
│   ├── auth/                       # Login & Signup
│   ├── product/                    # Product listing, details, search
│   ├── cart/                       # Shopping cart
│   ├── address/                    # Delivery addresses
│   └── order/                      # Checkout & order placement
│
├── navigation/
│   ├── graph/                      # AuthGraph, MainGraph, ProductGraph
│   └── routes/                     # Route definitions
│
└── di/
    └── AppModule.kt                # App-level DI (Firebase, ImageLoader)
```

Each feature follows the same internal structure:

```
features/{feature}/
├── di/              # Koin module
├── data/
│   ├── datasource/  # Remote (Firebase) and/or local data sources
│   ├── mapper/      # Data ↔ domain model mappers
│   └── repository/  # Repository implementation
├── domain/
│   ├── model/       # Domain entities
│   ├── repository/  # Repository interface
│   └── usecase/     # Business logic use cases
└── presentation/
    ├── model/       # UI state models
    └── screen/      # ViewModel + Compose screen + components
```

## Getting Started

### Prerequisites

- **Android Studio** Ladybug or later
- **JDK 11** or higher
- **Android SDK** with compileSdk 36
- A **Firebase project** with Firestore and Authentication enabled

### Firebase Setup

1. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
2. Add an Android app with package name `org.neo.yvstore`
3. Download the `google-services.json` file
4. Place it in the `app/` directory

### Clone and Build

```bash
# Clone the repository
git clone https://github.com/Neo-glitch/YouVerifyAssessment.git
cd YouVerifyAssessment

# Build the debug APK
./gradlew assembleDebug

# Install on a connected device/emulator
./gradlew installDebug
```

## Build Commands

```bash
# Build
./gradlew assembleDebug              # Build debug APK
./gradlew assembleRelease            # Build release APK

# Install
./gradlew installDebug               # Install debug APK on connected device

# Clean
./gradlew clean                      # Clean build outputs
./gradlew clean assembleDebug        # Clean build then build debug
```

## Running Tests

### Unit Tests (JVM)

```bash
# Run all unit tests
./gradlew testDebugUnitTest

# Run a specific test class
./gradlew testDebugUnitTest --tests "org.neo.yvstore.features.auth.presentation.screens.login.LoginViewModelUnitTest"

# Run all tests in a feature package
./gradlew testDebugUnitTest --tests "org.neo.yvstore.features.auth.*"

# Run tests with detailed output
./gradlew testDebugUnitTest --info
```

### Test Structure

```
app/src/test/java/org/neo/yvstore/
├── core/                          # Validator tests, mapper tests, utilities
├── features/
│   ├── auth/                      # Login/Signup ViewModel & repository tests
│   ├── product/                   # Product repository & use case tests
│   ├── cart/                      # Cart ViewModel & repository tests
│   ├── address/                   # Address ViewModel & repository tests
│   └── order/                     # Order repository & checkout tests
└── testdoubles/                   # Shared test fakes
    ├── dao/                       # Fake DAOs
    ├── datasource/                # Fake data sources
    ├── dispatcher/                # Test dispatchers
    └── repository/                # Fake repositories
```

Tests are organized into **unit tests** (isolated with mocks) and **integration tests** (testing component interactions with fakes). The project uses:

- **MockK** for mocking dependencies
- **Turbine** for testing Kotlin Flows
- **Google Truth** for readable assertions
- **Robolectric** for Android framework access on JVM
- **Test doubles** (fakes) in `testdoubles/` for integration tests

## Features

### Authentication
- Email/password login and signup via Firebase Auth
- Form validation (email format, password strength, name requirements)
- Persistent session management with automatic auth state detection
- Splash screen while checking authentication status

### Product Browsing
- Home screen with product grid
- Full product list with pull-to-refresh
- Product detail view with images, ratings, and reviews
- Product search functionality
- Remote data from Firebase Firestore with local Room cache

### Shopping Cart
- Add/remove products from cart
- Update item quantities
- Cart total calculation
- Persistent cart stored in Room database

### Delivery Addresses
- Add and delete delivery addresses
- Address list management
- Stored locally in Room and synced with Firebase

### Order Placement
- Checkout flow with address selection
- Order placement via Firebase Firestore
- Order success confirmation screen
- Cart clearing after successful order (wrapped in a database transaction)

## Design System

The app uses a **custom design system** built on top of Material 3:

- **Theme:** `YVStoreTheme` object exposes custom `YVStoreColors` via `CompositionLocalProvider`
- **Typography:** Poppins font family with 6 weight variants (light through black)
- **Colors:** Custom color tokens for light and dark themes, organized by domain:
  - `CardColors` — card styling
  - `IconColors` — icon tints
  - `NavigationColors` — nav bar colors
  - `PopUpColors` — dialog/popup colors
  - `TextColors` — text color variants

Access colors via:
```kotlin
// Custom app colors
YVStoreTheme.colors.someColor

// Standard Material 3 colors
MaterialTheme.colorScheme.primary
```

## Database Schema

Room database (`YVStoreDatabase`, version 3) with 3 tables:

| Table | Primary Key | Description |
|---|---|---|
| `products` | `id: String` | Cached product data (name, description, price, image, rating, reviews) |
| `cart_items` | `id: Long` (auto-gen) | Shopping cart entries (product reference, quantity, unit price) |
| `addresses` | `id: String` | User delivery addresses (street, city, state, country) |

Schema is exported for migration support (`exportSchema = true`).

## Dependency Injection

All DI is managed through **Koin** modules, loaded at app startup:

| Module | Scope |
|---|---|
| `appModule` | Firebase instances, ImageLoader, MainViewModel |
| `cacheModule` | DataStore preferences |
| `databaseModule` | Room database and DAOs |
| `networkModule` | Network/Firebase configuration |
| `dataModule` | Core data layer bindings |
| `commonModule` | Shared utilities and dispatchers |
| `authModule` | Auth data sources, repository, use cases, ViewModels |
| `productModule` | Product data sources, repository, use cases, ViewModels |
| `cartModule` | Cart repository, use cases, ViewModels |
| `addressModule` | Address data sources, repository, use cases, ViewModels |
| `orderModule` | Order data sources, repository, use cases, ViewModels |

## SDK Configuration

| Property | Value |
|---|---|
| compileSdk | 36 |
| targetSdk | 36 |
| minSdk | 28 (Android 9.0) |
| Java | 11 |
| Kotlin | 2.0.21 |
