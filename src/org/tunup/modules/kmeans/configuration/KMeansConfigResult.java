package org.tunup.modules.kmeans.configuration;

import java.util.Arrays;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.javaml.clustering.evaluation.ClusterEvaluation;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.tunup.modules.kmeans.dataset.KMeansDatasetConfiguration;

/**
 * Result of a KMeans execution. It contains the parameters configuration and
 * the fitness value evaluated.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
@XmlRootElement(name = "result")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KMeansConfigResult {
	@XmlTransient
	KMeansDatasetConfiguration dataset;
	String datasetName;
	KMeansConfiguration config;
	double[][] fitnessValues;
	int n; 
	
	public KMeansDatasetConfiguration getDataset() {
		return dataset;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	public void setConfig(KMeansConfiguration config) {
		this.config = config;
	}

	public void setFitnessValues(double[][] fitnessValues) {
		this.fitnessValues = fitnessValues;
	}

	public KMeansConfigResult() {
	}

	public KMeansConfigResult(KMeansDatasetConfiguration dataset, KMeansConfiguration config,
	    int n, ClusterEvaluation[] ce, double[][] fitnessValues) {
		super();
		this.dataset = dataset;
		this.datasetName = dataset.getName();
		this.config = config;
		this.n = n;
		this.fitnessValues = fitnessValues;
		assert fitnessValues.length == ce.length;
		assert fitnessValues[0].length == n;
	}
	public KMeansConfiguration getConfig() {
		return config;
	}

	public double[][] getFitnessValues() {
		return fitnessValues;
	}

	@XmlTransient
	public double[] getAverage() {
		double[] avg = new double[fitnessValues.length];
		if (n == 0) {
			return new double[0];
		}
		for (int i = 0; i < fitnessValues.length; i++) {
			for (int j = 0; j < n; j++) {
				double val = fitnessValues[i][j];
				avg[i] += val;
			}
			avg[i] /= n;
		}
		return avg;
	}

	@XmlTransient
	public double[] getStandardDeviation() {
		if (n < 2) {
			return new double[0];
		}

		double[] stdev = new double[fitnessValues.length];
		double[] avg2 = getAverage();
		for (int i = 0; i < avg2.length; i++) {
			avg2[i] = avg2[i] * avg2[i];
		}
		for (int i = 0; i < fitnessValues.length; i++) {
			for (int j = 0; j < n; j++) {
				double val = fitnessValues[i][j];
				stdev[i] += Math.sqrt(Math.abs(val * val - avg2[i]));
			}
			stdev[i] /= n - 1;
		}
		return stdev;
	}

	@Override
	public String toString() {
		String str = "Config: " + config + " FitnessValues: \n" + Arrays.deepToString(fitnessValues);
		return str;
	}
}
