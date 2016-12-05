# centos7 Postgresql环境搭建 #

1. 下载Postgresql压缩包并解压
	- yum -y install lrzsz sysstat e4fsprogs ntp readline-devel zlib zlib-devel openssl openssl-devel pam-devel libxml2-devel libxslt-devel python-devel tcl-devel gcc make smartmontools flex bison perl perl-devel perl-ExtUtils* OpenIPMI-tools systemtap-sdt-devel
	-  mkdir /opt/soft_bak
	- cd /opt/soft_bak
	- wget http://ftp.postgresql.org/pub/source/v9.3.4/postgresql-9.3.4.tar.bz2
	- bunzip2  postgresql-9.3.4.tar.bz2
	- tar xvf postgresql-9.3.4.tar
	- wget http://pgfoundry.org/frs/download.php/3186/pgfincore-v1.1.1.tar.gz
	- tar -zxvf pgfincore-v1.1.1.tar.gz
	- cp -r  pgfincore-1.1.1  postgresql-9.3.4
	- vi /etc/sysconfig/i18n
	- 输入LANG="en_US.UTF-8"
	- :wq保存并退出
	- vi /etc/sysctl.conf 

	```
	kernel.shmmni = 4096
	kernel.sem = 50100 64128000 50100 1280
	fs.file-max = 7672460
	net.ipv4.ip_local_port_range = 9000 65000
	net.core.rmem_default = 1048576
	net.core.rmem_max = 4194304
	net.core.wmem_default = 262144
	net.core.wmem_max = 1048576
	net.ipv4.tcp_tw_recycle = 1
	net.ipv4.tcp_max_syn_backlog = 4096
	net.core.netdev_max_backlog = 10000
	vm.overcommit_memory = 0
	net.ipv4.ip_conntrack_max = 655360
	fs.aio-max-nr = 1048576
	net.ipv4.tcp_timestamps = 0
	```
	- sysctl -p
	- vi /etc/security/limits.conf

	```
	core file size          (blocks, -c) unlimited
	data seg size           (kbytes, -d) unlimited
	scheduling priority             (-e) 0
	file size               (blocks, -f) unlimited
	pending signals                 (-i) 14983
	max locked memory       (kbytes, -l) 50000000
	max memory size         (kbytes, -m) unlimited
	open files                      (-n) 131072
	pipe size            (512 bytes, -p) 8
	POSIX message queues     (bytes, -q) 819200
	real-time priority              (-r) 0
	stack size              (kbytes, -s) 8192
	cpu time               (seconds, -t) unlimited
	max user processes              (-u) 131072
	virtual memory          (kbytes, -v) unlimited
	file locks                      (-x) unlimited

	```
	- vi /etc/sysconfig/selinux
	- SELINUX=disabled
	- setenforce 0

	- vi /home/postgres/.bash_profile
	- ./configure --prefix=/opt/pgsql9.3.4 --with-pgport=5432 --with-perl --with-tcl --with-python --with-openssl --with-pam --without-ldap --with-libxml --with-libxslt --enable-thread-safety --with-wal-blocksize=8 --with-blocksize=8 --enable-dtrace --enable-debug --enable-cassert && gmake world
	- gmake install-world

	```
	export PGPORT=5432
	export PGDATA=/home/postgres/pgdata
	export LANG=en_US.utf8
	export PGHOME=/opt/pgsql
	export LD_LIBRARY_PATH=$PGHOME/lib:/lib64:/usr/lib64:/usr/local/lib64:/lib:/usr/lib:/usr/local/lib:$LD_LIBRARY_PATH
	export DATE=`date +"%Y%m%d%H%M"`
	export PATH=$PGHOME/bin:$PATH:.
	export MANPATH=$PGHOME/share/man:$MANPATH
	export PGUSER=postgres
	export PGHOST=$PGDATA
	export PGDATABASE=postgres
	alias rm='rm -i'
	alias ll='ls -lh'
	```


