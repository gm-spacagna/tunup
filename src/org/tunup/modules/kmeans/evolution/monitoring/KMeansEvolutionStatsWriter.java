package org.tunup.modules.kmeans.evolution.monitoring;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;

/**
 * An observer of k-means evolution that writes in an output file the stats at
 * each evolution in csv format.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansEvolutionStatsWriter implements
    EvolutionObserver<KMeansConfiguration> {

	FileWriter writer;
	String sep = ",";

	public KMeansEvolutionStatsWriter(String name) {
		File file = new File("output/monitoring/simpleGA/" + name + "_evolution_stats_"
		    + System.currentTimeMillis() + ".csv");
		try {
			writer = new FileWriter(file);
			writer
			    .write("GEN COUNT, BEST K, BEST DIST MEAS ID, BEST ITER, BEST FITNESS, FITNESS STD DEV, "
			        + "FITNESS MEAN, POP SIZE\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printPopulationData(PopulationData<? extends KMeansConfiguration> data)
	    throws IOException {
		writer.write(data.getGenerationNumber() + sep
		    + data.getBestCandidate().getK() + sep
		    + data.getBestCandidate().getDistanceMeasureId() + sep
		    + data.getBestCandidate().getIterations() + sep
		    + data.getBestCandidateFitness() + sep
		    + data.getFitnessStandardDeviation() + sep
		    + data.getMeanFitness() + sep
		    + data.getPopulationSize() + "\n");
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
