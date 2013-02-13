package org.tunup.modules.kmeans.evolution.monitoring;

import org.tunup.modules.kmeans.configuration.KMeansConfiguration;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;

/** 
 * Observer of a simple k-means genetic evolution.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansEvolutionObserver implements
EvolutionObserver<KMeansConfiguration> {

	protected KMeansConfiguration best;
	
	protected final boolean isNatural;
	
	public KMeansEvolutionObserver(boolean isNatural) {
	  super();
	  this.isNatural = isNatural;
  }


	@Override
  public void populationUpdate(PopulationData<? extends KMeansConfiguration> data) {
		
  }
}
