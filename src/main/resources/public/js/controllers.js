
var controllers = angular.module('controllers',[]);
controllers.constant("SERVER_API","http://localhost:8081");

controllers.controller('usersCtl', function($scope,$http,SERVER_API) {
    /**
     * The response object has these properties:

     data – {string|Object} – The response body transformed with the transform functions.
     status – {number} – HTTP status code of the response.
     headers – {function([headerName])} – Header getter function.
     config – {Object} – The configuration object that was used to generate the request.
     statusText – {string} – HTTP status text of the response.

    *
    */
    $http.get(SERVER_API+'/users/').then(function successCallback(response)
    {
        $scope.greeting = response.data
    }), function errorCallback(response) {
        console.log(response.data)
    }
});
controllers.controller('testCtl', function($http,SERVER_API) {
    var self = this;
    $http.get(SERVER_API+'resource/').then(function(response) {
        console.log(response.data.content);
        self.greeting = response.data;
    });
});
controllers.controller('adduserCtl', function($scope,$http,SERVER_API) {
    $http.get(SERVER_API+'/createUser',{'email':$scope.email,'name':$scope.name,'password':$scope.password})
        .then(function successCallback(response)
    {
        $scope.greeting = response.data
    }), function errorCallback(response) {
        console.log(response.status);
    }
    //$scope.greeting = {id: 'xxx', content: 'Hello World!'}
});
controllers.controller('getByEmailCtl', function($scope,$http,SERVER_API)
{
    var inputData = {'email':$scope.email};
    $http.get(SERVER_API+'/getByEmail',inputData).then(function successCallback(response)
    {
        $scope.username = response.data.username;
        $scope.name = response.data.name;
        $scope.email = response.data.email;
        console.log("username from controller " + response.data.username);
    }), function errorCallback(response) {
        console.log(response.status);
    }

    //$scope.greeting = {id: 'xxx', content: 'Hello World!'}
});

controllers.controller('userLoginCtl', function($scope,$http,$routeParams,$location,SERVER_API)
{
   /* //var inputData = {'username':$scope.userdetails.username,'password':$scope.userdetails.password};
    var inputData = userdetails ? {authorization : "Basic "
    + btoa(userdetails.username + ":" + userdetails.password)
    } : {};
    $http.get('/userlogin',inputData).success(function(data)
    {
        $scope.username = data.username;
        $scope.password = data.password;
        console.log(data);
    })*/
    //$scope.greeting = {id: 'xxx', content: 'Hello World!'}
});
controllers.controller('homeCtl', function($scope, $http) {

       /* $http.get('/getByEmail').success(function(data)
        {
            console.log("hiddenText Value:" + $scope.hiddentext);
            $scope.username = data.username;
            $scope.name = data.name;
            $scope.email = data.email;
        })*/
    });
controllers.controller('navigationCtl',
        function($rootScope, $scope, $http, $location,SERVER_API)
        {
            console.log("We are inside navigation controller after login() click");

            /*if(!$scope.rememberMe) {
                $scope.rememberMe = false;
            }*/
            var authenticate = function(credentials, callback) {

                var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password)
                } : {};
                 //send the credentials to the backend server
                $http.get(SERVER_API+'/user', {headers : headers})
                    .then(function successCallback(response)
                    {
                        if (response.data.name) {
                            $rootScope.authenticated = true;
                        } else {
                            $rootScope.authenticated = false;
                            $scope.error=true;
                        }
                        callback && callback();
                    }, function errorCallback(response) {
                    $rootScope.authenticated = false;
                    $scope.error = true;
                    callback && callback();
                })
                    ;

            }
            authenticate(); // alias of the about function
            $scope.credentials = {};
            $scope.login = function()
            {
                authenticate($scope.credentials, function()
                {
                    if ($rootScope.authenticated) {
                        $location.path("/");
                        $scope.error = false;
                    }
                    else
                    {
                        $rootScope.authenticated = false;
                        $location.path("/login");
                        $scope.error = true;
                    }

                })

            };
            $scope.logout = function() {
                $http.post(SERVER_API+'logout',{})
                    .then(function successCallback(response)
                    {
                    $rootScope.authenticated = false;
                    $location.path("/");
                    }, function errorCallback(response)
                    {
                        $rootScope.authenticated = false;
                    })
                    ;
            }

        });

controllers.controller('choiceCtl', function($scope,$http,SERVER_API)
    {
        var self = this;
        $http.get(SERVER_API+'choice/').then(function(response) {
            console.log(response.data.content);
            self.greeting = response.data;
            //$scope.greeting = {id: 12345, content: 'Hello Choice!'}
        });

    });

controllers.controller('faqCtl', function($scope,$http,SERVER_API)
    {
        $http.get(SERVER_API+'/faqs').success(function(data)
        {
            $scope.faqs = data;
        });

        /*Post function parameters are: url,data,config*/
        $scope.addfaq = function () {
            /*
            * As the default header Content-Type set by Angular is json so we need to change to "x-www-form-urlencoded"
            * that is being done using config object in which we have defined headers.
            * */
            var config = {
                headers : {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                }
            };
            var data = $.param({
                question: $scope.question,
                category: $scope.category,
                answer: $scope.answer
            });

            $http.post(SERVER_API+"/addFaq",data,config)
                .success(function (data, status, headers, config) {
                  $scope.faqs =data;
                  //$scope.redirectTo= 'sogardettill.html';*/
                })
                .error(function (data, status, header, config) {
                    $scope.error=true;
                    $scope.postDataResponse=true;
                    $scope.postDataResponse="Data:" +data +
                        "<hr />status: " + status +
                        "<hr />headers: " + header +
                        "<hr />config: " + config;
                });
            
        };
    //$scope.greeting = {id: 'xxx', content: 'Hello World!'}
    });


