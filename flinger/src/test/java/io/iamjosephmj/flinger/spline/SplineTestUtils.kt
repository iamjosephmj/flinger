package io.iamjosephmj.flinger.spline

object SplineTestUtils {
    fun getSplinePositionsArray(): FloatArray {
        return floatArrayOf(1.0f, 3.2f, 5.6f, 8.1f, 10f)
    }

    fun getSplineTimeArray(): FloatArray {
        return floatArrayOf(5f, 7f, 10f, 15f, 20f)
    }

    fun getNumberOfSamples(): Int = 4

    fun getExpectedPositionArray(): FloatArray =
        floatArrayOf(7.3385214E-5f, 0.6618202f, 0.8786185f, 0.97391224f, 1.0f)

    fun getExpectedTimeArray(): FloatArray =
        floatArrayOf(9.157509E-7f, 0.047422685f, 0.14568907f, 0.3293493f, 1.0f)
}