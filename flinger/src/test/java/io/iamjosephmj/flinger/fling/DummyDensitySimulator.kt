package io.iamjosephmj.flinger.fling

import androidx.compose.ui.unit.Density

class DummyDensitySimulator : Density {
    override val density: Float = 10f
    override val fontScale: Float = 10f
}