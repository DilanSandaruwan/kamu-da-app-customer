# Kamu Da - Customer App

<img src="docs/customerapp_clip.gif" width="320">

## Overview

Welcome to **Kamu Da - Customer App** – your go-to solution for convenient, nutritious, and reliable
meal options delivered straight to your doorstep.

### Key Features

- **Diverse Meal Options**: Browse through a diverse menu offering a range of nutritious and
  delicious meals prepared by carefully selected houses.

- **Seamless Ordering System**: Effortlessly place and manage your meal orders directly through the
  app, bringing the convenience of restaurant-style dining to your fingertips.

- **Order Control**: Enjoy the flexibility of canceling orders (before acceptance by the food house)
  and easily reorder your favorite meals with just a few taps.

- **Real-Time Order Tracking**: Stay updated with the latest status of your ongoing order directly
  on the home screen.

Whether you're a busy professional, a student, or simply someone who appreciates a wholesome
meal, **Kamu Da - Customer App** ensures a delightful dining experience. Join our community and
savor the convenience of nutritious meals prepared with care and reliability.

## Screenshots

Include screenshots of your app to give users a visual representation of its interface and features.

## Prerequisites

- IDE：Android Studio Flamingo | 2022.2.1 Patch 2
- Kotlin：1.6.21
- Java：8
- Gradle：8.0
- minSdk：24
- targetSdk：33

## Installation

1. Clone the repository.
   ```bash
   git clone https://github.com/DilanSandaruwan/kamu-da-app-customer.git

## Configuration

To configure URLs for your app, follow these steps:

1. Create a package, **`mysecret`** in the same level where constant,di,model etc. exists.
2. Create a kotlin file **`MySecret.kt`** within the **`mysecret`** package.
3. Include host server URL. (this URL is addressed within `constant/NetworkConstant`)
4. Save the file.
5. Ensure that the **`mysecret`** package is listed in your `.gitignore` file to avoid accidentally
   exposing sensitive information.