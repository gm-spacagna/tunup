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

	protected final KMeansDatasetConfiguration executionConfig;

	protected final KMeansExecutor executor;

	protected final List<Integer> kVals;
	protected final List<Integer> distMeasIds;
	protected final List<Integer> iterationsVals;

	protected final KMeansParameterDimension<Integer> k;
	protected final KMeansParameterDimension<Integer> distMeasureId;
	protected final KMeansParameterDimension<Integer> iterations;
	
	protected final ClusterEvaluationWithNaturalFitness ce;

	protected final KMeansParametersSpace space;

	public KMeansExecutor getExecutor() {
		return executor;
	}

	AbstractKMeansTuning(KMeansDatasetConfiguration config, ClusterEvaluationWithNaturalFitness ce) {
		this.executionConfig = config;
		this.ce = ce;
	  executor =
		    KMeansExecutor.createInstance(config.getName(), config.getFilePath(), config.getSeparator(),
		        config.getClassIndex(), ce);
		kVals = new ArrayList<Integer>(config.getMaxK());
		for (int i = config.getMinK(); i < config.getMaxK(); i++) {
			kVals.add(i);
		}
		k = new KMeansParameterDimension<>("k", kVals);
		distMeasIds = new ArrayList<Integer>(config.getDistMeasures());
		for (int i = 0; i < config.getDistMeasures(); i++) {
			distMeasIds.add(i);
		}
		distMeasureId = new KMeansParameterDimension<>("DistanceMeasureId", distMeasIds);
		iterationsVals = new ArrayList<Integer>(config.getMaxIterations() - config.getMinIterations() + 1);
		for (int i = config.getMinIterations(); i <= config.getMaxIterations(); i++) {
			iterationsVals.add(i);
		}
		iterations = new KMeansParameterDimension<>("Iterations", iterationsVals);

		space = new KMeansParametersSpace(k, distMeasureId, iterations);
	}

	public abstract KMeansConfigResult getBestConfig();
}
