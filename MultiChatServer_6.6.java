import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
 
public class MultiChatServer {
    private HashMap<String, DataOutputStream> clients;
    private ServerSocket serverSocket;
 
    public static void main(String[] args) {
        new MultiChatServer().start();
    }
 
    public MultiChatServer() {
        clients = new HashMap<String, DataOutputStream>();
 
        // ���� �����忡�� ������ ���̹Ƿ� ����ȭ
        Collections.synchronizedMap(clients);
    }
 
    public void start() {
        try {
            Socket socket;
 
            // ������ ���� ����
            serverSocket = new ServerSocket(7777);
            System.out.println("������ ���۵Ǿ����ϴ�.");
 
            // Ŭ���̾�Ʈ�� ����Ǹ�
            while (true) {
                // ��� ������ �����ϰ� ������ ����(������ 1:1�θ� ����ȴ�)
                socket = serverSocket.accept();
                ServerReceiver receiver = new ServerReceiver(socket);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream input;
        DataOutputStream output;
 
        public ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }
 
        @Override
        public void run() {
			
            String name = "";
			String bool = "t";
			//String False ="";
			boolean that=true;
            try {
                // Ŭ���̾�Ʈ�� ������ �����ϸ� ��ȭ�濡 �˸���.
				
				that = true;
				
				Iterator<String> it = clients.keySet().iterator();
                name = input.readUTF();

				//�ߺ� �г���
				while (it.hasNext()){
					String _name = ((String)it.next());
					if(_name.equals(name)){
						name="exit";
						
						this.socket.close();
						that=false;
					}

				}
				
				//������ 5���̻�
				if(clients.size()==5){
					name="exit";
							
					this.socket.close();
					that=false;
				}
				
				
				if(that == true){
					sendToAll("[" + name + "]" + "���� ��ȭ�濡 �����Ͽ����ϴ�.");
	 
					clients.put(name, output);
					
					
					System.out.println("["+name + "]"
							+ "���� ��ȭ�濡 �����Ͽ����ϴ�.");
					System.out.println("���� " + clients.size() + "���� ��ȭ�濡 ���� ���Դϴ�.");
				}
				
                // �޼��� ����
                while (input != null) {
                    sendToAll(input.readUTF());
                }
            } catch (IOException e) {
            } finally {
				if(that == true){
                // ������ ����Ǹ�
					clients.remove(name);
					sendToAll("[" + name + "]"
							+ "���� ��ȭ�濡�� �������ϴ�.");
					System.out.println("[" + name + "]"
							+ "���� ��ȭ�濡�� �������ϴ�.");
					System.out.println("���� " + clients.size() + "���� ��ȭ�濡 ���� ���Դϴ�.");
				}
			}
        }
 
        public void sendToAll(String message) {
            Iterator<String> it = clients.keySet().iterator();
 
            while (it.hasNext()) {
                try {
                    DataOutputStream dos = clients.get(it.next());
                    dos.writeUTF(message);
                } catch (Exception e) {
                }
            }
        }
    }
}
