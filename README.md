# Running locally

1) Clone https://github.com/Deviad/clarity-config
2) clone https://github.com/Deviad/clarity
3) Substitute  in clarity-transaction-dispatcher.yml
```
  httpsEndpoint: <infura_https_endpoint>
  wsEndpoint: <infura_ws_endpoint>
```
with
```
  httpsEndpoint: http://localhost:8545
  wsEndpoint: ws://localhost:8546
```
and commit changes locally

4) in /path/to/clarity-config-server/src/main/resources/application.yml          
change `uri: ${HOME}/projects/clarity-config-private` with your path

5) Run docker-compose up -d in the main project folder.

6) Run clarity-config-server
7) Run clarity-eureka-server
8) Run clarity-dispatcher (the micronaut service)
