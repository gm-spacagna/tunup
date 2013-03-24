package org.tunup.modules.kmeans.evolution.monitoring;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;

public class KMeansEvolutionObserverConsole implements
    EvolutionObserver<KMeansConfiguration> {

	private void printPopulationData(PopulationData<? extends KMeansConfiguration> data) {
		System.out.println("");
		System.out.println("Generation n: " + data.getGenerationNumber());
		System.out.println("Time: " + data.getElapsedTime());
		System.out.println("Best candidate: " + data.getBestCandidate() + " fitness: "
		    + data.getBestCandidateFitness());
		System.out.println("FitnessStDev: " + data.getFitnessStandardDeviation());
		System.out.println("Mean fitness:" + data.getMeanFitness());
		System.out.println("Population size: " + data.getPopulationSize());
	}

	@Override
	public void populationUpdate(PopulationData<? extends KMeansConfiguration> data) {
		printPopulationData(data);
	}
}
