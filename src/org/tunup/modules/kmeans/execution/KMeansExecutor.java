package org.tunup.modules.kmeans.execution;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.tools.data.FileHandler;

import org.tunup.modules.kmeans.configuration.KMeansConfigResult;
import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.configuration.KMeansConfigurationWithCentroids;
import org.tunup.modules.kmeans.dataset.KMeansDatasetConfiguration;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.space.KMeansDistanceMeasure;
import org.tunup.modules.kmeans.utils.ClusterOperations;

/**
 * Executor of KMeans with input parameters k, distance metric and iterations.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansExecutor {

	private static final Map<String, KMeansExecutor> INSTANCES =
	    new HashMap<String, KMeansExecutor>();

	private String separator;
	private int class_index;

	private final KMeansDatasetConfiguration dataset;

	private final DistanceMeasure[] distMeasures = KMeansDistanceMeasure.getDistMeasures();


	private String filePath;

	private double fitnessValue = 0;

	private ClusterEvaluationWithNaturalFitness ce;

	private Dataset[] clusters;

	private Dataset data;
	
	public Dataset getData() {
		return data;
	}

	public static KMeansExecutor getInstance(String name) {
		return INSTANCES.get(name);
	}

	public static KMeansExecutor createInstance(KMeansDatasetConfiguration dataset) {
		return createInstance(dataset, null);
	}

	public static KMeansExecutor createInstance(KMeansDatasetConfiguration dataset,
	    ClusterEvaluationWithNaturalFitness ce) {
		if (!INSTANCES.containsKey(dataset.getName())) {
			KMeansExecutor instance = new KMeansExecutor(dataset, ce);
			INSTANCES.put(dataset.getName(), instance);
		}
		return INSTANCES.get(dataset.getName());
	}

	public KMeansExecutor(KMeansDatasetConfiguration dataset, ClusterEvaluationWithNaturalFitness ce) {
		this(dataset, ce, "");
	}

	public KMeansExecutor(KMeansDatasetConfiguration dataset, ClusterEvaluationWithNaturalFitness ce,
	    String relPathPrefix) {
		this.dataset = dataset;
		this.filePath = dataset.getFilePath();
		this.separator = dataset.getSeparator();
		this.class_index = dataset.getClassIndex();
		this.ce = ce;
		try {
			data = FileHandler.loadDataset(new File(relPathPrefix + filePath), class_index, separator);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public KMeansExecutor(KMeansDatasetConfiguration dataset) {
		this(dataset, null);
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
		return evaluate(config, execute(config), ce);
	}

	/**
	 * Execute k-means specifying the initial centroids.
	 * 
	 * @param config
	 * @return
	 */
	public Dataset[] execute(KMeansConfigurationWithCentroids config) {
		int k = config.getK();
		if (k > 0) {
			/*
			 * Create a new instance of the KMeans algorithm, with no options
			 * specified. By default this will generate 4 clusters.
			 */
			Clusterer km = new KMeansWithInitialCentroids(k, config.getIterations(),
			    distMeasures[config.getDistanceMeasureId()],
			    createCentroids(config.getInitCentroidsAttributes()));
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

	protected Instance[] createCentroids(double[][] attributes) {
		int m = attributes[0].length;
		int k = attributes.length;
		Instance[] centroids = new Instance[k];
		for (int i = 0; i < k; i++) {
			assert attributes[i].length == m;
			centroids[i] = ClusterOperations.createInstance(attributes[i]);
		}
		return centroids;
	}

	public Dataset[] execute(KMeansConfiguration config) {
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
			return ce.score(clusters);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException on: "
			    + config + " FitnessValue: " + fitnessValue);
			System.out.println(e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}

	public KMeansConfigResult executeAndEvaluate(KMeansConfiguration config, int n,
	    ClusterEvaluation[] ces) throws IOException {
		double fitnessValues[][] = new double[ces.length][n];

		for (int i = 0; i < n; i++) {
				clusters = execute(config);
				for (int c = 0; c < ces.length; c++) {
					fitnessValues[c][i] = evaluate(config, clusters, ces[c]);
				}
		}
		KMeansConfigResult result = new KMeansConfigResult(dataset, config, n, ces, fitnessValues);
		return result;
	}

	public double getFitnessValue() {
		return fitnessValue;
	}

	public Dataset[] getClusters() {
		return clusters;
	}

	public boolean getNaturalFitness() {
		return ce.isNatural();
	}
}
