package io.github.malyszaryczlowiek

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import org.apache.spark.sql.SparkSession

import org.apache.spark._


class Main
object Main {

  private val logger: Logger = LogManager.getLogger(classOf[Main])

  def main(args: Array[String]): Unit = {

    logger.trace(s"Starting app")

    val sparkSession = SparkSession
      .builder
      .appName("spark-cluster-test-environment")
      // two config below added to solve
      // Initial job has not accepted any resources;
      // check your cluster UI to ensure that workers
      // are registered and have sufficient resources
      //      .config("spark.shuffle.service.enabled", "false")
      //      .config("spark.dynamicAllocation.enabled", "false")
      // .master("local[2]")
      .master("spark://spark-master:7077")    // option for cluster  spark://spark-master:7077
      .getOrCreate()

    val sc = sparkSession.sparkContext

    import sparkSession.implicits._

    val data = Range.apply(0, 2000000).toDS()


    val filtered = data.filter(_ % 100000 == 0)


    filtered.write
      .format("console")
      .save()

    /*
    todo sprawdzić error
    21:41:57.598 [driver-heartbeater] ERROR org.apache.spark.util.Utils - Uncaught exception in thread driver-heartbeater
    java.lang.OutOfMemoryError: GC overhead limit exceeded

    oraz

    21:45:12.425 [dispatcher-CoarseGrainedScheduler] WARN  org.apache.spark.scheduler.TaskSetManager - Stage 1
    contains a task of very large size (16117 KiB). The maximum recommended task size is 1000 KiB.

     */

  }

}
