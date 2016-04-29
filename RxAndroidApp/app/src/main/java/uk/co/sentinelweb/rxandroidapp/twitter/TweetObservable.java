/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.sentinelweb.rxandroidapp.twitter;

import rx.Observable;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
// from http://blog.xebia.in/2015/09/01/day1-building-an-application-from-scratch-using-rxjava-and-java8/
public final class TweetObservable {

    public static Observable<Status> tweetObservable(final String[] searchKeywords) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("m7Kjo3xJNMUqBc40ZCHCAxahA")
          .setOAuthConsumerSecret("X4JjiDryahNI4RYdDgTwifhqTvJcdypURSDb8WVQauoD76H4M8")
          .setOAuthAccessToken("238308667-hZprvWUEWcmJI98ZciJbzDuD5TE2gkjQ5xsa8UAJ")
          .setOAuthAccessTokenSecret("hvPTKPxG5AQFsBUJkk5VqFmOP3yRXxw4L6j2snuZlzPMc");
        
        return Observable.create(subscriber -> {
            final TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            twitterStream.addListener(new StatusAdapter() {
                public void onStatus(Status status) {
                    //System.out.println("status:"+status);
                    subscriber.onNext(status);
                }

                public void onException(Exception ex) {
                    subscriber.onError(ex);
                }
            });
            FilterQuery query = new FilterQuery();
            query.language(new String[]{"en"});
            query.track(searchKeywords);
            twitterStream.filter(query);
        });


    }
}
