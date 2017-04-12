package guimain;
import keygens.RC4Gen;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.awt.*;
//import java.math.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class GUIClass{
	
	JTextField text;
	JFormattedTextField outLength;
	JLabel output;
	String op;
	JFrame frame;
	Window w;
	String encodeString;
	/**
	 * The below constructor draws the entire frame. Each swing element is placed into a panel
	 * and each panel is then stored in the main panel and covers the entire frame.
	 *  @author skeletrox
	 */
	public GUIClass()
	{
		int height = 768, width = 1024;
		authenticate();
		//Manufacturing frame(s)
		
		frame = new JFrame ("Password Generator");
		w = new Window(frame);
		
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 *This is the listener for the random password generator. A random string of a
	 *random length is generated and encoded with RC4, then with Salsa20 and the
	 *output is again set to a default 8 character long password.
	 * @author skeletrox
	 *
	 */
	
	class RandomListener implements ActionListener
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
            keygen.scheduleKey();
            keygen.generatePseudoRandom(true);
            op=keygen.GetOutput();
            output.setText("OUTPUT: " + keygen.GetOutput());
		}
	
	}
	
	/**
	 * Added toward the end of the GUI preparation, this is the authentication system
	 * for the password generator. Stores the encoded username and password into a 
	 * fileand sets the string with which any encoded data is XORed in order to preserve
	 * integrity of data.
	 * @TODO make the text in the password showInputDialog "secret".
	 */
	
	void authenticate()
	{
		boolean isRegistered = true;
		File accFile = new File("usr.txt");
		if (!(accFile.exists()))
		{
			try
			{
				accFile.createNewFile();
			}
			catch (IOException io)
			{
				JOptionPane.showMessageDialog(frame,
					    "Unable to create userID file!",
					    "Fatal Error",
					    JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			isRegistered = false;
		}
		String titleString = isRegistered?"Authenticate":"Welcome New User";
		String username = JOptionPane.showInputDialog(frame, "Enter User ID", titleString, JOptionPane.INFORMATION_MESSAGE);
		String password = JOptionPane.showInputDialog(frame, "Enter Password", titleString, JOptionPane.INFORMATION_MESSAGE);
		if (isRegistered)
		{
			try
			{
				FileInputStream fi = new FileInputStream(accFile);
				String keyHash = new Scanner(fi).nextLine();
				String salt = username.substring(0, 2) + password.substring(0, 2);
				if (hashText(username+password, salt).equals(keyHash))
				{
					encodeString = hashText(username+password, salt);
					return;
				}
				fi.close();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(frame,
					    "Unable to read file!",
					    "Fatal Error",
					    JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		else
		{
			try
			{
				FileOutputStream fo = new FileOutputStream(accFile);
				String whatToWrite = username+password;
				String salt = username.substring(0, 2) + password.substring(0, 2);
				whatToWrite = hashText(whatToWrite, salt);
				System.out.println(whatToWrite);
				fo.write(whatToWrite.getBytes());
				encodeString = hashText(username+password, salt);
				return;
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(frame,
					    "Unable to write to file!",
					    "Fatal Error",
					    JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		JOptionPane.showMessageDialog(frame,
			    "Wrong credentials!",
			    "You're locked out",
			    JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	/**
	 * Hashes the input username and password with a 4 character salt
	 * @param text: a concatenation of username and password
	 * @param salt: a salt generated by using the first 2 letters of
	 * 				username and password. Works around the security
	 * 				issue where attacker knows username+password, but
	 * 				not where they split
	 * @return: the encoded value.
	 *  @author skeletrox
	 */
	String hashText(String text, String salt)
	{
		try
		{
			
			RC4Gen secretGen = new RC4Gen();
			secretGen.SetKey(text+salt);
			secretGen.SetOPLength(64);
			secretGen.scheduleKey();
			secretGen.generatePseudoRandom(false);
			return secretGen.GetOutput();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame,
				    "Encoding error",
				    "Sorry!",
				    JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		return new String();
	}
	
	/**
	 * This is the listener for the User choice button. On clicking,
	 * the user input text and the length of text are taken in and
	 * then processed with just RC4. This allows the user to use
	 * predefined phrases and lengths without having to store them in
	 * the database.
	 * @author skeletrox
	 *
	 */
	class UserListener implements ActionListener
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
			keygen.scheduleKey();
			keygen.generatePseudoRandom(false);
			op=keygen.GetOutput();
			output.setText("OUTPUT: " + keygen.GetOutput());
		}
	
	}
	
	/**
	 *Opens the dialog(s) to write to file
	 * @author shahAbdul, skeletrox
	 *
	 */
	
	class WriterListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				openWriterDialog();
				
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
	/**
	 * Opens the TableView that reads from file
	 * @author shahAbdul,skeletrox
	 *
	 */
	class ReaderListener implements ActionListener{
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
	/**
	 * Opens 2 dialogs: One for website and another for Username
	 * @author shahAbdul, skeletrox
	 * @throws Exception: if file cannot be opened/written into
	 */
	public void openWriterDialog() throws Exception
	{
		if (op == null)
		{
			JOptionPane.showMessageDialog(frame,
				    "There is no password to write!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		String website = (String) JOptionPane.showInputDialog(frame, "Enter Website/Context", "Write to File", JOptionPane.INFORMATION_MESSAGE);
		if (website == null || website.length() < 1)
		{
			JOptionPane.showMessageDialog(frame,
				    "Context cannot be blank!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		String userName = (String) JOptionPane.showInputDialog(frame, "Enter UserName", "Write to File", JOptionPane.INFORMATION_MESSAGE);
		if (userName == null)
		{
			JOptionPane.showMessageDialog(frame,
				    "Username cannot be blank!",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		writeToFile(quickEncode(website), quickEncode(userName), quickEncode(op));
	}
	/**
	 * 
	 * @param strings : Strings to be written into file
	 * @author skeletrox
	 * @throws Exception: unwritable/nonexistent file, although technically
	 * 					  caught by the caller function beforehand
	 */
	public void writeToFile(String... strings) throws Exception
	{
		Path p=Paths.get("GeneratedPasswords.txt");
		try (BufferedWriter writer = Files.newBufferedWriter(p,StandardOpenOption.CREATE,StandardOpenOption.APPEND)) {
			for (String s : strings)
			{
				writer.write(s);
				writer.newLine();
			}
		}
	}
	

	/**
	 * Builds the table and populates it.
	 * Reads text, encodes them [XOR is a self-inverse function]
	 * @throws Exception while trying to get from BufferedReader
	 *  @author skeletrox, shahAbdul
	 */
	public void readFromFile() throws Exception
	{
		Path p=Paths.get("GeneratedPasswords.txt");
		try (BufferedReader reader = Files.newBufferedReader(p)) {
			Scanner readerScanner = new Scanner(reader);
			int count = 0;
			ArrayList<String> dataList = new ArrayList<String>();
			while (readerScanner.hasNext())
			{
				dataList.add(quickEncode(readerScanner.nextLine()));
				count++;
			}
			final int rowCount = count/3;
			AbstractTableModel encryData = new AbstractTableModel(){
				public int getColumnCount() {return 3;}
				public int getRowCount() {return rowCount;}
				public Object getValueAt (int row, int col) {return dataList.get(row*3 + (col)%3);}
			};
			JTable table = new JTable(encryData);
			table.getColumnModel().getColumn(0).setHeaderValue("Website/Context");
			table.getColumnModel().getColumn(1).setHeaderValue("Username");
			table.getColumnModel().getColumn(2).setHeaderValue("Password");
			JScrollPane scroller = new JScrollPane(table);
			JFrame dataFrame = new JFrame("Hello");
			dataFrame.add(scroller);
			dataFrame.setVisible(true);
			dataFrame.setSize(1024, 768);
			while(true)
			{
				String s = reader.readLine();
				if (s!=null)
				{
					System.out.println(s);
				}
				else
				{
					break;
				}
			}	
		}
		catch (NoSuchFileException nio)
		{
			JOptionPane.showMessageDialog(frame,
				    "You haven't initialized a file yet",
				    "Sorry!",
				    JOptionPane.ERROR_MESSAGE);
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
	
	/**
	 * Encodes the string with the encode constant declared during login
	 * @param s: string to be encoded
	 * @return: the encoded string
	 *  @author skeletrox
	 */
	public String quickEncode(String s)
	{
		byte[] b = s.getBytes();
		int count = 0;
		StringBuilder out = new StringBuilder();
		for (byte bt : b)
		{
			bt ^= encodeString.charAt(count);
			out.append((char) bt);
			count %= encodeString.length();
		}
		return out.toString();
	}
	

}
