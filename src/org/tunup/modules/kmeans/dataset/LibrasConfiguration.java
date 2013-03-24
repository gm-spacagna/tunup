package org.tunup.modules.kmeans.dataset;

import org.tunup.modules.kmeans.execution.KMeansExecutor;

public class LibrasConfiguration extends KMeansDatasetConfiguration {

	protected static final int MIN_K = 2;
	protected static final int MAX_K = 20;
	protected static final int MAX_ITERATIONS = 20;
	protected static final int MIN_ITERATIONS = 20;

	protected static final String NAME = "libras";
	protected static final String FILE_PATH = "data/libras/movement_libras.data";
	protected static final String SEPARATOR = ",";
	protected static final int CLASS_INDEX = 90;
	protected static final int[] DISTANCE_MEASURE_IDS = new int[] {
	    4
	};

	public LibrasConfiguration() {
		super(MIN_K, MAX_K, DISTANCE_MEASURE_IDS, MAX_ITERATIONS, MIN_ITERATIONS, NAME, FILE_PATH,
		    SEPARATOR, CLASS_INDEX);
	}
}
