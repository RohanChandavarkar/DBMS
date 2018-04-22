
import java.sql.*;
import java.util.Scanner;

//import jdbc_demo.Information_Processing;
//import jdbc_demo.Reports;
//import jdbc_demo.Service;
//import jdbc_demo.Billing_func;


public class Main {
	static Connection conn=null;
	public static void main(String args[]) throws Exception {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/sbekkem";
			String username="sbekkem";
			String password="adorable2";
			conn=DriverManager.getConnection(jdbcURL,username,password);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		Information_Processing IP = new Information_Processing(conn);
		Reports r=new Reports(conn);
		Service s=new Service(conn);
		Billing_func b=new Billing_func(conn);
		System.out.println("Select an option");
		System.out.println("1.Information Processing\n2.Reports\n3.Services\n4.Billing\n0.end");
		Scanner reader = new Scanner(System.in);
		int input=0;
		input=reader.nextInt();
		while(true) {
			if(input==0) {
				System.out.println("Thank you for using the service");
				break;
			}
		switch(input) {
		case 1:
			IP.menu();
			break;
		case 2:
			r.mymenu();
			break;
		case 3:
			s.menu();
			break;
		case 4:
			b.billing_menu();
			break;
		}
		System.out.println("\n\n\nSelect an option");
		System.out.println("1.Information Processing\n2.Reports\n3.Services\n4.Billing\n0.end\n");
			if (reader.hasNextInt())
				input = reader.nextInt();
		}
		IP.close();
		r.close();
		b.close();
		s.close();
		reader.close();
	}
}
