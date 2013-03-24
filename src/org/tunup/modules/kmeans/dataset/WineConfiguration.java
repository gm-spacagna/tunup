package org.tunup.modules.kmeans.dataset;

import org.tunup.modules.kmeans.execution.KMeansExecutor;

public class WineConfiguration extends KMeansDatasetConfiguration {

	protected static final int MIN_K = 2;
	protected static final int MAX_K = 10;
	protected static final int MAX_ITERATIONS = 50;
	protected static final int MIN_ITERATIONS = 50;

	protected static final String NAME = "wine";
	protected static final String FILE_PATH = "data/joensuu.fi/wine_nospaces.txt";
	protected static final String SEPARATOR = ",";
	protected static final int CLASS_INDEX = -1;
	protected static final int[] DISTANCE_MEASURE_IDS = new int[] {
	    0, 1, 2, 3, 4, 7, 8, 9, 10, 12
	};

	public WineConfiguration() {
		super(MIN_K, MAX_K, DISTANCE_MEASURE_IDS, MAX_ITERATIONS, MIN_ITERATIONS, NAME, FILE_PATH,
		    SEPARATOR, CLASS_INDEX);
	}
}
