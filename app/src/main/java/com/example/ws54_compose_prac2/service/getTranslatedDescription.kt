package com.example.ws54_compose_prac2.service

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ws54_compose_prac2.R

object GetTranslatedDescription{
    @Composable
    fun getDescription(description:String): String? {
        val descriptionMap = mapOf(
            "sunny" to stringResource(id = R.string.sunny),
            "rainy" to stringResource(id = R.string.middle_rain),
            "cloudy" to stringResource(id = R.string.cloudy)
        )
        return descriptionMap[description]
    }
}