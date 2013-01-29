package org.tunup.modules.kmeans.evaluation;

import net.sf.javaml.clustering.evaluation.ClusterEvaluation;

public interface ClusterEvaluationWithNaturalFitness extends ClusterEvaluation {
	
	/**
	 * Returns true if the cluster evaluation is natural (the higher the better), false otherwise.
	 * @return
	 */
	public boolean isNatural();

}
