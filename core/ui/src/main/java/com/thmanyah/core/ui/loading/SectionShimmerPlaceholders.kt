package com.thmanyah.core.ui.loading


import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.components.ShimmerBox
import com.thmanyah.domain.models.SectionLayout

private val shimmerTitleCorner = 6.dp
private val squareImageDp = 120.dp
private val bigSquareImageDp = 160.dp
private val twoLinesCellWidth = 300.dp
private val twoLinesRowHeight = 100.dp
private val queueHeroHeight = 240.dp
private val unknownThumbDp = 56.dp
private val loungeCardHeight = 192.dp

@Composable
fun ShimmerSectionTitleRow(
    showTrailing: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ThmanyahDimens.spaceMd, vertical = ThmanyahDimens.spaceSm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ShimmerBox(
            modifier = Modifier
                .height(22.dp)
                .fillMaxWidth(if (showTrailing) 0.52f else 0.62f),
            cornerDp = shimmerTitleCorner,
        )
        if (showTrailing) {
            ShimmerBox(
                modifier = Modifier
                    .height(14.dp)
                    .width(88.dp),
                cornerDp = 4.dp,
            )
        }
    }
}

@Composable
private fun SquareSectionShimmer() {
    Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
        ShimmerSectionTitleRow(showTrailing = false)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = ThmanyahDimens.spaceMd),
            horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
        ) {
            repeat(4) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(squareImageDp)
                            .height(squareImageDp),
                        cornerDp = ThmanyahDimens.cardCorner,
                    )
                    Spacer(Modifier.height(ThmanyahDimens.spaceSm))
                    ShimmerBox(
                        modifier = Modifier
                            .width(squareImageDp)
                            .height(12.dp),
                        cornerDp = 4.dp,
                    )
                    Spacer(Modifier.height(4.dp))
                    ShimmerBox(
                        modifier = Modifier
                            .width(96.dp)
                            .height(12.dp),
                        cornerDp = 4.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun TwoLinesGridSectionShimmer() {
    Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
        ShimmerSectionTitleRow(showTrailing = true)
        val gridHeight = twoLinesRowHeight * 2 + ThmanyahDimens.spaceSm
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight)
                .padding(horizontal = ThmanyahDimens.spaceMd),
            verticalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
            ) {
                repeat(2) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(twoLinesCellWidth)
                            .height(twoLinesRowHeight),
                        cornerDp = ThmanyahDimens.cardCorner,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
            ) {
                repeat(2) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(twoLinesCellWidth)
                            .height(twoLinesRowHeight),
                        cornerDp = ThmanyahDimens.cardCorner,
                    )
                }
            }
        }
    }
}

@Composable
private fun BigSquareSectionShimmer() {
    Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
        ShimmerSectionTitleRow(showTrailing = false)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = ThmanyahDimens.spaceMd),
            horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
        ) {
            repeat(3) {
                Column {
                    ShimmerBox(
                        modifier = Modifier
                            .width(bigSquareImageDp)
                            .height(bigSquareImageDp),
                        cornerDp = ThmanyahDimens.cardCorner,
                    )
                    Spacer(Modifier.height(ThmanyahDimens.spaceSm))
                    ShimmerBox(
                        modifier = Modifier
                            .width(bigSquareImageDp)
                            .height(14.dp),
                        cornerDp = 4.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun QueueSectionShimmer() {
    Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
        ShimmerSectionTitleRow(showTrailing = true)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ThmanyahDimens.spaceMd),
        ) {
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(queueHeroHeight),
                cornerDp = 18.dp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ThmanyahDimens.spaceSm),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(5) { i ->
                    ShimmerBox(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(6.dp)
                            .width(if (i == 2) 18.dp else 6.dp),
                        cornerDp = 3.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoungeSectionShimmer() {
    Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
        ShimmerSectionTitleRow(showTrailing = false)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ThmanyahDimens.spaceMd),
        ) {
            val cardWidth = maxWidth * 0.88f
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
            ) {
                repeat(2) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(cardWidth)
                            .height(loungeCardHeight),
                        cornerDp = ThmanyahDimens.cardCorner,
                    )
                }
            }
        }
    }
}

@Composable
private fun UnknownSectionShimmer() {
    Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
        ShimmerSectionTitleRow(showTrailing = false)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ThmanyahDimens.spaceMd),
        ) {
            val itemWidth = maxWidth * 0.88f
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
            ) {
                repeat(3) {
                    Row(
                        modifier = Modifier
                            .width(itemWidth)
                            .padding(vertical = ThmanyahDimens.spaceSm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(unknownThumbDp)
                                .height(unknownThumbDp),
                            cornerDp = ThmanyahDimens.cardCorner,
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = ThmanyahDimens.spaceSm),
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp),
                                cornerDp = 4.dp,
                            )
                            Spacer(Modifier.height(6.dp))
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth(0.55f)
                                    .height(12.dp),
                                cornerDp = 4.dp,
                            )
                        }
                    }
                }
            }
        }
    }
}

fun interface SectionShimmerRenderer {
    @Composable
    operator fun invoke()
}

object SectionShimmerRegistry {
    private val map = mapOf(
        SectionLayout.Square to SectionShimmerRenderer { SquareSectionShimmer() },
        SectionLayout.TwoLinesGrid to SectionShimmerRenderer { TwoLinesGridSectionShimmer() },
        SectionLayout.BigSquare to SectionShimmerRenderer { BigSquareSectionShimmer() },
        SectionLayout.Queue to SectionShimmerRenderer { QueueSectionShimmer() },
        SectionLayout.Lounge to SectionShimmerRenderer { LoungeSectionShimmer() },
        SectionLayout.Unknown to SectionShimmerRenderer { UnknownSectionShimmer() },
    )

    operator fun get(layout: SectionLayout): SectionShimmerRenderer =
        map[layout] ?: map.getValue(SectionLayout.Unknown)
}

@Composable
fun AppendLoadingSectionShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(ThmanyahDimens.spaceMd),
    ) {
        ShimmerSectionTitleRow(showTrailing = false)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
        ) {
            repeat(3) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(squareImageDp)
                            .height(squareImageDp),
                        cornerDp = ThmanyahDimens.cardCorner,
                    )
                    Spacer(Modifier.height(ThmanyahDimens.spaceSm))
                    ShimmerBox(
                        modifier = Modifier
                            .width(squareImageDp)
                            .height(12.dp),
                        cornerDp = 4.dp,
                    )
                }
            }
        }
    }
}

internal val LoadingShimmerLayoutSequence: List<SectionLayout> = listOf(
    SectionLayout.Square,
    SectionLayout.TwoLinesGrid,
    SectionLayout.BigSquare,
    SectionLayout.Queue,
    SectionLayout.Lounge,
    SectionLayout.Unknown,
    SectionLayout.Square,
)
