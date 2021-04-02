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
define('USER_CONFIRMATION', 'Please confirm your email, You must be receive an email.');
define('PASSWORD_UPDATED', 'password updated');
define('PASSWORD_NOTMATCHED', 'your old password did not match');
define('EMAIL_EXISTS', 'This email already exists');
define('USER_REGISTERED', 'Registration successful');
define('EMAIL_NOT_EXISTS', 'Email not found');
define('BREED_NOT_FOUND', 'No Breed Found');
define('BREED_FOUND', 'Breed Found');
define('PET_ADDED', 'Pet added successfully');
define('PET_UPDATED', 'Pet updated successfully');
define('PET_ID_INVALID', 'petid is not valid.');
define('APPOINTMENT_ADDED', 'Apointment added Successfully...');
define('APPOINTMENT_UPDATED', 'Apointment updated Successfully...');
define('APPOINTMENT_DELETED', 'Apointment deleted Successfully...');
define('PET_SHARED', 'Pet Shared Successfully...');
define('PET_ALREADY_SHARED', 'Pet already shared.');
define('PET_NOT_SHARED', 'Shared email not registered.');
define('IMAGE_ADDED', 'Image Added Successfully in gallery...');
define('GALLERY_LIST', 'Gallery List');
define('GALLERY_UPDATED', 'Gallery updated Successfully...');
define('GALLERY_DELETED', 'Gallery deleted Successfully...');
define('CARE_ADDED', 'Care added Successfully...');
define('CARE_UPDATED', 'Care updated Successfully...');
define('CARE_DELETED', 'Care deleted Successfully...');
define('CARE_COMPLETED', 'Care completed Successfully...');
define('MC_ADDED', 'Menstrual Cycle added Successfully...');
define('MC_UPDATED', 'Menstrual Cycle updated Successfully...');
define('MC_DELETED', 'Menstrual Cycle deleted Successfully...');
define('INS_ADDED', 'Insurance added Successfully...');
define('INS_DELETED', 'Insurance deleted Successfully...');
define('NO_STORY', 'No Story Found');
define('FOSTER_MSG', 'Thanks for registering with us, we will contact you soon..');
define('VACCINATION_ADDED', 'Vaccination added Successfully...');
define('VACCINATION_UPDATED', 'Vaccination updated Successfully...');
define('VACCINATION_DELETED', 'Vaccination deleted Successfully...');
define('VACCINATION_IMG_ADDED', 'Vaccination image Added Successfully...');
define('WORM_ADDED', 'Worm added Successfully...');
define('WORM_UPDATED', 'Worm updated Successfully...');
define('WORM_DELETED', 'Worm deleted Successfully...');
define('PRODUCT_SEND', 'Received');
define('WEIGHT_REQUIRED', 'Weight required');
define('WEIGHT_UPDATE', 'Weight update');
define('WEIGHT_INSERT', 'Weight inserted');
define('WEIGHT_SEND', 'All update weight send');
define('WEIGHT_NOT_SEND', 'Weight not updated');
define('MANUAL_UPDATED', 'Manualactivity update');
define('MANUAL_INSERTED', 'Manualactivity insert');
define('ACTIVITY_SEND', 'All Activity send');
define('UPDATE_FOOD','Update recommend food');
define('INSERT_FOOD', 'Insert recommend food ');
define('UPDATE_BODY_SCORE','Update current body score ');
define('CURRENT_BODY_SCORE', 'Insert current body score ');
define('ACTIVITY_TRACKER', 'Activity tracker data added successfully');
define('ACTIVITY_TRACKER_LOADED', 'Tracker data loaded successfully');
 /*Added By Varun_Andro*/
define('OTP_MSG', 'OTP send Successfully');
define('NOT_RESPONDING', 'Please try after some time');
define('UPDATE_PROFILE', 'Profile update successfully');
define('ADD_PET', 'Add new pet successfully');
define('ALL_PRODUCTS', 'Get All Products.');
define('CART_UPDATE', 'Cart updated successfully.');
define('ALL_PET', "Get All Pets");
define('REMOVE_CART', "Remove Product from Cart.");
define('ALL_MY_PET', "Get All Pets Successfully");
define('UPDATE_PUBLIC_PRIVATE_1', "Your pet is now public successfully");
define('UPDATE_PUBLIC_PRIVATE_2', "Your pet is now private successfully");
define('UPDATE_SALE_1', "Selling pet is enabled.");
define('UPDATE_SALE_2', "Selling pet is disabled.");
define('ALL_NEAR_BY', "Near by user find successfully");
define('PET_NOT_AVAILABLE', "Pets not available");
define('FIND_BREED', "Get all Breed successfully");
define('UPDATE_PET', "Pet update successfully");
define('ADD_PET_MEMORIES', "Memories add successfully");
define('GET_PET_MEMORIES', "Memories featch successfully");
define('GET_ORDERS', "Orders found");
define('GET_ORDERS_FAIL', "Orders not found");
