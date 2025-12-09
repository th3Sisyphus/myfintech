package com.example.myfintech.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val BackgroundColor = Color(0xFFF9FAFB)
val PurpleDark = Color(0xFF3F51B5)
val PurpleLight = Color(0xFF673AB7)
val TextGray = Color(0xFF4A5565)
val TextGrayHint = Color(0xFF717182)

val buttonGradient = Brush.horizontalGradient(
    colors = listOf(PurpleDark, PurpleLight)
)

val fogGradient = Brush.linearGradient(
    0f to Color(0xff2b7fff).copy(alpha = 0.2f),
    0.5f to Color(0xffad46ff).copy(alpha = 0.2f),
    1f to Color(0xff2b7fff).copy(alpha = 0.2f)
)