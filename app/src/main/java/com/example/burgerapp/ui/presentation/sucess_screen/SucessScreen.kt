import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SuccessScreen(onGoBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Modal container
        Column(
            modifier = Modifier
                .width(340.dp)
                .height(400.dp)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(top = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Circle with Check
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape
                    )
                    .background(Color(0xFF4CAF50), shape = CircleShape),
                contentAlignment = Alignment.Center // center the check
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp) // size of the checkmark
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Success",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your payment was successful.\nA receipt for this purchase has been sent to your email.",
                fontSize = 16.sp,
                color = Color.Gray,
                lineHeight = 22.sp,
                modifier = Modifier.width(199.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onGoBack,
                modifier = Modifier
                    .width(220.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Go Back",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}
