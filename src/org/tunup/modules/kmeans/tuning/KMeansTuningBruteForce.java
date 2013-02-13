package org.tunup.modules.kmeans.tuning;

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
		KMeansEvaluator evaluator = new KMeansEvaluator(executor, N, ce);
		for (int k = dataset.getMinK(); k <= dataset.getMaxK(); k++) {
			for (int distMeasId = 0; distMeasId < dataset.getDistMeasures(); distMeasId++) {
				for (int iter = dataset.getMinIterations(); iter <= dataset
				    .getMaxIterations(); iter++) {
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
		KMeansConfigResult bestConfigResult = bestConfig.getResult();
		System.out.println("Best is : " + bestConfigResult);
		System.out.println("Number of executions: " + evaluator.getCount());
		return bestConfigResult;
	}
}
