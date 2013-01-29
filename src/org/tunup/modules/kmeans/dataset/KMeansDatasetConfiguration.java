package org.tunup.modules.kmeans.dataset;

import net.sf.javaml.distance.DistanceMeasure;

import org.tunup.modules.kmeans.execution.KMeansExecutor;
import org.tunup.modules.kmeans.space.KMeansDistanceMeasure;

/**
 * A configuration for the execution of k-means.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public abstract class KMeansDatasetConfiguration {

	protected final int distMeasures = KMeansDistanceMeasure.getCardinality();

	protected final int minK;
	protected final int maxK;
	protected final int minIterations;
	protected final int maxIterations;

	protected final String name;
	protected final String filePath;
	protected final String separator;
	protected final int classIndex;

	public KMeansDatasetConfiguration(int minK, int maxK, int minIterations, int maxIterations,
	    String name, String filePath, String separator, int classIndex) {
		super();
		this.minK = minK;
		this.maxK = maxK;
		this.minIterations = minIterations;
		this.maxIterations = maxIterations;
		this.name = name;
		this.filePath = filePath;
		this.separator = separator;
		this.classIndex = classIndex;
	}

	public int getMaxK() {
		return maxK;
	}

	public int getMinIterations() {
		return minIterations;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public int getDistMeasures() {
		return distMeasures;
	}

	public String getName() {
		return name;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getSeparator() {
		return separator;
	}

	public int getClassIndex() {
		return classIndex;
	}

	public int getMinK() {
		return minK;
	}

	@Override
	public String toString() {
		String sep = " , ";
		return "KMeans Execution Configuration: name = " + name + " filePath = " + filePath
		    + " separator = " + separator + " class_index = " + classIndex + "\n" +
		    "Parameters: " + minK + sep + maxK + sep + minIterations + sep + maxIterations +
		    sep + distMeasures;
	}
	
	public boolean hasLabels() {
		return classIndex >= 0;
	}
}
