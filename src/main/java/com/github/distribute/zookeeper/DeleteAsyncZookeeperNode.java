package com.github.distribute.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class DeleteAsyncZookeeperNode  implements Watcher{

	static CountDownLatch connectedSemaphore  = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper = new  ZooKeeper(ConnectConstants.ZookeeperAddress,ConnectConstants.SessionTimeOut,new DeleteAsyncZookeeperNode());
		zooKeeper.delete(ConnectConstants.NodePath,0, new IsVoidCallback(),"delect info");
		connectedSemaphore.await();
		System.out.println("success deleted ");
		
		
	}
	
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			System.out.println(event.getType());
			if (EventType.NodeDeleted == event.getType()) {
				System.out.println(event.getType());
				connectedSemaphore.countDown();
			}
		}
	}

}

class  IsVoidCallback implements AsyncCallback.VoidCallback{

	public void processResult(int rc, String path, Object ctx) {
		System.out.println("deleted begin -- [rc=" +rc +" node info "+ path +ctx);
	}
	
}