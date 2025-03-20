package com.d10ng.compose.android.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.android.demo.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("色彩系统", style = MaterialTheme.typography.displayMedium)
        MaterialTheme.colorScheme.apply {
            ColorRow(primary, "primary")
            ColorRow(onPrimary, "onPrimary")
            ColorRow(primaryContainer, "primaryContainer")
            ColorRow(onPrimaryContainer, "onPrimaryContainer")
            ColorRow(inversePrimary, "inversePrimary")
            ColorRow(secondary, "secondary")
            ColorRow(onSecondary, "onSecondary")
            ColorRow(secondaryContainer, "secondaryContainer")
            ColorRow(onSecondaryContainer, "onSecondaryContainer")
            ColorRow(tertiary, "tertiary")
            ColorRow(onTertiary, "onTertiary")
            ColorRow(tertiaryContainer, "tertiaryContainer")
            ColorRow(onTertiaryContainer, "onTertiaryContainer")
            ColorRow(background, "background")
            ColorRow(onBackground, "onBackground")
            ColorRow(surface, "surface")
            ColorRow(onSurface, "onSurface")
            ColorRow(surfaceVariant, "surfaceVariant")
            ColorRow(onSurfaceVariant, "onSurfaceVariant")
            ColorRow(surfaceTint, "surfaceTint")
            ColorRow(inverseSurface, "inverseSurface")
            ColorRow(inverseOnSurface, "inverseOnSurface")
            ColorRow(error, "error")
            ColorRow(onError, "onError")
            ColorRow(errorContainer, "errorContainer")
            ColorRow(onErrorContainer, "onErrorContainer")
            ColorRow(outline, "outline")
            ColorRow(outlineVariant, "outlineVariant")
            ColorRow(scrim, "scrim")
            ColorRow(surfaceBright, "surfaceBright")
            ColorRow(surfaceDim, "surfaceDim")
            ColorRow(surfaceContainer, "surfaceContainer")
            ColorRow(surfaceContainerHigh, "surfaceContainerHigh")
            ColorRow(surfaceContainerHighest, "surfaceContainerHighest")
            ColorRow(surfaceContainerLow, "surfaceContainerLow")
            ColorRow(surfaceContainerLowest, "surfaceContainerLowest")
        }
        Text("字体排版", style = MaterialTheme.typography.displayMedium)
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            MaterialTheme.typography.apply {
                Text("displayLarge", style = displayLarge)
                Text("displayMedium", style = displayMedium)
                Text("displaySmall", style = displaySmall)
                Text("headlineLarge", style = headlineLarge)
                Text("headlineMedium", style = headlineMedium)
                Text("headlineSmall", style = headlineSmall)
                Text("titleLarge", style = titleLarge)
                Text("titleMedium", style = titleMedium)
                Text("titleSmall", style = titleSmall)
                Text("bodyLarge", style = bodyLarge)
                Text("bodyMedium", style = bodyMedium)
                Text("bodySmall", style = bodySmall)
                Text("labelLarge", style = labelLarge)
                Text("labelMedium", style = labelMedium)
                Text("labelSmall", style = labelSmall)
            }
        }
        Text("容器形状", style = MaterialTheme.typography.displayMedium)
        MaterialTheme.shapes.apply {
            ShapeBox(extraSmall, "extraSmall")
            ShapeBox(small, "small")
            ShapeBox(medium, "medium")
            ShapeBox(large, "large")
            ShapeBox(extraLarge, "extraLarge")
        }
    }
}

@Composable
private fun ColorRow(
    color: Color,
    name: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(30.dp)
                .background(color)
                .border(1.dp, Color.Gray)
        )
        Text(
            text = name,
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp)
        )
    }
}

@Composable
private fun ShapeBox(
    shape: Shape,
    name: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .background(Color.Blue, shape)
                .border(1.dp, Color.Gray)
        )
        Text(
            text = name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = false,
    device = "spec:width=1080px,height=7000px,dpi=440"
)
@Composable
fun GreetingPreview() {
    Greeting()
}