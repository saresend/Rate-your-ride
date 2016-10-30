import random
import string
import json
import datetime

import cherrypy

from pymongo import MongoClient

#import analysis

@cherrypy.expose
class BumpinessAnalytics(object):
    def GET(self, sessionId):
        json_data = getRideData(sessionId)
        return bumpiness(json_data['ride_data'])

def bumpiness(data):
    TRIGGER_LEVEL = 8
    
    totalCount = float(len(data))
    triggerPoints = []
    triggerCount = 0

    for i in range(1, len(data)-1):
        if abs(data[i-1] - data[i]) > TRIGGER_LEVEL and abs(data[i+1] - data[i]) > TRIGGER_LEVEL:
            triggerCount = triggerCount + 1
            triggerPoints.append(i)
        print "Trigger points: "
        print triggerPoints
    return triggerCount / totalCount


# API for analytics to grab ride data
@cherrypy.expose
class RideAnalytics(object):
    @cherrypy.tools.json_out()
    def POST(self):
        # Get most recent json ride data
        json_data = getRideData()
        return json_data

def getRideData():
    client = db_connect()
    db = client.rate_your_ride
    
    collection = db.data
    json_results = collection.find().limit(1).sort({'$natural':-1})

    cherrypy.log(str(json_results))
    return json_results

@cherrypy.expose
class RateYourRide(object):
    @cherrypy.tools.json_out()
    def POST(self, stringData, sessionId):
        # Parse parameters
        cherrypy.log("Start processing")
        cherrypy.log(sessionId)
        cherrypy.log(stringData)
        
        cherrypy.log("Processed data")
        stringData = stringData[9:-2]
        dataArray = map(int, stringData.split(','))
        
        cherrypy.log(sessionId)
        print dataArray

        response  = insert_ride_data(sessionId, dataArray)
        return response

    def GET(self):
        length = 12
        session_token = ''.join(random.sample(string.hexdigits, length))
        create_ride(session_token)

        return session_token

# Create ride data
def create_ride(session_id):
    client = db_connect()
    db = client.rate_your_ride

    new_ride = {}
    new_ride['session_id'] = session_id
    new_ride['start_time'] = datetime.datetime.utcnow()
    new_ride['end_time'] = ''

    rides = db.rides
    ride_id = rides.insert_one(new_ride).inserted_id
    
    client.close()
    return ride_id

# 
def insert_ride_data(session_id, ride_data):
    client = db_connect()
    db = client.rate_your_ride

    # Insert ride data into data collection
    data = {}
    data['session_id'] = session_id
    data['ride_data'] = ride_data

    collection = db.data
    data_id = collection.insert_one(data).inserted_id
    cherrypy.log(str(data_id))
    
    # Update ride end time to current time
    # TODO make this its own function
    result = db.rides.update_one({'session_id': session_id}, {'$set': {'end_time': datetime.datetime.utcnow()}})
    cherrypy.log(str(result))

    # Create metrics
    metrics = create_metrics(ride_data)
    return metrics

# Returns metrics about ride stored in JSON
def create_metrics(rideData):
    # TODO analyze data and create metrics
    metrics = {}
    return metrics

# Get database connection
def db_connect():
    client = MongoClient('localhost', 27017)
    return client

def cleanup_db():
    # Get db connection
    client = db_connect()
    db = client.rate_your_ride

    # Clean collections
    db.rides.drop()
    db.data.drop()

    # Kill connection
    client.close()
    return None

def setup_db():
    # Setup db connection
    client = db_connect()
    db = client.rate_your_ride

    #add test data and initialize collections
    rides = db.rides
    rideData = db.data

    # Kill db connection
    client.close()
    return None

if __name__ == '__main__':
    # Configure the server
    conf = {
        '/': {
            'request.dispatch': cherrypy.dispatch.MethodDispatcher(),
            'tools.sessions.on': True,
            'tools.response_headers.on': True,
            'tools.response_headers.headers': [('Content-Type', 'text/plain')],
        }
    }
    cherrypy.config.update({'server.socket_host': '0.0.0.0',
                            'server.socket_port': 80, })
    
    # Database setup
    cherrypy.engine.subscribe('start', setup_db)
    #cherrypy.engine.subscribe('stop', cleanup_db)

    # Start the server
    cherrypy.tree.mount(RateYourRide(), '/', conf)
    cherrypy.tree.mount(RideAnalytics(), '/analytics', conf)
    cherrypy.tree.mount(BumpinessAnalytics(), '/bumpiness', conf)

    cherrypy.engine.start()
    cherrypy.engine.block()