import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.io.*;


public class SitopCrawler {

	public static void main(String[] args) {
		String sitop = obterSITOPRuim("23/03/2019", 1, 92);
		System.out.println(sitop);

	}
	
	public static String obterSITOPRuim(String data, int periodo, int codigoOrg) {
		String par = null;
		try {

			par = data + "|" + periodo + "|" + codigoOrg;

			if (codigoOrg < 0)
				return null;

			URL urlSitop = new URL(
					"http://sitop.petrobras.biz/aplicativo/LI04-SITOP/consulta/generica/ConGenerica_Report.asp");

			HttpURLConnection con = (HttpURLConnection) urlSitop
					.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(12000);
			con.setReadTimeout(12000);
			String parametros = URLEncoder.encode("parametro", "UTF-8") + "="
					+ URLEncoder.encode(par, "UTF-8");
			con.setRequestProperty("Content-Length",
					"" + Integer.toString(parametros.getBytes().length));

			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			dos.writeBytes(parametros);
			dos.flush();
			dos.close();

			InputStream is = con.getInputStream();

			return IOUtils.toString(is, "UTF-8");

		} catch (SocketTimeoutException ste) {
			return "__ERROR500__" + par;
		} catch (Exception e) {

		}

	return null;
	}


}
