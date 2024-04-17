package pt.isec.sofiaigp.whatdoyoumeme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.isec.sofiaigp.whatdoyoumeme.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoBackBar(navController: NavHostController){
    TopAppBar(
        title = {
        Text(text = "")
    },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
       navigationIcon = {
           IconButton(
               onClick = { navController.navigateUp() },
               modifier = Modifier.size(40.dp)
           ) {
               Icon(
                   imageVector = ImageVector.vectorResource(id = R.drawable.arrow_circle_left),
                   contentDescription = "Close",
                   tint = Color.Unspecified
               )

           }
       }
    
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoBackBarWhite(navController: NavHostController){
    TopAppBar(
        title = {
            Text(text = "")
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_circle_left),
                    contentDescription = "Close",
                    tint = Color.White
                )

            }
        }

    )
}

@Preview(showBackground = true)
@Composable
fun GoBackBarPreview(){
    GoBackBar(rememberNavController())
}


