/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
class ChatFrame1 extends JFrame implements ActionListener, Runnable {
private JTextField txtToSend = new JTextField(25);
private JTextArea txtHistory = new JTextArea();
private JScrollPane sp = new JScrollPane(txtHistory);
private JButton btnSend = new JButton("Send");
private JButton btnShow = new JButton("Show List");
private JPanel pnlSouth = new JPanel();
private InputStreamReader fr ;
private OutputStreamWriter fw;
private OutputStream o;
private ObjectOutputStream os;
private Socket s;
private PrintWriter pw;
private BufferedReader br;
private String nickname;
public ChatFrame1() {
join(JOptionPane.showInputDialog(this, "Please Choose a NickName", "NickName", JOptionPane.QUESTION_MESSAGE));
initialize();
}
public void run() {
try {
while (true) {
String str = br.readLine();
if(str.equals("nf")){
	JOptionPane.showMessageDialog(this, "Not found", "Error", JOptionPane.ERROR_MESSAGE);
}
else{
txtHistory.append(str + "\n");
}
}
} catch (IOException ex) {
JOptionPane.showMessageDialog(this, "Disconnected FromServer", "Error", JOptionPane.ERROR_MESSAGE);
System.exit(0);
}
}
public void initialize() {
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.addWindowListener(new java.awt.event.WindowAdapter() {
	    @Override
	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	        	Quit();
	        }
	    
	});	
	
	
this.setTitle("Chatting: " + nickname);
this.setSize(500, 300);
this.setLocation(200, 15);
this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
Container c = this.getContentPane();
c.add(BorderLayout.SOUTH, pnlSouth);
pnlSouth.setLayout(new FlowLayout());
pnlSouth.add(txtToSend);
pnlSouth.add(btnSend);
pnlSouth.add(btnShow);
c.add(BorderLayout.CENTER, sp);
txtHistory.setEditable(false);
btnShow.addActionListener(this);
btnSend.addActionListener(this);
txtToSend.addActionListener(this);
}
public void Quit() {
	ArrayList a = new ArrayList();
	a.add("Exit");
	a.add(nickname);
	try {
		os.writeObject(a);
		os.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void actionPerformed(ActionEvent evt) {
	Object o = evt.getSource();
	if(o==btnShow){
		GetMemberList();
	}
	else{

String st = txtToSend.getText();
//System.out.println(st);
//System.out.println(getindex(st));
int dot = getindex(st);
if (!st.trim().equals("")) {
	if(getindex(st)==-1){
		JOptionPane.showMessageDialog(this, "You didn't mentioned the reciver follow this format: reciver: The message", "Error", JOptionPane.ERROR_MESSAGE);
	}
	else{
		Chat(nickname,st.substring(0, dot),3,st.substring(dot+1));
		txtToSend.setText("");
		
	}
}

	}
}
public void Chat(String nickname, String destination, int ttl, String message) {
	ArrayList a = new ArrayList();
	a.add(nickname);
	a.add(destination);
	a.add(ttl);
	a.add(message);
	
	try {
		os.writeObject(a);
		os.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public void join(String ss){
	nickname=ss;
	try {
		 
		s = new Socket("127.0.0.1", 6000);
		fr = new InputStreamReader(s.getInputStream());
		fw = new OutputStreamWriter(s.getOutputStream());
		pw = new PrintWriter(fw);
		br = new BufferedReader(fr);
		o = s.getOutputStream();
		os = new ObjectOutputStream(o);
		Thread t = new Thread(this);
		t.start();
		pw.println(nickname);
		pw.flush();
		
		} catch (IOException ex) {
		JOptionPane.showMessageDialog(this, "Can't Connect ToServer", "Error", JOptionPane.ERROR_MESSAGE);
		System.out.println(ex.getMessage());
		System.exit(0);
		}

}
public int getindex(String s)
{
	int index = 0;
	for(int i = 0; i<s.length();i++)
	{
		if(s.charAt(i) == ':')
		{
			index = i;
			return index;
			
		}
	}
	return -1;
	
}
public void GetMemberList(){

	ArrayList a = new ArrayList();
	a.add("GAM");
	a.add(nickname);
	try {
		os.writeObject(a);
		os.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}



}
public class Client1 {
public static void main(String[] args) {
ChatFrame1 f = new ChatFrame1();
f.setVisible(true);
}
}