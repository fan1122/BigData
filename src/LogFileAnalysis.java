import java.io.*;
import java.security.SecureRandom;
import java.util.*;
public class LogFileAnalysis {
	
	public final long fileCapacity=100;
	
	//����ÿ���ļ���������д���ļ��У�
	public final Map<Integer,BufferedWriter> bwMap = new HashMap<Integer,BufferedWriter>();
	
	//�ָ��ļ���-�洢�൱����֮���ٴ���ĳ���ļ�
	public final Map<Integer,List<String>> dataMap = new HashMap<Integer,List<String>>();
	
	//��¼ÿ��С�ļ��Ƿ�����
	public final Map<Integer,Long> isFullMap=new HashMap<Integer,Long> ();
	
	//�洢���ʴ���ǰ100��IP
	public Set<URL> set = new TreeSet<URL>();

	// ����url�ļ�
	public void creatLog(File log,long d) throws Exception{
		
		FileWriter fw = new FileWriter(log,true);
		
		BufferedWriter bw = new BufferedWriter(fw);
		
		SecureRandom random = new SecureRandom();
		
		for (int i = 0; i < d; i++) {//����d��URL
			
			bw.write("https://Yifan.Zhang.github.io/"+random.nextInt(255)+"\n");//URL
			
			if((i+1) % 1000 == 0){
				
				bw.flush();
				
			}
		}
		
		bw.flush();
		
		fw.close();
		
		bw.close();
	}

	// �ָ���־�ļ�
	public void splitLog(File logflie,int fileNums) throws Exception{
		
		FileReader fr = new FileReader(logflie);
		
		BufferedReader br =new BufferedReader(fr);
		
		String url = br.readLine();//��ȡһ��
		
		//�ȴ����ļ��������󷽱�ʹ��
		for(int i=0;i<fileNums;i++){//ѭ��1024��С�ļ�
			
			File file = new File("E:\\bigdata\\logSplit\\"+ i + ".txt");//�½�С�ļ�
			
			bwMap.put(i, new BufferedWriter(new FileWriter(file,true)));//bwMap��¼�ļ�ֵ��i-������
			
			dataMap.put(i, new LinkedList<String>());//dataMap��¼�ļ�ֵ��i-˫������С�ļ���
			
			isFullMap.put(i, (long) 0);//ÿ���ļ��տ�ʼ��û�г����ڴ��С��1G��
		
		}
		
		while(url != null){
			
			int hashCode = url.hashCode();
			
			hashCode = hashCode < 0 ? -hashCode : hashCode;
			
			int fileNum = hashCode % fileNums;//���IP�Ĺ�ϣֵȥ���Ҷ�Ӧ�Ĵ洢�ļ�
			
			int hashStep=1;
			
			while(isFullMap.get(fileNum)==fileCapacity) {
				
				fileNum = (hashCode +hashStep)% fileNums;
				
				hashStep++;
			}
			
			List<String> list = dataMap.get(fileNum);
			
			list.add(url + "\n");
			
			isFullMap.put(fileNum, (isFullMap.get(fileNum))+1);
				
			if(list.size() % 1000 == 0){//�ﵽһ����������д�����������д��С�ļ���
				
				BufferedWriter writer = bwMap.get(fileNum);
				
				for(String line : list){
					
					writer.write(line);
					
				}
				
				writer.flush();
				
				list.clear();
				
			}
			
			url = br.readLine();
			
		}
		
		for(int fn : bwMap.keySet()){//�е���������û�дﵽ1000�ģ�����Щ����д�뵽С�ļ���
			
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

	//����ͳ�ƣ��ҳ�����ǰ100��IP
	public void analysis(File logSplit) throws Exception{
		
		FileReader fr = new FileReader(logSplit);
		
		BufferedReader br =new BufferedReader(fr);
		
		String url = br.readLine();
		
		//��ʱtemp1�洢��ǰ�ļ�����IP
		Set<URL> temp1 = new TreeSet<URL>();
		
		while(url != null){
			
			url = url.trim();//ȥ�����ߵĿո�
			
			temp1.add(new URL(url,1));
			
			url = br.readLine();
			
		}
		
		br.close();
		
		fr.close();
		
		//��ȡtemp1�洢��ǰ�ļ����ʴ���ǰ100��IP��������set�ϲ�
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
