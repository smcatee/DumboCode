import org.apache.spark.sql.SparkSession;

SparkSession spark = SparkSession.builder().getOrCreate();
DataFrame df = spark.read()
  .format("xml")
  .option("rowTag", "PubmedArticle")
  .load("truncatedPubmed.xml");


// FOR CONVERSION BACK TO XML
// df.select("author", "_id").write()
//   .format("xml")
//   .option("rootTag", "books")
//   .option("rowTag", "book")
//   .save("newbooks.xml");