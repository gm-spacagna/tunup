package org.tunup.modules.kmeans.space;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A single parameter of k-means algorithm.
 * 
 * @param <T>
 *          The type of the value of the parameter.
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansParameterDimension<T> extends ParameterDimension<T> {

	public KMeansParameterDimension(String name, List<T> vals) {
		super(name, vals);
	}

	public KMeansParameterDimension(String name) {
	  super(name);
  }

	public String getName() {
		return name;
	}

	public List<T> getVals() {
		return vals;
	}

	/**
	 * Divide the range of possible values into n adjacent sub ranges.
	 * 
	 * @param n
	 *          Number of sub ranges
	 * @return List of all the sub ranges.
	 */
	public List<KMeansParameterDimension<T>> divideRanges(int n) {
		if (n > vals.size()) {
			throw new RuntimeException("Val of n must be smaller or equal to the cardinality of the " +
			    "vals. N: " + n + " Cardinality: " + vals.size());
		}
		List<KMeansParameterDimension<T>> ranges = new ArrayList<KMeansParameterDimension<T>>(n);
		int delta = vals.size() / n;
		int subMax = delta - 1;
		int subMin = 0;
		int counter = 0;
		for (int i = 0; i < vals.size(); i++) {
			if (i == subMax) {
				ranges.add(new KMeansParameterDimension<T>(name + counter++, vals.subList(subMin, subMax + 1)));
				subMin = subMax + 1;
				subMax += delta;
			}
		}
		return ranges;
	}

	/**
	 * Divide the set of possible values into n orthogonal sub spaces with random
	 * picked values.
	 * 
	 * @param n
	 * @return
	 */
	public List<KMeansParameterDimension<T>> randomDivideSpace(int n) {
		int size = vals.size();
		if (n > size) {
			throw new RuntimeException("Val of n must be smaller or equal to the cardinality of the " +
			    "vals. N: " + n + " Cardinality: " + vals.size());
		}
		List<T> remainingVals = new ArrayList<T>(vals);
		List<KMeansParameterDimension<T>> result = new ArrayList<KMeansParameterDimension<T>>(n);
		Random random = new Random();
		int subSize = size / n;
		for (int i = 0; i < n; i++) {
			List<T> subValues;
			if (i == n - 1) {
				subValues = new ArrayList<T>(remainingVals);
			} else {
				subValues = new ArrayList<T>(subSize);
				int count = 0;
				while (count++ < subSize) {
					int pos = random.nextInt(remainingVals.size());
					subValues.add(remainingVals.get(pos));
					remainingVals.remove(pos);
				}
			}
			result.add(new KMeansParameterDimension<T>(name + i, subValues));
		}
		return result;
	}

	@Override
  public String toString() {
	  return name + " -> " + vals;
  }
}
