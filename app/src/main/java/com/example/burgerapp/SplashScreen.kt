package com.example.burgerapp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.burgerapp.R
import com.example.burgerapp.ui.theme.LobsterFont


@Composable
fun SplashScreen() {
  //  val lobsterFont = FontFamily(Font(R.font.lobster_regular))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // Centered title
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = LobsterFont,
                fontSize = 60.sp,
                color = Color.White
            )
        }

        // Loader2: extreme bottom-left
        Image(
            painter = painterResource(id = R.drawable.splash_image_left2),
            contentDescription = "Loader 2",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-42).dp) // horizontal offset only
                .size(width = 246.dp, height = 288.dp)
        )

        // Loader1: also sticks to bottom, overlapping Loader2 a bit
        Image(
            painter = painterResource(id = R.drawable.splash_image_right1),
            contentDescription = "Loader 1",
            modifier = Modifier
                .align(Alignment.BottomStart) // sticks to bottom
                .offset(x = 134.dp) // horizontal shift
                .size(width = 202.dp, height = 202.dp)
                .zIndex(1f) // drawn above loader2
        )
    }
}
