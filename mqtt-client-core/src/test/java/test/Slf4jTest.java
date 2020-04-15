package test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jTest {

	private static Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
	public static void main(String[] args) {
		logger.error("普通的日志记录");
		//{}占位符记录日志
		for(int i=0;i<3;i++){
			logger.info("这是第{}条记录",i);
			System.out.println("hh");
		}
		//用\转义
		logger.info("Set \\{} differs from {}","3");
		//两个参数
		logger.info("两个占位符，可以传两个参数{}---{}",1,2);
	}
}

