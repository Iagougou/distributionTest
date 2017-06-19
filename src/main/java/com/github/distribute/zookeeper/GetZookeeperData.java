package com.github.distribute.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.exception.ZkAuthFailedException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooDefs.Ids;

public class GetZookeeperData implements Watcher{

	static CountDownLatch connectedSemaphore = new CountDownLatch(1); 
	ZooKeeper zk = null;
	static Stat  stat = new Stat();
	public static void main(String[] args) throws Exception {
		
		ZooKeeper zk = new ZooKeeper(ConnectConstants.ZookeeperAddress ,ConnectConstants.SessionTimeOut,new GetZookeeperData());
		
		connectedSemaphore.await();
		
		zk.create(ConnectConstants.NodePath+"/node0119","noadeValue".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		zk.create(ConnectConstants.NodePath+"/node018","node2Value".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		System.out.println(new String(zk.getData(ConnectConstants.NodePath+"/node2",true, stat)));
		
		zk.setData(ConnectConstants.NodePath+"/node2","changed3".getBytes(),-1);
		System.out.println("noWatch"+stat.getCtime()+","+stat.getMtime()+","+stat.getMzxid()+","+stat.getVersion());
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		System.out.println("dfdf");
		if (KeeperState.SyncConnected == event.getState() && null == event.getPath()) {
			connectedSemaphore.countDown();
		}else if (event.getType() == EventType.NodeDataChanged) {
			try {
				System.out.println("nodeDateChanged");
				System.out.println(new String(zk.getData(event.getPath(),true,stat)));
				System.out.println("evnetPath"+event.getPath());
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(stat.getCtime()+","+stat.getMtime()+","+stat.getMzxid()+","+stat.getVersion());
		}
	}

}
