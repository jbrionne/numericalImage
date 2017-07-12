package fr.next.numericalimage.service.border;

/**
 * Canny Edge Detector Param. See {@link CannyEdgeDetector}
 */
public class CannyEdgeDetectorParam {

	/*
	 * the low threshold for hysteresis. Suitable values for this parameter must
	 * be determined experimentally for each application. It is nonsensical
	 * (though not prohibited) for this value to exceed the high threshold
	 * value.
	 */
	private float lowThreshold = 3f;

	/*
	 * the high threshold for hysteresis. Suitable values for this parameter
	 * must be determined experimentally for each application. It is nonsensical
	 * (though not prohibited) for this value to be less than the low threshold
	 * value.
	 */
	private float highThreshold = 3.5f;

	/*
	 * the radius of the Gaussian convolution kernel used to smooth the source
	 * image prior to gradient calculation
	 */
	private float gaussianKernelRadius = 3f;

	/* The number of pixels across which the Gaussian kernel is applied. */
	private int gaussianKernelWidth = 16;

	/*
	 * The number of pixels across which the Gaussian kernel is applied. This
	 * implementation will reduce the radius if the contribution of pixel values
	 * is deemed negligable, so this is actually a maximum radius.
	 */
	private boolean contrastNormalized = false;

	public CannyEdgeDetectorParam(float lowThreshold, float highThreshold, float gaussianKernelRadius,
			int gaussianKernelWidth, boolean contrastNormalized) {
		super();
		this.lowThreshold = lowThreshold;
		this.highThreshold = highThreshold;
		this.gaussianKernelRadius = gaussianKernelRadius;
		this.gaussianKernelWidth = gaussianKernelWidth;
		this.contrastNormalized = contrastNormalized;
	}

	public float getLowThreshold() {
		return lowThreshold;
	}

	public float getHighThreshold() {
		return highThreshold;
	}

	public float getGaussianKernelRadius() {
		return gaussianKernelRadius;
	}

	public int getGaussianKernelWidth() {
		return gaussianKernelWidth;
	}

	public boolean isContrastNormalized() {
		return contrastNormalized;
	}

}
