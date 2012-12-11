package org.tunup.modules.kmeans.javaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfCentroidSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.AngularDistance;
import net.sf.javaml.distance.ChebychevDistance;
import net.sf.javaml.distance.CosineDistance;
import net.sf.javaml.distance.CosineSimilarity;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.JaccardIndexDistance;
import net.sf.javaml.distance.JaccardIndexSimilarity;
import net.sf.javaml.distance.ManhattanDistance;
import net.sf.javaml.distance.MinkowskiDistance;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import net.sf.javaml.distance.RBFKernel;
import net.sf.javaml.distance.SpearmanFootruleDistance;
import net.sf.javaml.distance.SpearmanRankCorrelation;
import net.sf.javaml.tools.data.FileHandler;

/**
 * Executor of KMeans with input parameters k, distance metric and iterations.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansExecutor {

	private static final Map<String, KMeansExecutor> INSTANCES = new HashMap<String, KMeansExecutor>();

	private static final DistanceMeasure[] DIST_MEASURES = { new AngularDistance(), // 0
	    new ChebychevDistance(), // 1
	    new CosineDistance(), // 2
	    new CosineSimilarity(), // 3
	    new EuclideanDistance(), // 4
	    new JaccardIndexDistance(), // 5
	    new JaccardIndexSimilarity(), // 6
	    new ManhattanDistance(), // 7
	    new MinkowskiDistance(), // 8
	    new PearsonCorrelationCoefficient(), // 9
	    new RBFKernel(), // 10
	    //new SpearmanFootruleDistance(), // 11
	    new SpearmanRankCorrelation() // 12
	};

	private final String filePath;

	private double fitnessValue = 0;
	private double sseScore = 0;
	private double scsScore = 0;
	private double aicScore = 0;
	private double bicScore = 0;

	private Dataset[] clusters;

	private Map<KMeansParameters, Double> cache = new HashMap<KMeansParameters, Double>();

	public static KMeansExecutor getInstance(String name) {
		return INSTANCES.get(name);
	}

	public static KMeansExecutor createInstance(String name, String filePath) {
		if (INSTANCES.containsKey(name)) {
			throw new RuntimeException("Instance already existent");
		}
		KMeansExecutor instance = new KMeansExecutor(filePath);
		INSTANCES.put(name, instance);
		return instance;
	}
	
	/**
	 * Returns the number of DistanceMeasure Metrics available.
	 * @return
	 */
	public static int getDistMeasures() {
		return DIST_MEASURES.length;
	}

	public KMeansExecutor(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Execute and evaluate KMeans using iterations = 100.
	 * 
	 * @param k
	 * @param distMeasureId
	 * @return
	 * @throws IOException
	 */
	public double executeAndEvaluate(int k, int distMeasureId) throws IOException {
		return executeAndEvaluate(k, distMeasureId, 100);
	}

	/**
	 * Execute JavaML KMeans and evaluate using AIC index.
	 * 
	 * @param k
	 * @param distMeasureId
	 * @return The fitness value computed in a non natural way.
	 * @throws IOException
	 *           In case is not possible to read from the file.
	 */
	public double executeAndEvaluate(int k, int distMeasureId, int iterations) 
			throws IOException {
		DistanceMeasure distMeasure = DIST_MEASURES[distMeasureId];
		KMeansParameters config = new KMeansParameters(k, distMeasureId, iterations);
		/* Check in the cache */

		if (!cache.containsKey(config)) {
			System.out.println("New entry: " + k + " " + distMeasureId);

			// execute:

			/* Load a dataset */
			Dataset data = FileHandler.loadDataset(new File(filePath), 4, ",");

			// data = FileHandler.loadDataset(new File("data/internet_ads/ad.data"),
			// 4, ",");

			if (k > 0) {
				/*
				 * Create a new instance of the KMeans algorithm, with no options
				 * specified. By default this will generate 4 clusters.
				 */
				Clusterer km = new KMeans(k, iterations, distMeasure);
				/*
				 * Cluster the data, it will be returned as an array of data sets, with
				 * each dataset representing a cluster.
				 */
				clusters = km.cluster(data);

				// evaluate:

				/* Create a measure for the cluster quality */
				ClusterEvaluation sse = new SumOfSquaredErrors();
				ClusterEvaluation scs = new SumOfCentroidSimilarities();
				ClusterEvaluation aic = new AICScore();
				ClusterEvaluation bic = new BICScore();

				/* Measure the quality of the clustering */
				// sseScore = sse.score(clusters);
				// scsScore = scs.score(clusters);
				aicScore = aic.score(clusters);
				// bicScore = bic.score(clusters);

				fitnessValue = aicScore;
				cache.put(config, fitnessValue);
				return fitnessValue;
			} else {
				return 0;
			}
		} else {
			System.out.println("Cached: " + k + " " + distMeasureId);
			return cache.get(config);
		}
	}

	public double getFitnessValue() {
		return fitnessValue;
	}

	public double getSseScore() {
		return sseScore;
	}

	public double getScsScore() {
		return scsScore;
	}

	public Dataset[] getClusters() {
		return clusters;
	}
}
