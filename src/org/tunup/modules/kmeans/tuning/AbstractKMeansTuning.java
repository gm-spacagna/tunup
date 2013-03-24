package org.tunup.modules.kmeans.tuning;

import java.util.ArrayList;
import java.util.List;

import org.tunup.modules.kmeans.configuration.KMeansConfigResult;
import org.tunup.modules.kmeans.dataset.KMeansDatasetConfiguration;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.evaluation.DunnScore;
import org.tunup.modules.kmeans.execution.KMeansExecutor;
import org.tunup.modules.kmeans.space.KMeansParameterDimension;
import org.tunup.modules.kmeans.space.KMeansParametersSpace;

/**
 * Abstract class for KMeansTuning.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public abstract class AbstractKMeansTuning {

	protected KMeansDatasetConfiguration dataset;

	protected KMeansExecutor executor;

	protected ClusterEvaluationWithNaturalFitness ce;

	protected KMeansParametersSpace space;

	public KMeansExecutor getExecutor() {
		return executor;
	}
	
	protected long start = 0, end = 0;
	
	public Long getTime() {
		return end - start;
	}

	AbstractKMeansTuning(KMeansDatasetConfiguration dataset, ClusterEvaluationWithNaturalFitness ce) {
		this.ce = ce;
		if (dataset != null) {
			this.dataset = dataset;
			executor = KMeansExecutor.createInstance(dataset, ce);
			this.space = dataset.createSpace();
		}
	}

	
/**
 * This method should be overridden if the tuning implementing class provides a best configuration.
 * @return The best configuration.
 */
	public KMeansConfigResult getBestConfig() {
		throw new RuntimeException("Method not implemented");
	}
}
