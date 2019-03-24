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
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.io.*;


public class SitopCrawler {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Digite a data inicial: dd/MM/yyyy\n");
		Pattern patternData = Pattern.compile("[0-9][0-9]/[0-9][0-9]/[0-9]{4}");
		//while (!s.hasNext(patternData));
		String textoDataInicial = s.next(patternData);
		System.out.println("Digite a data final: dd/MM/yyyy\n");
		String textoDataFinal = s.next(patternData);

		//System.out.println(textoDataInicial);
		Date dataInicial = null;
		Date dataFinal = null;

		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			dataInicial = df.parse(textoDataInicial);
			dataFinal = df.parse(textoDataFinal);

			System.out.println(obterSITOPs(dataInicial, dataFinal, 92)); //92 P-07


		} catch(Exception e) {
			e.printStackTrace();
		}

		/*Date hoje = Calendar.getInstance().getTime();

		String strHoje = df.format(hoje);
		System.out.println(strHoje);
		hoje.setMonth(hoje.getMonth()
		 */
		//String sitop = obterSITOPRuim("23/03/2019", 1, 92);
		//System.out.println(sitop);

	}

	public static String obterSITOPs(Date dataInicial, Date dataFinal, int codigoOrg) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataInicial);
		System.out.println(cal.getTime());
		String par = ""; //"23/03/2019|1|92;24/03/2019|1|92"
		while(cal.getTime().compareTo(dataFinal) < 0) {
			par += df.format(cal.getTime())+"|1|"+String.valueOf(codigoOrg)+";";				

			cal.add(Calendar.DATE, 1);
		}
		par += df.format(cal.getTime())+"|1|"+String.valueOf(codigoOrg);
		System.out.println(par);
		
		try {
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
			e.printStackTrace();
		}
		return null;		
	}




	}
