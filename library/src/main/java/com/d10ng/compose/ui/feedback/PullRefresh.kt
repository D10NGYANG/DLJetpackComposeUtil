package com.d10ng.compose.ui.feedback

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.Drag
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.feedback.PullRefreshDefaults.DragMultiplier
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.AlphaTween
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.ArcRadius
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.ArrowHeight
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.ArrowWidth
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.IndicatorSize
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.MaxAlpha
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.MaxProgressArc
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.MinAlpha
import com.d10ng.compose.ui.feedback.PullRefreshIndicatorDefaults.SpinnerShape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * A nested scroll modifier that provides scroll events to [state].
 * Taken from compose-material library and adapted for Material 3
 *
 * Note that this modifier must be added above a scrolling container, such as a lazy column, in
 * order to receive scroll events. For example:
 *
 * @param state The [PullRefreshState] associated with this pull-to-refresh component.
 * The state will be updated by this modifier.
 * @param enabled If not enabled, all scroll delta and fling velocity will be ignored.
 */
@ExperimentalMaterial3Api
fun Modifier.pullRefresh(
    state: PullRefreshState,
    enabled: Boolean = true,
) = inspectable(
    inspectorInfo = debugInspectorInfo {
        name = "pullRefresh"
        properties["state"] = state
        properties["enabled"] = enabled
    },
) {
    Modifier.pullRefresh(state::onPull, state::onRelease, enabled)
}

/**
 * A nested scroll modifier that provides [onPull] and [onRelease] callbacks to aid building custom
 * pull refresh components.
 *
 * Taken from compose-material library and adapted for Material 3
 *
 * Note that this modifier must be added above a scrolling container, such as a lazy column, in
 * order to receive scroll events. For example:
 *
 * @param onPull Callback for dispatching vertical scroll delta, takes float pullDelta as argument.
 * Positive delta (pulling down) is dispatched only if the child does not consume it (i.e. pulling
 * down despite being at the top of a scrollable component), whereas negative delta (swiping up) is
 * dispatched first (in case it is needed to push the indicator back up), and then the unconsumed
 * delta is passed on to the child. The callback returns how much delta was consumed.
 * @param onRelease Callback for when drag is released, takes float flingVelocity as argument.
 * The callback returns how much velocity was consumed - in most cases this should only consume
 * velocity if pull refresh has been dragged already and the velocity is positive (the fling is
 * downwards), as an upwards fling should typically still scroll a scrollable component beneath the
 * pullRefresh. This is invoked before any remaining velocity is passed to the child.
 * @param enabled If not enabled, all scroll delta and fling velocity will be ignored and neither
 * [onPull] nor [onRelease] will be invoked.
 */
@ExperimentalMaterial3Api
fun Modifier.pullRefresh(
    onPull: (pullDelta: Float) -> Float,
    onRelease: suspend (flingVelocity: Float) -> Float,
    enabled: Boolean = true,
) = inspectable(
    inspectorInfo = debugInspectorInfo {
        name = "pullRefresh"
        properties["onPull"] = onPull
        properties["onRelease"] = onRelease
        properties["enabled"] = enabled
    },
) {
    Modifier.nestedScroll(PullRefreshNestedScrollConnection(onPull, onRelease, enabled))
}

private class PullRefreshNestedScrollConnection(
    private val onPull: (pullDelta: Float) -> Float,
    private val onRelease: suspend (flingVelocity: Float) -> Float,
    private val enabled: Boolean,
) : NestedScrollConnection {

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource,
    ): Offset = when {
        !enabled -> Offset.Zero
        source == Drag && available.y < 0 -> Offset(0f, onPull(available.y)) // Swiping up
        else -> Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset = when {
        !enabled -> Offset.Zero
        source == Drag && available.y > 0 -> Offset(0f, onPull(available.y)) // Pulling down
        else -> Offset.Zero
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        return Velocity(0f, onRelease(available.y))
    }
}

/**
 * The default indicator for Compose pull-to-refresh, based on Android's SwipeRefreshLayout.
 * Taken from compose-material library and adapted for Material 3
 *
 *
 * @param refreshing A boolean representing whether a refresh is occurring.
 * @param state The [PullRefreshState] which controls where and how the indicator will be drawn.
 * @param modifier Modifiers for the indicator.
 * @param colors The colors of the indicator's container, arc and arrow.
 * @param shadowElevation The colors of the indicator's container, arc and arrow.
 * @param scale A boolean controlling whether the indicator's size scales with pull progress or not.
 */
