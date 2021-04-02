<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/*
|--------------------------------------------------------------------------
| File and Directory Modes
|--------------------------------------------------------------------------
|
| These prefs are used when checking and setting modes when working
| with the file system.  The defaults are fine on servers with proper
| security, but you may wish (or even need) to change the values in
| certain environments (Apache running a separate process for each
| user, PHP under CGI with Apache suEXEC, etc.).  Octal values should
| always be used to set the mode correctly.
|
*/
define('FILE_READ_MODE', 0644);
define('FILE_WRITE_MODE', 0666);
define('DIR_READ_MODE', 0755);
define('DIR_WRITE_MODE', 0755);

/*
|--------------------------------------------------------------------------
| File Stream Modes
|--------------------------------------------------------------------------
|
| These modes are used when working with fopen()/popen()
|
*/
define('FOPEN_READ', 'rb');
define('FOPEN_READ_WRITE', 'r+b');
define('FOPEN_WRITE_CREATE_DESTRUCTIVE', 'wb'); // truncates existing file data, use with care
define('FOPEN_READ_WRITE_CREATE_DESTRUCTIVE', 'w+b'); // truncates existing file data, use with care
define('FOPEN_WRITE_CREATE', 'ab');
define('FOPEN_READ_WRITE_CREATE', 'a+b');
define('FOPEN_WRITE_CREATE_STRICT', 'xb');
define('FOPEN_READ_WRITE_CREATE_STRICT', 'x+b');

/*
|--------------------------------------------------------------------------
| Display Debug backtrace
|--------------------------------------------------------------------------
|
| If set to TRUE, a backtrace will be displayed along with php errors. If
| error_reporting is disabled, the backtrace will not display, regardless
| of this setting
|
*/
define('SHOW_DEBUG_BACKTRACE', TRUE);

/*
|--------------------------------------------------------------------------
| Exit Status Codes
|--------------------------------------------------------------------------
|
| Used to indicate the conditions under which the script is exit()ing.
| While there is no universal standard for error codes, there are some
| broad conventions.  Three such conventions are mentioned below, for
| those who wish to make use of them.  The CodeIgniter defaults were
| chosen for the least overlap with these conventions, while still
| leaving room for others to be defined in future versions and user
| applications.
|
| The three main conventions used for determining exit status codes
| are as follows:
|
|    Standard C/C++ Library (stdlibc):
|       http://www.gnu.org/software/libc/manual/html_node/Exit-Status.html
|       (This link also contains other GNU-specific conventions)
|    BSD sysexits.h:
|       http://www.gsp.com/cgi-bin/man.cgi?section=3&topic=sysexits
|    Bash scripting:
|       http://tldp.org/LDP/abs/html/exitcodes.html
|
*/
define('EXIT_SUCCESS', 0); // no errors
define('EXIT_ERROR', 1); // generic error
define('EXIT_CONFIG', 3); // configuration error
define('EXIT_UNKNOWN_FILE', 4); // file not found
define('EXIT_UNKNOWN_CLASS', 5); // unknown class
define('EXIT_UNKNOWN_METHOD', 6); // unknown class member
define('EXIT_USER_INPUT', 7); // invalid user input
define('EXIT_DATABASE', 8); // database error
define('EXIT__AUTO_MIN', 9); // lowest automatically-assigned error code
define('EXIT__AUTO_MAX', 125); // highest automatically-assigned error code

//Pet constants

//Image Dir
//server path
define('UPLOAD_IMG_PATH','assets/images/');
//local path
//define('UPLOAD_IMG_PATH', '/opt/lampp/htdocs/petnew/images/');


