package com.example.ws54_compose_prac2.service

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ws54_compose_prac2.R

object GetTranslatedCityName{
    @Composable
    fun getName(name:String): String? {
        val translatedCityMap =  mapOf(
            "Ayo" to stringResource(id = R.string.ayo),
            "Taipei" to stringResource(id = R.string.taipei),
            "TestCity" to stringResource(id = R.string.testcity),
            "Yee" to stringResource(id = R.string.yee)
        )
        return translatedCityMap[name]
    }
}