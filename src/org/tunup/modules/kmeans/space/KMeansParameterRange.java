package org.tunup.modules.kmeans.space;

/**
 * A range of values that a single parameter in kmeans can assume.
 * See {@code KMeansParameter}.
 * 
 * @param <T>
 *          The type of the value of the parameter.
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class KMeansParameterRange<T> {
	private T left;
	private T right;
	
	public KMeansParameterRange(T left, T right) {
	  super();
	  this.left = left;
	  this.right = right;
  }

	public T getLeft() {
		return left;
	}

	public T getRight() {
		return right;
	}
}
