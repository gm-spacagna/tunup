package org.tunup.modules.kmeans.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.javaml.distance.DistanceMeasure;

import org.tunup.modules.kmeans.execution.KMeansExecutor;
import org.tunup.modules.kmeans.space.KMeansDistanceMeasures;
import org.tunup.modules.kmeans.space.KMeansParameterDimension;
import org.tunup.modules.kmeans.space.KMeansParametersSpace;

import com.google.common.collect.Lists;

/**
 * A configuration for the execution of k-means.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
@XmlRootElement(name = "dataset")
public abstract class KMeansDatasetConfiguration {

	protected int minK;
	protected int maxK;
	protected int minIterations;
	protected int maxIterations;

	protected String name;
	protected String filePath;
	protected String separator;
	protected int classIndex;

	protected int[] distanceMeasureIds;

	public KMeansDatasetConfiguration(int minK, int maxK, int[] distanceMeasureIds,
	    int minIterations, int maxIterations,
	    String name, String filePath, String separator, int classIndex) {
		super();
		this.minK = minK;
		this.maxK = maxK;
		this.distanceMeasureIds = distanceMeasureIds;
		this.minIterations = minIterations;
		this.maxIterations = maxIterations;
		this.name = name;
		this.filePath = filePath;
		this.separator = separator;
		this.classIndex = classIndex;
	}

	public KMeansDatasetConfiguration() {
	}

	public void setMinK(int minK) {
		this.minK = minK;
	}

	public void setMaxK(int maxK) {
		this.maxK = maxK;
	}

	public void setMinIterations(int minIterations) {
		this.minIterations = minIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setClassIndex(int classIndex) {
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

	public int[] getDistanceMeasureIds() {
		return distanceMeasureIds;
	}

	public void setDistanceMeasureIds(int[] distanceMeasureIds) {
		this.distanceMeasureIds = distanceMeasureIds;
	}

	@Override
	public String toString() {
		String sep = " , ";
		return "KMeans Execution Configuration: name = " + name + " filePath = " + filePath
		    + " separator = " + separator + " class_index = " + classIndex + "\n" +
		    "Parameters: " + minK + sep + maxK + sep + minIterations + sep + maxIterations +
		    sep + Arrays.toString(distanceMeasureIds);
	}

	public boolean hasLabels() {
		return classIndex >= 0;
	}

	public KMeansParametersSpace createSpace() {
		List<Integer> kVals = new ArrayList<Integer>(getMaxK());
		for (int i = getMinK(); i <= getMaxK(); i++) {
			kVals.add(i);
		}
		KMeansParameterDimension<Integer> k = new KMeansParameterDimension<Integer>("k", kVals);
		List<Integer> distMeasIds = new ArrayList<Integer>(distanceMeasureIds.length);
		for (int distMeasId : distanceMeasureIds) {
			distMeasIds.add(new Integer(distMeasId));
		}
		KMeansParameterDimension<Integer> distMeasureId = new KMeansParameterDimension<Integer>(
		    "DistanceMeasureId", distMeasIds);
		List<Integer> iterationsVals = new ArrayList<Integer>(getMaxIterations()
		    - getMinIterations() + 1);
		for (int i = getMinIterations(); i <= getMaxIterations(); i++) {
			iterationsVals.add(i);
		}
		KMeansParameterDimension<Integer> iterations = new KMeansParameterDimension<Integer>("Iterations",
		    iterationsVals);

		return new KMeansParametersSpace(k, distMeasureId, iterations);
	}
}
