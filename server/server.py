import random
import string
import datetime

import cherrypy

from pymongo import MongoClient

@cherrypy.expose
class RateYourRide(object):
    @cherrypy.tools.json_out()
    def POST(self, stringData, sessionId):
        cherrypy.log(sessionId)
        cherrypy.log(stringData)

        response  = insert_ride_data(sessionId, stringData)
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
    cherrypy.log(data_id)
    
    # Update ride end time to current time
    # TODO make this its own function
    result = db.rides.update_one({'session_id': session_id}, {'$set': {'end_time': datetime.datetime.utcnow()}})
    cherrypy.log(result)

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
    cherrypy.engine.subscribe('stop', cleanup_db)

    # Start the server
    cherrypy.tree.mount(RateYourRide(), '/', conf)

    cherrypy.engine.start()
    cherrypy.engine.block()