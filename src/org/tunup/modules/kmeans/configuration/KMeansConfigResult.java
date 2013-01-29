package org.tunup.modules.kmeans.configuration;


/**
 * Result of a KMeans execution. It contains the parameters configuration and the fitness value
 * evaluated.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansConfigResult {
	KMeansConfiguration config;
	double fitnessValue;
	
	public KMeansConfigResult(KMeansConfiguration config, double fitnessValue) {
	  super();
	  this.config = config;
	  this.fitnessValue = fitnessValue;
  }

	public KMeansConfiguration getConfig() {
		return config;
	}

	public double getFitnessValue() {
		return fitnessValue;
	}

	@Override
  public String toString() {
		return "Config: " + config + " FitnessValue: " + fitnessValue;
	}
}
