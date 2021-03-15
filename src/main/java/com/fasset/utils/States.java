package com.fasset.utils;

public class States {

	private static final String opt1 = "AK|AZ|CA|CO|HI|ID|IL|IN|IA|KS|MI|MN|MO|MT|NE|NV|ND|OH|OR|SD|UT|WA|WI|WY|GU|MP";
	private static final String opt2 = "AL|AR|CT|DE|FL|GA|KY|LA|ME|MD|MA|MS|NH|NJ|NM|NY|NC|OK|PA|RI|SC|TN|TX|VT|VA|WV|DC|PR|VI";

	public static boolean liveIn(String name, int opt) {
		if (opt == 0) {
			if (opt1.contains(name)) {
				return (true);
			}
		} else if (opt == 1) {
			if (opt2.contains(name)) {
				return (true);
			}
		}
		return (false);
	}

}
