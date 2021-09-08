# Myflix

### Description

My project consists of 3 web applications that communicate with eachother.
The main application (myflix) first contacts the tailored-recommendations app 
which communicates with the Imdb public API to fetch movies that are similar 
to our search term. If we do not search for anything specific, the main app 
contacts the generic-recommendations app instead, which fetches the most popular 
movies of the most popular genres by consuming the public API (we limit the results because of the monthly 
available requests that the public API offers). I also included user management 
in the main application by creating and storing users in a MySQL database. These 
users can later access the application with the right credentials.
Note that I removed my RapidAPI key from the yml file and another one must be 
generated so that the applications can run.