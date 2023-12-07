package com.example.cekcuaca

import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.Composable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cekcuaca.ui.theme.CekCuacaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CekCuacaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CekCuaca()
                }
            }
        }
    }
}

private val API_KEY = "36203c926eabf687d9f530b063a03766"
private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CekCuaca( modifier: Modifier = Modifier) {
    var cityName by remember { mutableStateOf("")}
        Column {
            TextField(
                value = cityName ,
                onValueChange = { cityName = it},
                label = { Text("Masukkan nama kota")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    getWeatherData(cityName)
                }
            ) {
                Text("OK")
            }
        }
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface CuacaApiService {
    @GET("weather")
    fun getWeatherData(
        @Query("q") city: String,
        @Query("appid" ) apiKey: String
    ): Call<String>

}

fun getWeatherData(cityName: String) {
    val service = retrofit.create(CuacaApiService::class.java)
    val call = service.getWeatherData(cityName, API_KEY)

    call.enqueue(object : Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            if (response.isSuccessful) {
                val data = response.body()
                println("Data cuaca: $data")
            } else {
                println("Gagal mendapatkan data cuaca: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            println("Terjadi kesalahan: ${t.message}")
        }

    })
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CekCuacaTheme {
        CekCuaca()
    }
}