DBMS
Hotel Management System

Project Narrative: Task is to design and build a management system for Wolf Inns, a popular hotel chain, with hotels in various cities across the country. The system will be used by the management of Wolf Inns, and should maintain the information on at least the followings:

• Hotel information: hotel ID, hotel name, hotel address, hotel phone number, ID of the hotel manager;

• Customer information: customer name, date of birth, contact information (phone number and email address);

• Staff information: staff ID, name, age, job title (Manager, Front Desk Representative, Room Service Staff, Billing Staff, Catering Staff, etc), department, contact information (phone number and address), hotel currently serving;

• Room information: room number, room category (Economy, Deluxe, Executive Suite, Presidential Suite, etc), max allowed occupancy, nightly rate, availability;

• Billing information: customer ID, SSN of the person responsible for the payment, billing address, payment information (payment method, card number, etc);

• Check-in information: customer name and date of birth, hotel ID, room number, number of guests, start date, end date, check-in time, check-out time, services offered.

By talking to the hotel operators, we have elicited for you the following information about Wolf Inns. (Note that in working on this project, you might discover that not every bit of the information has to be captured in the database. Part of the modeling effort is to decide what to keep and what to discard. In doing your project, you will need to make additional assumptions, as well as identify the potential inconsistencies and resolve them.Any reasonable assumptions are fine, but they must be documented in your reports. You can consult with the TAs or instructor if you have questions about the assumptions.)

• Front desk representatives can register customers, can process check-ins/checkouts, assign available rooms, and bill customers.

• Room prices vary by location and by the class of services offered. Rooms are classified as Economy, Deluxe, Executive Suites, and Presidential Suites. Each room has a 1,2,3 or 4 occupant capacity.

• Not all classes of rooms are available in all the hotels.

• At time of check-in, the presidential suite is assigned dedicated room-service staff as well as catering staff.

• Billing information should be entered at check-in time.

• The final bill, which is to be paid at customer check-out time, includes the price of all the services used during the stay. Tasks and Operations

The following are the four major kinds of tasks that need to be performed using your database. Each task potentially consists of a number of operations; an “operation” is something that corresponds to a separate action. For example, information processing is considered to be one task, which involves individual operations, such as entering and updating information about customers.

Information Processing: Enter/update/delete basic information about hotels, rooms, staff, and customers. Check if room(s) and room type requested are available. Assign rooms to customers according to their requests and to availability. Release rooms.
Maintaining Service Records: For each customer stay, enter/update service records for services such as phone bills, dry cleaning, gyms, room service, and special requests.
Maintaining billing accounts: Generate/maintain billing accounts for each customer stay. When generating bills, take into account that customers that pay with the hotel credit card get a 5% discount. At check-out time, return the total amount owed by the customer, as well as an itemized receipt.
Reports: Report occupancy by hotel, room type, date range, and city. Report total occupancy and percentage of rooms occupied, return information on staff grouped by their role. For each customer stay, return information on all the staff members serving the customer during the stay. Generate revenue earned by a given hotel during a given date range.