@Composable
@ExperimentalMaterial3Api
fun PullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    colors: PullRefreshIndicatorColors = PullRefreshIndicatorDefaults.colors(),
    tonalElevation: Dp = PullRefreshIndicatorDefaults.Elevation,
    shadowElevation: Dp = 0.dp,
    scale: Boolean = false,
) {
    val showElevation by remember(refreshing, state) {
        derivedStateOf { refreshing || state.position > PullRefreshIndicatorDefaults.POSITION_THRESHOLD }
    }

    Surface(
        color = colors.containerColor().value,
        contentColor = colors.contentColor().value,
        tonalElevation = if (showElevation) {
            tonalElevation
        } else {
            0.dp
        },
        shadowElevation = if (showElevation) {
            shadowElevation
        } else {
            0.dp
        },
        shape = SpinnerShape,
        modifier = modifier
            .size(IndicatorSize)
            .pullRefreshIndicatorTransform(state, scale),
    ) {
        Crossfade(
            targetState = refreshing,
            animationSpec = tween(durationMillis = PullRefreshIndicatorDefaults.CrossfadeDurationMs),
            label = "PullRefreshIndicator",
        ) { refreshing ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                val spinnerSize =
                    (ArcRadius + PullRefreshIndicatorDefaults.StrokeWidth).times(
                        2,
                    )

                if (refreshing) {
                    CircularProgressIndicator(
                        color = colors.contentColor().value,
                        strokeWidth = PullRefreshIndicatorDefaults.StrokeWidth,
                        modifier = Modifier.size(spinnerSize),
                    )
                } else {
                    CircularArrowIndicator(
                        state,
                        colors.contentColor().value,
                        Modifier.size(spinnerSize),
                    )
                }
            }
        }
    }
}

/**
 * Modifier.size MUST be specified.
 */
@Composable
@ExperimentalMaterial3Api
private fun CircularArrowIndicator(
    state: PullRefreshState,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val path = remember { Path().apply { fillType = PathFillType.EvenOdd } }

    val targetAlpha by remember(state) {
        derivedStateOf {
            if (state.progress >= 1f) {
                MaxAlpha
            } else {
                MinAlpha
            }
        }
    }

    val alphaState = animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = AlphaTween,
        label = "alphaState",
    )

    // Empty semantics for tests
    Canvas(modifier.semantics {}) {
        val values = ArrowValues(state.progress)
        val alpha = alphaState.value

        rotate(degrees = values.rotation) {
            val arcRadius = ArcRadius.toPx() + PullRefreshIndicatorDefaults.StrokeWidth.toPx() / 2f
            val arcBounds = Rect(
                size.center.x - arcRadius,
                size.center.y - arcRadius,
                size.center.x + arcRadius,
                size.center.y + arcRadius,
            )
            drawArc(
                color = color,
                alpha = alpha,
                startAngle = values.startAngle,
                sweepAngle = values.endAngle - values.startAngle,
                useCenter = false,
                topLeft = arcBounds.topLeft,
                size = arcBounds.size,
                style = Stroke(
                    width = PullRefreshIndicatorDefaults.StrokeWidth.toPx(),
                    cap = StrokeCap.Square,
                ),
            )
            drawArrow(path, arcBounds, color, alpha, values)
        }
    }
}

@Immutable
private class ArrowValues(
    val rotation: Float,
    val startAngle: Float,
    val endAngle: Float,
    val scale: Float,
)

private const val DISCARD_PROGRESS_PERCENTAGE = 0.4f
private const val TENSION_PERCENT_REDUCER = 4
private const val ADJUSTED_PERCENT_MULTIPLIER = 5
private const val ADJUSTED_PERCENT_ROTATION_MULTIPLIER = 0.4f
private const val ROTATION_REDUCER = -0.25f
private const val ADJUSTED_PERCENT_REDUCER = 3
private const val FULL_ROTATION_ANGLE = 360
private const val ROTATION_MULTIPLIER = 0.5f

