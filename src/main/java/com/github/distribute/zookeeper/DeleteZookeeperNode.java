/**
 * All rights reserved |Powered by ifeng
 */
package com.github.distribute.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.Op.Delete;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
 * @author£º HLF
 * @createTime£º ÏÂÎç2:52:29
 * @description£ºTODO
 */
public class DeleteZookeeperNode implements Watcher{

	public static CountDownLatch  connectedSemaphore = new CountDownLatch(1);
	
	final static String  nodePath = "/zk_test";
	
	public static void main(String[] args) throws Exception {
		
		ZooKeeper zooKeeper = new ZooKeeper("192.168.116.38:2181",5000,new DeleteZookeeperNode());
		
		connectedSemaphore.await();
		
	    zooKeeper.delete(nodePath,0);
		
		System.out.println("success delete:--"  + nodePath);
	}
	
	public void process(WatchedEvent event) {
		System.out.println("connectiondSate:"+event.getState());
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

}
