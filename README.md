# FoodExpress

> A modern Android food ordering application that lets users discover restaurants, browse menus, manage a cart, place orders, and track their deliveries.

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?logo=firebase&logoColor=black)](https://firebase.google.com/)
[![Google%20Maps](https://img.shields.io/badge/Maps-Google%20Maps-4285F4?logo=googlemaps&logoColor=white)](https://developers.google.com/maps)
[![Gradle](https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white)](https://gradle.org/)

## Overview

**FoodExpress** is an Android-based food ordering application developed as a Software Engineering project for **SWE 250**.

The application provides a complete customer-side ordering flow, starting from account creation and restaurant discovery through menu selection, cart management, checkout, payment selection, order placement, and delivery tracking.

The project is built as a native Android application and uses Firebase services for authentication, cloud data, storage, messaging, and analytics. Google Maps and location services are integrated to enhance the user experience with location-based features.

## Key Features

### Authentication
- User registration and login
- Email/password authentication
- Password reset flow
- Google sign-in support
- User profile management

### Restaurant Discovery
- Home screen with personalized greeting
- Restaurant browsing
- Food categories
- Popular restaurant and food listings
- Restaurant details and ratings
- Menu browsing

### Ordering
- Add food items to cart
- Adjust item quantities
- View cart summary
- Delivery address selection
- Location-based address support
- Checkout flow
- Payment method selection
- Order confirmation

### Order Management
- Order history
- Current order status
- Delivery progress
- Order details
- Reorder / continue ordering flow

### Notifications
- Push notification support through Firebase Cloud Messaging
- Order-related notification capability

### Profile & Account
- View and edit personal information
- Manage account details
- Access order history
- Sign out securely

## Application Flow

```text
Register / Login
       │
       ▼
     Home
       │
       ├── Browse Categories
       │
       ├── Explore Restaurants
       │        │
       │        ▼
       │     Restaurant
       │        │
       │        ▼
       │      Menu
       │        │
       │        ▼
       └────── Cart
                │
                ▼
             Checkout
                │
        ┌───────┴────────┐
        ▼                ▼
 Delivery Address    Payment Method
        │                │
        └───────┬────────┘
                ▼
        Order Confirmation
                │
                ▼
          Order Tracking
                │
                ▼
          Order History
```

## Technology Stack

| Area | Technology |
|---|---|
| Platform | Android |
| Build System | Gradle |
| Build Configuration | Kotlin DSL |
| UI | Android XML layouts + View Binding |
| UI Components | AndroidX, Material Components, RecyclerView, CardView |
| Navigation | Android Navigation Component |
| Authentication | Firebase Authentication |
| Database | Firebase Firestore |
| File Storage | Firebase Storage |
| Notifications | Firebase Cloud Messaging |
| Analytics | Firebase Analytics |
| Maps | Google Maps SDK for Android |
| Location | Google Play Services Location |
| Image Loading | Glide |
| Google Sign-In | Android Credential Manager + Google Identity |

The current project configuration targets **Android API 34**, supports devices from **API 24**, and uses **Java 11** compatibility settings.

## Architecture & Project Structure

The repository follows a standard Android application structure:

```text
SWE-250-Project/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/ or kotlin/
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   ├── layout/
│   │       │   ├── mipmap/
│   │       │   ├── values/
│   │       │   └── ...
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json
│
├── gradle/
│   └── libs.versions.toml
│
├── image/
│   └── project assets
├── screenshots/
│   └── App's screenshots
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

> The exact package-level organization may evolve as the project is developed. The structure above describes the major repository components.

## Getting Started

### Prerequisites

Before running the project, install:

- Android Studio
- Android SDK 34
- Android SDK Build Tools
- JDK 11
- A physical Android device or Android Emulator
- A Firebase project
- A Google Maps API key

### 1. Clone the Repository

```bash
git clone https://github.com/Hazerakteritu/SWE-250-Project.git
cd SWE-250-Project
```

### 2. Open in Android Studio

1. Open **Android Studio**.
2. Select **Open**.
3. Choose the cloned `SWE-250-Project` directory.
4. Allow Gradle to sync and download the required dependencies.

### 3. Configure Firebase

The application uses Firebase for authentication, Firestore, storage, messaging, and analytics.

For a new Firebase environment:

1. Create a project in Firebase Console.
2. Add an Android application.
3. Use the application's package ID:

```text
com.example.foodexpress
```

4. Download `google-services.json`.
5. Place it inside:

```text
app/google-services.json
```

6. Enable the Firebase services required by the application:
   - Authentication
   - Cloud Firestore
   - Cloud Storage
   - Cloud Messaging
   - Analytics

If you use the Firebase configuration already associated with the project, verify that it is still active before running the application.

### 4. Configure Google Maps

The application uses Google Maps and location services.

Create a Google Maps API key in Google Cloud Console and enable the required Maps SDK and location services for the Firebase/Google Cloud project.

Add the API key according to the project's Android resource/configuration setup.

> **Security note:** Do not commit unrestricted API keys or private credentials to a public repository.

### 5. Build and Run

Using Android Studio:

```text
Build → Make Project
Run → Run 'app'
```

Or from the command line:

```bash
./gradlew assembleDebug
```

On Windows:

```powershell
gradlew.bat assembleDebug
```

To install the debug APK on a connected device:

```bash
./gradlew installDebug
```

## Firebase Services

The application is configured with the following Firebase services:

| Firebase Service | Purpose |
|---|---|
| Firebase Authentication | User registration, login, and account access |
| Cloud Firestore | Application and ordering data |
| Firebase Storage | Image/file storage |
| Firebase Cloud Messaging | Push notifications |
| Firebase Analytics | Application usage analytics |

## User Journey

### 1. Account Access
Users can create an account or sign in to an existing account. Password recovery is also available.

### 2. Discover Food
After signing in, users can explore food categories, restaurants, popular items, and available menus.

### 3. Select Items
Users open a restaurant, browse its menu, select food items, and add them to the cart.

### 4. Review Cart
The cart provides an overview of selected items, quantities, and the order total before checkout.

### 5. Provide Delivery Information
Users select or provide a delivery address, with Google Maps and location services supporting the address workflow.

### 6. Checkout
Users review their delivery information and choose an available payment method.

### 7. Place Order
After confirmation, the order is submitted and the user receives an order confirmation.

### 8. Track Orders
Users can view order progress and access previous orders from the order history section.

## Screenshots

FoodExpress includes comprehensive screenshots showcasing the complete user journey and application features:

### 🔐 Authentication Screens

| Login | Registration | Forgot Password |
|-------|--------------|-----------------|
| ![Login Screen](screenshots/01_login.png) | ![Registration Screen](screenshots/02_registration.png) | ![Forgot Password](screenshots/03_forgot_password.png) |
| User login interface with email and password | New user registration form | Password recovery flow |

### 🏠 Home & Discovery

| Home Screen | Explore Restaurants | Categories |
|-------------|-------------------|-----------|
| ![Home Screen](screenshots/04_home.png) | ![Explore Restaurants](screenshots/05_restaurants_list.png) | ![Categories](screenshots/06_categories.png) |
| Personalized welcome and quick access | Browse available restaurants | Food categories filter |

### 🍽️ Restaurant & Menu

| Restaurant Details | Menu Items | Popular Items |
|--------------------|-----------|---------------|
| ![Restaurant Details](screenshots/07_restaurant_details.png) | ![Menu Items](screenshots/08_menu.png) | ![Popular Items](screenshots/09_popular_food.png) |
| Restaurant info, ratings, and location | Complete menu with food items | Popular and trending foods |

### 🛒 Cart & Ordering

| Shopping Cart | Cart Summary | Add Items |
|---------------|-------------|-----------|
| ![Shopping Cart](screenshots/10_cart_screen.png) | ![Cart Summary](screenshots/11_cart_summary.png) | ![Add Items](screenshots/12_add_to_cart.png) |
| View cart items and quantities | Order summary before checkout | Add food items to cart |

### 📦 Checkout & Payment

| Checkout Screen | Delivery Address | Payment Method |
|-----------------|------------------|-----------------|
| ![Checkout](screenshots/13_checkout.png) | ![Delivery Address](screenshots/14_delivery_address.png) | ![Payment](screenshots/15_payment_method.png) |
| Review order details | Select delivery location | Choose payment option |

### ✅ Order Confirmation & Tracking

| Order Confirmation | Order Placed | Order Tracking |
|-------------------|--------------|-----------------|
| ![Confirmation](screenshots/16_order_confirmation.png) | ![Order Placed](screenshots/17_order_placed.png) | ![Tracking](screenshots/18_order_tracking.png) |
| Order successfully confirmed | Order placement confirmation | Real-time delivery tracking |

### 📍 Delivery & History

| Delivery in Progress | Order Delivered | Order History |
|----------------------|-----------------|---------------|
| ![Delivery Progress](screenshots/19_delivery_progress.png) | ![Order Delivered](screenshots/20_order_delivered.png) | ![Order History](screenshots/21_order_history.png) |
| Live delivery status | Order successfully delivered | Previous orders and reorder |

### 👤 Account & Profile

| User Profile | Profile View | Account Settings |
|--------------|-------------|-----------------|
| ![Profile](screenshots/22_user_profile.png) | ![View Profile](screenshots/23_view_profile.png) | ![Settings](screenshots/24_account_settings.png) |
| User profile information | Full profile details | Account management options |

### 🎯 Additional Features

| Navigation Drawer | Search Feature | Location Map |
|------------------|-----------------|--------------|
| ![Navigation Drawer](screenshots/25_navigation_drawer.png) | ![Search](screenshots/26_search_feature.png) | ![Location Map](screenshots/27_location_map.png) |
| Main navigation menu | Search restaurants/food | Interactive location selector |

### User Interface Gallery

The screenshots directory (`screenshots/`) contains organized visual assets that showcase:
- Clean and intuitive UI design
- Seamless navigation flow
- Real-time order tracking interface
- Location-based services integration
- Firebase authentication screens
- Cart management and checkout flow

## Build Configuration

Current application configuration:

```text
Application ID: com.example.foodexpress
Compile SDK:    34
Target SDK:     34
Minimum SDK:    24
Java:           11
Version:        1.0
Version Code:   1
```

## Academic Context

**Course:** SWE 250 — Project Work II  
**Project:** FoodExpress  
**Platform:** Android  
**Repository:** [Hazerakteritu/SWE-250-Project](https://github.com/Hazerakteritu/SWE-250-Project)

This project was developed as part of an academic software engineering project with an emphasis on requirements, application design, and implementation.

---

<p align="center">
  <strong>FoodExpress</strong><br>
  A complete Android food ordering experience.<br>
  <em>Order food. Track delivery. Enjoy meals.</em>
</p>
