package guimain;
import keygens.RC4Gen;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.awt.*;
//import java.math.*;
import java.text.NumberFormat;

public class GUIClass{
	
	JTextField text;
	JFormattedTextField outLength;
	JLabel output;
	String op;
	public GUIClass()
	{
		int height = 768, width = 1024;
		
		//Manufacturing frame(s)
		
		JFrame frame = new JFrame ("Password Generator");
		
		//Manufacturing panel(s)
		JPanel maxPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel buttonPanel2 = new JPanel();
		JPanel titlePanel = new JPanel();
		JPanel textPanel = new JPanel();
		JPanel outputPanel = new JPanel();
		JPanel outTextPanel = new JPanel();
		JPanel separator = new JPanel();
		JPanel lengthPanel = new JPanel();
		JPanel descPanel = new JPanel();
		
		//Manufacturing font(s)
		
		Font font = new Font("Noto Sans", Font.TRUETYPE_FONT, 20);
		Font titleFont = new Font ("Noto Sans", Font.BOLD, 40);
		Font descFont = new Font ("Noto Sans", Font.ITALIC, 20);
		
		//Manufacturing label(s)
		
		JLabel title = new JLabel ("Enter your key here!");
		JLabel desc = new JLabel ("Or press \"Generate random password\" to generate a random password");
		JLabel lengthTeller = new JLabel ("Enter the length of output key: ");
		lengthTeller.setFont(font);
		title.setFont(titleFont);
		output = new JLabel("OUTPUT: ");
		output.setFont(font);
		desc.setFont(descFont);
		
		//Manufacturing button(s) and textfield(s)
		
		text = new JTextField(20);
		outLength = new JFormattedTextField(NumberFormat.getIntegerInstance());
		outLength.setColumns(3);
		JButton random = new JButton ("Generate random password");
		random.addActionListener (new RandomListener());
		JButton userIP = new JButton ("Use text in the text field ");
		userIP.addActionListener (new UserListener());
		text.setFont(font);
		outLength.setFont(font);
		JButton writeToFile= new JButton("Write to file.");
		writeToFile.addActionListener(new WriterListener());
		JButton readFromFile=new JButton("Read stored passwords.");
		readFromFile.addActionListener(new ReaderListener());
		
		//Add stuff to panels
		
		titlePanel.add(title);
		textPanel.add(text);
		descPanel.add(desc);
		buttonPanel.add(random);
		buttonPanel.add(separator);
		buttonPanel.add(userIP);
		//buttonPanel.add(separator);
		buttonPanel2.add(writeToFile);
		buttonPanel2.add(separator);
		buttonPanel2.add(readFromFile);
		outTextPanel.add(outLength);
		lengthPanel.add(lengthTeller);
		outputPanel.add(output);
		
		
		
		//More cosmetics headed our way!
		
		separator.setSize(50, 30);
		
		maxPanel.setLayout((new BoxLayout(maxPanel, BoxLayout.Y_AXIS)));
		buttonPanel.setLayout((new BoxLayout(buttonPanel, BoxLayout.X_AXIS)));
		buttonPanel2.setLayout((new BoxLayout(buttonPanel2, BoxLayout.X_AXIS)));
		//lengthPanel.setLayout((new BoxLayout(lengthPanel, BoxLayout.X_AXIS)));
		maxPanel.add(titlePanel);
		maxPanel.add(descPanel);
		maxPanel.add(textPanel);
		maxPanel.add(lengthPanel);
		maxPanel.add(outTextPanel);
		maxPanel.add(outputPanel);
		maxPanel.add(buttonPanel);
		maxPanel.add(buttonPanel2);
		maxPanel.setBounds(0, 0, width, height);
		//buttonPanel.setBounds(20, 50, height - 20, 25);
		
		//We shall now display our amalgamation
		
		frame.add(maxPanel);
		frame.setVisible (true);
		frame.setSize(width, height);
	}
	
	
	public class RandomListener implements ActionListener
	{
			public void actionPerformed (ActionEvent e)
		{
			//checking if random was clicked
			RC4Gen keygen=new RC4Gen();
			int i = 0, length = (int)( Math.random() * 8 + 8), num = 0;
			StringBuilder randStr = new StringBuilder();
            String inStr;
            for (i=0;i<length;i++)
            {
               num = (int) (Math.random()*127);
               if (num < 33)
               {
                   num += 33;
               }
               randStr.append((char) num);
            }
            inStr = randStr.toString();
            text.setText(inStr);
            keygen.SetKey (inStr);
            try
            {
            	keygen.SetOPLength(Integer.parseInt(outLength.getText()));
            }
            catch (NumberFormatException n)
            {
            	keygen.SetOPLength(8);
            	outLength.setText("8");
            }
            keygen.ScheduleKey();
            keygen.GeneratePseudoRandom(true);
            op=keygen.GetOutput();
            output.setText("OUTPUT: " + keygen.GetOutput());
		}
	
	}
	
	public class UserListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			RC4Gen keygen = new RC4Gen();
			String inStr = text.getText();
			try
			{
				keygen.SetKey(inStr);
				keygen.SetOPLength(Integer.parseInt(outLength.getText()));
			}
			
			catch(NumberFormatException z)
			{
				keygen.SetOPLength(8);
				keygen.SetKey("Please enter valid length");
				text.setText("Please enter valid length");
				outLength.setText("8");
			}
			keygen.ScheduleKey();
			keygen.GeneratePseudoRandom(false);
			op=keygen.GetOutput();
			output.setText("OUTPUT: " + keygen.GetOutput());
		}
	
	}
	
	public class WriterListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				writeToFile();
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
	
	public class ReaderListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				readFromFile();
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
	
	public void writeToFile() throws Exception
	{
		Path p=Paths.get("GeneratedPasswords.txt");
		try (BufferedWriter writer = Files.newBufferedWriter(p,StandardOpenOption.CREATE,StandardOpenOption.APPEND)) {
		    writer.write(op);
		    writer.newLine();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// EDit this to your liking. Idk enough of GUI to make it print in a frame, and also it's not prinitng all the stored passwords. But rest assured, they exist in the file.
	
	public void readFromFile() throws Exception
	{
		Path p=Paths.get("GeneratedPasswords.txt");
		try (BufferedReader reader = Files.newBufferedReader(p)) {
			while(reader.readLine()!=null)
			{
				System.out.print(reader.readLine()+'\n');
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		new GUIClass();
	}
}
