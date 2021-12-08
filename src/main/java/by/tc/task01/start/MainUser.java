package by.tc.task01.start;

import by.tc.task01.connection.ClientConnector;
import by.tc.task01.connection.Message;
import by.tc.task01.connection.SendRecive;
import by.tc.task01.presentation.APIClientSide;
import jakarta.xml.bind.JAXBException;

import java.util.EnumSet;
import java.util.Scanner;

public class MainUser {

	public static void main(String[] args) {
		System.out.println("This is client side of application.\n"+
							"You can authorize as admin(pass:admin),user(user),editor(editor)\n"+
							"Or you can register as new user with new login, but you can only view elements of archive if you user\n"+
							"To add and update data you ned to be editor\n"+
							"Admin can do anything and change role of user"
				);
		try {
			ClientConnector connector=new ClientConnector();
			var API=new APIClientSide(connector.connectToServer());
			Scanner sc=new Scanner(System.in);
			do {
				System.out.println("Enter action: ");
				EnumSet<Message.CommandEn> commandEns=EnumSet.range(Message.CommandEn.REGISTER_USER, Message.CommandEn.UPDATE_USER);
				for (var c:commandEns ) {
					System.out.println("\t"+c);
				}
				try {
					Message.CommandEn commandEn=Message.CommandEn.valueOf(sc.next());
					if (commandEn== Message.CommandEn.CLOSE_CONNECTION){
						return;
					}
					API.processCommand(commandEn);
				}catch (Exception e){
					//e.printStackTrace();
					System.out.println("You wrote incorrect action");
				}
			}while (true);
		} catch (JAXBException e) {
			//e.printStackTrace();
		}
	}
}
