package com.esl.amit.eslapplication.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	public boolean validateFields(String validationString) {

		if (validationString == null || validationString.length() == 0)
			return false;
		else
			return true;
	}

	public boolean emailValidator(String email) {

		String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(email);

		if (matcher.find())
			return true;
		else
			return false;
	}

	public boolean matchPassword(String pwd, String confirmPwd) {

		if (pwd.equalsIgnoreCase(confirmPwd))
			return false;
		else
			return true;
	}
	
	public boolean compareDate(String fromDateStr, String toDateStr){
		Date fromDt = new Date(fromDateStr);
		Date toDt = new Date(toDateStr);
		if(toDt.after(fromDt)){
			return true;
		}
		
		
		return false;
	}
	
	public static String toTitleCase(String input) {
	    StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            nextTitleCase = false;
	        }

	        titleCase.append(c);
	    }

	    return titleCase.toString();
	}
}
