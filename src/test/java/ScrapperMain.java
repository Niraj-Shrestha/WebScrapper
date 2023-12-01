import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ScrapperMain {
    public static void main(String[] args) {
        try{
            String url = "https://www.youtube.com";

            //Make URL connection
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            //Get ResponseCode and print
            int responseCode = con.getResponseCode();
            System.out.println("Response code: " + responseCode);
            if(responseCode == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String html = response.toString();
                Document doc = Jsoup.parse(html);
                Elements links = doc.select("a[href]");

                Writer writer = Files.newBufferedWriter(Paths.get("links.csv"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

                //Loop and print the links into the CSV record
                for (Element link : links) {
                    String href = link.attr("href");
                    csvPrinter.printRecord(href);
                }
                csvPrinter.close();
            }

        }catch (IOException e){
            throw new Error(e);
        }
    }
}
