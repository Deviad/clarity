# Running locally

In order to run the application locally you need to copy the config repository under ${HOME}/clarity-config.
By using a local path as the location of your property files, instead of a remote location,  you will be able 
to run (at least partially) the application offline.

The properties are accessible via this URL structure:

http://localhost:8888/<application-name-as-chosen-in-bootstrap-yml>/default

E.g.:
http://localhost:8888/clarity-config-server/default
http://localhost:8888/clarity-eureka-server/default
