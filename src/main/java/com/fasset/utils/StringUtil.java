package com.fasset.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.fasset.controller.abstracts.MyAbstractController;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.XfaForm;

@Component
public class StringUtil {

	public static final int EIGHTEEN = 18;
	public static final int THREE = 3;
	public static final int FIVE = 5;
	public static final int ERROR = -1;
	public static final int YEAR = 365;
	public static final int FIFTY = 50;
	public static final int FIFTYFIVE = 55;
	public static final int SIXTYFIVE = 65;
	public static final int SEVENTYFIVE = 75;
	public static final int FIFTEEN = 15;
	public static final int TWENTY = 20;
	public static final int TWENTYSIX = 26;
	public static final int EDAD_1 = 15;
	public static final int EDAD_2 = 79;
	public static final int FOURTEEN = 14;

	public static final String EMPTY = " ";
	public static final String USA = "USA";
	public static final int NO_TPRS = 0;

	public static boolean isEmpty(String string) {
		return string == null || string.length() < 1 || string.trim().equals("");
	}

	public static boolean like(String str1, String str2) {
		if ((isEmptyOrNullValue(str1)) || (isEmptyOrNullValue(str2))) {
			return false;
		}
		return str1.toLowerCase().contains(str2.toLowerCase());
	}

	public static boolean isNull(String string) {
		return string == null;
	}

	public static boolean isEmptyOrNullValue(String string) {
		return (StringUtil.isEmpty(string) || (string.trim().equalsIgnoreCase("null")));
	}

	public static boolean isEmptyOrNullOrNegValue(String string) {
		return ((isEmptyOrNullValue(string)) || (string.trim().equalsIgnoreCase("-1")));
	}

	public static boolean isEmptyOrNullOrNegOrZeroValue(String string) {
		return ((isEmptyOrNullOrNegValue(string)) || (string.trim().equalsIgnoreCase("0")));
	}

	public static boolean containsDigit(String s) {
		boolean containsDigit = false;

		if (s != null && !s.isEmpty()) {
			for (char c : s.toCharArray()) {
				if (containsDigit = Character.isDigit(c)) {
					break;
				}
			}
		}

		return containsDigit;
	}

	public static List<String> findInQoutes(String str) {
		List<String> ret = new ArrayList<>();
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(str);
		while (m.find()) {
			ret.add(m.group(1));
		}
		return ret;
	}

	public static String subString(String str, int maxSize) {
		if (StringUtil.isEmptyOrNullValue(str)) {
			return "";
		}
		if (str.length() > maxSize) {
			return str.substring(0, maxSize);
		}
		return str;
	}

	public static boolean patternMatch(String value, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(value);
		return (matcher.matches());
	}

	public static String getGenKey() {
		return RandomStringUtils.randomAlphabetic(MyAbstractController.SIZE_THIRTY);
	}

	public static LocalDate convertStr2LD(String date, String pattern) {
		LocalDate ld = null;
		try {
			ld = LocalDate.parse(date, DateTimeFormat.forPattern(pattern));
		} catch (Exception e) {

		}
		return ld;
	}

	public static LocalDate convertStr2LD_NullDateEQToday(String date, String pattern) {
		LocalDate ld = convertStr2LD(date, pattern);
		if (null == ld) {
			ld = new LocalDate();
		}
		return ld;
	}

	public static String convertLD2Str(LocalDate date, String pattern) {
		String ret = "";
		if (null != date) {
			try {
				ret = date.toString(pattern);
			} catch (Exception e) {

			}
		}
		return ret;
	}

	public static String convertLD2Str(LocalDateTime date, String pattern) {
		String ret = "";
		if (null != date) {
			try {
				ret = date.toString(pattern);
			} catch (Exception e) {

			}
		}
		return ret;
	}

	public static String convertD2Str(Date date, String pattern) {
		String returnDate = null;
		SimpleDateFormat simpleDateFormat = null;

		if (date == null) {
			returnDate = EMPTY;
		} else {
			simpleDateFormat = new SimpleDateFormat(pattern);
			returnDate = simpleDateFormat.format(date);
		}

		return returnDate;

	}

	public static String splitWholeWords(String input, int size) {
		return splitWholeWords(input, size, true);
	}

	public static String splitWholeWords(String input, int size, boolean soft) {
		if (input.length() <= size)
			return input;
		int pos = input.lastIndexOf(" ", size);
		if (pos < 0) {
			if (soft) {
				return "";
			} else {
				return input.substring(0, size);
			}
		}
		return input.substring(0, pos);
	}

	public static int[] splitDays(int noOfDays) {
		int[] ret = { 0, 0, 0, 0 };

		ret[0] = noOfDays / 365;
		noOfDays = noOfDays % 365;

		ret[1] = noOfDays / 30;
		noOfDays = noOfDays % 30;

		ret[2] = noOfDays / 7;
		noOfDays = noOfDays % 7;

		ret[3] = noOfDays;
		return ret;
	}

