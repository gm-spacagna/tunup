package org.tunup.modules.kmeans.evaluation;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

/*
 * Rand index to compute agreement between generated clusters and a-priori knowledge of labels.
 * The dataset should contains a label for each instance.
 */
public class RandIndexScore implements ClusterEvaluationWithNaturalFitness {

	/* initial dataset */
	protected final Dataset dataset;

	public RandIndexScore(Dataset dataset) {
		super();
		this.dataset = dataset;
	}

	@Override
	public double score(Dataset[] clusters) {
		/* we need at least one class */
		if (dataset.classes().isEmpty()) {
			return 0;
		}
		return calculateIndex(dataset, clusters);
	}

	@Override
	public boolean compareScore(double score1, double score2) {
		return Math.abs(score2) < Math.abs(score1);
	}

	@Override
	public boolean isNatural() {
		return true;
	}

	/**
	 * number of pairs of points with the same label in C and assigned to the same
	 * cluster in K
	 */
	protected int a = 0;
	/**
	 * the number of pairs with the same label, but in different clusters
	 */
	protected int b = 0;
	/**
	 * number of pairs in the same cluster, but with different class labels
	 */
	protected int c = 0;
	/**
	 * number of pairs with different label and different cluster
	 */
	protected int d = 0;

	/**
	 * The index produces a result in the range [0,1], where a value of 1.0
	 * indicates that the labels and the calculated clusters are identical. A high
	 * value for this measure generally indicates a high level of agreement
	 * between a clustering and the annotated natural classes.
	 */
	protected double calculateIndex(Dataset dataset, Dataset[] clusteredData) {
		countPairs(dataset, clusteredData);
		int n = dataset.size();
		double randIndex = ((double) (a + d)) / ((double) (a + b + c + d));
		return randIndex;
	}

	/**
	 * this looks at every pair of points and determines if the labels and the
	 * calculated clusters are equal or different. based on the result the
	 * instance variables a b c or d are incremented
	 */
	protected void countPairs(Dataset dataset, Dataset[] clusteredData) {
		Instance point1;
		Instance point2;

		for (int i = 0; i < dataset.size() - 1; i++) {
			for (int j = i + 1; j < dataset.size(); j++) {
				point1 = dataset.get(i);
				point2 = dataset.get(j);

				if (((String) point1.classValue()).equals((String) (point2.classValue()))) {
					// points with same label
					if (getCalculatedClusternumber(clusteredData, point1) == getCalculatedClusternumber(
					    clusteredData, point2)) {
						// points assigned to same cluster
						a++;
					} else {
						// different cluster
						b++;
					}
				}
				else {
					// different label
					if (getCalculatedClusternumber(clusteredData, point1) == getCalculatedClusternumber(
					    clusteredData, point2)) {
						// same calculated cluster
						c++;
					}
					else {
						// different cluster
						d++;
					}
				}
			}
		}
	}

	private Dataset getCalculatedClusternumber(Dataset[] clusteredData, Instance point) {
		for (Dataset dataset : clusteredData) {
			if (dataset.contains(point)) {
				return dataset;
			}
		}
		throw new RuntimeException("Instance not belonging to any cluster");
	}
}
