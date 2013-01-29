package org.tunup.modules.kmeans.utils;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

public class ClusterOperations {
	
	/**
	 * Part of the code has been copied from the KMeans.java class of JavaML
	 * framework (javaml.sf.net).
	 */
	public static Instance getCentroidCoordinates(Dataset data, DistanceMeasure dm) {
		int instanceLength = data.get(0).noAttributes();
		double[] sumPosition = new double[instanceLength];
		int countPosition = 0;
		for (int i = 0; i < data.size(); i++) {
			Instance in = data.instance(i);
			for (int j = 0; j < instanceLength; j++) {
				sumPosition[j] += in.value(j);
			}
			countPosition++;
		}
		double[] tmp = new double[instanceLength];
		for (int j = 0; j < instanceLength; j++) {
			tmp[j] = (float) sumPosition[j] / countPosition;
		}
		Instance centroid = new DenseInstance(tmp);
		return centroid;
	}
	
	public static Instance createInstance(double[] attributes) {
		return new DenseInstance(attributes);
	}
}
