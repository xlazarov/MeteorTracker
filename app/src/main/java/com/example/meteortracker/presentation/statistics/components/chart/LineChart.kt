package com.example.meteortracker.presentation.statistics.components.chart

import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meteortracker.R
import com.example.meteortracker.presentation.statistics.viewModel.MeteoriteChartViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

/**
 * Displays a line chart visualizing [viewModel]'s observed data.
 */
@Composable
fun LineChart(
    viewModel: MeteoriteChartViewModel = hiltViewModel(),
    dataName: String,
    chartName: String,
    modifier: Modifier = Modifier,
    chartColor: Color = colorScheme.primary,
    colorFill: Color = colorScheme.secondary,
    textColor: Color = colorScheme.onBackground,
) {
    val chartData by viewModel.chartData.observeAsState(emptyList())
    if (chartData.isEmpty()) Text(text = stringResource(id = R.string.no_data))

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        factory = { context ->
            val chart =
                LineChart(ContextThemeWrapper(context, R.style.Theme_MeteorTracker)).apply {
                    val entries: List<Entry> = chartData
                        .filterNot { it.data == null }
                        .sortedBy { it.data.toFloat() }
                        .map { Entry(it.data.toFloat(), it.count.toFloat()) }

                    val dataSet = LineDataSet(entries, chartName).apply {
                        color = chartColor.toArgb()
                        setDrawValues(true)
                        setDrawCircles(true)
                        setDrawFilled(true)
                        fillColor = colorFill.toArgb()
                        fillAlpha = 200
                    }
                    data = LineData(dataSet)

                    setTouchEnabled(true)
                    isDragEnabled = true
                    isScaleXEnabled = true
                    isScaleYEnabled = false
                    isDragXEnabled = true

                    description.isEnabled = true
                    description.text = dataName
                    description.textColor = textColor.toArgb()
                    legend.isEnabled = true
                    legend.textColor = textColor.toArgb()

                    axisLeft.textColor = textColor.toArgb()
                    axisRight.isEnabled = false
                    xAxis.textColor = textColor.toArgb()
                    xAxis.position = XAxis.XAxisPosition.BOTTOM

                    invalidate()
                }
            chart
        }
    )
}
