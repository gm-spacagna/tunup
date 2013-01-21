package org.tunup.modules.kmeans.dataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import be.abeel.io.LineIterator;

public class CustomDataLoader {

	public static Dataset load(File file, int classIndex, String separator, int n)
	    throws FileNotFoundException {

		Reader in = new InputStreamReader(new FileInputStream(file));

		LineIterator it = new LineIterator(in);
		it.setSkipBlanks(true);
		it.setSkipComments(true);
		Dataset out = new DefaultDataset();
		for (String line : it) {
			String[] arr = line.split(separator);
			int size = n;
			if (arr.length < n) {
				size = arr.length;
			}
			double[] values;
			if (classIndex == -1)
				values = new double[size];
			else
				values = new double[size - 1];
			String classValue = null;
			for (int i = 0; i < size; i++) {
				if (i == classIndex) {
					classValue = arr[i];
				} else {
					double val;
					try {
						val = Double.parseDouble(arr[i]);
					} catch (NumberFormatException e) {
						val = Double.NaN;
					}
					if (classIndex != -1 && i > classIndex)
						values[i - 1] = val;
					else
						values[i] = val;
				}
			}
			out.add(new DenseInstance(values, classValue));

		}
		return out;
	}

}
