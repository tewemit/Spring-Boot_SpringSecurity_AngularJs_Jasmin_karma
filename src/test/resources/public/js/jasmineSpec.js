/**
 * Created by tewe on 1/3/2017.
 */
describe("app", function() {

// load the controller's module using angular.mock.module
    beforeEach(function() {
       // module('app'); // <= initialize module that should be tested
        angular.mock.module('controllers'); // same as angular.mock.mudule('controllers')
    });
    // lets mock the backend service using the httpBackend service below
    var scope,httpBackend, createController;
    var SERVER_API ="http://localhost:8081";
    beforeEach(inject(function($injector) {
        httpBackend = $injector.get('$httpBackend');
        scope =  $injector.get('$rootScope').$new();
        createController = $injector.get('$controller');

            //function() {
           // return $controller('testCtrl', {
             //   '$scope': scope
           // });
        //};

    }));


    afterEach(function() {
        httpBackend.verifyNoOutstandingExpectation();
        httpBackend.verifyNoOutstandingRequest();
    });

    it("Should say Hello HMJ 'resource/' is called", function()
    {
        httpBackend.expectGET(SERVER_API+'resource/').respond(200,
            {
            id : 4321,
            content : 'Hello HMJ'
            });
        var controller = createController('testCtl');
        httpBackend.flush();
        expect(controller.greeting.content).toEqual('Hello HMJ');

    });
    it("shoud say Hello choice when choiceCtl loads", function()
    {
        httpBackend.expectGET(SERVER_API+'choice/').respond(200,
            {
                id : 12345,
                content : 'Hello Choice!'
            });
        var controller = createController('choiceCtl',{$scope: scope});
        httpBackend.flush();
        expect(controller.greeting.content).toEqual('Hello Choice!');



    });

    it("says Hello student when usersCtl loads", function()
    {
        httpBackend.expectGET(SERVER_API+'/users/').respond(200,
            {
                id : 12345,
                content : 'Hello student!'
            });
        var controller2 = createController('usersCtl',{$scope: scope});

        httpBackend.flush(); // respond to any pending requests
        console.log(scope.greeting.content);
        expect(scope.greeting.content).toEqual('Hello student!');

    });
    it("get user by email when usersCtl loads", function()
    {
        var inputData = {'email':'test@test.com'};
        var config = {
            headers : {
                'Content-Type': 'application/json;'

            }
        };
        httpBackend.expectGET(SERVER_API+'/getByEmail').
        respond(200,
            {id:1,name:"test",email:"test@test.com",passowrd:"user",username:"user"}


        );

        var controller3 = createController('getByEmailCtl',{$scope: scope});
        httpBackend.flush();
        console.log(scope);
        expect(scope.username).toEqual('user');



    });

    it("should save Faq when faqCtl loads", function()
    {
        var inputData = $.param({
        question: "Vad heter du?",
        category: "cat5",
        answer: "tittt"
    });
        var config = {
            headers : {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'

            }
        };
       // httpBackend.expectPOST(SERVER_API+'/addFaq',inputData,config).
        httpBackend.expectGET(SERVER_API+'/faqs').
        respond(200,
            {id:26,category:"cat5",question:"Vad heter du?",answer:"tittt"}

        );

        var FaqCtl = createController('faqCtl',{$scope: scope,SERVER_API:SERVER_API});
       // FaqCtl.addfaq();
        httpBackend.flush();
        console.log(scope);
        expect(scope.faqs.category).toEqual('cat5'); // the addFaq function of the controller is invoked inside and the result is assumed to have come from it.



    });

})