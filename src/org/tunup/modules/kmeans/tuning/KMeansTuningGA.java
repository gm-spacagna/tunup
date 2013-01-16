package org.tunup.modules.kmeans.tuning;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.tunup.modules.kmeans.javaml.ClusterEvaluationWithNaturalFitness;
import org.tunup.modules.kmeans.javaml.KMeansExecutor;
import org.tunup.modules.kmeans.javaml.KMeansConfiguration;
import org.tunup.modules.kmeans.space.KMeansParameterDimension;
import org.tunup.modules.kmeans.tuning.configurations.KMeansExecutionConfiguration;
import org.tunup.modules.kmeans.watchmaker.KMeansConfigResult;
import org.tunup.modules.kmeans.watchmaker.KMeansConfigurationFactory;
import org.tunup.modules.kmeans.watchmaker.KMeansCrossover;
import org.tunup.modules.kmeans.watchmaker.KMeansEvaluator;
import org.tunup.modules.kmeans.watchmaker.KMeansEvolutionObserver;
import org.tunup.modules.kmeans.watchmaker.KMeansMutation;
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
import org.uncommons.watchmaker.framework.termination.TargetFitness;

/**
 * A potential tuning of KMeans using Genetic Algorithm.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public final class KMeansTuningGA extends AbstractKMeansTuning {

	KMeansTuningGA(KMeansExecutionConfiguration config, ClusterEvaluationWithNaturalFitness ce) {
		super(config, ce);
	}

	// GA specific parameters:
	private static final double PROB_MUTATION = 0.3;
	private static final int CROSSOVER_POINTS = 1;
	private static final double PROB_CROSSOVER = 0.9;
	private static final int INIT_POP_SIZE = 6;
	private static final int STAGNATION_LIMIT = 3;
	private static final int MAX_EVOLUTIONS = 100;
	private static final int ELITISM = 1;
	private static final double FV_TH = 9900.0;
	private static final int N = 20;

	@Override
	public KMeansConfigResult getBestConfig() {

		// set up the GA algorithm:
		CandidateFactory<KMeansConfiguration> factory = new KMeansConfigurationFactory(space);
		List<EvolutionaryOperator<KMeansConfiguration>> operators =
		    new LinkedList<EvolutionaryOperator<KMeansConfiguration>>();

		operators.add(new KMeansMutation(new ConstantGenerator<Probability>(new Probability(
		    PROB_MUTATION)), space));
		operators.add(new KMeansCrossover(CROSSOVER_POINTS, new Probability(PROB_CROSSOVER)));
		EvolutionaryOperator<KMeansConfiguration> pipeline = new EvolutionPipeline<>(operators);
		FitnessEvaluator<KMeansConfiguration> evaluator = new KMeansEvaluator(executor, N);
		SelectionStrategy<Object> selection = new RouletteWheelSelection();
		Random rng = new MersenneTwisterRNG();

		// generate islands:

		EvolutionEngine<KMeansConfiguration> engine =
		    new GenerationalEvolutionEngine<KMeansConfiguration>(factory, pipeline, evaluator,
		        selection, rng);
		engine.addEvolutionObserver(new KMeansEvolutionObserver());
		KMeansConfiguration bestConfig = engine.evolve(INIT_POP_SIZE, ELITISM,
		    new Stagnation(STAGNATION_LIMIT, executor.getNaturalFitness()));
		// KMeansConfiguration bestConfig = engine.evolve(INIT_POP_SIZE, ELITISM,
		// new TargetFitness(FV_TH, false));

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
