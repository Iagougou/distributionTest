/**
 * All rights reserved |Powered by ifeng
 */
package com.github.distribute.zookeeper;

import java.util.List;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.exception.ZkAuthFailedException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.Children2Callback;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * @author： HLF
 * @createTime： 2017年5月30日{time}
 * @description：TODO
 */
public class GetZookeeperChildren implements Watcher{

	public static  CountDownLatch  connectedSemaphore = new  CountDownLatch(1); 
	public static  ZooKeeper zk = null;
	
	public static void main(String[] args) throws Exception {
	    zk = new ZooKeeper (ConnectConstants.ZookeeperAddress ,ConnectConstants.SessionTimeOut,new GetZookeeperChildren());
		
		connectedSemaphore.await();
		
		zk.create(ConnectConstants.NodePath,"123".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		zk.create(ConnectConstants.NodePath+"/Children","children123".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		System.out.println("node has been created");
		
		List<String> childrenList  = zk.getChildren(ConnectConstants.NodePath, true);
        System.out.println(childrenList);
        
		zk.setData(ConnectConstants.NodePath,"132".getBytes(),0);
		
		Thread.sleep(Integer.MAX_VALUE);
	}

	public void process(WatchedEvent event) {
		
		if (KeeperState.SyncConnected == event.getState()) {
			System.out.println("连接成功！");
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			}else if (EventType.NodeChildrenChanged == event.getType()) {
				try {
					System.out.println("ReGet child:"+ zk.getChildren(event.getPath(),true));
					
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
