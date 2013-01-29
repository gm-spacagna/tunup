package org.tunup.modules.kmeans.evolution.monitoring;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;

public class KMeansIslandEvolutionObserver implements
    IslandEvolutionObserver<KMeansConfiguration> {

	FileWriter writer;

	public KMeansIslandEvolutionObserver() {
		File file = new File("output/islands/irisEvolutionObservation" + System.currentTimeMillis());
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
			writer.write("### New epoch\n");
			writer.flush();
			printPopulationData(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void islandPopulationUpdate(int islandIndex,
	    PopulationData<? extends KMeansConfiguration> data) {
		try {
			writer.write(" - Island " + islandIndex + "\n");
			writer.flush();
			printPopulationData(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
