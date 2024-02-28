package com.example.ws54_compose_prac2.page

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ws54_compose_prac2.R
import com.example.ws54_compose_prac2.service.Current
import com.example.ws54_compose_prac2.service.DayData
import com.example.ws54_compose_prac2.service.GetIconIdByDescription
import com.example.ws54_compose_prac2.service.GetTranslatedDescription
import com.example.ws54_compose_prac2.service.HourData
import com.example.ws54_compose_prac2.service.WeatherResponse
import com.example.ws54_compose_prac2.ui.theme.Averta
import com.example.ws54_compose_prac2.ui.theme.labelSmall
import com.example.ws54_compose_prac2.ui.theme.widgetLabelSmall
import com.example.ws54_compose_prac2.widget.HomeAppBar
import com.example.ws54_compose_prac2.widget.NavDrawerContent
import kotlinx.coroutines.CoroutineScope
import java.time.LocalTime

object HomePage{
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun build(data:WeatherResponse,navController: NavController){
        val lazyListState = rememberLazyListState()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) //!!
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                NavDrawerContent.build(navController = navController,scope = scope, drawerState = drawerState)
            } })
        {
            Scaffold(
                modifier = Modifier.fillMaxSize(), topBar = {
                    HomeAppBar.build(
                        region = stringResource(id = R.string.current_location),
                        scope = scope,
                        drawerState = drawerState
                    )
                }
            ) {
                val currentWeatherData = data.Taichung.current
                val forecastWeatherDataList = data.Taichung.forecast
                val hourlyWeatherDataList=  data.Taichung.forecast.hourly
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp, top = 56.dp, 10.dp, 10.dp),
                    state = lazyListState
                ) {
                    item {
                        _buildCurrentTemp(currentWeatherData)
                        _buildMaxAndMinTemp(data = data)
                        Spacer(modifier = Modifier.height(20.dp))
                        _buildTempPerHour(data = hourlyWeatherDataList, scope)
                        Spacer(modifier = Modifier.height(20.dp))
                        _buildTempInTenDays(data)
                        Spacer(modifier = Modifier.height(20.dp))
                        _buildPM25_And_Uv(data = currentWeatherData)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

//--------------------------------------------------------------------------------------

//現在溫度+icon
@Composable
fun _buildCurrentTemp(data: Current) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "${data.temp_c}°",
                fontSize = 80.sp,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-8).sp,
                fontWeight = FontWeight.Light,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "${GetTranslatedDescription.getDescription(description = data.description)}",
                    modifier = Modifier.padding(start = 5.dp),
                    style = labelSmall
                )
            }
        }
        Image(
           painter =  painterResource(id = GetIconIdByDescription.getIconId(data.description)),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
        )
    }
}

// 今日最高最低溫度+體感溫度
@Composable
fun _buildMaxAndMinTemp(data: WeatherResponse){

    Text(text = "${data.Taichung.forecast.day[0].maxTemp_c}° / " +
            "${data.Taichung.forecast.day[0].minTemp_c}°  ${stringResource(id = R.string.feels_like)} " +
            "${data.Taichung.current.temp_c}°",
        style = labelSmall,
        modifier = Modifier
            .padding(start = 5.dp)
            .padding(start = 20.dp)
    )
}

//--------------------------------------------------------------------------------------
//每小時溫度區
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun _buildTempPerHour(data: List<HourData>, scope: CoroutineScope){
    val lazyListState = rememberLazyListState()
    val hoursData = data //list

    val coroutineScope = rememberCoroutineScope()
    var hourTime = LocalTime.now().hour
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .height(185.dp)
            .clip(RoundedCornerShape(size = 15.dp))
            .background(color = Color.Gray.copy(alpha = 0.5f))
            .padding(5.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
            ,horizontalArrangement = Arrangement.Center) {
            Text(text = stringResource(id = R.string.hourly_weather), fontFamily = Averta, color = Color.White, fontSize = 15.sp, textAlign = TextAlign.Center)
        }
        Divider(color = Color.White, modifier = Modifier
            .fillMaxWidth(0.92f)
            .padding(5.dp))
        LazyRow(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
            state = lazyListState,
            ) {
            items(hoursData.size)
            {
                _buildPerHourData(data = hoursData[it])
            }
        }

        LaunchedEffect(scope){
            lazyListState.scrollToItem(hourTime)
        }
    }
}

