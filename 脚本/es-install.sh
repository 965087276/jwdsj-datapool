# 请将所有软件压缩包放在/home/centos/software目录下
echo "开始安装elasticsearch"
sudo systemctl stop firewalld.service
ipaddr=$(ifconfig | grep inet | grep -v inet6 | grep -v 127 | cut -d '' -f2 | awk '{print $2}')
mkdir -p /home/centos/data/elasticsearch
mkdir -p /home/centos/logs/elasticsearch
cd /home/centos/software
tar -zxf elasticsearch-6.5.4.tar.gz
cd /home/centos/software/elasticsearch-6.5.4/config
echo "完成安装elasticsearch"

echo "开始安装hanlp分词器"
cd /home/centos/software
mkdir -p elasticsearch-6.5.4/plugins/analysis-hanlp
cp elasticsearch-analysis-hanlp-6.5.4.zip elasticsearch-6.5.4/plugins/analysis-hanlp/
cd elasticsearch-6.5.4/plugins/analysis-hanlp/
unzip elasticsearch-analysis-hanlp-6.5.4.zip
mkdir ../../config/analysis-hanlp
mv config/hanlp.properties config/hanlp-remote.xml ../../config/analysis-hanlp/


echo "完成安装hanlp分词器"


echo "开始修改elasticsearch.yml"
cd /home/centos/software/elasticsearch-6.5.4/config
cat >> elasticsearch.yml << EOF
cluster.name: es-datapool
node.name: node-${ipaddr}
path.data: /home/centos/data/elasticsearch
path.logs: /home/centos/logs/elasticsearch
network.host: ${ipaddr}
discovery.zen.ping.unicast.hosts: ["192.168.0.5", "192.168.0.8", "192.168.0.12"]
discovery.zen.minimum_master_nodes: 2
EOF
echo "完成修改elasticsearch.yml"

echo "开始修改jvm.options"
cd /home/centos/software/elasticsearch-6.5.4/config
sed -i 's/-Xms1g/-Xms16g/g' jvm.options
sed -i 's/-Xmx1g/-Xmx16g/g' jvm.options
echo "完成修改jvm.options"
