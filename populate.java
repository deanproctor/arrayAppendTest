import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.document.json.JsonArray;
import rx.Observable;
import rx.functions.Func1;

public class populate {
    public static void main(String... args) {

	Cluster cluster = CouchbaseCluster.create("ec2-35-160-22-211.us-west-2.compute.amazonaws.com");
    	Bucket bucket = cluster.openBucket("test");

        Observable
                .range(0,2)
                .flatMap(new Func1<Integer, Observable<?>>() {
            				@Override
            				public Observable<?> call(Integer i) {
                				return bucket.async().upsert(JsonDocument.create(i.toString(), JsonObject.empty().put("recs", JsonArray.create())));
            				}
        			})
		.toList().toBlocking().single();

	cluster.disconnect();
    }
}
