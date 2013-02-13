package org.tunup.modules.kmeans.evolution.monitoring;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;

public class KMeansEvolutionObserverFileWriter implements
    EvolutionObserver<KMeansConfiguration> {

	FileWriter writer;

	public KMeansEvolutionObserverFileWriter(String name) {
		File file = new File("output/monitoring/simpleGA/" + name + "EvolutionObserver"
		    + System.currentTimeMillis());
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printPopulationData(PopulationData<? extends KMeansConfiguration> data)
	    throws IOException {
		writer.write("Generation n: " + data.getGenerationNumber() + "\n");
		writer.write("Best candidate: " + data.getBestCandidate() + " fitness: "
		    + data.getBestCandidateFitness() + "\n");
		writer.write("FitnessStDev: " + data.getFitnessStandardDeviation() + "\n");
		writer.write("Mean fitness:" + data.getMeanFitness() + "\n");
		writer.write("Population size: " + data.getPopulationSize() + "\n");

		writer.flush();
	}

	@Override
	public void populationUpdate(PopulationData<? extends KMeansConfiguration> data) {
		try {
			printPopulationData(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
