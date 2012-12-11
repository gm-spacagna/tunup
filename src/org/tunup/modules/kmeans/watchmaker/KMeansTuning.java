package org.tunup.modules.kmeans.watchmaker;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.tunup.modules.kmeans.javaml.KMeansExecutor;
import org.tunup.modules.kmeans.javaml.KMeansParameters;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.Stagnation;

/**
 * A potential tuning of KMeans using Genetic Algorithm.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public final class KMeansTuning {

	private static final int MAX_K = 10;
	private static final int MAX_ITERATIONS = 100;
	private static final int MIN_ITERATIONS = 10;
	private static final double PROB_MUTATION = 0.1;
	private static final int CROSSOVER_POINTS = 3;
	private static final double PROB_CROSSOVER = 0.9;
	private static final String NAME = "Iris";
	private static final String FILE_PATH = "data/iris/iris.data";
	private static final int INIT_POP_SIZE = 5;
	private static final int STAGNATION_LIMIT = 10;

	private static final int DIST_MEASURES = KMeansExecutor.getDistMeasures();

	public static void main(String[] args) {
		System.out.println("Hello");
		System.out.println("Best Candidate is: " + new KMeansTuning().evolve());
	}

	public KMeansParameters evolve() {
		CandidateFactory<KMeansParameters> factory = new KMeansParametersFactory(MAX_K, DIST_MEASURES,
		    MAX_ITERATIONS, MIN_ITERATIONS);
		List<EvolutionaryOperator<KMeansParameters>> operators =
		    new LinkedList<EvolutionaryOperator<KMeansParameters>>();

		operators.add(new KMeansMutation(new ConstantGenerator<Probability>(new Probability(
		    PROB_MUTATION)), MAX_K, DIST_MEASURES, MAX_ITERATIONS));
		operators.add(new KMeansCrossover(CROSSOVER_POINTS, new Probability(PROB_CROSSOVER)));
		EvolutionaryOperator<KMeansParameters> pipeline = new EvolutionPipeline<>(operators);
		KMeansExecutor executor = KMeansExecutor.createInstance(NAME, FILE_PATH);
		FitnessEvaluator<KMeansParameters> evaluator = new KMeansEvaluator(executor);
		SelectionStrategy<Object> selection = new RouletteWheelSelection();
		Random rng = new MersenneTwisterRNG();
		EvolutionEngine<KMeansParameters> engine = new GenerationalEvolutionEngine<KMeansParameters>(
		    factory, pipeline, evaluator, selection, rng);
		return engine.evolve(INIT_POP_SIZE, 0, new Stagnation(STAGNATION_LIMIT, false));
	}
}
