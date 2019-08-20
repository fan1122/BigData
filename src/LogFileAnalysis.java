import java.io.*;
import java.security.SecureRandom;
import java.util.*;
public class LogFileAnalysis {
	
	public final long fileCapacity=100;
	
	//保存每个文件的流对象（写入文件中）
	public final Map<Integer,BufferedWriter> bwMap = new HashMap<Integer,BufferedWriter>();
	
	//分隔文件用-存储相当数量之后再存入某个文件
	public final Map<Integer,List<String>> dataMap = new HashMap<Integer,List<String>>();
	
	//记录每个小文件是否满了
	public final Map<Integer,Long> isFullMap=new HashMap<Integer,Long> ();
	
	//存储访问次数前100的IP
	public Set<URL> set = new TreeSet<URL>();

	// 生成url文件
	public void creatLog(File log,long d) throws Exception{
		
		FileWriter fw = new FileWriter(log,true);
		
		BufferedWriter bw = new BufferedWriter(fw);
		
		SecureRandom random = new SecureRandom();
		
		for (int i = 0; i < d; i++) {//生成d个URL
			
			bw.write("https://Yifan.Zhang.github.io/"+random.nextInt(255)+"\n");//URL
			
			if((i+1) % 1000 == 0){
				
				bw.flush();
				
			}
		}
		
		bw.flush();
		
		fw.close();
		
		bw.close();
	}

	// 分割日志文件
	public void splitLog(File logflie,int fileNums) throws Exception{
		
		FileReader fr = new FileReader(logflie);
		
		BufferedReader br =new BufferedReader(fr);
		
		String url = br.readLine();//读取一行
		
		//先创建文件及流对象方便使用
		for(int i=0;i<fileNums;i++){//循环1024个小文件
			
			File file = new File("E:\\bigdata\\logSplit\\"+ i + ".txt");//新建小文件
			
			bwMap.put(i, new BufferedWriter(new FileWriter(file,true)));//bwMap记录的键值是i-对象流
			
			dataMap.put(i, new LinkedList<String>());//dataMap记录的键值是i-双向链表（小文件）
			
			isFullMap.put(i, (long) 0);//每个文件刚开始都没有超出内存大小（1G）
		
		}
		
		while(url != null){
			
			int hashCode = url.hashCode();
			
			hashCode = hashCode < 0 ? -hashCode : hashCode;
			
			int fileNum = hashCode % fileNums;//针对IP的哈希值去查找对应的存储文件
			
			int hashStep=1;
			
			while(isFullMap.get(fileNum)==fileCapacity) {
				
				fileNum = (hashCode +hashStep)% fileNums;
				
				hashStep++;
			}
			
			List<String> list = dataMap.get(fileNum);
			
			list.add(url + "\n");
			
			isFullMap.put(fileNum, (isFullMap.get(fileNum))+1);
				
			if(list.size() % 1000 == 0){//达到一定的容量再写入对象流，即写入小文件中
				
				BufferedWriter writer = bwMap.get(fileNum);
				
				for(String line : list){
					
					writer.write(line);
					
				}
				
				writer.flush();
				
				list.clear();
				
			}
			
			url = br.readLine();
			
		}
		
		for(int fn : bwMap.keySet()){//有的链表容量没有达到1000的，将这些数据写入到小文件中
			
			List<String> list = dataMap.get(fn);
			
			BufferedWriter writer = bwMap.get(fn);
			
			for(String line : list){
				
				writer.write(line);
				
			}
			
			list.clear();
			
			writer.flush();
			
			writer.close();
			
		}
		
		bwMap.clear();
		
		fr.close();
		
		br.close();
		
	}

	//分析统计，找出次数前100的IP
	public void analysis(File logSplit) throws Exception{
		
		FileReader fr = new FileReader(logSplit);
		
		BufferedReader br =new BufferedReader(fr);
		
		String url = br.readLine();
		
		//临时temp1存储当前文件所有IP
		Set<URL> temp1 = new TreeSet<URL>();
		
		while(url != null){
			
			url = url.trim();//去掉两边的空格
			
			temp1.add(new URL(url,1));
			
			url = br.readLine();
			
		}
		
		br.close();
		
		fr.close();
		
		//提取temp1存储当前文件访问次数前100的IP并将其与set合并
		int i=0;
		
		for (URL o : temp1) {
			
			i++;
			
			set.add(o);
		
			if (i==100) {
				
				break;
				
			}
			
		}
		
		temp1=null;
		
	}
	
}
