package org.tunup.modules.kmeans.dataset;

import org.tunup.modules.kmeans.execution.KMeansExecutor;

public class WhiteWineConfiguration extends KMeansDatasetConfiguration {

	protected static final int MIN_K = 2;
	protected static final int MAX_K = 10;
	protected static final int MAX_ITERATIONS = 50;
	protected static final int MIN_ITERATIONS = 50;

	protected static final String NAME = "whitewine";
	protected static final String FILE_PATH = "data/winequality/winequality-white.csv";
	protected static final String SEPARATOR = ";";
	protected static final int CLASS_INDEX = 11 ;

	public WhiteWineConfiguration() {
		super(MIN_K, MAX_K, MAX_ITERATIONS, MIN_ITERATIONS, NAME, FILE_PATH, SEPARATOR,
		    CLASS_INDEX);
	}
}
