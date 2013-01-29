package org.tunup.modules.kmeans.space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Preconditions;

/**
 * A single parameters dimension of the algorithm to tune.
 * 
 * @param <T>
 *          The type of the value of the parameter. 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public abstract class ParameterDimension<T> {
	protected String name;
	protected List<T> vals;
	
	public ParameterDimension(String name, List<T> vals) {
	  super();
	  this.name = Preconditions.checkNotNull(name);
	  this.vals = removeDuplicates(Preconditions.checkNotNull(vals));
  }
	
	protected List<T> removeDuplicates(List<T> vals) {
	  Set<T> set = new HashSet<T>();
	  for (T val : vals) {
	  	set.add(val);
	  }
	  return new ArrayList<T>(set);
  }

	public ParameterDimension(String name) {
	  super();
	  this.name = name;
	  this.vals = new ArrayList<T>();
  }

	/**
	 * Gets the cardinality of the acceptable values.
	 * 
	 * @return The size of the values list.
	 */
	public int getCardinality() {
		return vals.size();
	}
	
	/**
	 * Divide the set of possible values into n orthogonal sub spaces with random
	 * picked values.
	 * 
	 * @param n
	 * @return
	 */
	public abstract List<? extends ParameterDimension<T>> randomDivideSpace(int n);
	
	/**
	 * Pick a random value in the set of admissible values.
	 * 
	 * @return The picked value.
	 */
	public T pickRandomVal(Random rng) {
		int pos = rng.nextInt(vals.size());
		return vals.get(pos);
	}
	
	
	public void merge(ParameterDimension<T> dimension) {
		// TODO: check if the two dimensions have the same name in that case used as identifier.
		this.vals.addAll(dimension.vals);
		this.vals = removeDuplicates(vals);
	}
}
