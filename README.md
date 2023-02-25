# sparksql-scalapb-test

Test project for SparkSQL and ScalaPB.

1. To build:

   ```
   $ sbt assembly
   ```

   Note a line like the follows that provides the path to the JAR we created:

   ```
   [info] Packaging /home/.../sparksql-scalapb-test/target/scala-2.12/sparksql-scalapb-test-assembly-1.0.0.jar ...
   ```

2. Produce some messages to kafka


   ```
   export KAFKA_SERVER=localhost:9092
   export KAFKA_TOPIC=persons-v1
   export PRODUCE=true

   /path/to/spark-3.3.0-bin-hadoop3/bin/spark-submit --jars . --class myexample.RunDemo target/scala-2.12/sparksql-scalapb-test-spark33-assembly-1.0.0.jar

   ```

3. Consume from kafka for Spark 3.3.0:

```

  export PRODUCE=false

  /path/to/spark-3.3.0-bin-hadoop3/bin/spark-submit --jars . --class myexample.RunDemo target/scala-2.12/sparksql-scalapb-test-spark33-assembly-1.0.0.jar

   ```


4. Consume from kafka for Spark 3.3.2 (works):

   ```
   /path/to/spark-3.3.2-bin-hadoop3/bin/spark-submit --jars . --class myexample.RunDemo target/scala-2.12/sparksql-scalapb-test-spark33-assembly-1.0.0.jar
   ```


