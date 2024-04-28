package pt.isec.sofiaigp.whatdoyoumeme.screens.common

import android.content.Context
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.sofiaigp.whatdoyoumeme.R
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun GameRulesScreen(context: Context, navController: NavHostController) {
    val gameRules = readGameRules(context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(Color.White)
            .padding(vertical = 60.dp, horizontal = 15.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .clip(RoundedCornerShape(size = 30.dp))
                .fillMaxSize()
                .background(DarkBlue)
                .padding(horizontal = 24.dp)
                .padding(top = 34.dp)
        ) {
            Row {
                Text(
                    "GAME RULES",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 15.dp)

                )

                Spacer(modifier = Modifier.padding(horizontal = 20.dp))

                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.close),
                        contentDescription = "Close",
                        tint = Color.Unspecified
                    )

                }
            }


            Spacer(modifier = Modifier.padding(17.dp))

            Text(
                text = gameRules,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Justify
            )

        }

    }


}

private fun readGameRules(context: Context): String {
    val inputStream = context.resources.openRawResource(R.raw.rules)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    var line: String? = reader.readLine()
    while (line != null) {
        stringBuilder.append(line).append("\n")
        line = reader.readLine()
    }
    reader.close()
    return stringBuilder.toString()
}

@Preview(showBackground = true)
@Composable
fun GameRulesScreenPreview() {
    // GameRulesScreen()
}