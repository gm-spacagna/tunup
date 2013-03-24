package org.tunup.modules.kmeans.dataset;

import org.tunup.modules.kmeans.execution.KMeansExecutor;

public class IrisConfiguration extends KMeansDatasetConfiguration {

	protected static final int MIN_K = 2;
	protected static final int MAX_K = 10;
	protected static final int MAX_ITERATIONS = 20;
	protected static final int MIN_ITERATIONS = 20;

	protected static final String NAME = "iris";
	protected static final String FILE_PATH = "data/iris/iris.data";
	protected static final String SEPARATOR = ",";
	protected static final int CLASS_INDEX = 4;
	
	protected static final int[] DISTANCE_MEASURE_IDS = new int[] {
	    0, 1, 2, 3, 4, 7, 8, 10, 12
	};

	public IrisConfiguration() {
		super(MIN_K, MAX_K, DISTANCE_MEASURE_IDS, MAX_ITERATIONS, MIN_ITERATIONS, NAME, FILE_PATH, SEPARATOR,
		    CLASS_INDEX);
	}
}
