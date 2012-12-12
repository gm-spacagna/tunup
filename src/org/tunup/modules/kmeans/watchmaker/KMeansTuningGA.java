package org.tunup.modules.kmeans.watchmaker;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.tunup.modules.kmeans.javaml.KMeansExecutor;
import org.tunup.modules.kmeans.javaml.KMeansConfiguration;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.islands.IslandEvolution;
import org.uncommons.watchmaker.framework.islands.RingMigration;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.Stagnation;

/**
 * A potential tuning of KMeans using Genetic Algorithm.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public final class KMeansTuningGA extends AbstractKMeansTuning {

	// GA specific parameters:
	private static final double PROB_MUTATION = 0.1;
	private static final int CROSSOVER_POINTS = 1;
	private static final double PROB_CROSSOVER = 1;
	private static final int INIT_POP_SIZE = 5;
	private static final int STAGNATION_LIMIT = 10;
	private static final int MAX_EVOLUTIONS = 100;
	private static final int ELITISM = 2;
	private static final int N_ISLANDS = 5;
	private static final int EPOCH_LENGTH = 1;
	private static final int MIGRANT_COUNT = 1;

	@Override
	public KMeansConfigResult getBestConfig() {
		// set up the GA algorithm:
		CandidateFactory<KMeansConfiguration> factory = new KMeansParametersFactory(MAX_K, DIST_MEASURES,
		    MAX_ITERATIONS, MIN_ITERATIONS);
		List<EvolutionaryOperator<KMeansConfiguration>> operators =
		    new LinkedList<EvolutionaryOperator<KMeansConfiguration>>();

		operators.add(new KMeansMutation(new ConstantGenerator<Probability>(new Probability(
		    PROB_MUTATION)), MAX_K, DIST_MEASURES, MAX_ITERATIONS));
		operators.add(new KMeansCrossover(CROSSOVER_POINTS, new Probability(PROB_CROSSOVER)));
		EvolutionaryOperator<KMeansConfiguration> pipeline = new EvolutionPipeline<>(operators);
		FitnessEvaluator<KMeansConfiguration> evaluator = new KMeansEvaluator(executor);
		SelectionStrategy<Object> selection = new RouletteWheelSelection();
		Random rng = new MersenneTwisterRNG();

		// generate islands:
		
		
		IslandEvolution<KMeansConfiguration> engine = new IslandEvolution<>(N_ISLANDS,
		    new RingMigration(), factory, pipeline, evaluator, selection, rng);
		KMeansConfiguration bestConfig = engine.evolve(INIT_POP_SIZE, ELITISM, EPOCH_LENGTH, MIGRANT_COUNT,
		    new Stagnation(STAGNATION_LIMIT, false));
		// EvolutionEngine<KMeansParameters> engine = new
		// GenerationalEvolutionEngine<KMeansParameters>(
		// factory, pipeline, evaluator, selection, rng);

		// evolve for all the populations:
		// KMeansConfigResult best = null;
		// for (int i = 0; i < N_ISLANDS; i++) {
		// KMeansConfigResult bestIndividual =
		// executor.getConfigResult(engine.evolve(INIT_POP_SIZE,
		// ELITISM, new Stagnation(STAGNATION_LIMIT, false)));
		// System.out.println("Best of population " + i + ": " + bestIndividual);
		// if (best == null || bestIndividual.getFitnessValue() <
		// best.getFitnessValue()) {
		// best = bestIndividual;
		// }
		// }
		KMeansConfigResult best = executor.getConfigResult(bestConfig);
		 System.out.println("Best ever: " + best);
		 System.out.println("Number of executions: " + executor.getCount());
		return best;
	}
}
