package org.tunup.modules.kmeans.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.javaml.clustering.KMeans;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.space.KMeansParametersSpace;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.google.common.base.Preconditions;

/**
 * Mutation operator for {@link KMeans}.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansMutation implements EvolutionaryOperator<KMeansConfiguration> {

	private final NumberGenerator<Probability> mutationProbability;

	// full space of solutions
	private final KMeansParametersSpace space;

	public KMeansMutation(NumberGenerator<Probability> mutationProbability,
	    KMeansParametersSpace space) {
		super();
		this.mutationProbability = Preconditions.checkNotNull(mutationProbability);
		this.space = Preconditions.checkNotNull(space);
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
			k = space.getK().pickRandomVal(rng);
		}
		// mutate distance measure:
		int distMeasureId = config.getDistanceMeasureId();
		if (mutationProbability.nextValue().nextEvent(rng)) {
			distMeasureId = space.getDistMeasureId().pickRandomVal(rng);
		}
		// mutate n iterations:
		int iterations = config.getIterations();
		if (mutationProbability.nextValue().nextEvent(rng)) {
			iterations = space.getIterations().pickRandomVal(rng);
		}
		return new KMeansConfiguration(k, distMeasureId, iterations);
	}
}
