package org.tunup.modules.kmeans.evaluation;


import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.utils.LogLikelihoodFunction;

public class KMeansAicScore extends AICScore implements ClusterEvaluationWithNaturalFitness {

	@Override
  public double score(Dataset[] clusters) {
	  int k  = clusters.length;
	  int m = clusters[0].noAttributes();
	  
	  LogLikelihoodFunction likelihood = new LogLikelihoodFunction();
		// loglikelihood log(L)
		double l = likelihood.loglikelihoodsum(clusters);
		// AIC score
		double aic = -2 * l + 2 * k * m;
		return aic;
	
	}

	@Override
  public boolean isNatural() {
	  return false;
  }
}
