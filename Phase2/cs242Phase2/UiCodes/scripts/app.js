// Define a new module for our app

var app = angular.module("instantSearch", ['ui.bootstrap']);

// Create the instant search filter

app.filter('searchFor', function(){

	// All filters must return a function. The first parameter
	// is the data that is to be filtered, and the second is an
	// argument that may be passed with a colon (searchFor:searchString)

	return function(arr, searchString){

		if(!searchString){
			return arr;
		}

		var result = [];

		searchString = searchString.toLowerCase();

		// Using the forEach helper method to loop through the array
		angular.forEach(arr, function(item){

			if(item.title.toLowerCase().indexOf(searchString) !== -1){
				result.push(item);
			}

		});

		return result;
	};

});




// The controller

app.controller('InstantSearchController', function($scope,$http){
    
	// The data model. These items would normally be requested via AJAX,
	// but are hardcoded here for simplicity. See the next example for
	// tips on using AJAX.
    s="";
    $scope.LuceneCall = function(){
        httpGetRequest(s,$scope,$http);
    }
    $scope.HadoopCall = function(){
        httpGetRequest(s,$scope,$http);
    }
    $scope.getResults = function(){
        httpGetRequest(s,$scope,$http);
    }
    $scope.getResultsTag = function($event){
        console.log(($event.target).text);
        s=($event.target).text;
        httpGetRequest(s,$scope,$http);
        s="";
    }
	
});

function httpGetRequest(q,$scope,$http){
    $(".ir-search-results").addClass("hide");
    $(".ir-search-results .ir-snippet").addClass("hide");
    if(q==""){
        console.log("hello"+q+"hello");
        q = $(".content-cont .content .bar input").val().toLowerCase();
    }
    else{
        $(".content-cont .content .bar input").val(q);
    }
    s = ($('.index-select-cont .btn.active').data("val"));
    if( q != ""){
        if(s == 1){
//            $scope.snip = {
//                url: 'http://www.tutorialspoint.com/angularjs/',
//                    title: 'AngularJs Tutorials ',
//                    image: 'http://www.tutorialspoint.com/angularjs/images/angularjs-mini-logo.jpg'
//            }
//            $scope.items = [
//                    {
//                        "tags": [
//                            "building",
//                            "trine",
//                            "painting",
//                            "architecture",
//                            "abstract",
//                            "cornice",
//                            "windows",
//                            "cityscape"
//                        ],
//                        "medium": "Painting - Acrylic On Canvas",
//                        "url": "https://fineartamerica.com/featured/cornice-christopher-triner.html",
//                        "size": "12 x 18",
//                        "title": "Cornice",
//                        "price": "$24",
//                        "idname": "faacid1",
//                        "color": [
//                            " Yellow",
//                            " Yellow"
//                        ],
//                        "views": "7,628",
//                        "description": "None",
//                        "likes": "37",
//                        "imgsrc": "https://images.fineartamerica.com/images/artworkimages/mediumlarge/1/cornice-christopher-triner.jpg",
//                        "filename": "Corniceimage.jpg",
//                        "artistURL": "https://fineartamerica.com/profiles/christopher-triner.html",
//                        "artist": "Christopher Triner"
//                    },
//                    {
//                        "tags": [
//                            "monarch",
//                            "the",
//                            "glen",
//                            "1851",
//                            "oil",
//                            "stag",
//                            "deer",
//                            "antlers",
//                            "mountain",
//                            "crt",
//                            "dgt",
//                            "scotland",
//                            "scottish highlands",
//                            "animal",
//                            "wild",
//                            "animals",
//                            "beast",
//                            "landscape",
//                            "landscapes",
//                            "horned"
//                        ],
//                        "medium": "Painting - Oil On Canvas",
//                        "url": "https://fineartamerica.com/featured/monarch-of-the-glen-sir-edwin-landseer.html",
//                        "size": "12 x 18",
//                        "title": "Monarch Of The Glen",
//                        "price": "$17",
//                        "idname": "faacid0",
//                        "color": [
//                            " Green",
//                            " Red"
//                        ],
//                        "views": "23,634",
//                        "description": "Monarch of the Glen, 1851 (oil on canvas) by Landseer, Sir Edwin (1802-73)",
//                        "likes": "83",
//                        "imgsrc": "https://images.fineartamerica.com/images/artworkimages/mediumlarge/1/monarch-of-the-glen-sir-edwin-landseer.jpg",
//                        "filename": "MonarchOfTheGlenimage.jpg",
//                        "artistURL": "https://fineartamerica.com/profiles/the-masters.html",
//                        "artist": "Sir Edwin Landseer"
//                    }
//                ]
            
            $http({
                method : "GET",
                url : "https://09231420.ngrok.io/api/lucenesearch/"+q
            }).then(function mySuccess(response) {
                console.log(response.data);
                $scope.items = response.data;
                $(".ir-search-results").removeClass("hide");
                $(".ir-search-results .ir-snippet").removeClass("hide");
            }, function myError(response) {
                $scope.items = response.statusText;
            });
        }
        else{
            q = $(".content-cont .content .bar input").val().toLowerCase();
            $http({
                method : "GET",
                url : "https://09231420.ngrok.io/api/searching/"+q
            }).then(function mySuccess(response) {
                console.log(response.data);
                $scope.items = response.data;
                $(".ir-search-results").removeClass("hide");
                $(".ir-search-results .ir-snippet").removeClass("hide");
            }, function myError(response) {
                $scope.items = response.statusText;
            });
        }    
    }

}

app.controller('ButtonsCtrl', function ($scope) {
    $scope.radioModel = 'Lucene';
    
});


