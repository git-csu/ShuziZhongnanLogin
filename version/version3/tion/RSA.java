package tion.com;
import java.math.BigInteger;
public class RSA {
	    
	public static String mudulusInHexStr = "a8a02b821d52d3d0ca90620c78474b78435423be99da83cc190ab5cb5b9b922a4c8ba6b251e78429757cf11cde119e1eacff46fa3bf3b43ef68ceb29897b7aa6b5b1359fef6f35f32b748dc109fd3d09f3443a2cc3b73e99579f3d0fe6a96ccf6a48bc40056a6cac327d309b93b1d61d6f6e8f4a42fc9540f34f1c4a2e053445";
	public static String exponentInHexStr = "10001";
	  
	public static  String  getRSAPsd(String passwordInStr){
		
		 String result="";
		
		 BigInteger bigInt_mudulus=new BigInteger(mudulusInHexStr,16);
		 BigInteger bigInt_exponent=new BigInteger(exponentInHexStr,16);
		 BigInteger bigInt_password=new BigInteger(new StringBuilder(passwordInStr).reverse().toString().getBytes());
		 BigInteger bigInt_result=bigInt_password.modPow(bigInt_exponent, bigInt_mudulus);
		 
		 result= bigInt_result.toString(16);		
		 
		 int zeroNum=mudulusInHexStr.length()-result.length();			 
		 for(int i=0;i<zeroNum;i++)
		       result="0"+result;
		 
		 return  result;
	}	
}