//API Messages
define('USER_FOUND', 'user found');
define('USER_NOT_FOUND', 'We could not find this email or password combination. Please try again.');
define('USER_CONFIRMATION', 'Please confirm your email, you must be receive an email.');
define('PASSWORD_UPDATED', 'Password updated successfully');
define('PASSWORD_NOTMATCHED', 'Your old password did not match');
define('EMAIL_EXISTS', 'This email already exists');
define('USER_REGISTERED', 'Registration successfully');
define('EMAIL_NOT_EXISTS', 'Email not found');
define('BREED_NOT_FOUND', 'No Breed Found');
define('BREED_FOUND', 'Breed Found');
define('PET_ADDED', 'Pet added successfully');
define('PET_UPDATED', 'Pet updated successfully');
define('PET_ID_INVALID', 'Petid is not valid.');
define('APPOINTMENT_ADDED', 'Apointment added successfully...');
define('APPOINTMENT_UPDATED', 'Apointment updated successfully...');
define('APPOINTMENT_DELETED', 'Apointment deleted successfully...');
define('PET_SHARED', 'Pet Shared successfully...');
define('PET_ALREADY_SHARED', 'Pet already shared.');
define('PET_NOT_SHARED', 'Shared email not registered.');
define('IMAGE_ADDED', 'Image added successfully in gallery...');
define('GALLERY_LIST', 'Gallery List');
define('GALLERY_UPDATED', 'Gallery updated successfully...');
define('GALLERY_DELETED', 'Gallery deleted successfully...');
define('CARE_ADDED', 'Care added successfully...');
define('CARE_UPDATED', 'Care updated successfully...');
define('CARE_DELETED', 'Care deleted successfully...');
define('CARE_COMPLETED', 'Care completed successfully...');
define('MC_ADDED', 'Menstrual Cycle added successfully...');
define('MC_UPDATED', 'Menstrual Cycle updated successfully...');
define('MC_DELETED', 'Menstrual Cycle deleted successfully...');
define('INS_ADDED', 'Insurance added successfully...');
define('INS_DELETED', 'Insurance deleted successfully...');
define('NO_STORY', 'No Story Found');
define('FOSTER_MSG', 'Thanks for registering with us, we will contact you soon..');
define('VACCINATION_ADDED', 'Vaccination added successfully...');
define('VACCINATION_UPDATED', 'Vaccination updated successfully...');
define('VACCINATION_DELETED', 'Vaccination deleted successfully...');
define('VACCINATION_IMG_ADDED', 'Vaccination image Added successfully...');
define('WORM_ADDED', 'Worm added successfully...');
define('WORM_UPDATED', 'Worm updated successfully...');
define('WORM_DELETED', 'Worm deleted successfully...');
define('PRODUCT_SEND', 'Received');
define('WEIGHT_REQUIRED', 'Weight required');
define('WEIGHT_UPDATE', 'Weight update');
define('WEIGHT_INSERT', 'Weight inserted');
define('WEIGHT_SEND', 'All update weight send');
define('WEIGHT_NOT_SEND', 'Weight not updated');
define('MANUAL_UPDATED', 'Manualactivity update');
define('MANUAL_INSERTED', 'Manualactivity insert');
define('ACTIVITY_SEND', 'All activity send');
define('UPDATE_FOOD','Update recommend food');
define('INSERT_FOOD', 'Insert recommend food ');
define('UPDATE_BODY_SCORE','Update current body score ');
define('IS_FOLLOW_TRUE','1');
define('IS_FOLLOW_FALSE','0');
define('MSG_SEND', "Message sent successfully");
define('CHAT_HST', "Get chat history.");
define('CON_LIST', "Get all country.");
define('GET_CHAT', "Get my conversation.");

define('NO_CHAT', "Not yet any conversation.");
define('USR_TBL', 'users');
define('FOLLOW_TBL', 'followers');

define('CHT_TBL', 'chat');
define('MYFOLLWER','My pet followers.');
define('MYPETVIEWER','My pet viewers.');
define('EDITCOMMENT','Comment successfully updated.');
define('DELCOMMENT','Comment deleted successfully.');
define('CURRENT_BODY_SCORE', 'Insert current body score ');
define('ACTIVITY_TRACKER', 'Activity tracker data added successfully');
define('ACTIVITY_TRACKER_LOADED', 'Tracker data loaded successfully');
 /*Added By Varun_Andro*/
