package org.tunup.modules.kmeans.execution;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.tools.data.FileHandler;

import org.tunup.modules.kmeans.configuration.KMeansConfigResult;
import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.evaluation.KMeansAicScore;
import org.tunup.modules.kmeans.space.KMeansDistanceMeasure;

import com.google.common.base.Preconditions;

/**
 * Executor of KMeans with input parameters k, distance metric and iterations.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansExecutor {

	private static final Map<String, KMeansExecutor> INSTANCES =
	    new HashMap<String, KMeansExecutor>();

	private final String separator;
	private final int class_index;

	private final DistanceMeasure[] distMeasures = KMeansDistanceMeasure.getDistMeasures();

	private int count = 0;

	private final String filePath;

	private double fitnessValue = 0;
	private double sseScore = 0;
	private double scsScore = 0;
	private double score = 0;

	private final ClusterEvaluationWithNaturalFitness ce;

	private Dataset[] clusters;

	private Map<KMeansConfiguration, Double> cache = new HashMap<KMeansConfiguration, Double>();

	public static KMeansExecutor getInstance(String name) {
		return INSTANCES.get(name);
	}

	public static KMeansExecutor createInstance(String name, String filePath, String separator,
	    int dim, ClusterEvaluationWithNaturalFitness ce) {
		if (!INSTANCES.containsKey(name)) {
			KMeansExecutor instance = new KMeansExecutor(filePath, separator, dim, ce);
			INSTANCES.put(name, instance);
		}
		return INSTANCES.get(name);
	}

	public KMeansExecutor(String filePath, String separator, int classIndex) {
		this(filePath, separator, classIndex, new KMeansAicScore());
	}

	public KMeansExecutor(String filePath, String separator, int classIndex,
	    ClusterEvaluationWithNaturalFitness ce) {
		this.filePath = filePath;
		this.separator = separator;
		this.class_index = classIndex;
		this.ce = ce;
	}

	public double executeAndEvaluate(int k, int distMeasureId, int iterations)
	    throws IOException {
		KMeansConfiguration config = new KMeansConfiguration(k, distMeasureId, iterations);
		return executeAndEvaluate(config);
	}

	/**
	 * Execute and evaluate based on the cluster evaluation specified in the
	 * constructor. All the evaluations are cached for a fast look-up.
	 * 
	 * @param config
	 * @return
	 * @throws IOException
	 */
	public double executeAndEvaluate(KMeansConfiguration config) throws IOException {
		if (cache.containsKey(config)) {
			return cache.get(config);
		} else {
			double fitnessValue = evaluate(config, execute(config), ce);
			cache.put(config, fitnessValue);
			count++;
			return fitnessValue;
		}
	}

	public Dataset[] execute(KMeansConfiguration config)
	    throws IOException {
		Dataset data = FileHandler.loadDataset(new File(filePath), class_index, separator);
		int k = config.getK();

		if (k > 0) {
			/*
			 * Create a new instance of the KMeans algorithm, with no options
			 * specified. By default this will generate 4 clusters.
			 */
			Clusterer km = new KMeans(k, config.getIterations(),
			    distMeasures[config.getDistanceMeasureId()]);
			/*
			 * Cluster the data, it will be returned as an array of data sets, with
			 * each dataset representing a cluster.
			 */
			clusters = km.cluster(data);
			return clusters;
		}
		else
		{
			throw new RuntimeException("k must be positive: " + k);
		}
	}

	public double evaluate(KMeansConfiguration config, Dataset[] clusters, ClusterEvaluation ce) {
		try {
			score = ce.score(clusters);
			fitnessValue = score;
			cache.put(config, fitnessValue);
			return fitnessValue;
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException on: "
			    + config + " FitnessValue: " + fitnessValue);
			System.out.println(e.getMessage());
			e.printStackTrace();
			return 0;
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

	public KMeansConfigResult getConfigResult(KMeansConfiguration config) {
		if (cache.containsKey(config)) {
			return new KMeansConfigResult(config, cache.get(config));
		}
		throw new RuntimeException("Configuration result not available");
	}

	public int getCount() {
		return count;
	}

	public void resetCount() {
		count = 0;
	}

	public void clearCache() {
		cache.clear();
	}

	public boolean getNaturalFitness() {
		return ce.isNatural();
	}
}
