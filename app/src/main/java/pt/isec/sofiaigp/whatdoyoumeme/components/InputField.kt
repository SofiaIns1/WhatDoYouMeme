package pt.isec.sofiaigp.whatdoyoumeme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.sofiaigp.whatdoyoumeme.R
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkLilac
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.Lilac

@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = DarkLilac,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        textStyle = TextStyle(textAlign = TextAlign.Center),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(Lilac),
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth()
    )
}

@Composable
fun StepperInputField(value: Int, onValueChange: (Int) -> Unit, placeholder: Int) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = { newValue ->
            onValueChange(newValue.toIntOrNull() ?: 0)
        },

        placeholder = {
            Text(
                text = placeholder.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = DarkLilac,
            )


        },
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = DarkLilac,
        ),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(Lilac),
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth(),

        leadingIcon = {
            IconButton(
                onClick = {
                    if (value > 0) {
                        onValueChange(value - 1)
                    }
                },
                modifier = Modifier.size(75.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_left),
                    tint = Color.Unspecified,
                    contentDescription = "Decrement",
                )
            }
        },
        trailingIcon = {


            IconButton(
                onClick = {
                    onValueChange(value + 1)
                },
                modifier = Modifier.size(75.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_right),
                    tint = Color.Unspecified,
                    contentDescription = "Increment",
                )
            }


        }
    )
}

@Composable
fun SearchInputField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(30.dp),

        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(30.dp)),

        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = DarkLilac,
            )


        },
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = DarkLilac,
        ),
        colors = OutlinedTextFieldDefaults.colors(Lilac),

        leadingIcon = {
            IconButton(
                onClick = {
                    //onValueChange
                    /*TODO*/
                },
                modifier = Modifier.size(75.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.search),
                    tint = Color.Unspecified,
                    contentDescription = "Decrement",
                )
            }
        },
    )
}