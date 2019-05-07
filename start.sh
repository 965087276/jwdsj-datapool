nohup java -jar datapool-eureka/target/datapool-eureka-1.0.0.jar --spring.profiles.active=$1 1> ./logs/eureka.log 2>&1 &
nohup java -jar datapool-gateway/target/datapool-gateway-1.0.0.jar --spring.profiles.active=$1 1> ./logs/gateway.log 2>&1 &
nohup java -jar datapool-dictionary/target/datapool-dictionary-1.0.0.jar --spring.profiles.active=$1 1> ./logs/dictionary.log 2>&1 &
nohup java -jar datapool-datastats/target/datapool-datastats-1.0.0.jar --spring.profiles.active=$1 1> ./logs/datastats.log 2>&1 &
nohup java -jar datapool-indexmanage/target/datapool-indexmanage-1.0.0.jar --spring.profiles.active=$1 1> ./logs/indexmanage.log 2>&1 &
nohup java -jar datapool-datasync/target/datapool-datasync-1.0.0.jar --spring.profiles.active=$1 1> ./logs/datasync.log 2>&1 &
nohup java -jar datapool-search/target/datapool-search-1.0.0.jar --spring.profiles.active=$1 1> ./logs/search.log 2>&1 &