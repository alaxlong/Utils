package util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/8/8
 * \* Description:
 * \
 */
public class SparkUtils {
    public static SparkSession getLocalSparkSession(){
        SparkSession session = SparkSession
                .builder()
                .appName("App")
                .master("local")
//                .enableHiveSupport()
                .getOrCreate();

        return session;
    }

    public static JavaSparkContext getLocalSparkContext(){
        SparkConf conf = new SparkConf().setAppName("App").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        return sc;
    }
}