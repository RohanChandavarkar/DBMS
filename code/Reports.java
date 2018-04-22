//package jdbc_demo;

import java.sql.*;
import java.util.*;

/*This class deals with reports where the following are done
 * 1.report occupancy by city
 * 2.report occupancy by hotel_id
 * 3.report occupancy by room type
 * 4.Rooms occupancy by date range
 * 5.staff grouped by role
 * 6.staff at customer service
 * 7.revenue
 */

public class Reports {
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	static String str, staff_name, title, dob, category_name, hotel_name, address, customer_name, email_id, time, date,
			city;
	static int input = 0, customer_id, staff_id, ssn, guest_count, room_no, catering, service, hotel_id, max_limit;
	static float costfactor;
	static long phone_no;
	int action;

	static Scanner reader = new Scanner(System.in);

	public Reports(Connection conn) {
		try {
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		// closes the connection, statement and result set.
		close(rs);
		close(stmt);
		close(conn);
	}

	public void mymenu() {
		System.out.println("Reports\n");
		//list of options for the user to choose in reports
		System.out.println("Enter a number according to the list to perform that task:\n"
				+ "1.report occupancy by city\n" 
				+ "2.report occupancy by hotel_id\n"
				+ "3.report occupancy by room type\n" 
				+ "4.Rooms occupancy by date range\n"
				+ "5.staff grouped by role\n" 
				+ "6.staff at customer service\n" 
				+ "7.revenue");
		input = reader.nextInt();
		switch (input) {
		case 1:
			// occupancy by city
			str = "select b.city,coalesce(booked,0) as booked , total, cast((coalesce(booked, 0)/total* 100)as "
					+ "decimal(10,0)) as occupancy_percentage from (select city,COUNT(Availability) as booked from hotel "
					+ "natural join room where Availability =0 group by city) as a  natural right outer join"
					+ "(select city,count(*) as total from hotel natural join room group by city)as "
					+ "b order by b.city";

			try {
				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				System.out.println("occupied rooms group by city: ");
				System.out.format("%20s\t%10s\t%10s\t%s\t\n", "city", "booked", "total", "occupancy percentage");
				while (rs.next()) {
					System.out.format("%20s\t%10d\t%10d\t%10.2f\n", rs.getNString(1), rs.getInt(2), rs.getInt(3),
							rs.getFloat(4));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong .please retry");
			}
			break;

		case 2:
			// occupancy by hotel using hotel_id
			str = "select b.hotel_id,coalesce(booked,0) as booked , total, cast((coalesce(booked, 0)/total* 100)as "
					+ "decimal(10,0)) as occupancy_percentage from (select hotel_id,COUNT(Availability) as booked from hotel "
					+ "natural join room where Availability =0 group by hotel_id) as a  natural right outer join"
					+ "(select hotel_id,count(*) as total from hotel natural join room group by hotel_id)as "
					+ "b order by b.hotel_id";
			try {
				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				System.out.println("occupied rooms by hotel-id");
				System.out.format("%10s\t%10s\t%10s\t%s\t\n", "hotel_id", "booked", "total", "occupancy percentage");
				while (rs.next()) {
					System.out.format("%10s\t%10d\t%10d\t%10.2f\n", rs.getInt(1), rs.getInt(2), rs.getInt(3),
							rs.getFloat(4));

				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong ,please try again");
			}
			break;

		case 3:
			// occupancy by room type
			str = "select b.category_name,coalesce(booked,0) as booked , total, cast((coalesce(booked, 0)/total* 100)as "
					+ "decimal(10,0)) as occupancy_percentage from (select category_name,COUNT(Availability) as booked from "
					+ " room where Availability =0 group by category_name) as a  natural right outer join"
					+ "(select category_name,count(*) as total from room group by category_name)as "
					+ "b order by b.category_name";

			try {

				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				System.out.println("occupied rooms by category:");
				System.out.format("%20s\t%10s\t%10s\t%s\t\n", "category", "booked", "total", "occupancy percentage");
				while (rs.next()) {
					System.out.format("%20s\t%10d\t%10d\t%10.2f\n", rs.getNString(1), rs.getInt(2), rs.getInt(3),
							rs.getFloat(4));

				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong ,please try again");
			}
			break;

		case 4:
			// occupancy by date range
			str = "select start_date, end_date,count(*) as booked, a.total, round(count(*)/a.total*100 ,2) as occupancy_percentage from customer,"
					+ "(select count(*) as total from room)as a group by start_date ,end_date;";

			try {

				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();

				System.out.println("Occupied rooms by start date and end date:");
				System.out.format("%20s\t%20s\t%10s\t%10s\t%s\n", "start-date", "end-date", "booked", "total",
						"occupancy percentage");
				while (rs.next()) {
					System.out.format("%20s\t%20s\t%10d\t%10d\t%10.2f\n", rs.getString(1), rs.getString(2),
							rs.getInt(3), rs.getInt(4), rs.getFloat(5));

				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong ,please try again");
			}
			break;

		case 5:
			// list of staff grouped by their role
			str = "SELECT title,staff_id,name from staff order by title";

			try {

				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				System.out.println("staff list:");
				System.out.format("%20s\t%10s\t%20s\t\n", "title", "id", "name");
				while (rs.next()) {
					System.out.format("%20s\t%10d\t%20s\n", rs.getNString(1), rs.getInt(2), rs.getNString(3));

				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong ,please try again");
			}
			break;

		case 6:
			// list of staff that served particular customer during their stay
			System.out.println("enter the customer_id");
			int customer_id = reader.nextInt();
			str = "select staff_id, name, title from serves natural join staff where customer_id = ?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, customer_id);
				rs = stmt.executeQuery();
				System.out.println("customer service staff list:");
				System.out.format("%10s\t%20s\t%20s\t\n", "staff_id", "name", "title");
				while (rs.next()) {
					System.out.format("%10s\t%20s\t%20s\t\n", rs.getInt(1), rs.getNString(2), rs.getString(3));

				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong ,please try again");
			}
			break;

		case 7:
			// revenue of the particular hotel with in the given date range
			System.out.println("enter the hotel id");
			int hotel_id = reader.nextInt();
			System.out.println("enter the start date(YYYY-MM-DD)");
			String dateString1 = reader.next();
			System.out.println("enter the end date(YYYY-MM-DD)");
			String dateString2 = reader.next();
			str = "select round(sum(total),2) from bill where customer_id in(select customer_id from customer where hotel_id=?)"
					+ "AND bill_date between ? and ?";

			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, hotel_id);
				stmt.setString(2, dateString1);
				stmt.setString(3, dateString2);
				rs = stmt.executeQuery();
				while (rs.next()) {
					System.out.println("revenue: " + rs.getFloat(1));

				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong ,please try again");
			}
			break;

		default:
			System.out.println("choose option from the above");
			break;

		}
	}

	// closing the connection, statement and result set
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
