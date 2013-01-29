package org.tunup.modules.kmeans.configuration;


/**
 * k-means configuration specifying the initial centroids coordinates.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansConfigurationWithCentroids extends KMeansConfiguration {

	/* Dimensionality of dataset */
	protected final int m;

	protected final double[][] initCentroidsAttributes;

	public KMeansConfigurationWithCentroids(double[][] attributes, int distanceMeasureId,
	    int iterations) {
		super(attributes.length, distanceMeasureId, iterations);
		this.m = attributes[0].length;
		this.initCentroidsAttributes = attributes;
		assert(k == initCentroidsAttributes.length);
	}

	public double[][] getInitCentroidsAttributes() {
		return initCentroidsAttributes;
	}
}
