<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});




Route::post('/login', function () {
	$user = array("first_name"=>"Muhammed Cihad"
	,"last_name"=>"ARSLANOĞLU",
	"mobile_activated"=>1,
	"student_id"=>18354012,
	"is_success"=>1);
	return response()->json($user,200);
	/*
	return response('{"first_name":"Muhammed Cihad",
	     "last_name": "ARSLANOĞLU",
	     "mobile_activated":1,
	     "student_id":18354012

	}',200)->header("Content-Type", "application/json");
	*/
});

Route::get('/login', function () {
	$user = array("first_name"=>"Muhammed Cihad"
	,"last_name"=>"ARSLANOĞLU",
	"mobile_activated"=>1,
	"student_id"=>18354012,
	"is_success"=>1);
	return response()->json($user,200);
	/*
	return response('{"first_name":"Muhammed Cihad",
	     "last_name": "ARSLANOĞLU",
	     "mobile_activated":1,
	     "student_id":18354012

	}',200)->header("Content-Type", "application/json");
	*/
	});

Route::get('/lessons/getLessons', function(){
	$lessons = array(array("lesson_id"=>"EEZ401","lesson_name"=>"BİTİRME PROJESİ"), array("lesson_id"=>"EESB003","lesson_name"=>"YAPAY SİNİR AĞLARI"));	
	return response()->json($lessons,200);
});

?>
