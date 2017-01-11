angular.module('app', ['ngRoute', 'controllers'])

    .config(function($routeProvider, $httpProvider){
        $routeProvider
            .when('/',{
                templateUrl: 'home.html',
                controller: 'homeCtl',
                title:"Home-HMJ using Spring boot with AngularJS"
            })
            .when('/test',{
                templateUrl: 'test.html',
                controller: 'testCtl',
                title:"Home-HMJ frontend test using Jasmine and karma"
            })
            .when('/login',{
                templateUrl: 'login.html',
                controller: 'navigationCtl',
                title:"Please Login"
            })
            .when('/userlogin',{
                templateUrl: 'login.html',
                controller: 'userLoginCtl',
                title:"Users Login"
            })
            .when('/users',{
                templateUrl: 'index.html',
                controller: 'usersCtl',
                title:"HMJ users"
            })
            .when('/adduser',{
                templateUrl: 'index.html',
                controller: 'adduserCtl',
                title:"Add User"
            })
            .when('/getUserByEmail:email',
                {
                templateUrl: 'index.html',
                controller: 'getByEmailCtl',
                title:"Find a user using email address"
                })
            .when('/analys',
                {
                templateUrl: 'index.html',
                controller: 'choiceCtl',
                title:"Analyspaket"
                })
            .when('/sogardettill',
                {
                    title:"So går det till",
                    templateUrl: 'sogardettill.html',
                    controller: 'faqCtl'
                })
            .when('/addfaq',
                {
                    templateUrl: 'addfaq.html',
                    controller: 'faqCtl',
                    title:"Add Faq"
                })

            .when('/403',
                {
                templateUrl: 'index.html',
                controller: 'choiceCtl',
                title:"Access denied",
                redirectTo:"403.html"
                })

            .otherwise(
                { redirectTo: '/'}
            );
        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    })
// change Page Title based on the routers
   .run(['$location', '$rootScope', function($location, $rootScope)
   {
       $rootScope.baseUrl='http://localhost:8081';
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous)
    {
        if (current.hasOwnProperty('$$route')) {

            $rootScope.title = current.$$route.title;
        }

    });
    }]

   );



