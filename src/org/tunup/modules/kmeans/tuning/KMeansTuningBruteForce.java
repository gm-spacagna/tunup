package org.tunup.modules.kmeans.tuning;

import java.io.IOException;

import org.tunup.modules.kmeans.configuration.KMeansConfigResult;
import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.dataset.KMeansDatasetConfiguration;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.evolution.KMeansEvaluator;

/**
 * KMeans Tuning using Brute force approach.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansTuningBruteForce extends AbstractKMeansTuning {

	private static final int N = 20;

	KMeansTuningBruteForce(KMeansDatasetConfiguration config, 
			ClusterEvaluationWithNaturalFitness ce) {
		super(config, ce);
	}

	@Override
	public KMeansConfigResult getBestConfig() {
		double best = Double.MAX_VALUE;
		KMeansConfiguration bestConfig = null;
		for (int k = executionConfig.getMinK(); k <= executionConfig.getMaxK(); k++) {
			for (int distMeasId = 0; distMeasId < executionConfig.getDistMeasures(); distMeasId++) {
				for (int iter = executionConfig.getMinIterations(); iter <= executionConfig
				    .getMaxIterations(); iter++) {
					KMeansEvaluator evaluator = new KMeansEvaluator(executor, N);
					Double fitnessVal = evaluator.getFitness(new KMeansConfiguration(k, distMeasId, iter),
					    null);

					System.out.println(k + "," + distMeasId + "," + iter + " = " + fitnessVal);
					if (fitnessVal < best) {
						best = fitnessVal;
						bestConfig = new KMeansConfiguration(k, distMeasId, iter);
					}
				}
			}
		}
		KMeansConfigResult bestConfigResult = executor.getConfigResult(bestConfig);
		System.out.println("Best is : " + executor.getConfigResult(bestConfig));
		System.out.println("Number of executions: " + executor.getCount());
		return bestConfigResult;
	}
}
