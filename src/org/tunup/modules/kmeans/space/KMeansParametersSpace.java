package org.tunup.modules.kmeans.space;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;

/**
 * The space of KMeans parameters identified by the 3 axis: K, DistMeasureId and
 * Iterations. It can be used to define a sub space of solutions to be explored.
 * It can not specify the 3 dimensions and contains sub spaces instead.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansParametersSpace extends ParametersSpace {

	// axis:
	KMeansParameterDimension<Integer> k;
	KMeansParameterDimension<Integer> distMeasureId;
	KMeansParameterDimension<Integer> iterations;

	public KMeansParametersSpace(KMeansParameterDimension<Integer> k,
	    KMeansParameterDimension<Integer> distMeasureId,
	    KMeansParameterDimension<Integer> iterations) {
		super();
		this.k = k;
		this.distMeasureId = distMeasureId;
		this.iterations = iterations;
		subSpaces = new ArrayList<ParametersSpace>();
	}

	/**
	 * Creates an empty KMeans Parameters Space.
	 */
	public KMeansParametersSpace() {
		this(new KMeansParameterDimension<Integer>("k"), new KMeansParameterDimension<Integer>(
		    "distance measure id"), new KMeansParameterDimension<Integer>("iterations"));
	}

	/**
	 * Divide the space of K, DistMeasureId and Iterations into a set of n
	 * not-overlapping discontinuous ranges. If n is not a power of 3 the biggest
	 * smaller value power of 3 will be considered.
	 * 
	 * @param n
	 *          Number of sub spaces.
	 * @return A list containing all the sub spaces.
	 */
	public List<KMeansParametersSpace> divideSpace(int n) {
		Double cbrt = Math.cbrt(n);
		int intCbrt = cbrt.intValue();

		List<KMeansParameterDimension<Integer>> subK = k.divideRanges(intCbrt);
		List<KMeansParameterDimension<Integer>> subDistMeasId =
		    distMeasureId.divideRanges(intCbrt);
		List<KMeansParameterDimension<Integer>> subIterations =
		    iterations.divideRanges(intCbrt);
		return generateSpaces(n, subK, subDistMeasId, subIterations);
	}

	/**
	 * Divide the space of K, DistMeasureId and Iterations into N sub spaces of
	 * randomly picked values.
	 * 
	 * @param n
	 *          Number of sub spaces.
	 * @return A list containing all the sub spaces.
	 */
	public List<KMeansParametersSpace> randomDivideSpace(int n) {

		List<KMeansParameterDimension<Integer>> subK = k.randomDivideSpace(n);
		List<KMeansParameterDimension<Integer>> subDistMeasId =
		    distMeasureId.randomDivideSpace(n);
		List<KMeansParameterDimension<Integer>> subIterations =
		    iterations.randomDivideSpace(n);
		return generateSpaces(n, subK, subDistMeasId, subIterations);
	}

	private List<KMeansParametersSpace> generateSpaces(int n,
	    List<KMeansParameterDimension<Integer>> subK,
	    List<KMeansParameterDimension<Integer>> subDistMeasId,
	    List<KMeansParameterDimension<Integer>> subIterations) {
		List<KMeansParametersSpace> spaces = new ArrayList<>(n);

		List<Triple<KMeansParameterDimension<Integer>>> subSpaces =
		    new ArrayList<Triple<KMeansParameterDimension<Integer>>>(subK.size() * subDistMeasId.size()
		        * subIterations.size());

		/* generate all possible combinations */
		for (KMeansParameterDimension<Integer> k : subK) {
			for (KMeansParameterDimension<Integer> dist : subDistMeasId) {
				for (KMeansParameterDimension<Integer> iter : subIterations) {
					subSpaces.add(new Triple<KMeansParameterDimension<Integer>>(k, dist, iter));
				}
			}
		}

		int combinations = subSpaces.size();
		int x = combinations / n;
		/*
		 * each subspace contains x random picked sub dimensions spaces except the
		 * last one that contains all the remaining sub dimensions
		 */
		Random random = new Random();
		KMeansParametersSpace space;
		for (int i = 0; i < n; i++) {
			space = new KMeansParametersSpace();
			if (i < n - 1) {
				for (int j = 0; j < x; j++) {
					int pos = random.nextInt(subSpaces.size());
					Triple<KMeansParameterDimension<Integer>> triple = subSpaces.get(pos);
					space.addSubSpaces(triple.getFirst(), triple.getSecond(), triple.getThird());
					subSpaces.remove(pos);
				}
			} else {
				/* add all remaining ones */
				for (Triple<KMeansParameterDimension<Integer>> triple : subSpaces) {
					space.addSubSpaces(triple.getFirst(), triple.getSecond(), triple.getThird());
				}
			}
			spaces.add(space);
		}
		return spaces;
	}

	/**
	 * A triple of values
	 * 
	 * @param <T>
	 *          The type of the values.
	 * @author Gianmario Spacagna (gmspacagna@gmail.com)
	 */
	private class Triple<T> {
		T first;
		T second;
		T third;

		public Triple(T first, T second, T third) {
			super();
			this.first = first;
			this.second = second;
			this.third = third;
		}

		public T getFirst() {
			return first;
		}

		public T getSecond() {
			return second;
		}

		public T getThird() {
			return third;
		}
	}

	public KMeansParameterDimension<Integer> getK() {
		return k;
	}

	public KMeansParameterDimension<Integer> getDistMeasureId() {
		return distMeasureId;
	}

	public KMeansParameterDimension<Integer> getIterations() {
		return iterations;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<KMeansParameterDimension> getDimensions() {
		List<KMeansParameterDimension> dimensions =
		    new ArrayList<KMeansParameterDimension>(3);
		dimensions.add(k);
		dimensions.add(distMeasureId);
		dimensions.add(iterations);
		return dimensions;
	}

	public void addSubSpaces(KMeansParameterDimension<Integer> k,
	    KMeansParameterDimension<Integer> distMeasureId,
	    KMeansParameterDimension<Integer> iterations) {
		ParametersSpace subSpace = new KMeansParametersSpace(k, distMeasureId, iterations);
		subSpaces.add(subSpace);
	}

	@Override
	protected String getStringRepresentation() {
		if (hasSubSpaces()) {
			return "Contains subspaces";
		} else {
			return "k: " + k + " ; DistMeasId: " + distMeasureId + " ; Iterations: " + iterations;
		}
	}

	@Override
	public KMeansConfiguration pickRandomPoint(Random rng) {
		if (hasSubSpaces()) {
			/* pick from subspaces */
			return (KMeansConfiguration) subSpaces.get(rng.nextInt(subSpaces.size()))
			    .pickRandomPoint(rng);
		} else {
			int kVal = k.pickRandomVal(rng);
			int distMeasIdVal = distMeasureId.pickRandomVal(rng);
			int iterationsVal = iterations.pickRandomVal(rng);
			return new KMeansConfiguration(kVal, distMeasIdVal, iterationsVal);
		}
	}
}
