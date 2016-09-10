from geo import us_states, Position, geo_distance
from tweet import Tweet
    
def find_centroid(polygon):
    """Find the centroid of a polygon.

    http://en.wikipedia.org/wiki/Centroid #Centroid_of_polygon

    polygon -- A list of positions, in which the first and last are the same

    Returns: 3 numbers; centroid latitude, centroid longitude, and polygon area

    Hint: If a polygon has 0 area, return its first position as its centroid

    >>> p1, p2, p3 = Position(1, 2), Position(3, 4), Position(5, 0)
    >>> triangle = [p1, p2, p3, p1]  # First vertex is also the last vertex
    >>> find_centroid(triangle)
    (Position(3.0, 2.0), 6.0)
    >>> find_centroid([p1, p3, p2, p1])
    (Position(3.0, 2.0), 6.0)
    >>> find_centroid([p1, p2, p1])
    (Position(1.0, 2.0), 0)
    """
    poly_area = 0
    lons = []
    lats = []
    helper = []
    ## even though I don't know the meaning, sum of helper is what called 'A'
    ## in the formula
    lons.append(polygon[0].longitude())
    lats.append(polygon[0].latitude())
    lon = 0
    lat = 0
    cur_pos = 1
    if len(polygon) == 1:
        return (Position(polygon[0].latitude(),polygon[0].longitude()),0)
    for pos in polygon[1:]:
        lons.append(pos.longitude())
        lats.append(pos.latitude())
        helper.append(lons[cur_pos - 1]*lats[cur_pos] - \
                      lons[cur_pos ]*lats[cur_pos -1])
        cur_pos += 1
    poly_area = 0.5*sum(helper)
    if poly_area == 0:
        return (polygon[0],0)
    for index in range(len(helper)):
        lon += (lons[index+1] + lons[index])*helper[index]
        lat += (lats[index+1] + lats[index])*helper[index]

    lon = lon/(6*poly_area)
    lat = lat/(6*poly_area)
    poly_area = abs(poly_area)
    return (Position(lat,lon),poly_area)
        
        

def find_center(polygons):
    """Compute the geographic center of a state, averaged over its polygons.

    The center is the average position of centroids of the polygons in polygons,
    weighted by the area of those polygons.

    Arguments:
    polygons -- a list of polygons

    >>> ca = find_center(us_states['CA'])  # California
    >>> round(ca.latitude(), 5)
    37.25389
    >>> round(ca.longitude(), 5)
    -119.61439

    >>> hi = find_center(us_states['HI'])  # Hawaii
    >>> round(hi.latitude(), 5)
    20.1489
    >>> round(hi.longitude(), 5)
    -156.21763
    """
    centroids = [find_centroid(poly) for poly in polygons]
    total_area = sum([centroids[poly][1] for poly in range(len(polygons))])
    sum_lat = sum([centroids[poly][1]*centroids[poly][0].latitude() \
                   for poly in range(len(polygons))])
    sum_lon = sum([centroids[poly][1]*centroids[poly][0].longitude() \
                   for poly in range(len(polygons))])
    return Position(sum_lat/total_area ,  sum_lon/total_area)

def find_closest_state(state_centers):
    """Returns a function that takes a tweet and returns the name of the state 
    closest to the given tweet's location.

    Use the geo_distance function (already provided) to calculate distance
    in miles between two latitude-longitude positions.

    Arguments:
    tweet -- a tweet abstract data type
    state_centers -- a dictionary from state names to positions.

    >>> state_centers = {n: find_center(s) for n, s in us_states.items()}
    >>> sf = Tweet("Welcome to San Francisco", None, 38, -122)
    >>> nj = Tweet("Welcome to New Jersey", None, 41.1, -74)
    >>> find_state = find_closest_state(state_centers)
    >>> find_state(sf)
    'CA'
    >>> find_state(nj)
    'NJ'
    """
    
    def find_state(tweet):
        tweet_pos = tweet.get_location()
        best_dis = None
        for state, center in state_centers.items():
            temp_dis = geo_distance(center, tweet_pos)
            if best_dis == None:
                best_dis = temp_dis
                best_state = state
            elif temp_dis < best_dis:
                best_dis = temp_dis
                best_state = state
        return best_state
    
    return find_state

def find_containing_state(states):
    """Returns a function that takes a tweet and returns the name of the state 
    containing the given tweet's location.

    Use the geo_distance function (already provided) to calculate distance
    in miles between two latitude-longitude positions.

    Arguments:
    tweet -- a tweet abstract data type
    us_states -- a dictionary from state names to positions.

    >>> sf = Tweet("Welcome to San Francisco", None, 38, -122)
    >>> ny = Tweet("Welcome to New York", None, 41.1, -74)
    >>> find_state = find_containing_state(us_states)
    >>> find_state(sf)
    'CA'
    >>> find_state(ny)
    'NY'
    """
    
    
def group_tweets_by_state(tweets,find_state):
    """Return a dictionary that aggregates tweets by their nearest state center.

    The keys of the returned dictionary are state names, and the values are
    lists of tweets that appear closer to that state center than any other.

    tweets -- a sequence of tweet abstract data types

    >>> state_centers = {n: find_center(s) for n, s in us_states.items()}
    >>> find_state = find_closest_state(state_centers);
    >>> sf = Tweet("Welcome to San Francisco", None, 38, -122)
    >>> ny = Tweet("Welcome to New York", None, 41, -74)
    >>> ca_tweets = group_tweets_by_state([sf, ny],find_state)['CA']
    >>> ca_tweets
    [Tweet('Welcome to San Francisco', None, 38, -122)]
    """
    tweets_by_state = {}
    for tweet in tweets:
        state = find_state(tweet)
        if not state in tweets_by_state:
            tweets_by_state[state] = [tweet]
        else:
            tweets_by_state[state].append(tweet)
    return tweets_by_state


