package org.tunup.modules.kmeans.tuning;

import org.tunup.modules.kmeans.dataset.AbaloneConfiguration;
import org.tunup.modules.kmeans.dataset.Arcbek1Configuration;
import org.tunup.modules.kmeans.dataset.Arcbek2Configuration;
import org.tunup.modules.kmeans.dataset.D31Configuration;
import org.tunup.modules.kmeans.dataset.FlameConfiguration;
import org.tunup.modules.kmeans.dataset.HarConfiguration;
import org.tunup.modules.kmeans.dataset.IrisConfiguration;
import org.tunup.modules.kmeans.dataset.KMeansDatasetConfiguration;
import org.tunup.modules.kmeans.dataset.LibrasConfiguration;
import org.tunup.modules.kmeans.dataset.R15Configuration;
import org.tunup.modules.kmeans.dataset.RedWineConfiguration;
import org.tunup.modules.kmeans.dataset.SeedsConfiguration;
import org.tunup.modules.kmeans.dataset.Synthetic1Configuration;
import org.tunup.modules.kmeans.dataset.WhiteWineConfiguration;
import org.tunup.modules.kmeans.dataset.WineConfiguration;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.evaluation.KMeansAicScore;

public class Tuning {

	public static void main(String[] args) {
		KMeansDatasetConfiguration[] configurations = new KMeansDatasetConfiguration[] {
		    new WineConfiguration(),
		    new IrisConfiguration(), new AbaloneConfiguration(), new SeedsConfiguration(),
		    new RedWineConfiguration() };

		configurations = new KMeansDatasetConfiguration[] { new Synthetic1Configuration() };
		ClusterEvaluationWithNaturalFitness ce;
		ce = new KMeansAicScore();
		for (KMeansDatasetConfiguration config : configurations) {
			System.out.println(config);

			AbstractKMeansTuning tuning;

//			System.out.println("KMeans Parallel GA Tuning");
//			tuning = new KMeansTuningParallelGA(config, ce);
//			tuning.getBestConfig();
//			System.out.println("Time: " + tuning.getTime());
//			System.out.println("");

//			System.out.println("KMeans GA Tuning");
//			tuning = new KMeansTuningGA(config, ce);
//			tuning.getBestConfig();
//			System.out.println("Time: " + tuning.getTime());
//			System.out.println("");

			
			  System.out.println("KMeans GA Tuning with Islands"); tuning = new
			  KMeansTuningGAWithIslands(config, ce);
			  tuning.getBestConfig();
			 

			/*
			 * System.out.println("KMeans BruteForce Tuning"); tuning = new
			 * KMeansTuningBruteForce(config, ce); tuning.getExecutor().clearCache();
			 * tuning.getExecutor().resetCount(); tuning.getBestConfig();
			 * System.out.println("");
			 */
		}
	}
}
