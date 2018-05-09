import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JOptionPane;
//class st extends Thread{
//	private Socket s;
//	private PrintWriter w;
//	public st(Socket s,PrintWriter w){
//		this.s=s;
//		this.w=w;
//	}
//	@Override
//	public void run(){
//	        try{
//	        	InputStream is = s.getInputStream();
//	        	ObjectInputStream ois = new ObjectInputStream(is);
//	        	
//	        }
//	        catch(Exception ex){}
//	
//	
//	
//	}
//}

class t extends Thread{
	private static int no=0;
	private InputStream is ;
	private ObjectInputStream ois ;
    private Socket s;
    private BufferedReader r;
    private PrintWriter w;
    private PrintWriter wms;
    private Socket mainsocket; 
    private ObjectOutputStream oo;
//    private static ArrayList pws = new ArrayList();
    private static Map<String, PrintWriter> data = new HashMap<String, PrintWriter>();
    
    
    public t(Socket s){
        this.s=s;
    }
    @Override
    public void run(){
        try{
        	//main server connection
        	if(no==0){
        		no++;
        	mainsocket = new Socket("127.0.0.1", 7000);
        	OutputStream oss = mainsocket.getOutputStream();
        	 oo = new ObjectOutputStream(oss);
        	OutputStreamWriter fwms = new OutputStreamWriter(mainsocket.getOutputStream());
            wms = new PrintWriter(fwms);
         	wms.println("server");
    		wms.flush();
//    		InputStream is2 = mainsocket.getInputStream();
//            ObjectInputStream ois2 = new ObjectInputStream(is2);
        	}
        	//------------------
        InputStreamReader fr = new InputStreamReader(s.getInputStream());
        r = new BufferedReader(fr);
            
        OutputStreamWriter fw = new OutputStreamWriter(s.getOutputStream());
        w = new PrintWriter(fw);
        InputStream is = s.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        JoinResponse(s);

        while(true){
        	Object o = ois.readObject();
        	ArrayList a = (ArrayList) o;
        	if(((String)a.get(0)).equals("GAM"))
        	{
            	MemberListResponse(a);
        	}
        	else if(((String)a.get(0)).equals("Exit"))
        	{
        		data.remove((String)a.get(1));
        	}
        	else if(((String)a.get(0)).equals("not found"))
        	{
        		data.get((String)a.get(0)).println("nf");
        	}
        	else if(((String)a.get(0)).equals("server")){
        		if(check( (String)a.get(1) )){
        			String st = (String)a.get(1);
        			data.get(st).println("<"+(String)a.get(2)+">"+(String)a.get(3));
        			data.get(st).flush();
        		}
        		else{
        			ArrayList a2 =new ArrayList();
            		
            		a2.add("not found");
            		a2.add((String)a.get(2));
            		oo.writeObject(a2);
            		oo.flush();
        		}
        	}
        	else if(!(check(((String)a.get(1))))){
        		ArrayList a2 =new ArrayList();
        		int ttl = (int) a.get(2);
        		//ttl-=1;
        		a2.add("server");
        		a2.add((String)a.get(1));// reciver name (index=1)
        		a2.add((String)a.get(0));// sender name (index=2)
        		a2.add((String)a.get(3));// message (index=3)
        		//a2.add(ttl);
//        		if(ttl<=0)
//        		{
//        			JOptionPane.showInputDialog(this, "Can't Connect ToServer");
//
//        		}
        		data.get((String)a.get(0)).println("<"+(String)a.get(0)+">"+(String)a.get(3));
        		data.get((String)a.get(0)).flush();
        		oo.writeObject(a2);
        		oo.flush();
        	}
        	
        	else
        	{
        		data.get((String)a.get(0)).println("<"+(String)a.get(0)+">"+(String)a.get(3));
        		data.get((String)a.get(1)).println("<"+(String)a.get(0)+">"+(String)a.get(3));
        		data.get((String)a.get(1)).flush();
        		data.get((String)a.get(0)).flush();
        		
        	}
        	
        	
        	
        	
        	
        	
        	
        	
//            String st =r.readLine();
//            for (int i = 0; i < pws.size(); i++) {
//                ((PrintWriter) pws.get(i)).println(st);
//                ((PrintWriter) pws.get(i)).flush();
//            }
        }
        
        }
        catch(Exception ex){
            
        }
    }
	private boolean check(String string) {
		String st;
		for (Entry<String, PrintWriter> entry : data.entrySet()) {
		    st =  entry.getKey();
		    if(st.equals(string)){
		    	return true;
		    }
		}
		System.out.print("hena check");
		return false;
	}
	private void JoinResponse(Socket s) {
		String st1;
		try {
			st1 = r.readLine();
			System.out.println(st1);
			for (Entry<String, PrintWriter> entry : data.entrySet()) {
				String ss;
			    ss =  entry.getKey();
			    if(st1.equals(ss)){
			    	System.out.println("This nickname already exists");
			    	s.close();
			    	return;
			    }
			    
			}

			data.put(st1, w);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void MemberListResponse(ArrayList a) {
		String st="The Member List is :"+'\n';

		for (Entry<String, PrintWriter> entry : data.entrySet()) {
			if(!entry.getKey().equals("server"))
		    st +=  entry.getKey()+'\n';
		    
		}
		st+="-----------------------------";
		data.get((String)a.get(1)).println(st);
		data.get((String)a.get(1)).flush();
		
	
	}
    
    
}










public class Server1 {

    public static void main(String[] args) {
       try{
            ServerSocket ss = new ServerSocket(6000);
            while(true){
                Socket s = ss.accept();
                t th = new t(s);
                th.start();
            }
            
            
            
            
            
        }
        catch(Exception ex){
            
        }
    }
    
}
