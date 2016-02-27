package cn.uncode.session.data.memcached;

import cn.uncode.session.data.SessionCacheManager;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * User: Antergone
 * Date: 16/2/25
 */
public class MemcachedPool implements InitializingBean, DisposableBean {

    private SockIOPool sockIOPool;
    private MemCachedClient memCachedClient;

    private String[] hosts;
    private Integer[] weights;
    private boolean failOver = true;
    private int initConn = 5;
    private int minConn = 5;
    private int maxConn = 200;
    private int maxIdle = 30 * 30 * 1000;
    private int mainThreadSleep = 30;
    private boolean nagle = false;
    private boolean aliveCheck = true;
    private int socketTO = 30;
    private int socketConnectTO = 0;

    public static MemcachedPool getPool() {
        return (MemcachedPool) SessionCacheManager.getApplicationContext().getBean(MemcachedPool.class);
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public void setWeights(Integer[] weights) {
        this.weights = weights;
    }

    public void setFailOver(boolean failOver) {
        this.failOver = failOver;
    }

    public void setInitConn(int initConn) {
        this.initConn = initConn;
    }

    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMainThreadSleep(int mainThreadSleep) {
        this.mainThreadSleep = mainThreadSleep;
    }

    public void setNagle(boolean nagle) {
        this.nagle = nagle;
    }

    public void setAliveCheck(boolean aliveCheck) {
        this.aliveCheck = aliveCheck;
    }

    public void setSocketTO(int socketTO) {
        this.socketTO = socketTO;
    }

    public void setSocketConnectTO(int socketConnectTO) {
        this.socketConnectTO = socketConnectTO;
    }

    public MemCachedClient getClient() {
        if (memCachedClient != null)
            return memCachedClient;
        return null;
    }


    @Override
    public void destroy() throws Exception {
        if (sockIOPool != null) {
            sockIOPool.shutDown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null != hosts) {
            this.sockIOPool = SockIOPool.getInstance();
            System.out.println("server are" + hosts);
            sockIOPool.setServers(hosts);
            sockIOPool.setWeights(weights);
            sockIOPool.setFailover(failOver);
            sockIOPool.setInitConn(initConn);
            sockIOPool.setMinConn(minConn);
            sockIOPool.setMaxConn(maxConn);
            sockIOPool.setMaxIdle(maxIdle);
            sockIOPool.setMaintSleep(mainThreadSleep);
            sockIOPool.setNagle(nagle);
            sockIOPool.setSocketTO(socketTO);
            sockIOPool.setAliveCheck(aliveCheck);
            sockIOPool.setSocketConnectTO(socketConnectTO);
            sockIOPool.initialize();
            memCachedClient = new MemCachedClient();
        }
    }
}
