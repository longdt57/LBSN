package object;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FunctionConstant {

	public static Date getDatefromString(String datestr){
        SimpleDateFormat format;
        if(datestr.charAt(datestr.length()-1)=='Z')
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        else
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try{

            Date ep = format.parse(datestr);
            return ep;

        }catch (Exception e){
            MyLog.log(e.toString());
            return new Date(System.currentTimeMillis());
        }
    }
}
