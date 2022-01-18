package com.gulshan.moneyfront.ui.portfolio

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gulshan.moneyfront.R
import androidx.fragment.app.Fragment
import com.google.android.material.composethemeadapter.MdcTheme
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.roundToInt

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Portfolio : Fragment(R.layout.fragment_portfolio) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView: ComposeView = view.findViewById(R.id.main_composeview)

        composeView.setContent {
            MdcTheme {
                Portfolio(
                    Modifier,
                    listOf(25f, 45f, 45f),
                    listOf(Color.Green, Color.Red, Color.Cyan),
                    currVal = "$12,345.33"
                )
            }
        }
    }

    @Composable
    fun Portfolio(
        modifier: Modifier,
        progress: List<Float>,
        colors: List<Color>,
        percentColor: Color = Color.White,
        currVal: String = "$123252"
    ) {
        val scrollState = rememberScrollState()
        val horizontalScrollState = rememberScrollState()

        if (progress.isEmpty() || progress.size != colors.size) return

        val total = progress.sum()
        val proportions = progress.map {
            it * 100 / total
        }
        val angleProgress = proportions.map {
            360 * it / 100
        }

        val progressSize = mutableListOf<Float>()
        progressSize.add(angleProgress.first())

        for (x in 1 until angleProgress.size)
            progressSize.add(angleProgress[x] + progressSize[x - 1])

        var activePie by remember {
            mutableStateOf(-1)
        }

        var startAngle = 270f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState, true)
                .padding(20.dp)
        ) {

            BoxWithConstraints(modifier = modifier) {

                val sideSize = min(constraints.maxWidth, constraints.maxHeight)
                val padding = (sideSize * 30) / 100f

                val pathPortion = remember {
                    Animatable(initialValue = 0f)
                }
                LaunchedEffect(key1 = true) {
                    pathPortion.animateTo(
                        1f, animationSpec = tween(1000)
                    )
                }

                val size = Size(sideSize.toFloat() - padding, sideSize.toFloat() - padding)

                Canvas(
                    modifier = Modifier
                        .width(sideSize.dp)
                        .height(sideSize.dp / 3)
                        .pointerInput(true)
                        {


                            detectTapGestures {
                                val clickedAngle = convertTouchEventPointToAngle(
                                    sideSize.toFloat(),
                                    sideSize.toFloat(),
                                    it.x,
                                    it.y
                                )
                                progressSize.forEachIndexed { index, item ->
                                    if (clickedAngle <= item) {
                                        if (activePie != index)
                                            activePie = index

                                        return@detectTapGestures
                                    }
                                }
                            }
                        }
                ) {

                    angleProgress.forEachIndexed { index, arcProgress ->
                        drawPie(
                            colors[index],
                            startAngle,
                            arcProgress * pathPortion.value,
                            size,
                            padding = padding,
                        )
                        startAngle += arcProgress
                    }

                    if (activePie != -1)
                        drawContext.canvas.nativeCanvas.apply {
                            val fontSize = 60.toDp().toPx()
                            drawText(
                                "${proportions[activePie].roundToInt()}%",
                                (sideSize / 2) + fontSize / 4, (sideSize / 2) + fontSize / 3,
                                Paint().apply {
                                    color = percentColor.toArgb()
                                    textSize = fontSize
                                    textAlign = Paint.Align.CENTER
                                }
                            )
                        }
                }
                Column(
                    modifier = Modifier
                        .width(this.maxWidth)
                        .height(this.maxWidth),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Current Value")
                    Text(text = currVal, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                }


            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = "$54466",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Invested Value",
                    textAlign = TextAlign.Center
                )
            }



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    text = "Templates",
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState, true),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CardItem(
                    name = "Equity",
                    code = "#11189",
                    date = "22 Dec 2021",
                    bal = "$1245",
                    color = Color.LightGray
                )
                CardItem(
                    name = "Cash",
                    code = "#11432",
                    date = "22 Dec 2022",
                    bal = "$31245",
                    color = Color.Green
                )
            }


        }


    }


    private fun DrawScope.drawPie(
        color: Color,
        startAngle: Float,
        arcProgress: Float,
        size: Size,
        padding: Float,
    ): Path {

        return Path().apply {
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = arcProgress,
                useCenter = false,
                size = size,
                style = Stroke(
                    width = 50f,
                    miter = 1000f,
                    cap = StrokeCap.Square,

                    ),

                topLeft = Offset(padding / 2, padding / 2)
            )
        }
    }

    private fun convertTouchEventPointToAngle(
        width: Float,
        height: Float,
        xPos: Float,
        yPos: Float
    ): Double {
        val x = xPos - (width * 0.5f)
        val y = yPos - (height * 0.5f)

        var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
        angle = if (angle < 0) angle + 360 else angle
        return angle
    }

    @Composable
    fun CardItem(
        name: String,
        code: String,
        date: String,
        bal: String,
        color: Color
    ) {
        Card(

            modifier = Modifier
                .height(130.dp)
                .width(160.dp)
                .background(color.copy(alpha = 0.2f))
                .clipToBounds(),
            shape = RoundedCornerShape(20.dp),
            elevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .background(color.copy(alpha = 0.2f))
            ) {
                Text(
                    text = code,
                    Modifier.padding(start = 8.dp, bottom = 2.dp, top = 10.dp)
                )
                Text(
                    text = name,
                    Modifier.padding(start = 8.dp, bottom = 2.dp),
                    fontWeight = FontWeight.Bold,
                )
                Text(text = date, Modifier.padding(start = 8.dp, bottom = 4.dp))

                Card(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(7.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(top = 10.dp, bottom = 5.dp)
                        .padding(horizontal = 4.dp)
                        .background(color),

                    ) {
                    Text(text = bal, Modifier.background(color), textAlign = TextAlign.Right)
                }
            }
        }
    }


}