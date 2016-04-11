package com.ibm.uitests.prepush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.*;
import javax.mail.search.SubjectTerm;

public class Gmail {
	
	private String tempPassword, message;
	private boolean found = false;
	
	public Gmail(String email, String password){
		Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            Store store;
			try {
				store = session.getStore("imaps");
				store.connect("imap.gmail.com", email, password);
				
				Folder folder = store.getFolder("INBOX");
	            folder.open(Folder.READ_WRITE);
	            
	            Message[] messages = null;
	            Message ubx= null;

	            //Search for mail from God
	            for (int i = 0; i < 5; i++) {
	                messages = folder.search(new SubjectTerm(
	                        "Your temporary UBX password"),
	                        folder.getMessages());
	                //Wait for 10 seconds
	                if (messages.length == 0) {
	                    Thread.sleep(10000);
	                }
	            }

	            //Search for unread mail from God
	            //This is to avoid using the mail for which 
	            //Registration is already done
	            for (Message mail : messages) {
	                if (!mail.isSet(Flags.Flag.SEEN)) {
	                    ubx = mail;
	                    found = true;
	                }
	            }

	            //Test fails if no unread mail was found from God
	            if (found) {
	                String line;
	                StringBuffer buffer = new StringBuffer();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(ubx.getInputStream()));
	                while ((line = reader.readLine()) != null) {
	                    buffer.append(line);
	                }
	                parseEmail(buffer.toString());
	            }else{
	            	System.out.println("No email found");
	            }
	            
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
	}
	
	private void parseEmail(String text){
		int colon = text.indexOf(":");
		int message = text.indexOf("You will");
		
		this.tempPassword = text.substring(colon+1, message);
		this.message = text.substring(message);
	}
	
	protected String getTempPassword() {
		return tempPassword;
	}

	protected String getMessage() {
		return message;
	}
	
	protected boolean isFound(){
		return found;
	}
}
