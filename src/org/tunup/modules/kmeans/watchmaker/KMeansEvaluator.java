package org.tunup.modules.kmeans.watchmaker;

import java.io.IOException;
import java.util.List;

import org.tunup.modules.kmeans.javaml.KMeansConfiguration;
import org.tunup.modules.kmeans.javaml.KMeansExecutor;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

/**
 * KMeans Fitness evaluator using JavaML KMeans executor.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansEvaluator implements FitnessEvaluator<KMeansConfiguration> {

	KMeansExecutor executor;
	private final int n;

	/**
	 * Creates an instance of the k-means evaluator with the specified executor and n = 10;
	 * @param executor k-means executor.
	 */
	public KMeansEvaluator(KMeansExecutor executor) {
		this(executor, 10);
	}

	/**
	 * Creates an instance of the k-means evaluator.
	 * @param executor k-means executor.
	 * @param n Number of executions to average the fitness value evaluated.
	 */
	public KMeansEvaluator(KMeansExecutor executor, int n) {
		super();
		this.executor = executor;
		this.n = n;
	}

	@Override
	public double getFitness(KMeansConfiguration candidate,
	    List<? extends KMeansConfiguration> population) {
		try {
			double fitnessVal = 0;
			int i = 0;
			while (i++ < n) {
				fitnessVal += executor.executeAndEvaluate(candidate);
			}
			fitnessVal /= n;
			return fitnessVal;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public boolean isNatural() {
		return executor.getNaturalFitness();
	}
}