// 每小時溫度小物件
@Composable
fun _buildPerHourData(data:HourData){
    Column(verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(5.dp)
    ) {
        Text(text = data.time.takeLast(5), fontSize = 15.sp, color = Color.White)
        Image(painter = painterResource(id = GetIconIdByDescription.getIconId(data.description)), contentDescription = null, modifier = Modifier.size(57.dp))
        Text(text = "${data.temp_c}°", fontFamily = Averta, color = Color.White, fontSize = 15.sp)
        Text(text = "${data.daily_chance_of_rain}%", fontFamily = Averta, color = Color.White, fontSize = 15.sp)
    }
}

//--------------------------------------------------------------------------------------
//10天內天氣狀態區域
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun _buildTempInTenDays(data: WeatherResponse){
    val daysData = data.Taichung.forecast.day //List<forecastDay>
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(15.dp))
        .background(color = Color.Gray.copy(alpha = 0.5f))
        .padding(10.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Row(
            Modifier
                .fillMaxWidth()
            ,horizontalArrangement = Arrangement.Center) {
            Text(text = stringResource(id = R.string.weather_in_ten_days), fontFamily = Averta, color = Color.White, fontSize = 15.sp, textAlign = TextAlign.Center)
        }
        Divider(color = Color.White, modifier = Modifier
            .fillMaxWidth(0.92f)
            .padding(5.dp))
        daysData.forEach(){
            _buildTempInTenDaysData(data = it)
        }
    }
}

//10天內天氣狀態
@Composable
fun _buildTempInTenDaysData(data: DayData){
    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),) {
        Row(Modifier.width(80.dp)) {
            Text(
                text = data.date
                    .takeLast(5)
                    .replace("-","/"),
                style = widgetLabelSmall
            )
        }
        Row(
            Modifier.width(60.dp)
        ){
            Text(
                text = "${data.daily_chance_of_rain}%",
                style = widgetLabelSmall
            )
        }

        Image(painter = painterResource(id = GetIconIdByDescription.getIconId(data.description)), contentDescription = null, modifier = Modifier.size(45.dp))
        Row(Modifier.width(60.dp)){
            Text(text = "${data.maxTemp_c}°", style = widgetLabelSmall)
        }
        Row(Modifier.width(60.dp)){
            Text(text = "${data.minTemp_c}°", style = widgetLabelSmall)
        }

    }
}

//--------------------------------------------------------------------------------------
//空氣品質
@Composable
fun _buildPM25_And_Uv(data: Current){
    Row(Modifier.fillMaxWidth() ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .clip(RoundedCornerShape(15.dp))
                .background(color = Color.Gray.copy(alpha = 0.5f))
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,)
        {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),horizontalArrangement = Arrangement.Center) {
                Text(text = "PM2.5", fontFamily = Averta, color = Color.White, fontSize = 15.sp, textAlign = TextAlign.Center)
            }
            Divider(color = Color.White, modifier = Modifier.fillMaxWidth(0.92f))
            Text(data.pm25.toString(),fontFamily = Averta, color = Color.White, fontSize = 40.sp,)
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(color = Color.Gray.copy(alpha = 0.5f))
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,)
        {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),horizontalArrangement = Arrangement.Center) {
                Text(text = "UV", fontFamily = Averta, color = Color.White, fontSize = 15.sp, textAlign = TextAlign.Center)
            }
            Divider(color = Color.White, modifier = Modifier.fillMaxWidth(0.92f))
            Text(data.uv.toString(),fontFamily = Averta, color = Color.White, fontSize = 40.sp,)
        }
    }
}