define('OTP_MSG', 'OTP send successfully');
define('NOT_RESPONDING', 'Please try after some time');
define('NO_FOLLOWER', 'No follower.');
define('NO_VIEWER', 'No viewer.');
define('UPDATE_PROFILE', 'Profile updated successfully');
define('SIGNUP_PROFILE','Signup successfully');
define('ADD_PET', 'New pet added successfully');
define('ALL_PRODUCTS', 'Get All Products.');
define('CART_UPDATE', 'Cart updated successfully.');
define('ALL_PET', "Get All Pets");
define('REMOVE_CART', "Remove product from Cart.");
define('ALL_MY_PET', "Get All Pets Successfully");
define('ALL_PET_MARCKET', "Get All Pets Marcket");
define('UPDATE_PUBLIC_PRIVATE_1', "Your pet is now public successfully");
define('UPDATE_PUBLIC_PRIVATE_2', "Your pet is now private successfully");
define('UPDATE_SALE_1', "Selling pet is enabled.");
define('UPDATE_SALE_2', "Selling pet is disabled.");
define('ALL_NEAR_BY', "Near by user find successfully");
define('ALL_FIELD_MANDATORY', "Please fill all fields");
define('PET_NOT_AVAILABLE', "Pets not available");
define('FIND_BREED', "Get all Breed successfully");
define('UPDATE_PET', "Pet update successfully");
define('ADD_PET_MEMORIES', "Memories add successfully");
define('GET_PET_MEMORIES', "Memories fetch successfully");
define('GET_ORDERS', "Orders found");
define('GET_ORDERS_FAIL', "Orders not found");
define('PORDER', "Your order is Pending; Your order Id is ");
define('DORDER', "Your order is Delivered; Your order Id is ");
define('PCORDER', "Your order is Packed; Your order Id is ");
define('CORDER', "Your order is Confirmed; Your order Id is ");
define('DISORDER', "Your order is Dispatched; Your order Id is ");
define('ACTIVATE', "Your account is activated by admin ");
define('INACTIVATE', "Your account is deactivated by admin ");
define('NOTFOLLOW', "You can not follow your profile. ");
define('FOLLOW', "Follow");
define('DELIVERED', "Your order has been successfully Delivered. Your order id is ");
define('CANCEL', "Your order has been cancelled. Your order id is ");
define('REQUEST', "Get All Request");
define('NOREQUEST', "Request not available, please add your request");
define('NOTACTIVATE', "Your account is pending for verification");
define('ORDER', "Order");
define('WELCOME_MSG', "Welcome to PetInfo");
define('WELCOME', "Welcome to PetInfo!<br><br> Warm Greetings! If you have any queries or suggestions, Feel free to write us at peteatsind@gmail.com or drop a text on +91 94259 77777.");
define('OTP_FAIL', "OTP not send, please try after some time.");
define('PROFILE_UPDATE_FAILED', "Profile not updated, please try again later.");
define('SEND_USER_ID', "please send user id");
define('NO_PET_FOUND', "No pet found, Please add your pet.");
define('DONE', "Done");
define('NOT_DONE', "Not Done");
define('INVALID_REQUEST', "Invalid Request");
define('SUCCESS', "Success");
define('REMINDER_DELETED', "Reminder deleted.");
define('NO_TASK', "No Task Found");
define('FAILED', "Failed");
define('REMOVEUSER', "User Removed Successfully.");
define('MEMORY_NOT_ADDED', "Memory not added");
define('REQUEST_SUCCESS', "Your request sent successfully");
define('MEMORY_NOT_AVAILABLE', "Memories not available please add your memories");
define('NO_MEMORIES', "No memories found");
define('GET_CART', "Get my Cart.");
define('CART_EMPTY', "Cart is empty");
define('MAKE_ORDER', "Make Order");
define('NO_PRODUCT', "No products");
define('PENDING', "Pending");
define('CONFIRM', "Confirm");
define('DELIVEREDMSG', "Delivered");
define('PACKEDMSG', "Packed");
define('DISMSG', "Dispatched");
define('CANCELLEDMSG', "Cancelled");
define('OTHER', "Other");
define('RECORD_FILTER', "Filter Record");
define('NO_RECORD', "No Record Found");
define('GET_POST', "Get Post");
define('LIKE_POST', "Like Post");

