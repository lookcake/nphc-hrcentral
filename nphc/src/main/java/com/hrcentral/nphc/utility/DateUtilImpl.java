package com.hrcentral.nphc.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DateUtilImpl implements DateUtility {

	private static final Logger logger = LogManager.getLogger();

	public Date getDateFromString(String dateString) {
		String[] requestedPatterns = Global.DATE_FORMAT;

		try {
			Date parsedDate = DateUtils.parseDateStrictly(dateString, requestedPatterns);
			return parsedDate;
		} catch (ParseException e) {
			logger.info(e);
		}
		return null;
	}

	public boolean isValidDate(String dateString) {
		String[] requestedPatterns = Global.DATE_FORMAT;

		try {
			DateUtils.parseDateStrictly(dateString, requestedPatterns);
			return true;
		} catch (ParseException e) {
			logger.info(e);
		}
		return false;
	}

}
