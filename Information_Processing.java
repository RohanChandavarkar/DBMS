//package jdbc_demo;

import java.sql.*;
import java.util.*;

/* this particular class deals with information processing mainly categorized as
 * insert/update/delete hotel/staff/room/customer
 */
public class Information_Processing {
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

	public Information_Processing(Connection conn) {
		try {
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// this close() function closed the result set, statement and connection
	public void close() {
		close(rs);
		close(stmt);
		close(conn);
	}

	public void menu() {
		// asking the user to choose from the list
		System.out.println("Information processing\n");
		System.out.println("Enter a number according to the list to perform that task:\n" + "1.enter a new location\n"
				+ "2.update location details\n" + "3.delete location\n" + "4.enter a new hotel\n"
				+ "5.update existing hotel\n" + "6.delete hotel\n" + "7.enter a staff\n" + "8.update staff details\n"
				+ "9.delete staff\n" + "10.enter a customer\n" + "11.update customer details\n" + "12.delete customer\n"
				+ "13.enter a room\n" + "14.update room details\n" + "15.delete room\n" + "16.show available rooms\n"
				+ "17.check if requested room available\n" + "18.change costfactor of occupancy and room type\n"
				+ "19.change/Add presidential dedicated staff catering\n"
				+ "20.change/Add presidential dedicated staff service\n" + "21.release room\n"
				+ "22.enter new costfactor of occupancy and room type\n");
		input = reader.nextInt();
		switch (input) {
		case 1:
			// enter a new location

			System.out.println("New Location:");
			System.out.println("Enter the following");
			System.out.println("city: ");
			reader.nextLine();
			city = reader.nextLine();
			System.out.println("cost: ");
			costfactor = reader.nextFloat();

			str = "insert into location(city, costfactor) values (?, ?)";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, city);
				stmt.setFloat(2, costfactor);
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
			// update location details
			System.out.println("Update Location:");
			System.out.println("Enter the following");
			System.out.println("city: ");
			reader.nextLine();
			city = reader.nextLine();
			System.out.println("cost: ");
			costfactor = reader.nextFloat();
			str = "update location set costfactor=? where city=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setFloat(1, costfactor);
				stmt.setString(2, city);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated succesfully");
				action = 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}

			break;
		case 3:
			// delete location
			System.out.println("Delete Location:");
			System.out.println("Enter the following");
			System.out.println("city: ");
			reader.nextLine();
			city = reader.nextLine();
			str = "delete from location where city=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, city);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("deleted succesfully");
				action = 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 4:
			// enter a new hotel

			System.out.println("New Hotel:");
			System.out.println("Enter the following");
			System.out.println("Name of hotel: ");
			reader.nextLine();
			hotel_name = reader.nextLine();
			System.out.println("phone_no: ");
			phone_no = reader.nextLong();
			System.out.println("address: ");
			reader.nextLine();
			address = reader.nextLine();
			System.out.println("city: ");
			city = reader.nextLine();
			str = "insert into hotel(name, phone_no, address, city) values (?,?,?,?)";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, hotel_name);
				stmt.setLong(2, phone_no);
				stmt.setString(3, address);
				stmt.setString(4, city);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("inserted successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// to return generated hotel_id for the hotel
			if (action == 1) {
				str = "select hotel_id from hotel where name=? and phone_no=? and address=? and city=?";
				try {
					stmt = conn.prepareStatement(str);
					stmt.setString(1, hotel_name);
					stmt.setLong(2, phone_no);
					stmt.setString(3, address);
					stmt.setString(4, city);
					rs = stmt.executeQuery();
					while (rs.next()) {
						System.out.println("the hotel_id for the new hotel:  " + rs.getInt(1));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			break;
		case 5:
			// update existing hotel

			System.out.println("Update Hotel:");
			System.out.println("Enter the following");
			System.out.println("hotel Id: ");
			hotel_id = reader.nextInt();
			System.out.println("name of the hotel: ");
			reader.nextLine();
			hotel_name = reader.nextLine();
			System.out.println("phone no: ");
			phone_no = reader.nextLong();
			System.out.println("address: ");
			reader.nextLine();
			address = reader.nextLine();

			str = "update hotel set name=?, phone_no=?, address=? where hotel_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, hotel_name);
				stmt.setLong(2, phone_no);
				stmt.setString(3, address);
				stmt.setInt(4, hotel_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated successfully");
				action = 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 6:
			// delete hotel
			System.out.println("Delete Hotel:");
			System.out.println("Enter the following");
			System.out.println("hotel Id:");
			hotel_id = reader.nextInt();
			str = "delete from hotel where hotel_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, hotel_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("deleted successfully");
				action = 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 7:
			// enter a staff

			System.out.println("New Staff:");
			System.out.println("Enter the following");
			System.out.println("hotel Id: ");
			hotel_id = reader.nextInt();
			System.out.println("name of staff: ");
			reader.nextLine();
			staff_name = reader.nextLine();
			System.out.println("phone no: ");
			phone_no = reader.nextLong();
			System.out.println("title");
			reader.nextLine();
			title = reader.nextLine();
			System.out.println("DOB (yyyy-mm-dd): ");
			dob = reader.next();
			str = "insert into staff( name, phone_no, title, DOB, hotel_id) " + "values (?,?,?,?,?)";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, staff_name);
				stmt.setLong(2, phone_no);
				stmt.setString(3, title);
				stmt.setString(4, dob);
				stmt.setInt(5, hotel_id);
				action = 0;
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("inserted successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// to return the staff_id of staff
			if (action == 1) {
				str = "select staff_id from staff where name=? and phone_no=? and title=? and dob=? and hotel_id=?";
				try {
					stmt = conn.prepareStatement(str);
					stmt.setString(1, staff_name);
					stmt.setLong(2, phone_no);
					stmt.setString(3, title);
					stmt.setString(4, dob);
					stmt.setInt(5, hotel_id);
					rs = stmt.executeQuery();
					while (rs.next()) {
						System.out.println("the staff_id of the new staff:  " + rs.getInt(1));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;
		case 8:
			// update staff details
			System.out.println("Update Staff:");
			System.out.println("Enter the following");
			System.out.println("staff Id: ");
			staff_id = reader.nextInt();
			System.out.println("name of the staff: ");
			reader.nextLine();
			staff_name = reader.nextLine();
			System.out.println("phone no: ");
			phone_no = reader.nextLong();
			System.out.println("DOB(yyyy-mm-dd): ");
			dob = reader.next();
			str = "update staff set name=?,phone_no=?,dob=? where staff_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, staff_name);
				stmt.setLong(2, phone_no);
				stmt.setString(3, dob);
				stmt.setInt(4, staff_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated successfully");

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 9:
			// delete staff
			System.out.println("Delete Staff:");
			System.out.println("Enter the following");
			System.out.println("staff Id: ");
			staff_id = reader.nextInt();
			str = "delete from staff where staff_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, staff_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("deleted successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 10:
			// enter a customer
			/*
			 * The use of transactions is important to do the following: 
			 * 1.the customer books a room in the hotel
			 * 2.the front desk staff who assigns the room to customer-- a record of is inserted into serves i.e. the staff serving customer
			 * 3.checking if the room is presidential
			 * 4.if presidential then assigned dedicated catering and service staff
			 * 5.checking if the catering and service staff are not assigned to other presidential rooms.
			 * 
			 * If any one of the above fails then the customer record is not entered into the database and 
			 * statements are rolled back, leaving the database consistent.
			 */

			System.out.println("New Customer:");
			System.out.println("Enter the following");
			System.out.println("staff id of the front desk booking the room for the customer: ");
			staff_id = reader.nextInt();
			System.out.println("name of customer: ");
			reader.nextLine();
			customer_name = reader.nextLine();
			System.out.println("DOB: ");
			dob = reader.next();
			System.out.println("phone no: ");
			phone_no = reader.nextLong();
			System.out.println("email Id: ");
			email_id = reader.next();
			System.out.println("ssn: ");
			ssn = reader.nextInt();
			System.out.println("room_no: ");
			room_no = reader.nextInt();
			System.out.println("hotel_id: ");
			hotel_id = reader.nextInt();
			System.out.println("guest count: ");
			guest_count = reader.nextInt();
			System.out.println("check in time(hh:mm:ss): ");
			time = reader.next();
			System.out.println("Start date(yyyy-mm-dd): ");
			date = reader.next();
			str = "select * from room where room_no=? and hotel_id=? and availability=1;";
			try {
				// to check if the customer is booking the room that is available
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, room_no);
				stmt.setInt(2, hotel_id);
				rs = stmt.executeQuery();
				if (!rs.next()) {
					System.out.println("Sorry!! the room is not available");
					break;
				} else {

					str = "insert into customer (name, dob, phone_number, email, ssn, room_no, hotel_id, "
							+ "guest_count, check_in, start_date) values (?,?,?,?,?,?,?,?,?,?)";
					try {
						//setting auto commit to false
						conn.setAutoCommit(false);
						stmt = conn.prepareStatement(str);
						stmt.setString(1, customer_name);
						stmt.setString(2, dob);
						stmt.setLong(3, phone_no);
						stmt.setString(4, email_id);
						stmt.setInt(5, ssn);
						stmt.setInt(6, room_no);
						stmt.setInt(7, hotel_id);
						stmt.setInt(8, guest_count);
						stmt.setString(9, time);
						stmt.setString(10, date);
						action = 0;
						action = stmt.executeUpdate();
						if (action == 1)
							System.out.println("inserted successfully");

						if (action == 1) {
							try {
								// to return the generated customer_id for the customer
								str = "select customer_id from customer where name=? and dob=? and phone_number=? and room_no=? and hotel_id=?";
								stmt = conn.prepareStatement(str);
								stmt.setString(1, customer_name);
								stmt.setString(2, dob);
								stmt.setLong(3, phone_no);
								stmt.setInt(4, room_no);
								stmt.setInt(5, hotel_id);
								rs = stmt.executeQuery();
								while (rs.next()) {
									customer_id = rs.getInt(1);
									System.out.println("the Id of the customer:  " + rs.getInt(1));
									System.out.println(" The customer booked the room successfully");
								}
							}catch (SQLException e) {
								if (conn != null) {
									try {
										System.out.println("there is some issue with details of customer");
										//rolling back in case of any error
										conn.rollback();
										//and then auto committing it
										conn.setAutoCommit(true);
										return;
									} catch (SQLException e1) {

										e.printStackTrace();
										return;
									}
								}
							}
							// insert into serves the front desk representative id and customer id
							str = "insert ignore into serves (staff_id,customer_id) values(?,?)";
							try {
								stmt = conn.prepareStatement(str);
								stmt.setInt(1, staff_id);
								stmt.setInt(2, customer_id);
								stmt.executeUpdate();
							} catch (SQLException e) {
								if (conn != null) {
									try {
										System.out.println("there is issue with the staff id entered");
										//rolling back in case of any error
										conn.rollback();
										//and then auto committing it
										conn.setAutoCommit(true);
										return;
									} catch (SQLException e1) {

										e.printStackTrace();
										return;
									}
								}
							}
							// checking if customer has booked presidential
							str = "select category_name from room where room_no=? and hotel_id=?";
							try {
								stmt = conn.prepareStatement(str);
								stmt.setInt(1, room_no);
								stmt.setInt(2, hotel_id);
								rs = stmt.executeQuery();
								if (rs.next()) {
									// the room customer booked presidential room, so catering and service staff
									// must be assigned
									if (rs.getString(1).equals("presidential")) {
										System.out.println("Assign catering staff and service staff to the customer:");
										System.out.println("Enter the following:");
										System.out.println("catering staff Id: ");
										catering = reader.nextInt();
										System.out.println("service staff Id: ");
										service = reader.nextInt();
										str = "update presidential set catering_staff_id=?, service_staff_id=? where room_no=? and hotel_id=?";
										try {
											stmt = conn.prepareStatement(str);
											stmt.setInt(1, catering);
											stmt.setInt(2, service);
											stmt.setInt(3, room_no);
											stmt.setInt(4, hotel_id);
											action = stmt.executeUpdate();
											System.out.println("successfully assigned staff");
										} catch (SQLException e) {
											if (conn != null) {
												try {
													System.out.println("there is issue setting presidential room dedicated staff");
													//rolling back in case of any error
													conn.rollback();
													//and then auto committing it
													conn.setAutoCommit(true);
													return;
												} catch (SQLException e1) {

													e.printStackTrace();
													return;
												}
											}
										}
										if (action == 1) {
											try {
												// inserting catering staff id and customer id in serves
												str = "insert ignore into serves (staff_id,customer_id) values(?,?)";
												stmt = conn.prepareStatement(str);
												stmt.setInt(1, catering);
												stmt.setInt(2, customer_id);
												stmt.executeUpdate();
											} catch (SQLException e) {
												if (conn != null) {
													try {
														System.out.println("there is issue with entering record into serves table");
														//rolling back in case of any error
														conn.rollback();
														//and then auto committing it
														conn.setAutoCommit(true);
														return;
													} catch (SQLException e1) {

														e.printStackTrace();
														return;
													}
												}
											}
											try {
												// inserting service staff id and customer id in serves
												str = "insert ignore into serves (staff_id,customer_id) values(?,?)";
												stmt = conn.prepareStatement(str);
												stmt.setInt(1, service);
												stmt.setInt(2, customer_id);
												stmt.executeUpdate();
											} catch (SQLException e) {
												if (conn != null) {
													try {
														System.out.println("there is issue with entering record into serves table");
														//rolling back in case of any error
														conn.rollback();
														//and then auto committing it
														conn.setAutoCommit(true);
														return;
													} catch (SQLException e1) {

														e.printStackTrace();
														return;
													}
												}
											}

										}
									}
								}

							} catch (SQLException e) {
								if (conn != null) {
									try {
										System.out.println("there is issue with querying the database about the room type");
										//rolling back in case of any error
										conn.rollback();
										//and then auto committing it
										conn.setAutoCommit(true);
										return;
									} catch (SQLException e1) {

										e.printStackTrace();
										return;
									}
								}
							}
							
						}
						conn.commit();
						//setting autocommit to true
						conn.setAutoCommit(true);
					} catch (SQLException e) {
						if (conn != null) {
							try {
								System.out.println("there is issue with the details of the customer");
								//rolling back in case of any error
								conn.rollback();
								//and then auto committing it
								conn.setAutoCommit(true);
								return;
							} catch (SQLException e1) {

								e.printStackTrace();
								return;
							}
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 11:
			// update customer details
			System.out.println("Update Customer:");
			System.out.println("Enter the following");
			System.out.println("customer Id: ");
			customer_id = reader.nextInt();
			System.out.println("customer name: ");
			reader.nextLine();
			customer_name = reader.nextLine();
			System.out.println("DOB: ");
			dob = reader.next();
			System.out.println("phone no: ");
			phone_no = reader.nextLong();
			System.out.println("email id: ");
			email_id = reader.next();
			System.out.println("ssn: ");
			ssn = reader.nextInt();
			str = "update customer set name=?, dob=?, phone_number=?, email=?, ssn=? where customer_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, customer_name);
				stmt.setString(2, dob);
				stmt.setLong(3, phone_no);
				stmt.setString(4, email_id);
				stmt.setInt(5, ssn);
				stmt.setInt(6, customer_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 12:
			// delete customer
			System.out.println("Delete Customer");
			System.out.println("Enter the following");
			System.out.println("customer Id: ");
			customer_id = reader.nextInt();
			str = "delete from customer where customer_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, customer_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("deleted successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 13:
			// enter a room

			System.out.println("New Room:");
			System.out.println("Enter the following");
			System.out.println("room number: ");
			room_no = reader.nextInt();
			System.out.println("hotel Id: ");
			hotel_id = reader.nextInt();
			System.out.println("maximum no of guests(1/2/3/4): ");
			max_limit = reader.nextInt();
			System.out.println("type of room(economy/deluxe/executive/presidential): ");
			category_name = reader.next();
			str = "insert into room(room_no, hotel_id, max_limit, category_name) values (?,?,?,?)";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, room_no);
				stmt.setInt(2, hotel_id);
				stmt.setInt(3, max_limit);
				stmt.setString(4, category_name);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("inserted successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 14:
			// update room details
			System.out.println("Update Room");
			System.out.println("Enter the following");
			System.out.println("room number: ");
			room_no = reader.nextInt();
			System.out.println("hotel Id: ");
			hotel_id = reader.nextInt();
			System.out.println("maximum no of guests(1/2/3/4): ");
			max_limit = reader.nextInt();
			System.out.println("type of room(economy/deluxe/executive/presidential): ");
			category_name = reader.next();
			str = "update room set max_limit=?,category_name=? where room_no=? and hotel_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, max_limit);
				stmt.setString(2, category_name);
				stmt.setInt(3, room_no);
				stmt.setInt(4, hotel_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 15:
			// delete room
			System.out.println("Delete Room:");
			System.out.println("Enter the following");
			System.out.println("room no: ");
			room_no = reader.nextInt();
			System.out.println("hotel Id: ");
			hotel_id = reader.nextInt();
			str = "delete from room where room_no=? and hotel_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, room_no);
				stmt.setInt(2, hotel_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("deleted successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 16:
			// show available rooms
			System.out.println("Show available rooms:");
			System.out.println("Enter the following");
			System.out.println("hotel Id: ");
			hotel_id = reader.nextInt();
			str = "select room_no,max_limit,category_name,rate from room where hotel_id=? and availability=1;";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, hotel_id);
				rs = stmt.executeQuery();
				System.out.format("%10s\t%12s\t%20s\t%5s\n\n", "room no", "no of guests", "room type", "rate($)");
				while (rs.next()) {
					System.out.format("%10d\t%12d\t%20s\t%5.2f\n", rs.getInt(1), rs.getInt(2), rs.getString(3),
							rs.getFloat(4));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 17:
			// check if requested room is available
			System.out.println("Check requested room available:");
			System.out.println("Enter the following");
			System.out.println("hotel Id: ");
			hotel_id = reader.nextInt();
			System.out.println("no of guests(1/2/3/4): ");
			max_limit = reader.nextInt();
			System.out.println("room type(economy,deluxe,executive,presidential): ");
			category_name = reader.next();
			str = "select room_no,rate from room where max_limit=? and category_name=? and hotel_id=? and availability=1;";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, max_limit);
				stmt.setString(2, category_name);
				stmt.setInt(3, hotel_id);
				rs = stmt.executeQuery();
				if (!rs.next()) {
					System.out.println("Sorry!! No rooms that meet your requirement");
				} else {
					System.out.format("%10s\t%5s\n\n", "room no", "rate($)");
					do {
						System.out.format("%10d\t%5.2f\n", rs.getInt(1), rs.getFloat(2));
					} while (rs.next());
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 18:
			// change costfactor of occupancy and room type
			System.out.println("Change of cost factor of occupancy and room type:");
			System.out.println("Enter the following");
			System.out.println("occupancy limit: ");
			max_limit = reader.nextInt();
			System.out.println("Room Type: ");
			category_name = reader.next();
			System.out.println("Costfactor: ");
			float costfactor = reader.nextFloat();
			str = "update cost set costfactor=? where max_limit=? and category_name=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setFloat(1, costfactor);
				stmt.setInt(2, max_limit);
				stmt.setString(3, category_name);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 19:
			// change presidential dedicated staff catering
			System.out.println("Change Presidential room dedicated catering staff: ");
			System.out.println("Enter the following");
			System.out.println("customer Id who booked the room: ");
			customer_id = reader.nextInt();
			System.out.println("room no: ");
			room_no = reader.nextInt();
			System.out.println("hotel_id: ");
			hotel_id = reader.nextInt();
			System.out.println("catering staff id: ");
			catering = reader.nextInt();
			str = "update presidential set catering_staff_id=? where room_no=? and hotel_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, catering);
				stmt.setInt(2, room_no);
				stmt.setInt(3, hotel_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (action == 1) {
				try {
					str = "insert ignore into serves (staff_id,customer_id) values(?,?)";
					stmt = conn.prepareStatement(str);
					stmt.setInt(1, catering);
					stmt.setInt(2, customer_id);
					stmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			break;
		case 20:
			// change presidential dedicated staff service
			System.out.println("Change Presidential room dedicated catering staff: ");
			System.out.println("Enter the following");
			System.out.println("customer Id who booked the room: ");
			customer_id = reader.nextInt();
			System.out.println("room no: ");
			room_no = reader.nextInt();
			System.out.println("hotel_id: ");
			hotel_id = reader.nextInt();
			System.out.println("service staff id: ");
			service = reader.nextInt();
			str = "update presidential set service_staff_id=? where room_no=? and hotel_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setInt(1, service);
				stmt.setInt(2, room_no);
				stmt.setInt(3, hotel_id);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("updated successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (action == 1) {
				try {
					str = "insert ignore into serves (staff_id,customer_id) values(?,?)";
					stmt = conn.prepareStatement(str);
					stmt.setInt(1, service);
					stmt.setInt(2, customer_id);
					stmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;
		case 21:
			// Release room
			System.out.println("Release room: ");
			System.out.println("Enter the following");
			System.out.println("Customer Id: ");
			customer_id = reader.nextInt();
			System.out.println("Check out time(hh:mm:ss): ");
			reader.nextLine();
			time = reader.nextLine();
			System.out.println("End date(yyyy-mm-dd): ");
			date = reader.nextLine();
			System.out.println("Enter room no: ");
			room_no = reader.nextInt();
			System.out.println("Enter hotel id: ");
			hotel_id = reader.nextInt();
			str = "update customer set end_date=?,check_out=? where customer_id=?";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setString(1, date);
				stmt.setString(2, time);
				stmt.setInt(3, customer_id);
				action = stmt.executeUpdate();
				if (action == 1) {
					System.out.println("Customer has checkout and the room is released successfully");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			break;
		case 22:
			// enter new costfactor of occupancy and room type
			System.out.println("Change of cost factor of occupancy and room type:");
			System.out.println("Enter the following");
			System.out.println("occupancy limit: ");
			max_limit = reader.nextInt();
			System.out.println("Room Type: ");
			category_name = reader.next();
			System.out.println("Costfactor: ");
			costfactor = reader.nextFloat();
			str = "insert into cost(costfactor,max_limit,category_name) values(?,?,?)";
			try {
				stmt = conn.prepareStatement(str);
				stmt.setFloat(1, costfactor);
				stmt.setInt(2, max_limit);
				stmt.setString(3, category_name);
				action = stmt.executeUpdate();
				if (action == 1)
					System.out.println("inserted successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("choose option from the above");
			break;
		}

	}

	// to close the connection, statement, resultset
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
