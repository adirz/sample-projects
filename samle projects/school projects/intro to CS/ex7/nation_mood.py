from data import load_tweets
from geo_tweet_tools import group_tweets_by_state
import tweet

def most_talkative_state(tweets,find_state):
    """Return the state that has the largest number of tweets containing term.
    >>> state_centers = {n: find_center(s) for n, s in us_states.items()}
    >>> tweets = load_tweets('texas')
    >>> find_state = find_closest_state(state_centers);
    >>> most_talkative_state(tweets,find_state)
    'TX'
    >>> tweets = load_tweets('sandwich')
    >>> most_talkative_state(tweets,find_state)
    'NJ'
    """
    tweets_by_state = group_tweets_by_state(tweets,find_state)
    most_talkative = None
    talks = 0
    for state,tweet in tweets_by_state.items():
        cur_talks = len(tweet)
        if talks < cur_talks:
            talks = cur_talks
            most_talkative = state
    return most_talkative


def average_sentiments(tweets_by_state,word_sentiments):
    """Calculate the average sentiment of the states by averaging over all
    the tweets from each state. Return the result as a dictionary from state
    names to average sentiment values (numbers).

    If a state has no tweets with sentiment values, leave it out of the
    dictionary entirely.  Do NOT include states with no tweets, or with tweets
    that have no sentiment, as 0.  0 represents neutral sentiment, not unknown
    sentiment.

    tweets_by_state -- A dictionary from state names to lists of tweets
    """
    averages = {}
    for state, tweets in tweets_by_state.items():
        sentis = []
        for tweet in tweets:
            senti = tweet.get_sentiment(word_sentiments)
            if senti != None:
                sentis.append(senti)
        if len(sentis) > 0:
            averages[state] = sum(sentis)/len(sentis)
    return averages
        

def group_tweets_by_hour(tweets):
    """Return a list of lists of tweets that are gouped by the hour 
    they were posted.

    The indexes of the returned list represent the hour when they were posted
    - the integers 0 through 23.

    tweets_by_hour[i] is the list of all
    tweets that were posted between hour i and hour i + 1. Hour 0 refers to
    midnight, while hour 23 refers to 11:00PM.

    To get started, read the Python Library documentation for datetime 
    objects:
    http://docs.python.org/py3k/library/datetime.html#datetime.datetime

    tweets -- A list of tweets to be grouped
    """
    MIN_HOUR = 0
    MAX_HOUR = 23

    tweets_by_hour = [[] for hours in range(MAX_HOUR +1)]
    for tweet in tweets:
        tweets_by_hour[tweet.get_time().hour].append(tweet)
    return tweets_by_hour

