package org.tunup.modules.kmeans.evolution;

import java.util.ArrayList;
import java.util.Collections;
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
		List<KMeansConfiguration> result = new ArrayList<KMeansConfiguration>(selectedCandidates.size());
		for (KMeansConfiguration candidate : selectedCandidates) {
			result.add(mutate(candidate, rng));
		}
		return result;
	}

	private KMeansConfiguration mutate(KMeansConfiguration config, Random rng) {
		// mutate K:
		int k = config.getK();
		if (mutationProbability.nextValue().nextEvent(rng)) {
			// k = space.getK().pickRandomVal(rng);
			ArrayList<Integer> vals = new ArrayList<Integer>(space.getK().getVals());
			// in case it is not sorted
			Collections.sort(vals);
			int pos = Collections.binarySearch(vals, k);
			int card = vals.size();
			int[] tickets = new int[card];
			int count = 0;
			for (int i = 0; i < card; i++) {
				if (i != pos) {
					int diff = Math.abs(i - pos);
					int p = 100 * (card - 1) / diff;
					// add p tickets to the i-th element
					tickets[i] = p;
					count += p;
				}
			}
			if (count > 0) {
				int winner = rng.nextInt(count) + 1;
				int partialCount = 0;
				int candidate = -1;
				do {
					candidate++;
					partialCount += tickets[candidate];
				} while (partialCount < winner);
				k = vals.get(candidate);
			}
		}
		// mutate distance measure:
		int distMeasureId = config.getDistanceMeasureId();
		if (mutationProbability.nextValue().nextEvent(rng)) {
			int newDistMeasureId = distMeasureId;
			do {
				newDistMeasureId = space.getDistMeasureId().pickRandomVal(rng);
			} while (newDistMeasureId == distMeasureId);
			distMeasureId = newDistMeasureId;
		}
		// mutate n iterations:
		int iterations = config.getIterations();
		if (mutationProbability.nextValue().nextEvent(rng)) {
			iterations = space.getIterations().pickRandomVal(rng);
		}
		return new KMeansConfiguration(k, distMeasureId, iterations);
	}
}
