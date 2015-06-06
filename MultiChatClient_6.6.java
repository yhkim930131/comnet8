import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
 
public class MultiChatClient {
    private String name;
	private String bool = "t";
    private Socket socket;
    private String serverIp = "127.0.0.1";
 
    public static void main(String[] args) {
        new MultiChatClient().start();
    }
 
    public void start() {
        try {
            socket = new Socket(serverIp, 7777);
            System.out.println("������ ����Ǿ����ϴ�. �й��� �Է��ϼ��� : ");
            name = new Scanner(System.in).nextLine();
 
            ClientReceiver clientReceiver = new ClientReceiver(socket);
            ClientSender clientSender = new ClientSender(socket);
             
            clientReceiver.start();
            clientSender.start();
        } catch (IOException e) {
        }
    }
 
    class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream input;
 
        public ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
            }
        }
 
        @Override
        public void run() {
            while (input != null) {
                try {
                    System.out.println(input.readUTF());
                } catch (IOException e) {
                }
            }
        }
    }
 
    class ClientSender extends Thread {
        Socket socket;
        DataOutputStream output;
 
        public ClientSender(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(name);
				//input.readUTF(bool);
                System.out.println("��ȭ�濡 �����Ͽ����ϴ�.");
				System.out.println("ä�� �Է� �ÿ� ä�� ���ʿ�  [�й�]�� ���� ������ "+
				"������ ���� ���� �� �Դϴ�.");
				System.out.println("ä���� �����Ͻ÷��� exit�� �Է��� �ֽʽÿ�.");
				
            } catch (Exception e) {
            }
        }
 
        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
            String msg = "";
 
            while (output != null) {
                try {
                    msg = sc.nextLine();
                    if(msg.equals("exit"))
                        System.exit(0);
                     
                    output.writeUTF("[" + name + "]" + msg);
                } catch (IOException e) {
                }
            }
        }
    }
}
