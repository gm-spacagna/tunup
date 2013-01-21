package org.tunup.modules.kmeans.evolution;

import java.util.Random;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.tunup.modules.kmeans.space.KMeansParametersSpace;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import com.google.common.base.Preconditions;

/**
 * Creates a new configuration of KMeans in the specified space.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansConfigurationFactory extends AbstractCandidateFactory<KMeansConfiguration>{

	private KMeansParametersSpace space;
	
	public KMeansConfigurationFactory(KMeansParametersSpace space) {
		this.space = Preconditions.checkNotNull(space);
	}

	@Override
  public KMeansConfiguration generateRandomCandidate(Random rng) {
	  return space.pickRandomPoint(rng);
	}
}