private fun ArrowValues(progress: Float): ArrowValues {
    // Discard first 40% of progress. Scale remaining progress to full range between 0 and 100%.
    val adjustedPercent = max(
        min(1f, progress) - DISCARD_PROGRESS_PERCENTAGE,
        0f,
    ) * ADJUSTED_PERCENT_MULTIPLIER / ADJUSTED_PERCENT_REDUCER
    // How far beyond the threshold pull has gone, as a percentage of the threshold.
    val overshootPercent = abs(progress) - 1.0f
    // Limit the overshoot to 200%. Linear between 0 and 200.
    val linearTension = overshootPercent.coerceIn(0f, 2f)
    // Non-linear tension. Increases with linearTension, but at a decreasing rate.
    val tensionPercent = linearTension - linearTension.pow(2) / TENSION_PERCENT_REDUCER

    // Calculations based on SwipeRefreshLayout specification.
    val endTrim = adjustedPercent * MaxProgressArc
    val rotation = (
            ROTATION_REDUCER +
                    ADJUSTED_PERCENT_ROTATION_MULTIPLIER * adjustedPercent +
                    tensionPercent
            ) * ROTATION_MULTIPLIER
    val startAngle = rotation * FULL_ROTATION_ANGLE
    val endAngle = (rotation + endTrim) * FULL_ROTATION_ANGLE
    val scale = min(1f, adjustedPercent)

    return ArrowValues(rotation, startAngle, endAngle, scale)
}

private fun DrawScope.drawArrow(
    arrow: Path,
    bounds: Rect,
    color: Color,
    alpha: Float,
    values: ArrowValues,
) {
    arrow.reset()
    arrow.moveTo(0f, 0f) // Move to left corner
    arrow.lineTo(x = ArrowWidth.toPx() * values.scale, y = 0f) // Line to right corner

    // Line to tip of arrow
    arrow.lineTo(
        x = ArrowWidth.toPx() * values.scale / 2,
        y = ArrowHeight.toPx() * values.scale,
    )

    val radius = min(bounds.width, bounds.height) / 2f
    val inset = ArrowWidth.toPx() * values.scale / 2f
    arrow.translate(
        Offset(
            x = radius + bounds.center.x - inset,
            y = bounds.center.y + PullRefreshIndicatorDefaults.StrokeWidth.toPx() / 2f,
        ),
    )
    arrow.close()
    rotate(degrees = values.endAngle) {
        drawPath(path = arrow, color = color, alpha = alpha)
    }
}

object PullRefreshIndicatorDefaults {
    const val CrossfadeDurationMs = 100
    const val MaxProgressArc = 0.8f
    private const val ALPHA_TWEEN_DURATION_MILLIS = 300
    const val POSITION_THRESHOLD = 0.5f

    val IndicatorSize = 40.dp
    val SpinnerShape = CircleShape
    val ArcRadius = 7.5.dp
    val StrokeWidth = 2.5.dp
    val ArrowWidth = 10.dp
    val ArrowHeight = 5.dp
    val Elevation = 6.dp

    // Values taken from SwipeRefreshLayout
    const val MinAlpha = 0.3f
    const val MaxAlpha = 1f
    val AlphaTween = tween<Float>(ALPHA_TWEEN_DURATION_MILLIS, easing = LinearEasing)

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
    ): PullRefreshIndicatorColors =
        PullRefreshIndicatorColors(
            containerColor = containerColor,
            contentColor = contentColor,
        )
}

@Immutable
class PullRefreshIndicatorColors internal constructor(
    private val containerColor: Color,
    private val contentColor: Color,
) {
    @Composable
    internal fun containerColor(): State<Color> {
        return rememberUpdatedState(containerColor)
    }

    @Composable
    internal fun contentColor(): State<Color> {
        return rememberUpdatedState(contentColor)
    }
}

/**
 * A modifier for translating the position and scaling the size of a pull-to-refresh indicator
 * based on the given [PullRefreshState].
 * Taken from compose-material library and adapted for Material 3
 *
 * @param state The [PullRefreshState] which determines the position of the indicator.
 * @param scale A boolean controlling whether the indicator's size scales with pull progress or not.
 */
