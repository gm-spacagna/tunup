package org.tunup.modules.kmeans.javaml;

import java.util.Arrays;
import java.util.List;

public class KMeansConfiguration {
	int k;
	int distanceMeasureId;
	int iterations;

	public KMeansConfiguration(int k, int distanceMeasureId, int iterations) {
		this.k = k;
		this.distanceMeasureId = distanceMeasureId;
		this.iterations = iterations;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KMeansConfiguration) {
			KMeansConfiguration obj2 = (KMeansConfiguration) obj;
			boolean equals = k == obj2.k && distanceMeasureId == obj2.distanceMeasureId
			    && iterations == obj2.iterations;
			return equals;
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public int hashCode() {
		int digits = String.valueOf(k).length();
		int hash = (distanceMeasureId << digits) | k;
		digits += String.valueOf(distanceMeasureId).length();
		hash |= iterations << digits;
		return hash;
	}

	public int getK() {
		return k;
	}

	public int getDistanceMeasureId() {
		return distanceMeasureId;
	}

	public int getIterations() {
		return iterations;
	}

	public List<int[]> toList() {
		return Arrays.asList(toArray());
	}

	public int[] toArray() {
		return new int[] { k, distanceMeasureId, iterations };
	}

	public int getParam(int index) {
		switch (index) {
		case 0:
			return k;
		case 1:
			return distanceMeasureId;
		case 2:
			return iterations;
		default:
			throw new RuntimeException("Parameter Index not valid " + index);
		}
	}

	@Override
	public String toString() {
		return "K: " + k + " DistMeasureId: " + distanceMeasureId + " Iterations: " + iterations;
	}
}
