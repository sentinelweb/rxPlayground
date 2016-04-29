/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.sentinelweb.rxandroidapp.twitter;

/**
 * @author robert
 */
import rx.Observable;
import rx.observables.ConnectableObservable;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class SentimentAnalyzer {

    public static void main(String[] args) throws Exception {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("m7Kjo3xJNMUqBc40ZCHCAxahA")
          .setOAuthConsumerSecret("X4JjiDryahNI4RYdDgTwifhqTvJcdypURSDb8WVQauoD76H4M8")
          .setOAuthAccessToken("238308667-hZprvWUEWcmJI98ZciJbzDuD5TE2gkjQ5xsa8UAJ")
          .setOAuthAccessTokenSecret("hvPTKPxG5AQFsBUJkk5VqFmOP3yRXxw4L6j2snuZlzPMc");
        
        String[] searchTerms = {"test"};
        ConnectableObservable<Status> observable = TweetObservable.tweetObservable(searchTerms).publish();
        observable.connect();
        Observable<String> tweetStream = observable.map(Status::getText);
        tweetStream
                //.toBlocking()
                .forEach(System.out::println);
        System.out.println("done");
    }
}
