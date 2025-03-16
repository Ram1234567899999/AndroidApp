package com.kumar.androidapp.data

import com.kumar.androidapp.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class CountryRepository {
    suspend fun fetchCountries(): Result<List<Country>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json")
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val countries = parseCountriesJson(response)
                    Result.success(countries)
                } else {
                    Result.failure(Exception("HTTP Error: $responseCode"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun parseCountriesJson(jsonString: String): List<Country> {
        val countries = mutableListOf<Country>()
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val country = Country(
                name = jsonObject.optString("name", "Unknown"),
                region = jsonObject.optString("region", "Unknown"),
                code = jsonObject.optString("code", "??"),
                capital = jsonObject.optString("capital", "Unknown")
            )
            countries.add(country)
        }

        return countries
    }
}