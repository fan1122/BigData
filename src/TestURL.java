import java.io.*;
import java.util.Date;
public class TestURL {
	
	public static void main(String[] args) throws Exception{
		
		//����ģ����־�ļ�
		LogFileAnalysis logAnalysis = new LogFileAnalysis();
		
		File log = new File("E:\\bigdata\\Dataset.txt");
		
		log.createNewFile();
		
		logAnalysis.creatLog(log, 10000);//����10000��URL
		
		//�ָ���־�ļ�
		logAnalysis.splitLog(log, 1024);

		//�����ļ�
		File logSplits = new File("E:\\bigdata\\logSplit");
		
		for (File logSplit : logSplits.listFiles()) {
			
			logAnalysis.analysis(logSplit);
			
		}
		
		int i=0;
		
		for (URL o : logAnalysis.set) {
			
			i++;
			
			System.out.println(o.url+"---"+o.nums);
			
			if(i==100)break;
			
		}
		
	}
	
}
