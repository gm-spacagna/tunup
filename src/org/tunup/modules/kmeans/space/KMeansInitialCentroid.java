package org.tunup.modules.kmeans.space;

import java.util.List;
import java.util.Random;

/**
 * Initial centroids of k-means.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansInitialCentroid {

	protected final int m;
	protected double[] min;
	protected double[] max;
	protected double[] sensibility;
	
	public KMeansInitialCentroid(int m, double[] min, double[] max, double[] sensibility) {
	  super();
	  this.m = m;
	  this.min = min;
	  this.max = max;
	  this.sensibility = sensibility;
  }

	double[] pickRandomCentroid() {
		double[] centroid = new double[m];
		for (int i = 0; i < m; i++) {
			centroid[i] = pickRandomVal(i);
		}
		return centroid;
	}

	private double pickRandomVal(int i) {
	  Random random = new Random();
	  int vals = (int)((max[i] - min[i]) / sensibility[i]);
	  int val = random.nextInt(vals);
	  return (double)(val * sensibility[i] + min[i]);
  }
}
