//package jdbc_demo;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/*
 * This particular section deals with printing itemized bill and performing 
 * calculations for final bill amount after taking the
 * mode of payment as input from the customers.
 */

public class Billing_func {
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	static String str, staff_name, title, dob, category_name, hotel_name, address, customer_name, email_id, time, date,
			city, bill_address, cust_payment_mode;
	static int input = 0, customer_id, staff_id, ssn, guest_count, room_no, catering, service, hotel_id, max_limit;
	static float costfactor, bill_amt;
	static long phone_no;
	int action;

	static Scanner reader = new Scanner(System.in);

	public Billing_func(Connection conn) {
		try {
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		close(rs);
		close(stmt);
		close(conn);
	}

	public void billing_menu() throws Exception {

		System.out.println("BILLING TASKS");
		/*
		 * Input : We take the customer's ID for whom we will perform the billing
		 * actions as input.
		 */
		System.out.println("Enter the Customer Id whose Billing tasks need to be performed:");
		customer_id = reader.nextInt();

		/*
		 * The below are the options that the staff will get as options for performing
		 * billing tasks. The staff can either print the itemized bill or calculate the
		 * total bill or exit from the Billing section altogether.
		 */
		System.out.println("Enter a number according to the list to perform that task:\n" + "1.Print Itemised Bill\n"
				+ "2.Perform Final Bill Calculation\n" + "3.Exit\n");
		input = reader.nextInt();

		switch (input) {
		case 1:

			/*
			 * This section prints the Itemized bill of the customer .
			 */
			System.out.println("Print Itemized Bill:");

			str = "(select 'room price' as description,rate*(datediff(End_date,Start_date)) as amount from customer natural join room where customer_id=?)"
					+ " UNION" + " (select service_name ,amount from uses where customer_id=?)" + " UNION"
					+ " (select 'amount',round(amount,2) from bill where customer_id=?)" + " UNION"
					+ " (select 'discount%',coalesce(discount,0) from bill where customer_id=?)" + " UNION"
					+ " (select 'total',round(total,2) from bill where customer_id=?);";

			try {

				stmt = conn.prepareStatement(str);

				stmt.setInt(1, customer_id);
				stmt.setInt(2, customer_id);
				stmt.setInt(3, customer_id);
				stmt.setInt(4, customer_id);
				stmt.setInt(5, customer_id);

				rs = stmt.executeQuery();
				System.out.printf("%10s %10s\n", "Description", "Amount");
				while (rs.next()) {

					String desc = rs.getString("description");
					float amount = rs.getFloat("amount");
					System.out.format("%10s %10s\n", desc, amount);
					action = 1;
				}

				if (action == 1)
					System.out.println("Itemised Bill Printed Successfully");
				action = 0;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Itemised Bill Print Failed");
			}
			break;

		case 2:
			/*
			 * We are in this case, because here we would calculate the total bill of the
			 * customer after taking the payment mode and the billing address of the
			 * customer as input.
			 */

			System.out.println("Enter the billing address of the customer:");
			reader.nextLine();
			bill_address = reader.nextLine();

			/*
			 * Here we update the bill address of the customer
			 */
			str = "update bill set bill_address =? where customer_id = ?;";
			stmt = conn.prepareStatement(str);
			stmt.setString(1, bill_address);
			stmt.setInt(2, customer_id);
			action = stmt.executeUpdate();

			/*
			 * In the immediate below section, we scan the customer table to get the end
			 * date and name of the customer.
			 */
			str = "select * from customer where customer_id = ?;";
			stmt = conn.prepareStatement(str);
			stmt.setInt(1, customer_id);
			rs = stmt.executeQuery();
			Date end_date = null;
			String name_temp = null;
			while (rs.next()) {
				end_date = rs.getDate("end_date");
				name_temp = rs.getString("name");
			}

			/*
			 * If the end date is found to be not null, that is, the customer has already
			 * checked out, then only we proceed with the final bill calculation. Here we
			 * are not taking into account any other consideration other than end date not
			 * equals null.
			 */
			if (end_date != null) {
				/*
				 * Calculating the initial bill amount before any payment method got selected.
				 */
				System.out.println("Calculating Initial Bill Amount.....");
				str = "UPDATE bill SET bill.amount= (select rate*(datediff(End_date,Start_date)) from customer natural join room where"
						+ " customer_id=?)+(select coalesce(sum(amount),0) from uses where customer_id=?), bill_date=(select end_date"
						+ " from customer where customer_id=?) WHERE customer_id =?;";
				try {

					stmt = conn.prepareStatement(str);

					stmt.setInt(1, customer_id);
					stmt.setInt(2, customer_id);
					stmt.setInt(3, customer_id);
					stmt.setInt(4, customer_id);
					action = stmt.executeUpdate();
					if (action == 1) {
						System.out.println("Initial Billing Calculation Performed");

					}
					action = 0;
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.print("Intial Billing Amount Calculation failed.");

				}

				/*
				 * In this immediate below section we are taking the mode of payment as input
				 */
				System.out.println("Please enter the bill payment mode of the customer(credit/card/cash):");
				cust_payment_mode = reader.nextLine();

				/*
				 * The next major if, deals with the case, if the customer wants to pay by hotel
				 * credit card, the corresponding else ,deals with debit/cash payment mode.
				 */

				if (cust_payment_mode.equalsIgnoreCase("credit")) {
					/*
					 * Here we first take the credit card number as input.
					 */
					System.out.println("Please provide the credit card number:");
					long card_num;
					card_num = reader.nextLong();
					try {

						/*
						 * Here we check whether the entered credit card is a valid one in the system.
						 */
						str = "select * from card where Card_no = ?;";
						stmt = conn.prepareStatement(str);
						stmt.setLong(1, card_num);
						rs = stmt.executeQuery();
					} catch (SQLException e) {
						System.out.println("No such card number exist please try another mode of payment.");
						e.printStackTrace();
					}

					float balance;

					if (rs.next()) {

						long card_num_ver = rs.getLong("card_no");
						String card_name = rs.getString("name");

						/*
						 * Validating if credit card number matches with the input card number and also
						 * whether the customer name matches.
						 */
						if ((card_num_ver == card_num) && (card_name.equals(name_temp))) {
							balance = rs.getFloat("balance");

							str = "select * from bill where customer_id = ?;";
							stmt = conn.prepareStatement(str);
							stmt.setInt(1, customer_id);
							rs = stmt.executeQuery();
							if (rs.next()) {
								bill_amt = rs.getFloat("amount");
							}
							/*
							 * Validating if the credit balance is >= bill_amount
							 */
							if (bill_amt <= balance) {
								str = "UPDATE bill,card"
										+ " SET bill.discount=5, bill.Payment_type='hotel credit card', bill.total= bill.amount-(bill.amount*5/100), "
										+ " card.balance= card.Balance-(bill.amount-(bill.amount*5/100))"
										+ " where bill.customer_id=? and card_no=?;";
								try {
									//setting auto commit to false
									conn.setAutoCommit(false);
									stmt = conn.prepareStatement(str);
									stmt.setInt(1, customer_id);
									stmt.setLong(2, card_num_ver);
									action = stmt.executeUpdate();

									/*
									 * After payment via credit card, updating the credit balance. Transaction
									 * control implemented here
									 */
									str = "update customer set card_no=? where customer_id=?;";
									try {
										stmt = conn.prepareStatement(str);
										stmt.setLong(1, card_num_ver);
										stmt.setInt(2, customer_id);
										action = stmt.executeUpdate();
									} catch (SQLException e) {
										e.printStackTrace();
									}
									//committing the transaction
									conn.commit();
									//setting auto commit to true
									conn.setAutoCommit(true);
									System.out.println("the transaction happened successfully");
									action = 0;
								} catch (SQLException e) {
									e.printStackTrace();
									System.out.println("Failed to Update Bill with Paying through Credit Card Info");
									//in case of any error rolling back the transaction
									conn.rollback();
									//setting auto commit to true
									conn.setAutoCommit(true);
								}

								try {
									str = "select * from bill where customer_id = ?;";
									stmt = conn.prepareStatement(str);
									stmt.setInt(1, customer_id);
									rs = stmt.executeQuery();
									System.out.printf("%10s %10s %10s %10s %10s %10s %10s \n", "bill_id", "cust_id",
											"bill_date", "amount", "disc", "tot", "pay_type");
									while (rs.next()) {
										int bill_id = rs.getInt("Bill_id");
										int cust_id = rs.getInt("customer_id");
										Date bill_date = rs.getDate("Bill_date");
										float amount = rs.getFloat("Amount");
										int disc = rs.getInt("Discount");
										float tot = rs.getFloat("Total");
										String pay_type = rs.getString("Payment_type");

										// print the results
										System.out.format("%10s %10s %10s %10s %10s %10s %10s  \n", bill_id, cust_id,
												bill_date, amount, disc, tot, pay_type);

									}

								} catch (SQLException e) {
									e.printStackTrace();
								}

							} else {
								System.out.print("Card does not have enough balance to pay the entire bill.\n"
										+ "Please pay bill by card/cash.");
							}

						} else {
							if ((card_num_ver != card_num))
								System.out.println("Such a card does not exist in the system.");
							else if (card_num_ver == card_num) {
								if (!card_name.equals(name_temp))
									System.out.println("Customer's Name associated with the card number does "
											+ "not match with the name of the customer ID given");
							}

						}

					} else {
						System.out.println("Card Number was not Found in the system.");
					}
				} else {
					/*
					 * This section deals with the bill payment by debit card or cash.
					 */
					str = "UPDATE bill SET discount=0,payment_type=?, total=amount " + " where customer_id=?;";
					try {

						stmt = conn.prepareStatement(str);
						stmt.setString(1, cust_payment_mode);
						stmt.setInt(2, customer_id);
						action = stmt.executeUpdate();
						if (action == 1) {
							System.out.println("Updated Bill when paying through debit card/cash.");

						}
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("Failed to update bill payment via debit card/cash");

					}

					try {
						str = "select * from bill where customer_id = ?;";
						stmt = conn.prepareStatement(str);
						stmt.setInt(1, customer_id);
						rs = stmt.executeQuery();
						System.out.printf("%10s %10s %10s %10s %10s %10s %10s \n", "bill_id", "cust_id", "bill_date",
								"amount", "disc", "tot", "pay_type");
						while (rs.next()) {
							// System.out.println("Bill " + rs.getInt(1));
							int bill_id = rs.getInt("Bill_id");
							int cust_id = rs.getInt("customer_id");
							Date bill_date = rs.getDate("Bill_date");
							float amount = rs.getFloat("Amount");
							int disc = rs.getInt("Discount");
							float tot = rs.getFloat("Total");
							String pay_type = rs.getString("Payment_type");

							// print the results
							System.out.format("%10s %10s %10s %10s %10s %10s %10s  \n", bill_id, cust_id, bill_date,
									amount, disc, tot, pay_type);

						}

					} catch (SQLException e) {
						e.printStackTrace();
					}

				}

			} else {
				System.out.println("Customer has not yet checked out, total bill cannot be generated.");
			}
			break;
		case 3:
			System.out.println("Exiting the system...");
			break;
		default:
			break;

		}

	}

	/* closing the connection,statement and resultset */
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
