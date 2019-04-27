package TorrentDownload;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RWDTorrent {
	public static char[] readFromGet(byte[] getTorrent) {
		char torrent[] = new char[getTorrent.length];
		if(torrent.length==0)
			return null;
		for(int i=0;i<torrent.length;i++) {
			torrent[i] = (char) getTorrent[i];
		}
		return torrent;
	}
	public static char[] read(String path) {
		int str;
		File file = new File(path);
		//如果文件夹不存在
		if(!file.exists()) {
			return null;
        }
		char torrent [] = new char [(int) file.length()];
		int count = 0;
		try {
			//BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			InputStream in = new FileInputStream(file);
			while((str= in.read())!=-1){
                torrent[count++] = (char)str;
            }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return torrent;
	}
	public static void write(String text,String path) {  
	   File file = new File(path);
	   if(!file.exists()) {
       	try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
	   FileOutputStream out;
	   char torrent[] = text.toCharArray();
		try {
			out = new FileOutputStream(file);
			for(int i=0;i<torrent.length;i++)
				out.write(torrent[i]); 
			out.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    
	}
	//将种子文件下载到文件中
	public static Boolean download(byte[] getTorrent,String path) {
        File file = new File(path);
        //如果文件不存在则创建一个
        if(!file.exists()) {
        	try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		try {
	        FileOutputStream fileOutputStream1 = new FileOutputStream(file);
	        fileOutputStream1.write(getTorrent);
	        fileOutputStream1.flush();
	        fileOutputStream1.close();
	        return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        
	}

}
