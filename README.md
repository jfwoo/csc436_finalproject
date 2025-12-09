Our app is designed to track calories of all the foods that the user is eating. It will also track the calories outtake found through exercise. You start by entering your name, age, height, weight, and average weekly exercise. The goal of this app is to help the user track what is a healthy amount of calories per day and give recommendations if their net calories are too high or too low.

Our Figma design can be found here: https://docs.google.com/document/d/1HA5RtkZdjhQNW6IH-0ANBc8EAAq39qBjknzrhRhL_yQ/edit?usp=sharing

There were a lot of Android and Jetpack Compose features that made our app possible. Some of the Jetpack Compose ones we used are Column, Row, Box, Spacers, clickable, Text, Fontweight, TextAlign, Button, Card, TextField, Horizontal Divider, MaterialTheme, remember, MutableStateOf, ComponentActivity, setContent, and enableEdgeToEdge. We did not have to use any third party libraries.

Additional dependancies used:
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui-text")
    implementation ("androidx.camera:camera-core:1.3.3")
    implementation ("androidx.camera:camera-camera2:1.3.3")
    implementation ("androidx.camera:camera-lifecycle:1.3.3")
    implementation ("androidx.camera:camera-view:1.3.3")
    implementation ("androidx.compose.ui:ui-viewbinding")

We also had to change our compileSdk to 36 and minSdk to 26.
