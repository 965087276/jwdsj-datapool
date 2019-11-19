# 请将jdk压缩包放在/home/centos/software目录下

yum -y install wget
mkdir /usr/local/java
cd /home/centos/software
wget http://10.10.169.58/softs/jdk-8u191-linux-x64.tar.gz
mv jdk-8u191-linux-x64.tar.gz /usr/local/java
cd /usr/local/java
tar -zxf jdk-8u191-linux-x64.tar.gz
echo "tar success"
cat >> /etc/profile << EOF
export JAVA_HOME=/usr/local/java/jdk1.8.0_191
export JRE_HOME=\${JAVA_HOME}/jre
export CLASSPATH=.:\${JAVA_HOME}/lib:\${JRE_HOME}/lib:\$CLASSPATH
export JAVA_PATH=\${JAVA_HOME}/bin:\${JRE_HOME}/bin
export PATH=\$PATH:\${JAVA_PATH}
EOF
echo "cat success"
source /etc/profile
echo "JDK is installed"
