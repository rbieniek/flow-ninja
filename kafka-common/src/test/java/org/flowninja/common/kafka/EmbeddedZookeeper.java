package org.flowninja.common.kafka;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Files;

import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmbeddedZookeeper implements InitializingBean, DisposableBean {
    private final EmbeddedZookeeperProperties zookeeperProperties;

    private ServerCnxnFactory cnxnFactory;
    private ZooKeeperServer zkServer;

    @Override
    public void destroy() throws Exception {
        if (zkServer.isRunning()) {
            zkServer.shutdown();
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final EmbeddedServerConfig config = new EmbeddedServerConfig();
        final File zookeeperTemp = Files.createTempDirectory(EmbeddedZookeeper.class.getSimpleName()).toFile();
        final File zookeeperData = new File(zookeeperTemp, "data");
        final File zookeeperLog = new File(zookeeperTemp, "log");

        zookeeperTemp.deleteOnExit();
        zookeeperData.deleteOnExit();
        zookeeperLog.deleteOnExit();

        zookeeperData.mkdirs();
        zookeeperLog.mkdirs();

        config.setClientPortAddress(new InetSocketAddress(zookeeperProperties.getBindAddr(), zookeeperProperties.getPortNumber()));
        config.setDataDir(zookeeperData.getAbsolutePath());
        config.setDataLogDir(zookeeperLog.getAbsolutePath());

        zkServer = new ZooKeeperServer();

        final FileTxnSnapLog txnLog = new FileTxnSnapLog(new File(config.getDataLogDir()), new File(
                config.getDataDir()));
        zkServer.setTxnLogFactory(txnLog);
        zkServer.setTickTime(config.getTickTime());
        zkServer.setMinSessionTimeout(config.getMinSessionTimeout());
        zkServer.setMaxSessionTimeout(config.getMaxSessionTimeout());
        cnxnFactory = ServerCnxnFactory.createFactory();
        cnxnFactory.configure(config.getClientPortAddress(),
                config.getMaxClientCnxns());
        cnxnFactory.startup(zkServer);
    }

    private static class EmbeddedServerConfig extends ServerConfig {

        public void setClientPortAddress(final InetSocketAddress _clientPortAddress) {
            clientPortAddress = _clientPortAddress;
        }

        public void setDataDir(final String _dataDir) {
            dataDir = _dataDir;
        }

        public void setDataLogDir(final String _dataLogDir) {
            dataLogDir = _dataLogDir;
        }

    }

}
