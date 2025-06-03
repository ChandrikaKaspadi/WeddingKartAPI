package weddingKart_GenericUtility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class JavaUtility {
	
	public int getRandomNumber() {
		Random random=new Random();
		int randomNumber=random.nextInt(5000);
		return randomNumber;					
	}
	
	public String getSystemDateYYYYMMDD()
	{
		Date dateObj = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String date=sdf.format(dateObj); 
		return date;
	}

  	public String getRequiredDateYYYYMMDD(int days) {
  		Calendar cal=Calendar.getInstance();
  		cal.add(Calendar.DAY_OF_MONTH,days);
  		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
  		String date=sdf.format(cal.getTime());
  		return date;
		
	}
  	
  	public String getSystemDatePlusOneMonthYYYYMMDD() {
  	    Calendar calendar = Calendar.getInstance();
  	    calendar.add(Calendar.MONTH, 1); // Add one month
  	    Date dateObj = calendar.getTime(); // Get the updated date
  	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    String date = sdf.format(dateObj); 
  	    return date;
  	}
}