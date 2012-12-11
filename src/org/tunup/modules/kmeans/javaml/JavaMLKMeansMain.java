package org.tunup.modules.kmeans.javaml;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.CIndex;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.GPlus;
import net.sf.javaml.clustering.evaluation.Gamma;
import net.sf.javaml.clustering.evaluation.HybridCentroidSimilarity;
import net.sf.javaml.clustering.evaluation.HybridPairwiseSimilarities;
import net.sf.javaml.clustering.evaluation.MinMaxCut;
import net.sf.javaml.clustering.evaluation.PointBiserial;
import net.sf.javaml.clustering.evaluation.SumOfAveragePairwiseSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfCentroidSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.clustering.evaluation.Tau;
import net.sf.javaml.clustering.evaluation.TraceScatterMatrix;
import net.sf.javaml.clustering.evaluation.WB;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.tools.data.FileHandler;

public class JavaMLKMeansMain {

	private static final String DATA_PATH = "data/internet_ads/ad.data";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(System.currentTimeMillis() + ": Start KMeans iterations on "
		    + DATA_PATH);
		/* Load a dataset */
		 Dataset irisData = FileHandler.loadDataset(new File("data/iris/iris.data"),
		 4, ",");
		//Dataset data = CustomDataLoader.load(new File(DATA_PATH), -1, ",", 3);
		System.out.println("Iris Data: " + irisData);
		System.out.println("###");
		System.out.println("Data: " + irisData);
		
		for (int j = 1; j <= 30; j++) {
			/*
			 * Create a new instance of the KMeans algorithm, with no options
			 * specified. By default this will generate 4 clusters.
			 */
			Clusterer km = new KMeans(j);
			/*
			 * Cluster the data, it will be returned as an array of data sets, with
			 * each dataset representing a cluster.
			 */
			Dataset[] clusters = km.cluster(irisData);

			/* Create a measure for the cluster quality */
			ClusterEvaluation sse = new SumOfSquaredErrors();
			ClusterEvaluation scs = new SumOfCentroidSimilarities();
			ClusterEvaluation aic = new AICScore();
			ClusterEvaluation bic = new BICScore();
			ClusterEvaluation gamma = new Gamma(new EuclideanDistance());

			DistanceMeasure ed = new EuclideanDistance();

			/* Measure the quality of the clustering */
			double sseScore = sse.score(clusters);
			double scsScore = scs.score(clusters);
			double aicScore = aic.score(clusters);
			double bicScore = bic.score(clusters);
			double gammaScore = gamma.score(clusters);
			double gammaPlusScore = new GPlus(ed).score(clusters);
			double hcsScore = new HybridCentroidSimilarity().score(clusters);
			double hpsScore = new HybridPairwiseSimilarities().score(clusters);
			double sapaScore = new SumOfAveragePairwiseSimilarities().score(clusters);
			double cScore = new CIndex(ed).score(clusters);
			double mmcScore = new MinMaxCut(ed).score(clusters);
			double pbScore = new PointBiserial(ed).score(clusters);
			double tauScore = new Tau(ed).score(clusters);
			double tsmScore = new TraceScatterMatrix().score(clusters);
			double wbScore = new WB(ed).score(clusters);

			// int i = 0;
			// for (Dataset cluster : clusters) {
			// // System.out.println("Cluster " + i++);
			// // System.out.println(cluster);
			// }

			String metrics = "k: " + j + "\t\t\tSSE: " + sseScore + "\t\t\tSCS: " + scsScore
			    + "\t\t\taic: " + aicScore + "\t\t\tbic: " + bicScore + "\t\t\tgamma: " + gammaScore
			    + "\t\t\tg+: " + gammaPlusScore + "\t\t\thcs: " + hcsScore + "\t\t\thps: "
			    + hpsScore + "\t\t\tsapa: " + sapaScore + "\t\t\tc: " + cScore + "\t\t\tmmc: "
			    + mmcScore + "\t\t\tpb: " + pbScore + "\t\t\ttau: " + tauScore + "\t\t\ttsm: "
			    + tsmScore + "\t\t\twb: " + wbScore;
			
			if (j == 3) {
				printSeparator(metrics.length());
				System.out.println(metrics);
				printSeparator(metrics.length());
			} else {
				System.out.println(metrics);
			}
		}
	}
	
	private static void printSeparator(int n) {
		for (int i = 0; i < n; i++) {
			System.out.print("-");
		}
		System.out.println("");
	}
}
