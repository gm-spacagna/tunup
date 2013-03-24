package org.tunup.modules.kmeans.configuration;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * An executable configuration of kmeans parameters.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
@XmlRootElement(name = "configuration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KMeansConfiguration {
	int k;
	int distanceMeasureId;
	int iterations;

	@XmlTransient protected KMeansConfigResult result;
	protected boolean evaluated = false;

	
	public KMeansConfigResult getResult() {
		return result;
	}
	
	public boolean isEvaluated() {
		return (result != null);
	}

	public void setResult(KMeansConfigResult result) {
		this.result = result;
	}

	public KMeansConfiguration() {
	}

	public KMeansConfiguration(int k, int distanceMeasureId, int iterations) {
		this.k = k;
		this.distanceMeasureId = distanceMeasureId;
		this.iterations = iterations;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KMeansConfiguration) {
			KMeansConfiguration obj2 = (KMeansConfiguration) obj;
			boolean equals = k == obj2.k && distanceMeasureId == obj2.distanceMeasureId
			    && iterations == obj2.iterations;
			return equals;
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public int hashCode() {
		int digits = String.valueOf(k).length();
		int hash = (distanceMeasureId << digits) | k;
		digits += String.valueOf(distanceMeasureId).length();
		hash |= iterations << digits;
		return hash;
	}

	public int getK() {
		return k;
	}

	public int getDistanceMeasureId() {
		return distanceMeasureId;
	}

	public int getIterations() {
		return iterations;
	}

	public List<int[]> toList() {
		return Arrays.asList(toArray());
	}

	public int[] toArray() {
		return new int[] { k, distanceMeasureId, iterations };
	}

	public int getParam(int index) {
		switch (index) {
		case 0:
			return k;
		case 1:
			return distanceMeasureId;
		case 2:
			return iterations;
		default:
			throw new RuntimeException("Parameter Index not valid " + index);
		}
	}

	public void setK(int k) {
		this.k = k;
	}

	public void setDistanceMeasureId(int distanceMeasureId) {
		this.distanceMeasureId = distanceMeasureId;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	@Override
	public String toString() {
		return "K: " + k + " DistMeasureId: " + distanceMeasureId + " Iterations: " + iterations;
	}
}
