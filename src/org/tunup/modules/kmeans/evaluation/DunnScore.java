package org.tunup.modules.kmeans.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;

/**
 * Dunn Index to identify dense and well-separated clusters. Higher values means
 * better clustering.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class DunnScore implements ClusterEvaluationWithNaturalFitness,
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

	/**
	 * Creates a Dunn cluster evaluation score with predefined Euclidean distance
	 * measure.
	 */
	public DunnScore() {
		this(new EuclideanDistance());
	}

	/**
	 * Creates a Dunn cluster evaluation score with the specified distance
	 * measure.
	 * 
	 * @param distanceMeasure
	 *          The distance measure used to compute distances.
	 */
	public DunnScore(DistanceMeasure distanceMeasure) {
		super();
		this.distanceMeasure = distanceMeasure;
	}

	private double calculateIndex(Dataset[] clusters) {
		Map<Integer, Dataset> clustermap = new LinkedHashMap<Integer, Dataset>(clusters.length);
		Integer i = 0;
		for (Dataset cluster : clusters) {
			clustermap.put(i++, cluster);
		}
		Integer[] clusterIds = (Integer[]) clustermap.keySet().toArray(new Integer[0]);

		double maxIntraClusterdist = 0;
		Double minClusterDistance = null;
		double temp = 0;
		double clustDist;
		Dataset clusterA, clusterB;

		for (i = 0; i < clusterIds.length; i++) {
			clusterA = clustermap.get(clusterIds[i]);
			temp = getMaxIntraClusterDistance(clusterA);
			if (temp > maxIntraClusterdist) {
				maxIntraClusterdist = temp;
			}
			for (int j = i + 1; j < clusterIds.length; j++) {
				clusterB = clustermap.get(clusterIds[j]);
				clustDist = calculateClusterdistance(clusterA, clusterB);
				if (minClusterDistance == null || clustDist < minClusterDistance) {
					minClusterDistance = clustDist;
				}

			}
		}
		if (minClusterDistance == null) {
			return Double.NaN; // This normaly means we have only one cluster
			// and can therefore not calculate anything
		}
		return minClusterDistance / maxIntraClusterdist;
	}

	private double getMaxIntraClusterDistance(Dataset cluster) {
		Instance a;
		Instance b;

		double maxIntraClusterDistance = 0.0f;
		for (int i = 0; i < cluster.size() - 1; i++) {
			a = cluster.get(i);
			for (int j = i + 1; j < cluster.size(); j++) {
				b = cluster.get(j);
				double currDist = distanceMeasure.measure(a, b);
				if (currDist > maxIntraClusterDistance) {
					maxIntraClusterDistance = currDist;
				}
			}
		}
		return maxIntraClusterDistance;
	}

	private double calculateClusterdistance(Dataset cluster1, Dataset cluster2) {
		Instance point1, point2;
		DistanceMeasure distanceMeasure = new EuclideanDistance();

		ArrayList<Double> distances = new ArrayList<Double>();
		for (int i = 0; i < cluster1.size(); i++) {
			point1 = cluster1.get(i);
			for (int j = 0; j < cluster2.size(); j++) {
				point2 = cluster2.get(j);

				double tempDist = distanceMeasure.measure(point1, point2);
				distances.add(tempDist);
			}
		}
		assert (distances.size() == cluster1.size() * cluster2.size());
		return Collections.min(distances);
	}

	@Override
	public double score(Dataset[] clusters) {
		return calculateIndex(clusters);
	}

	@Override
	public boolean compareScore(double score1, double score2) {
		return Math.abs(score2) < Math.abs(score1);
	}

	@Override
	public boolean isNatural() {
		return true;
	}
}
