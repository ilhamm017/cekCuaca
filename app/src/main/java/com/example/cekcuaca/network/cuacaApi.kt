import androidx.compose.runtime.Composable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val API_KEY = "36203c926eabf687d9f530b063a03766" //API key yang digunakan untuk mendapatkan akses
private val BASE_URL = "https://api.openweathermap.org/data/2.5/" //URL API untuk mendapatkan data cuaca


private val retrofit = Retrofit.Builder()//Membuat objek retrovit builder
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface CuacaApiService { //membuat intervace yang melakukan http request get dengan endpoint weather dan parameter city dan api key untuk mendapatkan akses
    @GET("weather")
    fun getWeatherData(
        @Query("q") city: String,
        @Query("appid" ) apiKey: String
    ): Call<String>

}

fun getWeather(cityName: String) { //mendapatkan data cuaca
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
