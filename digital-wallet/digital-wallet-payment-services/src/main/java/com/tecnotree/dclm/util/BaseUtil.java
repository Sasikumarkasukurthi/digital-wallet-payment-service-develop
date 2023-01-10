package com.tecnotree.dclm.util;

 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;





public class BaseUtil {

	/**
	 * @author oohareddy
	 *
	 */
	
	 static final String[] ones ={"", "One", "Two", "Three", "Four", "Five","Six", "Seven", "Eight", "Nine", "Ten",
     		"Eleven", "Twelve","Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
     static final String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty","Ninety"};
  
	public static final String EMPTY_STRING 		 =  " ";
    public static final int ZERO 					 = 0;
    public static final String SPACE		 		 = " ";



    public static String getStr(Object obj) {
        if (obj != null) {
        	
            return obj.toString().trim();
        } else {
            return EMPTY_STRING;
        }
    }
   
    /***
     * This method will return a trim string, if object passing is null
     * it will return EMPTY STRING (""). if the data is longer than truncate size,
     * to will return truncated value.
     * @param obj Object that are passed in
     * @param truncateSize Max length of data to be returned
     * @return a string representation of the object
     * @exception Exception if have error occur
     * 
     * @author Vinod
     * @created on 10/10/2016
     */
    public static String getStr(Object obj, int truncateSize) {
        if (obj != null) {
            String data = obj.toString().trim();
            if (data.length() > truncateSize)
            	return data.substring(0, truncateSize);
            else
            	return data;
        } else {
            return EMPTY_STRING;
        }
    }
    
    /**
     * This method will return a trim string, if object passing is null
     * it will return null. This function needed as JMS functions
     * need to maintain the null values rather than empty string
     * @param obj Object that are passed in
     * @return a string representation of the object
     */
    public static String getStrWithNull(Object obj) {
        if (obj != null) {
            return obj.toString().trim();
        } else {
            return null;
        }
    }
    
    /**
     * This method is same with getStrWithNull()
     * The returned string will be either in UPPERCASE or null values
     * @param obj Object that are passed in
     * @return a string representation of the object with UPPERCASE
     */
    public static String getStrUpperWithNull(Object obj) {
        if (obj != null) {
            return obj.toString().trim().toUpperCase();
        } else {
            return null;
        }
    }
    
    public static boolean isString(Object obj){
    	String str = BaseUtil.getStr(obj);
    	boolean isStr = false;
    	if (obj != null) {
    		char[] chrArr = str.toCharArray();
    		for(int i=0;i<chrArr.length;i++){
    			if(Character.isLetter(chrArr[i])){
    				isStr = true;
    			}
    		}
            return isStr;
        } else {
            return isStr;
        }
    }

    public static Integer getInt(Object obj) {
        if (obj != null) {
        	try {
        		return Integer.valueOf(obj.toString().trim());
        	}
        	catch(Exception e){
        		return ZERO;
        	}
            
        } else {
            return ZERO;
        }
    }

    /**
	 * This method will return a double value based on decimal format pattern.
	 * @param obj		the number to obtain double value
	 * @param pattern	decimal format pattern. eg. ##.##
	 * @author Vinodkumar
     * @created on 10/10/2016
	 */
	public static Double getDouble(Object obj, String pattern) {
		if (obj != null) {
			try {
				double d = Double.parseDouble(obj.toString().trim());
				
				if (!isObjNull(pattern)) {
					DecimalFormat df = new DecimalFormat(pattern);
					return Double.valueOf(df.format(d));
				} else {
					return d;
				}
			} catch (Exception e) {
				return (double) 0;
			}
		} else {
			return (double) 0;
		}
	}

	/**
	 * This method will return a double value.
	 * @param obj	the number to obtain double value
	 * @author Vinodkumar
     * @created on 10/10/2016
	 */
	public static Double getDouble(Object obj) {
		return getDouble(obj, null);
	}
	
    public static String getStrUpper(Object obj) {
        if (obj != null) {
            return obj.toString().trim().toUpperCase();
        } else {
            return EMPTY_STRING;
        }
    }

    public static String getStrLower(Object obj) {
        if (obj != null) {
            return obj.toString().trim().toLowerCase();
        } else {
            return EMPTY_STRING;
        }
    }
    
    /**
	 * This method will return string to Title Case
	 * 
	 * @param obj
	 * @return String in Title Case
	 * 
	 * @author Vinodkumar
     * @created on 10/10/2016
	 */
    public static String getStrTitle(Object obj) {
    	 if (!isObjNull(obj)) {
    		 String modStr = obj.toString().trim().toLowerCase();
    		 String[] strArr = modStr.split(SPACE);    	
    		 String newStr = EMPTY_STRING;
    		 for (int i = 0; i < strArr.length; i++) {
    			 strArr[i] = Character.toUpperCase(strArr[i].charAt(0)) + strArr[i].substring(1);
    			 newStr+= strArr[i] + SPACE;
    		 }
             return newStr.trim();
         } else {
             return EMPTY_STRING;
         }
    }
    
   
    public static boolean isMatching(String oriSrc, String compareSrc) {
        if (oriSrc != null && getStr(oriSrc).equals(getStr(compareSrc))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEquals(String oriSrc, String compareSrc) {
        if (oriSrc != null && getStr(oriSrc).equals(getStr(compareSrc))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEqualsCaseIgnore(String oriSrc, String compareSrc) {

        if (oriSrc != null && getStr(oriSrc).equalsIgnoreCase(getStr(compareSrc))) {
            return true;
        } else {
            return false;
        }
    }

    
    public static String getSQLContain(String input) {
        return "%" + getStr(input) + "%";
    }

    public static String getSQLStartWith(String input) {
        return getStr(input) + "%";
    }

    public static String getSQLEndWith(String input) {
        return "%" + getStr(input);
    }

    public static Integer getListSize(List o) {
        if (o != null && o.size() > 0) {
            return o.size();
        } else {
            return ZERO;
        }
    }

    public static Boolean isListNull(List o) {
        if (o != null && getListSize(o) > 0) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    
    public static Boolean isListZero(List o) {
        if (!isListNull(o) && o.size()==0 ) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static String genTwoAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "00".substring(genNo.length()) + genNo;
    }

    public static String genThreeAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "000".substring(genNo.length()) + genNo;
    }

    public static String genFourAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "0000".substring(genNo.length()) + genNo;
    }

    public static String genFiveAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "00000".substring(genNo.length()) + genNo;
    }

    public static String genSixAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "000000".substring(genNo.length()) + genNo;
    }
    
    public static String genSevenAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "0000000".substring(genNo.length()) + genNo;
    }

    public static String genEightAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "00000000".substring(genNo.length()) + genNo;
    }
    
