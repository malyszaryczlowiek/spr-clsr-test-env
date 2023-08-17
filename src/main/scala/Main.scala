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

    val data = Range.apply(0, 1000000).toDS()


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

/*

docker-compose up -d # --build

if [[ $do_not_build = false ]] ; then
  echo "Removing Binaries."
#  rm -rf ./target/scala-2.12/
#  ./generateJAR
  cp log4j2.xml ./dockerfiles/submitter/log4j2.xml
  cp ./target/scala-2.12/spr-clsr-test-env-0.1.0.jar ./dockerfiles/submitter/spr-clsr-test-env-0.1.0.jar
  cd ./dockerfiles/submitter

  docker build -t submitter .

  rm -rf ./log4j2.xml
  rm -rf ./spr-clsr-test-env-0.1.0.jar
  cd ../..

  docker run -d \
    --network spark-network \
    -p 8085:8080 \
    -p 7080:7077 \
    -e SPARK_MODE=worker \
    -e SPARK_MASTER_URL=spark://spark-master:7077 \
    -e SPARK_WORKER_MEMORY=1G \
    -e SPARK_WORKER_CORES=2 \
    -e SPARK_RPC_AUTHENTICATION_ENABLED=no \
    -e SPARK_RPC_ENCRYPTION_ENABLED=no \
    -e SPARK_LOCAL_STORAGE_ENCRYPTION_ENABLED=no \
    -e SPARK_SSL_ENABLED=no \
    submitter


  # rm -rf $HOME/spr-clsr-test-env/logs/application.log
fi

 */
