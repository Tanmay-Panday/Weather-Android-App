package com.example.mausam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mausam.api.WeatherModel

@Composable
fun WeatherDetails(data : WeatherModel) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            // Location Icon
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription ="Location Icon",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(3.dp))

            // Current Location Details
            Column {
                Text(text = data.location.name, fontSize = 30.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = data.location.region, fontSize = 20.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            // Temperature Icon
            Icon(
                modifier = Modifier.size(46.dp),
                imageVector = Icons.Filled.Thermostat,
                contentDescription = "Temperature Icon",
            )
            // displaying current temperature
            Text(
                text = "${data.current.temp_c}°C",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }


        // current weather condition icon
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "condition icon"
        )

        // current weather condition text
        Text(
            text = data.current.condition.text,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Displaying weather details
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(key = "Humidity", value = data.current.humidity)
                    WeatherKeyValue(key = "Wind Speed", value = "${data.current.wind_kph} km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(key = "UV", value = data.current.uv)
                    WeatherKeyValue(key = "Precipitation", value = "${data.current.precip_mm} mm")
                }
            }
        }
        WeatherAdvice(data = data)
    }
}

@Composable
fun WeatherKeyValue(key: String, value: String) {
    Column (
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}

@Composable
fun WeatherAdvice(data: WeatherModel) {
    val advice = when {
        data.current.condition.text.contains("rain", ignoreCase = true) -> "Take an umbrella."
        (data.current.temp_c).toDouble() <= 15 -> "Take a coat. It's cool outside."
        data.current.condition.text.contains("dust", ignoreCase = true) && (data.current.precip_mm).toDouble() == 0.0 -> "Take a mask. It might be dusty."
        data.current.condition.text.contains("sunny", ignoreCase = true) -> {
            if ((data.current.temp_c).toDouble() >= 30) {
                "Take sunglasses and a water bottle. It's very hot and sunny!"
            } else {
                "Take sunglasses. It's sunny."
            }
        }
        else -> "No specific advice. Enjoy your day!"
    }

    // Display the advice
    Text(
        text = advice,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 16.dp)
    )
}
