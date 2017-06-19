package com.github.distribute.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import com.github.distribute.zookeeper.ConnectConstants;

public class TestGetData {
	public static void main(String[] args) throws InterruptedException {
		ZkClient zkClient = new ZkClient(ConnectConstants.ZookeeperAddress,ConnectConstants.SessionTimeOut);
		String path = "/zkclient-test1";
		zkClient.createEphemeral(path,123);
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			
			public void handleDataDeleted(String arg0) throws Exception {
					System.out.println(arg0 +"has deleted ");
			}
			
			public void handleDataChange(String arg0, Object arg1) throws Exception {
				System.out.println(arg0 +" changes to " + arg1);
			}
		});
		
		System.out.println(zkClient.readData(path));
		User user = new User();
		user.setName("hlf");
		zkClient.writeData(path, user);
		
		Thread.sleep(1000L);
		System.out.println((User)zkClient.readData(path));
		
		zkClient.delete(path);
		Thread.sleep(1000L);
	}
}
