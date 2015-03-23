package tion;

public class ReverseInteger {
	public static long reverse(long x) {
        boolean zhengshu = true;
        
        if(x<0){
            x = 0 - x;
            zhengshu = false;
        }else if(x==0){
            return 0;
        }
        String num = Long.toString(x);
        String num2 = "";
        for(int i = 0;i < num.length();i++){
            num2  += num.charAt(num.length()-i-1); 
        }
        //Be careful of OverflowÐ¡ÐÄÒç³ö
        if(Long.parseLong(num2)>Integer.MAX_VALUE)
        	return 0;
        
        if(!zhengshu){
            return 0-Long.parseLong(num2);
        }else
            return Long.parseLong(num2);
    }
   public static void main(String[] args) {
   }
}
