package org.tunup.modules.kmeans.tuning;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

import org.tunup.modules.kmeans.configuration.KMeansConfigResult;
import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.dataset.IrisConfiguration;
import org.tunup.modules.kmeans.dataset.KMeansDatasetConfiguration;
import org.tunup.modules.kmeans.dataset.RedWineConfiguration;
import org.tunup.modules.kmeans.dataset.SeedsConfiguration;
import org.tunup.modules.kmeans.evaluation.RandScore;
import org.tunup.modules.kmeans.evolution.KMeansConfigurationFactory;
import org.tunup.modules.kmeans.execution.KMeansExecutor;

public class ClustersOutput extends AbstractKMeansTuning {

	public static void main(String[] args) {
		KMeansDatasetConfiguration config;
		config = new RedWineConfiguration();
		System.out.println("Clusters output of " + config);
		ClustersOutput instance = new ClustersOutput(config);
		try {
			instance.writeClustersOutput(instance.getRandomCandidate());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("output written");
	}

	KMeansExecutor executor;
	KMeansDatasetConfiguration dataset;

	public ClustersOutput(KMeansDatasetConfiguration dataset) {
		super(dataset, null);
		this.dataset = dataset;
		this.executor = new KMeansExecutor(dataset);
	}

	public KMeansConfiguration getRandomCandidate() {
		KMeansConfigurationFactory factory = new KMeansConfigurationFactory(space);
		Random rng = new Random();
		return factory.generateRandomCandidate(rng);
	}

	public void writeClustersOutput(KMeansConfiguration config) throws IOException {
		long ts = System.currentTimeMillis();
		String filePath = "output/" + dataset.getName() + "_clusters_output_" + config.getK() + "_"
		    + config.getDistanceMeasureId() + "_" + config.getIterations() + "_" + ts;
		FileWriter fw = new FileWriter(new File(filePath));
		Dataset[] clusters = executor.execute(config);
		double rand = new RandScore(executor.getData()).score(clusters);

		String header = "Timestamp:" + ts + " Dataset: " + dataset.getName()
		    + " KMeans configuration: " + config + " Rand: " + rand + "\n";
		fw.write(header);
		int i = 0; // cluster id
		for (Dataset cluster : clusters) {
			Iterator<Instance> iterator = cluster.iterator();
			Instance instance;
			while (iterator.hasNext()) {
				instance = iterator.next();
				fw.write(instance.getID() + "," + instance.classValue() + "," + i + "\n");
			}
			i++;
		}
		fw.close();
	}

	@Override
	public KMeansConfigResult getBestConfig() {
		throw new RuntimeException("Operation not supported.");
	}
}
