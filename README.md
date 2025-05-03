# MyProject

An Android application designed to make household chore management fair, engaging, and conflict-free for families.

## Project Overview

In many households, distributing chores among family members often leads to disputes, especially when motivation is lacking. This app aims to resolve these issues by gamifying household tasks: users earn points for completing chores, with the goal of increasing participation and reducing arguments.

Key features include:
- Parents define household tasks, their frequency, and whether rewards (e.g., monetary) are given at the end of each month.
- Family members rate each task from 1 to 5 stars based on personal preference.
- Tasks are assigned fairly, prioritizing preferred chores and allowing parents to boost point values for less desirable tasks.
- Chore assignments rotate monthly, and user point balances reset at month-end.
- Optional monthly rewards: parents receive reminders to compensate children according to points earned.

Additional interactive features:
- Users get chore notifications only when they are at home.
- Tasks become available again after a cooldown period once marked as completed.
- Users can negotiate chores among themselves:
  1. Delay a chore and lose some of its point value.
  2. Transfer a task to another available household member, offering them points in exchange.

The application is designed to be intuitive, accessible, and well-suited for Android smartphones.

## Features

- Task scheduling with `AlarmManager` and notifications
- Firebase integration (Authentication, Firestore, Storage)
- Activity results handling
- Countdown timer functionality
- Real-time location awareness
- Sound downloads (custom sounds per task)
- Multi-user support with negotiation system
- Dialogs and interactive menus

## Requirements

- Android Studio
- Git
- Java Development Kit (JDK)

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Let Gradle sync and install any required SDKs
4. Build and run the app on a connected device or emulator

## Project Structure

- `app/java/`: Application source code
- `app/res/`: Layouts, drawables, and values
- `AndroidManifest.xml`: App manifest
- `build.gradle`: Project and module configuration

## Building & Running

Use **Build > Build APK(s)** in Android Studio to generate the APK. To run the app:

- On a physical device: Enable developer mode and USB debugging, then connect and run via Android Studio
- On an emulator: Create and select a virtual device in Android Studio

---

This project is currently in development. Contributions, feedback, and feature suggestions are welcome.
