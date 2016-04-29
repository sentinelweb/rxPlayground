/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.sentinelweb.rxandroidapp.twitter;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 *
 * @author robert
 */
public class SentimentAnalyzerGrouped {
    public static void main(String[] args) throws Exception {
        String[] searchTerms = {"Android"};

        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("m7Kjo3xJNMUqBc40ZCHCAxahA", "X4JjiDryahNI4RYdDgTwifhqTvJcdypURSDb8WVQauoD76H4M8");

        ConnectableObservable<Status> observable = TweetObservable.tweetObservable(searchTerms)
                .publish();
        observable.connect();
        Observable<String> tweetStream = observable.map(Status::getText);
        boolean showStream = true;
        if (showStream) {
            tweetStream.groupBy(tweet -> sentiment(tweet)).subscribe(
                    (GroupedObservable<Sentiment, String> gr) ->
                            gr.asObservable()
                                    .forEach(tweet -> System.out.println(String.format("%s >> %s", gr.getKey(), tweet)))
            );
        } else {
            tweetStream.groupBy(tweet -> sentiment(tweet)).subscribe(
                (GroupedObservable<Sentiment, String> gr) ->
                        gr.asObservable()
                                .window(1, TimeUnit.MINUTES)
                                .subscribe(
                                        val -> val.count().subscribe(
                                                count -> System.out.println(String.format("%s %d tweets/minute", gr.getKey(), count))
                                        )
                                 )
            );
        }
    }

    private enum Sentiment {
        POSITIVE,
        NEGATIVE,
        NEUTRAL
    }
    
    private static Sentiment sentiment(String tweet) {
        if (tweet.contains("like")) {
            return Sentiment.POSITIVE;
        } else if (tweet.contains("hate")) {
            return Sentiment.NEGATIVE;
        } else {
            return Sentiment.NEUTRAL;
        }
    }

    
    
    
}
