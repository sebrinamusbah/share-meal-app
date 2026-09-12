# 🍽️ Share Meal

A native Android application that helps people share food donations with others in need through a simple and easy-to-use mobile interface.

## Features

- Create and publish food donations
- Browse available food donations
- View donation information
- Contact donors directly by phone
- Step-by-step guide explaining how to use the application
- Contact Us section
- Local storage using SQLite
- Simple and user-friendly interface

## How It Works

### Donate

Users can create a food donation by providing the required donation information.

### Receive

Users can browse available food donations and view their details. When a phone number is available, users can tap the number to open the phone application and contact the donor.

### How to Use

Provides instructions explaining how to use the Share Meal application.

### Contact Us

Provides a way for users to access contact information and get in touch with the application team.

## Technologies

- **Java**
- **Android SDK 34**
- **AndroidX**
- **Material Components**
- **ConstraintLayout**
- **RecyclerView**
- **CardView**
- **SQLite**
- **View Binding**
- **Gradle**
- **Android Studio**

## Project Structure

```text
FoodDonationApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/fooddonationapp/
│   │   │   │       ├── adapters/
│   │   │   │       ├── database/
│   │   │   │       ├── models/
│   │   │   │       ├── ContactActivity.java
│   │   │   │       ├── DonationFormActivity.java
│   │   │   │       ├── DonorActivity.java
│   │   │   │       ├── HomeActivity.java
│   │   │   │       ├── HowToUseActivity.java
│   │   │   │       ├── MyDonationsActivity.java
│   │   │   │       ├── ReceiverActivity.java
│   │   │   │       ├── SplashActivity.java
│   │   │   │       └── WelcomeActivity.java
│   │   │   └── res/
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── README.md
```

## Getting Started

### Prerequisites

Before running the project, make sure you have:

- Android Studio
- JDK 8 or compatible Java environment
- Android SDK
- Android device or Android Emulator

### Installation

1. Clone the repository:

```bash
git clone git@github.com:sebrinamusbah/share-meal-app.git
```

2. Open the project in **Android Studio**.

3. Allow Gradle to synchronize and download the required dependencies.

4. Connect an Android device or start an Android Emulator.

5. Run the application from Android Studio.

## Build from Command Line

To build the debug version:

```bash
./gradlew assembleDebug
```

The generated APK will be available under:

```text
app/build/outputs/apk/debug/
```

## 📸 Screenshots

Screenshots will be added in a future update.

## 👩‍💻 Developer

**Sebrina Musbah**

Software Engineering Student | Full-Stack Developer | AI Enthusiast

GitHub: [@sebrinamusbah](https://github.com/sebrinamusbah)

## 📄 License

This project is available for educational and portfolio purposes.
