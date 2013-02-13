package org.tunup.modules.kmeans.tuning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
import org.tunup.modules.kmeans.space.KMeansDistanceMeasure;

/**
 * Evaluation of the fitness functions in the entire k-means configuration
 * space. The fitness values for each configuration are averaged over n
 * different executions. Outputs are printed into a file.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansFullSpaceFitnessEvaluation extends AbstractKMeansTuning {

	static KMeansDatasetConfiguration EXEC_CONFIG = new RedWineConfiguration();

	public static void main(String[] args) {

		String name = EXEC_CONFIG.getName();
		System.out.println("KMeans full space fitness evaluation dataset " + name);
		int n = 20; // number of executions to average
		String filePath = "output/" + name + "_iter20_n" + n + "_complete" + ".dat";
		KMeansFullSpaceFitnessEvaluation instance = new KMeansFullSpaceFitnessEvaluation(
		    EXEC_CONFIG, null, filePath, n);
		instance.writeOutput();
		System.out.println("Output at " + filePath);
		instance.printBest();
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
		this.n = n;
	}

	ClusterEvaluationWithNaturalFitness aic = new KMeansAicScore();
	ClusterEvaluationWithNaturalFitness db = new DaviesBouldinScore();
	ClusterEvaluationWithNaturalFitness dunn = new DunnScore();
	ClusterEvaluationWithNaturalFitness rand = new RandScore(executor.getData());
	ClusterEvaluationWithNaturalFitness aRand = new AdjustedRandScore(executor.getData());
	ClusterEvaluationWithNaturalFitness sil = new SilhouetteScore();
	
	ClusterEvaluationWithNaturalFitness[] clusterEvaluations =
	    new ClusterEvaluationWithNaturalFitness[] { aic, db, dunn, sil, aRand };
	Map<ClusterEvaluationWithNaturalFitness, KMeansConfiguration> bestConfMap =
	    new LinkedHashMap<>(3);
	Map<ClusterEvaluationWithNaturalFitness, Double> bestFitnessValMap =
	    new LinkedHashMap<>(3);
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
				for (int distMeasId = 0; distMeasId < dataset.getDistMeasures(); distMeasId++) {
					int iter = 20;
					KMeansConfiguration config = new KMeansConfiguration(k, distMeasId, iter);
					System.out.println(config);

					Map<ClusterEvaluation, Double> fitnessVals = new LinkedHashMap<>(3);

					for (int i = 0; i < n; i++) {
						Dataset[] clusters = executor.execute(config);
						for (ClusterEvaluationWithNaturalFitness ce : clusterEvaluations) {
							Double val = fitnessVals.get(ce);
							if (val == null) {
								val = new Double(0);
								fitnessVals.put(ce, val);
							}
							val += executor.evaluate(config, clusters, ce);
							if (i == n - 1) {
								val /= n;
							}
							fitnessVals.put(ce, val);
						}
					}

					System.out.println(" fitnessVal: " + fitnessVals.get(aic) + "," + fitnessVals.get(db)
					    + "," + fitnessVals.get(dunn) + "," + fitnessVals.get(rand) + ","
					    + fitnessVals.get(aRand));
					String entry = k + " " + distMeasId + " " + iter;
					for (ClusterEvaluationWithNaturalFitness ce : clusterEvaluations) {
						double fitnessVal = fitnessVals.get(ce);
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
