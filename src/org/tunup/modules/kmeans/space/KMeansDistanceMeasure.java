package org.tunup.modules.kmeans.space;

import net.sf.javaml.distance.AngularDistance;
import net.sf.javaml.distance.ChebychevDistance;
import net.sf.javaml.distance.CosineDistance;
import net.sf.javaml.distance.CosineSimilarity;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.ManhattanDistance;
import net.sf.javaml.distance.MinkowskiDistance;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import net.sf.javaml.distance.SpearmanRankCorrelation;

/**
 * A distance measure used in k-means algorithm.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansDistanceMeasure {

	private static final DistanceMeasure[] DIST_MEASURES = {
	    new AngularDistance(), // 0
	    new ChebychevDistance(), // 1
	    new CosineDistance(), // 2
	    new CosineSimilarity(), // 3
	    new EuclideanDistance(), // 4
	    // new JaccardIndexDistance(), // 5
	    // new JaccardIndexSimilarity(), // 6
	    new ManhattanDistance(), // 7
	    new MinkowskiDistance(), // 8
	    new PearsonCorrelationCoefficient(), // 9
	    // new RBFKernel(), // 10
	    // new SpearmanFootruleDistance(), // 11
	    new SpearmanRankCorrelation() // 12
	};

	public static DistanceMeasure[] getDistMeasures() {
		return DIST_MEASURES;
	}
	
	public static DistanceMeasure get(int i) {
		return DIST_MEASURES[i];
	}
	
	public static int getCardinality() {
		return DIST_MEASURES.length;
	}
}
