package com.fasset.service.interfaces.generic;

import java.util.Locale;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import com.fasset.utils.StringUtil;

public class AbstractService {

	@Value("${db_datePattern}")
	private String defaultPattern;

	@Autowired
	protected MessageSource messages;

	protected String getMess(String key) {
		return getMess(key, null);
	}

	protected String getMess(String key, Object[] args) {
		return getMess(key, args, null);
	}

	protected String getMess(String key, Object[] args, Locale locale) {
		String ret = "";
		try {
			ret = messages.getMessage(key, args, locale);
		} catch (Exception e) {
			ret = key;
		}
		return ret;
	}

	protected LocalDate convertStr2LD(String date) {
		String pattern = getMess("pattern.date");
		if ("pattern.date".equals(pattern)) {
			pattern = defaultPattern;
		}
		return StringUtil.convertStr2LD_NullDateEQToday(date, pattern);
	}

	protected String convertLD2Str(LocalDate date) {
		String pattern = getMess("pattern.date");
		if ("pattern.date".equals(pattern)) {
			pattern = defaultPattern;
		}
		return StringUtil.convertLD2Str(date, pattern);
	}

	public void setPrivateName(String param) {
		defaultPattern = param;
	}

	public String getDefaultPattern() {
		return defaultPattern;
	}	
	
}
