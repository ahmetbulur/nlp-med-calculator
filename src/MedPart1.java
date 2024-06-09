import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MedPart1 {

	public static void main(String[] args) throws IOException {
        
		String fullText = " ";
        fullText = writeTextFileToString("vocabulary_tr.txt"); //read text file as a single line 
        fullText = fullText.replace('ð', 'ğ').replace('ý', 'ı').replace('þ', 'ş'); //replace nonalphabetic symbols with Turkish letters
        String[] textWords = fullText.split("\\r\\n"); //split words again and store them in array
        
    	GUI gui = new GUI();
    	
    	// GUI Instant Update
    	gui.inputWord.getDocument().addDocumentListener(new DocumentListener() {
            
        	@Override
            public void removeUpdate(DocumentEvent e) {
        		display(textWords, gui.inputWord.getText().toLowerCase(), gui);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
            	display(textWords, gui.inputWord.getText().toLowerCase(), gui);
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
            	display(textWords, gui.inputWord.getText().toLowerCase(), gui);
            }
        });
    }

	private static void display(String[] textWords, String userWord, GUI gui) {
        
    	long initialTime = System.currentTimeMillis();

        Word[] wordData = getWordData(textWords, userWord);
        Arrays.sort(wordData);
        
        // GUI Update
        gui.resultText.setText("The Nearest 5 Words: \n");
        
        // display top-5 nearest words
        for (int i = 0; i < 5; i++)
        	gui.resultText.append((i+1) + "- " + wordData[i].name + " : " + wordData[i].distance + "\n");
        
        gui.resultText.append("Total Running Time : " + (System.currentTimeMillis() - initialTime) + " milliseconds\n");
                                                                                                          
    }
	
	private static Word[] getWordData(String[] textWords, String userWord) {
        
    	Word[] wordData = new Word[textWords.length];
        
    	for (int i = 0; i < textWords.length; i++)
        	wordData[i] = new Word(textWords[i], editDistDP(textWords[i], userWord));
        
        return wordData;
    }

    private static String writeTextFileToString(String fileName) throws IOException {
		
    	StringBuffer text = new StringBuffer();
		
    	try {
			File fileDir = new File(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "ISO-8859-1"));
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line + "\r\n");
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text.toString();
    }

    private static int editDistDP(String str1, String str2) { // returns med value
        
    	int m = str1.length();
    	int n = str2.length();
    	
    	int dp[][] = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0)
                    dp[i][j] = j;
                else if (j == 0)
                    dp[i][j] = i;
                else if (str1.charAt(i - 1) == str2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]);
            }
        }
        return dp[m][n];
    }
    
    private static int min(int x, int y, int z) {
        
    	if (x <= y && x <= z)
            return x;
        if (y <= x && y <= z)
            return y;
        else
            return z;
    }
}

class Word implements Comparable<Word> {
    
	public String name;
    public int distance;

    public Word(String name, int distance) {
        this.name = name;
        this.distance = distance;
    }

	@Override
	public int compareTo(Word w) {
        if (this.distance != w.distance)
            return this.distance - w.distance;
        return 0;
    }
}

class GUI {
    
	JFrame frame;
    JTextArea resultText;
    JTextField inputWord;
    JLabel label1, label2;

    GUI() {
    	
        frame = new JFrame();
        
        label1 = new JLabel();
	    label1.setBounds(15, 20, 300, 30);
	    label1.setForeground(Color.BLUE);
	    label1.setText("Please Enter a Word(Turkish) :");
	    frame.add(label1);
	    
        label2 = new JLabel();
	    label2.setBounds(15, 100, 300, 30);
	    label2.setForeground(Color.BLUE);
	    label2.setText("Words-Distances :");
	    frame.add(label2);

	    inputWord = new JTextField("");
        inputWord.setBounds(200, 20, 300, 30);
        frame.add(inputWord);
        
        resultText = new JTextArea();
        resultText.setBounds(200, 100, 300, 120);
        resultText.setEditable(false);
        frame.add(resultText);

        frame.setTitle("NLP-MED-PART1");
        frame.setSize(530, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}