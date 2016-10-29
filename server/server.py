import random
import string

import cherrypy


class RateYourRide(object):
    @cherrypy.expose
    def index(self):
        return None

    # Starts session and returns session id token for client
    @cherrypy.expose
    def start_session(self, length=12):
        # Generate a session token
        session_token = ''.join(random.sample(string.hexdigits, int(length)))
        cherrypy.session['session_id'] = session_token

        # Setup a database document for the session

        return session_token

    # Recieve array of distance data from client
    @cherrypy.expose
    def submit_data(self, dataArray=None):
        # Turn input data into an array

        # Update document with array of data using session id

        # Run and return analytics on document data
        return None
    
    # Return Current session id
    @cherrypy.expose
    def display(self):
        return cherrypy.session['session_id']

    # End session
    @cherrypy.expose
    def end_session(self):
        cherrypy.session.pop('session_id', None)


if __name__ == '__main__':
    conf = {
        '/': {
            'tools.sessions.on': True
        }
    }
    cherrypy.quickstart(StringGenerator(), '/', conf)