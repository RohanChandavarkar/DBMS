//package jdbc_demo;

import java.sql.*;
import java.util.*;
/* this particular class deals with service and has options such as
 * 1.Enter a new service
 * 2.Update the charges for a service
 * 3.delete service 
 * 4.Enter service used
 * 5.Entering the service staff for a customer
 */
public class Service {
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	static String str, staff_name, title, dob, category_name, hotel_name, address, customer_name, email_id, time, date,
			service_name, period, amount;
	static int input = 0, staff_id, ssn, guest_count, room_no, catering, service, hotel_id, max_limit;
	float charge = 0;
	static long phone_no;
	int customer_id;
	int action;
	static Scanner reader = new Scanner(System.in);

	public Service(Connection conn) {
		try {
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// to close connection, result set and statement
	public void close() {
		close(rs);
		close(stmt);
		close(conn);
	}

	public void menu() {
		// list of options for the user to choose from services
		System.out.println("Maintaining Service Records\n");
		System.out.println("Enter a number according to the list to perform that task:\n" 
				+ "1.Enter a new service\n"
				+ "2.Update the charges for a service\n"
				+ "3.delete service\n" 
				+ "4.Enter service used\n"
				+ "5.Entering the service staff for a customer\n");

		input = reader.nextInt();
		switch (input) {
		case 1:
			// enter a new service
			System.out.println("New Service:");
			System.out.println("Enter the following");
			System.out.println("Service Name: ");
			String myservice = reader.next();
			System.out.println("Charge : ");
			int mycharge = reader.nextInt();
			System.out.println("Period : ");
			String myperiod = reader.next();

			str = "insert into services(service_name, charges, period) values (?, ?, ?)";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, myservice);
				stmt.setFloat(2, mycharge);
				stmt.setString(3, myperiod);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("inserted succesfully");
				action = 0;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("insert failed");
			}
			break;
		case 2:
			// update charges in the existing service
			System.out.println("Update charges:");
			System.out.println("Enter the following");
			System.out.println("Service Name: ");
			reader.nextLine();
			service_name = reader.nextLine();
			System.out.println("Charges: ");
			charge = reader.nextFloat();
			str = "update services set charges=? where service_name=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setFloat(1, charge);
				stmt.setString(2, service_name);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated succesfully");
				action = 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}

			break;
		case 3:
			// delete service
			System.out.println("Delete Services:");
			System.out.println("Enter the following");
			System.out.println("Service Name: ");
			reader.nextLine();
			service_name = reader.next();
			str = "delete from services where service_name=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, service_name);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("deleted succesfully");
				action = 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 4:
			// enter service used
			System.out.println("Entering the service used:");
			System.out.println("Enter the following");
			System.out.println("Service Name: ");
			reader.nextLine();
			service_name = reader.nextLine();
			System.out.println("Customer id : ");
			customer_id = reader.nextInt();
			str = "insert ignore into uses(service_name, customer_id,times,amount) select ? ,?, 1, charges from services where service_name=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, service_name);
				stmt.setInt(2, customer_id);
				stmt.setString(3, service_name);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("inserted succesfully");
				else
					System.out.println("record already exists in database");

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("insert failed");
			}
			break;
		case 5:
			// enter staff service for a customer
			System.out.println("Entering the service used:");
			System.out.println("Enter the following");
			System.out.println("Staff id: ");
			// reader.nextLine();
			staff_id = reader.nextInt();
			System.out.println("Customer id : ");
			customer_id = reader.nextInt();
			str = "insert ignore into serves(staff_id, customer_id) values (?, ?)";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, staff_id);
				stmt.setInt(2, customer_id);
				action = stmt.executeUpdate();
				System.out.println("inserted succesfully");

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("insert failed");
			}
			break;
		default:
			System.out.println("choose option from the above");
			break;
		}

	}

	// to close connection, statement, and result set
	static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Throwable whatever) {
			}
		}
	}

	static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (Throwable whatever) {
			}
		}
	}

	static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Throwable whatever) {
			}
		}
	}

}
