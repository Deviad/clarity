#!/usr/bin/env zsh

# this is another way to do "source ./color-map.sh"
. ./color-map.sh

#echo "Remember to use the following statement before building Docker images:"
# shellcheck disable=SC2016
#echo 'eval $(minikube docker-env -p clarity-mk)'
#eval $(minikube docker-env -p clarity-mk)

is_userdb=$(kubectl get pods 2>/dev/null|grep -i user-db)
is_keycloakdb=$(kubectl get pods 2>/dev/null|grep -i keycloak-db)
is_keycloakapp=$(kubectl get pods 2>/dev/null|grep -i keycloak-app)
#is_clarityauth=$(kubectl get pods 2>/dev/null|grep -i clarity-auth)
is_mongo_user_service=$(kubectl get services 2>/dev/null|grep -i mongo-user-service)
#is_clarityservice=$(kubectl get services 2>/dev/null|grep -i clarity-auth-service)
is_keycloak_service=$(kubectl get services 2>/dev/null|grep -i kc-service)
is_keycloak_db_service=$(kubectl get services 2>/dev/null|grep -i kc-db-service)
#is_clarityauthimage=$(docker image ls 2>/dev/null|grep -i clarityauth)

#if [[ -z "$is_userdb" ]];then
#  echo "$On_IYellow $BWhite An image named clarityauth does not exist $Color_Off"
#
#fi

if [[ -z "$(docker container ls|grep -i registry:2)" ]];then
  echo "$BGreen No local registry present, let's create one! $Color_Off"
  docker run -d -p 5000:5000 --restart=always --name registry registry:2
fi

#echo "$BGreen creating clarityauth image $Color_Off"
#
#./clarityauth/gradlew jibDockerBuild

#docker tag clarityauth localhost:5000/clarityauth

echo "$BGreen push clarityauth image to local registry $Color_Off"

#docker push localhost:5000/clarityauth

if [[ -z "$is_userdb" ]];then
      echo "$BGreen Pod user-db does not exist $Color_Off"
else
      echo "$On_IYellow $BWhite Deleting user-db since it already exists $Color_Off"
      kubectl delete pods user-db
fi
if [[ -z "$is_keycloakdb" ]];then
      echo "$BGreen Pod keycloak-db does not exist $Color_Off"
else
      echo "$On_IYellow $BWhite Deleting keycloak-db since it already exists $Color_Off"
      kubectl delete pods keycloak-db
fi
if [[ -z "$is_keycloakapp" ]];then
      echo "$BGreen Pod keycloak-app does not exist $Color_Off"
else
      echo "$On_IYellow $BWhite Deleting keycloak-app since it already exists $Color_Off"
      kubectl delete pods keycloak-app
fi

#if [[ -z "$is_clarityauth" ]];then
#      echo "$BGreen Pod clarity-auth does not exist $Color_Off"
#else
#      echo "$On_IYellow $BWhite Deleting clarity-auth since it already exists $Color_Off"
#      kubectl delete pods clarity-auth
#fi
if [[ -z "$is_mongo_user_service" ]];then
      echo "$BGreen Service mongo-user-service does not exist $Color_Off"
else
      echo "$On_IYellow $BWhite Deleting service mongo-user-service since it already exists $Color_Off"
      kubectl delete service mongo-user-service
fi
if [[ -z "$is_keycloak_service" ]];then
      echo "$BGreen Service kc-service does not exist $Color_Off"
else
      echo "$On_IYellow $BWhite Deleting service kc-service since it already exists $Color_Off"
      kubectl delete service kc-service
fi
if [[ -z "$is_keycloak_db_service" ]];then
      echo "$BGreen Service kc-db-service does not exist $Color_Off"
else
      echo "$On_IYellow $BWhite Deleting service kc-db-service since it already exists $Color_Off"
      kubectl delete service kc-db-service
fi

#if [[ -z "$is_clarityservice" ]];then
#      echo "$BGreen Service clarity-auth-service does not exist $Color_Off"
#else
#      echo "$On_IYellow $BWhite Deleting service clarity-auth-service since it already exists $Color_Off"
#      kubectl delete service clarity-auth-service
#fi
echo "$On_White $BGreen Creating new pods and services $Color_Off"

KEYCLOAK_ADMIN=superadmin
KEYCLOAK_PASSWORD=password
template=$(cat "kubernetes.yml" |
sed "s/{{KEYCLOAK_ADMIN}}/${KEYCLOAK_ADMIN}/g;
s/{{KEYCLOAK_PASSWORD}}/${KEYCLOAK_PASSWORD}/g")

echo "$template" | kubectl apply -f -



