package org.tunup.modules.kmeans.javaml;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfCentroidSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;

/**
 * Executor of KMeans provided in JavaML framework.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansOnlyKExecutor {

	private double fitnessValue = 0;
	private double sseScore = 0;
	private double scsScore = 0;
	private double aicScore = 0;
	private double bicScore = 0;

	private Dataset[] clusters;

	private int k = 0;

	public void execute(int k) throws IOException {
		/* Load a dataset */
		Dataset data = FileHandler.loadDataset(new File("data/iris/iris.data"), 4,
		    ",");
		/*
		 * Create a new instance of the KMeans algorithm, with no options specified.
		 * By default this will generate 4 clusters.
		 */
		if (k > 0) {
			this.k = k;
			Clusterer km = new KMeans(k);
			/*
			 * Cluster the data, it will be returned as an array of data sets, with
			 * each dataset representing a cluster.
			 */
			clusters = km.cluster(data);
		}
	}

	public double evaluate() {

		if (k > 0) {

			/* Create a measure for the cluster quality */
			ClusterEvaluation sse = new SumOfSquaredErrors();
			ClusterEvaluation scs = new SumOfCentroidSimilarities();
			ClusterEvaluation aic = new AICScore();
			ClusterEvaluation bic = new BICScore();

			/* Measure the quality of the clustering */
			sseScore = sse.score(clusters);
			scsScore = scs.score(clusters);
			aicScore = aic.score(clusters);
			bicScore = bic.score(clusters);

			fitnessValue = 1 / (aicScore * bicScore);
		}
		return fitnessValue;
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
