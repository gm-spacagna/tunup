package org.tunup.modules.kmeans.tuning;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.tunup.modules.kmeans.configuration.KMeansConfigResult;
import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.dataset.KMeansDatasetConfiguration;
import org.tunup.modules.kmeans.evaluation.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.evolution.KMeansConfigurationFactory;
import org.tunup.modules.kmeans.evolution.KMeansCrossover;
import org.tunup.modules.kmeans.evolution.KMeansEvaluator;
import org.tunup.modules.kmeans.evolution.KMeansMutation;
import org.tunup.modules.kmeans.evolution.monitoring.KMeansIslandEvolutionObserver;
import org.tunup.modules.kmeans.execution.KMeansExecutor;
import org.tunup.modules.kmeans.space.KMeansParametersSpace;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.interactive.InteractiveSelection;
import org.uncommons.watchmaker.framework.islands.IslandEvolution;
import org.uncommons.watchmaker.framework.islands.RingMigration;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.selection.StochasticUniversalSampling;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.Stagnation;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

/**
 * A potential tuning of KMeans using Genetic Algorithm.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public final class KMeansTuningGAWithIslands extends AbstractKMeansTuning {

	KMeansTuningGAWithIslands(KMeansDatasetConfiguration config, 
			ClusterEvaluationWithNaturalFitness ce) {
	  super(config, ce);
  }

	// GA specific parameters:
	private static final double PROB_MUTATION = 0.1;
	private static final int CROSSOVER_POINTS = 1;
	private static final double PROB_CROSSOVER = 0.9;
	private static final int INIT_POP_SIZE = 6;
	private static final int STAGNATION_LIMIT = 1;
	private static final int MAX_EVOLUTIONS = 100;
	private static final int ELITISM = 0;
	private static final int N_ISLANDS = 1;
	private static final int EPOCH_LENGTH = 1;
	private static final int MIGRANT_COUNT = 0;
	private static final double FV_TH = 9900.0;

	@Override
	public KMeansConfigResult getBestConfig() {
		// generate subspaces:

		// List<KMeansParametersSpace> spaces = space.divideSpace(N_ISLANDS);
		List<KMeansParametersSpace> spaces = space.randomDivideSpace(N_ISLANDS);
		// generate components of the engine:

		// print islands:
		// System.out.println("Islands: " + spaces);

		List<EvolutionaryOperator<KMeansConfiguration>> operators =
		    new LinkedList<EvolutionaryOperator<KMeansConfiguration>>();

		operators.add(new KMeansCrossover(CROSSOVER_POINTS, new Probability(PROB_CROSSOVER)));
		operators.add(new KMeansMutation(new ConstantGenerator<Probability>(new Probability(
		    PROB_MUTATION)), space));
		EvolutionaryOperator<KMeansConfiguration> pipeline = new EvolutionPipeline<>(operators);
		FitnessEvaluator<KMeansConfiguration> evaluator = new KMeansEvaluator(executor);
		SelectionStrategy<Object> selection = new StochasticUniversalSampling();
		Random rng = new MersenneTwisterRNG();

		// generate islands:

		List<EvolutionEngine<KMeansConfiguration>> islands =
		    new LinkedList<EvolutionEngine<KMeansConfiguration>>();
		for (KMeansParametersSpace subSpace : spaces) {
			islands.add(new GenerationalEvolutionEngine<KMeansConfiguration>(
			    new KMeansConfigurationFactory(subSpace),
			    pipeline, evaluator, selection, rng));
		}

		// generate the engine and evolve:

		// In case of uniform generated islands:
		IslandEvolution<KMeansConfiguration> engine = new
		    IslandEvolution<KMeansConfiguration>(N_ISLANDS, new RingMigration(), new
		        KMeansConfigurationFactory(space), pipeline, evaluator, selection, rng);

		// IslandEvolution<KMeansConfiguration> engine =
		// new IslandEvolution<KMeansConfiguration>(islands, new RingMigration(),
		// false, rng);
		// KMeansConfiguration bestConfig = engine.evolve(INIT_POP_SIZE, ELITISM,
		// EPOCH_LENGTH,
		// MIGRANT_COUNT,
		// new Stagnation(STAGNATION_LIMIT, false, false));

		/* use the fitness value threshold */
		engine.addEvolutionObserver(new KMeansIslandEvolutionObserver());
		KMeansConfiguration bestConfig = engine.evolve(INIT_POP_SIZE, ELITISM, EPOCH_LENGTH,
		    MIGRANT_COUNT,
		    new TargetFitness(FV_TH, false));

		KMeansConfigResult best = executor.getConfigResult(bestConfig);
		System.out.println("Best ever: " + best);
		System.out.println("Number of executions: " + executor.getCount());
		return best;
	}
}
