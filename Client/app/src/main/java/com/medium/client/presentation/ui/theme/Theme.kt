package com.medium.client.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = ChathamsBlue,
    primaryVariant = RockBlue,
    onPrimary = White,
    secondary = Solitude,
    secondaryVariant = Gray,
    surface = LightGray,
    background = White,
    onBackground = Black,
    error = Red,
    onError = Orange
)

private val LightColorPalette = lightColors(
    primary = ChathamsBlue,
    primaryVariant = RockBlue,
    onPrimary = White,
    secondary = Solitude,
    secondaryVariant = Gray,
    surface = LightGray,
    background = White,
    onBackground = Black,
    error = Red,
    onError = Orange

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ChatAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
    val colors = LightColorPalette
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(colors.primary)

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}