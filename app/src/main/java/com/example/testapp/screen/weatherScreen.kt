
import android.provider.CalendarContract.Attendees.query
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collect
import androidx.compose.runtime.snapshotFlow



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var currentCity by remember { mutableStateOf("City name") }
    var isloaded by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val weatherInfo = mapOf(
        "Country" to viewModel.name,
        "Temperature" to viewModel.temperature,
        "Wind speed" to viewModel.windSpeed + " m/s",
        "Feels like" to viewModel.feels_like,
        "humidity" to viewModel.humidity
    )
    LaunchedEffect(Unit) {
            focusRequester.requestFocus()
    }
    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(500)
            .collect { text ->
                if (text.isNotBlank()) {
                    viewModel.searchCity(text)
                }
            }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        TextField(
                            value = query,
                            onValueChange = {
                                query = it
                                expanded = it.isNotBlank()
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f).focusRequester(focusRequester).menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = {
                                Text(text = currentCity)
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(onClick = {
                            isloaded = true
                            if(query.isEmpty()){
                                currentCity = "Phnom Penh"
                                viewModel.getWeather(currentCity)
                            }else{
                                currentCity = query
                                viewModel.getWeather(currentCity)
                                query= ""
                            }

                        }) {
                            Text("Search")
                        }
                    }

                    ExposedDropdownMenu(
                        expanded = expanded && viewModel.suggestions.isNotEmpty()  ,
                        onDismissRequest = { expanded = false }
                    ) {
                        viewModel.suggestions.forEach { city ->
                            DropdownMenuItem(
                                text = { Text("${city.name}, ${city.country}") },
                                onClick = {
                                    isloaded = true
                                    query = city.name
                                    expanded = false
                                    currentCity = query
                                    viewModel.getWeather(currentCity) // 🔥 fetch weather
                                    query= ""

                                }
                            )
                        }
                    }


                }


            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()

        } else {
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Card (
                    modifier = Modifier.size(240.dp)
                ) {
                    if(isloaded){
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Text(
                                    text = viewModel.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                    )

                                Text(
                                    text = viewModel.temperature,
                                    style = MaterialTheme.typography.headlineLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()

                        Column (
                            modifier = Modifier.fillMaxSize()
                        ) {
                            weatherInfo.forEach { (key, value) ->
                                if (key.equals(
                                        "Country",
                                        ignoreCase = true
                                    ) || key.equals("Temperature", ignoreCase = true)
                                ) {
                                    return@forEach
                                } else {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text(
                                                text = "${key}: ",
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = value
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }else{
                        Box (
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text(text = "Please choose a city")
                            }
                        }
                    }



                }            }

        }
    }
}

