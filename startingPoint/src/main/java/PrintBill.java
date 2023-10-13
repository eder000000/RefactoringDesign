import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PrintBill {

	private Invoices invoices;

	public PrintBill(Invoices invoices) {
		this.invoices = invoices;
	}

	public static void main(String[] args) {
		// Read plays and invoices from JSON files
		Plays plays = readPlaysFromJson("C:\\Users\\edera\\eclipse-workspace\\Refactoring\\src\\startingPoint\\src\\main\\resources\\plays.json");
		Invoices invoices = readInvoicesFromJson("C:\\Users\\edera\\eclipse-workspace\\Refactoring\\src\\startingPoint\\src\\main\\resources\\invoices.json");

		PrintBill printBill = new PrintBill(invoices);
		// Process each invoice and print the statement
		printBill.processInvoices(plays);
	}
		private static Plays readPlaysFromJson(String filename) {
			try (FileReader reader = new FileReader(filename)) {
				Gson gson = new Gson();
				JsonObject json = gson.fromJson(reader, JsonObject.class);
				return parsePlays(json);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		private static Plays parsePlays(JsonObject json) {
			Plays plays = new Plays();
			JsonObject playsJson = json.getAsJsonObject("plays");

			for (String playID : playsJson.keySet()) {
				JsonObject playJson = playsJson.getAsJsonObject(playID);
				String name = playJson.get("name").getAsString();
				String type = playJson.get("type").getAsString();

				plays.addPlay(new Play(playID, name, type));
			}
			return plays;
		}

	private static Invoices readInvoicesFromJson(String filename) {
		try (FileReader reader = new FileReader(filename)) {
			Gson gson = new Gson();
			Invoice[] invoiceArray = gson.fromJson(reader, Invoice[].class);
			return new Invoices(Arrays.asList(invoiceArray));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static List<Invoice> parseInvoices(JsonArray jsonArray) {
		List<Invoice> invoices = new ArrayList<>();

		for (JsonElement element : jsonArray) {
			JsonObject invoiceJson = element.getAsJsonObject();
			String customer = invoiceJson.get("customer").getAsString();
			JsonArray performancesJson = invoiceJson.getAsJsonArray("performances");

			List<Performance> performances = parsePerformances(performancesJson);

			invoices.add(new Invoice(customer, performances));
		}

		return invoices;
	}

	private static List<Performance> parsePerformances(JsonArray performancesJson) {
		List<Performance> performances = new ArrayList<>();

		for (JsonElement element : performancesJson) {
			JsonObject performanceJson = element.getAsJsonObject();
			String playID = performanceJson.get("playID").getAsString();
			int audience = performanceJson.get("audience").getAsInt();

			performances.add(new Performance(playID, audience));
		}

		return performances;
	}



	public void processInvoices(Plays plays) {
		for (Invoice invoice : invoices.getInvoices()) {
			String statement = statement(invoice, plays);
			System.out.println(statement);
		}
	}

	public static String statement(Invoice invoice, Plays plays) {
			int totalAmount = 0;
			int volumeCredits = 0;
			StringBuilder result = new StringBuilder("Statement for " + invoice.getCustomer() + "\n");
			NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

		// Process each invoice and print the statement
		for (Performance perf : invoice.getPerformances()) {
			Play play = plays.getPlay(perf.getPlayID());
			int thisAmount = 0;

			switch (play.getType()) {
				case "tragedy":
					thisAmount = 40000;
					if (perf.getAudience() > 30) {
						thisAmount += 1000 * (perf.getAudience() - 30);
					}
					break;
				case "comedy":
					thisAmount = 30000;
					if (perf.getAudience() > 20) {
						thisAmount += 10000 + 500 * (perf.getAudience() - 20);
					}
					thisAmount += 300 * perf.getAudience();
					break;
				default:
					throw new IllegalArgumentException("Unknown type: " + play.getType());
			}

			// Add volume credits
			volumeCredits += Math.max(perf.getAudience() - 30, 0);
			// Add extra credit for every ten comedy attendees
			if ("comedy".equals(play.getType())) {
				volumeCredits += Math.floorDiv(perf.getAudience(), 5);
			}

			// Print line for this order
			result.append("  ").append(play.getName()).append(": ").append(format.format(thisAmount / 100.0))
					.append(" (").append(perf.getAudience()).append(" seats)\n");
			totalAmount += thisAmount;
		}

			result.append("Amount owed is ").append(format.format(totalAmount / 100.0)).append("\n");
			result.append("You earned ").append(volumeCredits).append(" credits\n");
			return result.toString();
	}
}
