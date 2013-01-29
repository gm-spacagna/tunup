package org.tunup.modules.kmeans.evaluation;

import org.tunup.modules.kmeans.utils.ClusterOperations;

import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;

/**
 * Cluster Evaluation through the Davies-Bouldin index. It favours clusters with
 * low intra-cluster distances and high inter-cluster distances. A smaller value
 * is considered a better clustering.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class DaviesBouldinScore implements ClusterEvaluationWithNaturalFitness,
    ClusterEvaluationWithConfigurableDistanceMeasure {

	protected DistanceMeasure distanceMeasure;

	@Override
	public DistanceMeasure getDistanceMeasure() {
		return distanceMeasure;
	}

	@Override
	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	@Override
	public double score(Dataset[] clusters) {
		return getDaviesBouldin(clusters);
	}

	@Override
	public boolean compareScore(double score1, double score2) {
		return Math.abs(score2) < Math.abs(score1);
	}

	/**
	 * Creates a Davies-Bouldin cluster evaluation score with predefined Euclidean
	 * distance measure.
	 */
	public DaviesBouldinScore() {
		this(new EuclideanDistance());
	}

	/**
	 * Creates a Davies-Bouldin cluster evaluation score with the specified
	 * distance measure.
	 * 
	 * @param distanceMeasure
	 *          The distance measure used to compute distances.
	 */
	public DaviesBouldinScore(DistanceMeasure measure) {
		super();
		this.distanceMeasure = measure;
	}

	/**
	 * Part of the code has been copied from the CentroidBasedEvaluator.java class
	 * of RapidMinner framework (rapidminner.com).
	 */
	private double getDaviesBouldin(Dataset[] clusters) {
		int numberOfClusters = clusters.length;

		if (numberOfClusters == 1) {
			throw new RuntimeException(
			    "Impossible to evaluate Davies-Bouldin index over a single cluster");
		} else {
			// counting distances within
			double[] withinClusterDistance = new double[numberOfClusters];

			for (int i = 0; i < numberOfClusters; i++) {
				Dataset cluster = clusters[i];
				Instance centroid = ClusterOperations.getCentroidCoordinates(cluster, distanceMeasure);
				for (Instance point : cluster) {
					withinClusterDistance[i] += distanceMeasure.measure(point,
					    centroid);
				}
			}

			// averaging by cluster sizes and sum over all
			for (int i = 0; i < numberOfClusters; i++) {
				withinClusterDistance[i] /= clusters[i].size();
			}

			double result = 0.0;

			for (int i = 0; i < numberOfClusters; i++) {
				double max = Double.NEGATIVE_INFINITY;
				for (int j = 0; j < numberOfClusters; j++)
					if (i != j) {
						double val = (withinClusterDistance[i] + withinClusterDistance[j])
						    / distanceMeasure.measure(ClusterOperations.getCentroidCoordinates(clusters[i], distanceMeasure),
						        ClusterOperations.getCentroidCoordinates(clusters[j], distanceMeasure));
						if (val > max)
							max = val;
					}
				result = result + max;
			}
			return result / numberOfClusters;
		}
	}

	

	@Override
	public boolean isNatural() {
		return false;
	}
}
