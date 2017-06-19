/**
 * All rights reserved |Powered by ifeng
 */
package com.github.distribute.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * @author£º HLF
 * @createTime£º ÏÂÎç8:46:39
 * @description£ºTODO
 */
public class CreaetZookeeperAsynNode  implements Watcher{

	public static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		
		ZooKeeper zooKeeper = new ZooKeeper("192.168.116.38:2181",5000,new CreaetZookeeperAsynNode()); 
	
		connectedSemaphore.await();
		
		zooKeeper.create("/zk-book/zk-test-semaphore","".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IsStringCallBack(),"i am context");
		
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

 
	
}

class IsStringCallBack  implements AsyncCallback.StringCallback{

		public void processResult(int rc, String path, Object ctx, String name) {
			System.out.println("creat path result ;["+rc+','+path+ctx+",realname"+name);
		}

		
 }
