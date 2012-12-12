package org.tunup.modules.kmeans.watchmaker;

import java.io.IOException;
import java.util.List;

import org.tunup.modules.kmeans.javaml.KMeansConfiguration;
import org.tunup.modules.kmeans.javaml.KMeansExecutor;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

/**
 * KMeans Fitness evaluator using JavaML KMeans executor.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansEvaluator implements FitnessEvaluator<KMeansConfiguration> {

	KMeansExecutor executor;
	
	public KMeansEvaluator(KMeansExecutor executor) {
	  super();
	  this.executor = executor;
  }

	@Override
  public double getFitness(KMeansConfiguration candidate, 
  		List<? extends KMeansConfiguration> population) {
		try {
	    return executor.executeAndEvaluate(candidate.getK(), 
	    		candidate.getDistanceMeasureId(), candidate.getIterations(), false);
    } catch (IOException e) {
	    throw new RuntimeException(e.getMessage());
    }
	}

	@Override
  public boolean isNatural() {
	  return false;
  }
}