	public static void main(String args[]) throws IOException, DocumentException {
		try {
			// readXFA("F:\\MyLegalSoftware\\Proyecto B\\Formas
			// Nuevas\\I485\\i-485.pdf", "F:\\yo\\i-485.xml");
			// readXFA("F:\\yo\\i-131saved.pdf", "F:\\yo\\i-131.xml");
			PdfReader.unethicalreading = true;

			final PdfReader reader = new PdfReader("C:\\MyLegalSoftware\\Proyecto B\\Formas Nuevas\\I-129F\\i-129f.pdf");

			reader.removeUsageRights();

			final PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("C:\\yo\\i-129fout.pdf"));
			final AcroFields form = stamper.getAcroFields();
			form.removeXfa();
			form.setGenerateAppearances(true);

			Set<String> fields2 = form.getFields().keySet();
			for (String key : fields2) {
				System.out.print(key + ": ");

				switch (form.getFieldType(key)) {
				case AcroFields.FIELD_TYPE_CHECKBOX:
					System.out.println("Checkbox: " + form.getField(key));

					break;
				case AcroFields.FIELD_TYPE_COMBO:
					System.out.println("Combo");
					/*
					 * String combo[] = form.getListSelection(key); for (int i =
					 * 0; i < combo.length; i++){ System.out.println(combo[i]);
					 * } combo = form.getListOptionDisplay(key); for (int i = 0;
					 * i < combo.length; i++){ System.out.println("ed0:" +
					 * combo[i]); } combo = form.getListOptionExport(key); for
					 * (int i = 0; i < combo.length; i++){
					 * System.out.println("ed1:" + combo[i]); }
					 */
					String combo[] = form.getListSelection(key);
					for (int i = 0; i < combo.length; i++) {
						// System.out.println("ed2:" + combo[i]);
					}
					combo = form.getAppearanceStates(key);
					for (int i = 0; i < combo.length; i++) {
						// System.out.println("ed3:" + combo[i]);
					}
					break;
				case AcroFields.FIELD_TYPE_LIST:
					System.out.println("List");
					break;
				case AcroFields.FIELD_TYPE_NONE:
					System.out.println("None");
					break;
				case AcroFields.FIELD_TYPE_PUSHBUTTON:
					System.out.println("Pushbutton");
					break;
				case AcroFields.FIELD_TYPE_RADIOBUTTON:
					System.out.println("Radiobutton");
					break;
				case AcroFields.FIELD_TYPE_SIGNATURE:
					System.out.println("Signature");
					break;
				case AcroFields.FIELD_TYPE_TEXT:
					System.out.println("Text: " + form.getField(key));
					break;
				default:
					System.out.println("?");
				}
			}
			// form.setField("form1[0].#subform[0].P1_checkbox6c_Unit[0]", " APT
			// ");
			for (int i = 34; i <= 66; i++)
				form.setField("F[0].#subform[1].TextField1[" + i + "]", "" + i + "");

			for (int i = 12; i <= 19; i++)
				form.setField("F[0].#subform[1].Date_of_Birth[" + i + "]", "" + i + "");
			form.setField("F[0].#subform[1].Date[0]", "otra");

			// form.setField("form1[0].#subform[8].MarriageEndedExplain[0]",
			// "edu");

			/*
			 * String combo[] = form.getListOptionExport(
			 * "form1[0].#subform[0].P1_checkbox6c_Unit[0]"); for (int i = 0; i
			 * < combo.length; i++) { System.out.println(":" + combo[i]); }
			 */
			/*
			 * form1[0].#subform[1].Line13a[0]: Checkbox: Off
			 * form1[0].#subform[1].Line13b[0]: Checkbox: Off
			 * form1[0].#subform[1].Line13c[0]: Checkbox: Off
			 */

			String[] states = form.getAppearanceStates("form1[0].#subform[5].Line5b_Unit[2]");
			if (null != states) {
				for (int i = 0; i < states.length; i++) { // System.out.print("
															// -
															// "); //
					System.out.println("values: " + states[i]);
				}
			}
			String barc = "";
			AcroFields.FieldPosition location = null;
			
			location = form.getFieldPositions("F[0].#subform[1].Code3of9BarCode1[1]").get(0);
			barc = "FORMI-130REV03-23-15YPAGE2";

			if (null != location) {
				try {
					final PdfContentByte pdfpage = stamper.getOverContent(location.page);
					createBarcodeImage(location, pdfpage, barc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			stamper.setFormFlattening(true);

			stamper.close();
			reader.close();
			System.out.println("fin");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void readXFA(String src, String dest) {
		try {
			FileOutputStream os = new FileOutputStream(dest);
			PdfReader reader = new PdfReader(src);
			XfaForm xfa = new XfaForm(reader);
			org.w3c.dom.Document doc = xfa.getDomDocument();
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.transform(new DOMSource(doc), new StreamResult(os));
			reader.close();
			os.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static Image createBarcodeImage(AcroFields.FieldPosition location, PdfContentByte cb, String barc) {
		final StringBuilder text = new StringBuilder(barc);

		final Barcode39 barcode = new Barcode39();
		barcode.setCode(text.toString());
		barcode.setStartStopText(false);
		System.out.println("image");
		try {
			final Image image = barcode.createImageWithBarcode(cb, null, null);
			 /*image.scaleAbsolute(location.position.getWidth() - 16,
			 location.position.getHeight() - 13);
			 image.setAbsolutePosition(location.position.getLeft() + 8,
			 location.position.getBottom() + 8);*/
			
			return image;
		} catch (final Exception e) {
		}
		return null;
	}
}