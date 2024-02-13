# Animated Clock App with Canvas and Value Animator

## Description
This is an animated clock app built using Canvas and Value Animator in Android. The app provides a customizable interface where users can adjust attributes such as gradient colors, clock text color, and size through the `attrs.xml` and `dimens.xml` files. Additionally, the app utilizes location tracking to obtain the current location of the user, which is then used to fetch weather data from an API to display the current temperature.

## Features
- Animated clock display
- Customizable attributes:
  - Gradient colors
  - Clock text color and size
- Location tracking for current location detection
- Integration with weather API to display current temperature

## Screenshots
![Animated Clock](screenshots/animated_clock.png)

## Customization
### Attributes Customization
You can customize various attributes of the clock by modifying values in the attrs.xml and dimens.xml files located in the res/values directory.

attrs.xml
```
clockGradientStartColor: Start color of the clock gradient.
clockGradientEndColor: End color of the clock gradient.
clockTextColor: Color of the clock text.
```
dimens.xml
```
clockTextSize: Size of the clock text.
clockTextSize: Size of the clock text.
```
Ensure that the necessary permissions for location access are added to the AndroidManifest.xml file.

## Requirements
- Android Studio 4.0 or higher
- Android SDK with minimum SDK version 21 (Android 5.0 Lollipop)
