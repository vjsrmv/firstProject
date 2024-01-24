package com.newgen.custom.sendmail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;



public class SendMail {



		public static void main(String[] args) throws MessagingException, IOException {
			Properties properties = new Properties();
			properties.setProperty("mail.store.protocol", "imaps"); 
			System.out.println("Hi");
			
			javax.mail.Session session = Session.getDefaultInstance(properties);
			// Connect to the IMAP server
			Store store = session.getStore("imaps");
			//store.connect("imap.gmail.com", "anshikamunshi414@gmail.com", "vrfhkxokmnpiyovy");
			store.connect("imap.gmail.com","ibpsoptimus@gmail.com","qeymhgkhsramvhrd");
			
			// Open the INBOX folder
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE); // Open the folder in read-only mode
			
			Folder tradeDemo = store.getFolder("TradeDemo");
			tradeDemo.open(Folder.READ_WRITE);
			
			System.out.println("Connected");
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message unreadmsg[] = inbox.search(unseenFlagTerm);
			System.out.println(unreadmsg.length);
			//javax.mail.Message[] messages = inbox.getMessages();

			for (javax.mail.Message message : unreadmsg) {
				System.out.println("Subject: " + message.getSubject());
				int number = message.getMessageNumber();
				System.out.println("Number="+ number);
				
				
				try {
					//String msg = getReplyText(message);
					String msg = getContent(message);
					
					String accuntNumber = msg.substring(msg.indexOf("Account to be credited")+"Account to be credited".length(),msg.indexOf("Type of account"));
					System.out.println(accuntNumber);
					accuntNumber = accuntNumber.replaceAll("(<|>)", "");
					System.out.println(accuntNumber);
					
					//System.out.println("MSG="+msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//			    System.out.println("From: " + message.getFrom()[0]);
//			    System.out.println("Sent Date: " + message.getSentDate());
//			    System.out.println("Content: " + message.getContent().toString());
				
				inbox.copyMessages(new Message[]{message}, tradeDemo);
			    message.setFlag(Flags.Flag.DELETED, true);
			}
			inbox.expunge();
			
			inbox.close(false);
			store.close();
		}
		
		
		
		 private static String getReplyText(Message message) throws Exception {
		        String content = "";

		        Object messageContent = message.getContent();
		        if (messageContent instanceof Multipart) {
		            Multipart multipart = (Multipart) messageContent;
		            for (int i = 0; i < multipart.getCount(); i++) {
		                BodyPart bodyPart = multipart.getBodyPart(i);
		                if (bodyPart.getDisposition() != null
		                        && bodyPart.getDisposition().equalsIgnoreCase(Part.INLINE)) {
		                    // Skip inline attachments
		                    continue;
		                }
		                content += bodyPart.getContent().toString();
		            }
		        } else {
		            content = messageContent.toString();
		        }

		        // Remove the original message quoted text (assuming ">" is used for quoting)
		        content = content.replaceAll("(?s)^.*?\\Q" + ">" + "\\E", "");

		        return content.trim();
		    }
		
		
		
		private static String getContent(Part p) throws Exception {
	        if (p.isMimeType("text/plain")) {
	        	System.out.println("Here");
	            return (String) p.getContent();
	        } else if (p.isMimeType("multipart/*")) {
	            Multipart mp = (Multipart) p.getContent();
	            System.out.println("Here New Content" + mp.getCount());
	            for (int i = 0; i < mp.getCount(); i++) {
	                BodyPart bodyPart = mp.getBodyPart(i);
	                String content = getContent(bodyPart);
	                System.out.println("Content=");
	                if (content != null) {
	                    return content;
	                }
	            }
	        }
	        return null;
	    }
	
}
