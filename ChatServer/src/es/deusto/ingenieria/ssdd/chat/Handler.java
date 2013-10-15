package es.deusto.ingenieria.ssdd.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import es.deusto.ingenieria.ssdd.chat.data.User;

public class Handler extends Thread {

	boolean ejecutar = true;
	DatagramPacket request, reply;
	DatagramSocket udpSocket;
	byte[] buffer;
	Brain brain;
	
	
	public Handler(DatagramPacket request, DatagramPacket reply, byte[] buffer, DatagramSocket udpSocket) {
		this.request = request;
		this.reply = reply;
		this.buffer = buffer;
		this.udpSocket = udpSocket;
		this.brain = new Brain(this);
	}

	public void stop(boolean stop){
		ejecutar = stop;
	}
	
	public void run(){
		while (ejecutar) {
			try{
			request = new DatagramPacket(buffer, buffer.length);
			udpSocket.receive(request);				
			System.out.println(" - Received a request from '" + request.getAddress().getHostAddress() + ":" + request.getPort() + 
			                   "' -> " + new String(request.getData()));
			brain.receivedMessage(new String(request.getData()), request.getAddress().getHostAddress());
			
			
			//reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
			//udpSocket.send(reply);				
		
			} catch (IOException e) {
				System.err.println("# UDPServer IO error: " + e.getMessage());
			}
		}
	}

	public void sendMessage(String message, String ip) {
		try {
			reply = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(ip), request.getPort());
			udpSocket.send(reply);
			System.out.println("- Sent a message to '" + ip + "' -> " + message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
