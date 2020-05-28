rm -rf /home/centos/code/backend/jwdsj-datapool/logs
cd /home/centos/code/backend/jwdsj-datapool
mkdir logs
nohup java -jar datapool-eureka/target/datapool-eureka-1.0.0.jar --spring.profiles.active=$1 1> ./logs/eureka.log 2>&1 &
nohup java -jar datapool-gateway/target/datapool-gateway-1.0.0.jar --spring.profiles.active=$1 1> ./logs/gateway.log 2>&1 &
nohup java -jar datapool-dictionary/target/datapool-dictionary-1.0.0.jar --spring.profiles.active=$1 1> ./logs/dictionary.log 2>&1 &
nohup java -jar datapool-indexmanage/target/datapool-indexmanage-1.0.0.jar --spring.profiles.active=$1 1> ./logs/indexmanage.log 2>&1 &
nohup java -jar datapool-search/target/datapool-search-1.0.0.jar --spring.profiles.active=$1 1> ./logs/search.log 2>&1 &
nohup java -jar datapool-delete/target/datapool-delete-1.0.0.jar --spring.profiles.active=$1 1> ./logs/delete.log 2>&1 &