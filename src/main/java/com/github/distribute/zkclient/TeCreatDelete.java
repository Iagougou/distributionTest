package com.github.distribute.zkclient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import com.github.distribute.zookeeper.ConnectConstants;

public class TeCreatDelete {

	public static void main(String[] args) throws InterruptedException {
		ZkClient zkClient = new ZkClient(ConnectConstants.ZookeeperAddress,ConnectConstants.SessionTimeOut);
	/*	String path = "/zk-bookauto/c1/cc1";
	//	zkClient.createPersistent(path+"/cc1");
	//	zkClient.delete(path);
		System.out.println("ifExist£º"+zkClient.exists(path));
		System.out.println("path:"+zkClient.getChildren(path));*/
	    String path = "/zk-test201761720:00";
	    zkClient.subscribeChildChanges(path,new zkListener());
		zkClient.createPersistent(path);
		Thread.sleep(1000L);
		zkClient.createEphemeral(path+"/test1");
		Thread.sleep(1000L);
		zkClient.deleteRecursive(path);
		Thread.sleep(1000L);
		
	}
}
   class zkListener implements IZkChildListener{
	   public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
			System.out.println(parentPath+"'s child chang,currentChilds:"+ currentChildren);
		}
   }

