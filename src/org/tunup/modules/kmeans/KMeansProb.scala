package org.tunup.modules.kmeans

import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.population.Chromosome
import net.kogics.jiva.Predef._
import net.kogics.jiva.gaprob.ProbBuilder
import jeshua.clustering.KMeansDemo._
import jeshua.clustering.KMeansPlusPlus
import jeshua.clustering.Utils._
import scalala.tensor.dense._
import scalala.tensor.sparse.SparseVector
import scalala.tensor._
import com.stromberglabs.cluster.KMeansClusterer
import org.tunup.modules.kmeans.javaml.KMeansExecutor
import org.tunup.modules.kmeans.javaml.KMeansWithDistanceExecutor

class KMeansProb extends jiva.ProbDefiner {
  val gaProblem = ProbBuilder.buildIntegerProb { buildr =>
    buildr name "KMeans"
    buildr popSize 1
    buildr chrSize (1, 20)
    buildr numEvolutions 100
    buildr fitnessFunction new KMeansFf
    buildr defaultEvolver (0.1, 0.9)
  }

  class ProbFf3 extends FitnessFunction[jint] {
    def evaluate(chr: Chromosome[jint]): Double = {
      chr(0).allele.floatValue
    }
  }

  class KMeansFf extends FitnessFunction[jint] {
    def evaluate(chr: Chromosome[jint]): Double = {
      val k: Int = chr(0).allele.intValue()
      println("Evaluate KMeans with k = " + k)
      val data = getSomeData()
      val initialCentroids =
        for (i <- 0 until k)
          yield data(rng.nextInt(data.length))
      val kmeans = new KMeansPlusPlus(k, data, initialCentroids, 1000)
      //kmeans.train()
      k
      //        val centroids = kmeans.getCentroids
      //    def loop(centroidsLeft: IndexedSeq[DenseVector[Double]], sumAccumulator: Int): Int = {
      //          if (centroidsLeft.isEmpty) sumAccumulator
      //          else loop(centroidsLeft.tail,
      //              sumAccumulator + sumOfDistances(centroidsLeft.head, centroids))
      //      }
      //    val sum = loop(centroids, 0)
      //    sum
    }
  }
}

class KMeansProb2 extends jiva.ProbDefiner {
  val gaProblem = ProbBuilder.buildIntegerProb { buildr =>
    buildr name "KMeans"
    buildr popSize 1
    buildr chrSize (1, 20)
    buildr numEvolutions 10
    buildr fitnessFunction new KMeansFf
    buildr defaultEvolver (0.1, 0.9)
  }

  class ProbFf3 extends FitnessFunction[jint] {
    def evaluate(chr: Chromosome[jint]): Double = {
      chr(0).allele.floatValue
    }
  }

  class KMeansFf extends FitnessFunction[jint] {
    def evaluate(chr: Chromosome[jint]): Double = {
      val k: Int = chr(0).allele.intValue()
      println("Evaluate KMeans with k = " + k)
      val data = getSomeData()
      val initialCentroids =
        for (i <- 0 until k)
          yield data(rng.nextInt(data.length))
      val kmeans = new KMeansClusterer()
      k
      //        val centroids = kmeans.getCentroids
      //    def loop(centroidsLeft: IndexedSeq[DenseVector[Double]], sumAccumulator: Int): Int = {
      //          if (centroidsLeft.isEmpty) sumAccumulator
      //          else loop(centroidsLeft.tail,
      //              sumAccumulator + sumOfDistances(centroidsLeft.head, centroids))
      //      }
      //    val sum = loop(centroids, 0)
      //    sum
    }
  }
}

class Prob4 extends jiva.ProbDefiner {
  val gaProblem = ProbBuilder.buildIntegerProb { buildr =>
    buildr name "KMeansProb"
    buildr popSize 10
    buildr chrSize (1, 100)
    buildr numEvolutions 100
    buildr fitnessFunction new ProbFf
    buildr defaultEvolver (0.1, 0.9)
  }

  class ProbFf extends FitnessFunction[jint] {
    def evaluate(chr: Chromosome[jint]): Double = {
      chr(0).allele.floatValue
    }
  }
}

class ProbJavaMLKMeans1 extends jiva.ProbDefiner {
  val gaProblem = ProbBuilder.buildIntegerProb { buildr =>
    buildr name "ProbJavaMLKmeans1"
    buildr popSize 5
    buildr chrSize (1, 10)
    buildr numEvolutions 100
    buildr fitnessFunction new ProbJavaMLKMeansFf
    buildr defaultEvolver (0.1, 0.9)
  }

  class ProbJavaMLKMeansFf extends FitnessFunction[jint] {
    def evaluate(chr: Chromosome[jint]): Double = {
      val k: Int = chr(0).allele.intValue
      val executor: KMeansExecutor = new KMeansExecutor()
      executor.execute(k)
      val fitnessVal = executor.evaluate
      println("Try with k: " + k + " -> scs = " +
        executor.getScsScore + " sse = " +
        executor.getSseScore + " fitnessVal = " + fitnessVal)
      fitnessVal
    }
  }
}

class ProbJavaMLKMeansWithDist extends jiva.ProbDefiner {
  val gaProblem = ProbBuilder.buildIntegerProb { buildr =>
    buildr name "ProbJavaMLKmeansWithDist"
    buildr popSize 5
    buildr chrSize (2, 10)
    buildr numEvolutions 100
    buildr fitnessFunction new ProbJavaMLKMeansWithDistFf
    buildr defaultEvolver (0.1, 0.9)
  }
  
  val executor: KMeansWithDistanceExecutor = new KMeansWithDistanceExecutor()

  class ProbJavaMLKMeansWithDistFf extends FitnessFunction[jint] {
    def evaluate(chr: Chromosome[jint]): Double = {
      val k: Int = chr(0).allele.intValue
      val distMeasureId: Int = chr(1).allele.intValue
      val fitnessVal = executor.executeAndEvaluate(k, distMeasureId)
      println("k: " + k + "\t\tdistMeasureId: " + distMeasureId + " -> " + 
        " scs = " +  executor.getScsScore +
        " sse = " + executor.getSseScore + 
        " fitnessVal = " + fitnessVal)
      fitnessVal
    }
  }
}



  
