package com.example.ws54_compose_prac2.service

import android.content.Context
import androidx.compose.ui.res.stringResource
import com.example.ws54_compose_prac2.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonService{


    fun getTranslatedCityName(cityName:String){

    }

    fun readJsonFromAssets(context: Context, fileName:String):String{
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    fun parseJson(jsonString: String): WeatherResponse? {

        val gson = Gson()
        return gson.fromJson(jsonString,WeatherResponse::class.java)
    }
}