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
	/**
	 * returns user credentials if it is existed. 
	 * {"first_name":"Muhammed Cihad","last_name": "ARSLANOĞLU","mobile_activated":true/false,"student_id":"18354012","is_success":"1"}
	 * 
	 * if there is no user with the provided username and password, returns
	 * {"is_success":"0","msg":"invalid username or password"}
	 */
	 


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
	     "mobile_activated":true/false,
	     "student_id":"18354012"

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
	$attendanceInformations = json_decode((request()->input("attendanceInformations")),true); 
	
	$attendanceInformations["attendanceList"] = array_unique(explode(",",$attendanceInformations["attendanceList"]));
	

	$allStudents = DB::table("student_has_lesson")->where(["lesson_id"=>$attendanceInformations["lesson_id"]])->get();
	$notCameStudents = array_diff($allStudents->pluck("id")->toArray(),$attendanceInformations["attendanceList"]);
	//DB::table("attendance")->insert([$notCameStudents=>"TRUE"]);

	for($count=0;$count<(int)$attendanceInformations["lessonCount"];$count++){
		foreach($notCameStudents as $id){
			DB::table("attendance")->insert([
				"student_id" =>$id, 
				"present"=>"FALSE",
				"lesson_id"=>$attendanceInformations["lesson_id"]
				]);
		}
		

	}
	
	//return var_dump(json_encode($attendanceInformations));
	return response()->json(array("is_success"=>"1","msg"=>"the all attendances were inserted into database"));
	//return response()->json(request()->input("attendanceInformations"));
});


?>
