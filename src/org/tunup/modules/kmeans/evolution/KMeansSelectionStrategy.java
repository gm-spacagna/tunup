package org.tunup.modules.kmeans.evolution;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;

public class KMeansSelectionStrategy implements SelectionStrategy<KMeansConfiguration> {

	RouletteWheelSelection roulette = new RouletteWheelSelection();

	@Override
	public <S extends KMeansConfiguration> List<S> select(List<EvaluatedCandidate<S>> population,
	    boolean naturalFitnessScores, int selectionSize, Random rng) {
		Set<S> selected = new HashSet<S>();
		selected.add(getBest(population));
		while (selected.size() < selectionSize) {
			List<S> rouletteSelected = roulette.select(population, false,
			    selectionSize - selected.size(),
			    rng);
			for (S picked : rouletteSelected) {
				selected.add(picked);
			}
		}
		return new LinkedList<S>(selected);
	}

	private <S extends KMeansConfiguration> S getBest(
	    List<EvaluatedCandidate<S>> population) {
		EvaluatedCandidate<S> best = population.get(0);
		double bestFitness = best.getFitness();
		for (EvaluatedCandidate<S> individual : population) {
			if (individual.getFitness() > bestFitness) {
				best = individual;
			}
		}
		return best.getCandidate();
	}
}
