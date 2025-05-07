package fr.delcey.circlecarousel

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.asin
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

const val CIRCLE_Y_CENTER_OFFSET_DP = 900
const val CARD_SIZE_DP = 300

@Composable
fun HorizontalPagerContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val pagerInteractionSource = remember { MutableInteractionSource() }
        val pagerIsPressed by pagerInteractionSource.collectIsPressedAsState()

        val pagerState = rememberPagerState(
            initialPage = Int.MAX_VALUE / 2,
            pageCount = { Int.MAX_VALUE },
        )
        val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

        // Stop auto-advancing when pager is dragged or one of the pages is pressed
        val autoAdvance = !pagerIsDragged && !pagerIsPressed
        if (autoAdvance) {
            LaunchedEffect(pagerState, pagerInteractionSource) {
                while (true) {
                    delay(2.seconds)
                    val nextPage = pagerState.currentPage + 1
                    pagerState.animateScrollToPage(page = nextPage, animationSpec = tween(500))
                }
            }
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2F)
                .clickable(
                    interactionSource = pagerInteractionSource,
                    indication = null
                ) {},
            state = pagerState,
            pageSize = object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int = CARD_SIZE_DP.dp.roundToPx()
            },
            beyondViewportPageCount = 2,
            snapPosition = SnapPosition.Center,
        ) { page ->
            Card(
                Modifier
                    .fillMaxHeight(0.8F)
                    .graphicsLayer {
                        val pageOffset = page - pagerState.currentPage

                        // [1, 0, -1]
                        val pageOffsetFraction = pageOffset - pagerState.currentPageOffsetFraction

                        val circleRadiusPx = CIRCLE_Y_CENTER_OFFSET_DP.dp.roundToPx()
                        val pageOffsetFractionPx = pageOffsetFraction * CARD_SIZE_DP.dp.roundToPx()

                        val deltaY = circleRadiusPx.minus(
                            sqrt((pageOffsetFractionPx * pageOffsetFractionPx - circleRadiusPx * circleRadiusPx).absoluteValue)
                        )

                        // 1 rad ≈ 57.3°
                        rotationZ = asin(pageOffsetFractionPx / circleRadiusPx) * 57.3F
                        translationY = deltaY
                    }
                    .zIndex(
                        if (page == pagerState.currentPage) {
                            1F
                        }  else {
                            0F
                        }
                    ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(
                            when (page % 4) {
                                0 -> R.drawable.hedgehog_1
                                1 -> R.drawable.hedgehog_2
                                2 -> R.drawable.hedgehog_3
                                3 -> R.drawable.hedgehog_4
                                else -> throw IllegalStateException("Unknown page = $page")
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.Black)
                            .align(
                                if (page % 2 == 0) {
                                    Alignment.BottomCenter
                                } else {
                                    Alignment.TopCenter
                                }
                            ),
                        text = when (page % 4) {
                            0 -> "Let's take a bath!"
                            1 -> "Doin' some yoga stuff. Or Pilates, can't tell."
                            2 -> "I'm on the top of the world!"
                            3 -> "Should I eat this grass?"
                            else -> throw IllegalStateException("Unknown page = $page")
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Text(
            modifier = Modifier
                .weight(1F)
                .fillMaxSize()
                .padding(16.dp)
                .wrapContentHeight(),
            text = "Hedgehogs are the cutest animals",
            textAlign = TextAlign.Center,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

