package org.tunup.modules.kmeans.space;

import java.util.List;
import java.util.Random;


/**
 * Space of parameters of the system to tune.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public abstract class ParametersSpace {

	protected List<ParametersSpace> subSpaces;
	
	public abstract List<? extends ParametersSpace> divideSpace(int n);
	
	public abstract List<? extends ParameterDimension> getDimensions();
	
	public boolean hasSubSpaces() {
		return !subSpaces.isEmpty();
	}

	public List<? extends ParametersSpace> getSubSpaces() {
		return subSpaces;
	}
	
	public abstract Object pickRandomPoint(Random rng);
	
	@Override
	public String toString() {
		if (hasSubSpaces()) {
			String str = "";
			int i = 0;
			for (ParametersSpace subSpace : subSpaces) {
				str += "SubSpace" + i++ + " : " + subSpace.toString() + "\n";
			}
			return str;
		} else {
			return getStringRepresentation();
		}
	}
	
	protected abstract String getStringRepresentation();
}