@ExperimentalMaterial3Api
fun Modifier.pullRefreshIndicatorTransform(
    state: PullRefreshState,
    scale: Boolean = false,
) = inspectable(
    inspectorInfo = debugInspectorInfo {
        name = "pullRefreshIndicatorTransform"
        properties["state"] = state
        properties["scale"] = scale
    },
) {
    Modifier
        // Essentially we only want to clip the at the top, so the indicator will not appear when
        // the position is 0. It is preferable to clip the indicator as opposed to the layout that
        // contains the indicator, as this would also end up clipping shadows drawn by items in a
        // list for example - so we leave the clipping to the scrolling container. We use MAX_VALUE
        // for the other dimensions to allow for more room for elevation / arbitrary indicators - we
        // only ever really want to clip at the top edge.
        .drawWithContent {
            clipRect(
                top = 0f,
                left = -Float.MAX_VALUE,
                right = Float.MAX_VALUE,
                bottom = Float.MAX_VALUE,
            ) {
                this@drawWithContent.drawContent()
            }
        }
        .graphicsLayer {
            translationY = state.position - size.height

            if (scale && !state.refreshing) {
                val scaleFraction = LinearOutSlowInEasing
                    .transform(state.position / state.threshold)
                    .coerceIn(0f, 1f)
                scaleX = scaleFraction
                scaleY = scaleFraction
            }
        }
}

/**
 * Creates a [PullRefreshState] that is remembered across compositions.
 *
 * Taken from compose-material library and adapted for Material 3
 *
 * Changes to [refreshing] will result in [PullRefreshState] being updated.
 *
 * @param refreshing A boolean representing whether a refresh is currently occurring.
 * @param onRefresh The function to be called to trigger a refresh.
 * @param refreshThreshold The threshold below which, if a release
 * occurs, [onRefresh] will be called.
 * @param refreshingOffset The offset at which the indicator will be drawn while refreshing. This
 * offset corresponds to the position of the bottom of the indicator.
 */
@Composable
@ExperimentalMaterial3Api
fun rememberPullRefreshState(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    refreshThreshold: Dp = PullRefreshDefaults.RefreshThreshold,
    refreshingOffset: Dp = PullRefreshDefaults.RefreshingOffset,
): PullRefreshState {
    require(refreshThreshold > 0.dp) { "The refresh trigger must be greater than zero!" }

    val scope = rememberCoroutineScope()
    val onRefreshState = rememberUpdatedState(onRefresh)
    val thresholdPx: Float
    val refreshingOffsetPx: Float

    with(LocalDensity.current) {
        thresholdPx = refreshThreshold.toPx()
        refreshingOffsetPx = refreshingOffset.toPx()
    }

    val state = remember(scope) {
        PullRefreshState(scope, onRefreshState, refreshingOffsetPx, thresholdPx)
    }

    SideEffect {
        state.setRefreshing(refreshing)
        state.setThreshold(thresholdPx)
        state.setRefreshingOffset(refreshingOffsetPx)
    }

    return state
}

private const val LINEAR_TENSION_DIVISION_RATE = 4

/**
 * A state object that can be used in conjunction with [pullRefresh] to add pull-to-refresh
 * behaviour to a scroll component. Based on Android's SwipeRefreshLayout.
 * Taken from compose-material library and adapted for Material 3
 *
 * Provides [progress], a float representing how far the user has pulled as a percentage of the
 * refreshThreshold. Values of one or less indicate that the user has not yet pulled past the
 * threshold. Values greater than one indicate how far past the threshold the user has pulled.
 *
 * Can be used in conjunction with [pullRefreshIndicatorTransform] to implement Android-like
 * pull-to-refresh behaviour with a custom indicator.
 *
 * Should be created using [rememberPullRefreshState].
 */
