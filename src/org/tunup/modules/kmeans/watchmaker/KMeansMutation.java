package org.tunup.modules.kmeans.watchmaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.javaml.clustering.KMeans;

import org.tunup.modules.kmeans.javaml.KMeansConfiguration;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

/**
 * Mutation operator for {@link KMeans}.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansMutation implements EvolutionaryOperator<KMeansConfiguration> {

	private final NumberGenerator<Probability> mutationProbability;
	private final int maxK;
	private final int nMeasures;
	private final int maxIterations;

	/**
	 * Creates a mutation operator for KMeans.
	 * @param mutationProbability Probability of mutate each parameter.
	 * @param maxK Max value of k.
	 * @param nMeasures Number of distance measures available.
	 * @param maxIterations Max number of iterations.
	 */
	public KMeansMutation(NumberGenerator<Probability> mutationProbability, int maxK, int nMeasures,
	    int maxIterations) {
		super();
		this.mutationProbability = mutationProbability;
		this.maxK = maxK;
		this.nMeasures = nMeasures;
		this.maxIterations = maxIterations;
	}

	@Override
	public List<KMeansConfiguration> apply(List<KMeansConfiguration> selectedCandidates, Random rng) {
		List<KMeansConfiguration> result = new ArrayList<>(selectedCandidates.size());
		for (KMeansConfiguration candidate : selectedCandidates) {
			result.add(mutate(candidate, rng));
		}
		return result;
	}

	private KMeansConfiguration mutate(KMeansConfiguration config, Random rng) {
		// mutate K:
		int k = config.getK();
		if (mutationProbability.nextValue().nextEvent(rng)) {
			k = rng.nextInt(maxK) + 1;
		}
		// mutate distance measure:
		int distMeasureId = config.getDistanceMeasureId();
		if (mutationProbability.nextValue().nextEvent(rng)) {
			distMeasureId = rng.nextInt(nMeasures);
		}
		// mutate n iterations:
		int iterations = config.getIterations();
//		if (mutationProbability.nextValue().nextEvent(rng)) {
//			iterations = rng.nextInt(maxIterations) + 1;
//		}
		return new KMeansConfiguration(k, distMeasureId, iterations);
	}
}
