package util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;

public final class SaveFile {

	/*
	 * create three files: easy, normal, lunatic
	 * 
	 * For each mode, store the following information: ------------------- Game
	 * played Game Won Winning percentage Max Continual Win Max Continual Lose
	 * 
	 * allgamestatus : win, lose(unfinished does not count)
	 */

	// cmd
	private static final String GAMEPLAYED = "gameplayed";
	private static final String GAMEWON = "gamewon";
	private static final String WINNINGPERCENTAGE = "winningpercentage";
	private static final String MAXCONTWIN = "maxcontwin";
	private static final String MAXCONTLOSE = "maxcontlose";
	private static final String ALLGAMESTATUS = "allGamestatus";

	private static final String ConfigName = "record.dat";
	private static final String[] modeAttr = { "easy", "normal", "lunatic", "customize"};

	private static DocumentBuilder dBuilder;
	private static DocumentBuilderFactory dbFactory;
	private static Document doc;
	private static File destFile;

	public static void init() {
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			destFile = new File(ConfigName);

			if (destFile.exists()) {
				doc = dBuilder.parse(destFile);
				doc.getDocumentElement().normalize();
				return;
			} else {
				doc = dBuilder.newDocument();
			}

			// root element
			Element record = doc.createElement("record");
			doc.appendChild(record);

			// different modes
			for (int i = 0; i < modeAttr.length; i++) {
				Element curMode = doc.createElement("modes");
				Attr attr = doc.createAttribute("mode");
				attr.setValue(modeAttr[i]);
				curMode.setAttributeNode(attr);
				record.appendChild(curMode);

				Element gamePlayed = doc.createElement(GAMEPLAYED);
				Element gameWon = doc.createElement(GAMEWON);
				Element winningPercent = doc.createElement(WINNINGPERCENTAGE);
				Element continualWin = doc.createElement(MAXCONTWIN);
				Element continualLose = doc.createElement(MAXCONTLOSE);
				Element allGameStatus = doc.createElement(ALLGAMESTATUS);

				gamePlayed.setTextContent("0");
				gameWon.setTextContent("0");
				winningPercent.setTextContent("0");
				continualWin.setTextContent("0");
				continualLose.setTextContent("0");
				allGameStatus.setTextContent("");

				curMode.appendChild(gamePlayed);
				curMode.appendChild(gameWon);
				curMode.appendChild(winningPercent);
				curMode.appendChild(continualWin);
				curMode.appendChild(continualLose);
				curMode.appendChild(allGameStatus);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static String getWinTime(final int mode) {
		Element curmode = (Element) doc.getElementsByTagName("modes").item(mode);
		return curmode.getElementsByTagName(GAMEWON).item(0).getTextContent();
	}
	
	public static String getGamePlayedTime(final int mode) {
		Element curmode = (Element) doc.getElementsByTagName("modes").item(mode);
		return curmode.getElementsByTagName(GAMEPLAYED).item(0).getTextContent(); 
	}

	public static String getPercent(final int mode) {
		Element curmode = (Element) doc.getElementsByTagName("modes").item(mode);
		return curmode.getElementsByTagName(WINNINGPERCENTAGE).item(0).getTextContent();
	}
	
	public static String getMaxContWin(final int mode) {
		Element curmode = (Element) doc.getElementsByTagName("modes").item(mode);
		return curmode.getElementsByTagName(MAXCONTWIN).item(0).getTextContent();
	}
	
	public static String getMaxContLose(final int mode) {
		Element curmode = (Element) doc.getElementsByTagName("modes").item(mode);
		return curmode.getElementsByTagName(MAXCONTLOSE).item(0).getTextContent();	
	}

	private static void increaseNodeValue(Node curnode) {
		int total = Integer.parseInt(curnode.getTextContent());
		total++;
		curnode.setTextContent(total + "");
	}

	private static void updateWinningPercent(Element curmode) {
		final int gameWin = Integer.parseInt(curmode.getElementsByTagName(GAMEWON).item(0).getTextContent());
		final int totalGamePlayed = Integer.parseInt(curmode.getElementsByTagName(GAMEPLAYED).item(0).getTextContent());
		
		float newPercent;
		if(totalGamePlayed == 0)
			newPercent = 0.0f;
		else
			newPercent = (float) gameWin / totalGamePlayed * 100.0f;
		curmode.getElementsByTagName(WINNINGPERCENTAGE).item(0).setTextContent(String.valueOf(newPercent));
	}

	private static void addRecord(Element curmode, final String status) {
		String allrecord = curmode.getElementsByTagName(ALLGAMESTATUS).item(0).getTextContent();
		allrecord = allrecord + status;
		curmode.getElementsByTagName(ALLGAMESTATUS).item(0).setTextContent(allrecord);
	}
	
	public static void updateWinning(final int mode) {
		Element curmode = (Element) doc.getElementsByTagName("modes").item(mode);
		
		addRecord(curmode, "w");
		increaseNodeValue(curmode.getElementsByTagName(GAMEPLAYED).item(0));
		increaseNodeValue(curmode.getElementsByTagName(GAMEWON).item(0));
		
		// max continual winning
		String allStatus = curmode.getElementsByTagName(ALLGAMESTATUS).item(0).getTextContent();
		if (allStatus.length() > 0 && allStatus.charAt(allStatus.length() - 1) == 'w') {
			int maxCont = 0;

			for (int j = allStatus.length() - 1; j >= 0 && allStatus.charAt(j) == 'w'; j--) {
				maxCont++;
			}

			final int prevMaxCont = Integer.parseInt(curmode.getElementsByTagName(MAXCONTWIN).item(0).getTextContent());
			if (prevMaxCont < maxCont)
				curmode.getElementsByTagName(MAXCONTWIN).item(0).setTextContent(maxCont + "");
		}

		// winning percentage
		updateWinningPercent(curmode);
	}

	public static void updateLosing(final int mode) {
		Element curmode = (Element) doc.getElementsByTagName("modes").item(mode);

		addRecord(curmode, "l");
		increaseNodeValue(curmode.getElementsByTagName(GAMEPLAYED).item(0));

		// max continual lose
		String allStatus = curmode.getElementsByTagName(ALLGAMESTATUS).item(0).getTextContent();
		if (allStatus.length() > 0 && allStatus.charAt(allStatus.length() - 1) == 'l') {
			int maxCont = 0;
			for (int j = allStatus.length() - 1; j >= 0 && allStatus.charAt(j) == 'l'; j--) {
				maxCont++;
			}

			final int prevMaxCont = Integer
					.parseInt(curmode.getElementsByTagName(MAXCONTLOSE).item(0).getTextContent());
			if (prevMaxCont < maxCont)
				curmode.getElementsByTagName(MAXCONTLOSE).item(0).setTextContent(maxCont + "");
		}

		// winning percentage
		updateWinningPercent(curmode);
	}

	public static void resetRecord() {
		try {
			// doc = dBuilder.parse(destFile);
			NodeList nodeList = doc.getElementsByTagName("modes");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element curMode = (Element) nodeList.item(i);

				curMode.getElementsByTagName(GAMEPLAYED).item(0).setTextContent("0");
				curMode.getElementsByTagName(GAMEWON).item(0).setTextContent("0");
				curMode.getElementsByTagName(WINNINGPERCENTAGE).item(0).setTextContent("0");
				curMode.getElementsByTagName(MAXCONTWIN).item(0).setTextContent("0");
				curMode.getElementsByTagName(MAXCONTLOSE).item(0).setTextContent("0");
				curMode.getElementsByTagName(ALLGAMESTATUS).item(0).setTextContent("");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void output() {
		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(destFile);
			transformer.transform(source, result);
		} catch (Exception e) {
		}
	}
}
