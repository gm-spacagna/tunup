package org.tunup.modules.kmeans.watchmaker;

import java.util.Random;

import org.tunup.modules.kmeans.javaml.KMeansConfiguration;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

/**
 * Creates a new population of KMeansParameters.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansParametersFactory extends AbstractCandidateFactory<KMeansConfiguration>{

	int maxK;
	int distMeasures;
	int maxIterations;
	int minIterations;
	
	public KMeansParametersFactory(int maxK, int distMeasures, int maxIterations, 
			int minIterations) {
	  super();
	  this.maxK = maxK;
	  this.distMeasures = distMeasures;
	  this.maxIterations = maxIterations;
	  this.minIterations = minIterations;
  }

	@Override
  public KMeansConfiguration generateRandomCandidate(Random rng) {
	  int k = rng.nextInt(maxK) + 1;
	  int distMeasureId = rng.nextInt(distMeasures);
	  int iterations = rng.nextInt(maxIterations - minIterations + 1) + minIterations;
	  return new KMeansConfiguration(k, distMeasureId, iterations);
	}
}
