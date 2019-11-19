echo "开始配置elasticsearch"
swapoff -a
cat >> /etc/sysctl.conf << EOF
vm.max_map_count=655360
EOF
sysctl -p
cat >> /etc/security/limits.conf << EOF
* soft nofile 65536
* hard nofile 131072
* soft nproc 2048
* hard nproc 4096
EOF
echo "完成配置elasticsearch"