    public static String genNineAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "000000000".substring(genNo.length()) + genNo;
    }
    
    public static String genTenAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "0000000000".substring(genNo.length()) + genNo;
    }
    
    public static String genTwelveAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "000000000000".substring(genNo.length()) + genNo;
    }
    
    public static String genFifteenAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "000000000000000".substring(genNo.length()) + genNo;
    }
    
    public static String genSixteenAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "0000000000000000".substring(genNo.length()) + genNo;
    }
    
    public static String genEightteenAutoId(int currentNo) {
        String genNo = "" + currentNo;
        return "000000000000000000".substring(genNo.length()) + genNo;
    }

    public static Boolean isMaxNoReached(Integer supplyNo, Integer compareNo) {
        return (supplyNo > compareNo) ? true : false;
    }

    public static String getCurrency(double input) {
        DecimalFormat Currency = new DecimalFormat("###,##0.00");
        return Currency.format(input);
    }

    public static String getCurrency(BigDecimal input) {
        DecimalFormat Currency = new DecimalFormat("###,##0.00");
        return Currency.format(input);
    }

    public static String getComboBox(String comboName, ArrayList comboData) {
        StringBuffer comboBox = new StringBuffer();
        comboBox.append("<select name=\"" + comboName + "\" id=\"" + comboName + "\">\n");
        comboBox.append("<option value=\"\">--select--</option>\n");
        for (int i = 0; comboData != null && i < comboData.size(); i++) {
            String output = BaseUtil.getStr(comboData.get(i));
            String[] comboxData = output.substring(1, output.length() - 1).split("\\, ");
            comboBox.append("<option value=\"" + comboxData[0] + "\">");
            comboBox.append(comboxData[1]);
            comboBox.append("</option>\n");
        }
        comboBox.append("</select>");
        return BaseUtil.getStr(comboBox);
    }

    public static String getMessage(String msgType, String msgInfo) {
        //info, .success, .warning, .error
        StringBuffer result = new StringBuffer();
        result.append("<div class=\"" + msgType + "\">");
        result.append(msgInfo);
        result.append("</div>");
        return BaseUtil.getStr(result);
    }

    public static void main2(String[] args) {
    	String serial = null;
        System.out.println("what ==== "+isObjNull(serial));
    }

    public static String getDispAmount(String input) {
        DecimalFormat Currency = new DecimalFormat("###,##0.00");
        return Currency.format(getExactAmount(input));
    }

    public static String getDispAmountWithCent(String input) {
        DecimalFormat Currency = new DecimalFormat("###,##0.00");
        String dispAmount = Currency.format(new Double(getAmountWithCent(input)));
        return dispAmount;
    }

    public static String getAmountWithCent(String input) {
        input = input.trim();
        if (input.isEmpty() || input == null) {
            return "0.00";
        } else if(input.length() < 3){
            return "0."+paddingString(input,2, '0', true);
        } else {
            String test3 = input.substring(0, input.length() - 2);
            String test2 = input.substring(input.length() - 2);
            String dispAmount = test3 + "." + test2;
            return dispAmount;
        }

    }

    public static double getExactAmount(String input) {
        double result = 0d;
        try {
            if (input != null && input.trim().length() > 2) {
                result = Double.parseDouble(input.substring(0, input.length() - 2));
            } else {
                result = 0d;
            }
        } catch (IndexOutOfBoundsException e) {
            result = 0d;
        }
        return result;
    }

    public static String getExactAmountHostLength(Double inputDouble, int lengthZero) {
        String amount = "";
        String genZero = "";
        String input = String.valueOf(inputDouble);

        if ((input.substring(input.length() - 2, input.length() - 1)).equalsIgnoreCase(".")) {
            input = input + "0";
        }
        amount = (input.substring(0, input.length() - 3)) + (input.substring(input.length() - 2, input.length()));
        for (int i = 0; i < (lengthZero - amount.length()); i++) {
            genZero += "0";
        }
        amount = genZero + amount;
        return amount;
    }

    public static String getExactAmountHost(Double inputDouble) {
        String amount = "";
        String input = String.valueOf(inputDouble);

        if ((input.substring(input.length() - 2, input.length() - 1)).equalsIgnoreCase(".")) {
            input = input + "0";
        }
        amount = (input.substring(0, input.length() - 3)) + (input.substring(input.length() - 2, input.length()));

        return amount;
    }

    public static int getRecordStartNo(String pageNo, int maxNo) {
        return ((pageNo != null && !pageNo.equalsIgnoreCase("") ? Integer.parseInt(pageNo) : 1) - 1) * maxNo;
    }

    public static boolean isObjNull(Object obj) {

        if (obj != null && getStr(obj).length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String paddingString(String s, int n, char c, boolean paddingLeft) {
        if (s == null) {
            return s;
        }
        int add = n - s.length(); // may overflow int size... should not be a problem in real life
        if (add <= 0) {
            return s;
        }
        StringBuilder str = new StringBuilder(s);
        char[] ch = new char[add];
        Arrays.fill(ch, c);
        if (paddingLeft) {
            str.insert(0, ch);
        } else {
            str.append(ch);
        }
        return str.toString();
    }

    public static String removeFrontZero(String input){
        int i = 0;
        for (i = 0; i < input.length(); i++) {
            if(input.charAt(i) != '0' ){
                break;
            }
        }
        return ((i==(input.length())) ? "0" : input.substring(i));
    }

    public static String genPaymentId(String brchNo, String currentDate, int txnNo) {
        return getStr(new StringBuffer().append(brchNo).append(currentDate).append(genSixAutoId(txnNo)));
    }

    public static String genBsnNo(int bsnNo) {
        return genSixAutoId(bsnNo);
    }

    public static String genRequestId(int bsnNo) {
        return genBsnNo(bsnNo);
    }


    public static String padRightBraces(String s, int n) {
        return paddingString(s, n, '}', false);
    }

    public static String padLeftBraces(String s, int n) {
        return paddingString(s, n, '}', true);
    }
    
    
    //Lud
    
    public static String convIntegerToString(Integer input){
    	return Integer.toString(input);    
    }
    
    /**
     * Copies a range of characters into a new String.
     *
     * @param input String need to be sub string
     * @param start the offset of the first character
     * @param end the offset one past the last character
     * 
     * @return	a new String containing the characters from start to end - 1
     *
     * @author Vinodkumar
     * @created on 10/10/2016
     */
    public static String subString (String input, int start, int end) 
    {
		if (input != null){
		    return getStr(input).substring(start, end);	
		}
		else {
		    return EMPTY_STRING;
		}
    }
    
    /**
     * Copies a range of characters into a new String from 0 to 2 characters.
     *
     * @param input String need to be sub string
     * 
     * @return	a new String containing the characters from 0 to 2 characters
     *
     * @author Vinodkumar
     * @created on 10/10/2016
     */
    public static String subStringZeroTwo (String input) 
    {
    	return subString(input, 0, 2);
    }
    
    /**
     * Copies a range of characters into a new String.
     *
     * @param input String need to be sub string
     * @param start the offset of the first character
     * 
     * @return	a new String containing the characters from start to the end
     *		of the string
     *
     * @author Vinodkumar
     * @created on 10/10/2016
     */
    public static String subString (String input, int start) 
    {
		if (input != null){
		    return getStr(input).substring(start);	
		}
		else {
		    return EMPTY_STRING;
		}
    }
    
    /**
     * Substring html parameter and turn into map object.
     *
     * @param input String need to be sub string
     * 
     * @return	a new Map object
     * @author Vinodkumar
     * @created on 10/10/2016
     */
    public static Map<String,Object> subStringHtmlParameter (String input) 
    {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	if(input.split("\\?").length > 0){
       	   String params = input.split("\\?")[1];
	       	 for(int i=0; i<params.split("&").length;i++){ 
	       		 String param = params.split("&")[i];
	       		 paramMap.put(param.split("=")[0],param.split("=")[1]);
	       	 }
        }
    	return paramMap;
    } 
    
    /**    
     * Check the input string with Regular Expression pattern.
     *
     * @param input String need to be checked
     * @param pattern The pattern of Regular Expression
     * 
     * @return	The boolean result after checking  with Regular Expression pattern
     * @author Vinodkumar
     * @created on 10/10/2016
     */
    public static boolean isMatchRegex(String input, String pattern) {
        boolean match = false;
        
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        if (m.matches()) match = true;
        
    	return match;
    }
    
    /**    
     * Strip all HTML Tags, leaving only content
     *
     * @param input String containing HTML Tags that need to be stripped
     * @param isRemoveNonBreakingSpace Indicate to remove <quote>&nbsp;</quote> in the content
     * 
     * @return	String content without HTML Tags
     * @author
     * @author Vinodkumar
     * @created on 10/10/2016
     */
    public static String stripHtmlTag(String input, boolean isRemoveNonBreakingSpace) {
            	
    	//boolean match = false;
       
    	//Pattern p = Pattern.compile("s/<(.*?)>//g");
    	//Matcher m = p.matcher(input);
    	//return m.replaceAll("");
    	String result = input.replaceAll("\\<.*?\\>", "");
    	if (isRemoveNonBreakingSpace) result = result.replaceAll("&nbsp;", "");
    	return result;
    	/*
        Pattern p = Pattern.compile("s/<(.*?)>//g");
       
        Matcher m = p.matcher(input);
        
        if (m.matches()) match = true;
        
    	return match;
    	*/
    }
    
    // 
    
    /**
     * Convert Object value to BigDecimal value. 
     * Object can be any String, Double, Integer, Long, etc
     *
     * @param input Object which need to be convert to BigDecimal
     * 
     * @return A BigDecimal value for specified Object
     *
     * @author Vinodkumar
     * @created on 10/10/2016       
     */
    public static BigDecimal getBigDecimal(Object input) 
    {  
        try {
        	if (input instanceof String) 
        		return new BigDecimal((String) input);
        	else if (input instanceof Double) 
        		return new BigDecimal((Double) input);
        	else if (input instanceof Integer) 
        		return new BigDecimal((Integer) input);
        	else if (input instanceof Long) 
        		return new BigDecimal((Long) input);
        	else if (input instanceof BigInteger) 
        		return new BigDecimal((BigInteger) input);
        	else if (input instanceof Float) 
        		return new BigDecimal((Float) input);
        	else if (input instanceof Short) 
        		return new BigDecimal((Short) input);
        	else if(input instanceof BigDecimal)
        		return (BigDecimal) input;
        	else
        		return new BigDecimal(ZERO);
        } catch (Exception e) {
            return new BigDecimal(ZERO);
        }		
    }
    /**
     * Convert Object value to byte[] value. 
     *
     * @param input Object which need to be convert to byte[]
     * 
     * @return A byte[] value for specified Object
     *
     * @author Vinodkumar
     * @created on 10/10/2016
     */
    public static byte[] getBytes(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		return buf;
	}
    
    /**
     * Extend getBigDecimal to get value after divided by 100. 
     * Object can be any String, Double, Integer, Long, etc
     *
     * @param input Object which need to be convert to BigDecimal
     * 
     * @return A BigDecimal value for specified Object after divided by 100
     *
     * @author Vinodkumar
     * @throws Exception 
     * @created on 10/10/2016
     */
    public static BigDecimal getBigDecimalDivideBy100(Object input) throws Exception 
    {
    	BigDecimal data = new BigDecimal(0);
    	try{
    		data = getBigDecimal(input).divide(new BigDecimal(100));
    	}
    	catch (Exception e)
    	{
    		//e.printStackTrace();
    		throw new Exception(e.getMessage(),e);
    	}
    	return data;
    }
    
    public static short getShort(Object obj) throws Exception {
        if (obj != null) {
        	short data = 0;
            try{
            	data = Short.parseShort(obj.toString().trim());
            }
            catch (Exception e)
            {
            	//e.printStackTrace();
            	throw new Exception(e.getMessage(),e);
            }
            return data; 
        } else {
            return ZERO;
        }
    }
    
    public static String getStateCodeFromBranchCode(String branchCode)
    {
    	if (getStr(branchCode).equals(EMPTY_STRING))
    		return EMPTY_STRING;
    	else
    		return subString(branchCode, 2, 4);
    }   
    
	
    /**
     * Convert Hex String to Bytes Array. Used for converting Hex String from DB to an actual binary data of a file.
     *
     * @param input String in Hex format, which represent the actual binary data of a file.
     * 
     * @return	array of bytes of actual binary data of a file.
     *
     * @author Vinodkumar
     * @created on 10/10/2016
     */
	public static byte[] hexStringToByteArray(String input) {
        int len = input.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                    + Character.digit(input.charAt(i+1), 16));
        }
        return data;
    }
	
	/**
     * Convert Bytes Array to Hex String. Used to store binary data of a file into DB as Hex String format.
     *
     * @param input array of bytes, the actual binary data of a file.
     * 
     * @return	Hex format string that represent the binary data of a file.
     *
     * @author Vinodkumar
     * @created on 10/10/2016        
     */
	public static String byteArrayToHexString(byte[] input){
        String result = "";
        for (int i=0; i < input.length; i++) {
            result +=
                Integer.toString( ( input[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
	
	/**
	 * Check if the email address is valid (Required JavaMail class) 	
	 * @param email The email address to be checked
	 * @return True is valid email address, False otherwise
	 * @author Vinodkumar
     * @created on 10/10/2016
	 */
	/*public static boolean isValidEmailAddress(String email)
	{
		try {
			new InternetAddress(email, true);
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		}
		return true;
//		boolean result = false;
//		if (!BaseUtil.isObjNull(email))
//			result = email.matches("/[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}/");
//		return result;
	}*/
	
	
	/**
	 * Check whether the given phone number is a Mobile Phone number.<br>
	 * Mobile Phone prefix will be first 3 or 4 characters after removing 7 digit suffix.<br>
	 * <br>
	 * Note: Will removed dash, dot and spaces from the given phone number automatically before checking.
	 * 
	 * @param phoneNo Phone Number to check
	 * @return true if the given phone number is Mobile Phone, false if otherwise.
	 * 
	 * @author Vinodkumar
     * @created on 10/10/2016
	 */
	
	
	
	private static boolean validateMyKadNo(String icNo) {
		
		if (icNo != null && !"".equals(icNo.trim())) {
		
        int i, nICNo = 0;
        int nTotal = 0;
        int[] weight = {2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 0};
        int LEN = icNo.length();

        if (icNo != null && !"".equals(icNo.trim())) {
            if (LEN != 12) {
                return false;
            }

            if (((icNo.charAt(2) == '0') && (icNo.charAt(3) != '0'))
                    || ((icNo.charAt(2) == '1') && (icNo.charAt(3) <= '2'))) {
                for (i = 0; i < LEN; i++) {
                    char cICNo = icNo.charAt(i);
                    nICNo = cICNo - '0';
                    nTotal += Character.getNumericValue(cICNo) * weight[i];
                }
            }

            int nRemainder;
            int nCheckDigit;

            nRemainder = nTotal % 11;

            int nK = 1 - nRemainder;

            if (nK > -1) {
                nCheckDigit = nK;
            } else {
                nCheckDigit = nK + 11;
            }

            if (nK != -1 && nCheckDigit == nICNo) {
                return true;
            } else {
                return false;
            }
        }
		}
        return true;
    }
	
	
	public static boolean getValidMyKadNO(String icNo){
		boolean result = false;
		
		// Check for 7 digit numeric
		if(!BaseUtil.isObjNull(icNo) && icNo.length() == 7){
			result = BaseUtil.isMatchRegex(icNo, "\\d{1,7}");
			
		// Check for 8 digit characters (1 alphanumeric 7 numeric)
		} else if(!BaseUtil.isObjNull(icNo) && icNo.length() == 8){
			if(BaseUtil.isString(icNo.substring(0, 1)) 
					&& BaseUtil.isMatchRegex(icNo.substring(1, 8), "\\d{1,7}")) {
				String firstChar = icNo.substring(0, 1);
				if(BaseUtil.isEqualsCaseIgnore("A", firstChar)
						|| BaseUtil.isEqualsCaseIgnore("B", firstChar)
						|| BaseUtil.isEqualsCaseIgnore("H", firstChar)
						|| BaseUtil.isEqualsCaseIgnore("K", firstChar)) {
					return true;
				}
			} 

			result = false;
			
		// Check for New MyKad
		} else {
			result = validateMyKadNo(icNo);
		}
		
		return result;
	}
	
	public static boolean getValidMyKadFormat(String icNo) {
        boolean result = false;

        int icLen = BaseUtil.isObjNull(icNo) ? 0 : icNo.length();
        
        // 21-09-2012 (Hazman): New Logic
        if (icLen == 12) {
        	result = isNumber(icNo);
        }
        else if (icLen == 7) {
        	String firstChar = icNo.substring(0, 1);
        	if (	BaseUtil.isEqualsCaseIgnore("B", firstChar)
        			|| BaseUtil.isEqualsCaseIgnore("H", firstChar)
              		|| BaseUtil.isEqualsCaseIgnore("K", firstChar)
              		|| isNumber(firstChar)) {
        		String balanceChar = icNo.substring(1);
        		result = isNumber(balanceChar);
        	}
        }
        else if (icLen == 8) {
        	String firstChar = icNo.substring(0, 1);
        	if (	BaseUtil.isEqualsCaseIgnore("A", firstChar)
        			|| BaseUtil.isEqualsCaseIgnore("H", firstChar)
              		|| BaseUtil.isEqualsCaseIgnore("K", firstChar)) {
        		String balanceChar = icNo.substring(1);
        		result = isNumber(balanceChar);
        	}
        }

        // Old Logic
//        // Check for 7 digit numeric
//        if (icLen == 7) {
//            result = BaseUtil.isMatchRegex(icNo, "\\d{1,7}");
//        } // Check for 12 digit numeric
//        else if (icLen == 12) {
//            result = isNumber(icNo);
//        }// Check for 8 digit characters (1 alphanumeric 7 numeric) 
//        else if (icLen == 8) {
//            if (BaseUtil.isString(icNo.substring(0, 1))
//                    && BaseUtil.isMatchRegex(icNo.substring(1, 8), "\\d{1,7}")) {
//                String firstChar = icNo.substring(0, 1);
//                if (BaseUtil.isEqualsCaseIgnore("A", firstChar)
//                        || BaseUtil.isEqualsCaseIgnore("B", firstChar)
//                        || BaseUtil.isEqualsCaseIgnore("H", firstChar)
//                        || BaseUtil.isEqualsCaseIgnore("K", firstChar)) {
//                    return true;
//                }
//            }
//
//            result = false;
//
//        } else {
//            return false;
//        }

        return result;
    }	
	
	
	public static boolean isNumber(String str) {
        String s = str;
        for (int i = 0; i < s.length(); i++) {
        //If we find a non-digit character we return false.
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
	
//	public static String generateTempFile(String fileName, String folderPath)
//	{
//		File tempFile;
//		String fileFullPath = "";
//		if (BaseUtil.isEquals(folderPath, BaseConstant.EMPTY_STRING))
//		{
//			tempFile = tempFile.cr
//		}
//		 
//	}
	
	
	public static String wordWrap(String text, int width) {
	    StringBuilder buf = new StringBuilder(text);
	    int lastspace = -1;
	    int linestart = 0;
	    int i = 0;

	    while (i < buf.length()) {
	       if ( buf.charAt(i) == ' ' ) lastspace = i;
	       if ( buf.charAt(i) == '\n' ) {
	          lastspace = -1;
	          linestart = i+1;
	          }
	       if (i > linestart + width - 1 ) {
	          if (lastspace != -1) {
	             buf.setCharAt(lastspace,'\n');
	             linestart = lastspace+1;
	             lastspace = -1;
	             }
	          else {
	             buf.insert(i,'\n');
	             linestart = i+1;
	             }
	          }
	        i++;
	       }
	    return buf.toString();
	}
	
	
	public static String hexStringToString(String input) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < input.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = input.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			
			// convert the decimal to character
			sb.append((char) decimal);

			//System.out.println("" + i + ") Hex: " + output + " --- Decimal: " + decimal + " --- Char: " + (char) decimal);
			
			temp.append(decimal);
		}
//		System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}
	
	
	public static String stringToHexString(String input) {

		char[] chars = input.toCharArray();

		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			String hexData = Integer.toHexString((int) chars[i]);
			if (hexData.length() == 1) hexData = "0" + hexData;
			//hex.append(Integer.toHexString((int) chars[i]));
			hex.append(hexData);
//			System.out.println("" + i + ") Chars: " + chars[i] + " --- Hex: " + Integer.toHexString((int) chars[i]));
		}

		return hex.toString();
	}
	
	/**
     * Convert String to Bytes Array. Used for converting String from DB to an actual binary data of a file.<br>
     * The flow process are converting the DB2 String to Hex String first, then Hex String to Byte array.
     *
     * @param String data from DB2
     * 
     * @return	array of bytes of actual binary data of a file.
     *
     * @author Vinodkumar
     * @created on 10/10/2016
     */
	public static byte[] stringToByteArray(String input) {
		String hexString = stringToHexString(input);
		return hexStringToByteArray(hexString);
	}
	
	/**
     * Convert Bytes Array to String. Used for converting actual binary data of a file to String for DB2.<br>
     * The flow process are converting the Byte array to Hex String first, then Hex String to String.
     *
     * @param array of bytes of actual binary data of a file.
     * 
     * @return String data.
     * 
     * @exception Exception If have problem convert byte array to String
     *
     * @author Vinodkumar
     * @created on 10/10/2016  
     */
	public static String byteArrayToString(byte[] input) {
		String hexString = byteArrayToHexString(input);
		return hexStringToString(hexString);
	}
	
	public static String getCommaSeparated(String[] values, boolean inBrackets){
		StringBuilder result = new StringBuilder();
		if(values!=null && values.length>0){
			if(inBrackets)
				result = result.append('(');
			for(String value:values){
				result = result.append(',').append(value).append(',');
			}
			result = result.deleteCharAt(result.length()-1);
			if(inBrackets)
				result = result.append(')');
		}
		return result.toString();
		
	}
	
	
	public static HashSet<String> convertStrToSet(String delimitedStr,String delimeter){
		HashSet<String> resultSet = new HashSet<String>();
	
		StringTokenizer tokenizer = new StringTokenizer(delimitedStr, delimeter);
		while(tokenizer.hasMoreTokens()){
			resultSet.add(tokenizer.nextToken());
			
		}
		return resultSet;
	}
	
	
	public static HashSet<String> convertListToSet(List<String> strList){
		HashSet<String> resultSet = new HashSet<String>();
	
		Iterator<String> itr = strList.iterator();
		while(itr.hasNext()){
			resultSet.add(itr.next());
		}
		return resultSet;
	}
	
	
	public static String removeInvalidComma(String sourceString) {

		StringBuilder result = new StringBuilder();

		String tempArray[] = null;

		if (sourceString != null) {
			tempArray = sourceString.split(",");

			for (int i = 0; i < tempArray.length; i++) {
				String s = tempArray[i].trim();

				if (s.trim().length() > 0) {
					result.append(s.trim()).append(", "); // comma with one space requested
				}
			}

			if (result.length() >= 3) {
				sourceString = result.subSequence(0, result.length() - 2)
						.toString();
			}

		}
		return sourceString;

	}
	
	public static String getTwoDigtsValue(double amount) {
		
	    DecimalFormat df = new DecimalFormat("#.00");
	    String angleFormated = df.format(amount);
//	    System.out.println(angleFormated); //output 20.30
		
		return angleFormated;
	}
	
	public static StringBuffer maskAccNumber(String accNumber)
	{
	System.out.println("Length of account number is "+accNumber.length());
    int i = 0;
    StringBuffer temp = new StringBuffer();
    while(i < (accNumber .length()))
    {
        if(i > accNumber.length() -5)
        {
            temp.append(accNumber.charAt(i));
        } else 
        {
            temp.append('x');
        }
        i++;
    }
       System.out.println(temp);
       return temp;
       }
	public static long simpleConversion(double n)	    
	 {     
	        long a=(long) n;//99
	        double p=(n+0.001);
	        double p1=(p-a)*100;
	        //System.out.println(p1);
	        System.out.println("Rs : "+a);
	        	int a1=(int) p1;
	        System.out.println("paise :" +a1);
	       long a2=a*100;
	        System.out.println(a2);
	        long finalValInPaisas =a2+a1;
	        System.out.println("Total rupees in paisa "+finalValInPaisas);
	   return finalValInPaisas;
	}
	
	public static long simpleConversionrupee(double n)	    
	 {     
	        long a=(long) n;//99
	        double p=(n+0.001);
	        double p1=(p-a)*100;
	        //System.out.println(p1);
	        System.out.println("Rs : "+a);
	        	int a1=(int) p1;
	        System.out.println("paise :" +a1);
	       long a2=a*100;
	        System.out.println(a2);
	        long finalValInRupee =a2+'.'+a1;
	        System.out.println("Total rupees in rupee "+finalValInRupee);
	   return finalValInRupee;
	}   
	
	public static String getHumanReadablePriceFromNumber(long number){

	    if(number >= 1000000000){
	        return String.format("%.2f Cr", number/ 1000000000.0);
	    }

	    if(number >= 1000000){
	        return String.format("%.2f L", number/ 1000000.0);
	    }

	    if(number >= 100000){
	        return String.format("%.2f K", number/ 100000.0);
	    }

	    if(number >=1000){
	        return String.format("%.2f K", number/ 1000.0);
	    }
	    return String.valueOf(number);

	}
	
	public static StringBuffer maskMobNumber(String mobNumber)
	{
	System.out.println("Length of account number is "+mobNumber.length());
    int i = 0;
    StringBuffer temp = new StringBuffer();
    while(i < (mobNumber .length()))
    {
        if(i > mobNumber.length() -4)
        {
            temp.append(mobNumber.charAt(i));
        } else 
        {
            temp.append('x');
        }
        i++;
    }
       System.out.println(temp);
       return temp;
       }
	
	
	
	private static String ConvertOnesTwos(String t)
    {
      
    String word="";
    int num=Integer.parseInt(t);
    if (num%10==0) word=tens[num/10]+""+word ;
    else if (num<20) word=ones[num]+""+word ;
    else
    {
        word=tens[(num-(num%10))/10]+word ;
        word=word+" "+ones[num%10] ;
    }
    return word;
    }
	
	// **RD formula**//*
	public static double RDCalculation(double r, double p, double t) {
		double sum = 0, sum1 = 0;
		for (double i = 1; i <= t; i++) {
			sum = p + sum;

		}

		sum1 = (double) (p * ((t * (t + 1)) / 24) * (r / 100));
		sum = sum + sum1;
		return Math.round(sum);

	}
	
		
	}