@ExperimentalMaterial3Api
class PullRefreshState internal constructor(
    private val animationScope: CoroutineScope,
    private val onRefreshState: State<() -> Unit>,
    refreshingOffset: Float,
    threshold: Float,
) {
    /**
     * A float representing how far the user has pulled as a percentage of the refreshThreshold.
     *
     * If the component has not been pulled at all, progress is zero. If the pull has reached
     * halfway to the threshold, progress is 0.5f. A value greater than 1 indicates that pull has
     * gone beyond the refreshThreshold - e.g. a value of 2f indicates that the user has pulled to
     * two times the refreshThreshold.
     */
    val progress get() = adjustedDistancePulled / threshold

    internal val refreshing get() = _refreshing
    internal val position get() = _position
    internal val threshold get() = _threshold

    private val adjustedDistancePulled by derivedStateOf { distancePulled * DragMultiplier }

    private var _refreshing by mutableStateOf(false)
    private var _position by mutableFloatStateOf(0f)
    private var distancePulled by mutableFloatStateOf(0f)
    private var _threshold by mutableFloatStateOf(threshold)
    private var _refreshingOffset by mutableFloatStateOf(refreshingOffset)

    internal fun onPull(pullDelta: Float): Float {
        if (_refreshing) {
            return 0f // Already refreshing, do nothing.
        }

        val newOffset = (distancePulled + pullDelta).coerceAtLeast(0f)
        val dragConsumed = newOffset - distancePulled
        distancePulled = newOffset
        _position = calculateIndicatorPosition()
        return dragConsumed
    }

    internal fun onRelease(velocity: Float): Float {
        if (refreshing) {
            return 0f // Already refreshing, do nothing
        }

        if (adjustedDistancePulled > threshold) {
            onRefreshState.value()
        }
        animateIndicatorTo(0f)
        val consumed = when {
            // We are flinging without having dragged the pull refresh (for example a fling inside
            // a list) - don't consume
            distancePulled == 0f -> 0f
            // If the velocity is negative, the fling is upwards, and we don't want to prevent the
            // the list from scrolling
            velocity < 0f -> 0f
            // We are showing the indicator, and the fling is downwards - consume everything
            else -> velocity
        }
        distancePulled = 0f
        return consumed
    }

    internal fun setRefreshing(refreshing: Boolean) {
        if (_refreshing != refreshing) {
            _refreshing = refreshing
            distancePulled = 0f
            animateIndicatorTo(
                if (refreshing) {
                    _refreshingOffset
                } else {
                    0f
                }
            )
        }
    }

    internal fun setThreshold(threshold: Float) {
        _threshold = threshold
    }

    internal fun setRefreshingOffset(refreshingOffset: Float) {
        if (_refreshingOffset != refreshingOffset) {
            _refreshingOffset = refreshingOffset
            if (refreshing) {
                animateIndicatorTo(refreshingOffset)
            }
        }
    }

    // Make sure to cancel any existing animations when we launch a new one. We use this instead of
    // Animatable as calling snapTo() on every drag delta has a one frame delay, and some extra
    // overhead of running through the animation pipeline instead of directly mutating the state.
    private val mutatorMutex = MutatorMutex()

    private fun animateIndicatorTo(offset: Float) = animationScope.launch {
        mutatorMutex.mutate {
            animate(initialValue = _position, targetValue = offset) { value, _ ->
                _position = value
            }
        }
    }

    private fun calculateIndicatorPosition(): Float = when {
        // If drag hasn't gone past the threshold, the position is the adjustedDistancePulled.
        adjustedDistancePulled <= threshold -> adjustedDistancePulled
        else -> {
            // How far beyond the threshold pull has gone, as a percentage of the threshold.
            val overshootPercent = abs(progress) - 1.0f
            // Limit the overshoot to 200%. Linear between 0 and 200.
            val linearTension = overshootPercent.coerceIn(0f, 2f)
            // Non-linear tension. Increases with linearTension, but at a decreasing rate.
            val tensionPercent = linearTension - linearTension.pow(2) / LINEAR_TENSION_DIVISION_RATE
            // The additional offset beyond the threshold.
            val extraOffset = threshold * tensionPercent
            threshold + extraOffset
        }
    }
}

/**
 * Default parameter values for [rememberPullRefreshState].
 */
@ExperimentalMaterial3Api
object PullRefreshDefaults {
    /**
     * If the indicator is below this threshold offset when it is released, a refresh
     * will be triggered.
     */
    val RefreshThreshold = 80.dp

    /**
     * The offset at which the indicator should be rendered whilst a refresh is occurring.
     */
    val RefreshingOffset = 56.dp

    /**
     * The distance pulled is multiplied by this value to give us the adjusted distance pulled, which
     * is used in calculating the indicator position (when the adjusted distance pulled is less than
     * the refresh threshold, it is the indicator position, otherwise the indicator position is
     * derived from the progress).
     */
    const val DragMultiplier = 0.5f
}
