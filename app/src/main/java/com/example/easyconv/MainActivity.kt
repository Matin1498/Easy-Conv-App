package com.example.easyconv

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easyconv.ui.theme.EasyConvTheme
import java.time.LocalDate
import java.util.*

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyConvTheme {
                var selectedTab by remember { mutableStateOf(0) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (selectedTab) {
                                        0 -> "Easy Conv"
                                        1 -> "Easy Conv"
                                        2 -> "Easy Conv"
                                        else -> ""
                                    }
                                )
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                label = { Text("Units") },
                                icon = {}
                            )
                            NavigationBarItem(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                label = { Text("Weather") },
                                icon = {}
                            )
                            NavigationBarItem(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                label = { Text("Date") },
                                icon = {}
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    when (selectedTab) {
                        0 -> UnitConverter(modifier = Modifier.padding(innerPadding))
                        1 -> WeatherConverter(modifier = Modifier.padding(innerPadding))
                        2 -> DateConverter(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun UnitConverter(modifier: Modifier = Modifier) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Select...") }
    var toUnit by remember { mutableStateOf("Select...") }
    var result by remember { mutableStateOf("") }

    val units = listOf("Km", "m", "cm", "mm", "Mile", "Yard", "Ft", "In")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Unit Converter", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Enter value") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Text("From", style = MaterialTheme.typography.bodyLarge)
        DropdownMenuWithItems(units, fromUnit) { fromUnit = it }

        Text("To", style = MaterialTheme.typography.bodyLarge)
        DropdownMenuWithItems(units, toUnit) { toUnit = it }

        Button(
            onClick = {
                val input = inputValue.toDoubleOrNull()
                result = if (input != null) {
                    String.format("%.2f", convertLength(input, fromUnit, toUnit))
                } else {
                    "Invalid input"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }

        AnimatedVisibility(visible = result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(result, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}

@Composable
fun WeatherConverter(modifier: Modifier = Modifier) {
    var temperatureInput by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Celsius") }
    var toUnit by remember { mutableStateOf("Fahrenheit") }
    var result by remember { mutableStateOf("") }

    val units = listOf("Celsius", "Fahrenheit")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Weather Converter", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = temperatureInput,
            onValueChange = { temperatureInput = it },
            label = { Text("Enter temperature") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Text("From", style = MaterialTheme.typography.bodyLarge)
        DropdownMenuWithItems(units, fromUnit) { fromUnit = it }

        Text("To", style = MaterialTheme.typography.bodyLarge)
        DropdownMenuWithItems(units, toUnit) { toUnit = it }

        Button(
            onClick = {
                val temp = temperatureInput.toDoubleOrNull()
                result = if (temp != null) {
                    val converted = when {
                        fromUnit == "Celsius" && toUnit == "Fahrenheit" -> (temp * 9 / 5) + 32
                        fromUnit == "Fahrenheit" && toUnit == "Celsius" -> (temp - 32) * 5 / 9
                        else -> temp
                    }
                    String.format("%.2f", converted)
                } else {
                    "Invalid input"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }

        AnimatedVisibility(visible = result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(result, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateConverter(modifier: Modifier = Modifier) {
    var selectedGregorianDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedJalaliDate by remember { mutableStateOf(Triple(1402, 1, 1)) } // y, m, d
    var result by remember { mutableStateOf("") }
    var convertTo by remember { mutableStateOf("Gregorian") }

    val options = listOf("Gregorian", "Iranian")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Date Converter", style = MaterialTheme.typography.headlineMedium)

        Text("Convert to:", style = MaterialTheme.typography.bodyLarge)
        DropdownMenuWithItems(options, convertTo) { convertTo = it }

        if (convertTo == "Gregorian") {
            // User selects Jalali date to convert to Gregorian
            JalaliDateSelector(selectedJalaliDate) { selectedJalaliDate = it }
        } else {
            // User selects Gregorian date to convert to Jalali
            DatePickerDialog(initialDate = selectedGregorianDate) { selectedGregorianDate = it }
        }

        Button(
            onClick = {
                result = if (convertTo == "Gregorian") {
                    jalaliToGregorian(selectedJalaliDate)
                } else {
                    gregorianToJalali(selectedGregorianDate)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }

        AnimatedVisibility(visible = result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(result, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}

@Composable
fun JalaliDateSelector(
    selectedDate: Triple<Int, Int, Int>,
    onDateSelected: (Triple<Int, Int, Int>) -> Unit
) {
    val years = (1300..1500).toList().map { it.toString() }
    val months = (1..12).map { it.toString() }
    val days = (1..31).map { it.toString() }

    var year by remember { mutableStateOf(selectedDate.first.toString()) }
    var month by remember { mutableStateOf(selectedDate.second.toString()) }
    var day by remember { mutableStateOf(selectedDate.third.toString()) }

    Text("Select Jalali Date", style = MaterialTheme.typography.bodyLarge)

    DropdownMenuWithItems(years, year) {
        year = it
        onDateSelected(Triple(year.toInt(), month.toInt(), day.toInt()))
    }
    DropdownMenuWithItems(months, month) {
        month = it
        onDateSelected(Triple(year.toInt(), month.toInt(), day.toInt()))
    }
    DropdownMenuWithItems(days, day) {
        day = it
        onDateSelected(Triple(year.toInt(), month.toInt(), day.toInt()))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(initialDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, initialDate.year)
        set(Calendar.MONTH, initialDate.monthValue - 1)
        set(Calendar.DAY_OF_MONTH, initialDate.dayOfMonth)
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    Button(onClick = {
        DatePickerDialog(context, { _, y, m, d ->
            onDateSelected(LocalDate.of(y, m + 1, d))
        }, year, month, day).show()
    }) {
        Text("Select Date: ${initialDate.toString()}")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun gregorianToJalali(date: LocalDate): String {
    val gy = date.year
    val gm = date.monthValue
    val gd = date.dayOfMonth

    val gDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

    var gy2 = gy - 1600
    var gm2 = gm - 1
    var gd2 = gd - 1

    var gDayNo = 365 * gy2 + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400
    for (i in 0 until gm2) gDayNo += gDaysInMonth[i]
    if (gm2 > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))) gDayNo++
    gDayNo += gd2

    var jDayNo = gDayNo - 79
    val jNp = jDayNo / 12053
    jDayNo %= 12053

    var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
    jDayNo %= 1461

    if (jDayNo >= 366) {
        jy += (jDayNo - 1) / 365
        jDayNo = (jDayNo - 1) % 365
    }

    var i = 0
    while (i < 11 && jDayNo >= jDaysInMonth[i]) {
        jDayNo -= jDaysInMonth[i]
        i++
    }

    val jm = i + 1
    val jd = jDayNo + 1

    return "$jy/$jm/$jd"
}

@RequiresApi(Build.VERSION_CODES.O)
fun jalaliToGregorian(date: Triple<Int, Int, Int>): String {
    val (jy, jm, jd) = date

    val j_days_in_month = intArrayOf(0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

    var gy: Int
    var gm: Int
    var gd: Int

    var jy2 = jy - 979
    var jm2 = jm - 1
    var jd2 = jd - 1

    var j_day_no = 365 * jy2 + (jy2 / 33) * 8 + ((jy2 % 33 + 3) / 4)
    for (i in 0 until jm2) j_day_no += j_days_in_month[i + 1]
    j_day_no += jd2

    var g_day_no = j_day_no + 79

    var gy2 = 1600 + 400 * (g_day_no / 146097)
    g_day_no %= 146097

    var leap = true
    if (g_day_no >= 36525) {
        g_day_no--
        gy2 += 100 * (g_day_no / 36524)
        g_day_no %= 36524

        if (g_day_no >= 365) g_day_no++ else leap = false
    }

    gy2 += 4 * (g_day_no / 1461)
    g_day_no %= 1461

    if (g_day_no >= 366) {
        leap = false
        g_day_no--
        gy2 += g_day_no / 365
        g_day_no %= 365
    }

    val g_days_in_month = intArrayOf(31, if (leap) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    var i = 0
    while (i < 12 && g_day_no >= g_days_in_month[i]) {
        g_day_no -= g_days_in_month[i]
        i++
    }

    gm = i + 1
    gd = g_day_no + 1

    return "$gy2/$gm/$gd"
}

@Composable
fun DropdownMenuWithItems(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedItem)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { label ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onItemSelected(label)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun convertLength(value: Double, from: String, to: String): Double {
    val meterValue = when (from) {
        "m" -> value
        "mm" -> value * 0.001
        "cm" -> value * 0.01
        "Km" -> value * 1000
        "Mile" -> value * 1609.34
        "Yard" -> value * 0.9144
        "Ft" -> value * 0.3048
        "In" -> value * 0.0254
        else -> value
    }

    return when (to) {
        "m" -> meterValue
        "mm" -> meterValue / 0.001
        "cm" -> meterValue / 0.01
        "Km" -> meterValue / 1000
        "Mile" -> meterValue / 1609.34
        "Yard" -> meterValue / 0.9144
        "Ft" -> meterValue / 0.3048
        "In" -> meterValue / 0.0254
        else -> meterValue
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConverters() {
    EasyConvTheme {
        UnitConverter()
    }
}