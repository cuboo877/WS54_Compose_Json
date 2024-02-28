package com.example.ws54_compose_prac2.service

import android.content.Context
import android.content.SharedPreferences
import android.view.Display.Mode

class PreferenceManager(context: Context){
    private val sharedPreferences:SharedPreferences = context.getSharedPreferences("Selected Region",Context.MODE_PRIVATE)

    fun addData(region:String){
        val addedCitySet = getCitySet().plus(region)
        val editor = sharedPreferences.edit()
        editor.putStringSet("CityList",addedCitySet)
        editor.apply()
    }

    fun removeData(region:String){
        val removedCitySet = getCitySet().minus(region)
        val editor = sharedPreferences.edit()
        editor.putStringSet("CityList",removedCitySet)
        editor.apply()
    }

    fun getCitySet(): Set<String> {
        val defaultValue = setOf("")
        return sharedPreferences.getStringSet("CityList", defaultValue) ?: defaultValue
    }
}