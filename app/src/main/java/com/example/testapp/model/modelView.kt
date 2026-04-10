import android.R.attr.description
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.GeoResponseItem
import com.example.testapp.data.RetrofitInstance
import com.example.testapp.data.RetrofitInstanceGeo
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    var temperature by mutableStateOf("")
    var windSpeed by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var description by mutableStateOf("")
    var country by mutableStateOf("")
    var name by mutableStateOf("")
    var feels_like by  mutableStateOf("")
    var humidity by mutableStateOf("")
    fun getWeather(city: String) {
        viewModelScope.launch {
            try {
                isLoading = true

                val result = RetrofitInstance.api.getWeather(
                    city = city,
                    apiKey = "e5cbf351b4a201a5641006401d4016d1",
                    units = "metric"
                )
                if(result.isSuccessful){
                    val success = result.body()
                    temperature = "${success?.main?.temp?.toInt()} °C"
                    description = success?.weather[0]?.description ?: "No data"
                    windSpeed = success?.wind?.speed.toString()
                    country = success?.sys?.country ?: "Unknown"
                    name  = success?.name ?: "Unknown"
                    feels_like = success?.main?.feels_like?.toString() + " °C"
                    humidity = success?.main?.humidity.toString()
                    isLoading = false
                } else {
                     if (result.code() == 404) {
                        description = "City not found"
                     } else {
                        description = "Error: ${result.code()}"
                     }
                }
            } catch (e: Exception) {
                isLoading = false
                description ="Error: ${e.message}"
                temperature = ""
                windSpeed =""
                country = ""
            }
        }
    }
    var suggestions by mutableStateOf<List<GeoResponseItem>>(emptyList())
    fun searchCity(city: String) {
        viewModelScope.launch {
            try {
                suggestions = RetrofitInstanceGeo.api.searchCity(
                    city,
                    apiKey = "e5cbf351b4a201a5641006401d4016d1"
                )
                Log.d("API_TEST", suggestions.toString())
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message.toString())
                suggestions = emptyList()
            }
        }
    }
}