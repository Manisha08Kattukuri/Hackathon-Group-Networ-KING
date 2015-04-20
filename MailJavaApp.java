
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JPasswordField;



public class MailJavaApp extends Frame {

	/*  Fields for the server */
	private Label servLabel = new Label("Local MailServer :");
	private TextField servTextField = new TextField("", 50);
	/*  From fields for the GUI*/
	private Label FroMsgLabel = new Label("From :");
	private TextField FromTextField = new TextField("", 60);
	/* Password fields for the GUI */
	private Label PswdLabel = new Label("Password :");  
	private JPasswordField PswdField=new JPasswordField(15);  
	/* To Fields for the GUI */
	private Label ToLabel = new Label("To :");
	private TextField ToTextField = new TextField("", 50);
	/*  CC Field for the GUI. */
	private Label CcLabel = new Label("Cc :");
	private TextField CcTextField = new TextField("", 60);
	/*   BCC Field for the GUI */
	private Label BccLabel = new Label("Bcc :");
	private TextField BccTextField = new TextField("", 50);
	/*  Subject Field for the GUI. */
	private Label SubjectLabel = new Label("Subject :");
	private TextField SubTextField = new TextField("", 60);
	/*  Message Field for the GUI. */
	private Label MsgLabel = new Label("Message :");
	private TextArea MsgTextField = new TextArea(15, 50);
	/*  Send Button placed in GUI.*/
	private Button BtnSend = new Button("Send");
	/*  Clear Button placed in GUI. */
	private Button BtnClear = new Button("Clear");
	/*  Quit  Button placed in GUI. */
	private Button BtnQuit = new Button("Quit");
		
	/**
	 * Creating a Mail form which is used by the user to provide the details such as To, Form, Password message and subject	 */
	public MailJavaApp() {
		super("Java Mailclient");
		
		/*
		 * Panels for placing the different fields specified above
		 */
		
		Panel servP = new Panel(new BorderLayout());
		Panel frmP = new Panel(new BorderLayout());
		Panel pswdP = new Panel(new BorderLayout());
		Panel toP = new Panel(new BorderLayout());
		Panel ccP = new Panel(new BorderLayout());
		Panel bccP = new Panel(new BorderLayout());
		Panel subjP = new Panel(new BorderLayout());
		Panel msgP = new Panel(new BorderLayout());
		servP.add(servLabel, BorderLayout.CENTER);
		servP.add(servTextField, BorderLayout.WEST);		
		frmP.add(FroMsgLabel, BorderLayout.CENTER);
		frmP.add(FromTextField, BorderLayout.WEST);
		pswdP.add(PswdLabel, BorderLayout.CENTER);
		pswdP.add(PswdField, BorderLayout.WEST);	
		toP.add(ToLabel, BorderLayout.CENTER);
		toP.add(ToTextField, BorderLayout.WEST);
		ccP.add(CcLabel, BorderLayout.CENTER);
		ccP.add(CcTextField, BorderLayout.WEST);
		bccP.add(BccLabel, BorderLayout.CENTER);
		bccP.add(BccTextField, BorderLayout.WEST);
		subjP.add(SubjectLabel, BorderLayout.CENTER);
		subjP.add(SubTextField, BorderLayout.WEST);
		msgP.add(MsgLabel, BorderLayout.SOUTH);
		msgP.add(MsgTextField, BorderLayout.WEST);
		Panel formP = new Panel(new GridLayout(0, 1));
		formP.add(servP);
		formP.add(frmP);
		formP.add(pswdP);
		formP.add(toP);
		formP.add(ccP);
		formP.add(bccP);
		formP.add(subjP);

		/*
		 * Create a panel for the buttons and add listeners to the buttons.
		 */
		
		Panel btnP = new Panel(new GridLayout(1, 0));
		BtnSend.addActionListener(new MailJavaApp.SendListener());
		BtnClear.addActionListener(new MailJavaApp.ClearListener());
		BtnQuit.addActionListener(new MailJavaApp.QuitListener());
		btnP.add(BtnSend);
		btnP.add(BtnClear);
		btnP.add(BtnQuit);
		
		add(formP, BorderLayout.CENTER);
		add(msgP, BorderLayout.WEST);
		add(btnP, BorderLayout.NORTH);
		pack();
		show();
	}

	public static void main(String argv[]) {
		new MailJavaApp();
	}

	/* Button senders handler code. */
	class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Properties prpts = new Properties();
			prpts.put("mail.smtp.host", "smtp.gmail.com");
			prpts.put("mail.smtp.socketFactory.port", "");
			prpts.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			prpts.put("mail.smtp.auth", "true");
			prpts.put("mail.smtp.port", "465");

			String Uname=FromTextField.getText();    	
	    	String Pswd=PswdField.getText();    	
	    	String ToAdd=ToTextField.getText();    	
	    	String CcAdd=CcTextField.getText();    	
	    	String Subj=SubTextField.getText();    	
	    	String msg=MsgTextField.getText();
			Session sesn = Session.getDefaultInstance(prpts,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									Uname, Pswd);
						}
					});

			System.out.println("Mail being sent");

			/* Verify weather mail server exists or not */
			if ((servTextField.getText()).equals("")) {
				System.out.println("name of the local mail server is required");
				return;
			}

			/* verifying weather the sender and recipient addresses are specified or not . */
			if ((FromTextField.getText()).equals("")) {
				System.out.println("Sender address is required");
				return;
			}
			if ((ToTextField.getText()).equals("")) {
				System.out.println("Recipent address is required");
				return;
			}
			
			Validations mailMsg = new Validations(FromTextField.getText(),
	                  ToTextField.getText(),
	                   SubTextField.getText(),
	                    MsgTextField.getText());

//			/* Verifying weather the sender and recipient addresses are given correctly or not . */
	          if(!mailMsg.isValid()) {
				return;
				}

			/* code for message creation */
			try {

				Message msg1 = new MimeMessage(session);
				msg1.setFrom(new InternetAddress(FromTextField.getText()));
				msg1.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(ToTextField.getText()));
				msg1.setRecipients(Message.RecipientType.CC,
						InternetAddress.parse(CcTextField.getText()));
				msg1.setRecipients(Message.RecipientType.BCC,
						InternetAddress.parse(BccTextField.getText()));

				msg1.setSubject(SubTextField.getText());
				msg1.setText(MsgTextField.getText());
				Transport.send(msg1);
				System.out.println("Message Delivery sent");

			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}

			System.out.println("Succesfull mail delivery");
		}
	}
     /* for clearing all the fields in the form */
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Clearing fields");
			SubTextField.setText("");
			FromTextField.setText("");
			MsgTextField.setText("");
			ToTextField.setText("");	
		}
	}
  /* for exit form the form */
	class QuitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	
	}

	
	}


