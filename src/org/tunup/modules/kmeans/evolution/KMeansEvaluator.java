package org.tunup.modules.kmeans.evolution;

import java.io.IOException;
import java.util.List;

import org.tunup.modules.kmeans.configuration.KMeansConfigResult;
import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.execution.KMeansExecutor;

/**
 * KMeans Fitness evaluator using JavaML KMeans executor.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansEvaluator implements FitnessEvaluatorWithCounter<KMeansConfiguration> {

	KMeansExecutor executor;
	protected final int n;
	protected final ClusterEvaluationWithNaturalFitness ce;

	protected int count = 0;

	/**
	 * Creates an instance of the k-means evaluator.
	 * 
	 * @param executor
	 *          k-means executor.
	 * @param n
	 *          Number of executions to average the fitness value evaluated.
	 * @param caching
	 *          True if caching is active, false otherwise.
	 */
	public KMeansEvaluator(KMeansExecutor executor, int n,
	    ClusterEvaluationWithNaturalFitness ce) {
		super();
		this.executor = executor;
		this.n = n;
		this.ce = ce;
	}

	@Override
	public double getFitness(KMeansConfiguration candidate,
	    List<? extends KMeansConfiguration> population) {
		KMeansConfigResult result = executor.executeAndEvaluate(
		    candidate, n,
		    new ClusterEvaluationWithNaturalFitness[] { ce });
		candidate.setResult(result);
		count++;
		return result.getMedian()[0];
	}

	@Override
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public boolean isNatural() {
		return ce.isNatural();
	}
}
