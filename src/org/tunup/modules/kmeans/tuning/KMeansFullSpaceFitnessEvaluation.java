package org.tunup.modules.kmeans.tuning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.core.Dataset;

import org.tunup.modules.kmeans.configuration.KMeansConfigResult;
import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.dataset.*;
import org.tunup.modules.kmeans.evaluation.AdjustedRandScore;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithConfigurableDistanceMeasure;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.evaluation.DaviesBouldinScore;
import org.tunup.modules.kmeans.evaluation.DunnScore;
import org.tunup.modules.kmeans.evaluation.KMeansAicScore;
import org.tunup.modules.kmeans.evaluation.RandScore;
import org.tunup.modules.kmeans.evaluation.SilhouetteScore;
import org.tunup.modules.kmeans.space.KMeansDistanceMeasures;

/**
 * Evaluation of the fitness functions in the entire k-means configuration
 * space. The fitness values for each configuration are averaged over n
 * different executions. Outputs are printed into a file.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansFullSpaceFitnessEvaluation extends AbstractKMeansTuning {

	static KMeansDatasetConfiguration DATASET = new Arcbek2Configuration();
	static int N = 20; // number of executions to average
	
	public static void main(String[] args) {

		String name = DATASET.getName();
		System.out.println("KMeans full space fitness evaluation dataset " + name);
		
		String filePath = "output/" + name + "_iter20_n" + N + "_median" + ".dat";
		long start = System.currentTimeMillis();
		KMeansFullSpaceFitnessEvaluation instance = new KMeansFullSpaceFitnessEvaluation(
		    DATASET, null, filePath, N);
		instance.writeOutput();
		System.out.println("Output at " + filePath);
		instance.printBest();
		long end = System.currentTimeMillis();
		System.out.println("Total time: " + (end - start));
	}

	private void printBest() {
		for (ClusterEvaluationWithNaturalFitness ce : clusterEvaluations) {
			System.out.println(ce.getClass().getSimpleName() + " " + bestConfMap.get(ce) + " fitness:"
			    + bestFitnessValMap.get(ce));
		}
	}

	KMeansFullSpaceFitnessEvaluation(KMeansDatasetConfiguration config,
	    ClusterEvaluationWithNaturalFitness ce, String filePath, int n) {
		super(config, ce);
		this.filePath = filePath;
		this.N = n;
	}

	ClusterEvaluationWithNaturalFitness aic = new KMeansAicScore();
	ClusterEvaluationWithNaturalFitness db = new DaviesBouldinScore();
	ClusterEvaluationWithNaturalFitness dunn = new DunnScore();
	ClusterEvaluationWithNaturalFitness rand = new RandScore(executor.getData());
	ClusterEvaluationWithNaturalFitness aRand = new AdjustedRandScore(executor.getData());
	ClusterEvaluationWithNaturalFitness sil = new SilhouetteScore();

	ClusterEvaluationWithNaturalFitness[] clusterEvaluations =
	    new ClusterEvaluationWithNaturalFitness[] { aic, dunn, db, sil };
	Map<ClusterEvaluationWithNaturalFitness, KMeansConfiguration> bestConfMap =
	    new LinkedHashMap<ClusterEvaluationWithNaturalFitness, KMeansConfiguration>(3);
	Map<ClusterEvaluationWithNaturalFitness, Double> bestFitnessValMap =
	    new LinkedHashMap<ClusterEvaluationWithNaturalFitness, Double>(3);
	private final String filePath;
	private int n;

	private void writeOutput() {

		for (ClusterEvaluationWithNaturalFitness ce : clusterEvaluations) {
			bestFitnessValMap.put(ce, (ce.isNatural()) ? Double.MIN_VALUE : Double.MAX_VALUE);
		}

		File file = new File(filePath);

		try {
			FileWriter fw = new FileWriter(file);
			for (int k = dataset.getMinK(); k <= dataset.getMaxK(); k++) {
				for (int distMeasId : dataset.getDistanceMeasureIds()) {
					int iter = 20;
					KMeansConfiguration config = new KMeansConfiguration(k, distMeasId, iter);
					System.out.println(config);

					KMeansConfigResult result = executor.executeAndEvaluate(config, N, clusterEvaluations);
					double[] average = result.getMean();
					double[] median = result.getMedian();
					double[] stDev = result.getStandardDeviation();
					System.out.println("Average: " + Arrays.toString(average));
					System.out.println("Median: " + Arrays.toString(median));
					System.out.println("St Dev: " + Arrays.toString(stDev));
					String entry = k + " " + distMeasId + " " + iter;
					for (int i = 0; i < clusterEvaluations.length; i++) {
//						double fitnessVal = average[i];
						double fitnessVal = median[i];
						ClusterEvaluationWithNaturalFitness ce = clusterEvaluations[i];
						double best = bestFitnessValMap.get(ce);
						if ((ce.isNatural() && fitnessVal > best)
						    || (!ce.isNatural() && fitnessVal < best)) {
							bestFitnessValMap.put(ce, fitnessVal);
							bestConfMap.put(ce, config);
						}
						entry += " " + fitnessVal;
					}
					entry += "\n";
					fw.write(entry);
					fw.flush();
				}
			}
			fw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public KMeansConfigResult getBestConfig() {
		throw new RuntimeException("Operation not supported");
	}
}
