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
	"student_id"=>18354001,
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
	$loginCredentials = json_decode(request()->input("loginCredentials"),true);
	//return $loginCredentials["personType"];
	$query = json_decode(json_encode(DB::table($loginCredentials["personType"])->where(["id"=>$loginCredentials["id"],
														"password"=>$loginCredentials["password"]
	])->get()->first()),true);

	
	if($query){
		$query["is_success"] = "1";
		if($loginCredentials["personType"] == "student"){
			DB::table($loginCredentials["personType"])->where(["id"=>$loginCredentials["id"],
														"password"=>$loginCredentials["password"]
														])->limit(1)->update(["mobile_activated"=>"true"]);
		}
		
	}else{
		return response()->json(array("is_success"=>0,"msg"=>"invalid username or password"));
	
	}
	
	return response()->json($query);

	$user = array("first_name"=>"Muhammed Cihad"
	,"last_name"=>"ARSLANOĞLU",
	"mobile_activated"=>1,
	"id"=>18354001,
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
	/**
	 * This function returns the all lessons teacher or student has. 
	 * 
	 * return [
	 * {"lesson_id":"EESB064","lesson_name":"ELEKTR\u0130K DA\u011eITIM S\u0130STEMLER\u0130"},
	 * {"lesson_id":"EESB060","lesson_name":"ENERJ\u0130 VER\u0130ML\u0130L\u0130\u011e\u0130"},
	 * {"lesson_id":"EESB003","lesson_name":"YAPAY S\u0130N\u0130R A\u011eLARI"},
	 * {"lesson_id":"EEZ401","lesson_name":"B\u0130T\u0130RME PROJES\u0130"},
	 * {"lesson_id":"EESB063","lesson_name":"MOB\u0130L \u0130LET\u0130\u015e\u0130M TEKNOLOJ\u0130LER\u0130"}
	 * ]
	 * 
	 * return is also equal to array(array("lesson_id"=>"EEZ401","lesson_name"=>"BİTİRME PROJESİ"), array("lesson_id"=>"EESB003","lesson_name"=>"YAPAY SİNİR AĞLARI"))
	 */


	$user = json_decode(request()->input("userCredentials"),true);
	$personType = $user["personType"];
	$userID = $user["id"];
	$lessonIDs = DB::table($personType."_has_lesson")->where("id",$userID)->pluck("lesson_id");
	$lessons = DB::table("lesson")->whereIn("lesson_id",$lessonIDs)->get();
	
	return response()->json($lessons,200);
});

Route::post("attendance/submit",function(){
	return response()->json(request()->input("attendanceInformations"));

});

Route::get("attendance/submit",function(){
	return response()->json(request()->input("attendanceInformations"));
});


?>