define('DISLIKE_POST', "Dislike Post");
define('POST_NOT_FOUND', "Post Not Found");
define('ALL_COMMENT', "All Comments");
define('ADD_COMMENT', "Add Comments");
define('TRY_AGAIN', "Try Again");
define('REPORT_POST', "Already Reported");
define('MARK_ABUSE', "Mark Abuse");
define('DELETE_POST', "Delete Post");
define('DELETE_MSG', "Delete your post");
define('DELETE_POST_TITLE', "PETIME");
define('ADD_POST', "Add Post");
define('EVENTDETETE', "Event Deleted Successfully.");
define('EVENTADD', "Event Added Successfully.");
define('EVENTEDIT', "Event Updated Successfully.");
define('OWNEVENT', "You Can Not Join On Your Event.");
define('JOINNEVENT', "Join Event Successfully.");
define('AlREADYJOINNEVENT', "You Have Already Joined This Event.");
define('POST_UPDATE', "Post Updated Successfully.");
define('PET_DELETED', "Your pet deleted successfully.");
define('PET_ALREADY_DELETED', "Already Deleted");
define('VIEW', "View");
define('UPDATE_RATING', "Rating Updated");
define('ALREADY_RATED', "Already Rated");
define('REVIEW_ADDED', "Review Added Successfully");
define('ALREADY_REVIEWED', "Already Reviewed");
define('JOIN_COMMUNITY', "Community Join successfully");
define('GET_ALL_RATING', "Get all rating successfully");
define('GET_ALL_CAT', "Get all Category successfully");
define('MEMORIES_UPLOADED', "Memories Uploaded Successfully");
define('ADOPT', "You have already contact with owner of this pet");
define('MEMORIES_NOT_UPLOADED', "Memories Not Uploaded");
define('INVITATION_SEND', "Invitation Send Successfully.");
define('INSFOLLOW', "Follow Successfully.");
define('UNFOLLOW', "Unfollow Successfully.");
define('CRYSET_TBL',"currency_setting");
define('CONTACT_MSG', " wants to contact you regarding your pet. Please reply As soon as Possible.");
define('CONTACTTITLE', "Contact");
define('FOLLOW_MSG', " is following your pet.");
define('VIEW_MSG', " is view your pet profile.");
define('PAYMENT_SUCCESS', "Your Payment is Successful And your order id is .");
define('GET_CONTACT_REQUEST', "Contact Details fetch Successfully.");
define('GETALLEVENT', "Get all event successfully.");
define('GETALLJOINEVENT', "Get all joined event successfully.");
define('GETALLJOINEVENTUSER', "Get all joined event user successfully.");
define('CREATEDEVENT', "created a evnt please join us.");
define('GET_NOTIFICATION_REQUEST', "Notification Details fetch Successfully.");
define('LOGIN_FLASH',"Please enter valid Username or Password");
define('MYPETMSG', 'You can not rate your self ');
define('server_key', 'SB-Mid-server-puqKJQ15XcV9wfxWcbXlhjeu');
define('ENABLE_ADOPT', "Adopt enable");
define('DISABLE_ADOPT', "Adopt disable");
define('VERIFIED', " is verified by admin.");
define('NOT_VERIFIED', " is unverified");
define('PETPRO', "Pet profile");
define('POST', "Post");
define('COMMENT', "Comment");
define('LIKE', "Like");
define('LIKE_MSG', " liked your post.");
define('COMMENT_MSG', " commented on your post.");
define('POST_MSG', " is add new post.");
define('GETALLCOMMENT', "Get all comments.");
define('NOCOMMENT', "No comments available.");
define('EDIT_COMM', "Comment edited successfully.");
define('NOTOFICATION_TBL', "notifications");
define('ADVVERIFIED', "adv is activated by admin.");
define('ADVUNVERIFIED', "adv is Deactivated by admin");
define('GETPETMARKET', "Get all pet market adv.");
define('NOPETMARKET',"No pet market adv available.");
define('ALREADY_LIKE',"Post already liked.");
define('FIRE_BASE_KEY', "AAAAHhqQxOQ:APA91bHDJHoRCh7r0GhcBJVlsONikqT8ytyjZIE2vBBhYE7ADrILFwP1U7MZFAd5AFaYEpXCQ0JGDMV9xwjL6XhEXqJu5JBtp9Rl3bk90MoSAVQBlwT5xeRAcVr87qfx-QAZ048GnNgk");
define('SHOPIFY_URL', "https://20a41e4738179a5731c1b5e02f03ae50:27dcb02357cbaeefd340dc416eebd2a2@babu62.myshopify.com");

define('REG_SUB',"PetInfo Registration");
define('PWD_SUB',"PetInfo Forgot Password");
define('GET_ALL_BRAND'," Get all data");
define('SENDER_EMAIL','info@samyotech.com');
define('MSG_AUTH_KEY',"205521Ay0uGpRMiR5da996d7");

