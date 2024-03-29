package com.thinxz.fastdfs.config;

import com.thinxz.common.pool.AbsPooledExecute;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置TrackerServer对象池
 *
 * @author thinxz
 */
public class TrackerServerPool extends AbsPooledExecute<TrackerServer> {

    TrackerServerPool(FastDFSProperty fastDFSProperty) {
        try {
            // 加载配置
            Properties props = new Properties();
            // 最大连接数 并发量较大的话可加大该连接数
            props.setProperty("max_storage_connection", "8");
            // fastdfs为前缀的是FastDFS的配置
            props.setProperty("fastdfs.connect_timeout_in_seconds", "10");
            props.setProperty("fastdfs.network_timeout_in_seconds", "30");
            props.setProperty("fastdfs.charset", "UTF-8");
            // token 防盗链功能
            props.setProperty("fastdfs.http_anti_steal_token", fastDFSProperty.getHttpAntiStealToken());
            // 密钥
            props.setProperty("fastdfs.http_secret_key", fastDFSProperty.getHttpSecretKey());
            // TrackerServer port
            props.setProperty("fastdfs.http_tracker_http_port", fastDFSProperty.getHttpTrackerHttpPort());
            //
            props.setProperty("fastdfs.tracker_servers", fastDFSProperty.getTrackerServers());
            ClientGlobal.initByProperties(props);

            if (this.borrowObject() == null) {
                throw new Exception();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public TrackerServer create() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }
}
