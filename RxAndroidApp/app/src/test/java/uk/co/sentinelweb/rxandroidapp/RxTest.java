package uk.co.sentinelweb.rxandroidapp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by robert on 28/04/16.
 */
public class RxTest {
    public RxTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    @Test @Ignore
    public void testMultipleSubscribersList() {
        System.out.println("------------ testMultipleSubscribersList()-------------------");
        List<String> tweets = Arrays.asList("learning RxJava", "Writing blog about RxJava", "RxJava rocks!!");
        Observable<String> observable = Observable.from(tweets);

        observable.subscribe(tweet -> System.out.println("Subscriber 1 >> " + tweet));
        observable.subscribe(tweet -> System.out.println("Subscriber 2 >> " + tweet));
        observable.subscribe(tweet -> System.out.println("Subscriber 3 >> " + tweet));
    }

    @Test @Ignore
    public void testMergedList() {
        System.out.println("------------ testMergedList()-------------------");
        List<String> tweets = Arrays.asList("learning RxJava", "Writing blog about RxJava", "RxJava rocks!!");
        Observable<String> observable = Observable.from(tweets);
        List<String> tweets2 = Arrays.asList("Tweet2", "Tweet3", "Tweet4");
        Observable<String> observable2 = Observable.from(tweets2);
        observable.
                mergeWith(observable2).
                take(4).
                subscribe(tweet -> System.out.println("Merged >> " + tweet))
        ;
    }

    @Test  @Ignore
    public void testIntStream() {
        System.out.println("------------ testIntStream()-------------------");

        Observable<Integer> obs1 = Observable.create(subscriber -> {
            Observable.range(2,7).forEach( num -> subscriber.onNext(Integer.valueOf(num)));
            subscriber.onCompleted();
        });
        obs1.subscribe(num -> System.out.println(Integer.toString(num)));
    }

    @Test
    public void testMap() {
        System.out.println("------------ testMap()-------------------");
        List<String> tweets = Arrays.asList("learning RxJava", "Writing blog about RxJava", "RxJava rocks!!");
        Observable<String> observable = Observable.from(tweets);

        observable
                .map(s -> "Tweet: "+s)
                .subscribe(System.out::println);
    }

    @Test
    public void testFilter() {
        System.out.println("------------ testMap()-------------------");
        List<String> tweets = Arrays.asList("learning RxJava", "Writing blog about RxJava", "RxJava rocks!!");
        Observable<String> observable = Observable.from(tweets);

        observable
                .filter(s -> s.toLowerCase().contains("rocks"))
                .map(s -> "Tweet Rocks: "+s)
                .subscribe(System.out::println);
    }

    @Test
    public void testMapToInt() {
        System.out.println("------------ testMapToInt()-------------------");
        List<String> tweets = Arrays.asList("learning RxJava", "Writing blog about RxJava", "RxJava rocks!!");
        Observable<String> observable = Observable.from(tweets);

        observable
                .map(s -> s.hashCode())// emits int
                .subscribe(System.out::println);
    }

    /**
     * Not working compose is to create a new observable and emit that
     */
    @Test
    public void testGroupIntStream() {
        System.out.println("------------ testGroupIntStream()-------------------");

        Observable<Integer> obs1 = Observable.create(subscriber -> {
            Observable.range(2,7).forEach(number -> subscriber.onNext(number));
            subscriber.onCompleted();
        });
        obs1
                .groupBy(n -> n % 2 == 0)
                .flatMap(g -> {
                    return g.toList();
                })
                .subscribe(System.out::println);
    }

    @Test
    public void testZip() {
        System.out.println("------------ testZip()-------------------");
        List<String> tweets = Arrays.asList("learning RxJava", "Writing blog about RxJava", "RxJava rocks!!");
        Observable<String> tweetsObservable = Observable.from(tweets);
        Observable<Integer> intsObservable = Observable.create(subscriber -> {
            Observable.range(2, tweets.size()).forEach(number -> subscriber.onNext(number));
            subscriber.onCompleted();
        });
        Observable.zip(
                tweetsObservable,
                intsObservable,
                (tweet ,i) -> i.intValue()+" : "+tweet
        )
                .subscribe(string -> System.out.println(string));
    }

    @Test
    public void testZipInterval() {
        System.out.println("------------ testZipInterval()-------------------");
        Observable<String> data = Observable.just("one", "two", "three", "four", "five");
        Observable.zip(
                data,
                Observable.interval(1, TimeUnit.SECONDS), //emits the time as an int
                (d, t) -> {return d + " " + t;}
        )
                .toBlocking()// blocks here until observables are emitted
                .forEach(System.out::println);
    }
}
