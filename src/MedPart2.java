import java.awt.Color;
import java.io.IOException;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MedPart2 {
	
	static String inpWord1 = "";
	static String inpWord2 = "";
	
	static Stack<String> operationData = new Stack<String>(); // to keep done operations
	
	public static void main(String[] args) throws IOException {
        
    	GUI gui = new GUI();

        gui.inputWord1.getDocument().addDocumentListener(new DocumentListener() {
            
        	@Override
            public void removeUpdate(DocumentEvent e) {
        		inpWord1 = gui.inputWord1.getText().toLowerCase();
                display(inpWord1, inpWord2, gui);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
            	inpWord1 = gui.inputWord1.getText().toLowerCase();
            	display(inpWord1, inpWord2, gui);
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
            	inpWord1 = gui.inputWord1.getText().toLowerCase();
            	display(inpWord1, inpWord2, gui);
            }
            
        });
        
        gui.inputWord2.getDocument().addDocumentListener(new DocumentListener() {
        	
            @Override
            public void removeUpdate(DocumentEvent e) {
            	inpWord2 = gui.inputWord2.getText().toLowerCase();
                display(inpWord1, inpWord2, gui);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
            	inpWord2 = gui.inputWord2.getText().toLowerCase();
            	display(inpWord1, inpWord2, gui);
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
            	inpWord2 = gui.inputWord2.getText().toLowerCase();
            	display(inpWord1, inpWord2, gui);
            }
            
        });
         
    }
	
	private static void display(String word1, String word2, GUI gui) {
        
		long initialTime = System.currentTimeMillis();
        int distance = editDistDP(word1, word2, gui);
        
        // GUI Update
        gui.resultText.append("Minimum Edit Distance : "  + distance +"\n");
        gui.resultText.append("\nOperations :\n");
        
        // display done operations
        while(!operationData.isEmpty())
        	gui.resultText.append(operationData.pop() + "\n");
        
        gui.resultText.append("\nTotal Running Time : " + (System.currentTimeMillis() - initialTime) + " milliseconds\n");
                                                                                                          
    }
	
    public static void findOperations(int[][] dp, String word1, String word2, int m, int n) { // to find done operations
    	
    	int indexX = m; int indexY = n;
    	
    	while(indexX != 0 || indexY != 0) {
    		
    		int dist = dp[indexX][indexY];
    		
    		if(indexY - 1 < 0) {
    			operationData.push("Delete " + word1.charAt(indexX - 1));
    			indexX --;
    			continue;
    		}
    		else if(indexX - 1 < 0) {
    			operationData.push("Insert " + word2.charAt(indexY - 1));
    			indexY --;
    			continue;
    		}
    		
            int leftDist = dp[indexX][indexY - 1];
            int upDist = dp[indexX - 1][ indexY];
            int leftUpDist = dp[indexX - 1][ indexY - 1];
            
            if ((leftUpDist <= leftDist && leftUpDist <= upDist) && (leftUpDist == dist - 1 || leftUpDist == dist))
            {
                if (leftUpDist == dist - 1)
                	operationData.push("Replace " + word1.charAt(indexX - 1) + " with " + word2.charAt(indexY - 1));
                
                indexX--;
                indexY--;
            }
            else if (leftDist <= leftUpDist && leftDist == dist - 1)
            {
            	operationData.push("Insert " + word2.charAt(indexY - 1));
                indexY--;                   
            }
            else
            {
            	operationData.push("Delete " + word1.charAt(indexX - 1));
                indexX--;                   
            }
    		
    	}
    }
	
    private static int editDistDP(String str1, String str2, GUI gui) { // returns med value
        
    	int m = str1.length();
    	int n = str2.length();
    	
    	int dp[][] = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
                else {
                	dp[i][j] = 1 + min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]);
                }
            }
        }
        
        // Update med table
        gui.resultText.setText("Minimum Edit Distance Table : \n");        
		
        for (int i = 0; i < m + 1; i++)
		{
			for (int j = 0; j < n + 1; j++)
			{
				gui.resultText.append(dp[i][j] + " ");
			}
			gui.resultText.append("\n");
			
		}
		gui.resultText.append("\n");
		
		findOperations(dp,str1,str2,m,n);
		
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

class GUI {
    
	JFrame frame;
    JTextArea resultText;
    JTextField inputWord1, inputWord2;
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
	    label2.setText("Please Enter a Word(Turkish) :");
	    frame.add(label2);
	    
	    inputWord1 = new JTextField("");
	    inputWord1.setBounds(200, 20, 300, 30);
        frame.add(inputWord1);
	    
	    inputWord2 = new JTextField("");
	    inputWord2.setBounds(200, 100, 300, 30);
        frame.add(inputWord2);

        resultText = new JTextArea();
        resultText.setBounds(15, 180, 485, 510);
        resultText.setEditable(false);
        frame.add(resultText);

        frame.setTitle("NLP-MED-PART2");
        frame.setSize(530, 750);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
}