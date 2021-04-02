<?php
defined('BASEPATH') OR exit('No direct script access allowed');

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
defined('SHOW_DEBUG_BACKTRACE') OR define('SHOW_DEBUG_BACKTRACE', TRUE);

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
defined('FILE_READ_MODE')  OR define('FILE_READ_MODE', 0644);
defined('FILE_WRITE_MODE') OR define('FILE_WRITE_MODE', 0666);
defined('DIR_READ_MODE')   OR define('DIR_READ_MODE', 0755);
defined('DIR_WRITE_MODE')  OR define('DIR_WRITE_MODE', 0755);

/*
|--------------------------------------------------------------------------
| File Stream Modes
|--------------------------------------------------------------------------
|
| These modes are used when working with fopen()/popen()
|
*/
defined('FOPEN_READ')                           OR define('FOPEN_READ', 'rb');
defined('FOPEN_READ_WRITE')                     OR define('FOPEN_READ_WRITE', 'r+b');
defined('FOPEN_WRITE_CREATE_DESTRUCTIVE')       OR define('FOPEN_WRITE_CREATE_DESTRUCTIVE', 'wb'); // truncates existing file data, use with care
defined('FOPEN_READ_WRITE_CREATE_DESTRUCTIVE')  OR define('FOPEN_READ_WRITE_CREATE_DESTRUCTIVE', 'w+b'); // truncates existing file data, use with care
defined('FOPEN_WRITE_CREATE')                   OR define('FOPEN_WRITE_CREATE', 'ab');
defined('FOPEN_READ_WRITE_CREATE')              OR define('FOPEN_READ_WRITE_CREATE', 'a+b');
defined('FOPEN_WRITE_CREATE_STRICT')            OR define('FOPEN_WRITE_CREATE_STRICT', 'xb');
defined('FOPEN_READ_WRITE_CREATE_STRICT')       OR define('FOPEN_READ_WRITE_CREATE_STRICT', 'x+b');

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
defined('EXIT_SUCCESS')        OR define('EXIT_SUCCESS', 0); // no errors
defined('EXIT_ERROR')          OR define('EXIT_ERROR', 1); // generic error
defined('EXIT_CONFIG')         OR define('EXIT_CONFIG', 3); // configuration error
defined('EXIT_UNKNOWN_FILE')   OR define('EXIT_UNKNOWN_FILE', 4); // file not found
defined('EXIT_UNKNOWN_CLASS')  OR define('EXIT_UNKNOWN_CLASS', 5); // unknown class
defined('EXIT_UNKNOWN_METHOD') OR define('EXIT_UNKNOWN_METHOD', 6); // unknown class member
defined('EXIT_USER_INPUT')     OR define('EXIT_USER_INPUT', 7); // invalid user input
defined('EXIT_DATABASE')       OR define('EXIT_DATABASE', 8); // database error
defined('EXIT__AUTO_MIN')      OR define('EXIT__AUTO_MIN', 9); // lowest automatically-assigned error code
defined('EXIT__AUTO_MAX')      OR define('EXIT__AUTO_MAX', 125); // highest automatically-assigned error code

define('USERALREADY', "User already registered");
define('USERRAGISTER', "User has been registered successfully. Please check you email.");
define('LOGINSUCCESSFULL',"User login successfully");
define('LOGINFAIL',"Login fail please check your email id and password");
define('FOUND',"Password is updated");
define('NOTFOUND',"Email not found");
define('USER_NOT_FOUND',"User not found");
define('NOTUPDATE',"Password not update");
define('AVAILABLE',"User available");
define('NOTAVAILABLE',"User not find");
define('EDITSUCCESSFULL',"Profile has been updated sucessfully");

define('EDITFAIL',"Profile has been not updated");

define('USERNOTFOND',"Profile not fount you have now registered as artist");

define('NOT_ACTIVE',"User not active.");

define('PASS_NT_MTCH',"Invaild Password.");
/*Get All Category*/
define('ALL_CAT',"Get all Categories");

/*No data*/
define('NO_DATA',"No data found.");

/*Get All Category*/
define('ALL_SKILLS',"Get all Skills");

/*Get All Category*/
define('ALL_ARTISTS',"Get all Artists");

/*Artist Upadte*/
define('ARTIST_UPDATE',"Artist updates successfully.");

/*Something went to wrong. Please try again later.*/
define('TRY_AGAIN',"Something went to wrong. Please try again later.");

/*Product Added successfully.*/
define('PRODUCT_ADD',"Product Added successfully.");

/*Qualification Added successfully.*/
define('QUALIFICATION_ADD',"Qualification Added successfully.");

/*Comment Added successfully.*/
define('ADD_COMMENT',"Comment Added successfully.");

/*Please Check you Email*/
define('CHECK_MAIL',"Please Check you Email.");

/*Gallery image added successfully.*/
define('ADD_GALLERY',"Gallery image added successfully.");

/*Appointment booked successfully.*/
define('BOOK_APP',"Appointment booked successfully.");

/*Get all Appointments*/
define('GET_APP',"Get all Appointments");

/*CURRENCY TYPE*/
define('CURRENCY_TYPE',"USD");

/*Booking end successfully*/
define('BOOKING_END',"Booking end successfully.");

/*"Get my current booking."*/
define('CURRENT_BOOKING',"Get my current booking.");

/*Get my invoice.*/
define('MY_INVOICE',"Get my invoice.");

/*Payment Confirm successfully*/
define('PAYMENT_CONFIRM',"Payment Confirm successfully.");