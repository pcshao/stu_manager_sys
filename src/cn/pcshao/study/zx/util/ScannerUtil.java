package cn.pcshao.study.zx.util;

import java.util.Scanner;

public class ScannerUtil {

	static Scanner scan;
	static{
		scan = new Scanner(System.in);
	}
	
	public static Float getFloatNumber(){
		return scan.nextFloat();
	}
	public static int getIntNumber(){
		return scan.nextInt();
	}
	public static String getString(){
		return scan.next();
	}
	public static double getDouble() {
		return scan.nextDouble();
	}

}
class ScanException extends Exception{
	public ScanException(String mess) {
		System.out.println(" ‰»Î≤ª∆•≈‰");
	}
}
