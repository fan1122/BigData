import java.io.*;
import java.util.Date;
public class TestURL {
	
	public static void main(String[] args) throws Exception{
		
		//生成模拟日志文件
		LogFileAnalysis logAnalysis = new LogFileAnalysis();
		
		File log = new File("E:\\bigdata\\Dataset.txt");
		
		log.createNewFile();
		
		logAnalysis.creatLog(log, 10000);//生成10000个URL
		
		//分割日志文件
		logAnalysis.splitLog(log, 1024);

		//分析文件
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
