FROM bitnami/spark:3.3.0
COPY ./spr-clsr-test-env-0.1.0.jar .
COPY ./log4j2.xml $SPARK_HOME/conf
# todo delete
# RUN ./sbin/start-worker.sh spark://spark-master:7077
CMD ["./bin/spark-submit", "--master", "spark://spark-master:7077", "--packages", "org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.0,org.apache.spark:spark-sql_2.12:3.3.0,org.slf4j:slf4j-nop:2.0.5", "--class", "io.github.malyszaryczlowiek.Main", "/opt/bitnami/spark/spr-clsr-test-env-0.1.0.jar", "1000"]
