import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import rx.Observable;
import rx.functions.Func1;

public class test {
    public static void main(String... args) {

    	Cluster cluster = CouchbaseCluster.create("ec2-35-160-22-211.us-west-2.compute.amazonaws.com");
    	Bucket bucket = cluster.openBucket("test");

	Observable
                .just(0)
                .flatMap(s ->
                        Observable.range(0, 1000)
                                .flatMap(new Func1<Integer, Observable<?>>() {
                                        @Override
                                        public Observable<?> call(Integer i) {
                                                return bucket.async().mutateIn(s.toString()).arrayAppend("recs", 1, false).execute()
							.map(result -> {
								System.out.println(result.status(0).toString() + "\n");
								return this;
							});
                                		}}))
                .toList().toBlocking().single();

	bucket.close();
	cluster.disconnect();
    }
}

