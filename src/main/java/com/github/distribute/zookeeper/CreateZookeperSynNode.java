/**
 * All rights reserved |Powered by ifeng
 */
package com.github.distribute.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author£º HLF
 * @createTime£º ÏÂÎç7:45:52
 * @description£ºTODO
 */
public class CreateZookeperSynNode  implements Watcher{

	public static CountDownLatch  connectedSemaphore= new CountDownLatch(1);
	public static void main(String[] args) throws  Exception {
		
		ZooKeeper zooKeeper = new ZooKeeper("192.168.116.38:2181",5000,new CreateZookeperSynNode());
		
	    connectedSemaphore.await();
	    String path = zooKeeper.create("/zk_test","".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	    System.out.println("success create node"+path);
	    
	    String path2 = zooKeeper.create("/zk_test_ephemeral","haha".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	    System.out.println("success creaete sequential"+ path2);
	    
	    Thread.sleep(1000);
	}
	
	
    
	public void process(WatchedEvent event) {
		System.out.println("event"+ event);
		if (KeeperState.SyncConnected==event.getState()) {
			connectedSemaphore.countDown();
		}
	}

}
